/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/FileUtils.h>
#include <boost/regex.hpp>
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include <konga/Soap/SoapFault.h>
#include <konga/Soap/SoapEnvelope.h>
#include <konga/Soap/SoapEncoding.h>

#include <konga/WebService/SimpleWebServiceClientSoap11.h>
#include <konga/Server/ServerHttpProxyConfiguration.h>

#include "crom.h"
#include "XsdUtils.h"
#include "WebServiceUtils.h"

const char WebServiceUtils::NAMESPACE_SEPARATOR = '^';

using namespace konga;
using namespace konga::xml;
using namespace std;


namespace {
 struct SalesforceRequestRedactor {
   std::string path;
   SalesforceRequestRedactor(const std::string& url,const std::string& file) {
     if(url.find(".salesforce.com/services/Soap/")!=std::string::npos) path=file;
   }
   ~SalesforceRequestRedactor() {
     if(path.empty())return;
     try {
       std::string text; konga::read_file_to_string(path,text);
       const boost::regex session("(<(?:[A-Za-z_][A-Za-z0-9_.-]*:)?sessionId(?:\\s[^>]*)?>)([^<]*)(</(?:[A-Za-z_][A-Za-z0-9_.-]*:)?sessionId\\s*>)");
       text=boost::regex_replace(text,session,[](const boost::smatch& match){return match[1].str()+std::string(match[2].length(),'*')+match[3].str();});
       konga::write_string_to_file(path,text);
     } catch(...) { std::remove(path.c_str()); }
   }
 };

	string GetNamespaceFromParent(DOMElementPtr node) {
		DOMElementPtr parent = node->getParentNode();
		while (parent != 0) {
			string uri(XmlUtils::GetNodeNamespaceURI(parent));
			if (uri.size()) {
				return uri;
			}
			parent = parent->getParentNode();
		}
		return "";
	}

	void GetNodeNameAndNamespace(DOMElementPtr node, string& stNodeName, string& stNodeNamespaceURI) {
		XmlUtils::GetNodeNameAndNamespace(node, stNodeName, stNodeNamespaceURI);
		if (stNodeNamespaceURI.empty()) {
			                                                                                           
			                                                                                   
			                             
			stNodeNamespaceURI = GetNamespaceFromParent(node);
		}
	}
}

unsigned long WebServiceUtils::SetSchemaTypeAttribute(konga::xml::DOMElementPtr pDOMNode, 
												CROM* pCROMNode, 
												const string& stAttributeName,
												string& stError)
{
	try {
		const string stCurrentTypeValue(pDOMNode->getAttribute(stAttributeName));
		string stType(pCROMNode->GetSchemaType());
		if (stCurrentTypeValue.empty() && stType.length() > 0) {
			string stTypeNamespace(pCROMNode->GetSchemaTypeNamespace());
			                                                                             
			                  
			string stPrefix(XmlUtils::GetNamespacePrefix(pDOMNode, stTypeNamespace));
			if (stPrefix.empty()) {
				XmlNamespaceDeclarations elementNamespaces(pDOMNode);
				                                                                              
				                                                                                  
				                                                                   
				stPrefix = XmlUtils::FindNamespacePrefixInChildNodes(pDOMNode, stTypeNamespace);
				if (stPrefix.empty()) {
					                                                                         
					                                                         
					stPrefix = elementNamespaces.GetNamespacePrefixCreateIfNotExist(stTypeNamespace, "ns");
				}
				else {
					                                                                          
					                                        
					int n = 0;
					string stPrefix2 = stPrefix;
					while (elementNamespaces.IsPrefixUsed(stPrefix2)) {
						stPrefix2 = stPrefix + konga::toString(++n);
					}
					stPrefix = stPrefix2;
				}
				                                             
				XmlUtils::AddNamespaceDeclaration(pDOMNode, stTypeNamespace, stPrefix);
			}
			string stValue = XmlUtils::GetQualifiedName(stPrefix, stType);
			pDOMNode->setAttribute(stAttributeName.c_str(), stValue.c_str());
		}

		konga::xml::DOMNodeListPtr pDOMChildren = pDOMNode->getChildNodes();
		for (int i = 0, iMax = pDOMChildren->getLength(); i < iMax; ++i) {
			konga::xml::DOMNodePtr pDOMChild = pDOMChildren->item[i];
			if (pDOMChild->getNodeType() == konga::xml::NODE_ELEMENT) {
				                                   
				string stNodeName, stNodeNamespaceURI;
				GetNodeNameAndNamespace(pDOMChild, stNodeName, stNodeNamespaceURI);
				CROM* pFirstChild = pCROMNode->GetFirstChild(), *pCROMChild = 0;
				if (pFirstChild != 0) {
					if (pFirstChild->IsOneOfNode()) {
						pCROMNode = pFirstChild;
					}
					pCROMChild = pCROMNode->FindChild(CROM_t_Element, stNodeName, stNodeNamespaceURI);
				}
				if (pCROMChild == 0) {
					                                                                                  
					                                            
					return S_OK;
				}
				if (pCROMChild->GetType()&CROM_t_DerivedTypeHolder) {
					string xsi_type(XmlUtils::GetAttributeValue(pDOMChild, "xsi:type"));
					string node_name(XmlUtils::GetLocalName(xsi_type));
					string prefix(XmlUtils::GetNamespacePrefix(xsi_type));
					string ns_url(pDOMChild->lookupNamespaceURI(prefix));
					bool found = false;
					for (CROM* pGrandChild = pCROMChild->GetFirstChild(); pGrandChild; pGrandChild = pGrandChild->GetNextSibling()) {
						if (pGrandChild->GetType()&CROM_t_TypeDefinition) {
							if (node_name == (const char*)pGrandChild->GetName() && ns_url == pGrandChild->GetSchemaTypeNamespace()) {
								pCROMChild = pGrandChild;
								found = true;
								break;
							}
						}
					}
					if (!found){
						                                                                                  
						                                            
						return S_OK;
					}
				}

				unsigned long hr = SetSchemaTypeAttribute(pDOMChild, pCROMChild, stAttributeName, stError);
				if (FAILED(hr)) {
					return hr;
				}
			}
		}
	}
	catch (CrException& e) {
		stError = string("Failed to insert the schema type information into the XML instance.\n") + e.GetErrorString();
		return e.GetErrorNumber();
	}
	return S_OK;
}

unsigned long WebServiceUtils::CallWebService(const string& stUrl,
										const string&stSoapAction,
										const string& stRequestXmlFileName, 
										const string& stResponseXmlFileName,
										long lMaxSizeBytes,
										const string& stCookiejarFileName,
										string& stLastErr,
										const string& stHttpUserName,
										const string& stHttpDecryptedPassword,
										const konga::HttpOptions& httpOptions,
										int follow_redirs,                                                               
										int numberOfRetries,
										int retryWaitInSeconds,
										long timeOutSeconds)
{
	stLastErr.clear();
	unsigned long hr=S_OK;
	if(stUrl.empty()){
		hr=E_FAIL;
		stLastErr="failed due to calling webservice with empty string for url.";
		return hr;
	}

	                                                                                            
   SalesforceRequestRedactor sensitiveRequest(stUrl,stRequestXmlFileName);
   SimpleWebServiceClientSoap11 client;
	client.setUrl(stUrl);
	client.setSoapAction(stSoapAction);
	client.setUserName(stHttpUserName);
	client.setPassword(stHttpDecryptedPassword);
	client.setMaxRequestSizeBytes(lMaxSizeBytes);
	client.setCookiejarFileName(stCookiejarFileName);
	client.setMaxRedirs(follow_redirs);
	client.setTimeOutInSeconds(timeOutSeconds);

	std::auto_ptr<HttpProxyConfiguration> pGlobalProxyConfig( konga::GetHttpProxyConfiguration() );
	if ( client.postSoapEnvelopeWithRetries(stRequestXmlFileName, stResponseXmlFileName, pGlobalProxyConfig.get(), httpOptions, numberOfRetries, retryWaitInSeconds) )
	{
		hr=E_FAIL;
		stLastErr=client.getLastErrorString();
	}
	else {
		                                         
		stLastErr = client.getWarnings();
	}
	return hr;
}

unsigned long WebServiceUtils::GetOperationList(const string&stWsdlFilePath,vector<string>&vMethodNames,string&stErr)
{
	unsigned long hr = S_OK;
    konga::xml::DOMDocumentPtr pWSDLDoc;
    try {
        pWSDLDoc = XmlUtils::LoadDoc(stWsdlFilePath,stErr,true);
    }
    catch(CrException&e){
        stErr= string("GetOperationList failed to create instance for document element!\n")+e.GetErrorString();
    }


	                                                      

	try
	{
		konga::xml::DOMNodePtr pDefinitions=pWSDLDoc->getDocumentElement();
		konga::xml::DOMNodeListPtr pChildren=pDefinitions->getChildNodes();
		konga::xml::DOMNodePtr pPortType=0;
		int n=pChildren->getLength();
		for(int i=0;i<n;i++){
			konga::xml::DOMNodePtr pChild=pChildren->item[i];
			if(pChild->getNodeType()!=konga::xml::NODE_ELEMENT)continue;
			if(pChild->getNodeName().c_str()=="portType"){
				pPortType=pChild;
				break;
			}
		}
		if(pPortType==0){
			stErr=string("GetOperationList(")+stWsdlFilePath+") failed to find element:\"portType\"!";
			return E_FAIL;
		}
		pChildren=pPortType->getChildNodes();
		n=pChildren->getLength();
		for(int i=0;i<n;i++){
			konga::xml::DOMElementPtr pChild=pChildren->item[i];
			if(pChild==0)continue;
			if(pChild->getBaseName().c_str()=="operation")
            {
                string sName= pChild->getAttribute("name");
				vMethodNames.push_back(sName);
            }
		}

	}
	catch(const CrException& e)
	{
		stErr = "GetOperationList(" + stWsdlFilePath + ") encounter unspecified error"+e.GetErrorString();
		return E_FAIL;
	}

	return hr;
}


namespace {

	const string stSOAPENV_PREFIX("soapenv");
	const string stID_NODE_XPATH_BASE("//" + stSOAPENV_PREFIX + ":Envelope/" + stSOAPENV_PREFIX + ":Body/*[@id");
}

unsigned long WebServiceUtils::ResolveHREFs(DOMDocument2Ptr pDoc,
									  konga::xml::DOMElementPtr pEnvelopeNode,
									  SoapConstants::Version soapVersion,
									  XsdConstants::SchemaVersion schemaVersion,
									  string& stError)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	const string& stSchemaInstanceNamespace = XsdUtils::GetSchemaInstanceNamespace(schemaVersion);

	pDoc->addSelectionNamespace(stSOAPENV_PREFIX, (soapVersion == SoapConstants::Version_11) ? SoapConstants::NAMESPACE_V11 : SoapConstants::NAMESPACE_V12);

	XmlNamespaceDeclarations namespaces(pEnvelopeNode);
	string stSchemaInstancePrefix = namespaces.GetNamespacePrefix( stSchemaInstanceNamespace );
	if( stSchemaInstancePrefix.empty() )
		stSchemaInstancePrefix = "xsi";

	DOMNodeListPtr envelopeChildren = pEnvelopeNode->getChildNodes();
	for (unsigned long n = 0, len = envelopeChildren->getLength(); n < len; ++n) {
		DOMNodePtr child = envelopeChildren->getItem(n);
		if (child->getNodeType() == ELEMENT_NODE) {
			unsigned long hr = FindAndReplaceHREFs(pDoc, child, stSchemaInstancePrefix, stError);
			if (FAILED(hr)) {
				return hr;
			}
		}
	}

	                                 
	string stXPath = stID_NODE_XPATH_BASE + "]";
	konga::xml::DOMNodeListPtr pIdNodes = pDoc->selectNodes( stXPath.c_str() );
	int nIdNodes = pIdNodes->getLength();
	vector<konga::xml::DOMNodePtr> vIdNodes;
	for( int i = 0; i < nIdNodes; ++i )
		vIdNodes.push_back( pIdNodes->item[i] );
	for( int i = 0; i < nIdNodes; ++i )
	{
		konga::xml::DOMNodePtr pIdNode = vIdNodes[i];
		konga::xml::DOMNodePtr pParent = pIdNode->getParentNode();
		if( pParent != 0 )
			pParent->removeChild(pIdNode);
	}

	return S_OK;
}

unsigned long WebServiceUtils::FindAndReplaceHREFs(DOMDocument2Ptr pDoc,
											 konga::xml::DOMElementPtr pElement,
											 const string& stSchemaInstancePrefix,
											 string& stError)
{
	string stHREF = XmlUtils::GetAttributeValue(pElement, "href");
	if( !stHREF.empty() )
	{
		string stID = stHREF.substr(1);
		                                                                                               
		                            
		string stXPath = GetIDNodeXPath(stID);
		konga::xml::DOMElementPtr pIdNode = pDoc->selectSingleNode( stXPath.c_str() );
		if( pIdNode != 0 )
		{
			                                                                                
			string stXsiTypeAttributeName = XmlUtils::GetQualifiedName(stSchemaInstancePrefix, XsdConstants::TYPE_ATTRIBUTE);
			string stXsiNilAttributeName = XmlUtils::GetQualifiedName(stSchemaInstancePrefix, XsdConstants::NIL);
			                                                  
			string stXsiNil = XmlUtils::GetAttributeValue(pIdNode, stXsiNilAttributeName);
			if( !stXsiNil.empty() )
				pElement->setAttribute( stXsiNilAttributeName.c_str(), stXsiNil.c_str() );

			                                                                
			string stXsiType = XmlUtils::GetAttributeValue(pIdNode, stXsiTypeAttributeName);

			string stNamespaceURI;
			if( !stXsiType.empty() )
			{
				pElement->setAttribute( stXsiTypeAttributeName.c_str(), stXsiType.c_str() );
				                                                                          
				                                                                     
				                                                               
				string stNamespacePrefix = XmlUtils::GetNamespacePrefix(stXsiType);
				stNamespaceURI = XmlUtils::TransferNamespacePrefix(pIdNode, pElement, stNamespacePrefix);
				                                                                                              
				                                                                                       
				                                        

				                           
				                                                                                  
				                                                                              
				string stTypeLocalName = XmlUtils::GetLocalName(stXsiType);
				string stUniversalName = XsdUtils::GetExpandedNameAsString(stNamespaceURI, stTypeLocalName);
				if( XsdUtils::IsSoapArrayType(stUniversalName) )
				{
					string stArrayTypeAttributeName = XmlUtils::GetQualifiedName(stNamespacePrefix, "arrayType");
					string stArrayType = XmlUtils::GetAttributeValue(pIdNode, stArrayTypeAttributeName);
					if( !stArrayType.empty() )
					{
						stNamespacePrefix = XmlUtils::GetNamespacePrefix(stArrayType);
						stNamespaceURI = XmlUtils::TransferNamespacePrefix(pIdNode, pElement, stNamespacePrefix);
					}
				}
			}
			
			konga::xml::DOMNodeListPtr pIdChildren = pIdNode->getChildNodes();
			for(size_t i = 0, iMax = pIdChildren->getLength(); i < iMax; ++i )
			{
				konga::xml::DOMNodePtr pChild = pIdChildren->item[static_cast<int>(i)];

				konga::xml::DOMNodeType childType = pChild->getNodeType();
				if( childType == konga::xml::NODE_ELEMENT )
				{
					konga::xml::DOMElementPtr pClone = XsdUtils::CloneElement(
						pDoc, pChild, stNamespaceURI, stSchemaInstancePrefix, stError);
					if( pClone == 0 )
						return E_FAIL;
					pElement->appendChild(pClone);
				}
				else
				{
					konga::xml::DOMNodePtr pClone = pDoc->importNode(pChild,true);
					pElement->appendChild(pClone);
				}
			}

			                                         
			pElement->removeAttribute("href");
		}
		else
		{
			stError = "Failed to locate a referenced element with the ID attribute = ";
			stError += stID;
			stError += ".";
			return E_FAIL;
		}
	}

	konga::xml::DOMNodePtr pChild = pElement->getFirstChild();
	while( pChild != 0 )
	{
		if( pChild->getNodeType() == konga::xml::NODE_ELEMENT )
		{
			unsigned long hr = FindAndReplaceHREFs(pDoc, pChild, stSchemaInstancePrefix, stError);
			if( FAILED(hr) )
				return hr;
		}
		pChild = pChild->getNextSibling();
	}
	return S_OK;
}

string WebServiceUtils::GetIDNodeXPath(const string& stID)
{
	string stID_(stID);
	if( stID.find('#') == 0 )
		stID_ = stID.substr(1);
	return stID_NODE_XPATH_BASE + string("='") + stID_ + "']";
}
