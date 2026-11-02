/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

                                   

#include <iostream>
#include <cassert>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/XmlInit.h>
#include <konga/Utils/util.h>

#include <konga/Xml/XmlIncludes.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlSchemaValidator.h>
#include <konga/Xml/DOMElementListPtr.h>
#include <konga/Xml/XmlSchemaUtils.h>
#include <konga/Xml/XercesXmlUtils.h>
#include <konga/Xml/XmlStrings.h>
#include <konga/Xml/DOMDocumentTypePtr.h>

int testCreateDoc();
int testParse();
int testOutput();
int testXPath();
int testSchemaValidator();
int test1();
int testImport();
int testRefCount();
int testElementList();
int testLoadDocWithExternalSchema();
int winTestWebServiceParsing();

int testSchemaValidatorForWsdl();

int testSchemaWithImport();

int testAmazon();

int testWsXsdParse();

int testWsdlParse();

int testPassByVal();

int testLoadDoc(const std::string& stXmlFileName, const std::string& stXsdFileName);

int testValidateSchema();

int testGoogleXsd();

int testXmlAttributes();

int testLoadGoogleXml();

int testWsdlXPath();

int testSugar();

                                                                                                    
int testValidateXmlUsingSchema();

int testLoadString();

int testFileTests();

int testTranscode();

int testTranscodePerformance();

using namespace std;
using namespace konga;
using namespace konga::xml;

int main(int argc, char* argv[])
{

	XmlInitializer::InitXml();

	return testTranscodePerformance();
                          


                                       
                      
                           
                          

	  
                            
            

                           
            

                        
            

                         
            

                                  
            

                         
            

                              
            
    

                                              
              

                                         
              

                                       

                                 

                         

                                          

                       

                  

                      

                           

                          

                          

                   

                                                                                                                                          
                                                                                                                 

                               

                      

                          
                              

                              

                          

                      
}

namespace {

	                                                                               
	string get_latin1_chars() {
		string latin1;
		for(unsigned char c = 32; c < 127; ++c ) {
			latin1.append(1, c);
		}

		for(unsigned char c = 160; c <= 254; ++c ) {
			latin1.append(1, c);
			if(c == 255) break;
		}
		latin1.append(1, 255);
		latin1.append(9, ' ');
		return latin1;
	}
}

int testTranscodePerformance() {

	const string latin1(get_latin1_chars());

	Clock c;
	for( int i = 0; i < 5000000; ++i ) {
		StringConverter sc(latin1);
		const XMLCh* unicode = sc.getXMLCh();
		const string test(XMLChToString(unicode));
		assert(test == latin1);
	}

	std::cout << c.getSeconds() << std::endl;
	return 0;
}

int testTranscode() {

	                     

	const string latin1(get_latin1_chars());

	string input(latin1);
	while(true) {
		input += latin1;
		if ( input.size() > 40*1024 ) break;
	}

	            
	StringConverter sc(input);
	const XMLCh* output = sc.getXMLCh();
	string test(XMLChToString(output));
	assert(input==test);

	                  
	StringConverter sc2(input + input + input);
	output = sc2.getXMLCh();
	test = XMLChToString(output);
	assert(test == string(input + input + input));
	
	               
	StringConverter sc3("");
	output = sc3.getXMLCh();
	std::cout << XMLChToString(output) << std::endl;
	test = XMLChToString(output);
	assert(test == string(""));

	              
	StringConverter sc4(latin1);
	output = sc4.getXMLCh();
	test = XMLChToString(output);
	assert(test == latin1);

	return 0;
}

int testFileTests() {

	string stError;
	bool b = XmlSchemaUtils::IsSchemaFile("C:\\Emanuel\\ETLForge\\Testing\\XML\\Mail\\mail.xsd", stError);
	cout << b << endl;
	if ( !b && !stError.empty() )
		cout << stError << endl;

	b = XmlSchemaUtils::IsWsdlFile("C:\\Emanuel\\ETLForge\\Testing\\WebServices\\Wsdl Include for 1.3\\CountryInfoService-include-xsd\\CountryInfoService.wsdl", stError);
	cout << b << endl;
	if ( !b && !stError.empty() )
		cout << stError << endl;
	return 0;
}

int testLoadString() {
	string stXmlString;
	try {
		read_file_to_string("../../data/personal.xml", stXmlString);

		DOMDocumentPtr pDoc;
		if ( !pDoc.loadString(stXmlString) ) {
			cerr << pDoc.getParseErrors() << endl;
			return 2;
		}
		cout << pDoc.getDocumentElement().getXml() << endl;
		return 0;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testValidateXmlUsingSchema() {

	string stXml("C:\\Emanuel\\ETLForge\\Testing\\XML\\t21_xsd\\t21a.xml");
	string stSchema("C:\\Emanuel\\ETLForge\\Testing\\XML\\t21_xsd\\output.xsd");
	string stTargetNamespace("http://www.etlforge.invalid/XsdFromWsdl");
	string stError;
	if ( !XmlUtils::ValidateXmlInstanceAgainstSchema(stXml, stSchema, stTargetNamespace, stError) ) {
		cerr << stError << endl;
		return 1;
	}
	return 0;
}

const string WSDL_SOAP_NAMESPACE_11 = "http://schemas.xmlsoap.org/wsdl/soap/";
const string WSDL_SOAP_NAMESPACE_12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
const string WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";

int testSugar() {
	const string stXml("C:\\tmp\\sugar\\sugarcrm\\ee9b5bcb-8268-4840-99e5-47c02cc2244c\\t1");
	const string stSchema("C:\\tmp\\sugar\\sugarcrm\\ee9b5bcb-8268-4840-99e5-47c02cc2244c\\input.xsd");
	string stErr;
	string stNamespace("http://www.etlforge.invalid/XsdFromWsdl");

	DOMDocumentPtr doc = XmlUtils::LoadDoc(stXml, stSchema, stNamespace, stErr);
	if ( !stErr.empty() ) {
		cout << stErr << endl;
		return 1;
	}
	cout << "XML is valid" << endl;

	return 0;
}

int testWsdlXPath() {
    const string stFile("../../data/stockq-wsdl.xml");
	string stErr;
	DOMDocumentPtr pDoc = XmlUtils::LoadDoc(stFile, stErr);
	if ( pDoc == 0 ) {
		cout << stErr << endl;
		return 1;
	}
	pDoc->addSelectionNamespace("soap", WSDL_SOAP_NAMESPACE_11);
	pDoc->addSelectionNamespace("soap12", WSDL_SOAP_NAMESPACE_12);
	pDoc->addSelectionNamespace("wsdl", WSDL_NAMESPACE);
	pDoc->addSelectionNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

	std::string stQuery = "//wsdl:definitions/wsdl:binding[@name='";
	stQuery += "StockQuoteSoap";
	stQuery += "']";
	DOMElementPtr m_pBindingNode = pDoc->selectSingleNode(stQuery);
	if ( m_pBindingNode == 0 ) {
		cout << "Failed to locate binding node!" << endl;
		return 1;
	}
	cout << m_pBindingNode.getXml() << endl;
	return 0;
}

int testLoadGoogleXml() {
	const string stXmlFileName = "C:\\Program Files\\Jargonware Konga\\DataInterchange/Temp/op_3_c2b29161-dfe4-4036-a016-bae66599d56e/t9.xml";
	const string stSchemaFileName = "C:/Program Files/Jargonware Konga/DataInterchange/wsdl/7bcb6ac1-80dc-44fc-be4b-38c7af69e9e6/output.xsd";
	const string stNamespace = "http://www.edgewaystech.com/XsdFromWsdl";
	string stErr;
	DOMDocumentPtr pDoc = XmlUtils::LoadDoc(stXmlFileName, stSchemaFileName, stNamespace, stErr);
	if ( pDoc == 0 ) {
		cout << stErr << endl;
		return 1;
	}
	return 0;
}

int testXmlAttributes() {
	DOMDocumentPtr doc;
	doc.CreateInstance();
	DOMElementPtr rootElement = doc.createElement("root");
	doc.setDocumentElement(rootElement);
	rootElement.setAttribute("att1", "val1");
	rootElement.setAttribute("att2", "val2");
	rootElement.setAttribute("att1", "val1");

	cout << doc.getXml() << endl;

	return 0;
}

int testGoogleXsd() {
	const string stXsd("C:/Program Files/Jargonware Konga/DataInterchange/wsdl/7bcb6ac1-80dc-44fc-be4b-38c7af69e9e6/output.xsd");
	string stErr;
	if ( !XmlUtils::ValidateXmlSchema(stXsd, stErr) ) {
		cout << stErr << endl;
		return 1;
	}
	return 0;
}

int testValidateSchema() {	
	string stError;
                                                                                                                                            
                                                                                                                                                
                                                        
	string stSchemaFile("../../data/test Xsd/output.xsd");

	if ( !XmlSchemaValidator().ValidateXmlSchema(stSchemaFile, stError) ) {
		cout << stError << endl;
		return 1;
	}

                                                                                                                                 
	string stXmlFile("../../data/test Xml/t5.xml");

	const string stNamespace("http://www.etlforge.invalid/XsdFromWsdl");
                          
	DOMDocumentPtr m_poDoc = XmlUtils::LoadDoc(stXmlFile, stSchemaFile, stNamespace, stError);
	if ( m_poDoc == 0 ) {
		cout << stError << endl;
		return 1;
	}
	if ( !m_poDoc.getParseWarnings().empty() ) {
		cout << "Warning!!" << endl;
		cout << m_poDoc.getParseWarnings() << endl;
		return 1;
	}

	if ( !XmlSchemaValidator().ValidateXmlInstance(stXmlFile, stSchemaFile, stNamespace, stError) ) {
		cout << stError << endl;
	}

	return 0;
}

int testLoadDoc(const std::string& stXmlFile, const std::string& stSchemaFile)
{
	cout << "testLoadDoc(" << stXmlFile << "," << stSchemaFile << ")" << endl;
	try
	{
		std::string stError;
		DOMDocumentPtr poDoc = XmlUtils::LoadDoc(stXmlFile, stSchemaFile, "", stError);
		if ( poDoc == 0 ) {
			cout << "LoadDoc failed" << endl;
			cout << stError << endl;
			return -1;
		}
	}
	catch(const CrException& e)
	{
		                     
		cout << "CrException" << endl;
		cout << "Code=" << e.GetErrorNumber() << " Code meaning: " << e.GetErrorString() << endl;
		return -1;
	}
	cout << "LoadDoc successful" << endl;
	return 0;
}


void LoadDocByVal(DOMDocumentPtr pDoc, const string& stXml) {
	pDoc.validateOnParse = true;
	pDoc.loadFile(stXml);
}

int testPassByVal() {

	DOMDocumentPtr pDoc;
	if ( pDoc.CreateInstance() ) {
		cout << pDoc.getLastError() << endl;
		return -1;
	}

	string stXml("../../data/personal.xml");
	LoadDocByVal(pDoc, stXml);
	return 0;
}

int testWsdlParse() {
	string stWsdl("../../data/ws.wsdl");

	string stError;
	if ( !XmlUtils::LoadDoc(stWsdl, stError) ) {
		cout << stError << endl;
		return -1;
	}

	return 0;
}

int testWsXsdParse() {
	string stWsXsd;
	stWsXsd = "../../wsdebug/output.xsd";

                                                 
                                                  
                                       
                                         
                                            
                                             

	string stErr;
	if ( !XmlUtils::ValidateXmlSchema(stWsXsd, stErr) ) {
		cout << stErr << endl;
		return -1;
	}

	return 0;
}

int testAmazon() {
	string stInventorySample("../../amazon-data/Inventory_sample.xml");
	string stPriceSample("../../amazon-data/Price_sample.xml");

	string stXsd("../../amazon-data/amzn-envelope.xsd");

	string stErr;
                         
                                      
                         

	if ( !XmlUtils::LoadDoc(stPriceSample, stErr, true) ) {
		cout << stErr << endl;
		return -1;
	}

	return 0;
}

int testSchemaWithImport() {
	string stSchema("C:\\tmp\\import_schema\\WS_294-270654.559135437_in.xsd");
	string stError;
	if ( !XmlUtils::ValidateXmlSchema(stSchema, stError) ) {
		cout << stError << endl;
		return -1;
	}

	string stXml;
	if ( !XmlUtils::ValidateXmlInstanceAgainstSchema(stXml, stSchema, "http://www.edgewaystech.com/XsdFromWsdl", stError) ) {
	}

	return 0;
}

int testSchemaValidatorForWsdl() {
	const string stFileName( "/tmp/input.xsd" );
	string stError;
	if ( !XmlUtils::ValidateXmlSchema(stFileName, stError) ) {
		cout << "Error: " << stError << endl;
		return -1;
	}
	cout << "success!" << endl;
	return 0;
}

void printNode(const DOMNodePtr& node) {
	cout << "Name = " << node.getNodeName() << endl << "Value = " << node.getNodeValue() << endl;
	DOMNodeListPtr kids = node.getChildNodes();
	cout << "Number of children = " << kids.getLength() << endl;
	for( unsigned long i = 0, n = kids.getLength(); i < n; ++i ) {
		printNode(kids->item[i]);
	}
}

int winTestWebServiceParsing() {
	bool bValidate = true;
	std::string stXmlFile("C:/Program Files/CommerceRoute/DataInterchange/Temp/op_39_3589e51a-09bd-4e3d-9e3f-7e650e87d6f3/t89");
	std::string stSchemaFile("C:/Program Files/CommerceRoute/DataInterchange/XML_DTD/WebServices/WS_85-5743524.43218231_out.xsd");

	std::string stError;
	konga::xml::DOMDocument2Ptr m_poDoc;
	if( bValidate )
		m_poDoc = XmlUtils::LoadDoc(stXmlFile, stSchemaFile, "", stError);
	else
		m_poDoc = XmlUtils::LoadDoc(stXmlFile, stError, false);
	if ( m_poDoc == 0 || !stError.empty() ) {
		cout << stError << endl;
		return -1;
	}
	printNode(m_poDoc.getDocumentElement());
	return 0;
}

int testLoadDocWithExternalSchema() {

	string stXml("../../data/personal-external.xml");
	string stSchema("personal.xsd");
	string stError;

	             
	DOMDocumentPtr doc = XmlUtils::LoadDoc(stXml, stSchema, "", stError);
	if ( doc == 0 ) {
		cout << stError << endl;
		return -1;
	}

	XmlSchemaValidator validator;
	stError.erase();
	if ( !validator.ValidateXmlInstance(stXml, stSchema, "", stError) ) {
		cout << stError << endl;
		return -1;
	}

	stError.clear();
	stSchema = "../../data/personal.xsd";
	if ( !validator.ValidateXmlSchema(stSchema, stError) ) {
		cout << stError << endl;
		return -1;
	}


	               
	stError.clear();
	stXml = "../../data/personal-external-invalid.xml";
	doc = XmlUtils::LoadDoc(stXml, stSchema, "", stError);
	if ( doc != 0 ) {
		cout << "Ooops, this xml is not supposed to be valid for this schema, error should be reported." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	                  
	stError.clear();
	stXml = "../../data/personal.xml";
	stSchema = "personal-bad.xsd";
	doc = XmlUtils::LoadDoc(stXml, stSchema, "", stError);
	if ( doc != 0 ) {
		cout << "Ooops, this schema is not supposed to be valid, error should be reported." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	cout << "testLoadDocWithExternalSchema successfull." << endl;
	return 0;

}

int testElementList() {

	string stFile("data/personal.xml");
	string stError;
	DOMDocumentPtr doc = XmlUtils::LoadDoc(stFile, stError, false);
	if ( doc == 0 || !stError.empty() ) {
		cout << "Failed to load document. " << stError << endl;
		return -1;
	}

	DOMElementPtr docElement = doc.getDocumentElement();

	DOMNodeListPtr nodes = docElement.getChildNodes();
	cout << nodes.getLength() << endl;

	DOMElementListPtr elements(docElement.getChildNodes());
	cout << elements.getLength() << endl;
	for( unsigned long i = 0, n = elements.getLength(); i < n; ++i ) {
		cout << elements.item[i].getNodeName() << endl;
	}

	DOMElementListPtr elements2 = elements;
	for( unsigned long i = 0, n = elements2.getLength(); i < n; ++i ) {
		cout << elements2.item[i].getNodeName() << endl;
	}

	return 0;
}

int testRefCount() {

	                                       
	string stErr;
	DOMNodePtr node;
	DOMNodeListPtr kids;
	{
		DOMDocumentPtr doc( XmlUtils::LoadDoc("C:\\tmp\\xml\\data\\personal.xml", stErr, true) );
		if ( stErr.length() > 0 ) {
			cout << stErr.c_str() << endl;
			return -1;
		}

		node = doc.getDocumentElement();
		kids = node.getChildNodes();
	}

	                                                               
	cout << node.getTextContent() << endl;

	return 0;
}

int testImport() {
	                                                                                                
	string stFile("data/personal.xml");
	string stError;
	DOMDocumentPtr doc = XmlUtils::LoadDoc(stFile, stError, true);
	if ( doc == 0 || !stError.empty() ) {
		cout << "Failed to load document. " << stError << endl;
		return -1;
	}

	DOMElementPtr docElement = doc.getDocumentElement();
	if ( docElement == 0 ) {
		cout << "Document element is null." << endl;
		return -1;
	}

	stError.clear();
	DOMDocumentPtr doc2;
	if ( XmlUtils::CreateDOMDocument2Instance(doc2, true, stError) != 0 ) {
		cout << stError << endl;
		return -1;
	}

	try {
		DOMNodePtr importedNode = doc2.importNode(docElement, true);
		doc2.appendChild(importedNode);
	}
	catch( const CrXmlException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}

	cout << doc2.getXml() << endl;

	cout << "testImport succeeded" << endl;
	return 0;
}

int test1() {
	string stError;
                                                        
	string stFile("../../data/personal.xml");
	DOMDocumentPtr doc = XmlUtils::LoadDoc(stFile, stError, true);
	if ( doc == 0 || !stError.empty() ) {
		cout << "Failed to load document. " << stError << endl;
		return -1;
	}

	DOMElementPtr pDocElement = doc.getDocumentElement();
	if ( pDocElement == 0 ) {
		cout << "Document element missing." << endl;
		return -1;
	}
	cout << "docElement: name = " << pDocElement.getNodeName() << " text = " << pDocElement.getTextContent() << endl;

	DOMNodePtr node = pDocElement.getFirstChild();
	if ( node == 0 ) {
		cout << "First child is null." << endl;
		return -1;
	}
	cout << "Text = " << node.getTextContent() << endl;
	cout << "Type = " << node.getNodeType() << endl;

	DOMNodeListPtr children = pDocElement.getChildNodes();
	cout << "Element node with " << children.getLength() << " children." << endl;

	DOMNodePtr thisNode;
	while ( (thisNode=children.nextNode()) != 0 ) {
		cout << thisNode.getNodeType() << " " << thisNode.getNodeName() << endl;
	}

	DOMElementPtr element(node);
	if ( element == 0 ) {
		cout << "First child is not an element. " << endl;
		return -1;
	}


	return 0;
}

int testSchemaValidator() {
	XmlSchemaValidator validator;
	string stError;
	                
	if ( !validator.ValidateXmlSchema("data/personal.xsd", stError) ) {
		cout << stError << endl;
		return -1;
	}

	                  
	stError.clear();
	if ( validator.ValidateXmlSchema("data/personal-bad.xsd", stError) ) {
		cout << "Ooops, this schema is not supposed to be valid, error should be reported." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	                     
	stError.clear();
	if ( validator.ValidateXmlSchema("data/personal-doesnotexist.xsd", stError) ) {
		cout << "Ooops, this schema does not exist!." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	               
	stError.clear();
	if ( !validator.ValidateXmlSchema("data/SOAP_Encoding_v11.xsd", stError) ) {
		cout << stError << endl;
		return -1;
	}

	                                 
	stError.clear();
	if ( !validator.ValidateXmlSchema("data/WS_in_1.xsd", stError) ) {
		cout << stError << endl;
		return -1;
	}

	stError.clear();
	if ( !validator.ValidateXmlSchema("data/WS_in_bridge_in.xsd", stError) ) {
		cout << stError << endl;
		return -1;
	}

	stError.clear();
	if ( !validator.ValidateXmlSchema("data/WS_in_host.xsd", stError) ) {
		cout << stError << endl;
		return -1;
	}


	                                      
	             
	stError.clear();
	if ( !validator.ValidateXmlInstance("data/personal-schema.xml", "data/personal.xsd", "", stError) ) {
		cout << stError << endl;
		return -1;
	}

	                        
	stError.clear();
	if ( validator.ValidateXmlInstance("data/personal-schema-invalid.xml", "data/personal.xsd", "", stError) ) {
		cout << "Oooops, this should fail..." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	                                       
	stError.clear();
	if ( validator.ValidateXmlInstance("data/personal-schema.xml", "data/personal-bad.xsd", "", stError) ) {
		cout << "Oooop, this should fail..." << endl;
		return -1;
	}
	cout << "This error is OK: " << stError << endl;

	                              
	stError.clear();
	if ( !validator.ValidateXmlInstance("data/personal-schema-external.xml", "data/personal.xsd", "", stError) ) {
		cout << stError << endl;
		return -1;
	}

	cout << "testSchemaValidator succeeded" << endl;
	return 0;
}

int testXPath() {
	                     
	string stErr;
	DOMDocumentPtr doc( XmlUtils::LoadDoc("../../data/personal.xml", stErr, true) );
	if ( doc == 0 || !stErr.empty() ) {
		cout << stErr.c_str() << endl;
		return -1;
	}

	try {
		                                   
		DOMNodePtr pNode = doc.selectSingleNode("/personnel");
		if ( pNode != 0 ) {
			DOMNodeListPtr children = pNode.getChildNodes();
			if ( children != 0 ) {
				while ( (pNode = children.nextNode()) != 0 ) {
					cout << pNode.getNodeName() << endl;
				}
			}
		}
		cout << pNode->getNodeName() << endl;

		DOMNodeListPtr nulltest;
		if ( nulltest != 0 ) {
			cout << "Hmm, this DOMNodeListPtr should be null..." << endl;
		}

		                                                                   
		DOMDocumentPtr doc2(doc);
		DOMNodeListPtr nodes = doc2.selectNodes("//person");
		if ( nodes != 0 ) {
			                                                 
			DOMNodeListPtr nodes2(nodes);
			unsigned long size = nodes2.getLength();
			cout << "Selected " << size << " nodes" << endl;
			for( unsigned long i = 0; i < size; ++i ) {
				DOMNodePtr node = nodes2.item[i];
				cout << node.getNodeName() << endl;
			}
		}

		DOMElementPtr pDocElement = doc.getDocumentElement();

		                         
		pNode = pDocElement.selectSingleNode("person[@id=\"one.worker\"]");
		if ( pNode != 0 ) {
			cout << pNode.getNodeName() << endl;
		}

		                  
		nodes = pDocElement.selectNodes("person/email");
		DOMNodePtr node;
		while ( (node = nodes.nextNode()) != 0 ) {
			cout << node.getTextContent() << endl;
		}

		                                 
		node = pDocElement.selectSingleNode("person[email=\"one@foo.com\"]");
		if ( node != 0 ) {
			cout << node->getTextContent() << endl;
		}

		                                 
		try {
			node = pDocElement.selectSingleNode("person[@id");
			cerr << "Ooops, this should not work..." << endl;
			return -1;
		}
		catch( const CrXmlException& ex ) {
			                              
			cout << "This exception is OK: " << ex.GetErrorString() << endl;
		}
	}
	catch( const CrXmlException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}

	cout << "testXPath succeded" << endl;
	return 0;
}

int testCreateDoc() {
	                       
	DOMDocumentPtr doc1 = 0;
	long lRes = doc1.CreateInstance();
	if ( lRes != 0 ) {
		cout << doc1->getLastError().c_str() << endl;
		return 1;
	}
	                       
	if ( doc1 == 0 )
		return 1;

	                          
	DOMDocumentPtr doc2(doc1);

	                   
	DOMDocumentPtr doc3;
	doc3 = doc2;

	                                                
	doc3 = doc1;

	                                                                                                   
	DOMDocument2Ptr doc4 = 0;
	lRes = doc4.CreateInstance();
	if ( lRes != 0 ) {
		cout << doc1->getLastError().c_str() << endl;
		return 1;
	}
	if ( doc1 == 0 )
		return 1;

	doc4 = doc1;                                                   

	                   
	cout << doc4.getNodeName().c_str() << endl;
	DOMProcessingInstructionPtr pi = doc4->createProcessingInstruction("xml", "version='1.0'");

	cout << pi->getNodeName() << endl;
	cout << pi->getPrefix() << endl;

	DOMElementPtr element = doc4->createElement("root");

	DOMElementPtr element2 = element;
	cout << element2->getBaseName() << endl;

	element = doc4->getDocumentElement();
	cout << doc4->getDocumentElement().getBaseName() << endl;

	cout << "Test DOMDocument create success" << endl;
	return 0;
}

int testParse() {
	string stErr;
	                      
	DOMDocumentPtr doc( XmlUtils::LoadDoc("../../data/personal.xml", stErr, false) );
	if ( doc == 0 ) {
		cout << stErr.c_str() << endl;
		return -1;
	}

	doc.selectSingleNode("");

	cout << doc.getLoadedDocumentPath() << endl;

	                               
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal.xml", stErr, true);
	if ( doc == 0 ) {
		cout << stErr.c_str() << endl;
		return -1;
	}

	                                 
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal-invalid.xml", stErr, true);
	if ( stErr.empty() ) {
		cout << "This XML is not valid. Should return an error!" << endl;
		return -2;
	}

	                                  
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal-bad.xml", stErr, false);
	if ( doc != 0 ) {
		cout << "Oooops! this xml is BAD! - should generate an error." << endl;
		return -3;
	}

	                         
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal-schema.xml", stErr, false);
	if ( doc == 0 ) {
		cout << stErr.c_str() << endl;
		return -4;
	}

	                                  
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal-schema.xml", stErr, true);
	if ( !stErr.empty() ) {
		cout << stErr.c_str() << endl;
		return -5;
	}

	                                    
	stErr.erase();
	doc = XmlUtils::LoadDoc("../../data/personal-schema-invalid.xml", stErr, true);
	if ( doc != 0 ) {
		cout << "This XML is not valid. Should return an error!" << endl;
		return -6;
	}

	                       
	stErr.erase();
	doc = XmlUtils::LoadDoc("data/non-existant.xml", stErr, true);
	if ( doc != 0 ) {
		cout << "I do not think this file exists. Should return an error!" << endl;
		return -7;
	}
  
                    
               
                                                                                                       
                            
                        
            
  

                     
               
                                                                  
                            
                        
            
  


                                         
   
	return 0;
}

int testOutput() {
	                              
	string stErr;
	DOMDocumentPtr doc( XmlUtils::LoadDoc("data/personal.xml", stErr, false) );
	if ( stErr.length() > 0 ) {
		cout << stErr.c_str() << endl;
		return -1;
	}

	string stXml = doc.getXml();
	if ( stXml.empty() ) {
		cout << "Failed to get xml:" << endl << doc.getLastError() << endl;
		return -1;
	}
	cout << stXml << endl;

	if ( !doc.save("data/personal.saved.xml") ) {
		cout << "Failed to save the xml:" << endl << doc.getLastError() << endl;
		return -1;
	}
	

	cout << "Output test successful" << endl;
	return 0;
}


