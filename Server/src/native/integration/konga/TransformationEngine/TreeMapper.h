/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef __TREEMAPPER_H_
#define __TREEMAPPER_H_

#include <vector>
#include <string>
#include <map>
#include <set>

#include <konga/Utils/KString.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/KeyValues.h>

#include <konga/Ldap/LdapFilter.h>

#include <konga/Xml/DOMNodePtr.h>
#include <konga/Soap/SoapFault.h>
#include <konga/WebService/WsdlConstants.h>

#include <konga/FileTransfer/HttpOptions.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/TranDbUtils/DatabaseDriverType.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/TransformationUtils/DbLocationConnectionInfo.h>
#include <konga/TransformationUtils/TransformationDataHolder.h>

#include "resource.h"                      
#include "TransformationEngine.h"
#include "StreamingTransform.h"
#include "TxtParser.h"
#include "CRXmlParser.h"
#include "TransformationChain.h"

#include "TransformationEngineApi.h"	                           

           
#define DBTARGET_DELIMITER ","
#define DB_DELIMITER_CHAR ','
#define DBTARGET_NULL	   "<null>"

#define TRANS "<trans>"
#define END_TRANS "</trans>"
#define SOURCE_TARGET_ID_UNKNOWN -2
static const KString c_userDefinedFunctionTag(USER_DEFINED_FUNCTION);
static const KString c_PostProcessIdentity(POST_PROCESS_IDENTITY);
static const size_t c_length_PostProcessIdentity=c_PostProcessIdentity.GetLength();
static const KString c_PostProcessSequece(POST_PROCESS_SEQUENCE);
static const size_t c_length_PostProcessSequece=c_PostProcessSequece.GetLength();
static const KString c_PostProcessOracleSequece(POST_PROCESS_USERDEFINED_SEQUENCE);
static const size_t c_length_PostProcessOracleSequece=c_PostProcessOracleSequece.GetLength();
static const size_t c_length_precondition=strlen(c_precondition);
static const size_t c_length_userDefinedFunctionTag=c_userDefinedFunctionTag.GetLength();


namespace konga {
    class DbConnection;
	class LdapFilterCollection;
	class Token;
	class TransformationWebServiceInfo;
	struct DbConnectionInfoStruct;
}

class CErrorViewLog;
class CErrorLog;
class CViewLog;
class LdapAttributeMap;
class CRData;
class CTable;

class CTableFilters;
class WebServiceStructure;
class Normalization;
class TextDocument;
class XmlDocument;

class TreeMapper
{
	friend class TransformationEngine;
	friend class Normalization;
	friend class CROM;
public:
	TreeMapper(TransformationEngine*pTransformationEngine, konga::TransformationDataHolder* pTransformationDataHolder, bool bLoadSourceData, bool bTestOperation, bool bTestTransformation, bool bTrace);
	TreeMapper(const TreeMapper& old, konga::TransformationDataHolder*pTransformationDataHolder);
	~TreeMapper();

public:
	std::string getTransformationName() const { return std::string((const char*)m_sTransformationName); }
	std::string getTransformationName30() const;
	std::string getTransformationName_() const ;
	std::string getTransformationName__(const char*tag) const ;

	unsigned long InitTransformation(konga::DbConnection& tranDbConnection, long TransformationID, int& nTransformationStatus, bool bTrace);

	void TreeMapper::validateSource(const std::string& stInputDataFileName, bool m_bUserETLForgeXmlParser);
	void TreeMapper::validateTarget(const std::string& stOutputDataFileName);
	unsigned long doTransformation(const std::string& stInputDataFileName, const std::string& stOutputDataFileName, const std::string& stLinkDataFileName,
		int& nTransformationStatus, konga::TransformationStatistics& statistics);

	void DeleteTargetXmlData();

	unsigned long HandleWebServiceCall(const std::string& sData, std::string& sReturnMessage);

	unsigned long CreateJtrFromMappings(long lTransformationID, const std::string& stSourceJtrFN, const std::string& stTargetJtrFN);

	                                 
	bool IsXmlSource(){return m_XmlSource;}
	bool IsXmlTarget(){return m_XmlTarget;}
	
	void AppendErrorMessage(const KString& csError);
	int GetSourceType() const { return m_sourceType;	}
	int GetTargetType() const {	return m_targetType;	}

	int getDbInsertUpdateMode() const {	return m_iMode;	}

	bool IsSourceEmpty() const {return m_bSourceIsEmpty;	}

	bool IncludeNullInTargetSQLStatement() const;

	enum TransformationPoint	{ Source, Target };

	const konga::SoapFault*	GetSoapFault() const { return m_pSoapFault; }

	                                                                     
	konga::SoapFault* AdoptSoapFault();
	DTDParser* GetSourceDTDParser();
	DTDParser* GetTargetDTDParser();

private:
	TransformationEngine*getTransformationEngine() const {return m_pTransformationEngine;}

	unsigned long creatStructureTrees(bool& bReturn, bool bCreateSourceJtr, bool bCreateTargetJtr);


	void doPreTransformationProcessing();

	                                                                                                        
	unsigned long GetTranDBInfo(konga::DbConnection* pTranDbConnection);
	unsigned long ReadODBCSource(const KString& sInputFileName, const char* adHocFilter);

	unsigned long InsertTextToDb(DTDParser* targetDTD, const char* tgtDataFileName, const char* tgtTableName, int iDbLogicMode, 
								DTDParser* sourceDTD, const char* srcDataFileName, const char* srcTableName, const char* Delimiter);
	void XmlTargetWriter(const char* targetXMLFileName, FILE*lnk);
	void HfTargetWriter(const char*targetHFileName, FILE*lnk);

	unsigned long DoXmlMap(FILE* out, CROM*targetNode, CRData*beginSourceData, int&nTargetDataNodeCreated, FILE*lnk, FILE*update_source_file, bool bXmlIndent);
	unsigned long DoHfMap_Write2Crom(FILE*out, CROM*targetNode, CRData*beginSourceData, int&iCreatedNodes, FILE*lnk, FILE*update_source_file,
		long long& nTotalRecords, konga::Utf8Conv* conv);

	bool isStreamingTransformationApplicable();
	void markMapFlagForSourceTree(CROM*pTargetNode=0);

public:
	                                                            
	void handleEvaluateError(int& err, KString& value, CROM* pTarget, int& iAdd, bool& bIsMappedToNull, bool targetIsDatabase, bool targetIsLdap, CTransform* pTrans, CErrorLog& errorLog);
	bool isTrimSourceWs();
	bool xmlPrettify();


protected:
	STRMAP_T m_maplist;

private:
	TargetWriteMode m_TargetWriteMode;
	bool m_bNumForBool;
	int m_TransformationID;

	int m_SourceID;
	int m_TargetID;
	bool m_XmlSource,m_XmlTarget;
	int m_sourceType,m_targetType;
	KString m_NatureOfSource,m_NatureOfTarget;
	int m_source_text_doc_id, m_target_text_doc_id;

	bool m_bSourceIsEmpty;
	bool m_bMappingPresent;

	bool m_bSourceIsDatabase;
	bool m_bModifyDBSourceSQL;

	                                              
	bool m_bQualifierRequired;
	bool m_bEdifact;

	int m_iMode;

	KString m_sTransformationName;

	long	m_Function_ID;
	long	m_NextTransformation_ID;
	long	m_PreviousTransformation_ID;

	KString m_inputStructFileName, m_inputDataFileName;
	KString m_loadSourceFilter;
	KString m_outputStructFileName, m_outputDataFileName;
	KString m_Target_DTD_URL;

	konga::TransformationWebServiceInfo* m_TransformationWebServiceInfo;
	WebServiceStructure* m_webServiceStructure;

	                                       
	konga::SoapFault*	m_pSoapFault;
	konga::DbLocationConnectionInfo m_sourceDbConnectionInfo;
	konga::DbLocationConnectionInfo m_targetDbConnectionInfo;
	konga::LdapFilterCollection* m_pLdapFilterCollection;
	bool m_bIncludeNullXmlElement, m_bIncludeEmptyXmlElement;

public:
	                                                                                    
	konga::DbConnectionInfoStruct *m_pSourceDbConnectionInfo, *m_pTargetDbConnectionInfo;
	long m_dwSrcTimeout;
	long m_dwTgtTimeout;
	KString m_sSourceConnectStr, m_sTargetConnectStr;
	konga::TransactionIsolationLevel m_lSourceTxnIsolationLevel, m_lTargetTxnIsolationLevel;
	       
	KString m_targetDTD_Root,m_sourceDTD_Root;
public:
	KString m_sSourceDsn;
private:
	bool FillDataElements(CRData*curSourceData, CROM*targetNode);
public:
	bool SourceIsDatabase() const {return m_sourceType==DbTypeId||m_sourceType==RdbTypeId;}
	bool TargetIsDatabase() const {return m_targetType==DbTypeId||m_targetType==RdbTypeId;}
	bool TargetIsLDAP() const { return m_targetType==LdapTypeId; }

	CROM* getSourceFlatFolder();

public: 
	unsigned long LoadSource(const std::string& inputFileName);
	unsigned long TestTransformation(long lTransformationID, const std::string& stInputFileName, const std::string& stOutputFileName, const std::string& sLinkFileName);

	void addWarning(const std::string& warning){ getViewLog()->LogMessage("%s",warning.c_str());}
	bool truncateDbCharTargets() const { return m_bTruncateDbCharTargets; }
	WebServiceStructure* getWebServiceStructure() const { return m_webServiceStructure; }
	const std::set<std::string>* getSourceDeSet(){return &m_source_de_set;}

private:
	CRXmlParser m_sourceXmlParser, m_targetXmlParser;
	DTDParser* m_sourceDTDParser, * m_targetDTDParser;
	std::set<std::string> m_source_de_set;
	CRData* m_pSourceDataRoot;
	KString m_UpdateFields;
	KString m_sSourceSQL,
			m_sSourceWhere;

	bool	m_bTrace;
	bool	m_bForbidStreaming;
	bool	m_bGeneratorDefined;

	long long m_lTxtSrcSize, m_lTxtTgtSize;
	long long m_lTxtSrcRecords, m_lTxtTgtRecords;

	                                                        
	                                           
	bool m_bErrorsInOutput;
	bool m_bIsTransformationValid;


	                            
	bool m_bDoXmlValidationRegistryFlag;
	bool m_bDoSourceXmlValidation, m_bDoTargetXmlValidation;
	std::string m_sSchemaLocation;

	bool m_bTruncateDbCharTargets;
	bool m_bUseOldRuleForLoopOneOfXmlNode;	
private:
	bool m_bTestOperation;
	bool m_bTestTransformation;
	bool m_bLoadSourceData;
	bool m_bStreamingTransformation;
	bool m_bAutoMappingForXmlNil;

private:

	HfLoop*		GetHfLoop(CROMData* pSrcStartNode, KString sEndPath, CROM*targetLoopNode0);
	CROMData*	FindInstance(CROMData* pSrcStartNode, KString sEndPath);
	CROMData*	GetLoopSearchParams(CROMData* pSrcStartNode, KString sEndPath, KString& sSearchPath);
	bool PassPreconditionCheck(CROM*pTargetNode, int& err);
	unsigned long DoStreamingTransform(FILE*lnk, const std::string& stInputFileName);
	
	unsigned long ProcessHfToXml(const char* szSourceFile, const char* szTargetFile, const std::string& bstrDSN);
	unsigned long ProcessNonXmlToXml(       KString& inputDataFileName,        KString& outputDataFileName, const std::string& bstrDSN);
	unsigned long ProcessNonXmlToNonXml(       KString& inputDataFileName, 
								         KString& outputDataFileName, 
								  const std::string& bstrDSN, bool isBulkTransform);
	unsigned long ProcessFlatTextToXml(const char* szSourceFile, const char* szTargetFile, const std::string& bstrDSN);
protected:
	unsigned long processInsertTextToDB(const std::string& stTargetFile);
	unsigned long ProcessSourceUpdate();
private:
	unsigned long ProcessHfToHf(const char* szSourceFile, const char* szTargetFile, const std::string& bstrDSN);
	unsigned long enterMapInfoToStructTree(bool flatToHf,bool flatToFlat, bool& bReturn);
	unsigned long findGenerators();

	void MarkSourceUpdateColumns(DTDParser* pSourceDTDParser);
	
	unsigned long CreateTestTransformTgtFile(const char* targetDataFile, std::vector<int>&instanceIndeces);
	unsigned long CreateTestOpFlatSource(const char* sourceFile, bool bDelInputFile);

	void WriteOperationLogHeader(CViewLog* pOpViewLog, const konga::CrDateTime& SysTime);
	void WriteTransformationLogHeader(const std::string& stInputFileName);

	void HandleNoMappings();
	void InitializeStructureParsers(bool always_from_structure_file, bool bDoSource, bool bDoTarget);
	unsigned long CheckBulkTransform(bool& bIsBulkTransform, bool& bReturn);
	unsigned long PrepareSpecialSourceTypes(bool& bReturn);
	void HandleSpecialTargetTypes();
	unsigned long PrepareDbSource(bool& bReturn);
							 
	unsigned long PrepareHostedWebServiceInput();
	unsigned long ProcessHostedWebServiceOutput();

	unsigned long ProcessLdapTarget(CROMData* pDataRoot);
	void processLdapDelete(const std::string& distinquished_name, int&nSuccess, FILE*out);
	void processLdapAdd(CROMData*entry, const std::string& distinquished_name, const std::string& className, int&nSuccess, FILE*out);
	void processLdapUpdate(CROMData*entry, const std::string& distinquished_name, LdapAttributeMap& attr_map, int& nSuccess, bool bLdapMatchIgnoreCase, FILE*out);
	void ldapUpdateSingleValueAttribute(CROMData*attribute, const std::string& attribute_name, LdapAttributeMap& attr_map, bool bLdapMatchIgnoreCase, FILE*out);
	void ldapUpdateMultiValueAttribute(const std::vector<CROMData*>attributes, const std::string& attribute_name, LdapAttributeMap& attr_map, bool bLdapMatchIgnoreCase, FILE*out);

	unsigned long dumpLdapSourceToFile(const std::string& stSourceDataFileName);

	void SetTransformationResultInfo(konga::TransformationStatistics& statistics, const std::string& stSourceFileName, const clock_t clkStart) const;
	void OnMapByTransformationExit(const unsigned long hr) const;

    unsigned long GetWebServiceInfo(konga::DbConnection* pTranDbInfo, int iFunctionID);
	
	
	unsigned long GetHostedWebServiceInfo(TransformationPoint transPoint, 
									konga::DbConnection* pTranDbConn);

	                                                                                  
	                                           
	unsigned long CallWebService(int iWebServiceId,
						   const std::string& stInputDataFilename,
						   const std::string& stOutputDataFilename);

	void collectSourceDataElements(CROM*dtd);

public:
	                                                                 
	unsigned long	HandleTransformationFunction(int iFunctionID, 
										 const std::string& stInputDataFilename,
										 const std::string& stOutputDataFilename);

private:
	bool IsHostedWebServiceOperation() const { return m_bHostedWebServiceOperation; }
	
	unsigned long ReadSourcesTabInfo(konga::DbConnection* pTranDbConn, KString& sLastOp);
	unsigned long ReadTransFilterTabInfo(konga::DbConnection* pTranDbConn, KString& sLastOp);
	unsigned long ReadTransformDefinitionTabInfo(konga::DbConnection* pTranDbConn, KString& sLastOp);
	unsigned long ReadTargetsTabInfo(konga::DbConnection* pTranDbConn, const KString& sTarget_ID, KString& sLastOp);
	unsigned long ReadTransformationsTabInfo(konga::DbConnection* pTranDbConn, KString& sLastOp, KString&sSource_ID, KString&sTarget_ID);

    bool GetValidateSourceXml();
	bool GetValidateTargetXml();

	void Create_ANY_ELEMENT(CROM*targetNode, KString& err_msg);
	unsigned long AddValueAttributeCROMs(CROM*targetNode, int&iCreatedNodes);
	unsigned long AddValueAttributeCROMs(CROM*targetNode, bool& hasAttributes, bool& hasValue);
	bool AddValueCROMs(CROM* targetNode);

	void EvaluateJMSGlobalDataElement();

public:
	CViewLog*getViewLog() const;
	CErrorLog*getErrorLog() const;
	std::string get_LastError() const;
	konga::DataElement* getGlobalDataElement(const char* tag);
	konga::OperationContext* getOperationContext() const {
		return m_pTransformationDataHolder->getOperationContext();
	}
	konga::OperationDataElementHolder* getOperationDataElementHolder() const {
		return m_pTransformationDataHolder->getOperationDataElementHolder();
	}
	konga::TransformationDataHolder* getTransformationDataHolder() const {
		return m_pTransformationDataHolder;
	}
	std::string replaceGlobalDataElementsInString(const std::string& s) {
		return getOperationDataElementHolder()->replaceOperationDataElementInString(s);
	}
	konga::LdapExecutor* getLdapExecutor() const {
		return m_pTransformationDataHolder->getLdapExecutor();
	}

	            
	void enterProfilingSection(const std::string& sWhere,const std::string& sExtra="");
	void exitProfilingSection(const std::string& Where);
	  
	bool IsSuitableForChunking();
	unsigned long dumpDatabaseSourceToFile(const KString& sInputFileName, const KString& sAdHocFilter);
	void DumpDatabaseSourceDataIfSourceIsDatabase(const std::string& inputDataFileName);
	void setHostedWebService(bool b){m_bHostedWebServiceOperation=b;}
	int getTransformationID() const {return m_TransformationID;}
	long getFunctionID(){return m_Function_ID;}
public:
	KString g_TreeText;
	KString m_lastErr;
	FILE*m_debug_log;
public:
	bool m_bChunking, m_bIsClone;
	bool m_bWriteLog;
	int m_iThread, m_iChunk, m_nChunkSize;
	konga::ProfilingKit* m_pProfilingKit;
public:
	const std::string& getSqlBeginSourceName(){return getBeginSourceName();}
	const std::string& getSqlEndSourceName(){return getEndSourceName();}
	const std::string& getSqlBeginTargetName(){return getBeginTargetName();}
	const std::string& getSqlEndTargetName(){return getEndTargetName();}
	std::string getAutoSequenceSql(const char* sTableName, const char* sColumnName);
	CROM* getSourceCrom(const std::string&path);
	void linkTokenToSourceDE(CTransform*tran, konga::Token*token, const std::string& path);
	void setName(const std::string&name){m_stTransformationName=name;}
	std::string getName(){return m_stTransformationName;}
	bool isTestOperation() const { return m_bTestOperation;}
	bool isLoadSourceData() const { return m_bLoadSourceData;}
	bool isTestTransformation() const {return m_bTestTransformation;}
	bool doStreaming() const {return m_bStreamingTransformation;}
	const char* getWriteModeStr() const;
	bool isSwallowCR() const {return true;}
	void SetTransformationChainItemInfo(const TransformationChainItem& item, long previousTransformationId);
	void defineSourceDbDriverDependentStr(const KString& csSourceConnectString);
	void defineTargetDbDriverDependentStr(const KString& csTargetConnectString);
	const std::string& getBeginSourceName() const {return m_sSourceBeginName;}
	const std::string& getEndSourceName() const {return m_sSourceEndName;}
	const std::string& getBeginTargetName() const {return m_sTargetBeginName;}
	const std::string& getEndTargetName() const {return m_sTargetEndName;}
	FILE* getDebugLog(){return m_debug_log;}
	void closeDbLookup();

	void setSourceIsDatabaseFlag(bool b){m_bSourceIsDatabase=b;}
	bool IsFlatSource(){
		return strnicmp((const char*)m_NatureOfSource,"flat",4)==0;
	}
	bool IsFlatTarget(){
		return strnicmp((const char*)m_NatureOfTarget,"flat",4)==0;
	}
	void setTargetTransacNode(const std::string&stPath);
	CROM* getSourceTransacNode(const std::string&stPath);
	const char* getSourceTransacPath();
	void setSourceTransacNode(CROM*node);
	const char* getTargetTransacPath();
	const char* getOutputDataFileName(){return (const char*)m_outputDataFileName;}
	void CombineTargetFiles(const std::string& stFileName, int nChunk);
	int splitSourceFile(const std::string& stFileName, const size_t chunk_size);
	CRXmlParser& getTargetXmlParser(){return m_targetXmlParser;}
	const char*getUpdateFieldString(){return (const char*)m_UpdateFields;}

    bool IsSourceWebServiceType() { return (m_sourceType == WebServiceInputTypeId) || (m_sourceType == WebServiceOutputTypeId); }
	bool IsTargetWebServiceType() { return (m_targetType == WebServiceInputTypeId) || (m_targetType == WebServiceOutputTypeId); }
	CROM*getSourceTransacCrom(){return m_source_transac_crom;}
	CROM*getTargetTransacCrom(){return m_target_transac_crom;}
private:
	bool m_bBulkTransform;
	bool m_bHostedWebServiceOperation;
	bool m_bNoSourceInMapping;

	TextDocument* m_pTextDocument;                                                        
	XmlDocument* m_pXmlDocument;

	TransformationEngine* m_pTransformationEngine;
	konga::TransformationDataHolder* m_pTransformationDataHolder;
	std::map<std::string,CROM*>g_sourceCromTable;
	CROM* m_source_transac_crom;
	CROM* m_target_transac_crom;
	std::string m_stTransformationName;
	std::string m_sSourceBeginName, m_sSourceEndName, m_sTargetBeginName, m_sTargetEndName;	                                                   
	std::string m_sSourceDbDriver, m_sTargetDbDriver;
	konga::DatabaseDriverType m_sourceDriverType, m_targetDriverType;
	Normalization* m_pNormalization;

	std::vector<CROM*> m_vTargetNode;
	std::vector<std::string> m_vSourceInstanceTag;
	konga::KeyValues m_xproperties;

private:
	std::string m_SourceBatchNodePath;
	int m_SourceBatchSize;
	bool m_DoBatchTarget;
	bool m_bUserETLForgeXmlParser;
	TreeMapper*m_nextTransformation;
	std::string m_sTargetDefaultNamespace;

private:
	void CreateXmlSourceTree_JbParser(const std::string& stSourceXMLFileName);
	void CreateXmlSourceTree(const std::string& stSourceXMLFileName);
	                                                    
	long long CreateNonXmlSourceTree(const std::string& stInputDataFileName);
	std::string createSourceInstanceTag(CROM*lastNode, CROM*targetNode, CRData*sourceDataNode, int instanceNumber);
	bool autoMappingForXmlNil();
	void DeleteData();
	bool isCSVSource() const;

public:
	   
                                                                              
                                                                                                   
                             
    
	CROMData* get_SourceDataRoot();
	void set_SourceDataRoot();
	CROM* getNormalizationFlatSourceTable();

	unsigned long ldapConnect(konga::LdapExecutor*p);
	konga::LdapTreeNode* loadLdapSource(konga::LdapExecutor*p, konga::LdapClassFilter*pClassFilter, FILE*out);
	unsigned long WriteToText(CROMData*pDataRoot, const KString& FileName, bool bIsFlat);
	bool isQualifierRequired() const;
	bool isEDIFACT() const;
	std::string getTempSourceJtrFileName();
	std::string getTempTargetJtrFileName();
	std::string getAuxSourceStructFileName();
	std::string getAuxTargetStructFileName();
	std::string getDebugFileName();
	std::string getOdbcSourceLogFileName();
	std::string getOdbcTargetLogFileName();
	std::string getJdbcSourceLogFileName();
	std::string getJdbcTargetLogFileName();
	std::string getDefaultFileName();
	std::string getSourceUpdateDataFileName();
	std::string getSourceDataFileName();
	std::string getTargetDataFileName();
	std::string getLinkFileName();
	std::string getOdbcUpdateSourceLogFileName();
	std::string getJdbcUpdateSourceLogFileName();
	const char* getSourceDbDriver() const {return m_sSourceDbDriver.c_str();}
	const char* getTargetDbDriver() const {return m_sTargetDbDriver.c_str();}
	bool IsNormalizationTransformation() const {return m_pNormalization!=0;}
	bool IsSourceUpdateTransformation() const {return !m_UpdateFields.IsEmpty();}
	void ReplaceGlobalDataElementsInConditions(CROM*node);
	const KString& GetSourceWhere() const {return m_sSourceWhere;}
	void createXmlInstance(const CROM*pTargetNode);
	void createHfInstance(const CROM*pTargetNode);
	bool IsNoSource() const {return m_sourceType==NoSourceTypeId || (m_bNoSourceInMapping && !isTestOperation());}
	bool readNextBatch(CRData*source0, CROM*generator, std::vector<CRData*>&instances);
	bool autoQueryMore() const;


public:
	bool doBatchTarget(){return m_DoBatchTarget;}
	void prepareBatchFunction(TreeMapper*next);
	void setTargetDefaultNamespace(const std::string& namespace_){
		m_sTargetDefaultNamespace=namespace_;
	}
	const std::string& getTargetDefaultNamespace(){
		return m_sTargetDefaultNamespace;
	}
	const std::string& getSchemaLocation(){
		return m_sSchemaLocation;
	}
	bool useETLForgeXmlParser();
	bool validateXml(CROM*root, const std::string& sourceXMLFileName, const std::string& inputStructFileName, std::string& errmsg);
	bool useJbNode(){return m_bUserETLForgeXmlParser;}
};

KString OverideDbFilter(const KString& originalSql, const KString& overRideParams);

inline	std::string TreeMapper::getTransformationName_() const {
		if(m_iThread==0 && m_iChunk==0)
			return "tran_"+konga::toString(getTransformationID());
		else
			return "tran_"+konga::toString(getTransformationID())+"_T"+konga::toString(m_iThread)+"_"+konga::toString(m_iChunk);
	}
inline	std::string TreeMapper::getTransformationName__(const char*tag) const {
		std::string guid=getOperationContext()->getLocalTmpDirForOperation();
		if(!m_bChunking)
			return "tran"+konga::toString(getTransformationID())+tag+"_"+guid.substr(guid.size()-9,8)+".log";
		else
			return "tran"+konga::toString(getTransformationID())+tag+"_"+guid.substr(guid.size()-9,8)+"_T"+konga::toString(m_iThread)+"_"+konga::toString(m_iChunk)+".log";
	}
inline	konga::SoapFault* TreeMapper::AdoptSoapFault() {
		konga::SoapFault* p = m_pSoapFault;
		m_pSoapFault = 0;
		return p;
	}
inline	DTDParser* TreeMapper::GetSourceDTDParser() {
		                                                                
		                                      
		assert(m_sourceDTDParser!=0);
		return m_sourceDTDParser;
	}
inline	DTDParser* TreeMapper::GetTargetDTDParser() {
		                                                                
		                                     
		assert(m_targetDTDParser!=0);
		return m_targetDTDParser;
	}

bool getDbTargetTransactionFlag(const konga::OperationDataElementHolder& deHolder);

#endif                  
