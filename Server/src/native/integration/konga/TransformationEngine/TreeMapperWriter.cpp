/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
                                                                                                           

#include <cassert>

#include <boost/scoped_ptr.hpp>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>					                 
#include <konga/Utils/util.h>
#include <konga/Utils/StringUtils.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Soap/SoapFault_11.h>
#include <konga/Xml/XmlUtils.h>

#include <konga/SystemVariables/SystemVariables.h>
#include <konga/Server/WDIPathFinder.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include <konga/JdbcUtils/WriteTarget.h>
#include <konga/TransformationUtils/TranDBUtils.h>

#include "ErrorCodes/resource.h"
#include "CRData.h"
#include "CROMData.h"
#include "DbWriter.h"
#include "CromToJtrWriter.h"
#include "SystemVariableDefs.h"
#include "TreeMapper.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

void TreeMapper::XmlTargetWriter(const char* targetXMLFileName, FILE*lnk)
{
	PROFILING doPrf(this,"XmlTargetWriter");
	KString tmp;
	unsigned long hr;

	getViewLog()->DebugString("TreeMapper::XmlTargetWriter - targetXMLFileName %s\n", targetXMLFileName);		

	if(m_targetXmlParser.m_poDoc==0){
		std::string stErr;
		if ( XmlUtils::CreateDOMDocument2Instance(m_targetXmlParser.m_poDoc, true, stErr) != 0 ) {
			hr=getErrorLog()->MakeError(__FILE__,__LINE__, ID_TREEMAPPER_FAILED_CREATE_DOMDUCUMENT);
			KString sErr(getErrorLog()->GetLastErr());
			throw konga::CrException((const char*)sErr);
		}
	}

	CROM* pRootNode=m_targetDTDParser->FindRoot();
	if(m_targetXmlParser.IsDTDType())
	{
		if((hr=m_targetXmlParser.SetDtd(m_outputStructFileName,pRootNode->GetDisplayName()))!=S_OK)
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_XMLXMLWRITER_SETDTD_FAILED, (const char*)m_outputStructFileName, (const char*)m_targetXmlParser.m_LastErr);
			KString sErr(getErrorLog()->GetLastErr());
			throw konga::CrException((const char*)sErr);
		}
		m_targetXmlParser.SetDtdUrl(m_Target_DTD_URL);
	}


	{
		PROFILING doPrf1(this,"DoXmlMap");
		FILE*out=fopen(targetXMLFileName, getWriteModeStr());
		if(out==0) {
			const long lErr=GetLastError();
			const std::string sErr(std::string("Failed to open target file '") + targetXMLFileName + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
			getViewLog()->DebugString("%s\n", sErr.c_str());
			throw konga::CrException(sErr);
		}
		AutoFileHandle adopt_out(out);

		CROM*curTargetNode=m_targetDTDParser->FindRoot();
		CRData* curSourceData=m_pSourceDataRoot;
		if(!IsNoSource()){
			if(IsFlatSource()) {
				CROMData* p=dynamic_cast<CROMData*>(m_pSourceDataRoot);
				assert(p);
				curSourceData=p->GetFirstChild();
				if(curSourceData==0){
					throw konga::CrException("No source data was created. This probably means that the source file is empty.");
				}
			}
			else {
				if(curSourceData==0){
					throw konga::CrException("An internal error happened. This is a bug, please contact ETLForge Support. Detailed error message was: XmlTargetWriter failed, the root of the source data tree is null.");
				}
			}
		}

		int nTargetDataNode;
#ifdef _DEBUG
		CRXmlData::resetLabel();
#endif
		FILE* update_source_file=0;
		if( S_OK != (hr = DoXmlMap(out, curTargetNode, curSourceData, nTargetDataNode, lnk, update_source_file, xmlPrettify())) )
		{
			CROM* sourceDTDNode=curSourceData->GetDTDNode();
			KString csSourcePath = sourceDTDNode ? sourceDTDNode->get_Path() : "";
			KString csTargetPath = curTargetNode->get_Path();
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_XMLXMLWRITER_DOXMLXMLMAP_FAILED, (const char*)csSourcePath, (const char*)csTargetPath);
			KString sErr(getErrorLog()->GetLastErr());

			                                                                      
			if( IsHostedWebServiceOperation() )
			{
				m_pSoapFault = new SoapFault_11(FaultCodeValue_11::Server, "Failed to process the web service.", 
												"", static_cast<const char*>(m_lastErr));
			}
			throw konga::CrException((const char*)sErr);
		}
		fputc('\n', out);
		size_t end_pos_saved=ftell(out);		
		adopt_out.Close();
		konga::ftruncate(targetXMLFileName, end_pos_saved);
	}
	EvaluateJMSGlobalDataElement();

	char *pUserDefPath = 0;
	bool bRemoveDtdPath = true;

	if (!m_Target_DTD_URL.IsEmpty())
	{
		pUserDefPath = m_Target_DTD_URL.GetBuffer(m_Target_DTD_URL.GetLength());
		bRemoveDtdPath = false;
	}

	if( hr != S_OK )	                                           
	{
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_XMLXMLWRITER_XMLSAVE_FAILED, targetXMLFileName, (const char*)m_targetXmlParser.m_LastErr);
		KString sErr=getErrorLog()->GetLastErr();

		                                                                      
		if( IsHostedWebServiceOperation() )
		{
			                                                  
			std::string stFaultString = "";
			stFaultString += m_lastErr;
			m_pSoapFault = new SoapFault_11(FaultCodeValue_11::Server, stFaultString);
		}

		throw konga::CrException((const char*)sErr);
	}

	if(getViewLog()->IsDebugLogEnabled()){
		konga::kCopyFile(targetXMLFileName, getDebugFileName()+".output");
	}

	                              
	   
	                           
	                                         
	                                                          

	                                                                                                            
	    
	                                 
	                                                               
	                                                                                              
	                                                             
	                                              

	                                                                          
	                                       
	     
	                               
	                                                 
	                                                                                                                        
	         
	                                                                                                                                                   
	                                                                               
	                                                   
	     

	                                                
	    
	                                
	   

}

void TreeMapper::HfTargetWriter(const char*targetHFileName, FILE*lnk)
{
	PROFILING doPrf(this,"HfTargetWriter");

	getViewLog()->DebugString("TreeMapper::HfTargetWriter - targetHFileName %s\n", targetHFileName);		

	CROM* pDtdRoot=m_targetDTDParser->FindRoot();
	if(pDtdRoot==0){
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_XMLHFWRITER_TARGET_ROOT_NODE_ISNULL);
		KString sErr(getErrorLog()->GetLastErr());
		getViewLog()->DebugString("%s\n", (const char*)sErr);
		throw konga::CrException((const char*)sErr);
	}

	FILE*out=fopen(targetHFileName, getWriteModeStr());
	if(out==0) {
		const long lErr=GetLastError();
		const std::string sErr(std::string("Failed to open target file '") + targetHFileName + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
		getViewLog()->DebugString("%s\n", sErr.c_str());
		throw konga::CrException(sErr);
	}
	AutoFileHandle adopt_out(out);

	boost::scoped_ptr<konga::Utf8Conv> conv(0);

	CRData*curSourceData=m_pSourceDataRoot;
	if(curSourceData==0 && !IsNoSource()){
		throw konga::CrException("HfTargetWriter failed, the root of the source data tree is null.");
	}

	FILE* update_source_file=0;

	CROM* curTargetNode=pDtdRoot;
	if(m_targetType != LdapTypeId){
		                
		                                                                                 
		curTargetNode=pDtdRoot->GetFirstChild();
	}
	if(IsFlatSource()&&!curTargetNode->isLoopNode()){
		CROMData* curSourceData_=(CROMData*)curSourceData, *firstCROMDataChild=curSourceData_->GetFirstChild();
		if(firstCROMDataChild!=0){
			CROM* dtd=firstCROMDataChild->GetDTDNode();
			if(dtd!=0 && dtd->GetName()==curTargetNode->GetName()){
				curSourceData=firstCROMDataChild;
			}
		}
	}

	for(; curTargetNode; curTargetNode=curTargetNode->GetNextSibling()){
		int iCreatedNodes=0;
		long long nTotalRecords = 0;
		unsigned long hr = DoHfMap_Write2Crom(out, curTargetNode, curSourceData, iCreatedNodes, lnk, update_source_file, nTotalRecords, conv.get());
		if (hr != S_OK){
			KString sOriginalError(m_lastErr);
			CROM* sourceCROMNode=curSourceData->GetDTDNode();
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_HFXMLWRITER_DOHFXMLMAP_FAILED, 
					 (const char*)curTargetNode->get_Path(), sourceCROMNode? (const char*)sourceCROMNode->get_Path(): "");
			KString sErr(sOriginalError);
			sErr += getErrorLog()->GetLastErr();
			throw konga::CrException((const char*)sErr);
		}
		m_lTxtTgtRecords = nTotalRecords;
		m_lTxtTgtRecords += iCreatedNodes;
	}

	if(out){                  
		size_t end_pos_saved=ftell(out);
		adopt_out.Close();
		konga::ftruncate(targetHFileName, end_pos_saved);
	}

	EvaluateJMSGlobalDataElement();
}

std::string TreeMapper::createSourceInstanceTag(CROM*lastNode, CROM*targetNode, CRData*sourceDataNode, int instanceNumber)
{
	return std::string();
}

namespace {
	                                                                         
	                                                                               
	                                                                   
	bool HandleEmptyDatabaseTarget(const string& stTargetFile, const OperationDataElementHolder& deHolder, string& msg) {
		if( NonEmptyFileExists(stTargetFile) ) return false;

		const string not_def("<nd>");
		string action = konga::GetSystemVariable("etlforge.target.db.no_data_action", &deHolder, not_def);
		if ( action == not_def ) {
			                                                                       
			                                                          
			action = konga::GetOptionAsString("ErrorHandling", ErrorConfiguration::NO_TARGET_DATA_GENERATED, "Warning");
		}

		if ( EqualsIgnoreCase(action, "ok") ) {
			return true;
		}
		else if ( EqualsIgnoreCase(action, "error") ) {
			throw CrException("No target data was generated. If you don't want this to result in an error use the global data element $etlforge.target.db.no_data_action to change this.");
		}
		else {
			msg = "No target data was generated. No data available to write to the database.";
			return true;
		}
	}
}

unsigned long TreeMapper::processInsertTextToDB(const string& stTargetFile)
{
	PROFILING doPrf(this,"ProcessInsertTextToDB");
	unsigned long hr = S_OK;
	if(m_targetType != DbTypeId)return hr;
	const bool bDebugEnabled=getViewLog()->IsDebugLogEnabled();
	try
	{
		string msg;
		if( HandleEmptyDatabaseTarget(stTargetFile, *getOperationDataElementHolder(), msg) ) {
			if ( !msg.empty() ) {
				getViewLog()->LogMessage("%s", msg.c_str());
			}
			return hr;
		}
		
		std::string sPreTargetSql=konga::GetSystemVariable(DB_TARGET_PRE_SQL, getOperationDataElementHolder(), "");

		switch(m_targetDriverType){
			case konga::ODBC:
				{
					bool bIsTarget=true;

					bool bErroroccured=false;
					int iSuccessOperations=0;
					CROM* targetDTDRoot = m_targetDTDParser->FindRoot();

					DbWriter db_writer(this, bIsTarget);
					if(bDebugEnabled){
						db_writer.setDebugLog(getOdbcTargetLogFileName());
					}
					db_writer.SetDoCommit(false);	                                                                                            
					db_writer.TextDoc2DB(stTargetFile, targetDTDRoot, bErroroccured, iSuccessOperations, sPreTargetSql);
					if(!bErroroccured)db_writer.SetDoCommit(true);
				}
				break;
			case konga::JDBC:
				{
					const string sDebugFile( bDebugEnabled ? getJdbcTargetLogFileName() : "" );
					CROM* pDtdRoot=m_targetDTDParser->FindRoot();
					const string& stJtrFileName=getTempTargetJtrFileName();
					if(!konga::FileExists(stJtrFileName)){
						CromToJtrWriter writer;
						writer.createJtr(CROMHeader(pDtdRoot), stJtrFileName);
					}
					const konga::UserInfo& userInfo(getOperationContext()->getCurrentUser());
					const bool bTransaction = getDbTargetTransactionFlag(*getOperationDataElementHolder());

					if ( m_pTargetDbConnectionInfo == 0 ) {
						throw InternalErrorException("The target database connection information has not been initialized. " \
							"This has to be done before a JDBC target can be written to.");
					}

					bool bIncludeNulls = GetSystemVariableAsBool("etlforge.target.db.include_null_in_sql_statement", getOperationDataElementHolder(), false);
					string sWarnings;
					WriteJdbcTarget(userInfo, m_TargetID, *m_pTargetDbConnectionInfo,
						m_iMode, bTransaction, sPreTargetSql, bIncludeNulls, stJtrFileName, stTargetFile, getAuxTargetStructFileName(), sDebugFile, sWarnings);
					if( !sWarnings.empty() ) {
						                                               
						getViewLog()->LogMessage("%s", msg.c_str());
					}
				}
				break;
			default:
				throw InternalErrorException("Unknown Target Driver Type: " + konga::toString(m_targetDriverType));
		}
	}
	catch(const CrException& e)
	{
		string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		hr = E_FAIL;
	}
	return hr;
}

unsigned long TreeMapper::ProcessSourceUpdate(){
	                                                                                        
	                                                              
	unsigned long hr = S_OK;
	if(!IsSourceUpdateTransformation())return hr;
	PROFILING doPrf(this,"ProcessSourceUpdate");
	closeDbLookup();
	bool bIsTarget=false;

	bool bErroroccured=false;
	int iSuccessOperations=0;
	string source_update=getSourceUpdateDataFileName();
	CROM* sourceDTDRoot = m_sourceDTDParser->FindRoot();
	try
	{
		switch(m_sourceDriverType){
			case konga::ODBC:
				{
					DbWriter db_writer(this, bIsTarget);
					db_writer.SetDoCommit(false);
					db_writer.TextDoc2DB(source_update.c_str(), sourceDTDRoot, bErroroccured, iSuccessOperations, "");
					if(!bErroroccured)db_writer.SetDoCommit(true);
				}
				break;
			case konga::JDBC:
				{
					const bool bWriteLog=getViewLog()->IsDebugLogEnabled();
					CROM* pDtdRoot=m_sourceDTDParser->FindRoot();
					const string stJtrFileName=getTempSourceJtrFileName();
					CromToJtrWriter writer;
					writer.createJtr(CROMHeader(pDtdRoot), stJtrFileName);
					const konga::UserInfo& userInfo(getOperationContext()->getCurrentUser());
					const bool bTransaction = getDbTargetTransactionFlag(*getOperationDataElementHolder());

					if ( m_pSourceDbConnectionInfo == 0 ) {
						throw InternalErrorException("The source database connection information has not been initialized. " \
							"This has to be done before a JDBC target can be written to.");
					}
					const string debug_file_name( bWriteLog ? getJdbcTargetLogFileName() + "-src-update" : "" );

					string sWarnings;
					string sPreTargetSql("");                                    
					bool bIncludeNulls = false;                                     
					WriteJdbcTarget(userInfo, m_SourceID, *m_pSourceDbConnectionInfo, MODE_SOURCE_UPDATE, bTransaction, 
						sPreTargetSql, bIncludeNulls, stJtrFileName, source_update.c_str(), getAuxSourceStructFileName(), debug_file_name, sWarnings);
					if( !sWarnings.empty() ) {
						getViewLog()->LogMessage("%s", sWarnings.c_str());
					}
				}
				break;
			default:
				throw InternalErrorException("Unknown Target Driver Type: " + konga::toString(m_targetDriverType));
		}
	}
	catch(const CrException& e)
	{
		string stErrMsg = e.GetErrorString();
		AppendErrorMessage(stErrMsg.c_str());
		hr = E_FAIL;
	}
	return hr;
}

bool getDbTargetTransactionFlag(const konga::OperationDataElementHolder& deHolder) {
	                                                                        
	const bool bDefault = konga::GetOptionAsBoolean("TreeMapper", "DbTransaction", true);
	return konga::GetSystemVariableAsBool("etlforge.target.db.transaction", &deHolder, bDefault);
}
