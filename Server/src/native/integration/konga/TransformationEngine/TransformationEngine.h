/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_ENGINE_H
#define KONGA_TRANSFORMATION_ENGINE_H

#include <string>

#include <boost/thread/condition.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Debugger/BreakpointLocation.h>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/HashTable.h>
#include <konga/Utils/KString.h>
#include <konga/Utils/MapUtils.h>
#include <konga/Utils/PointerCache.h>
#include <konga/Utils/Timing.h>
#include <konga/Utils/KeyValues.h>

#include <konga/Xml/DOMNodePtr.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/OperationContext/ErrorReporter.h>
#include <konga/OperationContext/DataElementHandler.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include <konga/TransformationUtils/FileTypeTargetCache.h>
#include <konga/TransformationUtils/TransformationDataHolder.h>
#include <konga/TransformationUtils/TransformationStatistics.h>

#include "TransformationEngineApi.h"	                           
#include "WebServiceHandler.h"

#define DSN_LESS_CONNECTION		"(dsnless)"
#define CUSTOM_CONNECTION		"(custom)"

namespace konga {
	class Token;
	class DataElement;
	class OperationContext;
	class DbConnection;
	class SoapFault;
	class ScriptCache;
	class LdapTreeNode;
	class LdapClassFilter;
};

class CROM;
class CTransform;
class TransformationEngine;
class TransformationChain;
class TreeMapper;

class TransformationEngineErrorReporter : public konga::ErrorReporter {
public:
	TransformationEngineErrorReporter() : m_pTransformationEngine(0) {}
	virtual ~TransformationEngineErrorReporter(void) {};

	void setTransformationEngine(TransformationEngine* pTransformationEngine) {
		m_pTransformationEngine = pTransformationEngine;
	}
	
	virtual std::string getError() const;
	virtual std::string getLastError() const;

private:
	TransformationEngine* m_pTransformationEngine;
};

enum TargetWriteMode {
	BINARY = 0,
	ASCII = 1
};

                                                                                                                          
                                                                                    
class TRANSFORMATIONENGINE_API TransformationEngine
{
	friend class DoTransformationChain;
	friend class TransformationEngineErrorReporter;

private:
	                                                                                                  
      TransformationEngine( const TransformationEngine& );
      const TransformationEngine& operator=( const TransformationEngine& );

private:
	konga::UserInfo m_CurrentUser;
	unsigned long m_hr;		                                              
	bool m_bGlobalDataElementInitialized;
	konga::TransformationStatistics m_statistics;	                      
	TargetWriteMode m_TargetWriteMode;
	std::map<void*,int> g_CRXmlDataMap;
	std::string m_sourceFileName;

	TransformationEngineErrorReporter m_ErrorReporter;

public:
	TransformationEngine(const konga::UserInfo& currentUser, konga::OperationContext* context = 0, konga::OperationDataElementHolder* deHolder = 0);
	virtual ~TransformationEngine();

	const std::string& getInputDataFileName(){return m_stInputDataFileName;}

	TargetWriteMode getTargetWriteMode() const { return m_TargetWriteMode; }

	inline CViewLog* getViewLog() const {return m_pTransformationDataHolder->getViewLog();}
	inline FILE* getDebugLog(){return m_debug_log;}
	konga::TransformationStatistics getTransformationStatistics() const;

	std::string getLastError() const;

	void setLastErrorCode(unsigned long hr);

	                                                                       
	std::string getErrorsAndWarnings();

	                                                                           
public:
	KString m_lastErr;
	FILE* m_debug_log;

	            
	konga::ProfilingKit* m_pProfilingKit;
	void enterProfilingSection(const std::string& sWhere, const std::string& sExtra);
	void exitProfilingSection(const std::string& sWhere);
	void initPROFILING(const std::string& prefix, int id);
	void initPROFILING(const char*prefix, int id);
	  

	konga::HashTable<const char*,const char*>* m_cacheLookup_CTransform;
	std::vector<char*> m_CacheVector_CTransform;

public:
	konga::FileTypeTargetCache& getFileTargetCache() {
		return *m_pTransformationDataHolder->getFileTypeTargetCache();
	}

	void closeDbLookup();

	static const char* getWriteModeStr(TargetWriteMode wm);
	const char*getWriteModeStr() const;

	                                                                                 

	int getTargetType() {
		return m_targetType;
	}

protected:
	inline bool isGlobalDataElementInitialized(){return m_bGlobalDataElementInitialized!=0;}
	inline CErrorLog* getErrorLog() const {return m_pTransformationDataHolder->getErrorLog();}

protected:
	int m_nTransformation;

private:
	int m_TransformationID;
	int m_originalSourceNumber;
	int m_nChunkSize;
	int m_OperationType;
	bool m_bTrace;
	bool m_bHostedWebServiceOperation;
	bool m_bChunking;
	TransformationChain* m_pTransformationChain;

	int m_sourceType, m_targetType;
	int m_nChunk, m_iChunk_next, m_chunking_MAX_THREADS;

	bool m_bTestOperation, m_bTestTransformation;
	konga::SoapFault* m_pSoapFault;

	std::vector<TreeMapper**> m_vpTreeMapper_list;
	std::vector<konga::TransformationDataHolder*>m_vpTransformationDataHolder;

	std::string m_stInputDataFileName, m_stOutputDataFileName;

	std::string m_log_guid;

public:
	                                                 
	                                                                                                                          
	unsigned long doOperation(const std::string& stInputDataFileName,
							  const std::string& stOutputDataFileName,
							  int originalSourceNumber,
							  int& nTransformationStatus,
							  int& iSourceType,
							  bool& bIsSourceEmpty );

	                                                                               
	unsigned long TestTransformation(long lTransformationID, std::string& stInputFileName, std::string& stOutputFileName, std::string& stLinkFileName);

	unsigned long LoadSourceData(long lTransformationID, const std::string& inputFileName, konga::DbConnection& tranDb);

	unsigned long doTransformationChain(int iThread, int iChunk, int nChunkSize, TreeMapper**pTreeMappers);

	unsigned long CreateJtrFromMappings(long lTransformationID, const std::string& stSourceJtrFN, const std::string& stTargetJtrFN);

	unsigned long TestOperation(long lOperationID, const std::string& stInputFN,  const std::string& stOutputFN, int& iSourceType, bool& bIsSourceEmpty);

	std::string TestExpression(const std::string& stNameValuePairs, const std::string& stExpression, std::string& stErrors);

	konga::DataElement* RunScript(long scriptId, konga::ScriptCache& scriptCache);

	                                                                    
	void HandleWebServiceCall(const std::string& sRequest, std::string& sResponse, bool& returnedSoapFault);

	int getOperationType() const {return m_OperationType;}

	konga::OperationContext* getOperationContext(){return m_pTransformationDataHolder->getOperationContext();}
	konga::OperationDataElementHolder* getOperationDataElementHolder() const {return m_pTransformationDataHolder->getOperationDataElementHolder();}

	const konga::SoapFault* getSoapFault() const {
		return m_pSoapFault;
	}

	bool autoQueryMore() const;

	bool m_bWriteLog;
	std::string m_sErrLogFile;
	std::string m_transac_source_path, m_transac_target_path;

private:
	inline long getOperationId() const { return m_pTransformationDataHolder->getOperationContext()->getOperationId(); }

	void AppendErrorMessage(const KString& csError);

	                               
	konga::DataElement* getGlobalDataElement(const char* tag);

	unsigned long InitializeOperation(bool& bRemoveTempDir, konga::DbConnection& tranDbConn);
	void InitDebugging();
	unsigned long GetOperationTabInfo(konga::DbConnection* pTranDbConn);
	void WriteOperationLogHeader();

	unsigned long doMultiThreadedTransformationChains(int nThread, TreeMapper** pTreeMappers_0, int& nTransformationStatus);
	void collectSoapFaults();

	void deleteClonedTreeMappers();

	mutable boost::mutex m_ThreadSyncLock, m_LdapThreadSyncLock;                                                 
	int getNextChunkId();
	void updateTransformationStatistics(const konga::TransformationStatistics& s);

	int getFirstTransformationSourceType(){return m_sourceType;}

	bool shouldCheckForNullCharacters() const;

	                                                                   
	bool isTesting() const { return m_bTestOperation || m_bTestOperation; }

	void HandleSpecialSourceStructures(const std::string& sourceFilePath, bool keepOriginal, konga::DbConnection* pTranDb);
	void HandleSpecialTargetStructures(const std::string& targetFilePath, long transformationId);

protected:
	konga::TransformationDataHolder* m_pTransformationDataHolder;

private:
	konga::LdapExecutor* m_pLdap_exec;
	konga::LdapClassFilter* m_pClassFilter;
	konga::KeyValues m_xoperationProperties;

public:
	konga::LdapTreeNode* retrieveLdapSource(TreeMapper*treeMapper, konga::LdapClassFilter*pClassFilter, FILE*out);
	void setmax_iChunk();

};

class PROFILING
{
public:
	PROFILING(TransformationEngine*p0,const std::string&sWhere0,const std::string&sExtra="");
	PROFILING(TreeMapper*p0,const std::string& sWhere0, const std::string&sExtra="");
	~PROFILING();

private:
	TransformationEngine*pTransformationEngine;
	TreeMapper*pTreeMapper;
	std::string sWhere, sExtra;
};


void addToSourceCromTable(CROM*node,std::map<std::string,CROM*>&table, bool bRecursive);

#endif                                 

