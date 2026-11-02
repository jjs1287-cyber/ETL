/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <memory>	                

#include <konga/Utils/KeyValues.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/MapUtils.h>
#include <konga/Utils/PortDefs.h>

#include <konga/DbRelations/SourceTypeIdConstants.h>

#include "FtpSource.h"
#include "FileShareSource.h"
#include "HttpSource.h"
#include "JmsSource.h"
#include "TemporaryStorageSource.h"
#include "GlobalVariableLocation.h"
#include "S3Location.h"

#include "FtpTarget.h"
#include "FileShareTarget.h"
#include "HttpTarget.h"
#include "JmsTarget.h"
#include "TemporaryStorageTarget.h"

#include "DatabaseSourceLocation.h"
#include "DatabaseTargetLocation.h"
#include "LdapSourceLocation.h"
#include "LdapTargetLocation.h"

#include "FileTypeSourceLocation.h"
#include "FileTypeTargetLocation.h"

#include "OperationEngineException.h"
#include "OperationObjectCreatorKeyValues.h"

using namespace konga;
using std::string;
using std::map;

namespace {
	const string stSourceTypeId("source_type_id");
	const string stDocumentId("document_id");
	const string stLocator("locator");
	const string stPath("path");
	const string stFileName("file_name");
	const string stTimeOut("timeout");
	const string stLogin("login");
	const string stPassword("password");
	const string stIgnoreFirst("ignore_first");
	const string stIgnoreLast("ignore_last");
	const string stDsn("dsn");
	const string stTransferType("transfer_type");
	const string stEncryptText("encrypt_text");
	const string stEncryptChoice("encrypt_choice");
	const string stDriver("driver");
	const string stDbTypeId("dbtype_id");
	const string stPort("port");
	const string stPassiveMode("passive_mode");
	const string stDbName("dbname");
	const string stConnectString("connectstring");
	const string stRenameFileTo("renamefileto");
	const string stSecured("secured");
	const string stReferralMode("referral_mode");
	const string stHttpUrl("httpurl");
	const string stCertName("certificatename");
	const string stOptConnectString("optional_connectstring");
	const string stOdbcUncUserId("odbc_unc_userid");
	const string stOdbcUncPassword("odbc_unc_password");
	const string stIdKey("item_id");

	const string stSmtpMessage("smtp_message");

	void fillCommonDataStruct(DataLocation::CommonDataStruct& commonData,
		const KeyValues& keyValues,
		int iSourceTypeId)
	{

		const int dataLocationType = iSourceTypeId;
		commonData.m_id = keyValues.GetLong(stIdKey);
		commonData.m_type = dataLocationType;
		commonData.m_lDocumentId = keyValues.GetLong(stDocumentId);
		commonData.m_stLocator = keyValues.GetString(stLocator);

		commonData.m_stPath = keyValues.GetString(stPath);
		commonData.m_stFileName = keyValues.GetString(stFileName);
		commonData.m_lTimeOut = keyValues.GetLong(stTimeOut);
		commonData.m_stLogin = keyValues.GetString(stLogin);
		commonData.m_stPassword = keyValues.GetString(stPassword);

		commonData.m_stDSN = keyValues.GetString(stDsn);
		commonData.m_lTransferType = keyValues.GetLong(stTransferType);
		commonData.m_stEncryptText = keyValues.GetString(stEncryptText);
		commonData.m_lEncryptChoice = keyValues.GetLong(stEncryptChoice);
		commonData.m_stDriver = keyValues.GetString(stDriver);

		commonData.m_lPort = keyValues.GetLong(stPort);
		commonData.m_iPassiveMode = keyValues.GetInt(stPassiveMode);
		commonData.m_stDBName = keyValues.GetString(stDbName);
		commonData.m_stConnectString = keyValues.GetString(stConnectString);
		commonData.m_lSecured = keyValues.GetLong(stSecured, 0);
		commonData.m_lReferralMode = keyValues.GetLong(stReferralMode);

		commonData.m_stHttpUrl = keyValues.GetString(stHttpUrl);
		commonData.m_stCertificateName = keyValues.GetString(stCertName);
		commonData.m_stOptionalConnectString = keyValues.GetString(stOptConnectString);
		commonData.m_stODBC_UNC_UserID = keyValues.GetString(stOdbcUncUserId);
		commonData.m_stODBC_UNC_Password = keyValues.GetString(stOdbcUncPassword);
	}

	int getSourceTypeId(const KeyValues& keyValues) {
		const int iSourceTypeId = keyValues.GetInt(stSourceTypeId);
		if ( iSourceTypeId == -1 )
			throw OperationEngineException("Bad Data: The key \"" + stSourceTypeId + "\" was not provided.");
		return iSourceTypeId;
	}
}

namespace {
	void FillJmsLocationProperties(map<string, string>& locationPropertiesMap,
		const KeyValues& keyValues)
	{
		KeyValues::const_iterator cit = keyValues.begin();
		for(; cit != keyValues.end(); ++cit ) {
			const string& keyValue = cit->first;
			if ( strncasecmp(keyValue.c_str(), "jms.", 4) == 0 ||
				 strncasecmp(keyValue.c_str(), "jndi.", 5) == 0 )
			{
				AddOrUpdate<map<string,string>, string, string>(locationPropertiesMap,
					keyValue, cit->second);
			}
		}
	}
}

FileTypeSourceLocation* OperationObjectCreatorKeyValues::getFileTypeSourceLocation(const KeyValues& keyValues,
																				   OperationDataElementHolder& deHolder)
{
	const int iSourceTypeId = getSourceTypeId(keyValues);

	SourceDataLocation* pSourceDataLocation = 0;
	DataLocation* pDataLocation = 0;
	std::auto_ptr<FileTypeSourceLocation> adopt_pSource(0);
	switch(iSourceTypeId) {
		case FtpSourceTypeId:
			{
				FtpSource* p = new FtpSource();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		case FileShareSourceTypeId:
			{
				FileShareSource* p = new FileShareSource();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		case HttpSourceTypeId:
			{
				HttpSource* p = new HttpSource();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		case JmsSourceTypeId:
			{
				JmsSource* p = new JmsSource("");
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;

				FillJmsLocationProperties(p->m_locationProperties, keyValues);
			}
			break;

		case S3SourceTypeId:
			{
				S3Source* p = new S3Source();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		case GlobalVariableSourceTypeId:
			{
				GlobalVariableSource* p = new GlobalVariableSource();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		case TemporaryStorageSourceTypeId:
		case LocalFileSourceTypeId:
			{
				TemporaryStorageSource* p = new TemporaryStorageSource();
				adopt_pSource.reset(p);
				pSourceDataLocation = p;
				pDataLocation = p;
			}
			break;

		default:
			throw OperationEngineException("The SourceTypeId = " + toString(iSourceTypeId) + " is not a file type or has not been implemented yet.");
	}

	fillCommonDataStruct(pDataLocation->m_CommonData, keyValues, iSourceTypeId);
	pDataLocation->m_pDataElementHolder = &deHolder;
	pDataLocation->m_extendedProperties = keyValues;

	SourceDataLocation::SourceDataStruct& sourceData = pSourceDataLocation->m_SourceData;
	sourceData.m_lSourceId = pDataLocation->m_CommonData.m_id;
	sourceData.m_lIgnoreFirst = keyValues.GetLong(stIgnoreFirst);
	sourceData.m_lIgnoreLast = keyValues.GetLong(stIgnoreLast);
	sourceData.m_lDBTypeId = keyValues.GetLong(stDbTypeId);
	sourceData.m_stRenameFileTo = keyValues.GetString(stRenameFileTo);
	sourceData.m_pExtendedProperties = &pDataLocation->m_extendedProperties;

	return adopt_pSource.release();
}

FileTypeTargetLocation* OperationObjectCreatorKeyValues::getFileTypeTargetLocation(const KeyValues& keyValues,
																				   OperationDataElementHolder& deHolder)
{
	const int iSourceTypeId = getSourceTypeId(keyValues);

	TargetDataLocation* pTargetDataLocation = 0;
	DataLocation* pDataLocation = 0;
	std::auto_ptr<FileTypeTargetLocation> adopt_pTarget(0);
	switch(iSourceTypeId) {
		case FtpSourceTypeId:
			{
				FtpTarget* p = new FtpTarget();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		case FileShareSourceTypeId:
			{
				FileShareTarget* p = new FileShareTarget();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		case HttpSourceTypeId:
			{
				HttpTarget* p = new HttpTarget();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		case JmsSourceTypeId:
			{
				JmsTarget* p = new JmsTarget(0);
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;

				FillJmsLocationProperties(p->m_locationProperties, keyValues);

			}
			break;

		case S3SourceTypeId:
			{
				S3Target* p = new S3Target();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		case GlobalVariableSourceTypeId:
			{
				GlobalVariableTarget* p = new GlobalVariableTarget();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		case TemporaryStorageSourceTypeId:
		case LocalFileSourceTypeId:
			{
				TemporaryStorageTarget* p = new TemporaryStorageTarget();
				adopt_pTarget.reset(p);
				pTargetDataLocation = p;
				pDataLocation = p;
			}
			break;

		default:
			throw OperationEngineException("The SourceTypeId = " + toString(iSourceTypeId) + " is not a file type or has not been implemented yet.");
	}

	fillCommonDataStruct(pDataLocation->m_CommonData, keyValues, iSourceTypeId);
	pDataLocation->m_pDataElementHolder = &deHolder;
	pDataLocation->m_extendedProperties = keyValues;

	pTargetDataLocation->m_TargetData.m_lSmtpMessage = keyValues.GetLong(stSmtpMessage);
	pTargetDataLocation->m_TargetData.m_lTargetId = pDataLocation->m_CommonData.m_id;

	return adopt_pTarget.release();
}

DatabaseLocation* OperationObjectCreatorKeyValues::getDatabaseLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder, bool bIsSource) {
	const int iSourceTypeId = getSourceTypeId(keyValues);

	if ( iSourceTypeId != DatabaseSourceTypeId )
		throw OperationEngineException("Bad Data: The SourceTypeId = " + toString(iSourceTypeId) + " is not a database type.");

	std::auto_ptr<DatabaseLocation> pDbLoc(0);
	DatabaseSourceLocation* pSource = 0;
	DatabaseTargetLocation* pTarget = 0;
	if ( bIsSource ) {
		pDbLoc.reset( pSource = new DatabaseSourceLocation() );
	} else {
		pDbLoc.reset( pTarget = new DatabaseTargetLocation() );
	}

	fillCommonDataStruct(pDbLoc->m_CommonData, keyValues, iSourceTypeId);
	pDbLoc->m_pDataElementHolder = &deHolder;
	pDbLoc->m_extendedProperties = keyValues;
	if ( pSource ) {
		pSource->m_SourceData.m_lSourceId = pDbLoc->getId();
	}
	if ( pTarget ) {
		pTarget->m_TargetData.m_lTargetId = pDbLoc->getId();
	}

	return pDbLoc.release();
}

LdapLocation* OperationObjectCreatorKeyValues::getLdapLocation(const KeyValues& keyValues, OperationDataElementHolder& deHolder, bool bIsSource) {
	const int iSourceTypeId = getSourceTypeId(keyValues);

	if ( iSourceTypeId != LdapSourceTypeId )
		throw OperationEngineException("The SourceTypeId = " + toString(iSourceTypeId) + " does not represent an LDAP source or target.");

	std::auto_ptr<LdapLocation> pLdapLoc(0);
	LdapSourceLocation* pSource = 0;
	LdapTargetLocation* pTarget = 0;
	if ( bIsSource ) {
		pLdapLoc.reset( pSource = new LdapSourceLocation() );
	} else {
		pLdapLoc.reset( pTarget = new LdapTargetLocation() );
	}

	fillCommonDataStruct(pLdapLoc->m_CommonData, keyValues, iSourceTypeId);
	pLdapLoc->m_pDataElementHolder = &deHolder;
	pLdapLoc->m_extendedProperties = keyValues;
	if ( pSource ) {
		pSource->m_SourceData.m_lSourceId = pLdapLoc->getId();
	}
	if ( pTarget ) {
		pTarget->m_TargetData.m_lTargetId = pLdapLoc->getId();
	}

	return pLdapLoc.release();
}

