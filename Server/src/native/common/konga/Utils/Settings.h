/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_SETTINGS_H
#define KONGA_UTILS_SETTINGS_H

#include <string>
#include <map>

namespace konga {
	std::string GetServerHome();
	std::string GetServerHomeWithSlash();
	
	std::string GetConfFileLocation();

	                                                                 
	                                                                   
	std::string GetLogDirectoryWithSlash(std::string& sErr, bool bCreateDirectory = true);

	int GetOptionAsInt(const std::string& stSection, const std::string& stName, int iDefaultVal = 0 );
	std::string GetOptionAsString(const std::string& stSection, const std::string& stName, const std::string& stDefaultVal = "" );
	double GetOptionAsDouble(const std::string& stSection, const std::string& stName, double DefaultValue);

	                                                                  
	                                                                                                            
	bool GetOptionAsBoolean(const std::string& stSection, const std::string& stName, bool bDefaultValue);

	                                         
	bool IsOptionDefined(const std::string& stSection, const std::string& stName);

	                                         
	void SetOptionsFromFile(const std::string& stFileName);

	                                                                        
	void ResetOptions();

	                                           
	                                                                         
	                                                                                 
	                      
	  
	                                 
	            
	              
	  
	                                                                                                
	                                                                            
	void SetCommandLineOptions(int argc, char* argv[]);
	
	                             
	void SetOptionAsInt(const std::string& stSection, const std::string& stName, int iVal);
	void SetOptionAsString(const std::string& stSection, const std::string& stName, const std::string& stVal);

	                                     
	void LoadSectionNameValuePairs(const std::string& sectionName, std::map<std::string, std::string>&nameValues);

	std::string defaultEncodingXml();

}                   
#endif                          

