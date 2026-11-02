/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/directory.hpp>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/VectorUtils.h>

#include <konga/Soap/SoapEncoding.h>
#include <konga/Soap/SoapUtils.h>

#include <konga/Xml/XmlSchemaUtils.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlConstants.h>

#include <konga/TransformationEngine/TreeMapper.h>

#include "ErrorCodes/resource.h"
#include "XsdFromWsdlCreator.h"
#include "XSDParser.h"
#include "RemoteSchemaResolver.h"
#include "SchemaCollection.h"
#include "WebServiceUtils.h"
#include "WsdlUtils.h"
#include "XsdParsingResult.h"
#include "XsdUtils.h"
#include "misc.h"

using namespace std;
using namespace konga;
using namespace konga::xml;
using namespace boost::filesystem;

const string XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE = "http://www.etlforge.invalid/XsdFromWsdl";
const string XsdFromWsdlCreator::ROOT = "root";
const string XsdFromWsdlCreator::HEADER = "header";
const string XsdFromWsdlCreator::BODY = "body";
const string XsdFromWsdlCreator::REQUEST = "request";
const string XsdFromWsdlCreator::RESPONSE = "response";
const string XsdFromWsdlCreator::TRANSACTION = "transaction";
const string XsdFromWsdlCreator::HOST_POSTFIX = "_host";


namespace {

	void ConvertNodeListToVector(DOMNodeListPtr nodeList, vector<DOMElementPtr>& nodeVector) {
		for (int n = 0, size = nodeList->getLength(); n < size; ++n) {
			nodeVector.push_back(nodeList->getItem(n));
		}
	}

	bool HasDefinitionsOnItsOwn(const DOMElementPtr& schemaNode) {
		const DOMNodeListPtr childNodes = schemaNode->getChildNodes();
		for (size_t i = 0; i < childNodes->getLength(); ++i) {
			const DOMNodePtr child = childNodes->item[static_cast<unsigned long>(i)];
			if (child->getNodeType() == ELEMENT_NODE) {
				const string name(child->getLocalName());
				if (Equals(name, "element") || Equals(name, "attribute") || 
					Equals(name, "simpleType") || Equals(name, "complexType") ||
					Equals(name, "group") || Equals(name, "attributeGroup")) {
						return true;
				}
			}
		}
		return false;
	}

	                                                                                                                       
                                                                                   

                                             
           
                  
                                                                           
                   
       
                
   
	bool IsCandidateForBug2390081(const DOMElementPtr& schemaNode, const string& schemaTargetNamespace, const string& wsdlTargetNamespace) {
		if (!schemaTargetNamespace.empty()) {
			return false;
		}
		if (HasDefinitionsOnItsOwn(schemaNode)) {
			return false;
		}
		vector<DOMElementPtr> importNodes;
		XsdUtils::GetImportNodes(schemaNode, importNodes);
		if (importNodes.size() == 1) {
			const string importedNamespace(XmlUtils::GetAttributeValue(importNodes[0], "namespace"));
			return (Equals(XmlUtils::GetAttributeValue(importNodes[0], "namespace"), wsdlTargetNamespace));
		}
		return false;
	}

}

XsdFromWsdlCreator::XsdFromWsdlCreator(TreeMapper*p,
									   const string& stWsdlFilename,
									   const string& stBinding,
									   const string& stOperation,
									   const string& stOperationInput,
									   const string& stOperationOutput,
									   const string& stSoapStyle,
									   const string& stSoapBodyUse,
									   const string& stInputXSDFilename,
									   const string& stOutputXSDFilename,
									   CErrorLog* pErrorLog) : 
												m_pTreeMapper(p),
												m_stWsdlFilename(stWsdlFilename),
												m_stBinding(stBinding),
												m_stOperation(stOperation),
												m_stOperationInput(stOperationInput),
												m_stOperationOutput(stOperationOutput),
												m_stSoapStyle(stSoapStyle),
												m_stSoapBodyUse(stSoapBodyUse),
												m_stInputXSDFilename(stInputXSDFilename),
												m_stOutputXSDFilename(stOutputXSDFilename),
												m_bIncludeRequestStructureInResponse(false),
												m_pWsdlRoot(0),
												m_pBindingNode(0),
												m_pPortTypeNode(0),
												m_pPortTypeOperationNode(0),
												m_pBindingOperationNode(0),
												m_pInputMessageNode(0),
												m_pOutputMessageNode(0),
												m_pInputMessagePartNodes(0),
												m_pOutputMessagePartNodes(0),
												m_mainInputSchema(pErrorLog),
												m_mainOutputSchema(pErrorLog),
												m_inputBridgeSchema(pErrorLog),
												m_outputBridgeSchema(pErrorLog),
												m_pErrorLog(pErrorLog),
												m_bRequireValidSchema(true)
{
	m_stFolderPath = konga::GetBranchPath(stInputXSDFilename);
	WsdlUtils::GetWsdlDefinitionSchemaLocation( m_stWsdlDefinitionSchemaLocation );
	SoapEncoding::GetSchemaLocation( SoapEncoding::SoapEncoding_Version_11, m_stSoapEncodingSchemaLocation );
	m_soapStyle = konga::GetSoapStyle(stSoapStyle);
	m_soapBodyUse = konga::GetSoapBodyUse(stSoapBodyUse);
}

XsdFromWsdlCreator::~XsdFromWsdlCreator(void)
{
}

unsigned long XsdFromWsdlCreator::CreateXsdFilesForDisplay()
{
	m_purpose = DISPLAY;
	return CreateXsdFiles();
}

unsigned long XsdFromWsdlCreator::CreateXsdFilesForTransformation()
{
	m_purpose = TRANSFORMATION;
	getTreeMapper()->getViewLog()->DebugString("entering CreateXsdFiles()\n");
	unsigned long hr = CreateXsdFiles();
	getTreeMapper()->getViewLog()->DebugString("CreateXsdFiles() return hr=%d\n",hr);
	return hr;
}

unsigned long XsdFromWsdlCreator::CreateXsdFiles()
{
	unsigned long hr = S_OK;

	m_stSchemaValidationWarnings = "";

	                                                                            
	                
	getTreeMapper()->getViewLog()->DebugString("LoadWsdlDoc()\n");
	hr = LoadWsdlDoc();
	if( FAILED(hr) ){
		getTreeMapper()->getViewLog()->DebugString("failed!\n");
		return hr;
	}

	                                                                                                 
	                                                                                    
	getTreeMapper()->getViewLog()->DebugString("ProcessExternalSchemas()\n");
	ProcessExternalSchemas();
	if (FAILED(hr)) {
		getTreeMapper()->getViewLog()->DebugString("failed!\n");
		return hr;
	}

	                                                                   
	getTreeMapper()->getViewLog()->DebugString("ProcessWsdlSchemas()\n");
	hr = ProcessWsdlSchemas();
	if( FAILED(hr) ){
		getTreeMapper()->getViewLog()->DebugString("failed!\n");
		return hr;
	}

	if( m_soapStyle == SoapConstants::Rpc )
	{
		                                                    
		getTreeMapper()->getViewLog()->DebugString("CreateBridgeSchemas()\n");
		hr = CreateBridgeSchemas();
		if( FAILED(hr) ){
			getTreeMapper()->getViewLog()->DebugString("failed!\n");
			return hr;
		}
	}

	                                                                   
	getTreeMapper()->getViewLog()->DebugString("entering CreateMainSchemas()\n");
	hr = CreateMainSchemas(false);
	if( FAILED(hr) ){
		getTreeMapper()->getViewLog()->DebugString("failed!\n");
		return hr;
	}

	if( m_purpose == TRANSFORMATION )
	{
		                                                                         
		m_purpose = HOSTED_WEBSERVICE;

		  
                                                                        

                       
          
                   
               
            
                                                                                            
               

                         
          
                   
                
                
             
                                                                                             
                
    


		                       
		getTreeMapper()->getViewLog()->DebugString("SetHostedMainSchemaFilenames()\n");
		SetHostedMainSchemaFilenames();

		                           
		getTreeMapper()->getViewLog()->DebugString("XsdFromWsdlCreator::CreateXsdFiles() entering CreateMainSchemas()\n");
		hr = CreateMainSchemas(true);
		if( FAILED(hr) ){
			getTreeMapper()->getViewLog()->DebugString("CreateMainSchemas() failed!\n");
			return hr;
		}
	}
	
	return S_OK;
}


unsigned long XsdFromWsdlCreator::ProcessExternalSchemas() {
	path wsdlFilePath(m_stWsdlFilename);
	const string wsdlFileName(wsdlFilePath.filename().string());
	for (directory_iterator itr(m_stFolderPath); itr != directory_iterator(); ++itr) {
		if (konga::Equals(itr->path().filename().string(), wsdlFileName)) {
			continue;
		}
		const string path(itr->path().string());
		string wsdlErr, xsdErr;
		if (!konga::XmlSchemaUtils::IsWsdlFile(path, wsdlErr) && konga::XmlSchemaUtils::IsSchemaFile(path, xsdErr)) {
			XsdUtils xsdutils(getTreeMapper());
			string err;
			unsigned long hr = xsdutils.ReplaceSoapArrayTypes(path, path, err);
			if (FAILED(hr)) {
				hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDLCREATOR_REPLACESOAPARRAYS_FAILED, path.c_str(), err.c_str());
				m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
				return hr;
			}
		}
		else {
			                                                      
		}
	}
	return S_OK;
}


  
                                                                                          
                                                                                           
                    
  
unsigned long XsdFromWsdlCreator::ProcessWsdlSchemas()
{
	                                                                             
	                                            
	unsigned long hr = FindWsdlSchemas();
	if( FAILED(hr) )
		return hr;

	                                                                             
	                                                           
	if( FAILED(AddMissingImportsToWsdlSchemas()) )
	{
		                                                                                                  
		                                 
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_ADDMISSINGIMPORTS_TO_WSDLXSD_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                
	hr = CorrectKnownSchemaLocations();
	if( FAILED(hr) ) {
		return hr;
	}

	hr = ResolveRemoteSchemaLocations();
	if (FAILED(hr)) {
		return hr;
	}

	                                                                                             
	                                                                                               
	                                                                              
	                                                                                            
	hr = ReplaceSoapArrayTypes();
	if( FAILED(hr) ) {
		return hr;
	}

	                                                                                              
	                                                           
	hr = AddMissingImportsToWsdlSchemas();
	if( FAILED(hr) )
	{
		                                                                                                  
		                                 
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_ADDMISSINGIMPORTS_TO_WSDLXSD_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                      
	                                                                       
	hr = SetAnyElementProcessContentsAttribute();
	if( FAILED(hr) ) {
		return hr;
	}



	                                                                             
	                                                                                
	                                            
	hr = SaveWsdlSchemas();
	if( FAILED(hr) ) {
		return hr;
	}

	                                                                         
	hr = ValidateWsdlSchemas();
	if( FAILED(hr) ) {
		return hr;
	}

	return S_OK;
}

const string stEventSource("TransformationEngine");

  
                                                                                        
                 
  
unsigned long XsdFromWsdlCreator::LoadWsdlDoc()
{
	try
	{
		                                          
		string stError;
		if( (m_pWsdlDoc = konga::XmlUtils::LoadDoc( m_stWsdlFilename, stError ))==0 )
		{
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
		m_pWsdlRoot = m_pWsdlDoc->getDocumentElement();

		                                                                                               
		                                  
		m_wsdlNamespaces.Load(m_pWsdlRoot);

		                                                                                       
		                                                                          
		m_wsdlSchemaVersion = XsdUtils::GetSchemaVersion(m_wsdlNamespaces);
		if( m_wsdlSchemaVersion == XsdConstants::SchemaVersion_Unknown )
		{
			                                                                           
			                                                 
			m_wsdlSchemaVersion = XsdUtils::GetDefaultSchemaVersion();
		}

		                                                                                        
		                                                                                      
		m_stSchemaDefinitionNamespace = XsdUtils::GetSchemaDefinitionNamespace(m_wsdlSchemaVersion);
		if( m_wsdlNamespaces.IsDefaultNamespace(m_stSchemaDefinitionNamespace) )
			m_stSchemaDefinitionNamespacePrefix = "";
		else
		{
			m_stSchemaDefinitionNamespacePrefix = m_wsdlNamespaces.GetNamespacePrefix(m_stSchemaDefinitionNamespace);
			if( m_stSchemaDefinitionNamespacePrefix.empty() )
				m_stSchemaDefinitionNamespacePrefix = m_wsdlNamespaces.GetNamespacePrefixCreateIfNotExist(m_stSchemaDefinitionNamespace, "xsd");
		}

		                        
		string stLogMsg = "soap = " + WsdlUtils::WSDL_SOAP_NAMESPACE_11;
		stLogMsg += "; soap12 = " + WsdlUtils::WSDL_SOAP_NAMESPACE_12;
		stLogMsg += "; wsdl = " + WsdlUtils::WSDL_NAMESPACE;
		stLogMsg += "; xsd = " + m_stSchemaDefinitionNamespace;
		m_pWsdlDoc->addSelectionNamespace("soap", WsdlUtils::WSDL_SOAP_NAMESPACE_11);
		m_pWsdlDoc->addSelectionNamespace("soap12", WsdlUtils::WSDL_SOAP_NAMESPACE_12);
		m_pWsdlDoc->addSelectionNamespace("wsdl", WsdlUtils::WSDL_NAMESPACE);
		m_pWsdlDoc->addSelectionNamespace("xsd", m_stSchemaDefinitionNamespace);

		                                                                                  
		                                        

		                           
		std::string stQuery = "//wsdl:definitions/wsdl:binding[@name='" + m_stBinding + "']";
		m_pBindingNode = m_pWsdlDoc->selectSingleNode(stQuery);
		if( m_pBindingNode == 0 )
		{
			stError = "Failed to locate the <binding> '";
			stError += m_stBinding;
			stError += "' in the WSDL file '";
			stError += m_stWsdlFilename;
			stError += "'.\nDEBUG: XPath: " + stQuery;
			const string stDebugFileName("/tmp/wsdl.xml");
			m_pWsdlDoc->save(stDebugFileName);
			stError += "\nDocument written to : " + stDebugFileName;
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		m_stPortType = konga::XmlUtils::GetLocalName( konga::XmlUtils::GetAttributeValue(m_pBindingNode, WsdlUtils::TYPE_ATTRIBUTE) );

		                                      
		stQuery += "/wsdl:operation[@name='";
		stQuery += m_stOperation;
		stQuery += "']";
		m_pBindingOperationNode = m_pWsdlDoc->selectSingleNode(stQuery);
		if( m_pBindingOperationNode == 0 )
		{
			stError = "Failed to locate the <operation> '";
			stError += m_stOperation;
			stError += "' in the binding '";
			stError += m_stBinding;
			stError += "' in the WSDL file '";
			stError += m_stWsdlFilename;
			stError += "'.";
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		                          
		string stInputHeadersQuery = stQuery + "/wsdl:input/soap:header";
		m_pInputSoapHeaders = m_pWsdlDoc->selectNodes( stInputHeadersQuery );

		                           
		string stOutputHeadersQuery = stQuery + "/wsdl:output/soap:header";
		m_pOutputSoapHeaders = m_pWsdlDoc->selectNodes( stOutputHeadersQuery );


		                            
		stQuery = "//wsdl:definitions/wsdl:portType[@name='";
		stQuery += m_stPortType;
		stQuery += "']";
		m_pPortTypeNode = m_pWsdlDoc->selectSingleNode(stQuery);
		if( m_pPortTypeNode == 0 )
		{
			stError = "Failed to locate the <portType> '";
			stError += m_stPortType;
			stError += "' in the WSDL file '";
			stError += m_stWsdlFilename;
			stError += "'.";
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		                                       
		m_pPortTypeOperationNode = LocatePortTypeOperationNode(stQuery);
		if( m_pPortTypeOperationNode == 0 )
		{
			stError = "Failed to locate the <operation> '";
			stError += m_stOperation;
			stError += "' in the portType '";
			stError += m_stPortType;
			stError += "' in the WSDL file '";
			stError += m_stWsdlFilename;
			stError += "'.";
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		                            
		DOMElementPtr pOperationInput = m_pPortTypeOperationNode->selectSingleNode("wsdl:input");
		if( pOperationInput!=0 )
		{
			string stInputMessageLocalName = 
				konga::XmlUtils::GetLocalName( konga::XmlUtils::GetAttributeValue(pOperationInput, WsdlUtils::MESSAGE) );
			string stInputMessageQuery = "//wsdl:definitions/wsdl:message[@name='";
			stInputMessageQuery += stInputMessageLocalName;
			stInputMessageQuery += "']";
			m_pInputMessageNode = m_pWsdlDoc->selectSingleNode(stInputMessageQuery);
			if( m_pInputMessageNode == 0 )
			{
				stError = "Failed to locate the <message> '";
				stError += stInputMessageLocalName;
				stError += "' in the WSDL file '";
				stError += m_stWsdlFilename;
				stError += "'.";
				unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
				m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
				return hr;
			}
			m_pInputMessagePartNodes = m_pInputMessageNode->selectNodes("wsdl:part");
		}

		                             
		DOMElementPtr pOperationOutput = m_pPortTypeOperationNode->selectSingleNode("wsdl:output");
		if( pOperationOutput != 0 )
		{
			string stOutputMessageLocalName = 
				konga::XmlUtils::GetLocalName( konga::XmlUtils::GetAttributeValue(pOperationOutput, WsdlUtils::MESSAGE) );
			string stOutputMessageQuery = "//wsdl:definitions/wsdl:message[@name='";
			stOutputMessageQuery += stOutputMessageLocalName;
			stOutputMessageQuery += "']";
			m_pOutputMessageNode = m_pWsdlDoc->selectSingleNode(stOutputMessageQuery);
			if( m_pOutputMessageNode == 0 )
			{
				stError = "Failed to locate the <message> '";
				stError += stOutputMessageLocalName;
				stError += "' in the WSDL file '";
				stError += m_stWsdlFilename;
				stError += "'.";
				unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, stError.c_str());
                m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
				return hr;
			}
			m_pOutputMessagePartNodes = m_pOutputMessageNode->selectNodes("wsdl:part");
		}
		return S_OK;
	}
	catch( const CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_LOADWSDL_FAILED, 
			e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}

DOMElementPtr XsdFromWsdlCreator::LocatePortTypeOperationNode(const string& stQueryBase)
{
	                                       
	string stQuery = stQueryBase + "/wsdl:operation[@name='";
	stQuery += m_stOperation;
	stQuery += "']";
	DOMNodeListPtr nodes = m_pWsdlDoc->selectNodes(stQuery);
	int numberOfNodes = nodes->getLength();
	if (numberOfNodes == 1) {
		return nodes->getItem(0);
	}
	else if (numberOfNodes > 1) {
		                                                             
		stQuery += "/wsdl:input[@name='" + m_stOperationInput + "']";
		DOMElementPtr input = m_pWsdlDoc->selectSingleNode(stQuery);
		if (input != 0) {
			DOMNodePtr parent = input->getParentNode();
			while (parent != 0) {
				if (konga::Equals(parent->getLocalName(), "operation")) {
					return parent;
				}
				parent = parent->getParentNode();
			}
		}
	}
	return 0;
}

  
                                                                                      
                                                                                         
                                                                                       
               
                                                                                          
                                                                                      
                                                            

                                                                                         
                                                  
  
unsigned long XsdFromWsdlCreator::FindWsdlSchemas() {
	try {
		                                                                                    
		string stSchemaFilenameBase = konga::RemoveFileExtension(m_stInputXSDFilename);

		                                                  
		DOMNodeListPtr pSchemaNodes = m_pWsdlDoc->selectNodes( "//wsdl:definitions/wsdl:types/xsd:schema" );
		for (size_t i = 0; i < pSchemaNodes->getLength(); ++i) {
			DOMElementPtr pSchemaNode = pSchemaNodes->item[static_cast<unsigned long>(i)];
			                                             
			konga::xml::DOMDocumentPtr pDoc;
			unsigned long hr = konga::XmlUtils::CreateDOMDocument2Instance(pDoc, true, m_stLastError);
			if (FAILED(hr)) {
				return hr;
			}
			pSchemaNode = pDoc->importNode(pSchemaNode, true);
			pDoc->appendChild(pSchemaNode);

			const string wsdlTargetNamespace(XmlUtils::GetAttributeValue(m_pWsdlRoot, "targetNamespace"));
			const string schemaTargetNamespace(XmlUtils::GetAttributeValue(pSchemaNode, "targetNamespace"));
			if (IsCandidateForBug2390081(pSchemaNode, schemaTargetNamespace, wsdlTargetNamespace)) {
				                                                                                        
				                                                                                             
				                                                                                               
				                                                                                              
				                        
				pSchemaNode->setAttribute("targetNamespace", XSD_FROM_WSDL_NAMESPACE + "/NotReallyANamespace");
			} else {
				                                                                                                   
				                                                                                      
				string stError;
				string stTargetNameSpacePrifix( m_wsdlNamespaces.GetNamespacePrefix(m_wsdlNamespaces.GetTargetNamespace()) );
				if (konga::XmlUtils::TransferNameSpace(m_pWsdlRoot, 
												pSchemaNode, 
												false,	                                                                                                   
												true, 
												stTargetNameSpacePrifix,
												stError) != 0) {
					stError = "Failed to transfer the namespace declarations from the WSDL root to the schema node. " + stError;
					unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_FINDWSDLSCHEMAS_FAILED,
						m_stWsdlFilename.c_str(), stError.c_str());
					m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
					return hr;
				}
				if (schemaTargetNamespace.empty()) {
					pSchemaNode->setAttribute("targetNamespace", XmlUtils::GetAttributeValue(m_pWsdlRoot, "targetNamespace"));
				}
			}

			                                                                                                     
			string stFilename = stSchemaFilenameBase + std::string("_") + konga::toString(static_cast<long>(i+1)) + std::string(".xsd");
                                                 
			m_vstSchemaFilenames.push_back(stFilename);
			
			                                                                                           
			XsdInfo* pXsdInfo = new XsdInfo(pDoc, pSchemaNode);
			pXsdInfo->SetSchemaFilename(stFilename);

			m_wsdlSchemas.AddXsdInfo(pXsdInfo);

			                                                                           
			const string& stSchemaDefNamespace = XsdUtils::GetSchemaDefinitionNamespace( pXsdInfo->GetSchemaVersion() );
			pDoc->addSelectionNamespace("xsd", stSchemaDefNamespace);
		}
	}
	catch( const CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_FINDWSDLSCHEMAS_FAILED,
			m_stWsdlFilename.c_str(), e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
	return S_OK;
}

unsigned long XsdFromWsdlCreator::InsertSchemaNamespaceImport(XsdInfo* pXsdInfo,
															  const std::string& stNamespace,
															  XsdConstants::SchemaVersion version)
{
	std::string stFilePath = XsdUtils::GetSchemaForSchema(version);
	std::string stFileName(konga::GetLeafPath(stFilePath));
	konga::kCopyFile(stFilePath, m_stFolderPath + stFileName);
	unsigned long hr = XsdUtils::InsertImportNode(pXsdInfo->GetSchemaDocument(), 
											pXsdInfo->GetSchemaNode(), 
											pXsdInfo->GetSchemaVersion(), 
											pXsdInfo->GetSchemaNamespacePrefix(), 
											stNamespace, 
											stFileName,
											m_stLastError);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
			stNamespace.c_str(), stFilePath.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
	}
	return hr;
}

  
                                                                                           
                                                                                            
                                                                                           
                                                                                         
                                                                                         
                                                                                        
                                                 
  
unsigned long XsdFromWsdlCreator::AddMissingImportsToWsdlSchemas()
{
	for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
	{
		XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo(i);
		XSDParser parser(getTreeMapper());
		SchemaCollection coll;

		DOMDocumentPtr pSchema = pXsdInfo->GetSchemaDocument();
		const std::string& stCurrentDirectory = GetBranchPath(pXsdInfo->GetSchemaFilename());
		unsigned long hr = parser.LoadSchema( pSchema, coll,
			false, stCurrentDirectory );
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDPARSER_LOAD_FAILED, 
				pXsdInfo->GetSchemaFilename().c_str(), parser.GetLastError().c_str());
			m_stLastError += parser.GetLastError();
			return hr;
		}

		                                                                    
		                                      
		vector<string> vstUsedNamespaces;
		parser.GetUsedNamespaces(vstUsedNamespaces);

		                                                          
		vector<string> vstImportedNamespaces;
		parser.GetImportedNamespaces(vstImportedNamespaces);

		                                                                               
		            
		vector<string> vstMissingImports;
		VectorUtils::GetUniqueStrings(vstUsedNamespaces, vstImportedNamespaces, vstMissingImports);

		                                              
		for(size_t j = 0; j < vstMissingImports.size(); ++j )
		{
			string stNamespace = vstMissingImports[j];
			if (konga::Equals(stNamespace, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001))
			{
				unsigned long hr = InsertSchemaNamespaceImport(pXsdInfo, stNamespace, XsdConstants::SchemaVersion_2001);
				if (FAILED(hr)) 
				{
					return hr;
				}
			}
			else if (konga::Equals(stNamespace, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_1999))
			{
				unsigned long hr = InsertSchemaNamespaceImport(pXsdInfo, stNamespace, XsdConstants::SchemaVersion_1999);
				if (FAILED(hr)) 
				{
					return hr;
				}
			}
			else if( konga::Equals(stNamespace, WsdlUtils::WSDL_NAMESPACE) )
			{
				                                                                            
				                         
				std::string stFileName = konga::GetLeafPath(m_stWsdlDefinitionSchemaLocation);
				konga::kCopyFile(m_stWsdlDefinitionSchemaLocation, m_stFolderPath + stFileName);
				unsigned long hr = XsdUtils::InsertImportNode(pXsdInfo->GetSchemaDocument(), 
														pXsdInfo->GetSchemaNode(), 
														pXsdInfo->GetSchemaVersion(), 
														pXsdInfo->GetSchemaNamespacePrefix(), 
														WsdlUtils::WSDL_NAMESPACE, 
														stFileName,
														m_stLastError);
				if( FAILED(hr) )
				{
					hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
						WsdlUtils::WSDL_NAMESPACE.c_str(), m_stWsdlDefinitionSchemaLocation.c_str());
					m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
					return hr;
				}
			}
			else if( konga::Equals(stNamespace, SoapEncoding::VERSION_11_NAMESPACE) )
			{
				                                                                          
				                         
				std::string stFileName = konga::GetLeafPath(m_stSoapEncodingSchemaLocation);
				konga::kCopyFile(m_stSoapEncodingSchemaLocation, m_stFolderPath + stFileName);
				unsigned long hr = XsdUtils::InsertImportNode(pXsdInfo->GetSchemaDocument(), 
														pXsdInfo->GetSchemaNode(), 
														pXsdInfo->GetSchemaVersion(), 
														pXsdInfo->GetSchemaNamespacePrefix(), 
														SoapEncoding::VERSION_11_NAMESPACE, 
														stFileName,
														m_stLastError);
				if( FAILED(hr) )
				{
					hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
						SoapEncoding::VERSION_11_NAMESPACE.c_str(), m_stSoapEncodingSchemaLocation.c_str());
					m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
					return hr;
				}
			}
			else
			{
				                                                                                      
				                                             
				XsdInfoVector vXsdInfo;
				m_wsdlSchemas.GetXsdInfo(stNamespace, vXsdInfo);
				size_t nMatchingSchemas = vXsdInfo.size();
				if( nMatchingSchemas == 0 )
				{
					                                                                               
					                                                                                   
					                                                              
				}
				else
				{
					for(size_t k = 0; k < nMatchingSchemas; ++k )
					{
						                                
						                                          
						std::string stImportPath = konga::GetLeafPath(vXsdInfo[k]->GetSchemaFilename());
						unsigned long hr = XsdUtils::InsertImportNode(pXsdInfo->GetSchemaDocument(), 
																pXsdInfo->GetSchemaNode(), 
																pXsdInfo->GetSchemaVersion(), 
																pXsdInfo->GetSchemaNamespacePrefix(), 
																stNamespace, 
																stImportPath,
																m_stLastError);
						if( FAILED(hr) )
						{
							hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
								stNamespace.c_str(), stImportPath.c_str());
                            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
							return hr;
						}
					}
				}
			}
		}
	}

	return S_OK;
}

  
                                                                                        
                                                                                         
                                                    

                                  
                                                                                       
                                                                                       
                                                                                          
                                                                                   
                                                                                             
                                                                                        
                                                                                         
                                                                            
  
unsigned long XsdFromWsdlCreator::CorrectKnownSchemaLocations()
{
	try
	{
		for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
		{
			XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo( m_vstSchemaFilenames[i] );
			DOMDocument2Ptr pDoc = pXsdInfo->GetSchemaDocument();
			                           
			vector<DOMElementPtr> vImportNodes;
			XsdUtils::GetImportNodes(pXsdInfo->GetSchemaNode(), vImportNodes);
			for(size_t j = 0; j < vImportNodes.size(); ++j )
			{
				DOMElementPtr pImport = vImportNodes[j];
				string stImportedNamespace = konga::XmlUtils::GetAttributeValue(pImport, konga::XmlConstants::NAMESPACE);
				if( konga::Equals(stImportedNamespace, WsdlUtils::WSDL_NAMESPACE) )
				{
					                                                                            
					                         
					std::string stFileName = konga::GetLeafPath(m_stWsdlDefinitionSchemaLocation);
					konga::kCopyFile(m_stWsdlDefinitionSchemaLocation, m_stFolderPath + stFileName);
					pImport->setAttribute( XsdConstants::SCHEMALOCATION_ATTRIBUTE.c_str(), stFileName );
				}
				else if( konga::Equals(stImportedNamespace, SoapEncoding::VERSION_11_NAMESPACE) )
				{
					                                                                          
					                         
					std::string stFileName = konga::GetLeafPath(m_stSoapEncodingSchemaLocation);
					konga::kCopyFile(m_stSoapEncodingSchemaLocation, m_stFolderPath + stFileName);
					pImport->setAttribute( XsdConstants::SCHEMALOCATION_ATTRIBUTE.c_str(), stFileName.c_str() );
				}
				else
				{
					                                                                                      
					                                             
					XsdInfoVector vXsdInfo;
					m_wsdlSchemas.GetXsdInfo(stImportedNamespace, vXsdInfo);
					
					size_t nMatchingSchemas = vXsdInfo.size();
					if( nMatchingSchemas == 0 )
					{
						                                                                               
						                                                                                   
						                                                              
					}
					else
					{
						                                                                             
						                                                                          
						                                                                      
						                                                                           
						                          

						                                                                              
						                                                                              
						                                                
						for(size_t k = 0; k < vXsdInfo.size(); ++k )
						{
							if( k == 0 )
							{
								                                                                        
								std::string stImportPath = konga::GetLeafPath(vXsdInfo[k]->GetSchemaFilename());
								pImport->setAttribute( XsdConstants::SCHEMALOCATION_ATTRIBUTE, stImportPath );
							}
							else
							{
								                            
								std::string stImportPath = konga::GetLeafPath(vXsdInfo[k]->GetSchemaFilename());
								unsigned long hr = XsdUtils::InsertImportNode(pXsdInfo->GetSchemaDocument(), 
																		pXsdInfo->GetSchemaNode(), 
																		pXsdInfo->GetSchemaVersion(), 
																		pXsdInfo->GetSchemaNamespacePrefix(), 
																		stImportedNamespace, 
																		stImportPath,
																		m_stLastError);
								if( FAILED(hr) )
								{
									                                                                                     
									                                                                          
									                                                                          
									                                                                               
									hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROM_WSDL_CORRECTKNOWNSCHEMALOCS_FAILED, 
										(const char*)m_pErrorLog->GetLastErr());
                                    m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
									return hr;
								}
							}
						}
					}
				}
			}
		}
		return S_OK;
	}
	catch( const CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, 
			ID_XSDFROM_WSDL_CORRECTKNOWNSCHEMALOCS_FAILED, 
                                           e.GetErrorString().c_str());
        m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}


unsigned long XsdFromWsdlCreator::ResolveRemoteSchemaLocations()
{
	try
	{
		for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
		{
			XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo( m_vstSchemaFilenames[i] );
			RemoteSchemaResolver resolver(GetBranchPath(pXsdInfo->GetSchemaFilename()));
			DOMDocument2Ptr pDoc = pXsdInfo->GetSchemaDocument();
			                           
			vector<DOMElementPtr> vImportNodes;
			XsdUtils::GetImportNodes(pXsdInfo->GetSchemaNode(), vImportNodes);
			for(size_t j = 0; j < vImportNodes.size(); ++j )
			{
				DOMElementPtr pImport = vImportNodes[j];
				string stImportedNamespace(XmlUtils::GetAttributeValue(pImport, XmlConstants::NAMESPACE));
				string stSchemaLocation(XmlUtils::GetAttributeValue(pImport, XsdConstants::SCHEMALOCATION_ATTRIBUTE));
				string stLocalLocation(resolver.Resolve(stImportedNamespace, stSchemaLocation));
				pXsdInfo->SetImportedSchemaLocation(stImportedNamespace, stLocalLocation);
				pImport->setAttribute(XsdConstants::SCHEMALOCATION_ATTRIBUTE.c_str(), stLocalLocation);
			}
		}
		return S_OK;
	}
	catch( const CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, 
			ID_XSDFROM_WSDL_RESOLVEREMOTESCHEMALOCS_FAILED, 
                                           e.GetErrorString().c_str());
        m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}

  
                                                                                     
                                                                                       
                                                             

                                                                                   
                                                                                  
                                                                
  
unsigned long XsdFromWsdlCreator::ReplaceSoapArrayTypes()
{
	std::string stError;
	XsdUtils xsdutils(getTreeMapper());
	for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
	{
		XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo(i);
		unsigned long hr = xsdutils.ReplaceSoapArrayTypes( pXsdInfo->GetSchemaDocument(), stError );
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDLCREATOR_REPLACESOAPARRAYS_FAILED,
				pXsdInfo->GetSchemaFilename().c_str(), stError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}
	return S_OK;
}

  
                                                                                     
                                                                                   
                                                                               

                                                                                          
                                                                                       
                                  
  
unsigned long XsdFromWsdlCreator::SetAnyElementProcessContentsAttribute()
{
	XsdUtils xsdutils(getTreeMapper());
	for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
	{
		XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo( m_vstSchemaFilenames[i] );
		string stError;
		if( xsdutils.SetAnyElementProcessContentsAttribute( pXsdInfo->GetSchemaDocument(), XsdConstants::Lax, stError ) != 0 )
		{
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDLCREATOR_SETPROCCONTATTR_FAILED, stError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}
	return S_OK;
}

unsigned long XsdFromWsdlCreator::SaveWsdlSchemas()
{
	std::string stError;
	for(size_t i = 0; i < m_wsdlSchemas.Size(); ++i )
	{
		XsdInfo* pXsdInfo = m_wsdlSchemas.GetXsdInfo(i);
		unsigned long hr = pXsdInfo->SaveSchema(stError);
		if( FAILED(hr) )
		{
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDLCREATOR_SAVE_SCHEMA_FAILED, stError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
		m_vstSavedSchemas.push_back( pXsdInfo->GetSchemaFilename() );
	}
	return S_OK;
}

unsigned long XsdFromWsdlCreator::ValidateWsdlSchemas()
{
	for(size_t i = 0; i < m_vstSavedSchemas.size(); ++i )
	{
		std::string stError;
		if( !konga::XmlUtils::ValidateXmlSchema( m_vstSavedSchemas[i], stError ) )
		{
			if( m_bRequireValidSchema )
			{
				unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_INVALID_WSDLXSD, stError.c_str());
                m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
				return hr;
			}
			else
				AddValidationWarning(stError);
		}
	}
	return S_OK;
}

  
                                                                                                
                                                                                                
                                                       
  
unsigned long XsdFromWsdlCreator::CreateBridgeSchemas()
{
	string stFilenameBase = konga::RemoveFileExtension(m_stInputXSDFilename);

	vector<DOMElementPtr> inputMessageParts;
	ConvertNodeListToVector(m_pInputMessagePartNodes, inputMessageParts);
	vector<DOMElementPtr> inputHeaderParts;
	LookupHeaderParts(m_pInputSoapHeaders, inputHeaderParts);
	                                     
	unsigned long hr = m_inputBridgeSchema.Create(WS_INPUT,
											m_stBinding,
											m_stOperation,
											inputMessageParts,
											inputHeaderParts,
											m_stOperation,
											stFilenameBase,
											m_pWsdlDoc,
											m_wsdlSchemas,
											m_wsdlSchemaVersion);
	if( FAILED(hr) )
	{
		m_stLastError += m_inputBridgeSchema.GetLastError();
		return hr;
	}
	if( FAILED(m_inputBridgeSchema.Validate()) )
	{
		if( m_bRequireValidSchema )
		{
			m_stLastError += m_inputBridgeSchema.GetLastError();
			return hr;
		}
		else
			AddValidationWarning( m_inputBridgeSchema.GetValidationError() );
	}

	                                                                                                        
	                                                                                                   
	                                                                                                 
	                                                                                                         
	                                                                                                 
	                                                                                                       
	                                                                                                   
	                                             
	  
	                                                                                                      
	                                                                                                           
	string stOutputElementName = konga::XmlUtils::GetAttributeValue( m_pOutputMessageNode, WsdlUtils::NAME_ATTRIBUTE );
	if( stOutputElementName.empty() )
		stOutputElementName = "Response";
	if( konga::Equals(stOutputElementName, m_stOperation) )
		stOutputElementName += "2";

	                                   
	vector<DOMElementPtr> outputMessageParts;
	ConvertNodeListToVector(m_pOutputMessagePartNodes, outputMessageParts);
	vector<DOMElementPtr> outputHeaderParts;
	LookupHeaderParts(m_pOutputSoapHeaders, outputHeaderParts);
	hr = m_outputBridgeSchema.Create(WS_OUTPUT,
									 m_stBinding,
									 m_stOperation,
									 outputMessageParts,
									 outputHeaderParts,
									 stOutputElementName,
									 stFilenameBase,
									 m_pWsdlDoc,
									 m_wsdlSchemas,
									 m_wsdlSchemaVersion);
	if( FAILED(hr) )
	{
		m_stLastError += m_outputBridgeSchema.GetLastError();
		return hr;
	}

	if( FAILED(m_outputBridgeSchema.Validate()) )
	{
		if( m_bRequireValidSchema )
		{
			m_stLastError += m_outputBridgeSchema.GetLastError();
			return hr;
		}
		else
			AddValidationWarning( m_outputBridgeSchema.GetValidationError() );
	}

	return S_OK;
}

  
                                                                                          
                                                                                 
  
unsigned long XsdFromWsdlCreator::CreateMainSchemas(bool isHosted)
{
	StructureMode structureMode;
	getTreeMapper()->getViewLog()->DebugString("XsdFromWsdlCreator::CreateMainSchemas() m_stInputXSDFilename=\"%s\", m_stOutputXSDFilename=\"%s\"\n",
														 m_stInputXSDFilename.c_str(),m_stOutputXSDFilename.c_str());
	if(!m_stInputXSDFilename.empty()){
		                                       

		                                                                         
		structureMode.IO = WS_INPUT;			                                         
		structureMode.Purpose = m_purpose;	                                                               

		                                                        
		getTreeMapper()->getViewLog()->DebugString("m_mainInputSchema.Initialize()\n");
		unsigned long hr = m_mainInputSchema.Initialize(structureMode,
										  isHosted,
										  m_pWsdlDoc,
										  m_wsdlSchemas,
										  m_wsdlSchemaVersion,
										  m_stSchemaDefinitionNamespace,
										  m_stSchemaDefinitionNamespacePrefix);
		if( FAILED(hr) )
		{
			getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
			m_stLastError += m_mainInputSchema.GetLastError()+"\n";
			return hr;
		} else {
			getTreeMapper()->getViewLog()->DebugString("success!\n");
		}
		if( m_soapStyle == SoapConstants::Rpc )
		{
			                                                                                  
			                                        
			getTreeMapper()->getViewLog()->DebugString("m_mainInputSchema.CreateRpcStyle\n");
			hr = m_mainInputSchema.CreateRpcStyle(m_stInputXSDFilename, m_pInputSoapHeaders, 0, &m_inputBridgeSchema, 0);
			if( FAILED(hr) )
			{
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainInputSchema.GetLastError()+"\n";
				return hr;
			} else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
		}
		else
		{
			                                                                                  
			                                                                                  
			                           
			getTreeMapper()->getViewLog()->DebugString("m_mainInputSchema.CreateDocumentStyle\n");
			hr = m_mainInputSchema.CreateDocumentStyle(m_stInputXSDFilename, 
														m_pInputMessagePartNodes, 
														m_pInputSoapHeaders);
			if( FAILED(hr) )
			{
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainInputSchema.GetLastError()+"\n";
				return hr;
			} else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
		}

		                                
		getTreeMapper()->getViewLog()->DebugString("m_mainInputSchema.Save()\n");
		hr = m_mainInputSchema.Save();
		if( FAILED(hr) )
		{
			getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
			m_stLastError += m_mainInputSchema.GetLastError()+"\n";
			return hr;
		} else {
			getTreeMapper()->getViewLog()->DebugString("success!\n");
		}
		                                                                                       
		                                                                        
		getTreeMapper()->getViewLog()->DebugString("m_mainInputSchema.Validate()\n");
		hr = m_mainInputSchema.Validate();
		if (FAILED(hr)) {
			if (m_bRequireValidSchema) {
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainInputSchema.GetLastError()+"\n";
				return hr;
			}
			else {
				getTreeMapper()->getViewLog()->DebugString("AddValidationWarning\n");
				AddValidationWarning( m_mainInputSchema.GetValidationError() );
			}
		} 
		else {
			getTreeMapper()->getViewLog()->DebugString("success.\n");
		}

		                                                                                               
		                                                                                    
		if (m_purpose == TRANSFORMATION) {
			getTreeMapper()->getViewLog()->DebugString("create XSDParser parser\n");
			XSDParser parser(getTreeMapper());
			getTreeMapper()->getViewLog()->DebugString("create SchemaCollection collection\n");
			SchemaCollection collection;
			getTreeMapper()->getViewLog()->DebugString("parser.SetContinueOnValidationError\n");
			parser.SetContinueOnValidationError( !m_bRequireValidSchema );
			getTreeMapper()->getViewLog()->DebugString("parser.LoadSchema\n");

			DOMDocumentPtr pSchemaDocument = m_mainInputSchema.GetSchemaDocument();
			const string& stCurrentDirectory = GetBranchPath(m_mainInputSchema.GetSchemaPath());
			hr = parser.LoadSchema(pSchemaDocument, collection, true, stCurrentDirectory);
			if (FAILED(hr)) {
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += parser.GetLastError()+"\n";
				return hr;
			} 
			else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
			getTreeMapper()->getViewLog()->DebugString("parser.Parse(ROOT)\n");
			hr = parser.Parse(ROOT);
			if (FAILED(hr)) {
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += parser.GetLastError()+"\n";
				return hr;
			} 
			else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
			m_bIncludeRequestStructureInResponse = parser.GetParsingResult().HasValueContent();
		}
		else {
			m_bIncludeRequestStructureInResponse = false;
		}
	}

	if(!m_stOutputXSDFilename.empty()){
		                                   
		structureMode.IO = WS_OUTPUT;
		getTreeMapper()->getViewLog()->DebugString("m_mainOutputSchema.Initialize()\n");
		unsigned long hr = m_mainOutputSchema.Initialize(structureMode,
										   isHosted,
										   m_pWsdlDoc,
										   m_wsdlSchemas,
										   m_wsdlSchemaVersion,
										   m_stSchemaDefinitionNamespace,
										   m_stSchemaDefinitionNamespacePrefix);
		if( FAILED(hr) )
		{
			getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
			m_stLastError += m_mainOutputSchema.GetLastError()+"\n";
			return hr;
		} else {
			getTreeMapper()->getViewLog()->DebugString("success!\n");
		}
		DOMNodeListPtr pRequestSoapHeaders = m_bIncludeRequestStructureInResponse ? m_pInputSoapHeaders : 0;
		if( m_soapStyle == SoapConstants::Rpc )
		{
			                                                                                           
			                                                                                                  
			                                                                                            
			                                                        
			XsdFromWsdlBridgeSchema* pInputBridge = m_bIncludeRequestStructureInResponse ? &m_inputBridgeSchema : 0;
			getTreeMapper()->getViewLog()->DebugString("m_mainOutputSchema.CreateRpcStyle()\n");
			hr = m_mainOutputSchema.CreateRpcStyle(m_stOutputXSDFilename, m_pOutputSoapHeaders, pRequestSoapHeaders, pInputBridge, &m_outputBridgeSchema);
			if( FAILED(hr) )
			{
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainOutputSchema.GetLastError()+"\n";
				return hr;
			} else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
		}
		else
		{
			                                                                                  
			                                                                                   
			                            
			                                                                                             
			                                                                                 
			                                                                                        
			             
			DOMNodeListPtr pRequestMessagePartNodes = m_bIncludeRequestStructureInResponse ?
																	m_pInputMessagePartNodes : 0;
			getTreeMapper()->getViewLog()->DebugString("m_mainOutputSchema.CreateDocumentStyle()\n");
			hr = m_mainOutputSchema.CreateDocumentStyle(m_stOutputXSDFilename, 
														m_pOutputMessagePartNodes, 
														m_pOutputSoapHeaders,
														pRequestMessagePartNodes,
														pRequestSoapHeaders);
			if( FAILED(hr) )
			{
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainOutputSchema.GetLastError()+"\n";
				return hr;
			} else {
				getTreeMapper()->getViewLog()->DebugString("success!\n");
			}
		}

		                                
		getTreeMapper()->getViewLog()->DebugString("m_mainOutputSchema.Save()\n");
		hr = m_mainOutputSchema.Save();
		if( FAILED(hr) )
		{
			getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
			m_stLastError += m_mainOutputSchema.GetLastError()+"\n";
			return hr;
		} else {
			getTreeMapper()->getViewLog()->DebugString("success!\n");
		}

		                                                                                       
		                                                                        
		getTreeMapper()->getViewLog()->DebugString("m_mainOutputSchema.Validate()\n");
		hr = m_mainOutputSchema.Validate();
		if( FAILED(hr) )
		{
			if( m_bRequireValidSchema )
			{
				getTreeMapper()->getViewLog()->DebugString("failed!\nerror=%s\n",m_mainInputSchema.GetLastError().c_str());
				m_stLastError += m_mainOutputSchema.GetLastError();
				return hr;
			}
			else {
				getTreeMapper()->getViewLog()->DebugString("AddValidationWarning()!\n");
				AddValidationWarning( m_mainOutputSchema.GetValidationError() );
			}
		} else {
			getTreeMapper()->getViewLog()->DebugString("success.\n");
		}
	}

	return S_OK;
}

void XsdFromWsdlCreator::SetHostedMainSchemaFilenames()
{
	m_stInputXSDFilename = konga::GetFileNameWithTagInserted(m_stInputXSDFilename, HOST_POSTFIX);
	m_stOutputXSDFilename = konga::GetFileNameWithTagInserted(m_stOutputXSDFilename, HOST_POSTFIX);
}

void XsdFromWsdlCreator::AddValidationWarning(const string& stWarning)
{
	if( m_mapLoggedWarnings.find(stWarning) == m_mapLoggedWarnings.end() )
	{
		m_mapLoggedWarnings[stWarning] = true;
		if( m_stSchemaValidationWarnings.length() )
			m_stSchemaValidationWarnings += " ";
		m_stSchemaValidationWarnings += stWarning;
	}
}

DOMElementPtr XsdFromWsdlCreator::LookupHeaderPart(DOMElementPtr pHeaderNode)
{
	string stMessageName = konga::XmlUtils::GetLocalName(
		konga::XmlUtils::GetAttributeValue( pHeaderNode, WsdlUtils::MESSAGE ) );
	string stPartName = konga::XmlUtils::GetLocalName(
		konga::XmlUtils::GetAttributeValue( pHeaderNode, WsdlUtils::PART ) );
	string stQuery = "//wsdl:definitions/wsdl:message[@name='";
	stQuery += stMessageName;
	stQuery += "']/wsdl:part[@name='";
	stQuery += stPartName;
	stQuery += "']";
	return m_pWsdlDoc->selectSingleNode( stQuery.c_str() );
}

void XsdFromWsdlCreator::LookupHeaderParts(DOMNodeListPtr soapHeaders, vector<DOMElementPtr>& parts) {
	for (int n = 0, size = soapHeaders->getLength(); n < size; ++n) {
		DOMElementPtr part = LookupHeaderPart(soapHeaders->getItem(n));
		if (part != 0) {
			parts.push_back(part);
		}
	}
}
