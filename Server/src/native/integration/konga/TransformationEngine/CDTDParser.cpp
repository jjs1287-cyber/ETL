/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/util.h>

#include <konga/Log/logging.h>

#include <konga/Soap/SoapEncoding.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlIncludes.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include <konga/DataElement/DataElement.h>

#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/WDIPathFinder.h>

#include <konga/OperationContext/DefaultOperationContext.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include <konga/TransformationUtils/TranDBUtils.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "CDTDParser.h"
#include "DTDParser.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "WebServiceUtils.h"
#include "WsdlUtils.h"
#include "XsdFromWsdlCreator.h"
#include "TreeMapper.h"

using namespace konga;
using namespace konga::xml;
using std::string;
using std::vector;

DOMElementPtr FindSchemaElement(const konga::xml::DOMElementPtr& pNode);
unsigned long AddSchemaInstanceNamespacePrefix(DOMElementPtr pSchemaNode, XsdConstants::SchemaVersion schemaVersion);

CDTDParser::CDTDParser(const konga::UserInfo& currentUser, const char* sPrefix) : TransformationEngine(currentUser),
	m_parser(0), m_iLastWsdlStatus(-1)
{
	m_pTreeMapper=new TreeMapper(this,this->m_pTransformationDataHolder, false, false, false, false);

	m_parser.setTreeMapper(m_pTreeMapper);
	CrDateTime SysTime;

	if(sPrefix == 0)
		getViewLog()->Initialize("CDTDParser", 0, m_debug_log, false, m_bWriteLog, 0, 0, false, konga::GetGUIDString().substr(0,8));
	else
		getViewLog()->Initialize(sPrefix, 0, m_debug_log, false, m_bWriteLog, 0, 0, false, konga::GetGUIDString().substr(0,8));
	initPROFILING("CDTDParser", -1);
}

CDTDParser::~CDTDParser(void) {
	if ( m_pTreeMapper ) delete m_pTreeMapper;
	m_pTreeMapper = 0;
}

                                                      
unsigned long CDTDParser::CreateBrowserTree(const std::string& stInfile, const std::string& stRoot_str, const std::string& stJavaFile, const std::string& stTitle, const std::string& stClick)
{
	PROFILING doPrf(this,"CDTDParser_CreateBrowserTree");
    KString infile(stInfile), root_str(stRoot_str), javafile(stJavaFile), title(stTitle), click(stClick);

	unsigned long hr = S_OK;
	try
	{
		             
		if (getViewLog()->IsDebugLogEnabled())
		{
			KString sDebugFile;
			WDIPathFinder::GetWDIDebugPath(sDebugFile);
			sDebugFile += "CreateBrowserTree_"+konga::GetGUIDString().substr(0,8)+".log";

			m_debug_log=fopen(sDebugFile, "a+");
			if (m_debug_log){
				fprintf(m_debug_log,"----------------------\nCreateBrowserTree(inputFileName=%s,rootName=%s,javaFileName=%s,click=%s,title=%s)\n",
					(const char*)infile,(const char*)root_str,(const char*)javafile,(const char*)click,(const char*)title);
				fflush(m_debug_log);
			}
		}

		if( infile.IsEmpty() )
		{
			throw konga::CrException("Internal error: Unexpected call to CDTDParser::CreateBrowserTree() infile is empty.");
		}

		int err = 0;	
		int k=infile.ReverseFind('.');        

		const char* fileExtension="";
		if(k>0)fileExtension=&((const char*)infile)[k+1];

		m_parser.SetRequireValidSchema(false);

		const std::string::size_type len=stClick.size();
		const bool bIsTarget=len>=6 && stricmp(stClick.substr(len-6).c_str(),"target")==0;
		m_parser.SetTarget(bIsTarget);

		                        
		if(stricmp(fileExtension,"scm")==0){
			                                    
			throw konga::InternalErrorException("Unexpected call to CDTDParser::CreateBrowserTree(stInfile=\""+stInfile+
				"\", stRoot_str=\""+stRoot_str+
				"\", stJavaFile=\""+stJavaFile+
				"\", stTitle=\""+stTitle+
				"\", stClick=\""+stClick+
				"\")\nThis method does not expect to see an scm file");
		}
		else if(stricmp(fileExtension,"dtd")==0){
			err=m_parser.createJtrFromDtd((const char*)infile,(const char*)root_str,(const char*)javafile);
		}
		else if(stricmp(fileExtension,"xsd")==0){
			err=m_parser.createJtrFromXSD((const char*)infile,(const char*)root_str,(const char*)javafile);
		}
		else {
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTDPARSER_ERR_UNKNOWN_FILE_EXTENSION);
			m_lastErr += getErrorLog()->GetLastErr();
			return hr;
		}
		                       

		if(err){
			hr=E_FAIL;
			m_lastErr += m_parser.m_lastErr+"\ninfile="+infile;
			if(m_debug_log){
				fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
				fflush(m_debug_log);
			}
		}


		if(getViewLog()->IsDebugLogEnabled())
		{
			if(err==0)
				getViewLog()->DebugString("\nCDTDParser::CreateBrowserTree SUCCESS END\n");
			else
				getViewLog()->DebugString("%s\n",(const char*)m_lastErr);
		}
	}
	catch(const CrFatalException& e)
	{
		std::string csErrMsg = e.GetErrorString();
		m_lastErr += "\nAn error was thrown when writing to the disk. The disk may be full or write-protected.";
		hr = E_FAIL;
	}

	if (m_debug_log) {
		fclose(m_debug_log);
		m_debug_log = 0;
	}
	
	return hr;
}


std::string CDTDParser::get_LastError() const
{
	return string( static_cast<const char*>(m_lastErr) );
}

int CDTDParser::GetLastWsdlStatus()
{
	return m_iLastWsdlStatus;
}

std::string CDTDParser::GetLastWsdlWarningMessage()
{
	return m_stLastWsdlWarningMsg;
}

unsigned long CDTDParser::CreateXSDFromWSDLForBothIO(const std::string& stWSDLFileName,  
										   const std::string& stBinding, 
										   const std::string& stOperationName, 
										   const std::string& stOperationInputName,
										   const std::string& stOperationOutputName,
										   const std::string& stSoapOperationStyle, 
										   const std::string& stSoapBodyUse, 
										   const std::string& stInputXSDFileName, 
										   const std::string& stOutputXSDFileName)
{
	PROFILING doPrf(this,"CDTDParser_CreateXSDFromWSDLForBothIO");
	CViewLog* viewLog=getViewLog();
	CErrorLog* errorLog=getErrorLog();
	{

	KString sOperationStyle(stSoapOperationStyle);
	sOperationStyle.MakeLower();
    KString sBodyUse(stSoapBodyUse);
	sBodyUse.MakeLower();

	viewLog->DebugString("CreateXSDFromWSDLForBothIO construct XsdFromWsdlCreator xsdCreator\n");

	XsdFromWsdlCreator xsdCreator( m_pTreeMapper,
								   stWSDLFileName,
								   stBinding,
								   stOperationName,
								   stOperationInputName,
								   stOperationOutputName,
								   (const char*)sOperationStyle,
								   (const char*)sBodyUse,
								   stInputXSDFileName,
								   stOutputXSDFileName,
								   errorLog);
	xsdCreator.SetRequireValidSchema(false);
	viewLog->DebugString("CreateXSDFromWSDLForBothIO entering xsdCreator.CreateXsdFilesForTransformation()\n");
	unsigned long hr = xsdCreator.CreateXsdFilesForTransformation();
	if( FAILED(hr) )
	{
		m_iLastWsdlStatus = 2;
		m_stLastWsdlWarningMsg = "";
		m_lastErr += std::string("CDTDParser::CreateXSDFromWSDLForBothIO() failed\n")+xsdCreator.GetLastError().c_str();
		return hr;
	}
	else if( xsdCreator.GetSchemaValidationWarning().length() )
	{
		m_iLastWsdlStatus = 1;
		m_stLastWsdlWarningMsg = xsdCreator.GetSchemaValidationWarning();
	}
	else
	{
		m_iLastWsdlStatus = 0;
		m_stLastWsdlWarningMsg = "";
	}
	viewLog->DebugString("CreateXSDFromWSDLForBothIO exitting 1\n");
	}

	viewLog->DebugString("CreateXSDFromWSDLForBothIO exitting 2\n");
	return S_OK;
}

unsigned long CDTDParser::CreateXSDFromWSDL(const char*csWSDLFileName,  
									const char*csBinding, 
									const char*csOperationName,
									const char*csOperationStyle,
									const char*csBodyUse,
									short intIOFlag, 
									const char*csXSDFileName)
{
	unsigned long hr = S_OK;

	                                                  

	return hr;
}

DOMElementPtr FindSchemaElement(const konga::xml::DOMElementPtr& pNode0)
{
	if(pNode0 == 0)
		return 0;
    konga::xml::DOMElementPtr pNode=pNode0;
	while(pNode != 0)
	{
		if(stricmp(pNode->getBaseName().c_str(), "schema") == 0)
			return pNode;
		DOMNodeListPtr pChildren=pNode->getChildNodes();
		int n=pChildren->getLength();
		for(int i=0;i<n;i++){
			DOMNodePtr pChild=pChildren->item[i];
			if(pChild->getNodeType()!=konga::xml::NODE_ELEMENT)continue;
			DOMElementPtr tmp=FindSchemaElement(pChild);
			if(tmp!=0)return tmp;
		}

		pNode = pNode->getNextSibling();
	}
	return 0;
}

unsigned long AddSchemaInstanceNamespacePrefix(DOMElementPtr pSchemaNode, XsdConstants::SchemaVersion schemaVersion)
{
	std::string sName = "xmlns:xsi";
	int n = 2;
    std::string value= pSchemaNode->getAttribute(sName);
	while(value.c_str()!=0)
	{
		sName = "xmlns:xsi"+ konga::toString(n++);
		value = pSchemaNode->getAttribute(sName);
	}
	return pSchemaNode->setAttribute(sName.c_str(), XsdUtils::GetSchemaInstanceNamespace(schemaVersion).c_str());
}

namespace {
	bool isSubElement(KString sName,CROM*obj){
		if(sName==obj->GetName())return true;
		CROM*pChild=obj->GetFirstChild();
		while(pChild!=0){
			if((pChild->m_Type&CROM_t_AttV)==0){
				if(isSubElement(sName,pChild))return true;
			}
			pChild=pChild->GetNextSibling();
		}
		return false;
	}
}

unsigned long CDTDParser::GetRootSelectionList(const std::string& bstrDtdFileName, std::vector<std::string>& vMostlikely, std::vector<std::string>& vLessLikely)
{
	PROFILING doPrf(this,"CDTDParser_GetRootSelectionList");

	unsigned long hr = S_OK;

	KString sDtdFileName(bstrDtdFileName);
	try
	{
		if (getViewLog()->IsDebugLogEnabled())
		{
			KString sDebugFile;
			WDIPathFinder::GetWDIDebugPath(sDebugFile);
			sDebugFile += "GetRootSelectionList_"+konga::GetGUIDString().substr(0,8)+".log";

			m_debug_log=fopen(sDebugFile, "a+");
			if (m_debug_log){
				fprintf(m_debug_log,"----------------------\nGetRootSelectionList(DtdFileName=%s)\n",
					(const char*)sDtdFileName);
				fflush(m_debug_log);
			}
		}
		int err = 0;	
		int k=sDtdFileName.ReverseFind('.');

		KString fileExtension;
		if(k>0)fileExtension=&((const char*)sDtdFileName)[k+1];
		if(stricmp((const char*)fileExtension,"dtd")!=0&&
			stricmp((const char*)fileExtension,"xsd")!=0&&
			stricmp((const char*)fileExtension,"wsdl")!=0)
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTDPARSER_ERR_UNKNOWN_FILE_EXTENSION);
			m_lastErr += getErrorLog()->GetLastErr();
			return hr;
		}
		if(stricmp((const char*)fileExtension,"dtd")==0){
			bool root_selected=false;
			err=m_parser.parseDtd(sDtdFileName,"",root_selected);

			if(err){
				m_lastErr=m_parser.m_lastErr;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
				}
				return E_FAIL;
			}
			vMostlikely.clear();
			vLessLikely.clear();
			std::vector<CROM*>&obj_list=m_parser.m_obj_list;
			int n=(int)obj_list.size();
			bool*bSubElement=new bool[n];
			for(int i=0;i<n;i++){
				bSubElement[i]=false;
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot()){
					for(int j=0;j<n;j++){
						if(obj_list[j]==0)continue;
						if(i!=j&&isSubElement(obj_list[i]->GetName(),obj_list[j])){
							bSubElement[i]=true;
						}
					}
				}
			}
			for(int i=0;i<n;i++){
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot() && !bSubElement[i])
					vMostlikely.push_back(std::string((const char*)obj_list[i]->GetDisplayName()));
			}
			for(int i=0;i<n;i++){
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot() && bSubElement[i])
					vLessLikely.push_back(std::string((const char*)obj_list[i]->GetDisplayName()));
			}
			delete[]bSubElement;
		} else if(stricmp((const char*)fileExtension,"xsd")==0){
			std::string stXsdFileName=(const char*)sDtdFileName;
			std::vector<std::string>vRootNames;
			XSDParser::GetPossibleRoots(stXsdFileName,vRootNames,m_pTreeMapper);
			vMostlikely.clear();
			int n=(int)vRootNames.size();
			for(int i=0;i<n;i++){
				vMostlikely.push_back(vRootNames[i]);
			}
		} else if(stricmp((const char*)fileExtension,"wsdl")==0){
			std::string stWsdlFileName=(const char*)sDtdFileName;
			std::vector<std::string>vMethodNames;
			std::string stErr;
			hr=WebServiceUtils::GetOperationList(stWsdlFileName,vMethodNames,stErr);
			if(FAILED(hr)){
				m_lastErr=stErr.c_str();
			} else {
				vMostlikely.clear();                                                              
				int n=(int)vMethodNames.size();
				for(int i=0;i<n;i++){
					vMostlikely.push_back(vMethodNames[i]);
				}
			}
		}

		if(getViewLog()->IsDebugLogEnabled())
		{
			if(err==0)
				getViewLog()->DebugString("\nCDTDParser::GetRootSelectionList SUCCESS END\n");
			else
				getViewLog()->DebugString("%s\n",(const char*)m_lastErr);
		}
	}
	catch(const CrFatalException& e)
	{
		std::string csErrMsg = e.GetErrorString();
		m_lastErr += "\nAn error was thrown when writing to the disk. The disk may be full or write-protected.";
        throw CrException();
	}

	if (m_debug_log) {
		fclose(m_debug_log);
		m_debug_log = 0;
	}
	
	return hr;
}


unsigned long CDTDParser::GetRootSelectionList(const std::string& bstrDtdFileName, const std::string& bstrOutputFileName, const std::string& bstrOutputFileName2)
{
	PROFILING doPrf(this,"CDTDParser_GetRootSelectionList");

	unsigned long hr = S_OK;

	KString sDtdFileName(bstrDtdFileName),sOutputFileName(bstrOutputFileName),sOutputFileName2(bstrOutputFileName2);
	try
	{
		if (getViewLog()->IsDebugLogEnabled())
		{
			KString sDebugFile;
			WDIPathFinder::GetWDIDebugPath(sDebugFile);
			sDebugFile += "GetRootSelectionList_"+konga::GetGUIDString().substr(0,8)+".log";

			m_debug_log=fopen(sDebugFile, "a+");
			if (m_debug_log){
				fprintf(m_debug_log,"----------------------\nGetRootSelectionList(DtdFileName=%s,OutputFileName=%s,OutputFileName2=%s)\n",
					(const char*)sDtdFileName,(const char*)sOutputFileName,(const char*)sOutputFileName2);
				fflush(m_debug_log);
			}
		}
		int err = 0;
		int k=sDtdFileName.ReverseFind('.');

		KString fileExtension;
		if(k>0)fileExtension=&((const char*)sDtdFileName)[k+1];
		if(stricmp((const char*)fileExtension,"dtd")!=0&&
			stricmp((const char*)fileExtension,"xsd")!=0&&
			stricmp((const char*)fileExtension,"wsdl")!=0)
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_DTDPARSER_ERR_UNKNOWN_FILE_EXTENSION);
			m_lastErr += getErrorLog()->GetLastErr();
			return hr;
		}
		if(stricmp((const char*)fileExtension,"dtd")==0){
			bool root_selected=false;
			err=m_parser.parseDtd(sDtdFileName,"",root_selected);

			if(err){
				m_lastErr=m_parser.m_lastErr;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
				}
				return E_FAIL;
			}
			FILE*out,*out2;

			out=fopen(sOutputFileName,"w+");
			if(out==0)
			{
				hr=E_FAIL;
				m_lastErr += " can't open file for writing, fileName="+sOutputFileName;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
					fclose(m_debug_log);
					m_debug_log=0;
				}
				return hr;
			}
			AutoFileHandle adopt_out(out);

			out2=fopen(sOutputFileName2,"w+");
			if(out2==0)
			{
				hr=E_FAIL;
				m_lastErr += " can't open file for writing, fileName="+sOutputFileName2;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
					fclose(m_debug_log);
					m_debug_log=0;
				}
				return hr;
			}
			AutoFileHandle adopt_out2(out2);
			std::vector<CROM*>&obj_list=m_parser.m_obj_list;
			int n=(int)obj_list.size();
			bool*bSubElement=new bool[n];
			for(int i=0;i<n;i++){
				bSubElement[i]=false;
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot()){
					for(int j=0;j<n;j++){
						if(i!=j&&isSubElement(obj_list[i]->GetName(),obj_list[j])){
							bSubElement[i]=true;
						}
					}
				}
			}
			for(int i=0;i<n;i++){
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot() && !bSubElement[i])
					fprintf(out,"%s\n",(const char*)obj_list[i]->GetDisplayName());
			}
			for(int i=0;i<n;i++){
				if(obj_list[i]!=0 && obj_list[i]->CouldBeRoot() && bSubElement[i])
					fprintf(out2,"%s\n",(const char*)obj_list[i]->GetDisplayName());
			}
			delete[]bSubElement;
		} else if(stricmp((const char*)fileExtension,"xsd")==0){
			FILE*out;
			out=fopen(sOutputFileName,"w+");
			if(out==0)
			{
				hr=E_FAIL;
				m_lastErr += " can't open file for writing, fileName="+sOutputFileName;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
					fclose(m_debug_log);
					m_debug_log=0;
				}
				return hr;
			}
			AutoFileHandle adopt_out(out);

			std::string stXsdFileName=(const char*)sDtdFileName;
			std::vector<std::string>vRootNames;
			XSDParser::GetPossibleRoots(stXsdFileName,vRootNames,m_pTreeMapper);
			int n=(int)vRootNames.size();
			for(int i=0;i<n;i++){
				fprintf(out,"%s\n",vRootNames[i].c_str());
			}
		} else if(stricmp((const char*)fileExtension,"wsdl")==0){
			FILE*out;
			out=fopen(sOutputFileName,"w+");
			if(out==0)
			{
				hr=E_FAIL;
				m_lastErr += " can't open file for writing, fileName="+sOutputFileName;
				if(m_debug_log){
					fprintf(m_debug_log,"m_lastErr=%s\n",(const char*)m_lastErr);
					fflush(m_debug_log);
					fclose(m_debug_log);
					m_debug_log=0;
				}
				return hr;
			}
			AutoFileHandle adopt_out(out);

			std::string stWsdlFileName=(const char*)sDtdFileName;
			std::vector<std::string>vMethodNames;
			std::string stErr;
			hr=WebServiceUtils::GetOperationList(stWsdlFileName,vMethodNames,stErr);
			if(FAILED(hr)){
				m_lastErr=stErr.c_str();
			} else {
				int n=(int)vMethodNames.size();
				for(int i=0;i<n;i++){
					fprintf(out,"%s\n",vMethodNames[i].c_str());
				}
			}
		}

		if(getViewLog()->IsDebugLogEnabled())
		{
			if(err==0)
				getViewLog()->DebugString("\nCDTDParser::GetRootSelectionList SUCCESS END\n");
			else
				getViewLog()->DebugString("%s\n",(const char*)m_lastErr);
		}
	}
	catch(const CrFatalException& e)
	{
		std::string csErrMsg = e.GetErrorString();
		m_lastErr += "\nAn error was thrown when writing to the disk. The disk may be full or write-protected.";
        throw CrException();
	}

	if (m_debug_log) {
		fclose(m_debug_log);
		m_debug_log = 0;
	}
	
	return hr;
}


std::string CDTDParser::GetSystemRootName(const std::string& stSampleXmlFileName)
{
    std::string stErr;
	DOMDocument2Ptr pDoc = XmlUtils::LoadDoc(stSampleXmlFileName, stErr, false);                             
    if ( !stErr.empty() ) {
        m_lastErr += stErr;
        throw CrException();
    }

	DOMElementPtr pDocumentElement=pDoc->getDocumentElement();
	return pDocumentElement->getBaseName();
}

unsigned long CDTDParser::CreateJtrForWS(const std::string& stWsdlFileName, 
										const std::string& stBinding, 
										const std::string& stOperation, 
										const std::string& stOperationInputName,
										const std::string& stOperationOutputName,
										const std::string& stSoapOperationStyle, 
										const std::string& stSoapBodyUse, 
										const std::string& stRequestJtrFileName, 
										const std::string& stResponseJtrFileName,
										const std::string& stTmpDir_in          )
{
	PROFILING doPrf(this,"CDTDParser_CreateJtrForWS");

	                                 
	std::string stTempDir( stTmpDir_in.empty() ? konga::getTmpDir() : stTmpDir_in );
	std::string stLogFileName=konga::getTmpFileName("CreateJtr_",stTempDir);
	std::string stRequestXSDFileName=stLogFileName+"_req.xsd";
	std::string stResponseXSDFileName=stLogFileName+"_res.xsd";

	std::string stRootName="root", stDisplayOption="xml";

	XsdFromWsdlCreator xsdCreator( m_pTreeMapper,
								   stWsdlFileName,
								   stBinding,
								   stOperation,
								   stOperationInputName,
								   stOperationOutputName,
								   stSoapOperationStyle,
								   stSoapBodyUse,
								   stRequestXSDFileName,
								   stResponseXSDFileName,
								   getErrorLog());
	xsdCreator.SetRequireValidSchema(false);
	unsigned long hr = xsdCreator.CreateXsdFilesForDisplay();
	if(FAILED(hr))
	{
		m_lastErr += std::string("CDTDParser::CreateJtrForWS() failed\n")+xsdCreator.GetLastError().c_str();
		return hr;
	}

	getViewLog()->DebugString("call CreateBrowserTree for request %s\n",stRequestJtrFileName.c_str());
	hr=CreateBrowserTree(stRequestXSDFileName, stRootName, stRequestJtrFileName, stRequestXSDFileName,
                          stDisplayOption);
	if(FAILED(hr))
	{
		getViewLog()->DebugString("ERROR: %s\n",(const char*)m_lastErr);
		m_lastErr += std::string("CDTDParser::CreateJtrForWS() failed\n")+m_lastErr;
		return hr;
	}

	getViewLog()->DebugString("call CreateBrowserTree for response %s\n",stResponseJtrFileName.c_str());
	hr=CreateBrowserTree(stResponseXSDFileName, stRootName, stResponseJtrFileName, stResponseXSDFileName,
                          stDisplayOption);
	if(FAILED(hr))
	{
		getViewLog()->DebugString("ERROR: %s\n",(const char*)m_lastErr);
		m_lastErr += std::string("CDTDParser::CreateJtrForWS() failed\n")+m_lastErr;
		return hr;
	}

	getViewLog()->DebugString("done removing tmp files\n");
	if(WDILogFileUtils::GenerateLogFile()==false){
		                                 
		konga::kDeleteFile(stLogFileName.c_str());
		konga::kDeleteFile(stRequestXSDFileName.c_str());
		konga::kDeleteFile(stResponseXSDFileName.c_str());
	}

	return S_OK;
}


unsigned long CDTDParser::ReadDtdDefinition(const string& stFileName,
											vector<string>& vExistingIncludes,
											vector<string>& vMissingIncludes)
{
	PROFILING doPrf(this,"CDTDParser_ReadDtdDefinition");
	vExistingIncludes.clear();
	vMissingIncludes.clear();

	                                        
	long bytesRead;
	string stError;
	boost::scoped_array<char> pBuf( read_file(stFileName, bytesRead, stError) );
	if ( !stError.empty() ) {
		m_lastErr = stError.c_str();
		return READ_FILE_ERR;
	}

	                               
	const string stPathName( GetBranchPath(stFileName) );

	unsigned long lRetVal = m_parser.ReadDtdDef(pBuf.get(), stPathName.c_str(), false, true);
	if ( lRetVal == READ_SYSTEM_LOCAL_ERR ) {
		                           
		vExistingIncludes = m_parser.GetExistingDtdFiles();
		vMissingIncludes = m_parser.GetMissingDtdFiles();
		const vector<string> vMissingDtdFiles = m_parser.GetMissingDtdFiles();
		return 0;
	}
	else if ( lRetVal == READ_SYNTAX_ERROR ) {
		m_lastErr = m_parser.m_lastErr;
		return lRetVal;
	}
	else if ( lRetVal != 0 ) {
		m_lastErr = m_parser.m_lastErr;
		return lRetVal;
	}

	return 0;
}
