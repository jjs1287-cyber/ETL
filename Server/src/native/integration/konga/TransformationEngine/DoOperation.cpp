/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdlib.h>
#include <stdio.h>

#include <time.h>
#include <sstream>
#include <string>
#include <limits>
#include <exception>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>

#include <boost/thread/thread.hpp>

#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/util.h>

#include <konga/Ldap/konga_ldap.h>
#include <konga/Ldap/LdapTreeNode.h>

#include <konga/WinUtils/WinErrorUtils.h>	                        

#include <konga/Log/logging.h>

#include <konga/Soap/SoapFault.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbLookup.h>
#include <konga/Db/TranDb/OperationsTab.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/OperationObjects/OperationType.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>

#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/WDIPathFinder.h>

#include <konga/SystemVariables/SystemVariables.h>	                            

#include <konga/TransformationUtils/Definitions.h>
#include <konga/TransformationUtils/TransformationDataHolder.h>
#include <konga/TransformationUtils/LdapExecutor.h>

#include <konga/TransformationDataStore/TransformationDataStore.h>

#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include "ProfilingSections.h"
#include "DiffConstants.h"
#include "Diff.h"
#include "TransformationEngine.h"
#include "TreeMapper.h"
#include "RaiseErrorException.h"
#include "DataElementUtils.h"
#include "ErrorCodes/resource.h"

using namespace std;
using namespace konga;
using namespace TranDb;

namespace {

                                                     
void appendChunkingFiles(const string& stFileName, int nChunk){
	std::string stFileContents;
	FILE*out=fopen(stFileName.c_str(),"a+");
	if(out==0){
		const long lErr = GetLastError();
		throw konga::CrException("CombineFile(\""+stFileName+"\") failed. " + ErrorResolver().Resolve(lErr) );
	}
	AutoFileHandle adopt_out(out);

	for(int iChunk=0; iChunk<nChunk; iChunk++){
		const string& stFileName_=konga::GetSequenceFileName(stFileName, "_", iChunk);
		if(!konga::FileExists(stFileName_))continue;

		                                  
		konga::read_file_to_string(stFileName_,stFileContents);
		if(!stFileContents.empty()){
			fprintf(out,"%s",stFileContents.c_str());
		}
		konga::kDeleteFile(stFileName_);
	}

	adopt_out.Close();

	                                 
	long lErr;
	if(konga::GetFileSize(stFileName, lErr) == 0) {
		try {
			konga::DeleteFileWithRetries(stFileName, 10, 1000);
		}
		catch(...) {
			                    
		}
	}
}

}                    

void TransformationEngine::deleteClonedTreeMappers(){
	for( vector<TreeMapper**>::iterator it=m_vpTreeMapper_list.begin();it!=m_vpTreeMapper_list.end();++it ) {
		TreeMapper** pTreeMapper_list=*it;
		for(int iTransformation=0; iTransformation < m_nTransformation; ++iTransformation){
			delete pTreeMapper_list[iTransformation];
			pTreeMapper_list[iTransformation]=0;
		}
		delete[] pTreeMapper_list;
		*it=0;
	}
	m_vpTreeMapper_list.clear();

	for(vector<konga::TransformationDataHolder*>::iterator it=m_vpTransformationDataHolder.begin();it!=m_vpTransformationDataHolder.end();it++){
		delete *it;
		*it=0;
	}
	m_vpTransformationDataHolder.clear();
}

void TransformationEngine::collectSoapFaults() {
	                                       
	if ( m_pSoapFault != 0 )
		return;

	for( vector<TreeMapper**>::iterator it=m_vpTreeMapper_list.begin();it!=m_vpTreeMapper_list.end();++it ) {
		TreeMapper** pTreeMapper_list=*it;
		for(int iTransformation=0; iTransformation < m_nTransformation; ++iTransformation){
			TreeMapper*pTreeMapper=pTreeMapper_list[iTransformation];
			const konga::SoapFault* const pSoapFault = pTreeMapper->GetSoapFault();
			if(pSoapFault!=0){
				m_pSoapFault = pTreeMapper->AdoptSoapFault();
				return;	                                                  
			}
		}
	}
}


class DoTransformationChain {
public:
	DoTransformationChain( TransformationEngine* pTransformationEngine, int iThread, int iChunk, int nChunkSize, TreeMapper** pTreeMappers) :
	  m_wasInitialized(false),
		m_iThread(iThread), m_iChunk(iChunk), m_nChunkSize(nChunkSize), 
		m_pTreeMappers(pTreeMappers), m_pTransformationEngine(pTransformationEngine),
		m_pProfilingKit(0)
	{}
	~DoTransformationChain() {
		if(!m_wasInitialized)return;

		for(int iTransformation=0, nTransformation=m_pTransformationEngine->m_nTransformation;
			iTransformation<nTransformation; iTransformation++){
				m_pTreeMappers[iTransformation]->m_pProfilingKit=m_pTransformationEngine->m_pProfilingKit;
		}
		if(m_pProfilingKit)delete m_pProfilingKit;
	}

	void operator()() {
		if(m_pTransformationEngine->m_pProfilingKit){
			m_pProfilingKit=new konga::ProfilingKit(m_pTransformationEngine->m_pProfilingKit, m_iThread);
			for(int iTransformation=0, nTransformation=m_pTransformationEngine->m_nTransformation;
				iTransformation<nTransformation; iTransformation++){
					m_pTreeMappers[iTransformation]->m_pProfilingKit=m_pProfilingKit;
			}
		}
		KongaExceptionHandler handler(Logger::GetLogFileName("TransformationEngine"));
		m_wasInitialized = true;
		unsigned long hr=m_pTransformationEngine->doTransformationChain(m_iThread, m_iChunk, m_nChunkSize, m_pTreeMappers);
		if(FAILED(hr))m_pTransformationEngine->setLastErrorCode(hr);
	}

private:
	bool m_wasInitialized;
	int m_iThread, m_iChunk, m_nChunkSize;
	TreeMapper** m_pTreeMappers;
	TransformationEngine* m_pTransformationEngine;
	konga::ProfilingKit* m_pProfilingKit;
};

namespace {
	void Clear(TreeMapper** ptm, int nSize) {
		for(int i = 0; i < nSize; ++i) ptm[i]=0;
	}
}

unsigned long TransformationEngine::InitializeOperation(bool& bRemoveTempDir, konga::DbConnection& tranDbConn) {

	                                                          
	bRemoveTempDir = false;
	try
	{
		konga::ItemProperties itemProps(&tranDbConn);
		m_xoperationProperties.Clear();
		itemProps.GetItemProperties(OperationEntityId, getOperationId(), m_xoperationProperties);

		m_nChunkSize=m_xoperationProperties.GetInt(OPERATION_PROPERTY_CHUNK_SIZE, 0);
		if( isTesting() )m_nChunkSize=0;
		m_transac_source_path=m_xoperationProperties.GetString(OPERATION_PROPERTY_SOURCE_CHUNK_NODE);
		m_transac_target_path=m_xoperationProperties.GetString(OPERATION_PROPERTY_TARGET_CHUNK_NODE);
		m_chunking_MAX_THREADS=m_xoperationProperties.GetInt(OPERATION_PROPERTY_MAX_THREADS);
		if(!m_bWriteLog)m_bWriteLog=m_xoperationProperties.GetInt(OPERATION_PROPERTY_WRITE_LOG, 0)!=0;
	}
	catch( konga::DbException& e )
	{
		AppendErrorMessage(e.GetErrorString().c_str());
		return E_FAIL;
	}
	std::string guid=getOperationContext()->getLocalTmpDirForOperation();
	getViewLog()->Initialize("op_", getOperationId(), m_debug_log, true, m_bWriteLog, 0, 0, false, guid.substr(guid.size()-37,36));
	m_sErrLogFile=getViewLog()->GetLogFileName();
	WriteOperationLogHeader();


	m_pTransformationChain=new TransformationChain(getOperationId());

	return GetOperationTabInfo(&tranDbConn);
}

namespace {
	const string DB_COMMIT_CHUNKS_SYSTEM_VARIABLE("etlforge.target.db.commit_chunks");

	const char TOTAL_THREADS_VARIABLE_NAME[]	= "etlforge.transformation.total_threads";
	const char TOTAL_CHUNKS_VARIABLE_NAME[]		= "etlforge.transformation.total_chunks";
	const char IS_CHUNKING_VARIABLE_NAME[]		= "etlforge.transformation.chunking";

	void setGlobalVariables(TreeMapper* pTreeMapper, int nThreads, bool bChunking, int nChunks) {
		if( pTreeMapper == 0 ) return;

		DataElement* de = pTreeMapper->getGlobalDataElement(TOTAL_THREADS_VARIABLE_NAME);
		de->setType(DataElement::t_int);
		de->setValue(konga::toString(nThreads).c_str());

		de = pTreeMapper->getGlobalDataElement(IS_CHUNKING_VARIABLE_NAME);
		de->setType(DataElement::t_bool);
		de->setValue(bChunking ? "true" : "false");

		de = pTreeMapper->getGlobalDataElement(TOTAL_CHUNKS_VARIABLE_NAME);
		de->setType(DataElement::t_int);
		de->setValue(konga::toString(nChunks).c_str());
	}

	const char THREAD_NUMBER_VARIABLE_NAME[]	= "etlforge.transformation.thread_number";
	const char CHUNK_NUMBER_VARIABLE_NAME[]		= "etlforge.transformation.chunk_number";

	                                  
	void setGlobalThreadVariable(TreeMapper* pTreeMapper, int iThread) {
		if( pTreeMapper==0 ) return;

		                                                                                      
		++iThread;

		DataElement* de = pTreeMapper->getGlobalDataElement(THREAD_NUMBER_VARIABLE_NAME);
		de->setType(DataElement::t_int);
		de->setValue(konga::toString(iThread).c_str());
	}

	                                
	void setGlobalChunkVariable(TreeMapper* pTreeMapper, int iChunk) {
		if( pTreeMapper==0 ) return;

		                                                                                      
		++iChunk;

		DataElement* de = pTreeMapper->getGlobalDataElement(CHUNK_NUMBER_VARIABLE_NAME);
		de->setType(DataElement::t_int);
		de->setValue(konga::toString(iChunk).c_str());
	}

	                                   
	void resetGlobalVariables(TreeMapper* pTreeMapper) {
		if( pTreeMapper==0 ) return;

		OperationDataElementHolder* deHolder = pTreeMapper->getOperationDataElementHolder();
		if( deHolder == 0 ) return;
		deHolder->removeOperationDataElement(THREAD_NUMBER_VARIABLE_NAME);
		deHolder->removeOperationDataElement(CHUNK_NUMBER_VARIABLE_NAME);
	}
}

unsigned long TransformationEngine::doOperation(const std::string& stInputDataFileName,
										   const std::string& stOutputDataFileName,
										   int originalSourceNumber,
										   int& nTransformationStatus,
										   int& iSourceType,
										   bool& bIsSourceEmpty)
{
	initPROFILING("Operation", getOperationId());
	PROFILING doPrf(this, DO_OPERATION_PROFILING_SECTION, "Name=\"" + html_string(getOperationContext()->getOperationName()) + "\" Id=\"" + konga::toString(getOperationId()) + "\"");

	unsigned long hr=S_OK;

	m_originalSourceNumber = originalSourceNumber;
	m_stInputDataFileName = stInputDataFileName;
	m_stOutputDataFileName = stOutputDataFileName;
	bool bRemoveTempDir = true;

	try {
		SmartDbPtr<DbConnection> pTranDbForInit( konga::GetBackEndDbConnection("TranDb") );
		hr = InitializeOperation(bRemoveTempDir, *pTranDbForInit);
		if ( FAILED(hr) )
			return hr;

		m_nTransformation=(int)m_pTransformationChain->Length();
		if(m_nTransformation < 1 )
			throw InternalErrorException("A transformation chain has to contain at least one transformation.");

		TreeMapper**pTreeMappers_0=new TreeMapper*[m_nTransformation];
		Clear(pTreeMappers_0, m_nTransformation);

		m_vpTreeMapper_list.push_back(pTreeMappers_0);

		                         
		if(m_nTransformation>0)
		{
			const TransformationChainItem& item = m_pTransformationChain->GetTransformationChainItem(m_nTransformation-1);
			if(item.TransformationId == -99)m_nTransformation--;
		}

		TreeMapper*pTreeMapper = 0;

		int prevTransformationId=-1;
		for( int iTransformation = 0; iTransformation < m_nTransformation; ++iTransformation )
		{
			const TransformationChainItem& item = m_pTransformationChain->GetTransformationChainItem(iTransformation);
			if(item.TransformationId != -99){
				pTreeMapper=new TreeMapper(this, m_pTransformationDataHolder, false, m_bTestOperation, m_bTestTransformation, m_bTrace);
				pTreeMappers_0[iTransformation]=pTreeMapper;
				pTreeMapper->SetTransformationChainItemInfo(item, prevTransformationId);
				if(pTreeMapper->getFunctionID()==0){
					if(iTransformation==0){
						                                                           
						pTreeMapper->m_inputDataFileName=m_stInputDataFileName;
					}
					hr = pTreeMapper->InitTransformation(*pTranDbForInit, item.TransformationId, nTransformationStatus, m_bTrace);
					if ( FAILED(hr) ) {
						AppendErrorMessage(pTreeMapper->get_LastError());
						return hr;
					}
				}
				prevTransformationId=pTreeMapper->getTransformationID();
			}

			prevTransformationId=pTreeMapper->getTransformationID();
		}

		pTreeMapper=pTreeMappers_0[0];
		m_sourceType=pTreeMapper->GetSourceType();
		bool bNoSource=pTreeMapper->IsNoSource();
		bool bChunking=false;
		std::string initialInputFileName;

		if(!bNoSource){
			pTreeMapper->m_inputDataFileName=m_stInputDataFileName;
			initialInputFileName=pTreeMapper->getSourceDataFileName();
			if( !stInputDataFileName.empty() && initialInputFileName!=stInputDataFileName && konga::FileExists(stInputDataFileName) ){
				                                                                              
				konga::kCopyFile(stInputDataFileName, initialInputFileName, false);
			}

            const int textSourceType=pTreeMapper->GetSourceType();
            if((textSourceType==XmlTypeId||textSourceType==TextTypeId||textSourceType==ComplexTextTypeId||textSourceType==JsonTypeId)
                && !initialInputFileName.empty() && konga::FileExists(initialInputFileName)) {
                etlforge::Utf8TextInput normalized(initialInputFileName,konga::GetSystemVariable("etlforge.source.text.character_encoding",pTreeMapper->getOperationDataElementHolder(),""));
                if(normalized.path()!=initialInputFileName)konga::kCopyFile(normalized.path(),initialInputFileName,true);
            }

			HandleSpecialSourceStructures(initialInputFileName, true, pTranDbForInit);

			                                                               
			pTranDbForInit.CloseAndDelete();

			bChunking=m_nChunkSize>0 && pTreeMapper->IsSuitableForChunking();

			                                        
			diff::DiffSnapShot diff(pTreeMapper);
			const diff::DiffStatus diff_status = diff.getDiffStatus();
			if ( diff_status==diff::DiffStatus_None || !diff.hasProcessed() ) {
				if(pTreeMapper->GetSourceType() == DbTypeId){                                                            
					std::string adHocFilter;
					hr = pTreeMapper->dumpDatabaseSourceToFile(initialInputFileName, adHocFilter);
					if (FAILED(hr)) {
						AppendErrorMessage(pTreeMapper->m_lastErr);
						return hr;
					}
					pTreeMapper->setSourceIsDatabaseFlag(false);
				}
				else if(pTreeMapper->GetSourceType()==LdapTypeId){
					pTreeMapper->dumpLdapSourceToFile(initialInputFileName);
				}

			}
			if(shouldCheckForNullCharacters() && !initialInputFileName.empty() && konga::FileExists(initialInputFileName)){
				PROFILING doPrf(this, READ_DATABASE_SOURCE_REMOVE_NULLS_PROFILING_SECTION);
				if(konga::removeNullCharsFromFile(initialInputFileName)){
					getViewLog()->LogMessage("some zero byte characters were removed from the source");
				}
			}

			if ( diff_status!=diff::DiffStatus_None ) {
				PROFILING doDiffPrf(this,"ProcessSnapShot");
				const string stPreDiffFileName(initialInputFileName);
				diff.ProcessSnapShot(initialInputFileName);
				pTreeMapper->setSourceIsDatabaseFlag(false);
				if ( stPreDiffFileName != initialInputFileName && !konga::FileExists(stPreDiffFileName) ) {
					                                                                              
					konga::kCopyFile(initialInputFileName, stPreDiffFileName, false);
					initialInputFileName = stPreDiffFileName;
				}
			}
		}
		pTranDbForInit.CloseAndDelete();

		int nThreads=1;
		if(bChunking){
			m_bChunking=true;
			pTreeMapper->setSourceTransacNode(pTreeMapper->getSourceTransacNode(m_transac_source_path));
			char sourceDelimiter(DB_DELIMITER_CHAR), sourceStrQual('"'), sourceNumQual(0);
			if ( pTreeMapper->GetSourceType() == TextTypeId ){
				CROM*pSourceRoot=pTreeMapper->m_sourceDTDParser->m_root;
				sourceStrQual=pSourceRoot->get_strQual();
				sourceNumQual=pSourceRoot->get_numQual();
				sourceDelimiter=pSourceRoot->get_delimiter();
			}
			m_nChunk=pTreeMapper->splitSourceFile(initialInputFileName, m_nChunkSize);
			nThreads=(m_nChunk<m_chunking_MAX_THREADS)? m_nChunk : m_chunking_MAX_THREADS;
			if(isTesting())nThreads=1;
		}
		else {
			m_bChunking=false;
			m_nChunk=1;
			nThreads=1;
		}

		setGlobalVariables(pTreeMapper, nThreads, m_bChunking, m_nChunk);

		if(!m_bChunking || (nThreads==1 && m_chunking_MAX_THREADS!=1)){                                                                                 
			hr=doTransformationChain(0, 0, 0, pTreeMappers_0);
		} else {
			hr = doMultiThreadedTransformationChains(nThreads, pTreeMappers_0, nTransformationStatus);
		}
		if(FAILED(hr))return hr;

		                                                                       
		                                                             
		try {
			PROFILING doPrf3(this, "FlushAllFileTargets");
			getFileTargetCache().FlushAll(m_CurrentUser);
		}
		catch( const CrException& ex ) {
			string sErr("Failed to write to target using WriteFile(). Reason: " + ex.GetErrorString());
			AppendErrorMessage(sErr);
			hr = E_FAIL;
			return hr;
		}

		closeDbLookup();
		
		{
			PROFILING doPrf3(this,"CombineAndWriteTarget");
			TreeMapper*pTreeMapper_last=pTreeMappers_0[m_nTransformation-1];
			if(pTreeMapper_last){
				const int target_type=pTreeMapper_last->GetTargetType();
				switch(target_type){
					case DbTypeId:
						if(konga::GetSystemVariableAsBool(DB_COMMIT_CHUNKS_SYSTEM_VARIABLE, pTreeMapper_last->getOperationDataElementHolder(), false)){
							                                                                                   
							                                                               
							break;
						}
					case TextTypeId:
					case ComplexTextTypeId:
					case XmlTypeId:
					case JsonTypeId:
					case WebServiceInputTypeId:
						if(m_nChunk>1){
							pTreeMapper_last->setTargetTransacNode(m_transac_target_path.c_str());
							pTreeMapper_last->CombineTargetFiles(m_stOutputDataFileName, m_nChunk);
						}
						else {
							const string& defaultTargetFN=pTreeMapper_last->getTargetDataFileName();
							if(konga::FileExists(defaultTargetFN) && !m_stOutputDataFileName.empty() )
								konga::kCopyFile(defaultTargetFN, m_stOutputDataFileName, true);
						}
						if(target_type==DbTypeId && FileExists(m_stOutputDataFileName) && !m_bTestOperation ){
							hr=pTreeMapper_last->processInsertTextToDB(m_stOutputDataFileName);
							if(FAILED(hr)){
								AppendErrorMessage(pTreeMapper_last->get_LastError());
							}
						}
						break;
					case WebServiceOutputTypeId:                      
					case 0:	               
						                                                            
						                                                                                              
						const string& targetDataFileName = pTreeMapper_last->getTargetDataFileName();
						if ( target_type == WebServiceOutputTypeId || konga::FileExists(targetDataFileName) ) {
							kMoveFile(targetDataFileName, m_stOutputDataFileName);
						}
						break;
				}

				m_targetType = target_type;
				if(pTreeMapper_last->IsSourceUpdateTransformation())pTreeMapper_last->ProcessSourceUpdate();

				HandleSpecialTargetStructures(stOutputDataFileName, pTreeMapper_last->getTransformationID());
				if((target_type==TextTypeId || target_type==ComplexTextTypeId || target_type==EdiTypeId || target_type==XmlTypeId || target_type==JsonTypeId)
					&& FileExists(stOutputDataFileName)) {
					try {etlforge::requireUtf8TextFile(stOutputDataFileName);}
					catch(const std::exception& failure){throw CrException(failure.what());}
				}

			}
		}
		int lastTransformation=(m_nTransformation>0)?(m_nTransformation-1):0;
		pTreeMapper=m_vpTreeMapper_list.at(0)[lastTransformation];

		iSourceType = pTreeMapper->GetSourceType();
		bIsSourceEmpty = pTreeMapper->IsSourceEmpty();

		collectSoapFaults();
		deleteClonedTreeMappers();
		appendChunkingFiles(m_sErrLogFile, m_nChunk);

		                                                              
		if ( isTesting() ) {
			TransformationDataStoreInstance instanceFiles;
			instanceFiles.GUID = getOperationContext()->getOperationChainInstanceGuid();
			instanceFiles.operationId = getOperationId();
			const TransformationChainItem* const tci = m_pTransformationChain->GetLastTransformationChainItem();
			instanceFiles.transformationId = (tci != 0) ? tci->TransformationId : m_TransformationID;
			instanceFiles.originalSourceNumber = m_originalSourceNumber;
			instanceFiles.chunkNumber = 0;
			instanceFiles.sourceFile = initialInputFileName;
			instanceFiles.targetFile = m_stOutputDataFileName;

			TransformationDataStore store;
			store.AddFiles(instanceFiles);
		}
	}
	catch(const konga::CrException& e){
		AppendErrorMessage("The operation \""+getOperationContext()->getOperationName()+"\" failed.\nError: "+e.GetErrorString());
		return E_FAIL;
	}

	return hr;
}

unsigned long TransformationEngine::doMultiThreadedTransformationChains(int nThread, TreeMapper**pTreeMappers_0, int& nTransformationStatus) {
	PROFILING doPrf(this,"DoMultiThreadedTransformationChains");
	unsigned long hr = S_OK;
	{                                             
		PROFILING doPrf1(this,"doMultiThreadedTransformationChains_clone");
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		                                                                                        
		for(int iThread=1; iThread < nThread; ++iThread){
			TreeMapper**pTreeMappers=new TreeMapper*[m_nTransformation];
			Clear(pTreeMappers, m_nTransformation);
			m_vpTreeMapper_list.push_back(pTreeMappers);

			konga::TransformationDataHolder*pTransformationDataHolder=new TransformationDataHolder(
				m_pTransformationDataHolder->getOperationContext()->getCurrentUser(),
				m_pTransformationDataHolder->getOperationContext(), NCT_REFERENCE,
				m_pTransformationDataHolder->getOperationDataElementHolder(), CT_CLONE,
				0, 0, CT_CREATE,	                   
				0, CT_CREATE,		                                      
				m_pTransformationDataHolder->getCachedDbLookup(), NCT_REFERENCE,
				m_pTransformationDataHolder->getFileTypeTargetCache(), NCT_REFERENCE,
				m_pTransformationDataHolder->getScriptCache(), NCT_REFERENCE);
			m_vpTransformationDataHolder.push_back(pTransformationDataHolder);
			int prevTransformationId=-1;
			for(int iTransformation=0; iTransformation < m_nTransformation; ++iTransformation){
				TreeMapper* pTreeMapper=new TreeMapper(*pTreeMappers_0[iTransformation], pTransformationDataHolder);
				const TransformationChainItem& item = m_pTransformationChain->GetTransformationChainItem(iTransformation);
				pTreeMapper->SetTransformationChainItemInfo(item, prevTransformationId);
				if(pTreeMapper->getFunctionID()==0){
					hr = pTreeMapper->InitTransformation(*pTranDb, item.TransformationId, nTransformationStatus, false);
					if ( FAILED(hr) ) {
						AppendErrorMessage(pTreeMapper->get_LastError());
						return hr;
					}
				}
				pTreeMappers[iTransformation]=pTreeMapper;
				prevTransformationId=pTreeMapper->getTransformationID();
			}
		}
	}

	{
		PROFILING doPrf2(this,"doMultiThreadedTransformationChains_doThread");

		m_iChunk_next = nThread<m_nChunk? nThread : m_nChunk;
		vector<boost::thread*> vThreads;
		AutoPtrDeleteVector<boost::thread> adopt_vThreads(vThreads);
		for(int iThread=0; iThread<nThread; ++iThread) {
			int iChunk=iThread;
			TreeMapper**pTreeMappers=m_vpTreeMapper_list[iThread];
			vThreads.push_back( new boost::thread(DoTransformationChain(this, iThread, iChunk, m_nChunkSize, pTreeMappers)) );
		}
		
		                                                                  
		for( vector<boost::thread*>::iterator itThread = vThreads.begin(); itThread != vThreads.end(); ++itThread) {
			boost::thread* t = *itThread;
			t->join();
			delete t;
			*itThread = 0;
		}

		if(m_pProfilingKit){
			TreeMapper**pTreeMappers=m_vpTreeMapper_list[0];
			for(int iTransformation=0; iTransformation<m_nTransformation; iTransformation++){
					pTreeMappers[iTransformation]->m_pProfilingKit=m_pProfilingKit;
			}
		}
	}

	                                                                       
	return m_hr;
}

namespace {
	template<typename T>
	class AutoCallClose {
	public:
		AutoCallClose(T* p) : m_p(p) {}
		~AutoCallClose(void) {
			if ( m_p ) {
				m_p->Close();
			}
		}

	private:
		T* m_p;
	};
}

namespace {
	void PrepareLogging(TreeMapper**pTreeMappers, int transformationNo, string& prefix, bool& writeToLog) {
		for( int iTransformation = 0; iTransformation < transformationNo; ++iTransformation ){
			TreeMapper*pTreeMapper=pTreeMappers[iTransformation];
			if(pTreeMapper->m_bWriteLog || writeToLog){
				prefix+="_"+konga::toString(pTreeMapper->getTransformationID());
				writeToLog=true;
			}
		}
	}
}

unsigned long TransformationEngine::doTransformationChain(int iThread, int iChunk, int nChunkSize, TreeMapper**pTreeMappers){
	PROFILING doPrf(pTreeMappers[0],"DoTransformationChain", "thread=\""+konga::toString(iThread)+"\"");
	unsigned long hr=S_OK;
	string stLastTransformationName;
	std::string stInputDataFileName, stOutputDataFileName, stLinkFileName;

	string stPrefix("tran");
	bool bWriteLog=m_bWriteLog;
	PrepareLogging(pTreeMappers, m_nTransformation, stPrefix, bWriteLog);

	setGlobalThreadVariable(pTreeMappers[0], iThread);

	try {
		while(iChunk < m_nChunk){
			setGlobalChunkVariable(pTreeMappers[0], iChunk);
			TransformationStatistics statistics0;
			PROFILING doPrf1(pTreeMappers[0],"DoTransformationChain", "thread=\""+konga::toString(iThread)+"\" chunk=\""+konga::toString(iChunk)+"\"");
			bool bPrevSourceWasEmpty=false;

			                                                                                            
			CViewLog* pViewLog = pTreeMappers[0]->getViewLog();
			pViewLog->Initialize(stPrefix.c_str(), getOperationId(), m_debug_log, true, bWriteLog, iThread, iChunk, m_bChunking, m_log_guid);
			pViewLog->setWriteLog(pTreeMappers[0]->m_bWriteLog||m_bWriteLog);
			AutoCallClose<CViewLog> call_pViewLog_Close(pViewLog);

			bool skipNextTransformation=false;
			for( int iTransformation = 0; iTransformation < m_nTransformation; ++iTransformation )
			{
				TreeMapper*pTreeMapper=pTreeMappers[iTransformation];
				if(pTreeMapper->doBatchTarget() && iTransformation+1 < m_nTransformation){
					TreeMapper* nextTreeMapper=pTreeMappers[iTransformation+1];
					if(nextTreeMapper->getFunctionID()!=0){
						pTreeMapper->prepareBatchFunction(nextTreeMapper);
						skipNextTransformation=true;
					}
				}
				pTreeMapper->DeleteTargetXmlData();

				pTreeMapper->m_iThread=iThread;
				pTreeMapper->m_iChunk=iChunk;

				if(iTransformation==0) {
					if (m_bChunking) pTreeMapper->setSourceIsDatabaseFlag(false);
					pTreeMapper->m_nChunkSize=nChunkSize;
					pTreeMapper->m_bChunking=m_bChunking;
					stInputDataFileName = pTreeMapper->IsNoSource() ? "" : pTreeMapper->getSourceDataFileName();
				}

				if( iTransformation == (m_nTransformation-1) )  {
					if( m_stOutputDataFileName.empty() ) m_stOutputDataFileName = pTreeMapper->getTargetDataFileName();
					pTreeMapper->m_iChunk=iChunk;
					pTreeMapper->m_bChunking=m_bChunking;
				}
				if ( iTransformation != 0 && pTreeMapper->SourceIsDatabase() && !m_bChunking ) {
					                                                         
					pTreeMapper->DumpDatabaseSourceDataIfSourceIsDatabase(stInputDataFileName);
				}

				stOutputDataFileName = pTreeMapper->getTargetDataFileName();
				stLinkFileName = pTreeMapper->getLinkFileName();

			    if (pViewLog->IsDebugLogEnabled()) {
					pViewLog->DebugString("stInputDataFileName = %s\nstOutputDataFileName = %s\nstLinkFileName = %s\n",
						stInputDataFileName.c_str(), stOutputDataFileName.c_str(), stLinkFileName.c_str());
				}

				if( pTreeMapper->getFunctionID() != 0 )
				{
					hr = pTreeMapper->HandleTransformationFunction(pTreeMapper->getFunctionID(), 
													  stInputDataFileName,
													  stOutputDataFileName);
					if (hr == 1) {
						hr = S_OK;
						                                  
						if ( konga::ErrorConfiguration::isOk(konga::ErrorConfiguration::NO_WEBSERVICE_REQUEST_DATA, false) ) {
							break;
						}
						else if ( konga::ErrorConfiguration::isWarning(konga::ErrorConfiguration::NO_WEBSERVICE_REQUEST_DATA, true) ) {
							if (bPrevSourceWasEmpty && !stLastTransformationName.empty()) {
								const string tmp("The source is empty for the Transformation '" + stLastTransformationName
									+ ".'\nNo request was generated and the webservice was not called.\n");
								pViewLog->LogMessage("%s", tmp.c_str());
							}
							else {
								const string tmp("No target was generated for the Transformation '" + stLastTransformationName
									+ "'.\nNo request was generated and the web service was not called.\n");
								pViewLog->LogMessage("%s", tmp.c_str());
							}
							break;
						}
						else {
							                        
							hr = pTreeMapper->getErrorLog()->MakeError(__FILE__, __LINE__, ID_EMPTY_WS_INPUT_NOT_ALLOWED);
						}
					}
					if( FAILED(hr) ) {
						AppendErrorMessage(pTreeMapper->getTransformationName()+(const char*)pTreeMapper->m_lastErr);
						return hr;
					}
				}
				else 
				{
					TransformationStatistics statistics;
					int nTransformationStatus = 0;

					                                                                                                
					if(m_nChunkSize==1)pTreeMapper->m_nChunkSize=1;
					hr = pTreeMapper->doTransformation(
												stInputDataFileName,
												stOutputDataFileName,
												stLinkFileName,
												nTransformationStatus,
												statistics);
					statistics0.Update(statistics);

					if( FAILED(hr) ) {
						AppendErrorMessage("Transformation '" + pTreeMapper->getTransformationName() + "' failed.\n"
							+ pTreeMapper->get_LastError());
						return hr;
					}


					                                                         
					const bool isDb = (pTreeMapper->GetTargetType()==DbTypeId);
					const bool commitDb = isDb &&
						( konga::GetSystemVariableAsBool(DB_COMMIT_CHUNKS_SYSTEM_VARIABLE, pTreeMapper->getOperationDataElementHolder(), false) || (iTransformation != m_nTransformation-1) );
					if( commitDb || ( isDb && m_bTestOperation ) ) {
						if(pTreeMapper->IsSourceUpdateTransformation()){
							throw konga::CrException("SourceUpdate with multithread, multichunking and commit_chunks has not been implemented yet. Please set $etlforge.target.db.commit_chunks=false and try again.");
						}
						hr=pTreeMapper->processInsertTextToDB(stOutputDataFileName);
						if(FAILED(hr)){
							AppendErrorMessage(pTreeMapper->get_LastError());
							return hr;
						}
					}

					                                 
					if ( isTesting() ) {
						TransformationDataStoreInstance inst;
						inst.GUID = getOperationContext()->getOperationChainInstanceGuid();
						inst.operationId = getOperationContext()->getOperationId();
						inst.transformationId = pTreeMapper->getTransformationID();
						inst.originalSourceNumber = m_originalSourceNumber;
						inst.chunkNumber = iChunk + 1;
						inst.sourceFile = stInputDataFileName;
						inst.linkFile = stLinkFileName;
						inst.targetFile = stOutputDataFileName;
						TransformationDataStore store;
						store.AddFiles(inst);
					}
				}

				                                                                                                    
				                                                       
				                                                                                                
				                                                                      
				if( pTreeMapper->GetTargetType() == WebServiceOutputTypeId )
					pTreeMapper->setHostedWebService(false);

				if(FAILED(m_hr))return hr;

				stInputDataFileName = stOutputDataFileName;
				stLastTransformationName=pTreeMapper->getTransformationName();

				if(skipNextTransformation){                                                      
					iTransformation++;
					skipNextTransformation=false;
				}

			}                   

			                       
			iChunk = getNextChunkId();
			updateTransformationStatistics(statistics0);
			if(isTesting())break;
		}         

		resetGlobalVariables(pTreeMappers[0]);
		return hr;
	}
	catch( const RaiseErrorException& e ) {
		AppendErrorMessage(e.GetError());
		hr = E_FAIL;
	}
	catch(const CrFatalException& e)
	{
		std::string stErrMsg = e.GetErrorString();
		KString csErr = "\nAn error was thrown when writing to the disk. Reported error: ";
		csErr += stErrMsg.c_str();
		AppendErrorMessage(csErr);
		hr = E_FAIL;
	}
	catch(const CrException& e)
	{
		std::string stErrMsg( e.GetErrorString() );
		AppendErrorMessage(stErrMsg);
		hr = E_FAIL;
	}
	catch(const std::exception& e ) {
		std::string stErrMsg( e.what() );
		AppendErrorMessage(stErrMsg);
		hr = E_FAIL;
	}
#ifndef _DEBUG
	catch(...)
	{
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_UNKNOWN_ERROR_CAUGHT, (const char*)m_lastErr);
		AppendErrorMessage( getErrorLog()->GetLastErr() );
	}
#endif
	return hr;
}

unsigned long TransformationEngine::GetOperationTabInfo(konga::DbConnection* pTranDbConn)
{
	unsigned long hr = S_OK;
	try
	{
		getViewLog()->DebugString("Opened DB connection\n");

        konga::SmartDbPtr<OperationsTab> op_tab( OperationsTab::MakeInstance(pTranDbConn) );

		const long lOperationId = getOperationId();
		string stOpTabFilter = "WHERE Operation_ID = " + konga::toString(lOperationId);
		op_tab->Select(stOpTabFilter);
		if( op_tab->Next() )
		{
			m_OperationType = op_tab->GetOperation_Type_ID();
			m_bTrace = (op_tab->IsTraceNull()||(op_tab->GetTrace() == 0)) ? false : true;
			m_TransformationID = op_tab->GetTransformation_ID();
			OperationContext* const pContext = getOperationContext();
			if ( pContext ) {
				const string stOperationName(pContext->getOperationName());
				if ( stOperationName.empty() )
					pContext->setOperationName(op_tab->GetOperation_Name());
				const string stOperationGuid(getOperationContext()->getOperationGuid());
				if ( stOperationGuid.empty() ) {
					pContext->SetOperationGuid( konga::GetGuidForId(lOperationId, OperationEntityId) );
				}
			}
		}
		else
		{
			AppendErrorMessage(KString("No operation found with the ID ") + konga::toString(getOperationId()));
			return E_FAIL;
		}

		                                     

		                                   
		if( m_pTransformationChain->Create(m_TransformationID, *getOperationDataElementHolder(), *pTranDbConn) != 0 )
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_FAILED_TO_CREATE_TRANSFORMATION_CHAIN, 
				lOperationId, m_pTransformationChain->GetError().c_str());
			return hr;
		}
		if( m_pTransformationChain->Length() == 0 )
		{
			hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_NO_TRANSFORMATION_IN_OPERATION, lOperationId);
			return hr;
		}
		return S_OK;
	}
	catch( konga::DbException& e )
	{
		AppendErrorMessage( e.GetErrorString().c_str());
		return E_FAIL;
	}
	catch( konga::CrException& e )
	{
		AppendErrorMessage( e.GetErrorString().c_str());
		return E_FAIL;
	}
	catch( ... )
	{
		AppendErrorMessage( "An unknown error occurred.");
		return E_FAIL;
	}	
}

void TransformationEngine::WriteOperationLogHeader()
{
	CViewLog* const pOpViewLog = getViewLog();
	if( pOpViewLog == 0 )
		return;

	const konga::CrDateTime SysTime;

	if (pOpViewLog->IsDebugLogEnabled()) {
		pOpViewLog->DebugString("\n-------------------- %d/%d/%d %d:%d:%d -------------------------\n",
					SysTime.GetMonth(), SysTime.GetDay(), SysTime.GetYear(), SysTime.GetHour(), SysTime.GetMinute(), SysTime.GetSecond());
		pOpViewLog->DebugString("1> TreeMapper::MapByOperationID - Begin\n");
#ifdef _DEBUG
		pOpViewLog->DebugString("DEBUG BUILD\n");
#endif
		if(m_bTestOperation) {
			pOpViewLog->DebugString("\n* This is a test operation *\n");
		}
	}
}

konga::LdapTreeNode* TransformationEngine::retrieveLdapSource(TreeMapper*treeMapper, LdapClassFilter*pClassFilter, FILE*out){
	boost::mutex::scoped_lock lock(m_LdapThreadSyncLock);
	const int nChunkSize=treeMapper->m_nChunkSize;
	if(m_pLdap_exec==0 || nChunkSize==0){
		std::auto_ptr<LdapExecutor> ldap_exec(new LdapExecutor());
		unsigned long hr=treeMapper->ldapConnect(ldap_exec.get());
		if(hr!=S_OK){
			treeMapper->getViewLog()->DebugString("ldapConnect failed");
			return 0;
		}
		konga::LdapTreeNode* pLdapTreeNode=treeMapper->loadLdapSource(ldap_exec.get(), pClassFilter, out);
		if(nChunkSize>0 && pLdapTreeNode!=0){
			if(m_pLdap_exec)delete m_pLdap_exec;
			m_pLdap_exec=ldap_exec.release();
			m_pClassFilter=pClassFilter;
		}
		return pLdapTreeNode;
	}
	else {
		if(m_pLdap_exec && (*m_pClassFilter)==(*pClassFilter)){
			konga::LdapTreeNode* pLdapTreeNode=m_pLdap_exec->NextSearchChunk(m_nChunkSize);
			if(pLdapTreeNode==0){
				delete m_pLdap_exec;
				m_pLdap_exec=0;
			}
			return pLdapTreeNode;
		}
		else {
			return 0;
		}
	}
}

void TransformationEngine::updateTransformationStatistics(const TransformationStatistics& s) {
	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	m_statistics.Update(s);
}

int TransformationEngine::getNextChunkId() {
	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	if(m_hr!=0)return m_nChunk;	                                                                               
	int iChunk = m_iChunk_next;
	if(m_iChunk_next < m_nChunk)m_iChunk_next++;
	return iChunk;
}

void TransformationEngine::setmax_iChunk() {
	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	m_iChunk_next = m_nChunk;
}

void TransformationEngine::setLastErrorCode(unsigned long hr) {
	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	m_hr = hr;
}

bool TransformationEngine::shouldCheckForNullCharacters() const {
	const string key("etlforge.transformation.source.check_null_characters");
	if ( m_pTransformationDataHolder->getOperationDataElementHolder()->isOperationDataElementDefined(key) ) {
		DataElement* de = m_pTransformationDataHolder->getOperationDataElementHolder()->getOperationDataElement(key);
		int err;
		return de->GetBool(err);
	}
	else {
		return true;
	}
}
