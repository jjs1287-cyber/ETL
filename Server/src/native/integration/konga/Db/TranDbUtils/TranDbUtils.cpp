/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>	           

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>
#include <konga/Db/TranDb/JmsProvidersTab.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "TranDbUtils.h"

using namespace konga;
using namespace std;

string konga::getDecryptedPasswordFromDb(const long lId, int iEntityTypeId) {
	
	                                   
	string sEncryptedPwd;
	switch( iEntityTypeId ) {
		case SourceEntityId: 
			sEncryptedPwd = getEncryptedPasswordForSourceFromDb(lId);
			break;
		case TargetEntityId: 
			sEncryptedPwd = getEncryptedPasswordForTargetFromDb(lId);
			break;
		default:
			throw CrException("Internal Error: entity type id " + toString(iEntityTypeId) + 
				" is not supported by getDecryptedPasswordFromDb(). Only sources (" +  toString(SourceEntityId) + 
				") and targets (" + toString(TargetEntityId) + ") are supported." );
	}

	                             
	string stPassword;
	try {
		stPassword = DecryptString(sEncryptedPwd);
	} catch(CrException& ex){
		std::string OveridePwd = konga::GetOptionAsString("Debug", "OveridePwd", "" );
		if(!OveridePwd.empty())stPassword=OveridePwd;
		else throw ex;
	}

	return stPassword;
}

string konga::getEncryptedPasswordForSourceFromDb(const long lId) {
	SmartDbPtr<DbConnection> pConnection( konga::GetBackEndDbConnection("TranDb") );
	const string stSourceId(toString(lId));
	SmartDbPtr<TranDb::SourcesTab> pSourcesTab( TranDb::SourcesTab::MakeInstance(pConnection) );
	pSourcesTab->Select("WHERE Source_ID = " + stSourceId);
	if ( pSourcesTab->Next() )
		return pSourcesTab->IsPasswordNull() ? "" : pSourcesTab->GetPassword();
	else
		throw CrException("No source with Source_ID = " + stSourceId + " has been defined.");
}

string konga::getEncryptedPasswordForTargetFromDb(const long lId) {
	SmartDbPtr<DbConnection> pConnection( konga::GetBackEndDbConnection("TranDb") );
	const string stTargetId(toString(lId));
	SmartDbPtr<TranDb::TargetsTab> pTargetsTab( TranDb::TargetsTab::MakeInstance(pConnection) );
	pTargetsTab->Select("WHERE Target_ID = " + stTargetId);
	if ( pTargetsTab->Next() )
		return pTargetsTab->IsPasswordNull() ? "" : pTargetsTab->GetPassword();
	else
		throw CrException("No Target with Target_ID = " + stTargetId + " has been defined.");
}

string konga::getJmsProviderClasspathFromDb(string& sProviderNames) {
	SmartDbPtr<DbConnection> pConnection( konga::GetBackEndDbConnection("TranDb") );
	SmartDbPtr<TranDb::JmsProvidersTab> pJmsProvidersTab( TranDb::JmsProvidersTab::MakeInstance(pConnection) );
	pJmsProvidersTab->Select();

	string sRet;
	while( pJmsProvidersTab->Next() ) {
		sProviderNames += pJmsProvidersTab->GetProviderName();
		sRet += pJmsProvidersTab->GetClasspath() + ';';
	}
	return sRet;
}

namespace {
	const string SOURCE_TAG("s.");
	const string TARGET_TAG("t.");
}

string konga::getLocationGuid(const string& prefixedGuid, bool& isSource) {
	if ( prefixedGuid.size() < 10 )
		throw CrException("Internal Error: " + prefixedGuid + " is not a valid source/target GUID.");

	string tag(prefixedGuid.substr(0,2));
	ToLower(tag);
	string guid;
	if ( tag == SOURCE_TAG || tag == TARGET_TAG ) {
		guid = prefixedGuid.substr(2);
		isSource = tag == SOURCE_TAG;
	}
	else {
		throw InternalErrorException("Internal Error: " + prefixedGuid + " is not a valid Source/Target identifier.");
	}
	return guid;
}

string konga::getItemName(const long lId, int iEntityTypeId, DbConnection& tranDbConnection) {
	const string& entityName = konga::GetEntityNameByEntityId(iEntityTypeId);
	const string sql("SELECT " + getNameColumnName(iEntityTypeId) + " FROM " + getTableName(entityName)
		+ " WHERE " + getIdName(entityName) + " = " + toString(lId));

	SmartDbPtr<DynamicDbRecordSet> rs(DynamicDbRecordSet::MakeInstance(&tranDbConnection, sql));
	rs->Open();
	if ( rs->Next() ) {
		if ( rs->IsFieldNull(0) )
			return "";
		return rs->GetFieldValue(0);
	}
	else {
		return "";
	}
}
