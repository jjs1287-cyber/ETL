/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/Settings.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Xml/XmlUtils.h>

#include <konga/Soap/SoapEnvelope.h>
#include <konga/Soap/SoapEncoding.h>
#include <konga/Soap/SoapFault_11.h>
#include <konga/Soap/SoapEnvelopeCleaner.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/PluginManager/PipelinePluginSelector.h>
#include <konga/PluginManager/PipelinePosition.h>
#include <konga/PluginManager/PipelinePluginManifest.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/PipelinePluginExecutor/PipelinePluginExecutor.h>

#include <konga/OperationContext/OperationContext.h>

#include <konga/TransformationLogging/ViewLog.h>

#include "TreeMapper.h"
#include "ErrorCodes/resource.h"
#include "DTDParser.h"
#include "TransformationEngine.h"
#include "XsdFromWsdlCreator.h"
#include "WebServiceResponseSynchronizer.h"
#include "SoapBodyArrayHandler.h"
#include "XsdNamespaceSupport.h"
#include "XsdUtils.h"
#include "crom.h"
#include "NoValueContentXmlCreator.h"
#include "XPathSearch.h"
#include "XmlEditor.h"
#include "WebServiceHandler.h"

using namespace konga;
using namespace konga::xml;
using std::string;
using std::vector;
using std::map;


void WebServiceInfo::ParseOtherNamespaces(map<string, string>& m_mapOtherNamespaces) const
{
	string stInput(m_stOtherNamespaces);
	while( stInput.length() > 0 )
	{
		string stNamespaceDeclaration = konga::strtok(stInput, WebServiceUtils::NAMESPACE_SEPARATOR, 0);
		if( stNamespaceDeclaration.length() > 0 )
		{
			size_t s = stNamespaceDeclaration.find('=');
			string stAttribName = stNamespaceDeclaration.substr(0, s);
			string stURI = stNamespaceDeclaration.substr(s+1);
			m_mapOtherNamespaces[stAttribName] = stURI;
		}
	}
}

WebServiceStructure::WebServiceStructure(TreeMapper*pTreeMapper,
										 const string& stRequestXsd,
										 const string& stRequestJtr,
										 const string& stResponseXsd,
										 const string& stResponseJtr) :
								m_pTreeMapper(pTreeMapper),
								m_stRequestXsd(stRequestXsd),
								m_stRequestJtr(stRequestJtr),
								m_stResponseXsd(stResponseXsd),
								m_stResponseJtr(stResponseJtr),
								m_isLoaded(false),
								m_requestHasValueContent(true)
{
	m_requestParser = new DTDParser(pTreeMapper);
	m_responseParser = new DTDParser(pTreeMapper);
	m_responseSchemaNamespaces = new XsdNamespaceSupport(stResponseXsd);
};

WebServiceStructure::~WebServiceStructure() {
	delete m_requestParser;
	delete m_responseParser;
	delete m_responseSchemaNamespaces;
}

namespace {
	void LoadStructure(DTDParser* parser, const string& jtrFile, const string& xsdFile) {
		if(konga::FileExists(jtrFile)) {
			parser->CreateCromFromJtr(jtrFile.c_str());
		}
		else {
			parser->createJtrFromXSD(xsdFile.c_str(), "root", jtrFile.c_str());
		}
	}
}

void WebServiceStructure::Load(bool loadRequestStructure, bool loadResponseStructure) {
	if (m_isLoaded) {
		return;
	}
	if (loadRequestStructure) {
		LoadRequestStructure();
	}
	if (loadResponseStructure) {
		LoadResponseStructure();
	}
	m_isLoaded = true;
}

void WebServiceStructure::LoadRequestStructure() {
	PROFILING doPrf(m_pTreeMapper, "LoadRequestStructure");
	LoadStructure(m_requestParser, m_stRequestJtr, m_stRequestXsd);
	m_requestHasValueContent = NoValueContentXmlCreator::HasValueContent(m_requestParser->m_root);
}

void WebServiceStructure::LoadResponseStructure() {
	PROFILING doPrf(m_pTreeMapper, "LoadResponseStructure");
	LoadStructure(m_responseParser, m_stResponseJtr, m_stResponseXsd);
	m_responseTransactionStructure.Load(m_responseParser->m_root);
}

string WebServiceStructure::GetRequestTargetNamespace() const {
	return m_isLoaded ? m_requestParser->m_root->GetNamespace() : "";
}

CROM* WebServiceStructure::GetRequestRoot() const {
	return m_isLoaded ? m_requestParser->m_root : 0;
}

string WebServiceStructure::GetResponseTargetNamespace() const {
	return m_isLoaded ? m_responseParser->m_root->GetNamespace() : "";
}

CROM* WebServiceStructure::GetResponseRoot() const {
	return m_isLoaded ? m_responseParser->m_root : 0;
}

void WebServiceStructure::CreateEmptyRequest(const string& stOutputFile) {
	XsdNamespaceSupport namespaces(m_stRequestXsd);
	NoValueContentXmlCreator creator(getTreeMapper()->getErrorLog(), namespaces);
	creator.CreateNoValueContentXML(GetRequestRoot(), XsdFromWsdlCreator::ROOT, 
		GetRequestTargetNamespace(), stOutputFile);
}

bool WebServiceStructure::ResponseStructureHasRequestNode() const {
	CROM* root = GetResponseRoot();
	CROM* transaction = root->FindDescendant(CROM_t_Element, XsdFromWsdlCreator::TRANSACTION, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
	if (transaction != 0) {
		CROM* request = transaction->FindChild(CROM_t_Element, XsdFromWsdlCreator::REQUEST);
		return request != 0;
	}
	return true;
}

WebServiceHandler::WebServiceHandler(TreeMapper*p,
						WebServiceInfo& info, 
						WebServiceIO& io,
						WebServiceStructure& structure,
						WebServiceRestrictions& restrictions,
						const konga::HttpOptions& httpOptions,
						CErrorLog& errorLog,
						CViewLog& viewLog) :
							m_pTreeMapper(p),
							m_info(info),
							m_io(io),
							m_structure(structure),
							m_restrictions(restrictions),
							m_bCreateSoapFaultOnError(false),
							m_bEmptyInput(false),
							m_pSoapFault(0),
							m_httpOptions(httpOptions),
							m_errorLog(errorLog),
							m_viewLog(viewLog) {
	const bool syncResponseGlobalValue = konga::GetOptionAsBoolean("TransformationEngine", "SyncWSResponse", true);
	m_syncResponse = konga::GetSystemVariableAsBool("etlforge.web_service_call.sync_response", p->getOperationDataElementHolder(), syncResponseGlobalValue);
}

WebServiceHandler::~WebServiceHandler() { 
	delete m_pSoapFault;
}

namespace {
	const string cookieJarFileNameDEName = "__CookiejarFileName";

	void SetCookieFileName(const std::string& stCookiejarFileName, OperationDataElementHolder* pDeHolder) {
		if ( pDeHolder == 0 ) return;
		DataElement* const de = pDeHolder->getOperationDataElement(cookieJarFileNameDEName);
		de->setType(DataElement::t_string);
		de->setValue(stCookiejarFileName.c_str());
	}

	string GetCookieFileNameImpl() {
		return konga::getTmpFileName("CookieJar");
	}
}

std::string WebServiceHandler::GetCookiejarFileName() {
	OperationDataElementHolder* const pDeHolder = getTreeMapper()->getOperationDataElementHolder();
	if ( !pDeHolder->isOperationDataElementDefined(cookieJarFileNameDEName) ) {
		                                                                                              
		const string& stCookiejarFileName = GetCookieFileNameImpl();
		SetCookieFileName(stCookiejarFileName, pDeHolder);
		return stCookiejarFileName;
	}
	else {
		                                                                                  
		DataElement* const pCookieJarFileDE = pDeHolder->getOperationDataElement(cookieJarFileNameDEName);
		int iErr = 0;
		const string stOldCookiejarFileName((const char*)pCookieJarFileDE->GetString(iErr));
		const string& stNewCookiejarFileName = GetCookieFileNameImpl();
		if ( NonEmptyFileExists(stOldCookiejarFileName) ) {
			kCopyFile(stOldCookiejarFileName, stNewCookiejarFileName, true);
		}
		SetCookieFileName(stNewCookiejarFileName, pDeHolder);
		return stNewCookiejarFileName;
	}
}

void WebServiceHandler::LoadMappingStructures() {
	PROFILING doPrf(getTreeMapper(), "LoadMappingStructures");
	if (!m_structure.IsLoaded()) {
		m_structure.Load(true, true);
	}
}

unsigned long WebServiceHandler::CreateRequestFiles() {
	PROFILING doPrf(getTreeMapper(), "CreateRequestFiles");
	unsigned long hr = S_OK;
	LoadMappingStructures();
	hr = CheckForEmptyInput();
	if (hr != S_OK) {
		return hr;
	}
	m_info.ParseOtherNamespaces(m_mapOtherNamespaces);
	if (m_bEmptyInput) {
		hr = EnvelopeToXsdStructure("",
									m_io.GetOutputFilename(),
									"",
									WebServiceHandler::Response,
									true,
									true);
	}
	else {
		hr = CreateSoapContent(WebServiceHandler::Request);
	}
	if (FAILED(hr) || m_bEmptyInput) {
		return hr;
	}
	const vector<string>::size_type nTotalRequests = m_vstRequestFileNames.size();
	if (nTotalRequests == 0) {
		m_bEmptyInput = true;
		hr = EnvelopeToXsdStructure("",
									m_io.GetOutputFilename(),
									"",
									WebServiceHandler::Response,
									true,
									true);
		return hr;
	}
	return S_OK;
}

                                                                                                               
namespace {
	bool isSalesforceQueryFinished(const std::string& responseFN, std::string& queryLocatorValue, std::string& doneValue, int& size){
		konga::XPathSearch search(responseFN);
		bool exists;
		std::string stSize=search.getSingleValue("result/size", exists);
		if(!exists) throw InternalErrorException("Failed to find the 'result/size' in response. Maybe the response is not a query response?");
		size=atoi(stSize.c_str());

		doneValue=search.getSingleValue("result/done", exists);
		if(!exists) throw InternalErrorException("Failed to find the 'done' flag in response. Maybe the response is not a query response?");
		if(doneValue=="true" || doneValue == "1") return true;

		exists = false;
		queryLocatorValue=search.getSingleValue("result/queryLocator", exists);
		if(queryLocatorValue.empty()) throw InternalErrorException("Failed to find queryLocator in response. Maybe the response is not a query response?");
		return false;
	}

	void createSalesforceQueryMoreRequest(const std::string& stRequestFN, const std::string& stRequestFN_queryMore, const std::string& queryLocatorValue){
		XmlEditor edt;
		edt.exec(XCMD_open, "", stRequestFN);
		edt.exec(XCMD_delete, "MruHeader");
		edt.exec(XCMD_delete, "PackageVersionHeader");
		edt.exec(XCMD_update, "query", "<tns1:queryMore xmlns:tns1=\"urn:enterprise.soap.sforce.com\"/>");
		edt.exec(XCMD_add, "", "<queryLocator/>");
		edt.exec(XCMD_add, "", queryLocatorValue);
		edt.exec(XCMD_write_all, "", stRequestFN_queryMore);
		edt.exec(XCMD_close);
		edt.exec(XCMD_end);
	}

	std::string xml_header_str="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
	std::string root_str="<jb_ws:root xmlns:jb_ws=\"http://www.etlforge.invalid/XsdFromWsdl\"/>";
	std::string transaction_str=
			"<jb_ws:transaction xmlns:ens=\"urn:sobject.enterprise.soap.sforce.com\" xmlns:fns=\"urn:fault.enterprise.soap.sforce.com\" "\
				"xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "\
				"xmlns:tns=\"urn:enterprise.soap.sforce.com\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"\
				"<jb_ws:response xmlns:prefix1=\"urn:enterprise.soap.sforce.com\" xmlns:sf=\"urn:sobject.enterprise.soap.sforce.com\" "\
					"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"\
					"<jb_ws:body>"\
						"<queryResponse xmlns=\"urn:enterprise.soap.sforce.com\"/>"\
					"</jb_ws:body>"\
				"</jb_ws:response>"\
			"</jb_ws:transaction>";
	std::string result_str="<result/>";
	std::string done_str="<done/>";
	std::string queryLocator_str="<queryLocator xsi:nil=\"true\"/>";
	std::string size_str="<size/>";

	                                                                                         
	void outputSalesforceQueryResponses(const std::vector<std::string>&responseFN_list, const std::string& outputResponseFN, const std::string& queryLocatorValue, const std::string&doneValue, int size)
	{
		XmlEditor edt;
		edt.exec(XCMD_open, "", "");
		edt.exec(XCMD_add, "", xml_header_str);
		edt.exec(XCMD_add, "", root_str);
		edt.exec(XCMD_add, "", transaction_str);
		edt.exec(XCMD_add, "queryResponse", result_str);
		edt.exec(XCMD_add, "result", done_str);
		edt.exec(XCMD_add, "done", doneValue);
		edt.exec(XCMD_add, "result", queryLocator_str);
		for(std::vector<std::string>::const_iterator it=responseFN_list.begin();
		  it!=responseFN_list.end(); ++it){
			std::string responseFN=*it;
			std::string recordsFN=responseFN+"_r";
			edt.exec(XCMD_open, "result/records", responseFN);
			edt.exec(XCMD_write_nodes, "", recordsFN);
			edt.exec(XCMD_close);
			edt.exec(XCMD_add_file, "result", recordsFN);
		}
		edt.exec(XCMD_add, "result", size_str);
		edt.exec(XCMD_add, "size", konga::toString(size));
		edt.exec(XCMD_write_all, "", outputResponseFN);
		edt.exec(XCMD_close);
		edt.exec(XCMD_end);
	}
}

unsigned long WebServiceHandler::SendSalesforceQueryMoreRequest(const string& stRequestFileName, const string& stResponseFileName, const string& stOutputFileName,
																const string& stCookiejarFileName, const string& stDecryptedPassword) {
	                                 
	std::vector<std::string> responseFN_list;
	std::vector<std::string> queryLocatorValue_list;
	std::string responseFN=stResponseFileName, stRequestFN_queryMore=stRequestFileName+"_queryMore",
		queryLocatorValue, doneValue;
	responseFN_list.push_back(responseFN);
	int size=0;
	for(int i=1; isSalesforceQueryFinished(responseFN, queryLocatorValue, doneValue, size)==false; ++i){
		queryLocatorValue_list.push_back(queryLocatorValue);
		createSalesforceQueryMoreRequest(stRequestFileName, stRequestFN_queryMore, queryLocatorValue);
		responseFN=stResponseFileName+"_"+konga::toString(i);
		const unsigned long hr = SendRequest(stRequestFN_queryMore, responseFN, stCookiejarFileName, stDecryptedPassword);
		if (FAILED(hr)) {
			return hr;
		}
		responseFN_list.push_back(responseFN);
	}
	queryLocatorValue_list.push_back(queryLocatorValue);
	outputSalesforceQueryResponses(responseFN_list, stOutputFileName, queryLocatorValue, doneValue, size);
	return S_OK;
}

unsigned long WebServiceHandler::CallWebService() {
	PROFILING doPrf(getTreeMapper(), "WebServiceHandler_CallWebService");
	unsigned long hr = CreateRequestFiles();
	if (FAILED(hr)) {
		return hr;
	}
	const string stDecryptedPassword( m_info.GetHttpPasswordEncrypted().empty() ? "" :
                 DecryptString(m_info.GetHttpPasswordEncrypted()).c_str() );
	const string& stCookiejarFileName = GetCookiejarFileName();
	const vector<string>::size_type nTotalRequests = m_vstRequestFileNames.size();
	const bool bAutoQueryMore=getTreeMapper()->autoQueryMore();
	for (vector<string>::size_type nReq = 0; nReq < nTotalRequests; ++nReq) {
		const string& stRequestFileName = m_vstRequestFileNames.at(nReq);
		hr = PrepareRequest(nReq, stRequestFileName);
		if (FAILED(hr)) {
			return hr;
		}
		const string stOutputFileName( (nReq == 0) ? m_io.GetOutputFilename() : GetIndexedFileName( m_io.GetOutputFilename(), nReq ));
		const string stResponseFileName(GetResponseFileName(nReq));
		hr = SendRequest(stRequestFileName, stResponseFileName, stCookiejarFileName, stDecryptedPassword);
		if (FAILED(hr)) {
			return hr;
		}
		if(bAutoQueryMore){
			hr = SendSalesforceQueryMoreRequest(stRequestFileName, stResponseFileName, stOutputFileName, stCookiejarFileName, stDecryptedPassword);
		}
		else {
			hr = ProcessResponse(stRequestFileName, stResponseFileName, stOutputFileName);
		}
		if (FAILED(hr)) {
			return hr;
		}
		m_vstOutputFileNames.push_back(stOutputFileName);
	}
	if(m_vstOutputFileNames.size()>1){
		hr = MergeOutputDocuments();
		if (FAILED(hr)) {
			return hr;
		}
	}
	return S_OK;
}

unsigned long WebServiceHandler::PrepareRequest(int nReq, const string& stRequestFileName) {
	PROFILING doPrf(getTreeMapper(), "PrepareRequest");
	if (nReq > 0) {
                                                      
		const long lMilliSleep = m_restrictions.GetDelay();
		if (lMilliSleep > 0) {
			konga::MilliSleep(lMilliSleep);
		}
	}

	try {
		PROFILING doPrf(getTreeMapper(), "RunPipelinePluginForRequest");
		RunPipelinePlugin(WebServiceHandler::Request, stRequestFileName);
	}
	catch( const CrException& ex ) {
		m_stLastError += "Failed to run pipeline plugin on web service request: " + ex.GetErrorString() + "\n";
		return E_FAIL;
	}
	return S_OK;
}

string WebServiceHandler::GetResponseFileName(int nReq) {
	return (nReq == 0) ? m_io.GetResponseFilename() : GetIndexedFileName(m_io.GetResponseFilename(), nReq);
}

unsigned long WebServiceHandler::SendRequest(const string& stRequestFile, 
											 const string& stResponseFile, 
											 const string& stCookiejarFile,
											 const string& stPassword)
{
	PROFILING doPrf(getTreeMapper(), "SendRequest");
	string stErr;
	unsigned long hr = WebServiceUtils::CallWebService(replace_de(m_info.GetUrl()),
										  replace_de(m_info.GetSoapAction()),
										  stRequestFile,
										  stResponseFile,
										  m_restrictions.GetMaxSizeBytes(),
										  stCookiejarFile,
										  stErr,
										  replace_de(m_info.GetHttpUserName()),
										  replace_de(stPassword),
										  m_httpOptions,
										  m_info.GetMaxRedirects(),
										  m_info.GetNumberOfRetries(),
										  m_info.GetRetryWaitTimeInSeconds(),
										  m_info.GetTimeOutSeconds());
	if (FAILED(hr)) {
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_WEBSERVICECALL_FAILED, stErr.c_str());
		if (!m_stLastError.empty()) {
			m_stLastError += "\n";
		}
		m_stLastError += m_errorLog.GetLastErr()+"\n";
	}
	else if ( !stErr.empty() ) {
		m_viewLog.LogMessage("%s", stErr.c_str());
	}
	return hr;
}

unsigned long WebServiceHandler::ProcessResponse(const string& stRequestFileName, const string& stResponseFileName, const string& stOutputFileName) {
	PROFILING doPrf(getTreeMapper(), "ProcessResponse");
	try {
		PROFILING doPrf(getTreeMapper(), "RunPipelinePluginForResponse");
		RunPipelinePlugin(WebServiceHandler::Response, stResponseFileName);
	}
	catch( const CrException& ex ) {
		m_stLastError += "Failed to run pipeline pluing on web service response. " + ex.GetErrorString() + "\n";
		return E_FAIL;
	}
	                                                         
	const string stNameOfRequestFileToIncludeInResponse(GetNameOfRequestFileToAttachToResponse(stRequestFileName));
	unsigned long hr = EnvelopeToXsdStructure(stResponseFileName,
								stOutputFileName,
								stNameOfRequestFileToIncludeInResponse,
								WebServiceHandler::Response,
								true,
								true);
	if (FAILED(hr)) {
		return hr;
	}
	if (m_syncResponse) {
		PROFILING doPrf(getTreeMapper(), "SynchronizeResponse");
		WebServiceResponseSynchronizer responseSynchronizer(m_structure.GetResponseRoot(), 
			m_structure.GetResponseSchemaNamespaces(), stOutputFileName, stOutputFileName, XsdFromWsdlCreator::ROOT);
		hr = responseSynchronizer.Synchronize();
		if (FAILED(hr)) {
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_WS_CALL_SYNCHRONIZE_RESPONSE_FAILED, 
				responseSynchronizer.GetLastError().c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}
	}
	return S_OK;
}

std::string WebServiceHandler::replace_de(const std::string& value) {
	return m_pTreeMapper->replaceGlobalDataElementsInString(value);
}


string WebServiceHandler::GetNameOfRequestFileToAttachToResponse(const string& stNameOfRequestSentToServer) const {
	string s(stNameOfRequestSentToServer);
	map<string, string>::const_iterator arrayConvertedFile = m_arrayConvertedFiles.find(stNameOfRequestSentToServer);
	if (arrayConvertedFile != m_arrayConvertedFiles.end()) {
		s = arrayConvertedFile->second;
	}
	return s;
}

namespace {
	bool HasRequestData(const string& filename) {
		if (konga::FileExists(filename)) {
			long lErr;
			return GetFileSize(filename, lErr) > 0L;
		}
		return false;
	}
}

unsigned long WebServiceHandler::CheckForEmptyInput() {
	PROFILING doPrf(getTreeMapper(), "CheckForEmptyInput");
	if (HasRequestData(m_io.GetInputFilename())) {
		return S_OK;
	}
	                                                                                                                 
	                                                                              
	if ( m_structure.RequestHasValueContent() ) {
		m_bEmptyInput = true;
		return S_OK;
	}
	else {
		                                                                                   
		try {
			m_structure.CreateEmptyRequest(m_io.GetInputFilename());
		}
		catch (CrException ex) {
			unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATENOVALUECONTENTXML_FAILED, 
				ex.GetErrorString().c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}
	}
	return S_OK;
}

string WebServiceHandler::GetSoapEnvelopeNamespacePrefix()
{
	string stDeclaration = "xmlns:soapenv";
	int nPrefix = 0;
	map<string, string>::const_iterator cit = m_mapOtherNamespaces.find( stDeclaration );
	while (cit != m_mapOtherNamespaces.end()) {
		if (konga::Equals(cit->second, SoapConstants::NAMESPACE_V11)) {
			                                                                                        
			                         
			break;
		}
		                                                                                  
		stDeclaration = "xmlns:soapenv" + konga::toString(++nPrefix);
		cit = m_mapOtherNamespaces.find(stDeclaration);
	}
	m_stSoapEnvelopePrefix = stDeclaration.substr(6);
	return stDeclaration;
}


unsigned long WebServiceHandler::CreateSoapContent(WebServiceHandler::Mode mode, const string& stSoapEnvelopeFilename         )
{
	PROFILING doPrf(getTreeMapper(), "CreateSoapContent");
	unsigned long hr = S_OK;
	try {
		const string& stOriginalXmlFile = (mode == WebServiceHandler::Request) ? m_io.GetInputFilename() : m_io.GetOutputFilename();
		DOMElementPtr pOriginalRoot = 0;
		hr = LoadXmlRequestFromTransformation(stOriginalXmlFile, pOriginalRoot);
		if (FAILED(hr)) {
			return hr;
		}

		                                                                                            
		                                            
		DOMNodeListPtr pTransactionList = pOriginalRoot->getChildNodes();

		                                                                                 
		GetSoapEnvelopeNamespacePrefix();
		string stEnvelope = XmlUtils::GetQualifiedName(m_stSoapEnvelopePrefix, "Envelope");
		
		CROM* pTransactionCromNode = GetTransactionNode(mode);

		                                                                                          
		                                                
		for (unsigned int i0 = 0, fileCounter = 0; i0 < pTransactionList->getLength() ; i0++) {
			DOMElementPtr pTransactionNode = pTransactionList->item[i0];
			if (pTransactionNode == 0 || pTransactionNode->getNodeType() != konga::xml::NODE_ELEMENT) {
				continue;
			}
			hr = CreateSoapContentForTransactionNode(pTransactionNode, pTransactionCromNode, pOriginalRoot, 
				stEnvelope, stSoapEnvelopeFilename, mode, fileCounter);
			if (FAILED(hr)) {
				return hr;
			}
			++fileCounter;
		}
	}
	catch (CrException& e) {
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPCONTENT_FAILED, 
			e.GetErrorString().c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		if (m_bCreateSoapFaultOnError) {
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, m_stLastError) );
		}
		return hr;
	}
#ifndef _DEBUG
	catch(...) {
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPCONTENT_UNKNOWN_ERROR,
			(mode == WebServiceHandler::Request) ? "input" : "output");
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		if (m_bCreateSoapFaultOnError) {
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, m_stLastError) );
		}
		return hr;
	}
#endif
	return S_OK;
}

unsigned long WebServiceHandler::LoadXmlRequestFromTransformation(const std::string stOriginalXmlFile,
																  DOMElementPtr& pOriginalRoot) {
	PROFILING doPrf(getTreeMapper(), "LoadXmlRequestFromTransformation");
	std::string stLoadingErr;
	                                                                                   
	                                                                                  
	                                           
	DOMDocument2Ptr pOriginalDoc = XmlUtils::LoadDoc( stOriginalXmlFile, stLoadingErr, false);
	if (pOriginalDoc == 0) {
		if (m_bCreateSoapFaultOnError) {
			                                                                                           
			                                                                                                   
			                                                                            
			                                                                                              
			                                   
			std::string stFaultString = "Failed to open the Xml document that was to be wrapped in a SOAP envelope. "
				"Reported error was: ";
			stFaultString += stLoadingErr;
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, stFaultString) );
		}
		unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPDOMDOCUMENT_FAILED, stLoadingErr.c_str());
		m_stLastError += m_errorLog.GetLastErr()+"\n";
		return hr;
	}
	pOriginalRoot = pOriginalDoc->getDocumentElement();
	if (pOriginalRoot == 0) {
		unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPCONTENT_NO_ROOT, stOriginalXmlFile.c_str());
		m_stLastError += m_errorLog.GetLastErr()+"\n";
		if (m_bCreateSoapFaultOnError) {
			                                                                                           
			                                                                                                   
			                                                                            
			                                                                                              
			                                   
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, m_stLastError) );
		}
		return hr;
	}
	return S_OK;
}

unsigned long WebServiceHandler::CreateSoapContentForTransactionNode(DOMElementPtr pTransactionNode,
																	 CROM* pTransactionCromNode,
																	 DOMElementPtr pOriginalRoot,
																	 const string& stEnvelope,
																	 const string& stSoapEnvelopeFilename,
																	 WebServiceHandler::Mode mode,
																	 unsigned int fileCounter) {
	PROFILING doPrf(getTreeMapper(), "CreateSoapContentForTransactionNode");
	                                                                                   
	DOMElementPtr pOriginalHeader = XmlUtils::GetChildElement(pTransactionNode, XsdFromWsdlCreator::HEADER);
	DOMElementPtr pOriginalBody = XmlUtils::GetChildElement(pTransactionNode, XsdFromWsdlCreator::BODY);

	DOMDocument2Ptr pRequestDoc;
	unsigned long hr = XmlUtils::CreateDOMDocument2Instance( pRequestDoc, true, m_stLastError );
	if (FAILED(hr)) {
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPDOMDOCUMENT_FAILED, m_stLastError.c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		if (m_bCreateSoapFaultOnError) {
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, m_stLastError) );
		}
		return hr;
	}

	string stSchemaTypeAttributeName;
	DOMElementPtr pRequestEnvelope = CreateRequestEnvelopeElement(pRequestDoc, pOriginalRoot, stEnvelope, stSchemaTypeAttributeName);

	if (pOriginalHeader != 0) {
		hr = CreateSoapHeader(pRequestDoc, 
							  pRequestEnvelope, 
							  pOriginalHeader, 
							  stSchemaTypeAttributeName, 
							  pTransactionCromNode);
		if (FAILED(hr)) {
			if (m_bCreateSoapFaultOnError) {
				std::string stFaultString = "The structure and/or contents of the generated SOAP-header "
					"is invalid according to the WSDL file.";
				SetSoapFault( new SoapFault_11(FaultCodeValue_11::Client, stFaultString) );
			}
			                                                              
			return hr;
		}
	}

	string stNewFilename(stSoapEnvelopeFilename);
	if (stNewFilename.empty()) {
		                                                                   
		stNewFilename = (mode == WebServiceHandler::Request) ? m_io.GetRequestFilename() : m_io.GetResponseFilename();
	}
	if (fileCounter > 0) {
		stNewFilename = GetIndexedFileName(stNewFilename, fileCounter);
	}

	hr = CreateSoapBody(pRequestDoc,
						pRequestEnvelope,
						pOriginalBody,
						stSchemaTypeAttributeName,
						pTransactionCromNode,
						stNewFilename);
	if (FAILED(hr)) {
		if (m_bCreateSoapFaultOnError) {
			std::string stFaultString = "The structure and/or contents of the generated SOAP-body "
				"is invalid according to the WSDL file.";
			SetSoapFault( new SoapFault_11(FaultCodeValue_11::Client, stFaultString, "", stFaultString) );
		}
		                                                              
		return hr;
	}

	if (!pRequestDoc->save(stNewFilename)) {
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_SAVING_SOAPENVELOPE_FAILED,
			(hr == E_FAIL) ? "Unknown error." : ErrorResolver().Resolve(hr).c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		return hr;
	}
	m_vstRequestFileNames.push_back(stNewFilename);
	return S_OK;
}

DOMElementPtr WebServiceHandler::CreateRequestEnvelopeElement(DOMDocument2Ptr pRequestDoc, 
															  DOMElementPtr pOriginalRoot, 
															  const string& stEnvelope,
															  string& stSchemaTypeAttributeName) const {
	PROFILING doPrf(getTreeMapper(), "CreateRequestEnvelopeElement");
	DOMElementPtr pRequestEnvelope = pRequestDoc->createNode(
		konga::xml::NODE_ELEMENT, stEnvelope, SoapConstants::NAMESPACE_V11);
	pRequestDoc->appendChild(pRequestEnvelope);
	XmlNamespaceDeclarations envelopeNamespaces(pRequestEnvelope);
	for (map<string, string>::const_iterator cit = m_mapOtherNamespaces.begin(); cit != m_mapOtherNamespaces.end(); ++cit) {
		string prefix(cit->first);
		                                                                                
		                                                                               
		                                                                                      
		if (envelopeNamespaces.IsPrefixUsed(prefix) || Equals("xmlns", prefix)) {
			continue;
		}
		string uri(cit->second);
		pRequestEnvelope->setAttribute(prefix, uri);
	}
	string stTargetNamespacePrefix;
	string stErr;
	XmlUtils::TransferNameSpace(pOriginalRoot, pRequestEnvelope, false, true, stTargetNamespacePrefix, stErr);
	                                                                                                          
	                                                                                                     
	                            
	string stSchemaInstanceNamespacePrefix = DeclareSchemaInstanceNamespace(pRequestEnvelope, envelopeNamespaces);
	stSchemaTypeAttributeName = XmlUtils::GetQualifiedName(stSchemaInstanceNamespacePrefix, "type");
	return pRequestEnvelope;
}

string WebServiceHandler::DeclareSchemaInstanceNamespace(DOMElementPtr& pEnvelopeNode,
													     XmlNamespaceDeclarations& envelopeNamespaces) const
{
	envelopeNamespaces.Load(pEnvelopeNode);

	       
	XsdConstants::SchemaVersion schemaVersion(XsdConstants::SchemaVersion_2001);
	  
                                                                                                      
                                                            
                                                      
  
   
	string stSchemaInstanceNamespace = XsdUtils::GetSchemaInstanceNamespace(schemaVersion);

	string stSchemaInstanceNamespacePrefix = envelopeNamespaces.GetNamespacePrefix(stSchemaInstanceNamespace);
	if (stSchemaInstanceNamespacePrefix.empty()) {
		stSchemaInstanceNamespacePrefix = "xsi";
		int nPrefix = 0;
		while (envelopeNamespaces.IsPrefixUsed(stSchemaInstanceNamespacePrefix)) {
			stSchemaInstanceNamespacePrefix = "xsi" + konga::toString(++nPrefix);
		}
		envelopeNamespaces.AddNamespaceDeclaration(stSchemaInstanceNamespace, stSchemaInstanceNamespacePrefix);
		string stTmp = "xmlns:" + stSchemaInstanceNamespacePrefix;
		pEnvelopeNode->setAttribute(stTmp, stSchemaInstanceNamespace);
	}
	return stSchemaInstanceNamespacePrefix;
}

unsigned long WebServiceHandler::CreateSoapHeader(DOMDocument2Ptr& pRequestDoc,
											DOMElementPtr& pEnvelopeNode,
											const DOMElementPtr& pOriginalHeader,
											const string& stSchemaTypeAttributeName,
											CROM* pTransactionCromNode)
{
	PROFILING doPrf(getTreeMapper(), "CreateSoapHeader");
	try
	{
		                               
		const string& stHeader = XmlUtils::GetQualifiedName(m_stSoapEnvelopePrefix, "Header");
		DOMElementPtr pRequestHeader = pRequestDoc->createNode(konga::xml::NODE_ELEMENT, 
																		stHeader,
																		"");                                                                                                                                                                       
                                                          
		pEnvelopeNode->appendChild(pRequestHeader);

		DOMNodeListPtr pChildList = pOriginalHeader->getChildNodes();
		for( unsigned int j = 0; j < pChildList->getLength(); j++ )
		{
			DOMElementPtr pChild = pChildList->item[j];
			if( pChild != 0 && pChild->getNodeType() == konga::xml::NODE_ELEMENT )
			{
				pRequestHeader->appendChild( pRequestDoc->importNode(pChild, true) );
			}
		}

		                                                                                       
		                                                                                           
		                                                                                                 
		                                                                             
		  
		                                                                                    
		CROM* pHeaderCromNode = pTransactionCromNode->FindDescendant(CROM_t_Element, 
																	XsdFromWsdlCreator::HEADER, 
																	XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);

		std::string stError;
		unsigned long hr = WebServiceUtils::SetSchemaTypeAttribute(pRequestHeader, 
															 pHeaderCromNode,
															 stSchemaTypeAttributeName,
															 stError);
		if( FAILED(hr) )
		{
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPHEADER_FAILED, stError.c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}
	}
	catch( CrException& e )
	{
		unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPHEADER_FAILED, 
			e.GetErrorString().c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		return hr;
	}
	return S_OK;
}

unsigned long WebServiceHandler::CreateSoapBody(DOMDocument2Ptr& pRequestDoc,
												DOMElementPtr& pRequestEnvelope,
												const DOMElementPtr& pOriginalBody,
												const string& stSchemaTypeAttributeName,
												CROM* pTransactionCromNode,
												const string& stFilename)
{
	PROFILING doPrf(getTreeMapper(), "CreateSoapBody");
	try
	{
		const string& stBody = XmlUtils::GetQualifiedName(m_stSoapEnvelopePrefix, "Body");
		DOMElementPtr pRequestBody = pRequestDoc->createNode(konga::xml::NODE_ELEMENT, 
															stBody, "");                                                                                                                                                                       
                                                          
		if( m_info.GetSoapBodyUse() == SoapConstants::Encoded )
		{
			const string stEncodingAttributeName = XmlUtils::GetQualifiedName(m_stSoapEnvelopePrefix, 
																		SoapConstants::ENCODING_STYLE);
			pRequestBody->setAttribute( stEncodingAttributeName, SoapEncoding::VERSION_11_NAMESPACE );
		}
		pRequestEnvelope->appendChild(pRequestBody);

		DOMNodeListPtr pChildList = pOriginalBody->getChildNodes();
		for( unsigned int j = 0; j < pChildList->getLength(); j++ )
		{
			DOMElementPtr pChild = pChildList->item[j];
			if( pChild != 0 && pChild->getNodeType() == konga::xml::NODE_ELEMENT )
			{
				DOMNodePtr pClone = pRequestDoc->importNode(pChild,true);
				pRequestBody->appendChild(pClone);
			}
		}

		                                                                                       
		                                                                                         
		                                                                                                 
		                                                                             
		  
		                                                                                    
		CROM* pBodyCromNode = pTransactionCromNode->FindDescendant(CROM_t_Element, 
																XsdFromWsdlCreator::BODY, 
																XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
		std::string stError;
		unsigned long hr = WebServiceUtils::SetSchemaTypeAttribute(pRequestBody, 
															 pBodyCromNode, 
															 stSchemaTypeAttributeName,
															 stError);
		if( FAILED(hr) )
		{
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPBODY_FAILED, stError.c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}

		SoapBodyArrayHandler arrayHandler(pRequestDoc, pRequestBody, pBodyCromNode, stSchemaTypeAttributeName);
		if (arrayHandler.hasArrayElements()) {
			                                                                           
			                                                                     
			                                        
			const string stBeforeArrayConversionFilename = stFilename + "_bac";                               
			if (!pRequestDoc->save(stBeforeArrayConversionFilename)) {
				m_stLastError += XmlUtils::GetLastError(pRequestDoc);
				return E_FAIL;
			}
			m_arrayConvertedFiles[stFilename] = stBeforeArrayConversionFilename;
			arrayHandler.handleArrays();
		}
		if ( GetOptionAsBoolean("Settings", "CleanSoapRequests", false) ) {
			SoapEnvelopeCleaner().CleanSoapEnvelope(pRequestEnvelope);
		}
	}
	catch( CrException& e )
	{
		unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_CREATESOAPBODY_FAILED, 
			e.GetErrorString().c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		return hr;
	}
	return S_OK;
}

unsigned long WebServiceHandler::EnvelopeToXsdStructure(const string& stOriginalXmlFilename,
												  const string& stOutputFilename,
												  const string& stRequestFilename,
												  WebServiceHandler::Mode mode,
												  bool bIncludeRequestInResponse,
												  bool bWrapBodyAndHeader)
{
	PROFILING doPrf(getTreeMapper(), "EnvelopeToXsdStructure");
	unsigned long hr = S_OK;
	try
	{
		DOMDocument2Ptr pOutputDoc;
		hr = XmlUtils::CreateDOMDocument2Instance(pOutputDoc, true, m_stLastError);
		if( FAILED(hr) ) {
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_DOMDOC_FAILED, m_stLastError.c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			if( m_bCreateSoapFaultOnError )
				SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, m_stLastError) );
			return hr;
		}

		                                                                                  
		const string etlforgePrefix("jb_ws:");
		const string rootName(etlforgePrefix + XsdFromWsdlCreator::ROOT);
		DOMElementPtr pRoot = pOutputDoc->createNode(
			konga::xml::NODE_ELEMENT, rootName, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
		pOutputDoc->appendChild(pRoot);


		if ( !m_bEmptyInput ) {
			                                                                                         
			const string transactionName(etlforgePrefix + XsdFromWsdlCreator::TRANSACTION);
			DOMElementPtr pTransaction = pOutputDoc->createNode(
				konga::xml::NODE_ELEMENT, transactionName, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
			pRoot->appendChild(pTransaction);

			                                     
			hr = CreateTransactionStructure(pOutputDoc, pTransaction, stOriginalXmlFilename, mode, bWrapBodyAndHeader);
			if( FAILED(hr) ) {
				                                                                                     
				return hr;
			}

			                                                                      
			                                                                                                             
			if (mode == Response && bIncludeRequestInResponse && m_structure.ResponseStructureHasRequestNode()) {
				hr = CreateTransactionStructure(pOutputDoc, pTransaction, stRequestFilename, Request, true);
				if ( FAILED(hr)) {
					                                                            
					return hr;
				}
			}
		}

		if( !pOutputDoc->save(stOutputFilename) )
		{
			m_stLastError += XmlUtils::GetLastError(pOutputDoc);
			return E_FAIL;
		}
	}
	catch( CrException& e )
	{
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_CREATE_TRANS_STRUCT_FAILED, 
			stOriginalXmlFilename.c_str(), e.GetErrorString().c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		hr = E_FAIL;
	}
#ifndef _DEBUG
	catch(...)
	{
		hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_CREATE_TRANS_STRUCT_FAILED, 
			stOriginalXmlFilename.c_str(), "Unkown error");
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		hr = E_FAIL;
	}
#endif
	return S_OK;
}

unsigned long WebServiceHandler::CreateTransactionStructure(DOMDocument2Ptr& pOutputDoc,
													  DOMElementPtr& pTransactionNode,
													  const string& stXmlFilename,
													  WebServiceHandler::Mode mode,
													  bool bWrapBodyAndHeader)
{
	try
	{
		unsigned long hr = S_OK;
		DOMDocument2Ptr pXmlDoc = XmlUtils::LoadDoc(stXmlFilename, m_stLastError, false);
		if( pXmlDoc == 0 )
		{
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_DOMDOC_FAILED, m_stLastError.c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			string stXmlContents;
			try {
				konga::read_file_to_string(stXmlFilename, stXmlContents);
				m_stLastError += "\nThe contents of the file:\n";
				m_stLastError += stXmlContents.substr(0,500);
			}
			catch (CrException& e){
				m_stLastError+=e.GetErrorString();
			}
			return hr;
		}
		DOMElementPtr pXmlEnvelope = pXmlDoc->getDocumentElement();

		if( mode == WebServiceHandler::Response )
		{
			string stError;
			hr = WebServiceUtils::ResolveHREFs(pXmlDoc, 
											pXmlEnvelope, 
											SoapConstants::Version_11, 
											       
											                                                            
											XsdConstants::SchemaVersion_2001,
											stError);
			if( FAILED(hr) )
			{
				if( m_bCreateSoapFaultOnError )
				{
					std::string stFaultString = "Failed to resolve ID/HREFs in the SOAP body.";
					SetSoapFault( new SoapFault_11(FaultCodeValue_11::Server, stFaultString, "", stFaultString) );
				}
				hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_RESOLVE_HREFS_FAILED, 
					stXmlFilename.c_str(), stError.c_str());
                m_stLastError += m_errorLog.GetLastErr()+"\n";
				return hr;
			}

		}
		else if ( mode == WebServiceHandler::Request )
		{
			                                                                                                                     
			                                                                                        
			string stTargetNameSpacePrefix;
			string stError;
			if( XmlUtils::TransferNameSpace(pXmlEnvelope,
											pTransactionNode,
											false,
											true,
											stTargetNameSpacePrefix,
											stError) )
			{
				std::string stError2 = "Failed to transfer the namespace declarations from the original "
					"SOAP-envelope node to the request node. " + stError;
				hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_CREATE_TRANS_STRUCT_FAILED,
					stXmlFilename.c_str(), stError2.c_str());
                m_stLastError += m_errorLog.GetLastErr()+"\n";
				return hr;
			}
		}

		DOMElementPtr pBodyHeaderParent = 0;
		if( bWrapBodyAndHeader )
		{
			                                                                                       
			string stNodeName = (mode == WebServiceHandler::Response) ? XsdFromWsdlCreator::RESPONSE : 
																		XsdFromWsdlCreator::REQUEST;
			const string etlforgePrefix("jb_ws:");
			pBodyHeaderParent = pOutputDoc->createNode(
				konga::xml::NODE_ELEMENT, etlforgePrefix + stNodeName, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);

			                                                                                           
			string stTargetNameSpacePrefix;
			string stError;
			if( XmlUtils::TransferNameSpace(pXmlEnvelope, 
											pBodyHeaderParent, 
											false, 
											false, 
											stTargetNameSpacePrefix,
											stError) != 0 )
			{
				std::string stError2 = "Failed to transfer the namespace declarations from the original "
					"SOAP-envelope node to the ";
				stError2 += (mode == WebServiceHandler::Response) ? "response node. " : "request node. ";
				stError2 += stError;
				hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_CREATE_TRANS_STRUCT_FAILED,
					stXmlFilename.c_str(), stError2.c_str());
                m_stLastError += m_errorLog.GetLastErr()+"\n";
				return hr;
			}

			pTransactionNode->appendChild(pBodyHeaderParent);
		}
		else
		{
			pBodyHeaderParent = pTransactionNode;
		}


		                                                                                                  
		CROM* pHeaderCrom = 0;
		if (mode == WebServiceHandler::Response) {
			const TransactionPartStructure* ps = m_structure.GetTransactionStructure()->GetResponseStructure();
			if (ps != 0) {
				pHeaderCrom = ps->GetHeader();
			}
		}
		else {
			const TransactionPartStructure* ps = m_structure.GetTransactionStructure()->GetRequestStructure();
			if (ps != 0) {
				pHeaderCrom = ps->GetHeader();
			}
		}
		if( pHeaderCrom != 0 )
		{
			DOMElementPtr pXmlHeader = XmlUtils::GetChildElement(pXmlEnvelope, "header");
			if( pXmlHeader == 0 )
			{
				                           
			}
			else
			{
				                                                                                 
				const string etlforgePrefix("jb_ws:");
				const string headerName(etlforgePrefix + XsdFromWsdlCreator::HEADER);
				DOMElementPtr pOutputHeader = pOutputDoc->createNode(
					konga::xml::NODE_ELEMENT, headerName, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
				pBodyHeaderParent->appendChild(pOutputHeader);

				                                                                                      
				MoveChildNodes(pXmlHeader, pOutputHeader, pOutputDoc, mode);
			}
		}

		                                                      
		DOMElementPtr pXmlBody = XmlUtils::GetChildElement(pXmlEnvelope, "body");
		if( pXmlBody == 0 )
		{
			hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_MISSING_BODY, stXmlFilename.c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}
		const string etlforgePrefix("jb_ws:");
		const string bodyName(etlforgePrefix + XsdFromWsdlCreator::BODY);
		DOMElementPtr pOutputBody = pOutputDoc->createNode(
			konga::xml::NODE_ELEMENT, bodyName, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE);
		pBodyHeaderParent->appendChild(pOutputBody);
		MoveChildNodes(pXmlBody, pOutputBody, pOutputDoc, mode);
		return S_OK;
	}
	catch( CrException& e )
	{
		unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_PROCESS_SOAPENV_CREATE_TRANS_STRUCT_FAILED, 
			stXmlFilename.c_str(), e.GetErrorString().c_str());
        m_stLastError += m_errorLog.GetLastErr()+"\n";
		return hr;
	}
}

void WebServiceHandler::MoveChildNodes(DOMElementPtr& pOriginalParent,
									   DOMElementPtr& pNewParent,
									   konga::xml::DOMDocumentPtr& pTargetDocument,
									   WebServiceHandler::Mode mode)
{
	if( mode == WebServiceHandler::Response )
	{
		DOMElementPtr pFirstChild = XmlUtils::GetFirstChildElement(pOriginalParent);
		                                                                                   
		XsdConstants::SchemaVersion version = XsdConstants::SchemaVersion_2001;
		string stSchemaInstancePrefix = XmlUtils::GetNamespacePrefix(
			pFirstChild, XsdUtils::GetSchemaInstanceNamespace(version));
		XsdUtils::SynchronizeNilAndTypeAttributes(pFirstChild, stSchemaInstancePrefix);
	}

	DOMNodeListPtr pChildren( pOriginalParent->getChildNodes() );
	for( unsigned int i = 0, n = pChildren->getLength(); i < n; ++i )
	{
		DOMElementPtr pElementNode( pChildren->item[i] );
		if( pElementNode != 0 )
		{
			pNewParent->appendChild( pTargetDocument->importNode(pElementNode, true) );
		}
	}
}

unsigned long WebServiceHandler::MergeOutputDocuments()
{
	PROFILING doPrf(getTreeMapper(), "MergeOutputDocuments");
	DOMDocument2Ptr pDoc0 = 0, pDoc1 = 0;
	DOMElementPtr pE0 = 0, pE1 = 0;
	
	size_t nOutputFiles = m_vstOutputFileNames.size();
	if( nOutputFiles > 1 )
	{
		try
		{
			std::string stError;
			pDoc0 = XmlUtils::LoadDoc(m_vstOutputFileNames[0], stError, false);
			if( pDoc0 == 0 )
			{
				unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_MERGE_MULTIPLE_WS_RESPONSES_LOAD_XML_FAILED,
														m_vstOutputFileNames[0].c_str(), nOutputFiles, stError.c_str());
                m_stLastError += m_errorLog.GetLastErr()+"\n";
				return hr;
			}
			pE0 = pDoc0->getDocumentElement();
			                                             
			for(size_t iOut = 1; iOut < nOutputFiles; iOut++ )
			{
				pDoc1 = XmlUtils::LoadDoc(m_vstOutputFileNames[iOut], stError, false);
				if( pDoc1 == 0 )
				{
					unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_MERGE_MULTIPLE_WS_RESPONSES_LOAD_XML_FAILED,
												m_vstOutputFileNames[iOut].c_str(), nOutputFiles, stError.c_str());
                    m_stLastError += m_errorLog.GetLastErr()+"\n";
					return hr;
				}
				pE1 = pDoc1->getDocumentElement();
				DOMNodeListPtr list = pE1->getChildNodes();
				for( int nChild = 0, nChildren = list->getLength(); nChild < nChildren; ++nChild )
				{
					DOMElementPtr pChild = list->item[nChild];
					if( pChild != 0 )
						pE0->appendChild( pDoc0->importNode(pChild,true) );
				}
			}
			pDoc0->save( m_io.GetOutputFilename() );
		}
		catch( CrException& e )
		{
			unsigned long hr = m_errorLog.MakeError(__FILE__, __LINE__, ID_MERGE_MULTIPLE_WS_RESPONSES_FAILED,
				nOutputFiles, e.GetErrorString().c_str());
			m_stLastError += m_errorLog.GetLastErr()+"\n";
			return hr;
		}
	}
	return S_OK;
}


string WebServiceHandler::GetIndexedFileName(const string&stOldFileName, unsigned int i)
{
	char buffer[32];
	sprintf(buffer,"%d",i);
	string si=buffer;
	string stNewFileName;

	string::size_type ipos = stOldFileName.find_last_of(".");
	if( ipos != string::npos && konga::EqualsIgnoreCase(stOldFileName.substr(ipos), ".xml") )
	{
		stNewFileName = stOldFileName.substr(0, ipos) + string("_") + konga::toString(i) + string(".xml");
	}
	else
	{
		stNewFileName = stOldFileName + string("_") + konga::toString(i) + string(".xml");
	}
	return stNewFileName;
}

unsigned long WebServiceHandler::ScaleDownSoapEnvelope(WebServiceHandler::Mode mode, bool isHostedWebService, const string& stNewFilename) {
	if (!m_structure.IsLoaded()) {
		m_structure.Load(true, isHostedWebService);
	}
	return EnvelopeToXsdStructure(m_io.GetInputFilename(), stNewFilename, "", mode, false, false);
}

unsigned long WebServiceHandler::CreateSoapEnvelopeForHostedResponse(const string& stNewFilename) {
	if (!m_structure.IsLoaded()) {
		m_structure.Load(false, true);
	}
	return CreateSoapContent(WebServiceHandler::Response, stNewFilename);
}

CROM* WebServiceHandler::GetTransactionNode(WebServiceHandler::Mode mode) {
	CROM* root = (mode == WebServiceHandler::Request) ?
		m_structure.GetRequestRoot() : m_structure.GetResponseRoot();
	return root->GetFirstChild();
}

konga::SoapFault* WebServiceHandler::GetSoapFault(bool bAdopt              ) {
	konga::SoapFault* p = m_pSoapFault;
	if( bAdopt )
		m_pSoapFault = 0;
	return p;
}

void WebServiceHandler::SetSoapFault(konga::SoapFault* pSoapFault) {
	delete m_pSoapFault;
	m_pSoapFault = pSoapFault;
}

void WebServiceHandler::RunPipelinePlugin(const Mode mode, const std::string& sFileName) {
	const OperationContext* const pContext = getTreeMapper()->getOperationContext();
	if ( pContext == 0 )
		return;

	OperationDataElementHolder& holder=* getTreeMapper()->getOperationDataElementHolder();

	const long webServiceCallId = m_info.GetWebServiceId();
	if ( webServiceCallId < 0 )
		return;

	const long operationId = pContext->getOperationId();
	const int relativePositionId = mode==Request?0:1;

	PipelinePluginSelector selector;
	PluginManifestListT plugins = selector.GetPipelinePluginInstances(
		operationId, 0, WebServiceEntityId, webServiceCallId, relativePositionId);
	if ( plugins.empty() )
		return;

	SortPipelinePlugins(plugins, WebServiceEntityId);

	vector<string> inputFiles;
	inputFiles.push_back(sFileName);

	PipelinePluginExecutor executor;
	const PipelinePosition pipelinePosition(WebServiceEntityId, relativePositionId, 0, true);
	int iPlugin = 1;
	for( PluginManifestListT::const_iterator citManifest = plugins.begin();
		citManifest != plugins.end(); ++citManifest, ++iPlugin)
	{
		PipelinePluginManifest* pManifest =
			dynamic_cast<PipelinePluginManifest*>(citManifest->get());
		if ( pManifest == 0 )
			continue;
		const PipelinePluginManifest& manifest = *pManifest;

		                                                                                          
		kCopyFile(sFileName, sFileName + "-pre-plugin-" + toString(iPlugin));

		vector<konga::OutputFile> outputFiles;
		bool bHasNewFiles = false;	                   
		executor.ExecutePipelinePlugin(manifest, pipelinePosition,
			*pContext, holder, inputFiles, bHasNewFiles, outputFiles);
		if ( bHasNewFiles && outputFiles.empty() )
			throw CrException("The pipeline plugin \"" + manifest.GetDisplayName()
			+ "\" executed for a web service call produced no files, cannot continue.");
		else if ( bHasNewFiles && outputFiles.size() > 1 )
			throw CrException("The pipeline plugin \"" + manifest.GetDisplayName()
			+ "\" executed for a web service call produced "
			+ toString(outputFiles.size()) + " files where only one is allowed, cannot continue.");
		else if ( !bHasNewFiles) {
			                                                                                        
			                                                   
			if ( !FileExists(sFileName) ) {
				throw CrException("The pipeline plugin \"" + manifest.GetDisplayName()
				+ "\" executed for a web service call produced no new files but deleted the input. "
				+  + "Please contact the plugin author about this problem.");
			}
			continue;
		}

		                                  
		const konga::OutputFile& newFile = *outputFiles.begin();
		if ( !newFile.Warning.GetWarning().empty() ) {
			const FileWarning::Severity severity = newFile.Warning.GetSeverity();
			if ( severity == FileWarning::Severity_Error ) {
				throw CrException("Pipeline plugin \"" + manifest.GetDisplayName()
				+ "\" reported the following error:" + newFile.Warning.GetWarning());
			}
			else if ( severity == FileWarning::Severity_Warning ) {
				getTreeMapper()->getViewLog()->LogMessage("The pipeline plugin '%s' logged the following warning message:\n%s",
					manifest.GetDisplayName().c_str(), newFile.Warning.GetWarning().c_str());
			}
		}

		                                          
		const string& sNewFileName = newFile.Name;
		if ( !FileExists(sNewFileName) ) {
			throw CrException("The pipeline plugin \"" + manifest.GetDisplayName()
				+ "\" reported that an output file was produced but the file does not exist on disk." \
				" Please contact the pipeline author about this problem.");
		}

		kMoveFile(sNewFileName, sFileName, true);
	}
}
