/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Ldap/LdapFilter.h>

#include <konga/Utils/KString.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>	                                 

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/ConnectionString.h>
#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>
#include <konga/Db/TranDbUtils/TranDbUtils.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/OperationObjects/DatabaseLocation.h>
#include <konga/OperationObjects/HttpLocation.h>

#include "TranDBUtils.h"

using namespace konga;
using namespace TranDb;
using std::string;

DbConnection* konga::GetTranDbConnection() {
	return konga::GetBackEndDbConnection("TranDb");
}

namespace {
	const int lDefaultTimeOut = 300;

	void SetAdditionalConnectionProperties(DbConnection& tranDbConn, int iEntityTypeId, long locationId,
		const string& sDriverName,
		TransactionIsolationLevel& txnIsolationLevel, string& sBeginQuote, string& sEndQuote, int& fetchSize) {
		txnIsolationLevel = UNDEFINED;

		ItemProperties itemProps(&tranDbConn);
		KeyValues properties;
		itemProps.GetItemProperties(iEntityTypeId, locationId, properties);

		                                  
		const int dbTxnLevel = properties.GetInt(DB_LOCATION_TRANSACTION_ISOLATION_LEVEL, -99);
		if ( dbTxnLevel != -99) {
			txnIsolationLevel = DatabaseLocation::fromClientIdentifier(dbTxnLevel);
		}

		fetchSize = properties.GetInt(DB_FETCH_SIZE, 0);

		                                  
		                                                                        
		const string undefined("undefined");
		sBeginQuote = konga::GetQuoteBeginForDriver(sDriverName, undefined);
		if ( sBeginQuote == undefined ) {
			sBeginQuote = properties.GetString(DB_LOCATION_BEGIN_QUOTE, "");
		}

		sEndQuote = konga::GetQuoteEndForDriver(sDriverName, undefined);
		if ( sEndQuote == undefined ) {
			sEndQuote = properties.GetString(DB_LOCATION_END_QUOTEKEY, "");
		}
	}

	                                   
	template<class T>
	void getConnectionInfoFromTab(bool isSource, T* tab, const UserInfo& userInfo, DbConnection& tranDbConn,
		int locationID, const OperationDataElementHolder* const pDEHolder,
		DbConnectionInfoStruct& dbConnInfo,
		string& sErr)
	{
		string stServer=tab->IsLocatorNull()?"":tab->GetLocator();
		string stDriver=tab->IsDriverNull()?"":tab->GetDriver();
		string stDbName=tab->IsDBNameNull()?"":tab->GetDBName();
		string stLogin=tab->IsLoginNull()?"":tab->GetLogin();
		string stPassword=tab->IsPasswordNull()?"":tab->GetPassword();
		try {
			stPassword = DecryptString(stPassword);
		} catch(CrException&){
			string OveridePwd = konga::GetOptionAsString("Debug", "OveridePwd", "" );
			if(OveridePwd!="")stPassword=OveridePwd;
		}
		string stPath=tab->IsPathNull()?"":tab->GetPath();
		string stDSN=tab->IsDSNNull()?"":tab->GetDSN();
		string stFile_Name=tab->IsFile_NameNull()?"":tab->GetFile_Name();
		string stConnectionString=tab->IsConnectStringNull()?"":tab->GetConnectString();
		string stAdditionalConnectParams = tab->IsOptional_ConnectStringNull() ? "" : tab->GetOptional_ConnectString();
		dbConnInfo.driverType=tab->IsDriverTypeNull() ? konga::ODBC : (konga::DatabaseDriverType)tab->GetDriverType();

		if ( pDEHolder != 0 ) {
			stServer = pDEHolder->replaceOperationDataElementInString(stServer);
			stDriver = pDEHolder->replaceOperationDataElementInString(stDriver);
			stDbName = pDEHolder->replaceOperationDataElementInString(stDbName);
			stLogin = pDEHolder->replaceOperationDataElementInString(stLogin);
			stPassword = pDEHolder->replaceOperationDataElementInString(stPassword);

			stConnectionString = pDEHolder->replaceOperationDataElementInString(stConnectionString);
			stAdditionalConnectParams = pDEHolder->replaceOperationDataElementInString(stAdditionalConnectParams);
		}

		                                                                                      
		const long port = tab->IsPortNull() ? -1 : tab->GetPort();

		GetConnectionString(stServer, port, stDriver, stDbName, stLogin, stPassword, stPath, stDSN,
			stFile_Name, stConnectionString, stAdditionalConnectParams, false, dbConnInfo.connectionString);

		int iTmp = tab->IsTimeoutNull() ? lDefaultTimeOut : tab->GetTimeout();
		if ( iTmp < 1 ) {
			iTmp = lDefaultTimeOut;
		}
		dbConnInfo.timeOutSeconds = iTmp;

		SetAdditionalConnectionProperties(tranDbConn, isSource ? SourceEntityId : TargetEntityId, locationID, stDriver,
			dbConnInfo.txnIsolationLevel, dbConnInfo.beginQuote, dbConnInfo.endQuote, dbConnInfo.fetchSize);

		         
		dbConnInfo.additionalParams = stAdditionalConnectParams;
		dbConnInfo.database = stDbName;
		dbConnInfo.driverName = stDriver;
		dbConnInfo.manualConnectionString = stConnectionString;
		dbConnInfo.password = stPassword;
		dbConnInfo.port = port;
		dbConnInfo.server = stServer;
		dbConnInfo.user = stLogin;
	}
}

unsigned long konga::getSourceConnectionInfo(DbConnection& tranDbConn,
											 const UserInfo& userInfo,
											 int sourceID,
											 const OperationDataElementHolder* const pDEHolder,
											 DbConnectionInfoStruct& dbConnInfo,
											 string& sErr)
{
	unsigned long hr = S_OK;
	dbConnInfo.txnIsolationLevel = UNDEFINED;
	try
	{
        SmartDbPtr<SourcesTab> srcTab( SourcesTab::MakeInstance(&tranDbConn) );
		const string stFilter(" WHERE Source_ID = " + konga::toString(sourceID));
		srcTab->Select(stFilter);
		if( srcTab->Next() )
		{
			UserAccess userAccess(userInfo, tranDbConn);
			if ( !userAccess.HasExecutePermission(sourceID, SourceEntityId) ) {
				const string name( srcTab->IsSource_NameNull() ? "" : srcTab->GetSource_Name() );
				throw CrException("The current user does not have execute permission for the source '" + name + "'.");
			}
			getConnectionInfoFromTab<SourcesTab>(true, srcTab, userInfo, tranDbConn, sourceID, pDEHolder, dbConnInfo, sErr);
		}
		else
		{
			sErr = "No source defined with ID " + konga::toString(sourceID);
			hr = E_FAIL;
		}
	}
	catch( const DbException& e )
	{
		sErr = e.GetErrorString();
		hr = E_FAIL;
	}

	return hr;
}

unsigned long konga::getTargetConnectionInfo(DbConnection& tranDbConn,
											 const UserInfo& userInfo,
											 int targetID,
											 const OperationDataElementHolder* const pDEHolder,
											 DbConnectionInfoStruct& dbConnInfo,
											 string& sErr)
{
	unsigned long hr = S_OK;
	try
	{
        SmartDbPtr<TargetsTab> tgtTab( TargetsTab::MakeInstance(&tranDbConn) );
		const string stFilter("WHERE Target_ID = " + konga::toString(targetID));
		tgtTab->Select(stFilter);
		if( tgtTab->Next() )
		{
			UserAccess userAccess(userInfo, tranDbConn);
			if ( !userAccess.HasExecutePermission(targetID, TargetEntityId) ) {
				const string name( tgtTab->IsTarget_NameNull() ? "" : tgtTab->GetTarget_Name() );
				throw CrException("The current user does not have execute permission for the target '" + name + "'.");
			}

			getConnectionInfoFromTab<TargetsTab>(false, tgtTab, userInfo, tranDbConn, targetID, pDEHolder, dbConnInfo, sErr);
		}
		else
		{
			sErr = "No target defined with ID " + konga::toString(targetID);
			hr = E_FAIL;
		}
	}
	catch( const DbException& e )
	{
		sErr = e.GetErrorString();
		hr = E_FAIL;
	}
	return hr;
}

long konga::getLocationId(const string& prefixedGuid) {
	SmartDbPtr<DbConnection> pTranDbConn(GetTranDbConnection());
	return konga::getLocationId(prefixedGuid, *pTranDbConn);
}

long konga::getLocationId(const string& prefixedGuid, DbConnection& tranDbConnection) {
	bool dummy;
	return getLocationId(prefixedGuid, dummy, tranDbConnection);
}

long konga::getLocationId(const string& prefixedGuid_in, bool& isSource, DbConnection& tranDbConnection) {
	const string& guid = getLocationGuid(prefixedGuid_in, isSource);
	const int entityTypeId = isSource ? SourceEntityId : TargetEntityId;

	DbGuidToIdMapper guidMapper(&tranDbConnection);
	const long id = guidMapper.GetId(guid, entityTypeId);
	if ( id == GuidToIdMapper::invalid_id )
		throw CrException("The requested " + konga::GetEntityNameByEntityIdSingular(entityTypeId)
		+ " has not been deployed to this server, please deploy your project and try again. " \
		"If this does not help, please contact ETLForge support.");
	return id;
}

long konga::GetOperationIdFromTag(const std::string& prefixedGuid, DbConnection& tranDbConnection) {
	if( prefixedGuid.length() < 4 || !EqualsIgnoreCase(prefixedGuid.substr(0, 3), "op.") )
		throw CrException("Invalid Operation GUID (" + prefixedGuid + "). Expected: 'op.GUID'");
	const string stOperationGuid( prefixedGuid.substr(3) );                 
	DbGuidToIdMapper guidMapper(&tranDbConnection);
	const long lOperationId = guidMapper.GetId(stOperationGuid, OperationEntityId);
	if ( lOperationId == GuidToIdMapper::invalid_id )
		throw CrException("The requested " + konga::GetEntityNameByEntityIdSingular(OperationEntityId)
		+ " has not been deployed to this server, please deploy your project and try again. " \
		"If this does not help, please contact ETLForge support.");
	return lOperationId;
}

string konga::getConnectionStringFromDb(DbConnection* pTranDbConn,
										const UserInfo& userInfo,
										const string& prefixedGuid,
										const OperationDataElementHolder* const pDEHolder,
										DbConnectionInfoStruct& dbConnInfo)
{
	SmartDbPtr<DbConnection> adopt_our_tranDbConn;
	if ( pTranDbConn == 0 ) {
		try {
			adopt_our_tranDbConn.Attach( pTranDbConn = GetTranDbConnection() );
		}
		catch( const DbException& e ) {
			throw CrException("Failed to open database connection.\n" + e.GetErrorString());
		}
	}

	bool bIsSource = true;
	const long locationID = konga::getLocationId(prefixedGuid, bIsSource, *pTranDbConn);
	string stErr;
	if(bIsSource){
		unsigned long hr = getSourceConnectionInfo(*pTranDbConn, userInfo, locationID, pDEHolder, dbConnInfo, stErr);
		if( FAILED(hr) )
			throw CrException("Failed to get connection string for database source.\n" + stErr);
	} else {
		unsigned long hr = getTargetConnectionInfo(*pTranDbConn, userInfo, locationID, pDEHolder, dbConnInfo, stErr);
		if( FAILED(hr) )
			throw CrException("Failed to get connection string for database target.\n" + stErr);
	}
	return dbConnInfo.connectionString;
}

void konga::getLdapFiltersForTransformation(long lTranId,
											konga::LdapFilterCollection& filterCollection,
											DbConnection& dbConnection)
{
	SmartDbPtr<DynamicDbRecordSet> rsLdapFiltersTab( DynamicDbRecordSet::MakeInstance(&dbConnection) );
	rsLdapFiltersTab->SetSqlStatement("SELECT ClassName, Path, Filter, Scope FROM LdapSearchParametersTab " \
		"WHERE Transformation_Id = " + toString(lTranId));

	rsLdapFiltersTab->Open();
	while( rsLdapFiltersTab->Next() ) {
		const string& stClassName = rsLdapFiltersTab->GetFieldValue(0);
		const string& stPath = rsLdapFiltersTab->GetFieldValue(1);
		const string& stFilter = rsLdapFiltersTab->GetFieldValue(2);
		const string& stScope = rsLdapFiltersTab->GetFieldValue(3);
		const LdapClassFilter::SearchScope scope = static_cast<LdapClassFilter::SearchScope>(atoi(stScope.c_str()));

		filterCollection.add( new LdapClassFilter(stClassName, stPath, stFilter, scope) );
	}
}

void konga::replaceGlobalDataElementsInCollection(LdapFilterCollection& filterCollection,
												  const OperationDataElementHolder& deHolder)
{
	LdapClassFilter* pFilter = filterCollection.getFirstLdapClassFiler();
	while( pFilter ) {
		const string& sClassName = pFilter->getClassName();
		const string& sFilter = pFilter->getFilter();
		const string& sPath = pFilter->getPath();

		pFilter->setClassName( deHolder.replaceOperationDataElementInString(sClassName) );
		pFilter->setFilter( deHolder.replaceOperationDataElementInString(sFilter) );
		pFilter->setPath( deHolder.replaceOperationDataElementInString(sPath) );

		pFilter = filterCollection.getNextLdapClassFilter();
	}
}

konga::TestTransformationSourceType konga::getTestTransformationSourceType(int transformation_ID, FILE* debug_log) {

	SmartDbPtr<DbConnection> pTranDbConn;
	try {
		pTranDbConn.Attach( GetTranDbConnection() );
	}
	catch( const DbException& e ) {
		throw CrException(string("get Transformation Source type (transformation_ID=")+konga::toString(transformation_ID)+" failed to get DB Connection.\n"+e.GetErrorString());
	}

	string sSelect = "SELECT Source_Type_ID, NatureOfSource FROM TransformationsTab_New WHERE (Transformation_ID = " + konga::toString(transformation_ID) + ")";

	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(pTranDbConn, sSelect.c_str()) );
	pSelect->Open();
	if ( pSelect->Next() ){
		int Source_Type_ID=atoi(pSelect->GetFieldValue(0).c_str());
		string NatureOfSource=pSelect->GetFieldValue(1);
		if(debug_log!=0){
			fprintf(debug_log,"getTransformationSourceType transformation_ID=%d, Source_Type_ID=%d, NatureOfSource=%s\n",
				transformation_ID, Source_Type_ID, NatureOfSource.c_str());
			fflush(debug_log);
		}
		if( Source_Type_ID==DbTypeId || Source_Type_ID == LdapTypeId ){
                                                                                               
                                                                                                     
			return SOURCE_TYPE_FILTER;
		}
		else if(Source_Type_ID==NoSourceTypeId){
			return SOURCE_TYPE_NO_SOURCE;                           
		}
	}

	return SOURCE_TYPE_FILE;        
}

HttpOptions konga::GetRuntimeHttpOptionsForEntity(long lItemId, int EntityTypeId, const OperationDataElementHolder& deHolder, DbConnection* pTranDb) {
	ItemProperties itemProps(pTranDb);
	KeyValues props;
	itemProps.GetItemProperties(EntityTypeId, lItemId, props);

	return konga::GetRuntimeHttpOptions(props, deHolder);
}

HttpOptions konga::GetRuntimeHttpOptions(const KeyValues& keyValues, const OperationDataElementHolder& deHolder) {
	HttpOptions httpOptions;

	string key(HTTP_LOCATION_ENABLE_CONTENT_ENCODING);
	if ( keyValues.IsKeyDefined(key) ) httpOptions.enableContentEncoding(keyValues.GetBool(key, false));
	key = HTTP_LOCATION_ENABLE_CHUNKED_TRANSFER_ENCODING;
	if ( keyValues.IsKeyDefined(key) ) httpOptions.enableChunkedTransferEncoding(keyValues.GetBool(key, false));
	key = HTTP_LOCATION_SEND_EXPECT_100_CONTINUE;
	if ( keyValues.IsKeyDefined(key) ) httpOptions.enableExpect100ContinueHeader(keyValues.GetBool(key, false));

	key = HTTP_LOCATION_CONTENT_TYPE;
	if ( keyValues.IsKeyDefined(key) ) {
		const string& value = deHolder.replaceOperationDataElementInString(keyValues.GetString(key, ""));
		httpOptions.setContentType(value);
	}
	key = HTTP_LOCATION_REQUEST_HEADERS;
	if ( keyValues.IsKeyDefined(key) ) {
		const string& value = deHolder.replaceOperationDataElementInString(keyValues.GetString(key, ""));
		httpOptions.setCustomRequestHeaders(value);
	}
	return httpOptions;
}
