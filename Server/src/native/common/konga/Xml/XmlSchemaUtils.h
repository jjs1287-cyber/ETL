/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_XML_SCHEMA_UTILS
#define KONGA_XML_XML_SCHEMA_UTILS

#include <vector>
#include <string>

namespace konga {
	namespace XmlSchemaUtils {

		    
                                                                                                       
                                                                                                    
                                                                                             
                                                               
    
                                                                                
    
                                   
     
		void FindXsdIncludesAndImports(const std::string& stXsdFileName,
			std::vector<std::string>& vExistingIncludes, std::vector<std::string>& vMissingIncludes );

		   
                                                           
     
                                                           
     
		void FindWsdlIncludesAndImports(const std::string& stWsdlFileName,
			std::vector<std::string>& vExistingIncludes, std::vector<std::string>& vMissingIncludes );

		enum XmlSchemaType { XML_SCHEMA = 0, XML_DTD = 1 };

		   
                                                      
                                                                                                  
                                                                                               
                                                                       
                                                                         
     
		XmlSchemaType GetSchemaType(const std::string& stXmlSchemaFileName);

		                                                                    
		                                                                     
		                                                                 
		bool IsSchemaFile(const std::string& stFileName, std::string& stError);
		bool IsWsdlFile(const std::string& stFileName, std::string& stError);

	}                            
}                   

#endif                              

