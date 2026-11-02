/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>
#include <string>
#include <cassert>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>

#include <boost/scoped_array.hpp>

#include <konga/Utils/AutoCleanup.h>	                       
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>
#include <konga/Utils/Settings.h>		                      

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>
#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/Transformation_DefinitionsTab.h>
#include <konga/Db/TranDb/TargetsTab.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/DataElement/DataElement.h>

#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/WDIPathFinder.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/ItemProperties/Definitions.h>
#include <konga/ItemProperties/ItemProperties.h>

#include <konga/OperationObjects/Transformation.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include <konga/TransformationUtils/Definitions.h>
#include <konga/TransformationUtils/DbLocationConnectionInfo.h>
#include <konga/TransformationUtils/TranDBUtils.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "Normalization.h"
#include "Table.h"
#include "TextDocument.h"
#include "XmlDocument.h"
#include "OdbcSource.h"
#include "KongaProtocolRpcInterface.h"
#include "DataElementUtils.h"
#include "CROMData.h"

using namespace std;
using namespace konga;
using namespace TranDb;

namespace konga {
	                                                                      
	                                                 
	                                                                    
	                                                   
	                                            
	void ReplaceInConnectString(KString sParam, const KString& sNewValue, KString& sConnectString);
}

void konga::ReplaceInConnectString(KString sParam, const KString& sNewValue, KString& sConnectString)
{
	                                                    
	if (sConnectString.IsEmpty())
		return;

	KString sOldValue;
	                           
	KString sTempConnectString = sConnectString;	
	sTempConnectString.MakeUpper();

	sParam += "=";
	int iParamPos = sTempConnectString.Find(sParam);

	if (-1 == iParamPos) {
		if (sParam.CompareNoCase("DATABASE=") == 0)
			sParam = "DBQ=";                                                                    
		else if (sParam.CompareNoCase("SERVER=") == 0)
			sParam = "SRVR=";                                                                        

		                                  
		iParamPos = sTempConnectString.Find(sParam);
	}

	if (-1 != iParamPos)
	{
		                                                          
		int iSemiColon = sConnectString.Find(';', iParamPos);
		int iStartPos = iParamPos + (int)sParam.GetLength();
		                                    
		sParam = sConnectString.Mid(iParamPos, sParam.GetLength());

		if (-1 != iSemiColon)
		{
			sOldValue = sConnectString.Mid(iStartPos, iSemiColon - iStartPos);
		}
		else
		{
			sOldValue = sConnectString.Right(sConnectString.GetLength() - iStartPos);
		}
		sConnectString.Replace(sParam + sOldValue, sParam + sNewValue);
	}
	else
	{
		if(sConnectString.Right(1) != ";")sConnectString += ";";
		sConnectString += sParam;
		sConnectString += sNewValue;
		sConnectString += ";";
	}
}

#define CRYPTO_ON 1

unsigned long TreeMapper::GetTranDBInfo(DbConnection* pTranDbConn)
{
	PROFILING doPrf(this,"GetTranDBInfo");
	unsigned long hr = S_OK;
	KString sLastOp("Connection to CrTranDb...");

	SmartDbPtr<DbConnection> adopt_private_dbConnection(0);
	if ( pTranDbConn == 0 ) {
		adopt_private_dbConnection.Attach (
			pTranDbConn = konga::GetBackEndDbConnection("TranDb")
			);
	}

	try
	{
		                                                                         
		UserAccess userAccess(getOperationContext()->getCurrentUser(), *pTranDbConn);
		                                                                                                             
		if( m_TransformationID != Transformation::PASS_THROUGH_TRANSFORMATION_ID &&  !userAccess.HasReadPermission(m_TransformationID, TransformationEntityId)) {
			AppendErrorMessage("The current user does not have read permission for this transformation.");
			return E_FAIL;
		}

        KString sSource_ID, sTarget_ID;
		hr = ReadTransformationsTabInfo(pTranDbConn, sLastOp, sSource_ID, sTarget_ID);
		if( FAILED(hr) )
			return hr;

		                                          
		if ( m_SourceID > 0 ) {
			if ( !userAccess.HasReadPermission(m_SourceID, SourceEntityId) ) {
				throw CrException("The current user does not have read permission for the source used to define the source structure.");
			}
		}

		if ( m_TargetID > 0 ) {
			if ( !userAccess.HasWritePermission(m_TargetID, TargetEntityId) ) {
				throw CrException("The current user does not have write permission for the target used to define the target structure.");
			}
		}
		
		hr = ReadTransformDefinitionTabInfo(pTranDbConn, sLastOp);
		if( FAILED(hr) )
			return hr;

		                           
		ItemProperties ip(pTranDbConn);
		m_xproperties.Clear();
		ip.GetItemProperties(TransformationEntityId, m_TransformationID, m_xproperties);
		if( !m_bWriteLog ) {
			                                               
			m_bWriteLog=m_xproperties.GetBool(TRANSFORMATION_PROPERTY_WRITE_LOG, false);
		}
		m_bDoSourceXmlValidation = m_xproperties.GetBool(TRANSFORMATION_DO_SOURCE_XML_VALIDATION, true);
		m_bDoTargetXmlValidation = m_xproperties.GetBool(TRANSFORMATION_DO_TARGET_XML_VALIDATION, true);
		m_sSchemaLocation = m_xproperties.GetString(TRANSFORMATION_SCHEMA_LOCATION, "");
		m_bTruncateDbCharTargets = m_xproperties.GetBool(TRANSFORMATION_TRUNCATE_DB_CHAR_TARGETS, false);
		m_SourceBatchNodePath = m_xproperties.GetString(TRANSFORMATION_SOURCE_BATCH_NODE_PATH, "");
		m_DoBatchTarget = m_xproperties.GetBool(TRANSFORMATION_DO_BATCH_TARGET, false);
		m_SourceBatchSize = m_xproperties.GetInt(TRANSFORMATION_SOURCE_BATCH_SIZE, 0);
		m_bNoSourceInMapping = m_xproperties.GetBool(NO_SOURCE_DATA_USED_IN_MAPPING, false);
			
		if(m_targetType==RdbTypeId || m_targetType==DbTypeId ||  m_targetType==PSQueryTypeId || m_targetType==PSMsgAgentTypeId  || m_targetType==WorkFlowTypeId )
		{
			hr = ReadTargetsTabInfo(pTranDbConn, sTarget_ID, sLastOp);
			if (FAILED(hr)) {
				return hr;
			}
		}
		if (m_sourceType == DbTypeId || m_sourceType == LdapTypeId) {
			DbLocationConnectionInfoFactory f;
			m_sourceDbConnectionInfo = f.fromSourcesTab(pTranDbConn, m_SourceID);
		}
		if (m_targetType == DbTypeId || m_targetType == LdapTypeId) {
			DbLocationConnectionInfoFactory f;
			m_targetDbConnectionInfo = f.fromTargetsTab(pTranDbConn, m_TargetID);
		}
		if (m_sourceType == LdapTypeId) {
			m_pLdapFilterCollection= new konga::LdapFilterCollection();
			konga::getLdapFiltersForTransformation(m_TransformationID,*m_pLdapFilterCollection, *pTranDbConn);
		}

		if( (GetTargetType()==TextTypeId || GetTargetType()==ComplexTextTypeId) && m_target_text_doc_id > 0){
			ItemProperties textDocumentProperties(pTranDbConn);
			string qm;
			textDocumentProperties.GetItemProperty(DocumentEntityId, m_target_text_doc_id, TEXT_DOCUMENT_QUALIFIER_MODE, qm);
			m_bQualifierRequired = EqualsIgnoreCase(qm, "ALWAYS");
			textDocumentProperties.GetItemProperty(DocumentEntityId, m_target_text_doc_id, TEXT_DOCUMENT_EDIFACT_MODE, qm);
			m_bEdifact = EqualsIgnoreCase(qm, "1");
		}
	}
	catch(const CrFatalException& e)
	{
		throw e;
	}
	return hr;
}

namespace {
	                                                                                
	string ReplaceDataElementsInWhereClause(const string& stSql,
		TreeMapper* pTranEngine)
	{
		if ( pTranEngine == 0 )
			return stSql;

		string stSqlUpper(stSql);
		konga::ToUpper(stSqlUpper);
		string::size_type ipos = stSqlUpper.find("WHERE");
		if ( ipos == string::npos )
			return stSql;

		string stRetVal(stSql.substr(0, ipos+5));
		stRetVal += pTranEngine->replaceGlobalDataElementsInString(
			stSql.substr(ipos+5)
			);
		return stRetVal;
	}
}

unsigned long TreeMapper::ReadTransformationsTabInfo(DbConnection* pTranDbConn, KString& sLastOp, KString&sSource_ID, KString&sTarget_ID)
{
	try
	{
		sLastOp = "Opening table TransformationsTab ";
        SmartDbPtr<TRANSFORMATIONSTAB_V> tf( TRANSFORMATIONSTAB_V::MakeInstance(pTranDbConn) );
		string stTransTabFilter = "WHERE Transformation_ID = " + toString(m_TransformationID);
		tf->Select(stTransTabFilter);
		if( tf->Next() )
		{
			if(tf->IsSource_Type_IDNull()||
				tf->IsTarget_Type_IDNull())throw CrException("Either Source_Type_ID or Target_Type_ID was undefined for the TransformationId="+konga::toString(m_TransformationID));
			m_sourceType = tf->GetSource_Type_ID();
			m_targetType = tf->GetTarget_Type_ID();
			m_UpdateFields = tf->IsUpdateFieldsNull()? "": tf->GetUpdateFields();
			m_Target_DTD_URL = tf->IsTarget_DTD_URLNull()? "": tf->GetTarget_DTD_URL();
			m_sTransformationName = tf->IsTransformation_NameNull()? "": tf->GetTransformation_Name();
			setName(string("Transformation: \"")+(const char*)m_sTransformationName+"\"");
			m_sourceDTD_Root = tf->IsSourceDTD_RootNull()? "": tf->GetSourceDTD_Root();
			m_targetDTD_Root = tf->IsTargetDTD_RootNull()? "": tf->GetTargetDTD_Root();
			m_Function_ID = tf->IsFunction_IDNull() ? 0 : tf->GetFunction_ID();
			m_source_text_doc_id = 0;
			m_target_text_doc_id = 0;
			if(!tf->IsSource_Text_DocNull())m_source_text_doc_id=atoi(tf->GetSource_Text_Doc().c_str());
			if(!tf->IsTarget_Text_DocNull())m_target_text_doc_id=atoi(tf->GetTarget_Text_Doc().c_str());

			if(!tf->IsSource_SourceIDNull()){
				sSource_ID = tf->GetSource_SourceID();
				m_SourceID = atoi(sSource_ID);
			} else {
				sSource_ID = "";
			}
			if(!tf->IsTarget_TargetIDNull()){
				sTarget_ID = tf->IsTarget_TargetIDNull()? "0": tf->GetTarget_TargetID();
				m_TargetID = atoi(sTarget_ID);
			} else {
				sTarget_ID = "";
			}

			if( tf->IsIsValidNull() ) {
				                                                                           
				m_bIsTransformationValid = true;
			}
			else {
				if( tf->GetIsValid() == -1 ) {
					                                       
					                                                                   
					m_bIsTransformationValid = true;
				}
				else {
					m_bIsTransformationValid = tf->GetIsValid() != 0;
				}
			}
	
			const char*SrcTgtTypes[] = { "Text", 
										 "EDI", 
										 "Database", 
										 "XML", 
										 "RemoteDatabase", 
										 "PeopleSoft Message Agent", 
										 "WebWorkFlow",
										 "SAP", 
										 "PeopleSoft Query", 
										 "Complex Text", 
										 "RosettaNet",
										 "WebServiceInputTypeId",
										 "No Source",
										 "WebServiceOutputTypeId",
										 "UnknownTypeId",
										 "LdapTypeId"};

			string stNatureOfSource=tf->IsNatureOfSourceNull()?"":tf->GetNatureOfSource();
			if( (tf->GetSource_Type_ID() - 1) < 0 || (tf->GetSource_Type_ID() > LdapTypeId) )
				getViewLog()->DebugString("Unknown source type number = %d\n", tf->GetSource_Type_ID());
			else
				getViewLog()->DebugString("\nTransformation Name = %s\n\nSource Type = %s\nSource SQL = %s\nNature of Source = %s\n",
				tf->GetTransformation_Name().c_str(), SrcTgtTypes[tf->GetSource_Type_ID() - 1], tf->IsSource_SQLNull() ? "<Source_SQL is NULL>" : tf->GetSource_SQL().c_str(), stNatureOfSource.c_str());
			if( (tf->GetTarget_Type_ID() - 1) < 0 || (tf->GetTarget_Type_ID() > LdapTypeId) )
				getViewLog()->DebugString("Unknown target type number = %d\n", tf->GetTarget_Type_ID());
			else
				getViewLog()->DebugString("\nTarget Type = %s\nTarget SQL = %s\nNature of Target = %s\n\n", SrcTgtTypes[tf->GetTarget_Type_ID() - 1], tf->IsTarget_SQLNull() ? "<Target_SQL is NULL>" : tf->GetTarget_SQL().c_str(), tf->IsNatureOfTargetNull()?"":tf->GetNatureOfTarget().c_str());
			getViewLog()->DebugString("Update fields = '%s'\n", (const char*)m_UpdateFields);
			                                      
			m_iMode = tf->GetInsert_Mode();
	
			                                                                                                 
			if (!(sSource_ID.IsEmpty()||sSource_ID=="0") && !tf->IsSource_Type_IDNull() && tf->GetSource_Type_ID() != RosettanetTypeId)
			{
				unsigned long hr = ReadSourcesTabInfo(pTranDbConn, sLastOp);
				if( FAILED(hr) )
					return hr;
			}
			else
			{
				m_sSourceDsn = tf->IsSource_DSNNull()?"":tf->GetSource_DSN();

			}
	
			                                                                                             
			                                                                      
			if (m_targetType == PSMsgAgentTypeId )
			{
				KString TgtTables;
				TgtTables = tf->IsTarget_TablesNull()?"":tf->GetTarget_Tables();
				int FirstComma = TgtTables.Find(',');
				if(FirstComma<0)FirstComma=(int)TgtTables.GetLength();
			}
	
			m_sSourceSQL = ReplaceDataElementsInWhereClause(tf->IsSource_SQLNull() ? "" : tf->GetSource_SQL(), this);
			m_sSourceWhere = tf->IsSource_WhereNull()? "" : tf->GetSource_Where().c_str();

			KString sNatureOfSource(stNatureOfSource.c_str());
			m_NatureOfSource = sNatureOfSource.SpanExcluding(",");
			int iPos = sNatureOfSource.Find(',');
			if(iPos != -1 && iPos < ((int)sNatureOfSource.GetLength()-1))
			{
				char c = ((const char*)sNatureOfSource)[iPos+1];
			}
	
			                                                                       
			if(m_sourceType==TextTypeId || m_sourceType == PSQueryTypeId || m_sourceType==DbTypeId || m_sourceType==RdbTypeId){
                                                                          
            }
			else if(m_sourceType==XmlTypeId || m_sourceType==WebServiceInputTypeId || m_sourceType==WebServiceOutputTypeId || m_sourceType==EdiTypeId)
                m_NatureOfSource="Tree";
			else if(IsNoSource())
                m_NatureOfSource="NoSource";
	
			if(m_targetType==TextTypeId ||m_targetType==DbTypeId ||m_targetType==RdbTypeId || m_targetType == WorkFlowTypeId)
			{
				m_NatureOfTarget=tf->IsNatureOfTargetNull()?"":tf->GetNatureOfTarget();
			}
			else if(m_targetType==XmlTypeId || m_targetType==EdiTypeId || m_targetType==WebServiceInputTypeId || m_targetType==WebServiceOutputTypeId)m_NatureOfTarget="Tree";
	
			if( m_sourceType == WebServiceInputTypeId || m_targetType == WebServiceOutputTypeId )
			{
				                                                                                      
				                                                                                   
				                                                                              
				                                                                  
				  
				                                                                                
				                                                                                           
				                                        
				if( !m_bHostedWebServiceOperation )
					m_bHostedWebServiceOperation = (m_sourceType == WebServiceInputTypeId);
				TreeMapper::TransformationPoint transPoint = (m_sourceType == WebServiceInputTypeId) ?
																TreeMapper::Source : TreeMapper::Target;
				unsigned long hr = GetHostedWebServiceInfo(transPoint, pTranDbConn);
				if( FAILED(hr) )
				{
					return hr;
				}
			}
			
	
			m_inputStructFileName="";
			if(!tf->IsSource_XMLNull()){
				m_inputStructFileName=tf->GetSource_XML();
				if(!m_inputStructFileName.IsEmpty())
					m_inputStructFileName=konga::getAbsolutePath((const char*)m_inputStructFileName);
			}
			m_outputStructFileName="";
			if(!tf->IsTarget_XMLNull()){
				m_outputStructFileName=tf->GetTarget_XML();
				if(!m_outputStructFileName.IsEmpty())
					m_outputStructFileName=konga::getAbsolutePath((const char*)m_outputStructFileName);
			}
			if(m_targetType == SAPTypeId)
			{
				                                       
			}
		}
		else
		{
			AppendErrorMessage(sLastOp+"\nThere is no transformation defined with ID " + toString(m_TransformationID));
			return E_FAIL;
		}
	}
	catch( DbException& e )
	{
		AppendErrorMessage(sLastOp+"\nDbException: "+e.GetErrorString());
		return E_FAIL;
	}
	return S_OK;
}

unsigned long TreeMapper::ReadSourcesTabInfo(DbConnection* pTranDbConn, KString& sLastOp)
{
	assert(pTranDbConn);
	PROFILING doPrf(this,"ReadSourcesTabInfo");
	sLastOp = "Opening SourcesTab ";
	try
	{
		string sErr;
		if ( m_pSourceDbConnectionInfo ) delete m_pSourceDbConnectionInfo;
		m_pSourceDbConnectionInfo = new DbConnectionInfoStruct();

		const unsigned long hr=getSourceConnectionInfo(*pTranDbConn, getOperationContext()->getCurrentUser(), m_SourceID,
			getOperationDataElementHolder(), *m_pSourceDbConnectionInfo, sErr);
		if(FAILED(hr)){
			AppendErrorMessage(sLastOp);
			AppendErrorMessage(sErr);
			return hr;
		}

		                                                                 
		DbConnectionInfoStruct& dbConnInfo = *m_pSourceDbConnectionInfo;
		m_sSourceConnectStr = dbConnInfo.connectionString;
		m_dwSrcTimeout = dbConnInfo.timeOutSeconds;
		m_lSourceTxnIsolationLevel = dbConnInfo.txnIsolationLevel;
		m_sSourceBeginName = dbConnInfo.beginQuote;
		m_sSourceEndName = dbConnInfo.endQuote;
		m_sourceDriverType = dbConnInfo.driverType;
		  

		defineSourceDbDriverDependentStr(m_sSourceConnectStr);
		return hr;
	}
	catch( const CrException& e )
	{
		AppendErrorMessage(sLastOp);
		AppendErrorMessage(e.GetErrorString());
		return E_FAIL;
	}
}

unsigned long TreeMapper::ReadTargetsTabInfo(DbConnection* pTranDbConn, const KString& sTarget_ID, KString& sLastOp)
{
	assert(pTranDbConn);
	PROFILING doPrf(this,"ReadTargetsTabInfo");
	const int targetID=atoi((const char*)sTarget_ID);
	sLastOp = "Opening TargetsTab ";
	try
	{
		string sErr;
		if ( m_pTargetDbConnectionInfo ) delete m_pTargetDbConnectionInfo;
		m_pTargetDbConnectionInfo = new DbConnectionInfoStruct();

		const unsigned long hr=getTargetConnectionInfo(*pTranDbConn, getOperationContext()->getCurrentUser(), targetID,
			getOperationDataElementHolder(), *m_pTargetDbConnectionInfo, sErr);
		if(FAILED(hr)){
			AppendErrorMessage(sLastOp+"\nFailed to read target with Target_ID = " + sTarget_ID + "\nError:" + sErr);
			return hr;
		}

		                                         
		DbConnectionInfoStruct& dbConnInfo = *m_pTargetDbConnectionInfo;
		m_sTargetConnectStr = dbConnInfo.connectionString;
		m_dwTgtTimeout = dbConnInfo.timeOutSeconds;
		m_lTargetTxnIsolationLevel = dbConnInfo.txnIsolationLevel;
		m_sTargetBeginName = dbConnInfo.beginQuote;
		m_sTargetEndName = dbConnInfo.endQuote;
		m_targetDriverType = dbConnInfo.driverType;
		  

		defineTargetDbDriverDependentStr(m_sTargetConnectStr);
		return hr;
	}
	catch( const CrException& e )
	{
		AppendErrorMessage(sLastOp);
		AppendErrorMessage(e.GetErrorString());
		return E_FAIL;
	}
}

namespace {
	KString CheckForAlias(const KString& name, const map<KString, KString>& aliases) {
		map<KString, KString>::const_iterator it = aliases.find(name);
		if (it != aliases.end()) {
			return it->second;
		}
		return name;
	}
}



unsigned long TreeMapper::ReadTransformDefinitionTabInfo(DbConnection* pTranDbConn, KString& sLastOp)
{
	try
	{
		sLastOp = "Opening Transformation_DefinitionsTab ";
        SmartDbPtr<Transformation_DefinitionsTab> tfd( Transformation_DefinitionsTab::MakeInstance(pTranDbConn) );
		const string stTransDefTabFilter("WHERE Transformation_ID = " + toString(m_TransformationID));
		tfd->Select(stTransDefTabFilter);

		const bool bIsDebugLogEnabled = getViewLog()->IsDebugLogEnabled();
		if(bIsDebugLogEnabled)getViewLog()->DebugString("ReadTransformDefinitionTabInfo");

		m_maplist.clear();
		m_bMappingPresent = false;
		while( tfd->Next() ) {
			m_maplist.insert(STRMAP_T::value_type(
				KString(tfd->GetTarget_Field_ID().c_str()), KString(tfd->GetTransformation().c_str())
				));
			if(bIsDebugLogEnabled ) {
				KString tmp;
				tmp.Format("{%s;%s}", tfd->GetTarget_Field_ID().c_str(), tfd->GetTransformation().c_str());
				getViewLog()->DebugString("%s",(const char*)tmp);
			}
		}
		m_bMappingPresent = !m_maplist.empty();
	}
	catch( const DbException& e )
	{
		AppendErrorMessage(sLastOp);
		AppendErrorMessage(e.GetErrorString());
		return E_FAIL;
	}

	return S_OK;
}

unsigned long TreeMapper::ReadODBCSource(const KString& sInputFileName, const char* adHocFilter)
{
	PROFILING doPrf(this, "ReadODBCSource");
	unsigned long hr = S_OK;
	KString LastOperation;

	try
	{
		                                                                 
		LastOperation = "TreeMapper::LoadDbSrc - Marking source for mapped fields.";
		if (m_sSourceConnectStr.IsEmpty())
		{
  			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_LOADDBSRC_SOURCE_DSN_NOT_SPECIFIED);
			AppendErrorMessage(getErrorLog()->GetLastErr());
			return hr;
		}

		CROM* pDtdRoot=m_sourceDTDParser->FindRoot();
		LastOperation = "Creating DBTreeSource to read data from tree source database ";
		getViewLog()->DebugString("%s\n", (const char*)LastOperation);
		const long long nRecords = OdbcSource::createSourceTextDocumentFromODBC(pDtdRoot, (const char*)sInputFileName, adHocFilter);
		m_bSourceIsEmpty = (nRecords==0);
		LastOperation = "Reading data from source database - OdbcSource::createSourceTextDocumentFromODBC";
		getViewLog()->DebugString("%s\n", (const char*)LastOperation);
		LastOperation.Empty();
	}
	catch(const DbException& e )
	{
		const string stSourceDSNNoPwd( konga::ReplacePassword((const char*)m_sSourceConnectStr, "<removed>") );
  		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_LOADDBSRC_DB_EXCEPTION1,
			stSourceDSNNoPwd.c_str(), e.GetErrorString().c_str(), e.GetStateNativeOrigin().c_str());
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}
	catch( const KString& strErr )
	{
  		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_LOADDBSRC_EXCEPTION_MESSAGE, (const char*)strErr);
		m_lastErr=getErrorLog()->GetLastErr();
		return hr;
	}

	return hr;
}


void TreeMapper::CreateXmlSourceTree_JbParser(const string& stSourceXMLFileName)
{
	PROFILING doPrf(this,"CreateXmlSourceTree_JbParser");
	getViewLog()->DebugString("CreateXmlSourceTree_JbParser() begin. sourceXmlFileName=\"%s\"\n", stSourceXMLFileName.c_str());
	                        
	                            
	   
	                           
	                                          
	                                                          

	                                                                                                                                 
	    
	                                 
	                                                               
	                                                                                              
	                                                             
	                                              

	                                                                          
	                                       
	     
	                               
	                                                
	                                                                                                                        
	         
	                                                                                                                                                   
	                                                                               
	                                                   
	     

	                                                
	    
	                                
	   
	if(m_pXmlDocument)delete m_pXmlDocument;
	m_pXmlDocument=new XmlDocument(stSourceXMLFileName, m_sourceDTDParser, m_SourceBatchNodePath, m_SourceBatchSize);

	if(m_SourceBatchNodePath != "" || m_SourceBatchSize>0){
		getViewLog()->DebugString("perform batch transformation batch_size=%d, batch_node_path=\"%s\"\n", m_SourceBatchSize, m_SourceBatchNodePath.c_str()); 
	}
	{
		m_pXmlDocument->createDataTree();
		m_pSourceDataRoot=m_pXmlDocument->getDataRoot();
	}

	if(getViewLog()->IsDebugLogEnabled())
	{
    	KString WDIDebugPath;
		if(WDIPathFinder::GetWDIDebugPath(WDIDebugPath) && m_bWriteLog){
			WDIDebugPath+=getTransformationName_()+"_SrcXmlDoc_"+konga::GetGUIDString().substr(0,8)+".log";
    		FILE* pDebug = fopen(WDIDebugPath, "w+");
    		if ( pDebug )
			{
				m_pXmlDocument->getDataRoot()->writeDebugDataTree(pDebug, "");
    			fclose(pDebug);
			}
		}
	}
}


void TreeMapper::CreateXmlSourceTree(const std::string& stSourceXMLFileName)
{
	PROFILING doPrf(this,"CreateXmlSourceTree");
	getViewLog()->DebugString("CreateXmlSourceTree() begin. sourceXmlFileName=\"%s\"\n", stSourceXMLFileName.c_str());

	if(IsNoSource() && !isTestOperation())return;

	m_sourceXmlParser.createDataTree(m_sourceDTDParser->m_ElementType_map, m_sourceDTDParser->m_Element_map);
}

long long TreeMapper::CreateNonXmlSourceTree(const string& stInputDataFileName)
{
	long long nRecords = 0;
	PROFILING doPrf(this,"CreateNonXmlSourceTree");
	getViewLog()->DebugString("CreateNonXmlSourceTree() begin. srcDataFile=\"%s\"\n", stInputDataFileName.c_str());
	if(m_sourceType==NoSourceTypeId || (m_bNoSourceInMapping && !isTestOperation())){
		get_SourceDataRoot();
	}
	else {
		if(m_sourceType==LdapTypeId && konga::FileExists(stInputDataFileName)==false){
			dumpLdapSourceToFile(stInputDataFileName);
		}
		if ( m_pTextDocument ) delete m_pTextDocument;
		m_pTextDocument=new TextDocument(stInputDataFileName, m_sourceDTDParser->m_root, m_SourceBatchNodePath, m_SourceBatchSize, isEDIFACT(), false);
		m_pSourceDataRoot=m_pTextDocument->createDataTree(nRecords);
	}
	if ( m_sourceType != NoSourceTypeId && konga::FileExists(stInputDataFileName) ) {
		long lErr = 0;
		m_lTxtSrcSize = konga::GetFileSize(stInputDataFileName, lErr);
	}

	return nRecords;
}

namespace {
	KString transform_formula(const char*map_str, TreeMapper*p)
	{
		const int open_tag_len=(int)strlen(TRANS), close_tag_len=(int)strlen(END_TRANS);
		KString tmp;
		const char*s0=map_str,*s1,*s2;
		while((s1=strstr(s0,TRANS))){
			int end_tran_pos=find_END_KEY_position(s1, 0);
			if(end_tran_pos<0){                      
				break;
			}
			s2=s1+end_tran_pos;
			if(s0<s1){
				if(!tmp.IsEmpty())tmp+="+";
				tmp="'"+KString(s0,s1-s0)+"'";
			}
			if(!tmp.IsEmpty())tmp+="+";
			s1+=open_tag_len;
			KString formula(s1,s2-s1);
			CTransform tr(p, (long)0);
			int err;
			konga::Token**ts=tr.TokenizeToRPN(formula,err);
			AutoArrayOfPointerDelete<konga::Token> adopt_ts(ts);
			if(err==0 && ts && ts[0]!=0){                                                             
				tmp+=formula;
				s0=s2+close_tag_len;
			}
			else if(err==NO_TOKENS){
				tmp="''";
				s0=s2+close_tag_len;
			}
			else {
				break;
			}
		}
		if(*s0!=0){
			if(!tmp.IsEmpty())tmp+="+";
			tmp+="'"+KString(s0)+"'";
		}
		return tmp;
	}
	const char*fcn_str[]={
		"Int0",
		"Long0",
		"Float0",
		"Double0",
		"Date0",
		"String0",
		"Bool0",
		"Timespan0",
		"Bool0"};

	const char*fcnStart="Eval(";
	const char*fcnFormatStart="Eval3(";

}

