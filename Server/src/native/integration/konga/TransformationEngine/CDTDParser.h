/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __CDTDPARSER_H_
#define __CDTDPARSER_H_

#include <string>
#include <vector>

#include "resource.h"                      
#include "DTDParser.h"

#include "TransformationEngine.h"
#include "TransformationEngineApi.h"	                           

namespace konga {
	class UserInfo;
}

                                                                
class TRANSFORMATIONENGINE_API CDTDParser : public TransformationEngine
{
public:
	CDTDParser(const konga::UserInfo& currentUser, const char* sPrefix=0);
	~CDTDParser(void);

             
public:
                                                                                                                            
                                         
	std::string get_LastError() const;
	std::string getTransformationName() const { return std::string(""); }

	unsigned long CreateBrowserTree(const std::string& inputFileName, const std::string& rootName, const std::string& javaFileName, const std::string& title, const std::string& click);

public:
	                                                                                       
	                                                       
	unsigned long CreateXSDFromWSDLForBothIO(const std::string& stWDSLFileName, const std::string& stBinding, const std::string& stOperationName, const std::string& stOperationInputName, const std::string& stOperationOutputName, const std::string& stSoapOperationStyle, const std::string& stSoapBodyUse, const std::string& stInputXSDFileName, const std::string& stOutputXSDFileName);
	unsigned long GetRootSelectionList(const std::string& stDtdFileName, const std::string& stOutputFileName, const std::string& stOutputFileName2);
	unsigned long GetRootSelectionList(const std::string& stDtdFileName, std::vector<std::string>& vMostLikely, std::vector<std::string>& vLessLikely);
	std::string GetSystemRootName(const std::string& stSampleXmlFileName);

	                                                                                                           
	unsigned long CreateJtrForWS(const std::string& stWsdlFileName, const std::string& stBinding, const std::string& stOperation, const std::string& stOperationInputName, const std::string& stOperationOutputName, const std::string& stSoapOperationStyle, const std::string& stSoapBodyUse, const std::string& stRequestJtrFileName, const std::string& stResponseJtrFileName, const std::string& stTmpDir = "");

	int GetLastWsdlStatus();
	std::string GetLastWsdlWarningMessage();

	                                                                       
	                            
	                                                                            
	unsigned long ReadDtdDefinition(const std::string& stFileName,
		std::vector<std::string>& vExistingIncludes, std::vector<std::string>& vMissingIncludes);

private:
	TreeMapper*m_pTreeMapper;
	DTDParser m_parser;
	bool m_bInputXSDHasValueContains;

	int			m_iLastWsdlStatus;
	std::string	m_stLastWsdlWarningMsg;

	unsigned long CreateXSDFromWSDL(const char* csWSDLFileName,  
							const char* csBinding, 
							const char* csOperationName,
							const char* csOperationStyle,
							const char* csBodyUse,
							short intIOFlag, 
							const char* csXSDFileName);
};


#endif                  
