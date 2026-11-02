/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/FileUtils.h>	             

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>
#include <konga/Db/TranDb/OperationsTab.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/DbRelations/SourceTypeIdConstants.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/FileTransfer/HttpVerbEnum.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/DataElement/DataElement.h>

#include <konga/ItemProperties/ItemProperties.h>

#include <konga/Utils/PortDefs.h>	             

#include "Transformation.h"
#include "WebServiceTransformation.h"

#include "OperationEngineException.h"

#include "NoSource.h"
#include "NoTarget.h"

#include "FtpSource.h"
#include "DatabaseSourceLocation.h"
#include "HttpSource.h"
#include "FileShareSource.h"
#include "JmsSource.h"
#include "LdapSourceLocation.h"
#include "TemporaryStorageSource.h"
#include "GlobalVariableLocation.h"
#include "S3Location.h"

#include "FtpTarget.h"
#include "DatabaseTargetLocation.h"
#include "HttpTarget.h"
#include "FileShareTarget.h"
#include "JmsTarget.h"
#include "LdapTargetLocation.h"
#include "TemporaryStorageTarget.h"

#include "OverrideFileTypeSourceLocation.h"
#include "PreviousTargetAsSource.h"
#include "SourceFromHostedWebServicecall.h"

#include "SourceTextDataStructure.h"
#include "SourceDatabaseDataStructure.h"
#include "SourceXmlDataStructure.h"
#include "TargetTextDataStructure.h"
#include "TargetDatabaseDataStructure.h"
#include "TargetXmlDataStructure.h"
#include "ComplexTextDataStructure.h"
#include "NoDataStructure.h"
#include "DefaultDataStructure.h"

#include "SourceWebServiceInputDataStructure.h"
#include "TargetWebServiceInputDataStructure.h"
#include "SourceWebServiceOutputDataStructure.h"
#include "TargetWebServiceOutputDataStructure.h"

#include "LocationCreatorDb.h"

using namespace std;
using namespace boost;
using namespace konga;

Transformation* LocationCreatorDb::getTransformation(long lTransformationId,
															DbConnection& dbConnection,
															OperationDataElementHolder& dataElementHolder,
															PipelinePluginSelector* pPluginSelector) {
	auto_ptr<Transformation> pTransformation;
	try {
		SmartDbPtr<TranDb::TRANSFORMATIONSTAB_V> pTransformationsTab( TranDb::TRANSFORMATIONSTAB_V::MakeInstance(&dbConnection) );
		stringstream sWhere;
		sWhere << "WHERE Transformation_ID = " << lTransformationId;
		pTransformationsTab->Select(sWhere.str());

		bool bIsFunction = false;
		if ( !pTransformationsTab->Next() ) {
			stringstream sErr;
			sErr << "Failed to get the Transformation with TransformationID = " << lTransformationId << ". No such Transformation in the database.";
			throw OperationEngineException(sErr.str());
		}
		
		if ( !pTransformationsTab->IsFunction_IDNull() ) {
			long lFunctionId = pTransformationsTab->GetFunction_ID();
			if ( lFunctionId > 0 ) {
				bIsFunction = true;
				if ( !pTransformationsTab->IsFunctionType_NameNull() ) {
					string stFunctionTypeName( pTransformationsTab->GetFunctionType_Name() );
					if ( strcasecmp(stFunctionTypeName.c_str(), "WebService") == 0 ) {
						auto_ptr<WebServiceTransformation> pWebServiceTransformation( new WebServiceTransformation(lFunctionId) );
						                                       
						pTransformation.reset( pWebServiceTransformation.release() );
					}
					else {
						stringstream sErr;
						sErr << "Transformation with TransformationID = " << lTransformationId << " has Function_ID = " << lFunctionId;
						sErr << " but the specified function type \"" << stFunctionTypeName << "\" is not implemented.";
						throw OperationEngineException(sErr.str());
					}
				}
				else {
					stringstream sErr;
					sErr << "Transformation with TransformationID = " << lTransformationId << " has a Function_ID = " << lFunctionId << " but FunctionType_Name is NULL.";
					throw OperationEngineException(sErr.str());
				}
			}
			else {
				stringstream sErr;
				sErr << "Transformation with TransformationID = " << lTransformationId << " has invalid Function_ID value, " << lFunctionId << ".";
				throw OperationEngineException(sErr.str());
			}
		}
		else {
			pTransformation.reset( new Transformation );
		}

		pTransformation->m_lID = pTransformationsTab->GetTransformation_ID();
		if ( !bIsFunction ) {
			pTransformation->m_stName = pTransformationsTab->IsTransformation_NameNull() ? "" : pTransformationsTab->GetTransformation_Name();
			pTransformation->m_bIsValid = pTransformationsTab->IsIsValidNull() ? false : pTransformationsTab->GetIsValid() == 1;

			pTransformation->m_pSourceStructure.reset( getSourceStructure(pTransformationsTab, dbConnection, dataElementHolder, pPluginSelector) );
			pTransformation->m_pTargetStructure.reset( getTargetStructure(pTransformationsTab, dbConnection, dataElementHolder, pPluginSelector) );
		}
		pTransformation->m_iTargetJmsMessageId = pTransformationsTab->IsTargetJmsMessageIdNull() ? -1 : pTransformationsTab->GetTargetJmsMessageId();
	}
	catch( const DbException& ex) {
		stringstream sErr;
		sErr << "Failed to get the Transformation with TransformationID = " << lTransformationId << "." << endl;
		sErr << "A database exception occurred. The reported error was:" << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str());
	}

	return pTransformation.release();
}

namespace {
	OverrideFileTypeSourceLocation* createOverrideSourceLocation(OperationDataElementHolder& dataElementHolder) {
		if ( !dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME) )
			return 0;

		const DataElement* const deFileName = dataElementHolder.getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME);
		int err;
		const string overrideSourceFileName((const char*)deFileName->GetString(err));
		if ( err == 0 && !overrideSourceFileName.empty() ) {
			dataElementHolder.removeOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME);
			bool isFileExclusive = false;
			if ( dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE) ) {
				const DataElement* const deExcluseive = dataElementHolder.getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE);
				if ( deExcluseive->getType() == DataElement::t_bool ) {
					isFileExclusive = deExcluseive->GetBool(err);
				}
				dataElementHolder.removeOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE);
			}
			if ( !konga::FileExists(overrideSourceFileName) ) {
				throw InternalErrorException("Override source file '" + overrideSourceFileName + "' does not exist!");
			}
			return new OverrideFileTypeSourceLocation(overrideSourceFileName, isFileExclusive);
		}
		return 0;
	}

	void removeOverrideSourceLocation(OperationDataElementHolder& dataElementHolder) {
		if( dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME) ) {
			dataElementHolder.removeOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME);
			dataElementHolder.removeOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE);
		}
	}
}

SourceDataLocation* LocationCreatorDb::getSourceLocation(long lSourceId, DbConnection& dbConnection,
														 OperationDataElementHolder& dataElementHolder,
														 PipelinePluginSelector* pPluginSelector,
														 bool bAllowOverride)
{
	                                    
	                                
	if ( lSourceId == PreviousTargetAsSource::SOURCE_ID ) {
		if ( dataElementHolder.isOperationDataElementDefined(PreviousTargetAsSource::GLOBAL_DE_NAME_FOR_SOURCE_PATH) ) {
			int err = 0;
			const KString sourceDirectory(dataElementHolder.getOperationDataElement(PreviousTargetAsSource::GLOBAL_DE_NAME_FOR_SOURCE_PATH)->GetString(err));
			return new PreviousTargetAsSource((const char*)sourceDirectory);
		}
		else {
			                                                                                            
			if ( bAllowOverride && dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME) ) {
				OverrideFileTypeSourceLocation* p = createOverrideSourceLocation(dataElementHolder);
				if(p)return p;
			}
			else {
				throw OperationEngineException("Failed to process source from previous operation. " \
					"Internal Error: The data element \"" + PreviousTargetAsSource::GLOBAL_DE_NAME_FOR_SOURCE_PATH
					+ "\" is not defined.");
			}
		}
	}
	                                                                                  
	else if ( bAllowOverride && lSourceId == OverrideFileTypeSourceLocation::HTTP_ENDOINT_SOURCE_ID ) {
		if ( dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME) ) {
			OverrideFileTypeSourceLocation* p = createOverrideSourceLocation(dataElementHolder);
			if ( p == 0 ) {
				throw OperationEngineException("Failed to process source from HTTP Endpoint. No request file was specified!(2)");
			}
			return p;
		}
		else {
			throw OperationEngineException("Failed to process source from HTTP Endpoint. No request file was specified!");
		}
	}
	                                                     
	else if ( bAllowOverride && dataElementHolder.isOperationDataElementDefined(SourceFromHostedWebServiceCall::GLOBAL_DE_NAME_FOR_REQUEST_PATH) ) {
		int err = 0;
		DataElement* const de = dataElementHolder.getOperationDataElement(SourceFromHostedWebServiceCall::GLOBAL_DE_NAME_FOR_REQUEST_PATH);
		const string requestFilePath((const char*)de->GetString(err));
		if ( err == 0 && !requestFilePath.empty() ) {
			                                                                              
			dataElementHolder.removeOperationDataElement(SourceFromHostedWebServiceCall::GLOBAL_DE_NAME_FOR_REQUEST_PATH);
			return new SourceFromHostedWebServiceCall(requestFilePath.c_str());
		}
	}
	else if (lSourceId <= 0 ) {
		removeOverrideSourceLocation(dataElementHolder);
		return new NoSource();
	}

	auto_ptr<SourceDataLocation> pSourceDataLocation;
	try {
		SmartDbPtr<TranDb::SourcesTab> pSourcesTab( TranDb::SourcesTab::MakeInstance(&dbConnection) );
		stringstream sWhere;
		sWhere << "WHERE Source_ID = " << lSourceId;
		pSourcesTab->Select(sWhere.str());

		if ( !pSourcesTab->Next() ) {
			stringstream sErr;
			sErr << "Failed to get the Source (location) with SourceID = " << lSourceId << ". Make sure the required source has been deployed.";
			throw OperationEngineException(sErr.str());
		}
		
		const int type = pSourcesTab->GetSource_Type_ID();
		switch(type) {
		case FtpSourceTypeId:
			pSourceDataLocation.reset( new FtpSource );
			break;
		case DatabaseSourceTypeId:
			pSourceDataLocation.reset( new DatabaseSourceLocation );
			break;
		case FileShareSourceTypeId:
			pSourceDataLocation.reset( new FileShareSource );
			break;
		case HttpSourceTypeId:
			pSourceDataLocation.reset( new HttpSource );
			break;
		case JmsSourceTypeId:
			pSourceDataLocation.reset( getJmsSource(dataElementHolder) );
			break;
		case LdapSourceTypeId:
			pSourceDataLocation.reset( new LdapSourceLocation );
			break;
		case S3SourceTypeId:
			pSourceDataLocation.reset(new S3Source());
			break;
		case GlobalVariableSourceTypeId:
			pSourceDataLocation.reset(new GlobalVariableSource());
			break;
		case TemporaryStorageSourceTypeId:
		case LocalFileSourceTypeId:
			pSourceDataLocation.reset( new TemporaryStorageSource() );
			break;
		default:
			throw OperationEngineException("Failed to get the Source (location) with SourceID = "
				+ toString(lSourceId) + ". The Source_Type_ID "
				+ toString(type) + " has not been implemented yet.");
		};

		FileTypeSourceLocation* pTmp = dynamic_cast<FileTypeSourceLocation*>(pSourceDataLocation.get());
		if ( bAllowOverride && pTmp && dataElementHolder.isOperationDataElementDefined(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME) ) {
			                                                                                       
			OverrideFileTypeSourceLocation* p = createOverrideSourceLocation(dataElementHolder);
			if ( p != 0 ) return p;
		}
		else {
			                                                                      
			removeOverrideSourceLocation(dataElementHolder);
		}

		DataLocation::CommonDataStruct data;
		data.m_type = type;
		data.m_id = pSourcesTab->GetSource_ID();
		data.m_stName = pSourcesTab->GetSource_Name();
		data.m_lDocumentId = pSourcesTab->IsDocument_IDNull() ? -1 : pSourcesTab->GetDocument_ID();
		data.m_stLocator = pSourcesTab->IsLocatorNull() ? "" : pSourcesTab->GetLocator();
		data.m_stPath = pSourcesTab->IsPathNull() ? "" : pSourcesTab->GetPath();
		data.m_stFileName = pSourcesTab->IsFile_NameNull() ? "" : pSourcesTab->GetFile_Name();
		data.m_lTimeOut = pSourcesTab->IsTimeoutNull() ? -1 : pSourcesTab->GetTimeout();
		data.m_stLogin = pSourcesTab->IsLoginNull() ? "" : pSourcesTab->GetLogin();
		data.m_stPassword = pSourcesTab->IsPasswordNull() ? "" : pSourcesTab->GetPassword();
		data.m_stDSN = pSourcesTab->IsDSNNull() ? "" : pSourcesTab->GetDSN();
		data.m_lTransferType = pSourcesTab->IsTransfer_TypeNull() ? -1 : pSourcesTab->GetTransfer_Type();
		data.m_stEncryptText = pSourcesTab->IsEncrypt_textNull() ? "" : pSourcesTab->GetEncrypt_text();
		data.m_lEncryptChoice = pSourcesTab->IsEncrypt_choiceNull() ? 0 : pSourcesTab->GetEncrypt_choice();
		data.m_stDriver = pSourcesTab->IsDriverNull() ? "" : pSourcesTab->GetDriver();
		data.m_lPort = pSourcesTab->IsPortNull() ? -1 : pSourcesTab->GetPort();
		data.m_iPassiveMode = pSourcesTab->IsPassive_ModeNull() ? 0 : pSourcesTab->GetPassive_Mode();
		data.m_stDBName = pSourcesTab->IsDBNameNull() ? "" : pSourcesTab->GetDBName();
		data.m_stConnectString = pSourcesTab->IsConnectStringNull() ? "" : pSourcesTab->GetConnectString();
		data.m_lSecured = pSourcesTab->IsSecuredNull() ? 0 : pSourcesTab->GetSecured();
		data.m_stHttpUrl = pSourcesTab->IsHTTPURLNull() ? "" : pSourcesTab->GetHTTPURL();
		data.m_stCertificateName = pSourcesTab->IsCertIFicateNameNull() ? "" : pSourcesTab->GetCertIFicateName();
		data.m_stOptionalConnectString = pSourcesTab->IsOptional_ConnectStringNull() ? "" : pSourcesTab->GetOptional_ConnectString();
		data.m_stODBC_UNC_UserID = pSourcesTab->IsODBC_UNC_UserIDNull() ? "" : pSourcesTab->GetODBC_UNC_UserID();
		data.m_stODBC_UNC_Password = pSourcesTab->IsODBC_UNC_PasswordNull() ? "" : pSourcesTab->GetODBC_UNC_Password();
		data.m_iHttpVerb = pSourcesTab->IsHttpVerbNull() ? static_cast<int>(HttpGet) : pSourcesTab->GetHttpVerb();

		                        
		SourceDataLocation::SourceDataStruct source_data;
		source_data.m_lIgnoreFirst = pSourcesTab->IsIgnore_FirstNull() ? 0 : pSourcesTab->GetIgnore_First();
		source_data.m_lIgnoreLast = pSourcesTab->IsIgnore_LastNull() ? 0 : pSourcesTab->GetIgnore_Last();
		source_data.m_lDBTypeId = pSourcesTab->IsDBType_IdNull() ? -1 : pSourcesTab->GetDBType_Id();
		source_data.m_stRenameFileTo = pSourcesTab->IsRenameFileToNull() ? "" : pSourcesTab->GetRenameFileTo();

		source_data.m_lSourceId = lSourceId;
		source_data.m_sSourceName = data.m_stName;

		                                                                                         
		DataLocation* const pDL = dynamic_cast<DataLocation*>(pSourceDataLocation.get());
		if ( pDL != 0 ) {
			pDL->m_pDataElementHolder = &dataElementHolder;
			pDL->m_CommonData = data;

			                       
			ItemProperties itemProps(&dbConnection);
			itemProps.GetItemProperties(SourceEntityId, lSourceId, pDL->m_extendedProperties);
			source_data.m_pExtendedProperties = &pDL->m_extendedProperties;
		}

		pSourceDataLocation->m_SourceData = source_data;
		pSourceDataLocation->m_pPluginSelector = pPluginSelector;
	}
	catch( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to get the Source (location) with SourceID = " << lSourceId << "." << endl;
		sErr << "A database exception occurred. The reported error was:" << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str());
	}

	return pSourceDataLocation.release();
}

TargetDataLocation* LocationCreatorDb::getTargetLocation(long lTargetId,
														 DbConnection& dbConnection,
														 OperationDataElementHolder& dataElementHolder,
														 PipelinePluginSelector* pPluginSelector,
														 const Transformation* const pTransformation)
{
	if ( lTargetId <= 0 )
		return new NoTarget();

	auto_ptr<TargetDataLocation> pTargetDataLocation;
	try {
		SmartDbPtr<TranDb::TargetsTab> pTargetsTab( TranDb::TargetsTab::MakeInstance(&dbConnection) );
		stringstream sWhere;
		sWhere << "WHERE Target_ID = " << lTargetId;
		pTargetsTab->Select(sWhere.str());

		if ( !pTargetsTab->Next() ) {
			stringstream sErr;
			sErr << "Failed to get the Target (location) with TargetID = " << lTargetId << ". Make sure the required target has been deployed.";
			throw OperationEngineException(sErr.str());
		}
		
		const int type = pTargetsTab->GetSource_Type_ID();
		switch(type) {
		case FtpSourceTypeId:
			pTargetDataLocation.reset( new FtpTarget );
			break;
		case DatabaseSourceTypeId:
			pTargetDataLocation.reset( new DatabaseTargetLocation );
			break;
		case FileShareSourceTypeId:
			pTargetDataLocation.reset( new FileShareTarget );
			break;
		case HttpSourceTypeId:
			pTargetDataLocation.reset( new HttpTarget );
			break;
		case JmsSourceTypeId:
			{
			int iJmsTargetId = -1;
			if ( pTransformation )
				iJmsTargetId = pTransformation->getTargetJmsMessageId();
			pTargetDataLocation.reset( new JmsTarget(iJmsTargetId) );
			}
			break;
		case LdapSourceTypeId:
			pTargetDataLocation.reset( new LdapTargetLocation );
			break;
		case S3SourceTypeId:
			pTargetDataLocation.reset(new S3Target());
			break;
		case GlobalVariableSourceTypeId:
			pTargetDataLocation.reset(new GlobalVariableTarget());
			break;
		case TemporaryStorageSourceTypeId:
		case LocalFileSourceTypeId:
			pTargetDataLocation.reset( new TemporaryStorageTarget() );
			break;
		default:
			throw OperationEngineException("Internal Error: Failed to get the Target (location) with TargetID = "
				+ toString(lTargetId) + ". The Target_Type_ID "
				+ toString(type) + " has not been implemented yet.");
		};

		DataLocation::CommonDataStruct data;
		data.m_type = type;
		data.m_id = pTargetsTab->GetTarget_ID();
		data.m_stName = pTargetsTab->GetTarget_Name();
		data.m_lDocumentId = pTargetsTab->IsDocument_IDNull() ? -1 : pTargetsTab->GetDocument_ID();
		data.m_stLocator = pTargetsTab->IsLocatorNull() ? "" : pTargetsTab->GetLocator();
		data.m_stPath = pTargetsTab->IsPathNull() ? "" : pTargetsTab->GetPath();
		data.m_stFileName = pTargetsTab->IsFile_NameNull() ? "" : pTargetsTab->GetFile_Name();
		data.m_lTimeOut = pTargetsTab->IsTimeoutNull() ? -1 : pTargetsTab->GetTimeout();
		data.m_stLogin = pTargetsTab->IsLoginNull() ? "" : pTargetsTab->GetLogin();
		data.m_stPassword = pTargetsTab->IsPasswordNull() ? "" : pTargetsTab->GetPassword();
		data.m_stDSN = pTargetsTab->IsDSNNull() ? "" : pTargetsTab->GetDSN();
		data.m_lTransferType = pTargetsTab->IsTransfer_TypeNull() ? -1 : pTargetsTab->GetTransfer_Type();
		data.m_stEncryptText = pTargetsTab->IsEncrypt_textNull() ? "" : pTargetsTab->GetEncrypt_text();
		data.m_lEncryptChoice = pTargetsTab->IsEncrypt_choiceNull() ? 0 : pTargetsTab->GetEncrypt_choice();
		data.m_stDriver = pTargetsTab->IsDriverNull() ? "" : pTargetsTab->GetDriver();
		data.m_lPort = pTargetsTab->IsPortNull() ? -1 : pTargetsTab->GetPort();
		data.m_iPassiveMode = pTargetsTab->IsPassive_ModeNull() ? 0 : pTargetsTab->GetPassive_Mode();
		data.m_stDBName = pTargetsTab->IsDBNameNull() ? "" : pTargetsTab->GetDBName();
		data.m_stConnectString = pTargetsTab->IsConnectStringNull() ? "" : pTargetsTab->GetConnectString();
		data.m_lSecured = pTargetsTab->IsSecuredNull() ? 0 : pTargetsTab->GetSecured();
		data.m_stHttpUrl = pTargetsTab->IsHTTPURLNull() ? "" : pTargetsTab->GetHTTPURL();
		data.m_stCertificateName = pTargetsTab->IsCertIFicateNameNull() ? "" : pTargetsTab->GetCertIFicateName();
		data.m_stOptionalConnectString = pTargetsTab->IsOptional_ConnectStringNull() ? "" : pTargetsTab->GetOptional_ConnectString();
		data.m_stODBC_UNC_UserID = pTargetsTab->IsODBC_UNC_UserIDNull() ? "" : pTargetsTab->GetODBC_UNC_UserID();
		data.m_stODBC_UNC_Password = pTargetsTab->IsODBC_UNC_PasswordNull() ? "" : pTargetsTab->GetODBC_UNC_Password();
		data.m_iHttpVerb = pTargetsTab->IsHttpVerbNull() ? static_cast<int>(HttpPost) : pTargetsTab->GetHttpVerb();

		                        
		TargetDataLocation::TargetDataStruct target_data;
		target_data.m_lSmtpMessage = pTargetsTab->IsSMTP_MessageNull() ? -1 : pTargetsTab->GetSMTP_Message();
		target_data.m_lTargetId = lTargetId;
		target_data.m_sTargetName = data.m_stName;

		                         
		target_data.m_lTarget_TargetId = pTargetsTab->IsTarget_Target_IDNull() ? -1 : pTargetsTab->GetTarget_Target_ID();
		if ( type == HttpSourceTypeId &&
			target_data.m_lTarget_TargetId > 0 &&
			lTargetId != target_data.m_lTarget_TargetId )
		{
			HttpTarget* httpTarget = dynamic_cast<HttpTarget*>(pTargetDataLocation.get());
			if ( httpTarget != 0 ) {
				httpTarget->setResponseTarget( getTargetLocation(
					target_data.m_lTarget_TargetId, dbConnection, dataElementHolder, pPluginSelector, 0) );
			}
		}

		                                                                                  
		DataLocation* const pDL = dynamic_cast<DataLocation*>(pTargetDataLocation.get());
		if ( pDL != 0 ) {
			pDL->m_pDataElementHolder = &dataElementHolder;
			pDL->m_CommonData = data;

			                       
			ItemProperties itemProps(&dbConnection);
			itemProps.GetItemProperties(TargetEntityId, lTargetId, pDL->m_extendedProperties);
		}

		pTargetDataLocation->m_TargetData = target_data;
		pTargetDataLocation->m_pPluginSelector = pPluginSelector;
	}
	catch( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to get the Target (location) with TargetID = " << lTargetId << "." << endl;
		sErr << "A database exception occurred. The reported error was:" << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str());
	}
	return pTargetDataLocation.release();
}

SourceDataStructure* LocationCreatorDb::getSourceStructure(TranDb::TRANSFORMATIONSTAB_V* pTranTab,
														   DbConnection& dbConnection,
														   OperationDataElementHolder& dataElementHolder,
														   PipelinePluginSelector* pPluginSelector)
{
	const int type = pTranTab->GetSource_Type_ID();
	switch (type) {
	case TextTypeId:
		{
		auto_ptr<SourceTextDataStructure> pText( new SourceTextDataStructure() );
		pText->m_stTextDoc = pTranTab->IsSource_Text_DocNull() ? "" : pTranTab->GetSource_Text_Doc();
		pText->m_stStructureFile = pTranTab->IsSource_XMLNull() ? "" : pTranTab->GetSource_XML();                                                                          
		return pText.release();
		}
	case DbTypeId:
		{
		auto_ptr<SourceDatabaseDataStructure> pDb(new SourceDatabaseDataStructure);
		string stSourceId = pTranTab->IsSource_SourceIDNull() ? "" : pTranTab->GetSource_SourceID();                                                                       
		if ( stSourceId.empty() ) {
			stringstream sErr;
			sErr << "Failed to get the source location for database source of transformation with TransformationID = " << pTranTab->GetTransformation_ID() << ". The database entry contains an empty string.";
			throw OperationEngineException(sErr.str());
		}
		long lSourceId = atol(stSourceId.c_str());

		                                                                                                                   
		SourceDataLocation* pSourceLoc = getSourceLocation(lSourceId, dbConnection, dataElementHolder, pPluginSelector, false);
		auto_ptr<SourceDataLocation> adopt_pSourceLoc(pSourceLoc);	                                                                           
		DatabaseSourceLocation* pDbSourceLoc = dynamic_cast<DatabaseSourceLocation*>( pSourceLoc );
		if ( pDbSourceLoc == 0 ) {
			stringstream sErr;
			sErr << "Failed to get the source location for database source of transformation with TransformationID = " << pTranTab->GetTransformation_ID() << ". The source location with SourceID = " << lSourceId << " is not a database source.";
			throw OperationEngineException(sErr.str());
		}
		adopt_pSourceLoc.release();                                              
		pDb->m_pDatabaseSourceLocation.reset(pDbSourceLoc);

		pDb->m_stScm = pTranTab->IsSource_XMLNull() ? "" : pTranTab->GetSource_XML();	                                                                                                       
		pDb->m_stTables = pTranTab->IsSource_TablesNull() ? "" : pTranTab->GetSource_Tables();
		pDb->m_stSQL = pTranTab->IsSource_SQLNull() ? "" : pTranTab->GetSource_SQL();
		string stNatureOfSource =  pTranTab->IsNatureOfSourceNull() ? "Flat" : pTranTab->GetNatureOfSource();
		if ( strcasecmp(stNatureOfSource.c_str(), "Tree") == 0 ) {
			pDb->m_Nature = NATURE_TREE;
		}
		else {
			pDb->m_Nature = NATURE_FLAT;
		}
		pDb->m_stWhere = pTranTab->IsSource_WhereNull() ? "" : pTranTab->GetSource_Where();
		pDb->m_stDiffKeys = pTranTab->IsSource_DiffKeysNull() ? "" : pTranTab->GetSource_DiffKeys();

		return pDb.release();
		}
	case XmlTypeId:
	case WebServiceInputTypeId:
	case WebServiceOutputTypeId:
	case JsonTypeId:
		{
		auto_ptr<SourceXmlDataStructure> pXml;
		switch(type) {
		case XmlTypeId:
		case JsonTypeId:
			pXml.reset( new SourceXmlDataStructure );
			break;
		case WebServiceInputTypeId:
			pXml.reset( new SourceWebServiceInputDataStructure );
			break;
		case WebServiceOutputTypeId:
			pXml.reset( new SourceWebServiceOutputDataStructure );
			break;
		default:                                            
			throw OperationEngineException("Should not come here... bug");
		}

		pXml->m_stSchema = pTranTab->GetSource_XML();
		pXml->m_stRoot = pTranTab->GetSourceDTD_Root();
		pXml->m_lTransformationId = pTranTab->GetTransformation_ID();

		return pXml.release();
		}
	case ComplexTextTypeId:
		return new ComplexTextDataStructure;
	case NoSourceTypeId:
		return new NoDataStructure;
	case LdapTypeId:
		return new DefaultDataStructure;
	default:
		break;
	};
	                             
	throw OperationEngineException("Internal error: The source data structure type " + toString(type)
		+ " has not been implemented. This is definately a bug!!");
}

TargetDataStructure* LocationCreatorDb::getTargetStructure(TranDb::TRANSFORMATIONSTAB_V* pTranTab,
																  DbConnection& dbConnection,
																  OperationDataElementHolder& dataElementHolder,
																  PipelinePluginSelector* pPluginSelector)
{
	const int type = pTranTab->GetTarget_Type_ID();
	switch( type ) {
	case TextTypeId:
		{
		auto_ptr<TargetTextDataStructure> pText( new TargetTextDataStructure );
		pText->m_stTextDoc = pTranTab->GetTarget_Text_Doc();
		pText->m_stStructureFile = pTranTab->GetTarget_XML();

		return pText.release();
		}
	case DbTypeId:
		{
		auto_ptr<TargetDatabaseDataStructure> pStructure( new TargetDatabaseDataStructure );
		string stTargetID = pTranTab->GetTarget_TargetID();
		if ( stTargetID.empty() ) {
			stringstream sErr;
			sErr << "Failed to get the target location for database target of transformation with TransformationID = " << pTranTab->GetTransformation_ID() << ". The database entry contains an empty string.";
			throw OperationEngineException(sErr.str());
		}
		long lTargetID = atol(stTargetID.c_str());
		TargetDataLocation* pTargetLocation = getTargetLocation(lTargetID, dbConnection, dataElementHolder, pPluginSelector, 0);
		auto_ptr<TargetDataLocation> adopt_pDataLocation(pTargetLocation);	                                                            
		DatabaseTargetLocation* pDbTargetLoc = dynamic_cast<DatabaseTargetLocation*>(pTargetLocation);
		if ( pDbTargetLoc == 0 ) {
			stringstream sErr;
			sErr << "Failed to get the target location for database target of transformation with TransformationID = " << pTranTab->GetTransformation_ID() << ". The target location with TargetID = " << lTargetID << " is not a database target.";
			throw OperationEngineException(sErr.str());
		}

		adopt_pDataLocation.release();	                                    
		pStructure->m_pDatabaseTargetLocation.reset(pDbTargetLoc);

		pStructure->m_stScm = pTranTab->IsTarget_XMLNull() ? "" : pTranTab->GetTarget_XML();
		pStructure->m_stTables = pTranTab->IsTarget_TablesNull() ? "" : pTranTab->GetTarget_Tables();
		pStructure->m_stUpdateFields = pTranTab->IsUpdateFieldsNull() ? "" : pTranTab->GetUpdateFields();
		pStructure->m_stSQL = pTranTab->IsTarget_SQLNull() ? "" : pTranTab->GetTarget_SQL();
		string stNatureOfTarget = pTranTab->IsNatureOfTargetNull() ? "Flat" : pTranTab->GetNatureOfTarget();
		if ( strcasecmp(stNatureOfTarget.c_str(), "Tree") == 0 ) {
			pStructure->m_Nature = NATURE_TREE;
		}
		else {
			pStructure->m_Nature = NATURE_FLAT;
		}

		pStructure->m_iInsertMode = pTranTab->GetInsert_Mode();
		pStructure->m_stUpdateKey = pTranTab->IsTarget_UpdateKeysNull() ? "" : pTranTab->GetTarget_UpdateKeys();

		return pStructure.release();
		}
	case XmlTypeId:
	case WebServiceInputTypeId:
	case WebServiceOutputTypeId:
	case JsonTypeId:
		{
		auto_ptr<TargetXmlDataStructure> pXml;
		switch(type) {
		case XmlTypeId:
		case JsonTypeId:
			pXml.reset( new TargetXmlDataStructure );
			break;
		case WebServiceInputTypeId:
			pXml.reset( new TargetWebServiceInputDataStructure );
			break;
		case WebServiceOutputTypeId:
			pXml.reset( new TargetWebServiceOutputDataStructure );
			break;
		default:
			throw OperationEngineException("Should not come here...bug");
		}

		pXml->m_stSchema = pTranTab->GetTarget_XML();
		pXml->m_stSchemaUrl = pTranTab->IsTarget_DTD_URLNull() ? "" : pTranTab->GetTarget_DTD_URL();
		pXml->m_stRoot = pTranTab->IsTargetDTD_RootNull() ? "" : pTranTab->GetTargetDTD_Root();
		pXml->m_lTransformationId = pTranTab->GetTransformation_ID();

		return pXml.release();
		}
	case NoTargetTypeId:
		return new NoDataStructure;
	case ComplexTextTypeId:
		return new ComplexTextDataStructure;
	case LdapTypeId:
		return new DefaultDataStructure();
	default:
		break;
	};
	                           
	throw OperationEngineException("Internal Error: The target data structure type " + toString(type)
		+ " has not been implemented. This is definately a bug!!");
}

bool LocationCreatorDb::usesPreviousTargetAsSource(long lOperationId, DbConnection& dbConnection) {

	SmartDbPtr<TranDb::OperationsTab> pOperationsTab(
		TranDb::OperationsTab::MakeInstance(&dbConnection));
	pOperationsTab->Select("WHERE Operation_ID = " + toString(lOperationId));
	if ( !pOperationsTab->Next() )
		throw OperationEngineException("Internal Error: There is no operation with OperationID = " + toString(lOperationId)
		+ ". Please report this bug!");

	if ( pOperationsTab->IsSource_IDNull() )
		return false;
	return ( pOperationsTab->GetSource_ID() == PreviousTargetAsSource::SOURCE_ID );
}
