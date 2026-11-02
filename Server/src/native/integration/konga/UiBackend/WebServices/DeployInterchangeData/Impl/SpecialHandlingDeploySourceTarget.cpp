/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDbUtils/DatabaseDriverType.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/SourceTypeIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  


#include "DeployUtils.h"
#include "DatabaseDriverManager.h"
#include "SpecialHandlingDeploySourceTarget.h"
#include "InterchangeDataStructs.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::vector;

namespace {

	int GetDriverColPos(const xsd__string_Array& columnNames) {
		vector<string> vDriverColNames(1);
		vDriverColNames[0] = "driver";
		vColPosT vUserColPos = GetFieldIndexes(columnNames, vDriverColNames);
		if ( vUserColPos.empty() )
			throw CrException("Bad data (client error): Column \"" + vDriverColNames[0] + "\" not found for source/target");
		return vUserColPos.at(0);
	}

	int GetLoginColPos(const xsd__string_Array& columnNames) {
		vector<string> vUserColNames(1);
		vUserColNames[0] = "login";
		vColPosT vUserColPos = GetFieldIndexes(columnNames, vUserColNames);
		if ( vUserColPos.empty() )
			throw CrException("Bad data (client error): Column \"" + vUserColNames[0] + "\" not found for source/target");
		return vUserColPos.at(0);
	}

	int GetGuidColPos(const xsd__string_Array& columnNames, bool bIsSource) {
		vector<string> vIdColNames(1);
		vIdColNames[0] = bIsSource ? "source_id" : "target_id";
		vColPosT vIdColPos = GetFieldIndexes(columnNames, vIdColNames);
		if ( vIdColPos.empty() )
			throw CrException("Bad data (client error): Column \"" + vIdColNames[0] + "\" not found for source/target");
		return vIdColPos.at(0);
	}

	int GetPasswordColPos(const xsd__string_Array& columnNames) {
		vector<string> vPasswordColNames(1);
		vPasswordColNames[0] = "password";
		vColPosT vPwdColPos = GetFieldIndexes(columnNames, vPasswordColNames);
		if ( vPwdColPos.empty() )
			throw CrException("Bad data (client error): Column \"password\" not found for source/target");
		return vPwdColPos.at(0);
	}

	int GetConnectStringColPos(const xsd__string_Array& columnNames) {
		vector<string> vPasswordColNames(1);
		vPasswordColNames[0] = "connectstring";
		vColPosT vPwdColPos = GetFieldIndexes(columnNames, vPasswordColNames);
		if ( vPwdColPos.empty() )
			throw CrException("Bad data (client error): Column \"connectstring\" not found for source/target");
		return vPwdColPos.at(0);
	}

	int GetSourceTypeColPos(const xsd__string_Array& columnNames) {
		vector<string> vSourceTypeColNames(1);
		vSourceTypeColNames[0] = "source_type_id";
		vColPosT vLocTypeIdColPos = GetFieldIndexes(columnNames, vSourceTypeColNames);
		if ( vLocTypeIdColPos.empty() )
			throw CrException("Bad data (client error): Column \"source_type_id\" not found for source/target");
		return vLocTypeIdColPos.at(0);
	}

	int GetDriverTypeColPos(const xsd__string_Array& columnNames) {
		vector<string> vDriverTypeColNames(1);
		vDriverTypeColNames[0] = "drivertype";
		vColPosT v = GetFieldIndexes(columnNames, vDriverTypeColNames);
		if ( v.empty() )
			throw CrException("Bad data (client error): Column \"drivertype\" not found for source/target");
		return v.at(0);
	}
}

              
SpecialHandlingDeploySourceTarget::SpecialHandlingDeploySourceTarget(const xsd__string_Array& columnNames,
																	 const DatabaseDriverManager& databaseDriverManager,
																	 const InterchangeDataTableStruct& jmsLocationProperties,
																	 GuidToIdMapper& guidToIdMapper,
																	 DbConnection& dbConnection,
																	 bool bIsSource) :
	Crypto(GetPasswordColPos(columnNames)),
	m_databaseDriverManager(databaseDriverManager),
	m_columnNames(columnNames),
	m_bIsSource(bIsSource), m_bDidEncryption(false), m_bJmsLocationsWereUpdated(false),
	m_iEntityTypeId(bIsSource? SourceEntityId : TargetEntityId),
	m_iLocationTypeId(-1),
	m_iConnectStringColPos(GetConnectStringColPos(columnNames)),
	m_iPwdStringColPos(GetPasswordColPos(columnNames)),
	m_iIdColPos(GetGuidColPos(columnNames, bIsSource)),
	m_iLoginColPos(GetLoginColPos(columnNames)),
	m_iDriverColPos(GetDriverColPos(columnNames)),
	m_iLocationTypeColPos(GetSourceTypeColPos(columnNames)),
	m_iDriverTypeColPos(GetDriverTypeColPos(columnNames)),
	m_guidToIdMapper(guidToIdMapper),
	m_dbConnection(dbConnection)
	{
		const vInterchangeDataRowStructT& rows = jmsLocationProperties.m_vRows;
		for( vInterchangeDataRowStructT::const_iterator cit = rows.begin();
			cit != rows.end(); ++ cit )
		{
			const string& guid = cit->m_vFields.at(1).str();
			JmsLocationMapT::iterator it = m_jmsLocationPropertiesMap.find(guid);
			if ( it != m_jmsLocationPropertiesMap.end() ) {
				it->second.push_back(&(*cit));
			}
			else {
				std::vector<const InterchangeDataRowStruct*> v;
				v.push_back(&(*cit));
				m_jmsLocationPropertiesMap.insert(
					JmsLocationMapT::value_type(guid, v)
					);
			}
		}
	}

SpecialHandlingDeploySourceTarget::~SpecialHandlingDeploySourceTarget(void) {
	assert(m_vJmsLocationProperties.empty());
}

bool SpecialHandlingDeploySourceTarget::HandleRow(InterchangeDataRowStruct& row) {

	if ( row.m_vFields.size() < m_iConnectStringColPos + 1 || row.m_vFields.size() < m_iPwdStringColPos + 1 )
		throw CrException("Internal Error: Not enough columns in row for source/target. Please report this bug.");

	                                                                                                      
	const NullableString& login = row.m_vFields.at(m_iLoginColPos);
	m_stLogin.clear();
	m_stGuid.clear();
	if ( login != 0 && !login.const_ref().empty() ) {
		m_stLogin = login.const_ref();
	}
	const NullableString& guid = row.m_vFields.at(m_iIdColPos);
	if ( guid == 0 || guid.const_ref().empty() )
		throw CrException("Client Error: No guid specified for source/target. Please report this bug.");
	m_stGuid = guid;

	m_iLocationTypeId = atoi(row.m_vFields.at(m_iLocationTypeColPos).str().c_str());

	                                            
	const NullableString& driver = row.m_vFields.at(m_iDriverColPos);
	if ( driver != 0 && !driver.const_ref().empty() ) {
		const string& sDriver = driver.const_ref();
		const NullableString& driver_type = row.m_vFields.at(m_iDriverTypeColPos);
		DatabaseDriverType dt = ODBC;
		if ( driver_type != 0 ) {
			const string& sDriverType = driver_type.str();
			const int iDriverType = atoi(sDriverType.c_str());
			dt = static_cast<konga::DatabaseDriverType>(iDriverType);
		}

		if ( dt == konga::ODBC ) {
			if ( !m_databaseDriverManager.ExistsDriver(sDriver) ) {
				const string stType(m_bIsSource? "source" : "target");
				const string stWarning(
					"The driver \"" + sDriver + "\" does not exist on this server. This " + stType
					+ " will not work without it. Specify another driver or install the required driver on the system."
					);
				AddWarning(m_bIsSource? SourceEntityId:TargetEntityId, m_stGuid, stWarning);
			}
		}
		else {
			                                
		}
	}

	HandleJms();

	return ( m_bDidEncryption = Crypto::HandleRow(row) );
}

bool SpecialHandlingDeploySourceTarget::HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row)
{
	if ( !m_bDidEncryption && !m_stLogin.empty() ) {
		const string stType(m_bIsSource? "source" : "target");
		const string stWarning(
			"No password was specified for login \"" + m_stLogin + "\" for this " + stType +
			". If this " + stType + " requires a password you need to enter it and deploy again."
			);
		AddWarning(m_iEntityTypeId, m_stGuid, stWarning);
	}
	return m_bDidEncryption;
}

void SpecialHandlingDeploySourceTarget::HandleJms() {
	if ( m_iLocationTypeId == JmsSourceTypeId ) {
		m_bJmsLocationsWereUpdated = true;
		                                                              
		JmsLocationMapT::const_iterator cit = m_jmsLocationPropertiesMap.find(m_stGuid);
		if ( cit == m_jmsLocationPropertiesMap.end() )
			throw CrException("Internal Error: Failed to find JMS Location properties for GUID = " + m_stGuid);
		vector<const InterchangeDataRowStruct*> properties = cit->second;

		vector<const InterchangeDataRowStruct*>::const_iterator citProps = properties.begin();
		for(; citProps != properties.end(); ++citProps ) {
			const string& keyName = (*citProps)->m_vFields.at(2).str();
			const string& value = (*citProps)->m_vFields.at(3).str();
			vector<string> vJmsLocationPropertyInfo;
			vJmsLocationPropertyInfo.push_back( toString(m_iEntityTypeId) );
			vJmsLocationPropertyInfo.push_back(m_stGuid);
			vJmsLocationPropertyInfo.push_back(keyName);
			vJmsLocationPropertyInfo.push_back(value);

			m_vJmsLocationProperties.push_back(vJmsLocationPropertyInfo);
		}
	}
}

void SpecialHandlingDeploySourceTarget::UpdateJmsLocationProperties() {
	                                                
	if ( m_vJmsLocationProperties.empty() )
		return;

	long jmsLocationPropertyId = GetNextId("JmsLocationPropertiesTab", "JmsLocationPropertyId", m_dbConnection);

	typedef std::vector< std::vector<std::string> > vvsT;
	vvsT::const_iterator cit = m_vJmsLocationProperties.begin();
	string lastGuid;
	for(; cit != m_vJmsLocationProperties.end(); ++cit ) {
		const vector<string>& vProperties = *cit;
		assert(vProperties.size()==4);

		const string& sEntityTypeId = vProperties.at(0);
		const long entityTypeId = toLong(sEntityTypeId);
		const string& guid = vProperties.at(1);
		const long locationId =
			m_guidToIdMapper.GetId(guid, entityTypeId);
		if ( locationId == GuidToIdMapper::invalid_id )
			throw CrException("Internal Error: Failed to find " + string(entityTypeId==SourceEntityId?"source":"target")
			+ " with GUID = " + guid);

		if ( guid != lastGuid ) {
			                
			                     
			string sql = "DELETE FROM JmsLocationPropertiesTab WHERE JmsLocationEntityTypeId = "
				+ sEntityTypeId + " AND JmsLocationId = "
				+ toString(locationId);
			m_dbConnection.ExecuteSql(sql);
		}
		lastGuid = guid;

		const string& keyName = vProperties.at(2);
		const string& value = vProperties.at(3);
		string sql = "INSERT INTO JmsLocationPropertiesTab " \
			"(jmslocationpropertyid, jmslocationentitytypeid, jmslocationid, keyname, value) VALUES("
			+ toString(jmsLocationPropertyId) + ", " + sEntityTypeId + ", "
			+ toString(locationId) + ", '" + SqlEscape(keyName) + "', '"
			+ SqlEscape(value) + "')";
		m_dbConnection.ExecuteSql(sql);
		++jmsLocationPropertyId;
	}
	                                            
	m_vJmsLocationProperties.clear();
}

bool SpecialHandlingDeploySourceTarget::JmsLocationsWereUpdated() const {
	return m_bJmsLocationsWereUpdated;
}

namespace {
	int GetTargetIdTargetIdColPos(const xsd__string_Array& columnNames) {
		vector<string> vColName(1);
		vColName[0] = "target_target_id";
		vColPosT vColPos = GetFieldIndexes(columnNames, vColName);
		if ( vColPos.empty() )
			return -1;
		else
			return vColPos.at(0);
	}
}

              
SpecialHandlingDeployTarget::SpecialHandlingDeployTarget(const xsd__string_Array& columnNames,
	const DatabaseDriverManager& databaseDriverManager,
	const InterchangeDataTableStruct& jmsLocationProperties,
	GuidToIdMapper& guidToIdMapper,
	DbConnection& dbConnection) :
SpecialHandlingDeploySourceTarget(columnNames, databaseDriverManager, jmsLocationProperties, guidToIdMapper, dbConnection, false),
m_iTargetIdColPos(GetGuidColPos(columnNames, false)),
m_iTargetTargetIdColPos(GetTargetIdTargetIdColPos(columnNames))
{
}

bool SpecialHandlingDeployTarget::HandleRow(InterchangeDataRowStruct& row) {
	                                        
	                                                   
	bool bHandled = false;
	if ( m_iTargetTargetIdColPos >= 0 ) {
		NullableString& ttId = row.m_vFields.at(m_iTargetTargetIdColPos);
		const string& guid = row.m_vFields.at(m_iTargetIdColPos).str();
		if ( ttId != 0 && !ttId.str().empty() ) {
			const string& ttGuid = ttId.str();
			m_targetsToUpdate.insert(
				TargetGuid_TargetTargetGuidMapT::value_type(guid, ttGuid)
				);
			                                                                 
			ttId = 0;
		}
		else {
			                                               
			m_targetsToUpdate.insert(
				TargetGuid_TargetTargetGuidMapT::value_type(guid, "")
				);
		}
		bHandled = true;
	}
	return SpecialHandlingDeploySourceTarget::HandleRow(row) || bHandled;
}

bool SpecialHandlingDeployTarget::Update(DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	bool retVal = false;
	TargetGuid_TargetTargetGuidMapT::const_iterator cit = m_targetsToUpdate.begin();
	for(; cit != m_targetsToUpdate.end(); ++cit ) {
		const string& guid = cit->first;
		long lTargetId = guidToIdMapper.GetId(guid, TargetEntityId);
		if ( lTargetId == GuidToIdMapper::invalid_id )
			continue;

		const string& ttGuid = cit->second;
		if ( ttGuid.empty() ) {
			dbConnection.ExecuteSql("UPDATE " + getTableName(nameTargets)
				+ " SET target_target_id = NULL WHERE " + getIdName(nameTargets)
				+ " = " + toString(lTargetId));
		}
		else {
			long lTargetTargetId = guidToIdMapper.GetId(ttGuid, TargetEntityId);
			if ( lTargetTargetId == GuidToIdMapper::invalid_id )
				continue;

			dbConnection.ExecuteSql("UPDATE " + getTableName(nameTargets)
				+ " SET target_target_id = " + toString(lTargetTargetId)
				+ " WHERE " + getIdName(nameTargets)
				+ " = " + toString(lTargetId));
		}
		retVal = true;
	}
	return retVal;
}
