/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#if !defined(AFX_XMLCLEANUP_H__AAA6193C_EE1B_4908_9F5B_F0097D009B7F__INCLUDED_)
#define AFX_XMLCLEANUP_H__AAA6193C_EE1B_4908_9F5B_F0097D009B7F__INCLUDED_

#include <vector>
#include <string>

#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodeListPtr.h>

namespace konga {

	class CleanupRule;
	class FileCleanup;

class RuleParser  
{
public:
	RuleParser(const std::string& stXmlFilePath):
	  m_stXmlFilePath(stXmlFilePath) {};
	virtual ~RuleParser() {};

	                                                   
	                                     
	                                        
	void InitCleanupRules(std::vector<CleanupRule*>& rules);

	const std::vector<std::string>& GetWarnings() const;

private:
	void InitFileCleanupRules(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& fileRules);
	void InitOperationLogCleanupRules(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules);
	void InitBillingTabCleanupRules(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules);
	void InitTransferHistoryCleanupRules(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules);
	void InitDbMaintenanceRule(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules);
	void InitTransformationDataStoreRule(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules);

	void PopulatePatternList(konga::xml::DOMNodePtr pCleanupNode, 
		FileCleanup& cleanupRule, 
		bool isNotPattern=false);

	void PopulateFileAge(konga::xml::DOMNodePtr pCleanupNode,
		FileCleanup& cleanupRule);

	void PopulateFileSize(konga::xml::DOMNodePtr pCleanupNode,
		FileCleanup& cleanupRule);

	void PopulateDirSetting(konga::xml::DOMNodePtr pCleanupNode,
		FileCleanup& cleanupRule);

	void PopulateSubDirSetting(konga::xml::DOMNodePtr pCleanupNode,
		FileCleanup& cleanupRule);

	void PopulateFileCompression(konga::xml::DOMNodePtr pCleanupNode,
		FileCleanup& cleanupRule);

	                                                
	konga::xml::DOMDocumentPtr ReadXmlSource();

	void AddWarning(const std::string& stWarning);

	std::string m_stXmlFilePath;
	std::vector<std::string> m_Warnings;
};

}                   

#endif                                                                               
