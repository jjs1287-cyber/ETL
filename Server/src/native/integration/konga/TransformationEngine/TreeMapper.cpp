/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include <sstream>
#include <string>
#include <cassert>
#include <exception>

#include <konga/Utils/PortDefs.h>
#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/util.h>		                       
#include <konga/Utils/ErrorResolver.h>
#include <konga/Xml/XmlSchemaValidator.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/Soap/SoapFault.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include <konga/JdbcUtils/ReadSource.h>

#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include <konga/TransformationUtils/Definitions.h>
#include <konga/TransformationUtils/DbLookupCache.h>
#include <konga/TransformationUtils/TranDBUtils.h>
#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include <konga/Server/WDIPathFinder.h>

#include "ErrorCodes/resource.h"
#include "CROMData.h"
#include "CromToJtrWriter.h"
#include "RaiseErrorException.h"
#include "ProfilingSections.h"
#include "TransacParser.h"
#include "TransFilterTab.h"
#include "Transform.h"
#include "Normalization.h"
#include "TextDocument.h"
#include "CombineFiles.h"
#include "TreeMapper.h"
#include "WebServiceHandler.h"


using namespace std;
using namespace konga;

namespace {
	const string AUTO_STREAMING_CONF_FILE("AutoStreaming");
	const string AUTO_STREAMING_DE_NAME("etlforge.transformation.auto_streaming");
	const string JBXMLPARSER_DE_NAME("etlforge.transformation.jbxmlparser");
	const string SOURCE_PRESERVE_WS("etlforge.source.preserve_char_whitespace");
	const string SOURCE_DB_PRESERVE_WS("etlforge.source.db.preserve_char_whitespace");
	const string TARGET_INCLUDE_NULL_XML("etlforge.target.xml.include_null_xml");
	const string TARGET_INCLUDE_EMPTY_XML("etlforge.target.xml.include_empty_xml");
	const string XML_PRETTIFY("etlforge.target.xml.prettify");

	bool findUnreadElements(CROM* node, CROM* transac, std::set<CROM*>& sourceCroms){
		std::set<CROM*>::iterator it=sourceCroms.find(node);
		if(it!=sourceCroms.end()){
			sourceCroms.erase(it);
		}
		for(CROM*child=node->GetFirstChild(); child; child=child->GetNextSibling()){
			if(child->IsLeaf())continue;
			if(findUnreadElements(child, transac, sourceCroms))return true;
		}
		return node==transac;
	}
}



TreeMapper::TreeMapper(TransformationEngine*pTransformationEngine, konga::TransformationDataHolder* pTransformationDataHolder,
					   bool bLoadSourceData, bool bTestOperation, bool bTestTransformation, bool bTrace) :
	m_pTransformationEngine(pTransformationEngine),
	m_pTransformationDataHolder(pTransformationDataHolder),
	m_bLoadSourceData(bLoadSourceData),
	m_bTestOperation(bTestOperation),
	m_bTestTransformation(bTestTransformation),
	m_bStreamingTransformation(false),
	m_bAutoMappingForXmlNil(false),
	m_TargetWriteMode(pTransformationEngine->getTargetWriteMode()),
	m_bNumForBool(false),
	m_TransformationID(-1), 
	m_TransformationWebServiceInfo(0),
	m_webServiceStructure(0),
	m_XmlSource(false), m_XmlTarget(false),
	m_sourceType(UnknownStructureType), m_targetType(UnknownStructureType),
	m_source_text_doc_id(0), m_target_text_doc_id(0),
	m_bSourceIsEmpty(false),
	m_bMappingPresent(true),
	m_bSourceIsDatabase(false),
	m_bModifyDBSourceSQL(false),
	m_bTrace(bTrace),
	m_iMode(-1),
	m_pSourceDbConnectionInfo(0), m_pTargetDbConnectionInfo(0),
	m_dwSrcTimeout(180), m_dwTgtTimeout(180),
	m_Function_ID(-1),
	m_NextTransformation_ID(-1),
	m_PreviousTransformation_ID(-1),
	m_bHostedWebServiceOperation(false),
	m_bNoSourceInMapping(false),
	m_pSoapFault(0),
	m_sourceXmlParser("TreeMapper SourceParser"),
	m_targetXmlParser("TreeMapper TargetParser"),
	m_pSourceDataRoot(0),
	m_bForbidStreaming(false),
	m_lTxtSrcSize(0), m_lTxtTgtSize(0),
	m_lTxtSrcRecords(0), m_lTxtTgtRecords(0),
	m_bErrorsInOutput(false),
	m_bIsTransformationValid (true),
	m_bDoXmlValidationRegistryFlag( getXmlValidationRegistryFlag() ),
	m_bDoSourceXmlValidation(true),
	m_bDoTargetXmlValidation(true),
	m_bTruncateDbCharTargets(false),
	m_pLdapFilterCollection(0),
	m_iThread(0),
	m_iChunk(0),
	m_nChunkSize(0),
	m_bChunking(false),
	m_bIsClone(false),
	m_bUseOldRuleForLoopOneOfXmlNode(false),
	m_bWriteLog(pTransformationEngine->m_bWriteLog),
	m_bQualifierRequired(false),
	m_bEdifact(false),
	m_debug_log(0),
	m_source_transac_crom(0),
	m_target_transac_crom(0),
	m_pProfilingKit(pTransformationEngine->m_pProfilingKit),
	m_bGeneratorDefined(false),
	m_pTextDocument(0),
	m_pXmlDocument(0),
	m_pNormalization(0),
	m_SourceBatchSize(0),
	m_DoBatchTarget(false),m_bUserETLForgeXmlParser(false),
	m_nextTransformation(0)

{
	assert(m_pTransformationEngine);
	assert(m_pTransformationDataHolder);
	m_bNumForBool=konga::GetSystemVariableAsBool("etlforge.target.xml.num_for_bool",pTransformationDataHolder->getOperationDataElementHolder(), true);
	                          
		m_sourceDTDParser=new DTDParser(this);
	m_sourceDTDParser->SetTarget(false);
	                          
		m_targetDTDParser=new DTDParser(this);
	m_targetDTDParser->SetTarget(true);
	m_sourceXmlParser.setTreeMapper(this, false);
	m_targetXmlParser.setTreeMapper(this, true);
	getViewLog()->SetLogFileName(m_pTransformationEngine->m_sErrLogFile);
	m_bAutoMappingForXmlNil=konga::GetOptionAsBoolean("TransformationEngine", "AutoXsiNil",false);
	m_bIncludeNullXmlElement=konga::GetSystemVariableAsBool(TARGET_INCLUDE_NULL_XML, getOperationDataElementHolder(), true);
	m_bIncludeEmptyXmlElement=konga::GetSystemVariableAsBool(TARGET_INCLUDE_EMPTY_XML, getOperationDataElementHolder(), false);
}

                                      
                                                                                       
TreeMapper::TreeMapper(const TreeMapper& old, konga::TransformationDataHolder*pTransformationDataHolder) :
	m_pTransformationEngine(old.m_pTransformationEngine),
	m_pTransformationDataHolder(pTransformationDataHolder),
	m_bLoadSourceData(old.m_bLoadSourceData),
	m_bTestOperation(old.m_bTestOperation),
	m_bTestTransformation(old.m_bTestTransformation),
	m_bStreamingTransformation(old.m_bStreamingTransformation),
	m_bAutoMappingForXmlNil(old.m_bAutoMappingForXmlNil),
	m_TargetWriteMode(old.m_TargetWriteMode),
	m_bNumForBool(old.m_bNumForBool),
	m_TransformationID(old.m_TransformationID),
	m_TransformationWebServiceInfo(0),
	m_webServiceStructure(0),
	m_XmlSource(old.m_XmlSource),
	m_XmlTarget(old.m_XmlTarget),
	m_sourceType(old.m_sourceType),
	m_targetType(old.m_targetType),
	m_source_text_doc_id(old.m_source_text_doc_id), m_target_text_doc_id(old.m_target_text_doc_id),
	m_bSourceIsEmpty(old.m_bSourceIsEmpty),
	m_bMappingPresent(old.m_bMappingPresent),
	m_bSourceIsDatabase(old.m_bSourceIsDatabase),
	m_bModifyDBSourceSQL(old.m_bModifyDBSourceSQL),
	m_bTrace(old.m_bTrace),
	m_iMode(old.m_iMode),
	m_pSourceDbConnectionInfo(0), m_pTargetDbConnectionInfo(0),
	m_dwSrcTimeout(old.m_dwSrcTimeout),
	m_dwTgtTimeout(old.m_dwTgtTimeout),
	m_Function_ID(old.m_Function_ID),
	m_NextTransformation_ID(old.m_NextTransformation_ID),
	m_PreviousTransformation_ID(old.m_PreviousTransformation_ID),
	m_inputDataFileName(old.m_inputDataFileName),	                                                                      
	m_bHostedWebServiceOperation(old.m_bHostedWebServiceOperation),
	m_bNoSourceInMapping(old.m_bNoSourceInMapping),
	m_bDoXmlValidationRegistryFlag( old.m_bDoXmlValidationRegistryFlag ),
	m_bDoSourceXmlValidation(old.m_bDoSourceXmlValidation),
	m_bDoTargetXmlValidation(old.m_bDoTargetXmlValidation),
	m_bTruncateDbCharTargets(old.m_bTruncateDbCharTargets),
	m_pSoapFault(0),
	m_sourceXmlParser("TreeMapper SourceParser"),
	m_targetXmlParser("TreeMapper TargetParser"),
	m_pSourceDataRoot(0),
	m_bForbidStreaming(false),
	m_lTxtSrcSize(0), m_lTxtTgtSize(0),
	m_lTxtSrcRecords(0), m_lTxtTgtRecords(0),
	m_bErrorsInOutput(false),
	m_bIsTransformationValid (true),
	m_pLdapFilterCollection(0),
	m_iThread(0),
	m_iChunk(0),
	m_nChunkSize(0),
	m_bChunking(false),
	m_bIsClone(true),
	m_bUseOldRuleForLoopOneOfXmlNode(false),
	m_bWriteLog(old.m_bWriteLog),
	m_bQualifierRequired(old.m_bQualifierRequired),
	m_bEdifact(old.m_bEdifact),
	m_debug_log(0),
	m_target_transac_crom(old.m_target_transac_crom),
	m_source_transac_crom(old.m_source_transac_crom),
	m_pProfilingKit(old.m_pProfilingKit),
	m_bGeneratorDefined(old.m_bGeneratorDefined),
	m_pTextDocument(0),
	m_pXmlDocument(0),
	m_pNormalization(0),
	m_SourceBatchSize(0),
	m_DoBatchTarget(false),m_bUserETLForgeXmlParser(old.m_bUserETLForgeXmlParser),
	m_nextTransformation(0)
	{
	                         
	if ( old.m_pSourceDbConnectionInfo ) {
		this->m_pSourceDbConnectionInfo = new DbConnectionInfoStruct(*old.m_pSourceDbConnectionInfo);
	}
	if ( old.m_pTargetDbConnectionInfo ) {
		this->m_pTargetDbConnectionInfo = new DbConnectionInfoStruct(*old.m_pTargetDbConnectionInfo);
	}
	if ( old.m_TransformationWebServiceInfo ) {
		this->m_TransformationWebServiceInfo = new konga::TransformationWebServiceInfo(*old.m_TransformationWebServiceInfo);
	}

	                          
		m_sourceDTDParser=new DTDParser(this);
	m_sourceDTDParser->SetTarget(false);
	                          
		m_targetDTDParser=new DTDParser(this);
	m_targetDTDParser->SetTarget(true);

	m_sourceXmlParser.setTreeMapper(this, true);
	m_targetXmlParser.setTreeMapper(this, false);
	getViewLog()->SetLogFileName(m_pTransformationEngine->m_sErrLogFile);
	m_bIncludeNullXmlElement=konga::GetSystemVariableAsBool(TARGET_INCLUDE_NULL_XML, getOperationDataElementHolder(), true);
	m_bIncludeEmptyXmlElement=konga::GetSystemVariableAsBool(TARGET_INCLUDE_EMPTY_XML, getOperationDataElementHolder(), false);
}

TreeMapper::~TreeMapper()
{
	PROFILING doPrf(this, "TreeMapperDtor");
	if ( m_pSourceDbConnectionInfo ) {
		PROFILING doPrf(this, "delete_SourceDbConnectionInfo");
		delete m_pSourceDbConnectionInfo;
		m_pSourceDbConnectionInfo = 0;
	}
	if ( m_pTargetDbConnectionInfo ) {
		PROFILING doPrf(this, "delete_TargetDbConnectionInfo");
		delete m_pTargetDbConnectionInfo;
		m_pTargetDbConnectionInfo = 0;
	}

	this->closeDbLookup();

	if(m_pSoapFault) {
		PROFILING doPrf(this, "delete_SoapFault");
		delete m_pSoapFault;
	}

	if(m_pLdapFilterCollection) {
		PROFILING doPrf(this, "delete_LdapFilterCollection");
		delete m_pLdapFilterCollection;
	}
	if(m_TransformationWebServiceInfo) {
		PROFILING doPrf(this, "delete_TransformationWebServiceInfo");
		delete m_TransformationWebServiceInfo;
	}
	if(m_webServiceStructure) delete m_webServiceStructure;

	if(m_sourceDTDParser) {
		PROFILING doPrf(this, "delete_sourceDTDParser");
		delete m_sourceDTDParser;
	}
	if(m_targetDTDParser) {
		PROFILING doPrf(this, "delete_targetDTDParser");
		delete m_targetDTDParser;
	}
	                                         
	                                                  
	                                                
	   

	if(m_pNormalization) {
		PROFILING doPrf(this, "delete_Normalization");
		delete m_pNormalization;
	}
	if(m_pTextDocument) {
		PROFILING doPrf(this, "delete_TextDocument");
		delete m_pTextDocument;
	}
	if(m_pXmlDocument) {
		PROFILING doPrf(this, "delete_XmlDocument");
		delete m_pXmlDocument;
	}
}

void TreeMapper::DeleteTargetXmlData() {
	m_targetXmlParser.DeleteData();
}

bool TreeMapper::IncludeNullInTargetSQLStatement() const {
	return GetSystemVariableAsBool("etlforge.target.db.include_null_in_sql_statement", getOperationDataElementHolder(), false);
}

unsigned long TreeMapper::InitTransformation(DbConnection& tranDbConnection, long TransformationID, int& nTransformationStatus, bool bTrace)
{
	m_TransformationID = TransformationID;
	getTransformationEngine()->initPROFILING("Transformation", TransformationID);
	PROFILING doPrf(this,INIT_TRAN_PROFILING_SECTION);
	unsigned long hr = S_OK;
	setName("Transformation: TransformationId="+konga::toString(TransformationID));

	assert(m_pTransformationEngine);

	try
	{		
		if( !getViewLog()->IsInitialized() ){
			std::string guid=getOperationContext()->getLocalTmpDirForOperation();
			getViewLog()->Initialize("init_tran", m_TransformationID, m_debug_log, !m_bIsClone, m_bWriteLog&&!m_bIsClone, 0, 0, false, guid.substr(guid.size()-9,8));
		}
		else {
			getViewLog()->setWriteLog(m_bWriteLog);
		}
		if( !getErrorLog()->IsInitialized() ){
			getErrorLog()->SetViewLog(getViewLog());
			getErrorLog()->Initialize();
		}

		WriteTransformationLogHeader("");
		m_bUseOldRuleForLoopOneOfXmlNode=konga::GetOptionAsInt("TransformationEngine","UseOldRuleForLoopOneOfXmlNode")!=0;

		if( FAILED(hr = GetTranDBInfo(&tranDbConnection)) ) return hr;

		if( !m_bIsTransformationValid )
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_MAPBYTRANSFORMATIONID_INVALID_TRANSFORMATION, (const char*)m_sTransformationName);
			m_lastErr=getErrorLog()->GetLastErr();
			return hr;
		}

		HandleNoMappings();

		if( SourceIsDatabase() && !m_bChunking)
		{
			m_bSourceIsDatabase = true;
			m_bModifyDBSourceSQL = konga::ModifyDBSourceSelect(m_sSourceSQL);
		}

		m_XmlSource = (m_sourceType == XmlTypeId) ||
					  (m_sourceType == WebServiceInputTypeId) ||
					  (m_sourceType == WebServiceOutputTypeId) ||
					  (m_sourceType == JsonTypeId);

		m_XmlTarget =  (m_targetType == XmlTypeId) ||
					   (m_targetType == WebServiceInputTypeId) ||
					   (m_targetType == WebServiceOutputTypeId) ||
					   (m_targetType == JsonTypeId);
		bool always_from_structure_file=false;

		const bool initTargetStructure=!isLoadSourceData();

		InitializeStructureParsers(always_from_structure_file, true, initTargetStructure);

                               
                                 
                                                         
                                                     
                                                                                                          
     

		if(initTargetStructure){
			                                                                                          
			bool bFlat2Hierarch=IsFlatSource() && !IsFlatTarget();
			bool bFlat2Flat=IsFlatSource() && IsFlatTarget();

			collectSourceDataElements(m_sourceDTDParser->FindRoot());

			bool bReturn;
			hr=enterMapInfoToStructTree(bFlat2Hierarch, bFlat2Flat, bReturn);

			if(bFlat2Flat){
				CROM*sourceFlatNode=m_sourceDTDParser->m_root->GetFirstChild(), *targetFlatNode=m_targetDTDParser->m_root->GetFirstChild();
				targetFlatNode->m_Generator=sourceFlatNode;
			}
			
			hr = PrepareSpecialSourceTypes(bReturn);
			if( FAILED(hr) ) return hr;
			
			if(bFlat2Hierarch){
				if(m_pNormalization)delete m_pNormalization;
				m_pNormalization=new Normalization(m_sourceDTDParser, m_targetDTDParser->m_root);
			}
		}
		
		return hr;
	}
	catch( const RaiseErrorException& e ) {
		AppendErrorMessage(e.GetError());
		hr = E_FAIL;
	}
	catch(const CrFatalException& e)
	{
		std::string stErrMsg = e.GetErrorString();
		KString csErr = "\nAn error was thrown when writing to the disk. Reported error: ";
		csErr += stErrMsg.c_str();
		AppendErrorMessage(csErr);
		hr = E_FAIL;
	}
	catch(const CrException& e)
	{
		std::string stErrMsg(e.GetErrorString());
		AppendErrorMessage(stErrMsg.c_str());
		hr = E_FAIL;
	}
	catch( const std::exception& e ) {
		std::string stErrMsg( e.what() );
		AppendErrorMessage(stErrMsg.c_str());
		hr = E_FAIL;
	}
#ifndef _DEBUG
	catch(...)
	{
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_UNKNOWN_ERROR_CAUGHT, (const char*)m_lastErr);
		AppendErrorMessage( getErrorLog()->GetLastErr() );
	}
#endif

	nTransformationStatus = hr;
    return hr;
}

namespace {
	const string TRANSFORMATION_NAME_DE("etlforge.transformation.name");
}

void TreeMapper::doPreTransformationProcessing() {
	                                                                             
	if( GetTargetType()==TextTypeId || GetTargetType()==ComplexTextTypeId ) {
		m_bQualifierRequired=konga::GetSystemVariableAsBool("etlforge.text.qualifier_required", getOperationDataElementHolder(), m_bQualifierRequired);
	}

	                                                           
	DataElement* const de = getOperationDataElementHolder()->getOperationDataElement(TRANSFORMATION_NAME_DE);
	de->setType(DataElement::t_string);
	de->setValue(getTransformationName().c_str());
}

void TreeMapper::validateSource(const string& stInputDataFileName, bool m_bUserETLForgeXmlParser){
	if (!IsXmlSource()) return ;
	if(IsNoSource() && !isTestOperation())return;
	const string& stSourceXMLFileName=stInputDataFileName;
	unsigned long hr=S_OK;
	bool bValidateSourceXml = GetValidateSourceXml();
	if (!m_bUserETLForgeXmlParser) {
		const string validatemessage=bValidateSourceXml ? " with validation " :" without validation ";
		PROFILING doPrf(this,"DOM Parser reads source File=" + stSourceXMLFileName + validatemessage);
		m_sourceXmlParser.DeleteData();
		if(m_sourceXmlParser.Read(m_TransformationID, true, stSourceXMLFileName.c_str(), (const char*)m_inputStructFileName, (const char*)m_sourceDTD_Root, bValidateSourceXml)!=S_OK)
		{
			string sErr = (const char*)m_sourceXmlParser.m_LastErr;
			AppendErrorMessage(sErr);
			getViewLog()->DebugString("%s", sErr.c_str());
			                                                                      
			if( IsHostedWebServiceOperation() )
			{
				if( !m_sourceXmlParser.IsValidXml() )
				{
					                                                                      
					string stFaultString;
					if( m_sourceType == WebServiceInputTypeId )
						stFaultString = "The webservice input is invalid according to the structure defined in the WSDL file.";
					else
						stFaultString = "An intermediate state in the webservice processing has produced XML that is invalid according to the specified DTD/schema.";
					m_pSoapFault = new SoapFault_11(FaultCodeValue_11::Client, stFaultString, 
													"", m_sourceXmlParser.GetValidationError());
				}
			}
			throw konga::CrException(sErr);
		}
	} else {

		if(bValidateSourceXml)
		{
			PROFILING doPrf(this,"validate source File=" + stSourceXMLFileName );
			CRXmlParser testXMLFile;
			testXMLFile.setTreeMapper(this, false);
			testXMLFile.SetDocType(m_sourceXmlParser.GetDocType());

			if(testXMLFile.ValidateTargetXml(stSourceXMLFileName.c_str(), (const char*)m_inputStructFileName, m_TransformationID) != S_OK)
			{
				testXMLFile.m_pDTDParser = 0;
				KString csTmp = testXMLFile.IsDTDType() ? "DTD" : "schema";
				hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_XMLXMLWRITER_VALIDATE_SOURCE_FAILED, 
					(const char*)csTmp, (const char*)testXMLFile.m_LastErr);
				KString sErr(getErrorLog()->GetLastErr());

				                                                                      
				if( IsHostedWebServiceOperation() )
				{
					std::string stFaultString;
					if( m_sourceType == WebServiceInputTypeId )
						stFaultString = "The requesting webservice input is invalid according to the structure defined in the WSDL file.";
					else
						stFaultString = "An intermediate state in the webservice processing has produced XML that is invalid according to the specified DTD/schema.";
					m_pSoapFault = new SoapFault_11(FaultCodeValue_11::Client, stFaultString, 
													"", testXMLFile.GetValidationError());
				}

				throw konga::CrException((const char*)sErr);
			}
			testXMLFile.m_pDTDParser = 0;
		}

	}

	return ;
}
void TreeMapper::validateTarget(const string& stOutputDataFileName){
	if (!IsXmlTarget()) return ;
	const char* targetXMLFileName=stOutputDataFileName.c_str();
	unsigned long hr=S_OK;

	if( GetValidateTargetXml() )
	{
		PROFILING doPrf(this,"validate target File=" + stOutputDataFileName );
		CRXmlParser testXMLFile;
		testXMLFile.setTreeMapper(this, true);
		testXMLFile.SetDocType(m_targetXmlParser.GetDocType());

		if (testXMLFile.ValidateTargetXml(targetXMLFileName, m_outputStructFileName, m_TransformationID) != S_OK)
		{
			testXMLFile.m_pDTDParser = 0;
			KString csTmp = testXMLFile.IsDTDType() ? "DTD" : "schema";
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_XMLXMLWRITER_VALIDATE_TARGET_FAILED, 
				(const char*)csTmp, (const char*)testXMLFile.m_LastErr);
			KString sErr(getErrorLog()->GetLastErr());

			                                                                      
			if( IsHostedWebServiceOperation() )
			{
				std::string stFaultString;
				if( m_targetType == WebServiceOutputTypeId )
					stFaultString = "The resulting webservice output is invalid according to the structure defined in the WSDL file.";
				else
					stFaultString = "An intermediate state in the webservice processing has produced XML that is invalid according to the specified DTD/schema.";
				m_pSoapFault = new SoapFault_11(FaultCodeValue_11::Client, stFaultString, 
												"", testXMLFile.GetValidationError());
			}

			throw konga::CrException((const char*)sErr);
		}
		testXMLFile.m_pDTDParser = 0;
	}
	return ;
}

unsigned long TreeMapper::doTransformation(const string& stInputDataFileName,
										   const string& stOutputDataFileName,
										   const string& stLinkDataFileName,
										   int& nTransformationStatus,
										   TransformationStatistics& statistics)
{
	PROFILING doPrf(this, "doTransformation", "Name=\"" + html_string(getTransformationName()) + "\" Id=\"" + konga::toString(m_TransformationID) + "\"");
	const clock_t clkStart = clock();

	unsigned long hr=S_OK;
	try
	{
		if ( isTestTransformation() && !isTestOperation() && !stInputDataFileName.empty() && konga::FileExists(stInputDataFileName) ) {
			PROFILING doPrf(this, READ_DATABASE_SOURCE_REMOVE_NULLS_PROFILING_SECTION);
			if(konga::removeNullCharsFromFile(stInputDataFileName)){
				getViewLog()->LogMessage("Some zero byte characters were removed from the source.");
			}
		}

		Debuggee* pDebuggee = getOperationContext()->getDebuggee();
		if ( pDebuggee ) {
			pDebuggee->getCallStack().push(
				new IntegrationBreakpointLocation(-1, m_TransformationID, -1, -1, "")
				);
		}
		AutoPopCallStack pop_stack(pDebuggee);

		if( !getViewLog()->IsInitialized() ){
			std::string guid=getOperationContext()->getLocalTmpDirForOperation();
			getViewLog()->Initialize("do_tran", m_TransformationID, m_debug_log, true, m_bWriteLog, 0, 0, false, guid.substr(guid.size()-9,8));
		}
		else {
			getViewLog()->setWriteLog(m_bWriteLog);
		}
		if( !getErrorLog()->IsInitialized() ){
			getErrorLog()->SetViewLog(getViewLog());
			getErrorLog()->Initialize();
		}

		m_outputDataFileName = stOutputDataFileName;
		if(m_sourceType==NoSourceTypeId)m_bSourceIsEmpty=true;

		                                                                                          
		                                                                       
		WriteTransformationLogHeader(stInputDataFileName);
		
		doPreTransformationProcessing();

		FILE*lnk=fopen(stLinkDataFileName.c_str(), "w+");
		if(lnk==0){
			const long lErr = GetLastError();
			throw konga::CrException("Failed to create link file\"" + stLinkDataFileName + "\": " + ErrorResolver().Resolve(lErr) );
		}
		konga::AutoFileHandle adopt_link(lnk);

		m_bUserETLForgeXmlParser=useETLForgeXmlParser();
		m_bStreamingTransformation=isStreamingTransformationApplicable();
		validateSource(stInputDataFileName, m_bUserETLForgeXmlParser || m_bStreamingTransformation);
		if(m_bStreamingTransformation){
			if(m_outputDataFileName.IsEmpty()){
				throw konga::CrException("m_outputDataFileName is empty");
			}
			hr = DoStreamingTransform(lnk, stInputDataFileName);
		}
		else {
			if(m_pNormalization){
				CROMData*sourceDataRoot=get_SourceDataRoot();
				m_lTxtSrcRecords = m_pNormalization->createNormalizedDataTree(sourceDataRoot, stInputDataFileName, m_lTxtSrcSize);
			}
			else {
				if(m_XmlSource){
					
					if(m_bUserETLForgeXmlParser){
						CreateXmlSourceTree_JbParser(stInputDataFileName);
					}
					else {
						CreateXmlSourceTree(stInputDataFileName);
						set_SourceDataRoot();
					}
					if(m_pSourceDataRoot) {
						((CRXmlData*)m_pSourceDataRoot)->writeDebugDataTree(0,"");
					}
				}
				else {
					m_lTxtSrcRecords = CreateNonXmlSourceTree(stInputDataFileName);
				}
				if (!m_bMappingPresent) {
					return S_OK;
				}
			}
			if(m_XmlTarget){
				XmlTargetWriter(m_outputDataFileName, lnk);
			}
			else {
				HfTargetWriter(m_outputDataFileName, lnk);
			}
		}
		validateTarget((const char*) m_outputDataFileName);
		if ( !isTestTransformation() || isTestOperation()) {
			HandleSpecialTargetTypes();
		}
	}
	catch( const RaiseErrorException& e ) {
		AppendErrorMessage(e.GetError());
		hr = E_FAIL;
	}
	catch( const CrException& e ) {
		AppendErrorMessage(e.GetErrorString());
		hr = E_FAIL;
	}
	catch( const std::bad_alloc& e ) {
		AppendErrorMessage( "Out of memory exception. " + string(e.what()) );
		hr = E_FAIL;
	}
	catch( const std::exception& e ) {
		AppendErrorMessage(e.what());
		hr = E_FAIL;
	}
#ifndef _DEBUG
	catch(...) {
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_UNKNOWN_ERROR_CAUGHT, (const char*)m_lastErr);
		AppendErrorMessage( getErrorLog()->GetLastErr() );
		if ( konga::shouldThrowOnError() ) throw;
	}
#endif

	SetTransformationResultInfo(statistics, stInputDataFileName, clkStart);
	OnMapByTransformationExit(hr);
	DeleteData();

	nTransformationStatus = hr;
    return hr;
}

CROM* TreeMapper::getNormalizationFlatSourceTable() {return m_pNormalization->getFlatSourceTable();}


   
                                                                                                                          
                                                                                         
   
CROMData* TreeMapper::get_SourceDataRoot()
{
	if(m_XmlSource)throw konga::CrException("Internal error, the source is XML and TreeMapper::get_SourceDataRoot() can be called only for non XML source.");
	if(!m_pSourceDataRoot){
		m_pTextDocument=new TextDocument();
		CROMData*dataRoot=new CROMData(0, 0, m_sourceDTDParser->m_root, 0);
		m_pSourceDataRoot=dataRoot;
		                                          
		m_pTextDocument->setCROMDataRoot(dataRoot);
	}
	return (CROMData*)m_pSourceDataRoot;
}
void TreeMapper::set_SourceDataRoot()
{
	if(!m_XmlSource)throw konga::CrException("Internal error, the source is non XML and TreeMapper::set_SourceDataRoot() can be called only for XML source.");
	m_pSourceDataRoot=m_sourceXmlParser.m_rootXmlData;
}

bool TreeMapper::isQualifierRequired() const {
	return m_bQualifierRequired;
}

bool TreeMapper::isEDIFACT() const {
	return m_bEdifact;
}

std::string TreeMapper::getDefaultFileName(){

	std::string sourceFN(m_inputDataFileName);
	if(sourceFN.empty()){
		sourceFN=getOperationContext()->getLocalTmpDirForOperation();
	}
	if(m_iChunk==0){
		return sourceFN+"_t"+konga::toString(getTransformationID());
	}
	else {
		return sourceFN+"_t"+konga::toString(getTransformationID())+"_"+konga::toString(m_iChunk);
	}
}

std::string TreeMapper::getSourceUpdateDataFileName()
{
	return getDefaultFileName()+".srcupd";
}
std::string TreeMapper::getSourceDataFileName()
{
	return getDefaultFileName()+".source";
}
std::string TreeMapper::getTargetDataFileName()
{
	return getDefaultFileName()+".target";
}

std::string TreeMapper::getLinkFileName()
{
	return getDefaultFileName()+".link";
}


std::string TreeMapper::getTempSourceJtrFileName()
{
	return getOperationContext()->getLocalTmpDirForOperation()+getTransformationName30()+konga::toString(m_iThread)+"-input.jtr";
}

std::string TreeMapper::getTempTargetJtrFileName()
{
	return getOperationContext()->getLocalTmpDirForOperation()+getTransformationName30()+konga::toString(m_iThread)+"-output.jtr";
}

std::string TreeMapper::getAuxSourceStructFileName()
{
	return getOperationContext()->getLocalTmpDirForOperation()+getTransformationName30()+konga::toString(m_iThread)+".inaux";
}

std::string TreeMapper::getAuxTargetStructFileName()
{
	return getOperationContext()->getLocalTmpDirForOperation()+getTransformationName30()+konga::toString(m_iThread)+".outaux";
}

std::string TreeMapper::getDebugFileName(){
	std::string WDIDebugPath;
	WDIPathFinder::GetWDIDebugPath(WDIDebugPath);
	WDIDebugPath+=getTransformationName30();
	WDIDebugPath+="_"+konga::GetGUIDString().substr(0,5);

	return WDIDebugPath;
}

std::string TreeMapper::getJdbcSourceLogFileName()
{
	return getDebugFileName()+"-srcJdbc.log";
}

std::string TreeMapper::getJdbcTargetLogFileName()
{
	return getDebugFileName()+"-tgtJdbc.log";
}

std::string TreeMapper::getOdbcSourceLogFileName()
{
	return getDebugFileName()+"-srcOdbc.log";
}

std::string TreeMapper::getOdbcTargetLogFileName()
{
	return getDebugFileName()+"-tgtOdbc.log";
}

std::string TreeMapper::getOdbcUpdateSourceLogFileName()
{
	return getDebugFileName()+"-upSrcOdbc.log";
}

std::string TreeMapper::getJdbcUpdateSourceLogFileName()
{
	return getDebugFileName()+"-upSrcJdbc.log";
}


namespace {
	const char* RESERVED_WORDS[] = {
        "CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
        "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", 0
    };

	const char* ILLEGAL_CHARS = "'\\/:?\"<>'| ";
}

std::string TreeMapper::getTransformationName30() const {
	if(m_sTransformationName.IsEmpty()){
		return "WS_tranid_"+konga::toString(m_TransformationID);
		                                                                                           
	}
	std::string name=(const char*)m_sTransformationName;
	name=konga::TrimCharsFromString(name, " \t");
	if(name.size() > 30)name=name.substr(0,30);
	for(int i=0, n=name.size(); (i=strcspn(name.c_str()+i, ILLEGAL_CHARS)+i)<n; ++i){
		name.replace(i, 1, 1, '_');
	}
	for(int i=0; RESERVED_WORDS[i]!=0; ++i){
		if(stricmp(name.c_str(), RESERVED_WORDS[i])==0){
			name+="_";
			break;
		}
	}
	return name;
}

namespace {
	KString getFlatPath(const KString&path){
		StringTokenizer tk(path,"$.#@");
		KString newPath,tag;
		bool skip=false;
		while(tk.NextToken()){
			const char*token=tk.token();
			switch(token[0]){
			case '$':
				if(!newPath.IsEmpty()){
					skip=false;
					break;
				}
			case '.':
				newPath+=tag+token;
				skip=false;
				break;
			case '#':
			case '@':
				skip=true;
				break;
			default:
				if(!skip)tag=token;
				break;
			}
		}
		return newPath;
	}
}

namespace {
	                                     
	KString removeTableName(KString deName){
		int i=deName.Find('.');
		return deName.Mid(i+1);
	}
};

                                                            
                                                                                
                             
                                                                                      
                                                                                
                                                            
                                                                                    
                       
unsigned long TreeMapper::enterMapInfoToStructTree(bool flatToHf,bool flatToFlat, bool& bReturn) 
{
	PROFILING doPrf(this,"enterMapInfoToStructTree");
	FILE*LOG=getViewLog()->GetDebugLog();
	if(m_sourceDTDParser && !IsNoSource())m_sourceDTDParser->definePath(flatToHf||flatToFlat);
	if(m_targetDTDParser)m_targetDTDParser->definePath(false);
	if(m_maplist.empty())return S_OK;
	m_bForbidStreaming=false;                                  
	for (STRMAP_T::const_iterator it= m_maplist.begin(); it!=m_maplist.end();++it)
	{
		KString targetPath=it->first;
		KString sourceExpr=it->second;
		if(!targetPath.IsEmpty()&&targetPath.GetAt(0)=='$')continue;                                    
		if(LOG){
			fprintf(LOG,"%s -> %s\n", (const char*)targetPath, (const char*)sourceExpr);
			fflush(LOG);
		}
		CROM*source, *target;
		if(targetPath=="[]"){
			target=m_targetDTDParser->FindRoot();
			if(IsFlatTarget() && m_targetType==TextTypeId)target=target->GetFirstChild();
		} else {
			target=m_targetDTDParser->findTarget_expandTreeIfNotExist(targetPath);                                           
			if(target==0){
				stringstream ssErr;
				ssErr << "TreeMapper::enterMapInfoToStructTree - findTarget_expandTreeIfNotExist(" << (const char*)targetPath << ") failed 1" << endl;
				getViewLog()->DebugString(ssErr.str().c_str());
				AppendErrorMessage(ssErr.str().c_str());
				throw CrException (ssErr.str());
				return E_FAIL;
			}
		}
		if(!target->IsLeaf() && m_sourceDTDParser!=0 && !IsNoSource()){
			m_bGeneratorDefined=true;
			if(strncmp("Fixed [",(const char*)sourceExpr,6)==0){                                                 
				sourceExpr=sourceExpr.Mid(6);
			}
			if(sourceExpr=="[]"){
				source=m_sourceDTDParser->FindRoot();
			}
			else {
				source=m_sourceDTDParser->findSource_expandTreeIfNotExist((const char*)sourceExpr);
			}
			if(source){
				target->m_Generator=source;
			}
			else {
				stringstream ssErr;
				ssErr << "TreeMapper::enterMapInfoToStructTree - findSource_expandTreeIfNotExist(" << (const char*)sourceExpr << ") failed" << endl;
				getViewLog()->DebugString(ssErr.str().c_str());
				AppendErrorMessage(ssErr.str().c_str());
				throw CrException (ssErr.str());
				return E_FAIL;
			}
		}

		target->markMapFlag();
		if(sourceExpr.Left(c_length_userDefinedFunctionTag)==c_userDefinedFunctionTag){
			target->setUseDbFunctionFlag();
			                                                             
		}
		if((sourceExpr.Left(c_length_PostProcessIdentity)==c_PostProcessIdentity) 
			||(sourceExpr.Left(c_length_PostProcessSequece)==c_PostProcessSequece)
			||(sourceExpr.Left(c_length_PostProcessOracleSequece)==c_PostProcessOracleSequece))
		{
			target->setUseDbFunctionFlag();
			if (sourceExpr.Left(c_length_PostProcessOracleSequece)==c_PostProcessOracleSequece)
				target->setOracleSeqNoFlag();
		}

		if(target->IsLeaf()){
			CROM*targetParent=target->GetParent();
			CTransform* pTrans=targetParent->getTrans();
			int err;
			pTrans->createTAListItemForCrom(sourceExpr, target, err);
			if(!m_bForbidStreaming)m_bForbidStreaming=CTransform::useInstanceResolvingFunction(target->getTAList());
		}
	}

	markMapFlagForSourceTree();

	if(!m_bGeneratorDefined){
		getViewLog()->DebugString("MapByTransformationID findGenerators\n");
		unsigned long hr = S_OK;
		if((hr=findGenerators())!=S_OK){
			bReturn = true;
			return hr;
		}
	}

	return S_OK;
}

void TreeMapper::markMapFlagForSourceTree(CROM*pTargetNode)
{
	if(!pTargetNode)pTargetNode=m_targetDTDParser->FindRoot();
	CTransform* pTrans=pTargetNode->getTrans();
	for(int i=0, n=pTrans->getInputDEListSize(); i<n; ++i){
		konga::DataElement* de=pTrans->getInputDEOf(i);
		CROM*pSourceAttr=m_sourceDTDParser->findSource_expandTreeIfNotExist(de->getName());
		if(pSourceAttr && !(pSourceAttr->m_Type&CROM_t_Mapped))pSourceAttr->m_Type |= CROM_t_Mapped;
	}
	for(CROM*pChild=pTargetNode->GetFirstChild(); pChild; pChild=pChild->GetNextSibling())
	{
		if(!pChild->IsLeaf())markMapFlagForSourceTree(pChild);
	}
}


                                                            
                                                                  
                        
                                             
                                              
unsigned long TreeMapper::findGenerators(){
	unsigned long hr = S_OK;
	if(IsNoSource())return hr;
                          
	if(m_targetDTDParser==0||m_sourceDTDParser==0){
		                                                                                                                                  
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_FINDGENERATOR_VALUES_NOT_SET);
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}
	CROM*targetRoot=m_targetDTDParser->FindRoot();
	if(targetRoot==0){
		                                                              
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_FINDGENERATOR_ROOT_IS_ZERO_TARGET);
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}
	CROM*sourceroot=m_sourceDTDParser->FindRoot();
	if(sourceroot==0){
		                                                              
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_FINDGENERATOR_ROOT_IS_ZERO_SOURCE);
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}
	targetRoot->checkLoopNode();
	sourceroot->checkLoopNode();

	if(m_sourceDTDParser->findGenerators(targetRoot)!=0){
		KString tmp=m_sourceDTDParser->m_lastErr;
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_FINDGENERATOR_ERROR, 
			(m_sourceDTDParser->m_conflict_target_loop_node==0)?"unknown target":
			(const char*)m_sourceDTDParser->m_conflict_target_loop_node->get_Path(),
			(const char*)tmp);
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}
	return S_OK;
}

void TreeMapper::MarkSourceUpdateColumns(DTDParser* pSourceDTDParser)
{
	if(pSourceDTDParser == 0 || m_UpdateFields.IsEmpty())
		return;

	CROM* pStructureRoot = pSourceDTDParser->FindRoot();
	if(pStructureRoot == 0)
		return;

	CROM* pFlatTableNode=pStructureRoot->GetFirstChild();
	std::string sqlTableName=pFlatTableNode->get_sqlTableName();
	if(sqlTableName.size()<2){
		throw konga::CrException("unexpected sqlTableName="+sqlTableName);
	}
	std::string sBeginQuote=sqlTableName.substr(0,1), sEndQuote=sqlTableName.substr(sqlTableName.size()-1);

	StringTokenizer strTok(m_UpdateFields, "^=");
	std::string token, colname, value;;
	CROM* pColumn = 0;
	while(strTok.NextToken())
	{
		token = strTok.token();
		switch(token[0])
		{
		case '^':
			colname.clear();
			break;
		case '=':
			break;
		default:
			if(colname.empty())
			{
				colname = token;
				std::string owner, table_name, field_name;
				parseOwnerTableFieldString(colname.c_str(), owner, table_name, field_name, sBeginQuote, sEndQuote);
				pColumn = pFlatTableNode->FindChildLeaf(field_name.c_str());
				if(pColumn)pColumn->SetSourceUpdateColumn();
			}
			else {
				value=token;
				konga::CSV_restore(value);
				if(pColumn)pColumn->m_Comment=value.c_str();
			}
			break;
		}
	}
}

void TreeMapper::SetTransformationResultInfo(TransformationStatistics& statistics, const string& stSourceFileName, const clock_t clkStart) const {
	if( m_bStreamingTransformation ) {
		statistics.SourceSizeBytes = m_lTxtSrcSize;
		statistics.TargetSizeBytes = m_lTxtTgtSize;
		statistics.SourceRecords = m_lTxtSrcRecords;
		statistics.TargetRecords = m_lTxtTgtRecords;
	}
	else if ( (m_sourceType != XmlTypeId && ( m_targetType != XmlTypeId && m_targetType != WebServiceInputTypeId && m_targetType != WebServiceOutputTypeId) ) && 
			  (m_sourceType == TextTypeId && ( m_targetType == DbTypeId || m_targetType==RdbTypeId ) ) )
	{
		statistics.SourceSizeBytes = m_lTxtSrcSize;
		statistics.TargetSizeBytes = m_lTxtTgtSize;
		statistics.SourceRecords = m_lTxtSrcRecords;
		statistics.TargetRecords = m_lTxtTgtRecords;
	}
	else
	{
		if ( m_sourceType == XmlTypeId ) {
			statistics.SourceSizeBytes = m_sourceXmlParser.GetDataSize();
		}
		else {
			statistics.SourceSizeBytes = m_lTxtSrcSize;
			statistics.SourceRecords = m_lTxtSrcRecords;
		}
		
		if ( m_targetType == XmlTypeId ) {
			statistics.TargetSizeBytes = m_targetXmlParser.GetDataSize();
		}
		else {
			statistics.TargetSizeBytes = m_lTxtTgtSize;
			statistics.TargetRecords = m_lTxtTgtRecords;
		}
	}

	                                      
                                                                                                              
                  
                                                                 
                                                        
    

	const double clkPeriod(clock() - clkStart);
	statistics.TransformationTimeMs = (long long)((clkPeriod*1000)/CLOCKS_PER_SEC);
}

void TreeMapper::OnMapByTransformationExit(const unsigned long hr) const
{
	if(getViewLog()->IsDebugLogEnabled())
	{
		CrDateTime sysTime;
		getViewLog()->DebugString("\nEnd Time %d/%d/%d %d:%d:%d \n",
					sysTime.GetMonth(), sysTime.GetDay(), sysTime.GetYear(), sysTime.GetHour(), sysTime.GetMinute(), sysTime.GetSecond());
		                                                               
		long lSrc = 0;                                         
		long lTgt = 0;                                         
		getViewLog()->DebugString("\n\nSrc Size %ld, Tgt Size %ld\n\n", lSrc, lTgt ) ;
	}

	if (hr != S_OK) {
		getViewLog()->DebugString("TreeMapper::MapByTransformationID %d - Failed End \nLast Error = %s\n", m_TransformationID, (const char*)m_lastErr);
	} else {
		getViewLog()->DebugString("TreeMapper::MapByTransformationID %d - Success End \nLast Error = %s\n", m_TransformationID, (const char*)m_lastErr);
	}

	                                               
	getOperationDataElementHolder()->removeOperationDataElement(TRANSFORMATION_NAME_DE);
}

void TreeMapper::HandleSpecialTargetTypes()
{
	getViewLog()->DebugString("HandleSpecialTargetTypes() begin\n");
	unsigned long hr=S_OK;
	if( m_targetType == WebServiceOutputTypeId )
	{
		getViewLog()->DebugString("ProcessHostedWebServiceOutput()\n");
		hr=ProcessHostedWebServiceOutput();
	}
	else if(m_targetType == LdapTypeId){
		TextDocument td((const char*)m_outputDataFileName, m_targetDTDParser->m_root, "", 0, isEDIFACT(), true);
		long long m_lTxtTgtRecords = 0;
		CROMData* pDataRoot=td.createDataTree(m_lTxtTgtRecords);
		hr=ProcessLdapTarget(pDataRoot);
	}
	if(FAILED(hr)){
		throw konga::CrException((const char*)m_lastErr);
	}
	return;
}

bool TreeMapper::GetValidateSourceXml()
{
	                                                                                              

	                                                                   
	const bool bValidate = (m_bDoXmlValidationRegistryFlag && m_bDoSourceXmlValidation);

	if( bValidate ) {
		return !IsSourceWebServiceType();
	}
	return false;
}

bool TreeMapper::GetValidateTargetXml()
{
	                                                                                              

	                                                                   
	const bool bValidate = (m_bDoXmlValidationRegistryFlag && m_bDoTargetXmlValidation);

	if( bValidate ) {
		return !IsTargetWebServiceType();
	}
	return false;
}

void TreeMapper::AppendErrorMessage(const KString& csError)
{
	if ( csError.IsEmpty() ) return;

	if( !m_lastErr.IsEmpty() )
	{
		if ( m_lastErr.Find(csError) >= 0  ) return;
		m_lastErr += "\n";
	}
	m_lastErr += csError;
}

unsigned long TreeMapper::CreateJtrFromMappings(long lTransformationID, const std::string& stSourceJtrFN, const std::string& stTargetJtrFN)
{
	PROFILING doPrf(this,"CreateJtrFromMappings");

	                                                                                          
	                                                                       
	AutoCallTerminate<CViewLog> adopt_globalViewLog(0);
	AutoCallTerminate<CErrorLog> adopt_globalErrorLog(0);
	if( !getViewLog()->IsInitialized() )
	{
		std::string guid=getOperationContext()->getLocalTmpDirForOperation();
		getViewLog()->Initialize("trans", m_TransformationID, m_debug_log, false, m_bWriteLog, 0, 0, false, guid.substr(guid.size()-9,8));
		adopt_globalViewLog.Attach(getViewLog());
	}
	if( !getErrorLog()->IsInitialized() )
	{
		getErrorLog()->SetViewLog(getViewLog());
		getErrorLog()->Initialize();
		adopt_globalErrorLog.Attach(getErrorLog());
	}
	WriteTransformationLogHeader("");

	m_TransformationID=lTransformationID;
	setName("Transformation: TransformationID="+konga::toString(m_TransformationID));

	bool bReturn;
	unsigned long hr=creatStructureTrees(bReturn, stSourceJtrFN != "", stTargetJtrFN != "");
	if(FAILED(hr))return hr;

	if( m_sourceDTDParser && !IsNoSource() && !stSourceJtrFN.empty() ){
		const char click[] = "source";
		const string fileExtension(konga::GetFileExtension((const char*)m_inputStructFileName));
		hr=m_sourceDTDParser->writeJtr(stSourceJtrFN.c_str(),click,fileExtension.c_str());
		if(FAILED(hr))return hr;
	}

	if( m_targetDTDParser && !stTargetJtrFN.empty() ){
		const char click[] = "target";
		const string fileExtension(konga::GetFileExtension((const char*)m_outputStructFileName));
		hr=m_targetDTDParser->writeJtr(stTargetJtrFN.c_str(),click,fileExtension.c_str());
		if(FAILED(hr))return hr;
	}

	return hr;
}

unsigned long TreeMapper::creatStructureTrees(bool& bReturn, bool bCreateSourceJtr, bool bCreateTargetJtr)
{
	PROFILING doPrf(this,"creatStructureTrees");
	unsigned long hr=S_OK;
	try
	{
		if( FAILED(hr = GetTranDBInfo(0)) )return hr;

		m_XmlSource = (m_sourceType == XmlTypeId) || 
					  (m_sourceType == RosettanetTypeId) || 
					  (m_sourceType == WebServiceInputTypeId) ||
					  (m_sourceType == WebServiceOutputTypeId);

		m_XmlTarget =  m_targetType == XmlTypeId ||
					   m_targetType == SAPTypeId || 
					   m_targetType == RosettanetTypeId || 
					   m_targetType == WebServiceInputTypeId ||
					   m_targetType == WebServiceOutputTypeId;
		bool always_from_structure_file=true;
		InitializeStructureParsers(always_from_structure_file, bCreateSourceJtr, bCreateTargetJtr);
	}
	catch(const CrException& e)
	{
		std::string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		hr = E_FAIL;
	}
	if(getViewLog()->IsErrLogEmpty())getViewLog()->Delete();
    return hr;
}

void TreeMapper::EvaluateJMSGlobalDataElement(){
	int err;
	for (STRMAP_T::const_iterator iterator= m_maplist.begin();iterator!=m_maplist.end();++iterator)
	{
		KString targetPath=iterator->first;
		if(targetPath.IsEmpty()||targetPath.GetAt(0)!='$')continue;                                    
		CTransform t(this, (long)0);
		KString sourceExpr=iterator->second, value;
		t.TokenizeTemplate((const char*)sourceExpr);
		err=t.Evaluate_dbg((const char*)sourceExpr,t.m_TokenArrayList,"",0,"",value,0);

		DataElement::Type type(DataElement::t_string);
		std::map<std::string,std::string>::iterator it=m_targetDTDParser->m_jmsProperties.find((const char*)targetPath);
		if(it!=m_targetDTDParser->m_jmsProperties.end()){
			type=DataElement::get_type(it->first.c_str(),0);
		}

		DataElement*de=getGlobalDataElement((const char*)targetPath+1), *de2=new DataElement(type,(const char*)value);
		de->set(de2);
	}
}

namespace {

	void replaceDataElementsInManualSQLStatement(TreeMapper* treeMapper, CROM* root) {
		CROM* table = root->GetFirstChild();
		if (table != 0 && table->isManualSqlStatement()) {
			const string sql(table->get_manualSQL());
			const string replaced(treeMapper->replaceGlobalDataElementsInString(sql));
			table->set_manualSQL(replaced);
		}
	}

}


unsigned long TreeMapper::dumpDatabaseSourceToFile(const KString& sInputFileName, const KString& adHocFilter){
	PROFILING doPrf(this,READ_DATABASE_SOURCE_PROFILING_SECTION);
	unsigned long hr = S_OK;
	if(m_sourceType != DbTypeId)throw CrException("DumpDbSource called for a non-DB source.");
	try
	{
		ReplaceGlobalDataElementsInConditions(m_sourceDTDParser->FindRoot());
		const bool bWriteLog=getViewLog()->IsDebugLogEnabled();
		switch(m_sourceDriverType){
			case konga::ODBC:
				{
					CROM* pDtdRoot=m_sourceDTDParser->FindRoot();
					replaceDataElementsInManualSQLStatement(this, pDtdRoot);
					hr=ReadODBCSource(sInputFileName, adHocFilter);
					if(FAILED(hr))return hr;
				}
				break;
			case konga::JDBC:
				{
					CROM* pDtdRoot=m_sourceDTDParser->FindRoot();
					replaceDataElementsInManualSQLStatement(this, pDtdRoot);
					const string& stJtrFileName=getTempSourceJtrFileName();
					CromToJtrWriter writer;
					writer.createJtr(CROMHeader(pDtdRoot), stJtrFileName);
					SmartDbPtr<DbConnection> pTranDbConn( konga::GetTranDbConnection() );
					const konga::UserInfo& userInfo = getOperationContext()->getCurrentUser();

					if ( m_pSourceDbConnectionInfo == 0 ) {
						throw InternalErrorException("No source database connection info has been set. This has to be configured before a JDBC source can be loaded.");
					}
					const string debug_file_name( bWriteLog ? getJdbcSourceLogFileName() : "" );

					PROFILING doPrf(this,"ReadJdbcSource");
					ReadJDBCSource(*pTranDbConn, userInfo, m_SourceID, *m_pSourceDbConnectionInfo,
						(const char*)adHocFilter, stJtrFileName, (const char*)sInputFileName,
						getAuxSourceStructFileName(), debug_file_name);
				}
				break;
			default:
				throw InternalErrorException("Unknown Source Driver Type: " + konga::toString(m_sourceDriverType));
		}
		if(getViewLog()->IsDebugLogEnabled()){
			getViewLog()->debugCopyFile("DumpDbSource:sInputFileName",(const char*)sInputFileName, getDebugFileName()+".input");
		}
		if(!konga::NonEmptyFileExists((const char*)sInputFileName)){
			string stErr("No database rows available. Either the table is empty, the WHERE clause returned no rows or tables could not be joined.");
			if ( ErrorConfiguration::isWarning(ErrorConfiguration::EMPTY_DATABASE_SOURCE, true) ) {
				getViewLog()->LogMessage("%s", stErr.c_str());
			}
			else if ( ErrorConfiguration::isError(ErrorConfiguration::EMPTY_DATABASE_SOURCE, false) ) {
				throw CrException(stErr);
			}
			else {
				                                  
			}
			konga::touch_file((const char*)sInputFileName);
		}
	}
	catch(const CrException& e)
	{
		std::string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		throw CrException(stErrMsg);
	}
	return hr;
}

CViewLog* TreeMapper::getViewLog() const {
	return m_pTransformationDataHolder->getViewLog();
}

CErrorLog* TreeMapper::getErrorLog() const {
	return m_pTransformationDataHolder->getErrorLog();
}

std::string TreeMapper::get_LastError() const {
	return std::string((const char*)m_lastErr);
}

DataElement* TreeMapper::getGlobalDataElement(const char* tag)
{
	if ( !getOperationDataElementHolder() ) {
		throw InternalErrorException("Global data elements are not available since the operation data element holder has not been set.");
	}
	return getOperationDataElementHolder()->getOperationDataElement(tag);
}

void TreeMapper::enterProfilingSection(const string& sWhere,const string&sExtra)
{
	if(m_pProfilingKit==0)return;
	m_pProfilingKit->start(sWhere, sExtra);
}

void TreeMapper::exitProfilingSection(const string& sWhere)
{
	if(m_pProfilingKit==0)return;
	m_pProfilingKit->end(sWhere);
}

bool TreeMapper::IsSuitableForChunking(){
	if(m_sourceType==LdapTypeId){
		return m_pLdapFilterCollection->size()==1;
	}
	return true;
}

namespace {
	void enterCromPathToMap(std::map<std::string,CROM*>& map, CROM*node){
		if(node==0)return;
		string path( (const char*)node->get_Path() );
		map[path]=node;
		for(CROM*child=node->GetFirstChild(); child; child=child->GetNextSibling()){
			if(child->IsLeaf()){
				path=child->get_Path();
				map[path]=child;
			}
			else {
				enterCromPathToMap(map, child);
			}
		}
	}
}

CROM* TreeMapper::getSourceCrom(const std::string&path)
{
	if(g_sourceCromTable.empty()){
		enterCromPathToMap(g_sourceCromTable, m_sourceDTDParser->FindRoot());
	}
	const std::string path0( (const char*)DTDParser::stripInstanceTag(path) );
	std::map<std::string,CROM*>::iterator it=g_sourceCromTable.find(path0);
	if(it==g_sourceCromTable.end())return 0;
	return it->second;
}

void TreeMapper::linkTokenToSourceDE(CTransform*tran, Token*token, const std::string& path)
{
	DataElement* de=tran->find_de(path);
	if(de==0){
		de=new DataElement(path.c_str());
		                                                                   
		CROM*node=getSourceCrom(path);
		if(node!=0){
			de->setFormatStr(node->GetFormat());
			de->setType(node->get_DataElementType());
			if(!node->isDB())de->setDefaultString(node->GetDefaultValue());
			de->setPosBegin(node->get_fieldPos());
			de->setPosLength(node->get_fieldLength());
		}
		tran->enterSourceDeMap(de,path);
		tran->m_InputDEList.push_back(de);
		de->setError(-1);
	}
	token->setDataElement(de);
}

void TreeMapper::closeDbLookup(){
	PROFILING doPrf(this, "CloseDbLookup");
	try {
		getTransformationDataHolder()->getDbLookupCache()->closeAll();
	}
	catch( const CrException& ex ) {
		                                 
		getViewLog()->LogMessage("Failed to close connections used for DbLookup: %s", ex.GetErrorString().c_str());
	}
	catch( ... ) {
		                                 
		getViewLog()->LogMessage("Failed to close connections used for DbLookup: Unknown exception (should never happen)");
	}
}

std::string getCurrentSequence(const char* szDriverName, const char* sSchemaTableName, const char* sColumnName)
{
	if(strncasecmp(szDriverName,"postgresql",10)==0){
		std::string sSchema, sTableName;
		int k=findk(sSchemaTableName,':');
		if(sSchemaTableName[k]==0){
			sSchema="public";
			sTableName=sSchemaTableName;
		} else {
			sSchema=std::string(sSchemaTableName,k);
			sTableName=std::string(&sSchemaTableName[k+1]);
		}
		return std::string("SELECT CURRVAL('\"")+sSchema+"\".\""+sTableName+"_"+sColumnName+"_seq\"');";
	}
	if(strncasecmp(szDriverName,"sql server",10)==0)return "SELECT @@IDENTITY;";
	if(strncasecmp(szDriverName,"MySQL",5)==0)return "SELECT last_insert_id();";
	if(strncasecmp(szDriverName,"DB2",3)==0)return "select identity_val_local()";
	if(strncasecmp(szDriverName,"sybase",6)==0)return "SELECT @@IDENTITY;";
	return "";
}


void TreeMapper::defineSourceDbDriverDependentStr(const KString& csSourceConnectString)
{
	konga::defineDbDriverDependentStr((const char*)csSourceConnectString, m_sSourceDbDriver);                                           
}
void TreeMapper::defineTargetDbDriverDependentStr(const KString& csTargetConnectString)
{
	konga::defineDbDriverDependentStr((const char*)csTargetConnectString, m_sTargetDbDriver);                                           
}

std::string TreeMapper::getAutoSequenceSql(const char* sTableName, const char* sColumnName)
{
	return getCurrentSequence(m_sTargetDbDriver.c_str(), sTableName, sColumnName);
}

const char* TreeMapper::getTargetTransacPath(){
	if(IsFlatTarget())return "";
	return m_target_transac_crom?((const char*)m_target_transac_crom->get_Path()):"";
}

void TreeMapper::setTargetTransacNode(const std::string&stPath){
	if(IsFlatTarget()){
		m_target_transac_crom=0;
		return;
	}
	if(stPath.size()==0){
		int i = 0;
		m_target_transac_crom=m_targetDTDParser->m_root->findFirstLoopNode(i);
	} else {
		std::string path((stPath.at(0)!='[')?("["+stPath+"]"):stPath);
		try {
			m_target_transac_crom=m_targetDTDParser->findNode(path.c_str(), true);
		}
		catch(const konga::CrException&){
			throw konga::CrException(std::string("Failed to find target chunk node: \"")+stPath+"\"");
		}
	}
}

const char* TreeMapper::getSourceTransacPath(){
	if(IsFlatSource())return "";
	return m_source_transac_crom?((const char*)m_source_transac_crom->get_Path()):"";
}

CROM* TreeMapper::getSourceTransacNode(const std::string&stPath){
	assert(m_sourceDTDParser!=0);
	CROM*root=m_sourceDTDParser->m_root, *sourceTransacNode=0;
	if(IsFlatSource()){
		assert(root->GetFirstChild() && root->GetFirstChild()->IsElement());
		sourceTransacNode = root->GetFirstChild();
	}
	else {
		if(stPath.size()==0){
			int i = 0;
			sourceTransacNode = m_sourceDTDParser->m_root->findFirstLoopNode(i);
		} else {
			std::string path((stPath.at(0)!='[')?("["+stPath+"]"):stPath);
			try {
				sourceTransacNode = m_sourceDTDParser->findNode(path.c_str(), false);
			}
			catch(const konga::CrException&){
				throw konga::CrException(std::string("Failed to find source chunk node: \"")+stPath+"\"");
			}
		}
	}
	if ( sourceTransacNode == 0 ) {
		throw CrException("Failed to find source chunk node: " + stPath + "\nMake sure the operation option \"Source chunk node\" matches a repeating source node in your transformation.\n" \
			"Right click on the operation and select Options... to configure the Source chunk node.");
	}
	return sourceTransacNode;
}

void TreeMapper::setSourceTransacNode(CROM*node){
	m_source_transac_crom=node;
}

int TreeMapper::splitSourceFile(const std::string& stFileName, const size_t chunk_size)
{
	PROFILING doPrf(this,"splitSourceFile");
	if(chunk_size==0)return 1;

	bool hasData=false;
	int iChunk=0;
	bool isXml=false;

	{
		CROM*pDtdRoot=m_sourceDTDParser->m_root;
		std::vector<konga::JbNode*>transac_list;
		TransacParser parser(stFileName, m_sourceType, pDtdRoot, m_source_transac_crom, transac_list);
		isXml=pDtdRoot->IsXml();


		std::vector<konga::JbNode*>::iterator it=transac_list.begin();
		while(it!=transac_list.end()){
			hasData=true;
			                               
			const string& stFileName_=konga::GetSequenceFileName(stFileName, "_", iChunk++);
			FILE*out=fopen(stFileName_.c_str(),"wb+");
			if(out==0) {
				const long lErr = GetLastError();
				throw CrException("Failed to open file '" + stFileName_ + "' when chunking source file '" + stFileName + "'.\n" + ErrorResolver(lErr).Resolve(lErr));
			}
			AutoFileHandle adopt_out(out);
			  
			std::stack<konga::JbNode*> current_nodes;
			konga::JbNode*xml_header=parser.getXmlHeader(), *doc_type=parser.getDocType();
			if(xml_header!=0)fprintf(out,"%s\n", xml_header->getContents().c_str());
			if(doc_type!=0)fprintf(out,"%s\n", doc_type->getContents().c_str());

			konga::JbNode*transac_node;
			unsigned int iTransac=0;
			do {
				transac_node=*it;
				parser.write_transac_node(out, transac_node, current_nodes, isXml);
				it++;
				iTransac++;
			}
			while(iTransac < chunk_size && it!=transac_list.end());
			parser.write_transac_trailer(out, transac_node, current_nodes, isXml);
		}
	}
	if(hasData) {
		const string& stFileName_=konga::GetSequenceFileName(stFileName, "_", 0);
		konga::kMoveFile(stFileName_, stFileName, true);
	}
	return iChunk;
}

void TreeMapper::CombineTargetFiles(const std::string& stFileName, int nChunk)
{
	PROFILING doPrf(this,"CombineTargetFiles");
	if (this->IsFlatTarget ())
	{
		for(int iChunk=0; iChunk<nChunk; ++iChunk){
			m_iChunk=iChunk;
			std::string stChunkFilename_=getTargetDataFileName();
			kAppendFile(stFileName,stChunkFilename_,true);
		}
	}
	else
	{
		CombineFiles cf(this);
		cf.processFiles(stFileName, nChunk);
	}
}

namespace {
	bool hasMultipleAnyElements(CROM*node){
		int iAny=0;
		for(CROM*child=node->GetFirstChild(); child; child=child->GetNextSibling()){
			if(child->IsLeaf())continue;
			if(child->IsAnyElement())iAny++;
			if(iAny>1)return true;
			if(hasMultipleAnyElements(child))return true;
		}
		return false;
	}
}

bool TreeMapper::isTrimSourceWs(){
	CROM*sourceDtdRoot=m_sourceDTDParser->FindRoot();
	assert(sourceDtdRoot!=0);
	if(IsFlatSource())sourceDtdRoot=sourceDtdRoot->GetFirstChild();
	bool bPreserv = (m_sourceType!=DbTypeId) && sourceDtdRoot && sourceDtdRoot->get_delimiter()!=0;
	bPreserv = konga::GetSystemVariableAsBool(SOURCE_PRESERVE_WS, getOperationDataElementHolder(), bPreserv);
	if(m_sourceType==DbTypeId)bPreserv = konga::GetSystemVariableAsBool(SOURCE_DB_PRESERVE_WS, getOperationDataElementHolder(), bPreserv);
	return !bPreserv;
}

bool TreeMapper::xmlPrettify(){
	return konga::GetSystemVariableAsBool(XML_PRETTIFY, getOperationDataElementHolder(), true);
}

bool TreeMapper::isStreamingTransformationApplicable(){
	if(!IsFlatTarget())return false;

	const bool bDefault = konga::GetOptionAsBoolean("TransformationEngine", AUTO_STREAMING_CONF_FILE, true);
	const bool bAutoStreaming = konga::GetSystemVariableAsBool(AUTO_STREAMING_DE_NAME, getOperationDataElementHolder(), bDefault);

	if(!bAutoStreaming)return false;
	if(m_XmlSource){
		CROM* pFirstNode=m_targetDTDParser->FindRoot()->GetFirstChild();
		CROM* pMutipleMappingNode=pFirstNode?pFirstNode->GetNextSibling():0;
		CROM* transaction=pFirstNode?pFirstNode->m_Generator:0;
		if(transaction!=0 && pFirstNode!=0){
			CTransform* trans=pFirstNode->getTrans();
			std::set<CROM*> sourceCroms;
			for(int i=0, n=trans->getInputDEListSize(); i<n; ++i){
				DataElement* de=trans->getInputDEOf(i);
				CROM*node=m_sourceDTDParser->findSource_expandTreeIfNotExist(de->getName());
				if(node==0){
					return false;
				}
				CROM*folder=node->GetParent();
				if(folder!=0)sourceCroms.insert(folder);
			}
			findUnreadElements(m_sourceDTDParser->m_root, transaction, sourceCroms);
			if(sourceCroms.size()>0)return false;
		}
		bool allowStreaming = !m_bForbidStreaming && transaction && transaction->IsElement() && pMutipleMappingNode==0;
		if(allowStreaming==false)return false;
		                                               
		return !hasMultipleAnyElements(m_sourceDTDParser->FindRoot());
	}
	return IsFlatSource();
}

bool TreeMapper::autoMappingForXmlNil(){
	return m_bAutoMappingForXmlNil;
}

void TreeMapper::DeleteData() {
	if(IsXmlSource()) {
		m_sourceXmlParser.DeleteData();
	}
	else {
		                                                     
	}
	m_pSourceDataRoot=0;

	if(IsXmlTarget()) {
		m_targetXmlParser.DeleteData();
	}
}

void TreeMapper::prepareBatchFunction(TreeMapper*next)
{
	m_nextTransformation=next;
}

bool TreeMapper::useETLForgeXmlParser(){
	return konga::GetSystemVariableAsBool(JBXMLPARSER_DE_NAME, getOperationDataElementHolder(), false);
}

bool TreeMapper::validateXml(CROM*root, const std::string& sourceXMLFileName, const std::string& inputStructFileName, std::string& errmsg){
	konga::xml::XmlSchemaValidator validator;
	if(root!=0)
		return validator.ValidateXmlInstance(sourceXMLFileName, inputStructFileName, root->GetNamespace(), errmsg);
	else {
		errmsg="Failed to validate xml documeny. Structure root must be defined before validating xml document.";
		return false;
	}
}

bool TreeMapper::readNextBatch(CRData*source, CROM*generator, std::vector<CRData*>&instances){
	if(isTestTransformation() || isTestOperation())return 0;
	CROM*dtd=source->GetDTDNode();
	if(dtd->isBatchParent()){
		if(m_pXmlDocument){
			if(!m_pXmlDocument->isBatchLoading())return false;
			CRXmlData* beginCRXmlData=dynamic_cast<CRXmlData*>(source);
			return m_pXmlDocument->readNextBatch(beginCRXmlData, generator, instances);
		}
		else if(m_pTextDocument){
			if(!m_pTextDocument->isBatchLoading())return false;
			CROMData* beginCROMData=dynamic_cast<CROMData*>(source);
			return m_pTextDocument->readNextBatch(beginCROMData, generator, instances);
		}
	}
	return false;
}

namespace {
	const char wbplus[]="wb+";
	const char wplus[]="w+";
}

const char* TreeMapper::getWriteModeStr() const {
	return TransformationEngine::getWriteModeStr(m_TargetWriteMode);
}

bool TreeMapper::autoQueryMore() const {
	return getTransformationEngine()->autoQueryMore();
}

bool TreeMapper::isCSVSource() const {
	if(m_sourceDTDParser==0 || m_sourceDTDParser->m_root==0) return false;
	CROM* firstChild = m_sourceDTDParser->m_root->GetFirstChild();
	if(firstChild==0 || firstChild->IsLeaf() ) return false;
	return firstChild->isCsv();
}

void TreeMapper::DumpDatabaseSourceDataIfSourceIsDatabase(const string& inputDataFileName) {
	if(m_bSourceIsDatabase){
		getViewLog()->DebugString("TreeMapper::DumpDatabaseSourceDataIfSourceIsDatabase()");
		unsigned long hr = dumpDatabaseSourceToFile(inputDataFileName, m_loadSourceFilter);
		if (FAILED(hr)) {
			throw konga::CrException(GetLastError());
		}
		setSourceIsDatabaseFlag(false);
	}
}
