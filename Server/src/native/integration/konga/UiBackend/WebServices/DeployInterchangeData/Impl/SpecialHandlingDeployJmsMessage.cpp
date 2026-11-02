/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <map>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>
#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>

#include "SpecialHandlingDeployJmsMessage.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::vector;
using std::map;

typedef konga::interchange_deploy::SpecialHandlingDeployJmsMessage cls;

cls::SpecialHandlingDeployJmsMessage(GuidToIdMapper& guidMapper,
									 const UserInfo& userInfo,
									 InterchangeDataTableStruct& dataElementsTab,
									 DbConnection& dbConnection)
: m_iCurrentDataElementId(-1), m_guidMapper(guidMapper),
m_userInfo(userInfo), m_dbConnection(dbConnection)
{
	if ( dataElementsTab.m_vRows.empty() )
		return;

	vInterchangeDataRowStructT::const_iterator citRows = dataElementsTab.m_vRows.begin();
	for(; citRows != dataElementsTab.m_vRows.end(); ++ citRows ) {
		const InterchangeDataRowStruct& row = *citRows;
		int ownerEntityTypeId = atoi(row.m_vFields.at(0).str().c_str());
		if ( ownerEntityTypeId != JmsMessageEntityId )
			continue;	                                                    

		const string& jmsGuid = row.m_vFields.at(1).str();

		DE de;
		de.name = row.m_vFields.at(2).str();
		de.type = row.m_vFields.at(3).str();
		de.value = row.m_vFields.at(4)==0 ? "" : row.m_vFields.at(4).str();

		DEVectorMapT::iterator it = m_dataElements.find(jmsGuid);
		if ( it == m_dataElements.end() ) {
			DEVector v;
			v.push_back(de);
			m_dataElements.insert(DEVectorMapT::value_type(jmsGuid, v));
		}
		else {
			it->second.push_back(de);
		}
	}
}

bool cls::HandleRow(InterchangeDataRowStruct& row) {

	                                 
	if ( row.m_vFields.at(2) == 0 )
		return false;	                             

	const string& stPyloadEntityTypeId = row.m_vFields.at(2).str();
	long entityTypeId = -1;
	try {
		entityTypeId = konga::toLong(stPyloadEntityTypeId);
	}
	catch ( const CrException& e ) {
		throw CrException("Client Error: Illegal PayloadEntityType Id for JMS Message. " + e.GetErrorString());
	}

	if ( entityTypeId == DocumentEntityId ) {
		HandleDocumentEntityType(row);
	}
	else if ( entityTypeId == XmlEntityId ) {
		HandleXmlEntityType(row);
	}
	else if ( entityTypeId == InvalidEntityId ) {
		HandleNoPayloadType(row);
	}
	else {
		throw CrException("Client Error: Invalid entity type for a JMS message. " \
			"Only Document or XML are allowed (\"" + stPyloadEntityTypeId + "\" was sent).");
	}

	return true;
}

namespace {
	const string TAB_NAME = "DataElementsTab";
	const string ID_COL_NAME = "DataElementId";
	const string DETAB_COLUMNS = "(dataelementid, ownerentitytypeid, ownerentityid, dataelementname, dataelementtype, dataelementdefaultvalue)";
}

bool cls::HandleRowAfterInsertOrUpdateImpl(long newId, InterchangeDataRowStruct& row) {
	                                          
	const long jmsId = newId;
	string sql = "DELETE FROM DataElementsTab WHERE OwnerEntityTypeId = "
		+ toString(JmsMessageEntityId) + " AND OwnerEntityId = "
		+ toString(jmsId);
	m_dbConnection.ExecuteSql(sql);

	if ( m_dataElements.empty() )
		return false;	                                             

	const string& jmsGuid = m_guidMapper.GetGuid(jmsId, JmsMessageEntityId);
	if ( jmsGuid.empty() )
		throw CrException("Internal Error: Failed to get the GUID for JmsMessage with ID = " + toString(jmsId));

	DEVectorMapT::const_iterator citProperties = m_dataElements.find(jmsGuid);
	if ( citProperties == m_dataElements.end() )
		return false;	                                                   

	if ( m_iCurrentDataElementId < 0 ) {
		m_iCurrentDataElementId = konga::GetNextId(TAB_NAME, ID_COL_NAME, m_dbConnection);
	}

	const DEVector& deVector = citProperties->second;
	DEVector::const_iterator cit = deVector.begin();
	for(; cit != deVector.end(); ++cit ) {
		const DE& de = *cit;
		sql = "INSERT INTO Dataelementstab " + DETAB_COLUMNS
			+ " VALUES("
			+ toString(m_iCurrentDataElementId) + ","
			+ toString(JmsMessageEntityId) + ","
			+ toString(jmsId) + ",'"
			+ SqlEscape(de.name) + "','" + SqlEscape(de.type) + "','" + SqlEscape(de.value) + "')";
		m_dbConnection.ExecuteSql(sql);
		++m_iCurrentDataElementId;
	}
	return true;

}

bool cls::HandleRowAfterUpdate(long newId, InterchangeDataRowStruct& row) {
	return HandleRowAfterInsertOrUpdateImpl(newId, row);
}

bool cls::HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row) {
	return HandleRowAfterInsertOrUpdateImpl(newId, row);
}

void cls::HandleDocumentEntityType(InterchangeDataRowStruct& row) {
	NullableString& value = row.m_vFields.at(3);
	if ( value == 0 )
		throw CrException("Expected Document Id GUID is null for a JMS Message.");
	const string& guid = value.str();
	const long documentId = m_guidMapper.GetId(guid, DocumentEntityId);
	if ( documentId == GuidToIdMapper::invalid_id )
		throw CrException("The document referenced by JMS Message has not been deployed.");	
	value = toString(documentId);
}

void cls::HandleXmlEntityType(InterchangeDataRowStruct& row) {
	NullableString& value = row.m_vFields.at(3);
	if ( value == 0 )
		throw CrException("Expected XML file path.");

	const string& sClientPath = value.str();

	long userId = m_userInfo.GetId();
	const string& sUserGuid = m_guidMapper.GetGuid(userId, UserEntityId);
	assert(!sUserGuid.empty());
	string sPath, sName;
	GetServerPath(sClientPath, sUserGuid, sPath, sName);

	UploadedFile queryFile(sPath, sName, "", INVALID_FILE_TYPE, "", 0);
	UploadedFile existingFile;
	UploadedFilesManager fileMgr(m_dbConnection);

	if ( !fileMgr.ExistsFile(queryFile, existingFile, true) ) {
		throw CrException("XML file with path \"" + sClientPath + "\" does not exist on the server. " \
			"It is needed for a JMS Message.");
	}

	const string& guid = existingFile.GetGuid();
	long xmlId = m_guidMapper.GetId(guid, XmlEntityId);
	if ( xmlId == GuidToIdMapper::invalid_id ) {
		throw CrException("Internal Error: Failed to find Id for XML file with guid \""
			+ guid + "\".");
	}

	value = toString(xmlId);
}

void cls::HandleNoPayloadType(InterchangeDataRowStruct& row) {
	row.m_vFields.at(2) = "0";
	row.m_vFields.at(3) = "0";
}

