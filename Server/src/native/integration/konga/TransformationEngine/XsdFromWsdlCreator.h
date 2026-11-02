/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef XSD_FROM_WSDL_CREATOR_H
#define XSD_FROM_WSDL_CREATOR_H

#include <string>
#include <vector>
#include <map>

#include <konga/Soap/SoapConstants.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>

#include "XsdFromWsdlUtils.h"
#include "XsdFromWsdlMainSchema.h"
#include "XsdFromWsdlBridgeSchema.h"
#include "TransformationEngineApi.h"	                           


class CErrorLog;

  
                                                                  

                                                                              
                           

                                 

                                                                           
                                                                                         
                                                                              
                                                   

                                                                              
                                                                                
                  
                                                                                 
                                                                                       
                                                                                        
             


                            

                                                                                         
                                                                       
                                                                                             
                                                                                                  
                                                                                             
                                                                                                   
                                                                                              
                                                                                            
                                                                                                
                                                                                                        

                                                                                             
                                                                                                   
                                                                                                
                                                               
                                                                                                 
                                         



                                                                                           
                                                                                            
                                                                                         
                                 

  

class TRANSFORMATIONENGINE_API XsdFromWsdlCreator
{
public:
	  
                                                                             
                                                                                 
                                                                                 
       
   
	XsdFromWsdlCreator(TreeMapper*p,
		const std::string& stWsdlFilename,
		const std::string& stBinding,
		const std::string& stOperation,
		const std::string& stOperationInput,
		const std::string& stOperationOutput,
		const std::string& stSoapStyle,
		const std::string& stSoapBodyUse,
		const std::string& stInputXSDFilename,
		const std::string& stOutputXSDFilename,
		CErrorLog*pErrorLog);
	virtual ~XsdFromWsdlCreator(void);

	TreeMapper*getTreeMapper() {
		return m_pTreeMapper;
	}

	  
                                                                                        
                                                                           
  
                      
         
                  
              
           
                                                                                           
              

                        
         
                  
               
               
            
               
               
            
                                                                                            
                                                                                        

   
	unsigned long	CreateXsdFilesForTransformation();

	unsigned long	ResolveRemoteSchemaLocations();

	  
                                                                                             
                                                                                           
         

                                            
         
             
          
                                                                                           
                       
   
	unsigned long CreateXsdFilesForDisplay();

	void SetRequireValidSchema(bool b) { 
		m_bRequireValidSchema = b; 
	}
	std::string	GetSchemaValidationWarning() const { 
		return m_stSchemaValidationWarnings; 
	}


	std::string	GetLastError() const { 
		return m_stLastError; 
	}

	                                                                                                          
	static const std::string XSD_FROM_WSDL_NAMESPACE;
	static const std::string ROOT;
	static const std::string HEADER;
	static const std::string BODY;
	static const std::string REQUEST;
	static const std::string RESPONSE;
	static const std::string TRANSACTION;
	static const std::string HOST_POSTFIX;

private:
	TreeMapper *const m_pTreeMapper;
	const std::string m_stWsdlFilename;
	const std::string m_stBinding;
	const std::string m_stOperation;
	const std::string m_stOperationInput;
	const std::string m_stOperationOutput;
	const std::string m_stSoapStyle;
	const std::string m_stSoapBodyUse;
	std::string m_stInputXSDFilename;
	std::string m_stOutputXSDFilename;
	bool m_bIncludeRequestStructureInResponse;

	konga::xml::DOMElementPtr m_pWsdlRoot;
	konga::xml::DOMElementPtr m_pBindingNode;
	konga::xml::DOMElementPtr m_pPortTypeNode;
	konga::xml::DOMElementPtr m_pPortTypeOperationNode;
	konga::xml::DOMElementPtr m_pBindingOperationNode;
	konga::xml::DOMElementPtr m_pInputMessageNode;
	konga::xml::DOMElementPtr m_pOutputMessageNode;
	konga::xml::DOMNodeListPtr m_pInputMessagePartNodes;
	konga::xml::DOMNodeListPtr m_pOutputMessagePartNodes;

	                                                                                                         
	XsdFromWsdlMainSchema m_mainInputSchema;
	XsdFromWsdlMainSchema m_mainOutputSchema;
	                                                                                       
	                                                                   
	XsdFromWsdlBridgeSchema m_inputBridgeSchema;
	XsdFromWsdlBridgeSchema m_outputBridgeSchema;
	CErrorLog *const m_pErrorLog;
	bool m_bRequireValidSchema;

	std::string m_stFolderPath;
	std::string m_stSchemaValidationWarnings;
	std::string m_stLastValidationWarning;
	std::map<std::string, bool> m_mapLoggedWarnings;
	konga::xml::DOMDocument2Ptr m_pWsdlDoc;
	konga::xml::DOMNodeListPtr m_pInputSoapHeaders;
	konga::xml::DOMNodeListPtr m_pOutputSoapHeaders;
	std::string m_stBridgeSchemaNamespacePrefix;

	konga::xml::XmlNamespaceDeclarations m_wsdlNamespaces;
	XsdConstants::SchemaVersion m_wsdlSchemaVersion;
	std::string m_stSchemaDefinitionNamespace;
	std::string m_stSchemaDefinitionNamespacePrefix;
	konga::SoapConstants::SoapStyle m_soapStyle;
	konga::SoapConstants::SoapBodyUse m_soapBodyUse;

	std::string m_stLastError;
	std::string m_stPortType;

	                                                                    
	PurposeEnum m_purpose;
	
	WsdlXsdCollection m_wsdlSchemas;
	std::vector<std::string> m_vstSchemaFilenames;
	std::vector<std::string> m_vstSavedSchemas;

	std::string	m_stWsdlDefinitionSchemaLocation;
	std::string	m_stSoapEncodingSchemaLocation;


	unsigned long CreateXsdFiles();

	  
                                                                                           
                                                                         
   
	unsigned long CreateMainSchemas(bool isHosted);
	
	  
                                                                                         
                  
   
	unsigned long LoadWsdlDoc();

	unsigned long ProcessExternalSchemas();

	  
                                                                                           
                                                                                            
                     
   
	unsigned long ProcessWsdlSchemas();

	  
                                                                                       
                                                                                          
                                                                                        
                
                                                                                           
                                                                                       
                                                             

                                                                                          
                                                   
   
	unsigned long	FindWsdlSchemas();

	  
                                                                                      
                                                                                        
                                                              

                                                                                    
                                                                                   
                                                                 
   
	unsigned long ReplaceSoapArrayTypes();

	  
                                                                                         
                                                                                          
                                                     

                                   
                                                                                        
                                                                                     
                                                                                        
                                                                                 
                                                                                           
                                                                                      
                                                                                       
                                                                          
   
	unsigned long CorrectKnownSchemaLocations();

	  
                                                                                            
                                                                                             
                                                                                            
                                                                                          
                                                                                          
                                                                                         
                                                  
   
	unsigned long	AddMissingImportsToWsdlSchemas();

	  
                                                                                      
                                                                                    
                                                                                

                                                                                           
                                                                                        
                                   
   
	unsigned long SetAnyElementProcessContentsAttribute();
	unsigned long SaveWsdlSchemas();
	unsigned long ValidateWsdlSchemas();

	  
                                                                                                 
                                                                                                 
                                               
   
	unsigned long CreateBridgeSchemas();

	void SetHostedMainSchemaFilenames();

	void AddValidationWarning(const std::string& stWarning);

	unsigned long InsertSchemaNamespaceImport(XsdInfo* pXsdInfo, 
											  const std::string& stNamespace, 
											  XsdConstants::SchemaVersion version);
	konga::xml::DOMElementPtr LocatePortTypeOperationNode(const std::string& stQueryBase);

	konga::xml::DOMElementPtr LookupHeaderPart(konga::xml::DOMElementPtr pHeaderNode);

	void LookupHeaderParts(konga::xml::DOMNodeListPtr soapHeaders, std::vector<konga::xml::DOMElementPtr>& parts);
};

#endif                           

