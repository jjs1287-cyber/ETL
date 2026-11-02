/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>
#include <iostream>
#include <memory>	                

#include <konga/Utils/CrException.h>
#include <konga/Utils/Version.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/KeyValues.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>	                              
#include <konga/Db/TranDb/Key_Values.h>		                                                   

#include <konga/SysInfo/SysInfo.h>		                      

#include <konga/Authentication/Authentication.h>
#include <konga/DbRelations/EntityIdConstants.h>	               

#include <konga/Server/Server.h>					                  
#include <konga/ServerVersion/ServerVersion.h>		                     
#include <konga/Server/lmcheck.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "AuthenticateImpl.h"

using namespace konga;

namespace {
	const Version requiredClientVersion(4,1,0,15,"");

	void ValidateClientVersion(const Version& clientVersion) {
		if ( clientVersion < requiredClientVersion )
			throw CrException("You need to have at least client version "
			+ requiredClientVersion.GetShortVersionString() +
			" to log in to this server.");
	}
}

AuthenticateImpl::AuthenticateImpl() : DbConnectionHolder(DBConnectionInfo("TranDb").connectionString()) {}

AuthenticateResult* AuthenticateImpl::login(const char* const user, const char* const password) {
	                                                                            
	                            
	return loginWithVersion(user, password, "1.1.1");
}

AuthenticateResult* AuthenticateImpl::loginWithVersion(const char* const user, const char* const password,
													   const char* const version)
{
	return loginWithVersionAndKeyValues(user, password, version, 0);
}


namespace {

	void ExtractKeyValues(KeyValues& keyValues, WsKeyValuePairs* const wsKeyValues) {
		if ( wsKeyValues == 0 )
			return;

		ArrayOf_tns2_WsKeyValue* tns2_arr = wsKeyValues->getkeyValues();
		if ( tns2_arr == 0 )
			return;

		WsKeyValue_Array* wsArr =  tns2_arr->getitem();
		if ( wsArr == 0 )
			return;

		int size = 0;
		WsKeyValue** arr = wsArr->get(size);
		if ( size == 0 || arr == 0 )
			return;

		for( int i = 0; i < size; ++i ) {
			WsKeyValue* keyValue = arr[i];
			if ( keyValue == 0 )
				continue;

			const string& key = toString(keyValue->getkey());
			keyValues.AddCString(key, keyValue->getvalue());
		}
	}

	void AddKeyValue(WsKeyValue_Array* pKeyValues, const string& key, const string& value) {
		auto_ptr<WsKeyValue> pKeyValue( new WsKeyValue() );
		pKeyValue->setkey(const_cast<xsd__string>(key.c_str()), true);
		pKeyValue->setvalue(const_cast<xsd__string>(value.c_str()), true);
		pKeyValues->addElement(pKeyValue.release());
	}

	void AddKeyValues(WsKeyValue_Array* pKeyValues, const KeyValues& keyValues) {
		for ( KeyValues::const_iterator cit = keyValues.begin(); cit != keyValues.end(); ++cit ) {
			const string& key = cit->first;
			const string& value = cit->second;
			AddKeyValue(pKeyValues, key, value);
		}
	}

	void AddAvailableVersionKeyValues(KeyValues& keyValues, const string& sClientVersion, DbConnection& tranDbConnection) {
		try {
			konga::SmartDbPtr<TranDb::Key_Values> pKey_Values(TranDb::Key_Values::MakeInstance(&tranDbConnection));
			pKey_Values->Select("WHERE group_id=5");
			while( pKey_Values->Next() && !pKey_Values->IsKey_NameNull() && !pKey_Values->IsKey_ValueNull() ) {
				keyValues.AddString(pKey_Values->GetKey_Name(), pKey_Values->GetKey_Value());
			}
		}
		catch( const DbException& e ) {
			throw InternalErrorException(e.GetPrettyErrorMessage());
		}

		try {
			SetKeyValue("ClientVersionNumber", sClientVersion, tranDbConnection);
		}
		catch( const DbException& e ) {
			throw InternalErrorException(e.GetPrettyErrorMessage());
		}
	}

	bool isClientTrial(const KeyValues& keyValues) {
		return keyValues.GetBool("trial", false);
	}
}

AuthenticateResult* AuthenticateImpl::loginWithVersionAndKeyValues(const char* const user, const char* const password,
																   const char* const version, WsKeyValuePairs* wsKeyValues)
{
	const string& stVersion = toString(version);
	try {
		                          
		Version clientVersion( stVersion );
		ValidateClientVersion(clientVersion);
	}
	catch( const CrException& ex ) {
		UIBACKEND_TRACE("Failed to validate client version '" + stVersion + "'. " + ex.GetErrorString());
		const string& stUser = toString(user);
		auditLog(stUser + " login failure: " + ex.GetErrorString());
		throw;
	}

	string stUser("unknown");
	try {
		stUser = toString(user);
		UIBACKEND_TRACE("Authentication called with user ='" + stUser + "'");

		const string& stPassword = toString(password);
		const UserInfo& userInfo = Authentication::GetValidUser(stUser, stPassword, &GetDbConnection());

		KeyValues inputParameters;
		ExtractKeyValues(inputParameters, wsKeyValues);

		KeyValues outputParameters;
		lm* plm = konga::get_lm(&GetDbConnection());
		if ( plm ) {
			std::auto_ptr<lm> adopt_plm(plm);
			plm->inspect(outputParameters);
		}

		outputParameters.AddString("OS", GetPrettyOsString());
		outputParameters.AddString("SysInfo", GetPrettySysInfoString());
		outputParameters.AddString("IsAdministrator", userInfo.IsAdministrator() ? "1" : "0");

		AddAvailableVersionKeyValues(outputParameters, version, GetDbConnection());

		UIBACKEND_TRACE(outputParameters.toString());

		auto_ptr<AuthenticateResult> pRet( new AuthenticateResult() );
		pRet->setserverGuid(const_cast<xsd__string>( GetServerGUID(GetDbConnection()).c_str() ), true);
		pRet->setuserGuid(
			const_cast<xsd__string>( DbGuidToIdMapper(&GetDbConnection()).GetGuid(userInfo.GetId(), UserEntityId).c_str() ),true);
		pRet->setversionString( const_cast<xsd__string>( GetServerVersion().GetLongVersionString().c_str() ),true);

		auto_ptr<WsKeyValue_Array> pKeyVals( new WsKeyValue_Array() );
		AddKeyValues(pKeyVals.get(), outputParameters);

		auto_ptr<ArrayOf_tns2_WsKeyValue> ptns2KeyVals( new ArrayOf_tns2_WsKeyValue() );
		ptns2KeyVals->setitem(pKeyVals.get());	                                  

		auto_ptr<WsKeyValuePairs> pKeyValPairs( new WsKeyValuePairs() );
		pKeyValPairs->setkeyValues(ptns2KeyVals.release());	                                                                       

		pRet->setkeyValues(pKeyValPairs.release());	                         

		auditLog("Login: " + stUser + ", Client Version: " + stVersion);

		return pRet.release();
	}
	catch( const konga::Authentication::AuthenticationException& e ) {
		auditLog("Authentication Failed: " + e.GetUserName() + ", " + e.GetErrorString());
		throw;
	}
	catch( const CrException& e) {
		UIBACKEND_TRACE("Authentication failed, caught CrException: " + e.GetErrorString());
		auditLog(stUser + " login authentication failure: " + e.GetErrorString());
		throw;
	}
	catch( const std::exception& e) {
		string stErr("Internal error: std::exception caught in loginWithVersion: "
			+ std::string(e.what()));
		UIBACKEND_TRACE(stErr);
		auditLog(stUser + " login authentication failure: " + stErr);
		throw CrException(stErr);
	}
}

