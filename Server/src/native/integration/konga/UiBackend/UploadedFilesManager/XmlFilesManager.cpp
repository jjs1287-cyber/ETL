/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <sstream>		               
#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlSchemaUtils.h>

#include <konga/Zip/ZipUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "FileUploadUtils.h"
#include "UploadedFilesManager.h"
#include "XmlFilesManager.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;

namespace {
	const string stSelectMaxSql("SELECT MAX(xmldtdid) AS max1, MAX(xml_id) AS max2 FROM XMLDtdTab");

	                                        
	                                                                       
	long AddXmlFileImpl(const std::string& stPath, const std::string& stFileName,
		DbConnection& tranDbConnection)
	{
		if ( tranDbConnection.GetTransactionIsolationLevel() != konga::SERIALIZABLE )
			throw InternalErrorException("The connection has to be serializable");

		SmartDbPtr<DynamicDbRecordSet> selectMax( DynamicDbRecordSet::MakeInstance(&tranDbConnection, stSelectMaxSql) );
		selectMax->Open();
		selectMax->Next();
		string tmp = selectMax->GetFieldValue(0);
		                                                                                                         
		const long keyId = atol(selectMax->GetFieldValue(0).c_str()) + 1;
		tmp = selectMax->GetFieldValue(1);
		const long lXmlId = atol(selectMax->GetFieldValue(1).c_str()) + 1;
		stringstream ssInsertSql;
		ssInsertSql << "INSERT INTO XMLDtdTab (XMLDtdID, XML_ID, XML_FileName, XML_Path) "
				   << "VALUES (" << keyId << ", " << lXmlId << ", '" << stFileName << "', '" << stPath << "')";
		tranDbConnection.ExecuteSql(ssInsertSql.str());
		return lXmlId;
	}

	string get_xml_path(const UploadedFile& file) {
		return string( GetUploadedFilesDir() + file.GetCanonicalPath() );
	}
}

long konga::AddXmlFile(const ZippedUploadedFile& incomingFile, bool bExists, bool bAdd,
				UploadedFilesManager& fileManager, GuidToIdMapper& guidMapper,
				UserAccess& userAccess, DbConnection& dbConnection) {
	const string& stGuid = incomingFile.GetGuid();
	long lXmlId = -1;

	                                                   
	if ( bExists ) {
		lXmlId = guidMapper.GetId(stGuid, XmlEntityId);
		if ( lXmlId == GuidToIdMapper::invalid_id ) {
			throw InternalErrorException("File exists on the system but the ID could not be found." \
				" This is probably a bug. Try to delete the file and try to upload it again.");
		}
		if ( !userAccess.HasWritePermission(lXmlId, XmlEntityId) ) {
			throw CrException("The file already exists on the system but the current user does not" \
				" have write permission for it.");
		}
	}

	if ( bAdd ) {
		if ( bExists )
			fileManager.UpdateFile(incomingFile);
		else
			fileManager.AddFile(incomingFile);

		if ( !bExists ) {
			                                               
			const string& stXmlPath = get_xml_path(incomingFile);
			lXmlId = AddXmlFileImpl(stXmlPath, incomingFile.GetName(), dbConnection);

			                             
			userAccess.AddItemOwner(lXmlId, XmlEntityId);

			            
			guidMapper.SetGuidForItem(lXmlId, XmlEntityId, stGuid);
		}
	}
	return lXmlId;
}

namespace {
	void CloneXmlDependencies(const long lParentXmlId, const long lClonedParentXmlId,
		GuidToIdMapper& guidMapper, const UserAccess& userAccess, const UserInfo& userInfo,
		const UploadedFilesManager& fileManager, DbConnection& tranDbConnection)
	{
		vector<UploadedFile> dependencies;
		GetXmlDependencies(lParentXmlId, dependencies, guidMapper, userAccess, fileManager, tranDbConnection);
		
		for( vector<UploadedFile>::const_iterator cit = dependencies.begin(); cit != dependencies.end(); ++cit ) {
			const UploadedFile& dependency = *cit;
			const string& guid = dependency.GetGuid();
			const long lChildXmlId = guidMapper.GetId(guid, XmlEntityId);
			if ( lChildXmlId == GuidToIdMapper::invalid_id )
				continue;	                           

			long lClonedChildXmlId = CloneXmlFile(lChildXmlId, tranDbConnection, userInfo, guidMapper);
			if ( lClonedChildXmlId != lChildXmlId ) {
				AddXmlDependency(lClonedParentXmlId, lClonedChildXmlId, tranDbConnection);
			}
		}
	}
}

long konga::CloneXmlFile(const long lXmlId, DbConnection& tranDbConnection, const UserInfo& userInfo, GuidToIdMapper& guidMapper) {

	string sql("select xml_filename, xml_path from xmldtdtab where xml_id = " + toString(lXmlId));
	SmartDbPtr<DynamicDbRecordSet> rs1( DynamicDbRecordSet::MakeInstance(&tranDbConnection, sql) );
	rs1->Open();
	if ( !rs1->Next() ) {
		throw InternalErrorException("Failed to clone XML file with xml_id = " + toString(lXmlId)
			+ " since it does not exist.");
	}

	const string& xml_filename = rs1->GetFieldValue(0);
	const string& xml_path = rs1->GetFieldValue(1);
	rs1.CloseAndDelete();
	
	const string absolutePath(konga::getAbsolutePath(xml_path) + xml_filename);
	char* pcZ = 0;
	const string& stMD5 = ZipUtils::ZipAndBase64EncodeFile(absolutePath, pcZ, true);
	assert(pcZ);
	boost::scoped_array<char> adopt_pcZ(pcZ);	                                                

	                                                                
	const string& stUserGuid = konga::GetUserGuid(userInfo, guidMapper);
	string stPath, stName;
	konga::GetServerPath(xml_filename, stUserGuid, stPath, stName);

	UserAccess userAccess(userInfo, tranDbConnection);

	UploadedFilesManager fileManager(tranDbConnection);
	UploadedFile file(stPath, stName);
	long lClonedXmlId = -1;
	if ( fileManager.ExistsFile(file, file, false) ) {
		                                          
		                                
		if ( stMD5 == file.GetMd5() ) {
			sql = "select xml_id from xmldtdtab where xml_filename = '" + xml_filename + "' and xml_path = '"
				+ get_xml_path(file) + "'";
			SmartDbPtr<DynamicDbRecordSet> rs2( DynamicDbRecordSet::MakeInstance(&tranDbConnection, sql) );
			rs2->Open();
			if ( !rs2->Next() ) {
				throw InternalErrorException("Failed to find existing XML file in XMLDtdTab. xml_filename = '"
					+ xml_filename + "', xmp_path = '" + xml_path + "'.");
			}

			lClonedXmlId = toLong(rs2->GetFieldValue(0));
			return lClonedXmlId;	                                                              
		}
		else {
			                            
			ZippedUploadedFile zippedFile(file);
			zippedFile.SetMd5(stMD5);
			zippedFile.SetZippedBase64Contents(pcZ);
			zippedFile.SetModifiedNow();
			lClonedXmlId = AddXmlFile(zippedFile, true, true, fileManager, guidMapper, userAccess, tranDbConnection);

			                                                                      
		}
	}
	else {
		XmlSchemaUtils::XmlSchemaType type = XmlSchemaUtils::GetSchemaType(absolutePath);
		UploadedFileType fileType = (type == XmlSchemaUtils::XML_DTD) ? XML_DTD_FILE_TYPE : XML_SCHEMA_FILE_TYPE;

		                                           
		ZippedUploadedFile zippedFile(file);
		zippedFile.SetFileType(fileType);
		zippedFile.SetGuid(konga::GetGUIDString());
		zippedFile.SetMd5(stMD5);
		zippedFile.SetModifiedNow();
		zippedFile.SetZippedBase64Contents(pcZ);

		lClonedXmlId = AddXmlFile(zippedFile, false, true, fileManager, guidMapper, userAccess, tranDbConnection);
	}

	                         
	CloneXmlDependencies(lXmlId, lClonedXmlId, guidMapper, userAccess, userInfo, fileManager, tranDbConnection);

	return lClonedXmlId;
}

void konga::AddXmlDependency(long lParentXmlId, long lChildXmlId, DbConnection& dbConnection) {
	if ( lParentXmlId == lChildXmlId )
		throw CrException("Invalid Input: Recursive structure file not allowed. The parent and child structure files are the same file.");

	                                  
	stringstream ssSql;
	ssSql << "SELECT parent_dtd FROM xmldtdlinks WHERE parent_dtd = " << lParentXmlId
		<< " AND child_dtd = " << lChildXmlId;
	SmartDbPtr<DynamicDbRecordSet> selectLink(
		DynamicDbRecordSet::MakeInstance(&dbConnection, ssSql.str())
		);

	selectLink->Open();
	if ( !selectLink->Next() ) {
		                                                
		stringstream ssInsertLinkSql;
		ssInsertLinkSql << "INSERT INTO xmldtdlinks (parent_dtd, child_dtd) VALUES ("
			<< lParentXmlId << ", " << lChildXmlId << ")";
		dbConnection.ExecuteSql(ssInsertLinkSql.str());
	}
}

	                                            
void konga::GetXmlDependencies(const long lXmlId, vector<UploadedFile>& dependencies,
	const GuidToIdMapper& guidMapper, const UserAccess& userAccess,
	const UploadedFilesManager& fileManager, DbConnection& dbConnection) {

	dependencies.clear();
	const string stSelectLinks("SELECT child_dtd FROM " + getTableName(nameXMLDtdLinks)
		+ " WHERE parent_dtd = " + toString(lXmlId));

	SmartDbPtr<DynamicDbRecordSet> pRecordSet( DynamicDbRecordSet::MakeInstance(&dbConnection, stSelectLinks) );
	pRecordSet->Open();
	while ( pRecordSet->Next() ) {
		const long lChildXmlId = atoi(pRecordSet->GetFieldValue(0).c_str());
		const string& stChildGuid = guidMapper.GetGuid(lChildXmlId, XmlEntityId);
		if ( stChildGuid.empty() )
			throw InternalErrorException("A child xml does not have corresponding GUID defined on the system.");

		if ( !userAccess.HasReadPermission(lChildXmlId, XmlEntityId) )
			continue;

		UploadedFile child("", "", stChildGuid), existingChild;
		if ( !fileManager.ExistsFile(child, existingChild, true) )
			throw InternalErrorException("Child schema file with GUID = " + stChildGuid
			+ " does not exist on the server.");

		dependencies.push_back( existingChild );
	}
}
