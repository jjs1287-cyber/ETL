/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <time.h>

#include <memory>		                

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Xml/DOMElementListPtr.h>
#include <konga/Xml/XmlUtils.h>

#include <konga/Utils/PortDefs.h>			                  

#include <konga/Log/logging.h>

#include "CleanupUtils.h"
#include "FileCleanup.h"
#include "OperationLogCleanupRule.h"
#include "BillingTabCleanupRule.h"
#include "TransferHistoryCleanupRule.h"
#include "DbMaintenanceCleanupRule.h"
#include "TransformationDataStoreCleanupRule.h"
#include "RuleParser.h"
#include "resource.h"

using namespace konga;
using std::string;
using std::vector;

extern int g_log_type;

#ifdef WIN32
#include <shlwapi.h>
#else
namespace { 
	bool PathIsRelative(const std::string& sPath){
		if(sPath.empty())return false;
		char c=sPath.at(0);
		                                                                                                
		                                                           
		return c!='/';
	}
	const char* PathFindFileName(const char* sPath){
		for(int n=strlen(sPath),i=n-2;i>=0;i--){
			if(sPath[i]=='/'){
				                                                                                        
				                                                           
				return &sPath[i+1];
			}
		}
		                                                                     
		                                                           
		return sPath;
	}
	void PathRemoveBackslash(char* buffer){
		                                                                      
		int pos=strlen(buffer)-1;
		if(pos>0&&buffer[pos]=='/'){
			buffer[pos]=0;
		}
		                            
		                                                           
	}
}
#endif                


void RuleParser::InitCleanupRules(std::vector<CleanupRule*>&rules)
{
	xml::DOMDocumentPtr pCleanupDoc=ReadXmlSource();

	xml::DOMNodeListPtr pNodeList=0;

	                                                          
	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/TransformationDataStoreRule");
		InitTransformationDataStoreRule(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating the transformatin data store rules.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/FileCleanup/CleanupRule");
		InitFileCleanupRules(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating file cleanup rules.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/DatabaseCleanup/OperationLogArchiveRule");
		InitOperationLogCleanupRules(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating database cleanup rules for OperationLog.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/DatabaseCleanup/BillingTabArchiveRule");
		InitBillingTabCleanupRules(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating database cleanup rules for BillingTab.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/DatabaseCleanup/TransferHistoryArchiveRule");
		InitTransferHistoryCleanupRules(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating database cleanup rules for Transfer History.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

	try {
		pNodeList = pCleanupDoc->selectNodes("CleanupRules/DatabaseCleanup/MaintenanceRule");
		InitDbMaintenanceRule(pNodeList, rules);
	} catch (const CrException& ce ) {
		string err = "An error occurred while locating database Maintenance rules.\n" + ce.GetErrorString();
		ReportError(g_log_type, err, EVENTLOG_WARNING_TYPE);
	}

}

void RuleParser::InitFileCleanupRules(const xml::DOMNodeListPtr& pNodeList,
									  std::vector<CleanupRule*>& fileRules)
{
	const long numNodes = pNodeList->getLength();

	for(long i = 0; i < numNodes; ++i) {
		try {
			std::auto_ptr<FileCleanup> rule(new FileCleanup());
			xml::DOMNodePtr pNode=pNodeList->getItem(i);

			PopulateDirSetting(pNode, *rule);
			PopulateSubDirSetting(pNode, *rule);
			PopulatePatternList(pNode, *rule, false);
			PopulatePatternList(pNode, *rule, true);
			PopulateFileAge(pNode, *rule);
			PopulateFileSize(pNode, *rule);
			PopulateFileCompression(pNode, *rule);

			fileRules.push_back(rule.release());
		}
		catch( const CrException& ex ) {
			AddWarning(ex.GetErrorString());
		}
	}
}

void RuleParser::InitOperationLogCleanupRules(const xml::DOMNodeListPtr& pNodeList,
											  std::vector<CleanupRule*>& rules)
{
	const long numNodes = pNodeList->getLength();

	for( long i = 0; i < numNodes; ++i ) {
		try {
			xml::DOMNodePtr pNode=pNodeList->getItem(i);
			xml::DOMElementPtr pAgeNode = pNode->selectSingleNode("Age");
			if ( pNode == 0 ) {
				AddWarning("Operation log cleanup rule is missing the Age node.");
				continue;
			}

			const string& sAge = XmlUtils::GetAttributeValue(pAgeNode, "NumDays");
			long age = 0;
			try {
				age = konga::toLong(sAge);
			}
			catch( const CrException& ex ) {
				AddWarning("Invalid age value: " + sAge + ". " + ex.GetErrorString());
				continue;
			}

			xml::DOMElementPtr pStatusNode = pNode->selectSingleNode("Status");
			if ( pStatusNode == 0 ) {
				AddWarning("Operation log cleanup rule is missing the Status node.");
				continue;
			}

			const string& sStatus = pStatusNode->getTextContent();
			rules.push_back( new OperationLogCleanupRule(age, sStatus) );
		}
		catch( const CrException& ex ) {
			AddWarning(ex.GetErrorString());
			continue;
		}
	}
}

void RuleParser::InitBillingTabCleanupRules(const xml::DOMNodeListPtr& pNodeList,
											  std::vector<CleanupRule*>& rules)
{
	const long numNodes = pNodeList->getLength();

	for( long i = 0; i < numNodes; ++i ) {
		try {
			xml::DOMNodePtr pNode=pNodeList->getItem(i);
			xml::DOMElementPtr pAgeNode = pNode->selectSingleNode("Age");
			if ( pNode == 0 ) {
				AddWarning("Billing tab cleanup rule is missing the Age node.");
				continue;
			}

			const string& sAge = XmlUtils::GetAttributeValue(pAgeNode, "NumDays");
			long age = 0;
			try {
				age = konga::toLong(sAge);
			}
			catch( const CrException& ex ) {
				AddWarning("Invalid age value: " + sAge + ". " + ex.GetErrorString());
				continue;
			}

			rules.push_back( new BillingTabCleanupRule(age) );
		}
		catch( const CrException& ex ) {
			AddWarning(ex.GetErrorString());
			continue;
		}
	}
}

void RuleParser::InitTransferHistoryCleanupRules(const xml::DOMNodeListPtr& pNodeList,
											  std::vector<CleanupRule*>& rules)
{
	const long numNodes = pNodeList->getLength();

	for( long i = 0; i < numNodes; ++i ) {
		try {
			xml::DOMNodePtr pNode=pNodeList->getItem(i);
			xml::DOMElementPtr pAgeNode = pNode->selectSingleNode("Age");
			if ( pNode == 0 ) {
				AddWarning("Transfer history cleanup rule is missing the Age node.");
				continue;
			}

			const string& sAge = XmlUtils::GetAttributeValue(pAgeNode, "NumDays");
			long age = 0;
			try {
				age = konga::toLong(sAge);
			}
			catch( const CrException& ex ) {
				AddWarning("Invalid age value: " + sAge + ". " + ex.GetErrorString());
				continue;
			}

			rules.push_back( new TransferHistoryCleanupRule(age) );
		}
		catch( const CrException& ex ) {
			AddWarning(ex.GetErrorString());
			continue;
		}
	}
}

namespace {
	bool isValueTrue(const string& value, bool def) {
		if ( value.empty() ) return def;
		return ( EqualsIgnoreCase(value, "true") || EqualsIgnoreCase(value, "yes") || value == "1" );
	}
}

void RuleParser::InitDbMaintenanceRule(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules) {
	
	                                                                                
	bool bVacuum = true, bFull = true, bReindex = true;

	                                                             
	if ( pNodeList.getLength() > 0 ) {
		xml::DOMNodePtr pMaintenanceRule = pNodeList->getItem(0);

		xml::DOMElementPtr pVacuum = pMaintenanceRule->selectSingleNode("Vacuum");
		if ( pVacuum == 0 ) {
			bVacuum = false;
			bFull = false;
		}
		else {
			const string& value = pVacuum->getTextContent();
			if ( isValueTrue(value, true) ) {
				bVacuum = true;
				const string& full = konga::XmlUtils::GetAttributeValue(pVacuum, "Full");
				bFull = isValueTrue(full, true);
			}
			else {
				                                
				bVacuum = false;
				bFull = false;
			}
		}

		xml::DOMElementPtr pReindex = pMaintenanceRule->selectSingleNode("Reindex");
		if ( pReindex == 0 ) {
			bReindex = false;
		}
		else {
			bReindex = isValueTrue(pReindex->getTextContent(), true);
		}
	}

	if ( bVacuum == false && bReindex == false ) return;

	rules.push_back(
		new DbMaintenanceCleanupRule(
		string(DBConnectionInfo("TranDb").connectionString()), bVacuum, bFull, bReindex)
		);
	rules.push_back(
		new DbMaintenanceCleanupRule(
		string(DBConnectionInfo("IDb").connectionString()), bVacuum, bFull, bReindex)
		);
	rules.push_back(
		new DbMaintenanceCleanupRule(
		string(DBConnectionInfo("MsgDb").connectionString()), bVacuum, bFull, bReindex)
		);
}

void RuleParser::InitTransformationDataStoreRule(const konga::xml::DOMNodeListPtr& pNodeList, std::vector<CleanupRule*>& rules) {
	                                                                                
	int ageDays = 1;
	if ( pNodeList.getLength() > 0 ) {
		xml::DOMElementPtr pStoreRule = pNodeList->getItem(0);
		if ( pStoreRule != 0 ) {
			xml::DOMElementPtr pAgeNode = pStoreRule->selectSingleNode("Age");
			if ( pAgeNode != 0 ) {
				const string stNumDays = pAgeNode->getAttribute("NumDays", false);
				if ( !stNumDays.empty() ) {
					ageDays = konga::toLong(stNumDays);
					                                      
					if ( ageDays < 1 ) ageDays = 0;
				}
			}
		}
	}

	rules.push_back( new TransformationDataStoreRule(ageDays) );
}

void RuleParser::PopulatePatternList(xml::DOMNodePtr pCleanupNode, 
									  FileCleanup& rule, 
									  bool isNotPattern)
{
	string queryString = isNotPattern?"./NotPattern":"./Pattern";
	if (pCleanupNode==0) {
		AddWarning("NULL argument passed to method PopulatePatternList.");
		return;
	}

	std::string errMsg;
	xml::DOMElementListPtr pNodeList = xml::DOMElementListPtr(pCleanupNode->selectNodes(queryString));
	const unsigned long numNodes = pNodeList.getLength();

	for( unsigned long i = 0; i < numNodes; ++i ) {
		xml::DOMElementPtr pNode = pNodeList.item[i];
		                                
		MatchPattern Pattern;
		Pattern.stPattern=pNode->getTextContent();

		                               
		std::string stPatternScope=pNode->getAttribute("ApplyToSubDirectory");
		if (stPatternScope.empty()) {
			Pattern.bApplyToSubdir=!isNotPattern;                      
		} else {
			Pattern.bApplyToSubdir = stricmp(stPatternScope.c_str(), "yes")==0;
		}

		if (isNotPattern)
			rule.AddNotPattern(Pattern);
		else
			rule.AddPattern(Pattern);
	}
}

void RuleParser::PopulateFileAge(xml::DOMNodePtr pCleanupNode,
								  FileCleanup& rule)
{
	xml::DOMElementPtr pFileAgeNode=pCleanupNode->selectSingleNode("./FileAge");
	if(pFileAgeNode==0)
		throw CrException("Failed to locate 'FileAge' element.");

	string strNodeValue( pFileAgeNode->getAttribute("NumDays") );
	if (strNodeValue.empty())
		throw CrException("Failed to locate 'NumDays' attribute for 'FileAge' element.");
	
	FileAge fAge;
	fAge.nDays = toLong(strNodeValue);

	strNodeValue=pFileAgeNode->getAttribute("Comparator");
	if (strNodeValue.empty())
		throw CrException("Failed to locate 'Comparator' attribute for 'FileAge' element.");

	fAge.comparator = FileCleanup::GetComparator(strNodeValue);

	rule.SetFileAge(fAge);
}

void RuleParser::PopulateFileSize(xml::DOMNodePtr pCleanupNode,
								   FileCleanup& rule)
{
	xml::DOMElementPtr pFileSize=pCleanupNode->selectSingleNode("./FileSize");
	if(pFileSize==0)
		throw CrException("Failed to locate 'FileSize' element.");

	string strNodeValue( pFileSize->getAttribute("Size") );
	if (strNodeValue.empty())
		throw CrException("Failed to locate 'Size' attribute for 'FileSize' element.");

	const long lSize = konga::toLong(strNodeValue);

	const string sizeUnit( pFileSize->getAttribute("SizeUnit") );
	if ( sizeUnit.empty() )
		throw CrException("Failed to locate 'SizeUnit' attribute for 'FileSize' element.");

	SizeUnit unit = FileCleanup::GetSizeUnit(sizeUnit);
	FileSize fSize;
	fSize.sizeBytes = FileCleanup::GetSizeInBytes(lSize, unit);

	strNodeValue=pFileSize->getAttribute("Comparator");
	if (strNodeValue.empty())
		throw CrException("Failed to locate 'Comparator' attribute for 'FileSize' element.");

	fSize.comparator = FileCleanup::GetComparator(strNodeValue);

	rule.SetFileSize(fSize);
}

void RuleParser::PopulateFileCompression(konga::xml::DOMNodePtr pCleanupNode,
										 FileCleanup& cleanupRule)
{
	xml::DOMElementPtr pCompress = pCleanupNode->selectSingleNode("Compress");
	if(pCompress==0)
		return;	                   

	FileCompression fComp;
	fComp.enabled = true;

	string sAttrVal( pCompress->getAttribute("MaxNumberOfCompressedFiles") );
	if ( sAttrVal.empty() )
		fComp.maxCompressed = 1;
	else
		fComp.maxCompressed = toLong(sAttrVal);

	cleanupRule.SetFileCompression(fComp);
}

void RuleParser::PopulateDirSetting(xml::DOMNodePtr pCleanupNode,
									 FileCleanup& rule)
{
	xml::DOMElementPtr pDirectoryPath=pCleanupNode->selectSingleNode("./DirectoryPath");
	if (pDirectoryPath==0)
		throw CrException("Failed to locate 'DirectoryPath' element.");

	std::string strNodeValue=pDirectoryPath->getTextContent();
	if (strNodeValue.empty())
		throw CrException("Failed to get the value of 'DirectoryPath' element.");

	rule.SetDirectory(strNodeValue);

	strNodeValue=pDirectoryPath->getAttribute("SearchSubDirectory");
	if (strNodeValue.empty())
		throw CrException("Failed to locate 'SearchSubDirectory' attribute for 'DirectoryPath' element.");

	if (stricmp(strNodeValue.c_str(), "yes")==0)
		rule.SetSearchOption(true);
	else
		rule.SetSearchOption(false);

	rule.ConvertToAbsolutePath();
}

void RuleParser::PopulateSubDirSetting(xml::DOMNodePtr pCleanupNode,
										FileCleanup& rule)
{
	                                                               
	if (!rule.GetSearchOption()) 
		return;

	if (pCleanupNode==0)
		throw CrException("NULL argument passed to method PopulateSubDirSetting.");

	xml::DOMElementListPtr pNodeList = xml::DOMElementListPtr(pCleanupNode->selectNodes("./IgnoreSubDirectory"));
	const unsigned long numNodes = pNodeList.getLength();

	for( unsigned long i = 0; i < numNodes; ++i ) {
		xml::DOMElementPtr pNode=pNodeList.item[i];
		                                                           
		std::string nodeValue = pNode->getTextContent();
		if (nodeValue.empty())
			throw CrException("Failed to get the value of element 'IgnoreSubDirectory'.");

		                                                              
		std::string stSubDirPath = pNode->getTextContent();
		if (!PathIsRelative(stSubDirPath.c_str())) {
			if (!konga::IsDirectory(stSubDirPath.c_str()))
				continue;
			char buffer[1024];
			strcpy(buffer, PathFindFileName(stSubDirPath.c_str()));
			PathRemoveBackslash(buffer);
			stSubDirPath = buffer;
		}

		rule.AddIgnoreSubDir(stSubDirPath);
	}
}

xml::DOMDocumentPtr RuleParser::ReadXmlSource()
{
	std::string errorString;
	                                                               
	xml::DOMDocumentPtr pTempDoc = konga::XmlUtils::LoadDoc(m_stXmlFilePath, errorString, true);
	if (pTempDoc==0) {
		string err = "Failed to load the CleanupRules.xml document.\n" + errorString;
		throw CrException(FC_ERR_INVALID_XML_FILE, err);
	}
	return pTempDoc;
}

void RuleParser::AddWarning(const std::string& stWarning) {
	m_Warnings.push_back(stWarning);
}

const vector<string>& RuleParser::GetWarnings() const {
	return m_Warnings;
}
