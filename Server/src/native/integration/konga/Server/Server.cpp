/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/UsersTab.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/util.h>

#include "JavaLaunchDetails.h"
#include "Server.h"

using namespace konga;
using std::string;

namespace {

	string quote_and_escape(const string& s) {
		return string("'" + SqlEscape(s) + "'");
	}

	string key_name_where(const string& stKeyName) {
		return string(" WHERE Key_Name = " + quote_and_escape(stKeyName));
	}

	string value_name(const bool isBigValue) {
		return string( isBigValue ? "Long_Key_Value" : "Key_Value" );
	}

	string key_name_select_sql(const string& stKeyName, const bool isBigValue) {
		return string( "SELECT " + value_name(isBigValue) + " FROM Key_Values" + key_name_where(stKeyName));
	}

	string GetKeyValueImpl(const string& stKeyName, const bool isBigValue, DbConnection& dbConnection, const string& stDefaultValue) {
		const string& sql = key_name_select_sql(stKeyName, isBigValue);
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
		rs->Open();
		if ( rs->Next() ) {
			if ( !rs->IsFieldNull(0) ) {
				return rs->GetFieldValue(0);
			}
		}
		return stDefaultValue;
	}

	void SetKeyValueImpl(const string& stKeyName, const string& stKeyValue, const bool isBigValue, DbConnection& dbConnection) {
		const string& select_sql = key_name_select_sql(stKeyName, isBigValue);
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, select_sql) );
		rs->Open();
		const bool recordExists = rs->Next();
		rs.CloseAndDelete();

		string sql;
		if ( recordExists ) {
			sql = "UPDATE Key_Values SET " + value_name(isBigValue) + " = " + quote_and_escape(stKeyValue) + key_name_where(stKeyName);
		}
		else {
			sql = "INSERT INTO Key_Values (Key_Name, " + value_name(isBigValue) + ") VALUES(" + quote_and_escape(stKeyName) + "," + quote_and_escape(stKeyValue) + ")";
		}
		dbConnection.ExecuteSql(sql);
	}
}

string konga::GetKeyValue(const string& stKeyName, DbConnection& dbConnection, const string& stDefaultValue) {
	return GetKeyValueImpl(stKeyName, false, dbConnection, stDefaultValue);
}

void konga::SetKeyValue(const string& stKeyName, const string& stKeyValue, DbConnection& dbConnection) {
	SetKeyValueImpl(stKeyName, stKeyValue, false, dbConnection);
}

string konga::GetKeyValue(const string& stKeyName, const string& stDefaultValue) {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	return GetKeyValue(stKeyName, *pTranDb, stDefaultValue);
}

void konga::SetKeyValue(const string& stKeyName, const string& stKeyValue) {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	SetKeyValue(stKeyName, stKeyValue, *pTranDb);
}

string konga::GetBigKeyValue(const string& stKeyName, DbConnection& dbConnection, const string& stDefaultValue) {
	return GetKeyValueImpl(stKeyName, true, dbConnection, stDefaultValue);
}

void konga::SetBigKeyValue(const string& stKeyName, const string& stKeyValue, DbConnection& dbConnection) {
	SetKeyValueImpl(stKeyName, stKeyValue, true, dbConnection);
}

string konga::GetBigKeyValue(const string& stKeyName, const string& stDefaultValue) {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	return GetBigKeyValue(stKeyName, *pTranDb, stDefaultValue);
}

void konga::SetBigKeyValue(const string& stKeyName, const string& stKeyValue) {
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	SetBigKeyValue(stKeyName, stKeyValue, *pTranDb);
}

namespace {
	const string SERVER_GUID_KEY("ServerGUID");
	const string VERSION_KEY("VersionNumber");

	string GetServerGUIDImpl(const string& stServerGUID) {
		if ( !stServerGUID.empty() )
			return stServerGUID;

		                                                          
		string stNewServerGuid;
		GetGUIDString(stNewServerGuid);
		SetKeyValue(SERVER_GUID_KEY, stNewServerGuid);
		return stNewServerGuid;
	}
}

bool konga::HasServerGuid() {
	const string guid( GetKeyValue(SERVER_GUID_KEY) );
	return !guid.empty();
}

string konga::GetServerGUID(DbConnection& dbConnection) {
	return GetServerGUIDImpl(GetKeyValue(SERVER_GUID_KEY, dbConnection));
}

string konga::GetServerGUID() {
	return GetServerGUIDImpl(GetKeyValue(SERVER_GUID_KEY));
}

void konga::SetServerGUID(const string& stServerGuid) {
	SetKeyValue(SERVER_GUID_KEY, stServerGuid);
}

string konga::ResetServerGuid() {
	SetKeyValue(SERVER_GUID_KEY, "");
	return konga::GetServerGUID();
}

konga::Version konga::GetDbVersion() {
	const string& stVersion = GetKeyValue(VERSION_KEY, "0");
	return Version(stVersion);
}

namespace {
	const string SERVER_LABEL("ServerLabel");
}

string konga::GetServerLabel() {
	return GetKeyValue(SERVER_LABEL);
}

void konga::SetServerLabel(const string& sLabel) {
	SetKeyValue(SERVER_LABEL, sLabel);
}

void konga::GetUserCredentials(long lUserId, string& sUserName, string& sMd5Password) {
	DBConnectionInfo dbInfo("TranDb");
	SmartDbPtr<DbConnection> pConnection( DbConnection::MakeInstance(dbInfo.connectionString()) );

	SmartDbPtr<TranDb::UsersTab> pUsers( TranDb::UsersTab::MakeInstance(pConnection) );

	pUsers->Select("WHERE user_id = " + toString(lUserId));

	if( !pUsers->Next() )
		throw CrException("User (" + toString(lUserId) + ") not found in TranDb UsersTab.");

	sUserName = pUsers->IsLoginNull() ? "" : pUsers->GetLogin();
	sMd5Password = pUsers->IsPasswordNull() ? "" : pUsers->GetPassword();
}

void konga::GetAdminCredentials(string& sUserName, string& sMd5Password) {
	GetUserCredentials(0, sUserName, sMd5Password);
}

void konga::GetInternalAdminCredentials(string& sUserName, string& sMd5Password) {
	GetUserCredentials(-1, sUserName, sMd5Password);
}

string konga::GetDataDirectoryWithSlash() {
	return AppendPathSeparator( GetServerHomeWithSlash() + "DataInterchange" );
}

string konga::GetTemporaryLoadedSourceDataDirWithSlash() {
	string stTmp(getTmpDir());
	stTmp += "LoadedSourcesTmp";
	if( !IsDirectory(stTmp) ) {
		kCreateDirectory(stTmp);
	}
	AppendPathSeparator(stTmp);
	return stTmp;
}

string konga::GetUploadedProjectsBaseDirectoryWithSlash() {
	string stTmp(GetDataDirectoryWithSlash());
	stTmp += "UploadedJitterpaks";
	if ( !IsDirectory(stTmp) ) {
		kCreateDirectory(stTmp);
	}
	AppendPathSeparator(stTmp);
	return stTmp;
}

string konga::GetUploadedWsdlBaseDirectoryWithSlash() {
	string stWsdlBaseDir( GetOptionAsString("Settings/Wsdl", "Wsdl", "DataInterchange/wsdl") );
	AppendPathSeparator(stWsdlBaseDir);
	return getAbsolutePath(stWsdlBaseDir, false);
}

namespace {
	string GetJtrDirectoryWithSlash() {
		string sJtrDir( GetDataDirectoryWithSlash() + "jtr" );
		if ( !IsDirectory(sJtrDir) ) {
			try {
				kCreateDirectory(sJtrDir);
			}
			catch( const CrException& ex ) {
				throw CrException("Failed to create directory for storing jtr files." \
					"\nReason: " + ex.GetErrorString());
			}
		}
		AppendPathSeparator(sJtrDir);
		return sJtrDir;
	}

	string jtrFile(long lTransformationId, const string st) {
		return string(GetJtrDirectoryWithSlash() + "tran"
			+ toString(lTransformationId) + st + ".jtr");
	}
}

string konga::GetSourceJtrFileName(long lTransformationId) {
	return jtrFile(lTransformationId, "s");
}
string konga::GetTargetJtrFileName(long lTransformationId) {
	return jtrFile(lTransformationId, "t");
}

void konga::GetJavaLaunchDetails(konga::JavaLaunchDetails& javaLaunchDetails, const string& sComponentName, unsigned long lDefaultPort) {
#ifdef WIN32
	javaLaunchDetails.sProcessPath = "java.exe";
#else
	javaLaunchDetails.sProcessPath = "java";
#endif
	javaLaunchDetails.sProcessPath = konga::GetOptionAsString(sComponentName, "ProcessPath", javaLaunchDetails.sProcessPath);

	javaLaunchDetails.sArgs += " -jar \"" + konga::GetServerHomeWithSlash();
	string sRelativeJarPath = konga::GetOptionAsString(sComponentName, "RelativeJarPath", "");
	if( sRelativeJarPath.empty() )
		throw CrException("Must specify \n\n[" + sComponentName + "]\nRelativeJarPath=\n\n in " +
		GetServerHomeWithSlash() + "ETLForge.conf. The value of RelativeJarPath should be"
		" the path of the JAR file for the " + sComponentName +
		" and it should be relative " + GetServerHomeWithSlash());
	javaLaunchDetails.sArgs += sRelativeJarPath + "\"";

	javaLaunchDetails.sArgs = konga::GetOptionAsString(sComponentName, "ProcessArgs", javaLaunchDetails.sArgs);
	javaLaunchDetails.sArgs += " " + konga::GetOptionAsString(sComponentName, "AdditionalArgs", "-Xmx256m") + " ";

	javaLaunchDetails.lPort = konga::GetOptionAsInt(sComponentName, "CommandPort", lDefaultPort);

	javaLaunchDetails.sWorkingDir = konga::GetOptionAsString(sComponentName, "WorkingDir", "");

	javaLaunchDetails.sStdOutPath = konga::GetOptionAsString(sComponentName, "StdOutPath", "");
	if( javaLaunchDetails.sStdOutPath.empty() ) {
		javaLaunchDetails.sStdOutPath = konga::GetServerHomeWithSlash() + "log/" + sComponentName + ".stdout.txt";
	}

	javaLaunchDetails.sStdErrPath = konga::GetOptionAsString(sComponentName, "StdErrPath", "");
	if( javaLaunchDetails.sStdErrPath.empty() ) {
		javaLaunchDetails.sStdErrPath = konga::GetServerHomeWithSlash() + "log/" + sComponentName + ".stderr.txt";
	}
}
