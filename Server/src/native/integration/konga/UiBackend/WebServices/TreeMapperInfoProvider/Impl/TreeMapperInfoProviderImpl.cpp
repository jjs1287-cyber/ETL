/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <cstring>	         
#include <cassert>	         
#include <iostream>
#include <exception>

#include <boost/noncopyable.hpp>
#include <boost/scoped_array.hpp>
#include <boost/thread.hpp>

#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>	                  
#include <konga/Utils/CrException.h>

#include <konga/Zip/ZipUtils.h>
#include <konga/Crypto/Crypto.h>	                   

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Debugger/DebugExceptions.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/OperationContext/DefaultOperationContext.h>
#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/OperationEngine/OperationEngine.h>

#include <konga/Server/WDIPathFinder.h>
#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/Server.h>

#include <konga/PluginManager/PluginManifestDbStore.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationEngine/TransformationEngine.h>

#include <konga/TransformationUtils/TranDBUtils.h>
#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include <konga/TransformationDataStore/TransformationDataStore.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/CachedGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "TreeMapperUtils.h"
#include "TreeMapperDebug.h"
#include "TreeMapperInfoProviderImpl.h"

using namespace konga;
using namespace std;

namespace {
	FILE* getDebugLogFile(const string& sLeaf) {
		FILE*debug_log=0;
		if(WDILogFileUtils::GenerateLogFile()){
			std::string WDIDebugPath;
			if ( WDIPathFinder::GetWDIDebugPath(WDIDebugPath) ) {
				WDIDebugPath+=sLeaf;
				debug_log=fopen(WDIDebugPath.c_str(),"w+");
			}
		}
		return debug_log;
	}

	void createTmpFileName(std::string& stTmpFileName, const string& guid, AutoDeleteFileSet& auto_delete_tmp_file, const char*ext=0){
		string stTmp(konga::GetTemporaryLoadedSourceDataDirWithSlash());
		if( !guid.empty() ) stTmp.append(guid);
		else stTmp.append(GetGUIDString());
		if( !konga::IsDirectory(stTmp) ) {
			konga::kCreateDirectory(stTmp);
		}

		konga::AppendPathSeparator(stTmp);
		stTmp.append(GetGUIDString());
		if(ext!=0)stTmp.append(ext);

		stTmpFileName = stTmp;
		auto_delete_tmp_file.AddFile(stTmpFileName);
	}

	void copyDataToTmpFileName(long lTranId, const std::string& stData, std::string& stTmpFileName, const string& guid, AutoDeleteFileSet& auto_delete_tmp_file, const char*ext=0)
	{
		createTmpFileName(stTmpFileName, guid, auto_delete_tmp_file, ext);
		write_string_to_file(stTmpFileName, stData);
	}

	void loadSourcePreconditions(const WsLoadSourceDataInput* const pLoadSourceDataInput) {
		assert(pLoadSourceDataInput);
		if ( !pLoadSourceDataInput )
			throw CrException("Client Error: The input to loadSourceDataTree is null.");
	}

	long lookupId(DbConnection& tranDbConn, const string& guid, int iEntityTypeId, const string& type) {
		DbGuidToIdMapper guidToIdMapper(&tranDbConn);
		const long lTranId = guidToIdMapper.GetId(guid, iEntityTypeId);
		if ( lTranId == GuidToIdMapper::invalid_id ) {
			throw CrException("The specified " + type + " has not been deployed to this server.");
		}
		return lTranId;
	}

	long lookupTransformationId(DbConnection& tranDbConn, const string& stTranGuid) {
		if ( stTranGuid.empty() ) return -1;
		return lookupId(tranDbConn, stTranGuid, TransformationEntityId, "transformation");
	}

	long lookupTransformationId(DbConnection& tranDbConn, WsLoadSourceDataInput* const pLoadSourceDataInput) {
		const string stTranGuid( toString(pLoadSourceDataInput->getTransformationGuid()) );
		return lookupTransformationId(tranDbConn, stTranGuid);
	}

	long lookupSourceId(DbConnection& tranDbConn, WsLoadSourceDataInput* const pLoadSourceDataInput) {
		const string stSourceGuid( toString(pLoadSourceDataInput->getSourceGuid()) );
		if ( stSourceGuid.empty() ) return -1;
		return lookupId(tranDbConn, stSourceGuid, SourceEntityId, "source");
	}

	long lookupOperationId(DbConnection& tranDbConn, const string& stOpGuid) {
		if ( stOpGuid.empty() ) return -1;
		return lookupId(tranDbConn, stOpGuid, OperationEntityId, "operation");
	}

	void runPipelinePluginsOnSourceData(DbConnection& tranDbConn, 
										const WsLoadSourceDataInput* const pLoadSourceDataInput,
										const DefaultOperationContext& context, 
										const DefaultOperationDataElementHolder& deHolder,
										const string& stSourceDataFileName)
	{
		if ( pLoadSourceDataInput->PipelinePlugins && pLoadSourceDataInput->PipelinePlugins->item ) {
			PipelinePositionStruct_Array* pPipelinePlugins = pLoadSourceDataInput->PipelinePlugins->item;
			int nPlugins = 0;
			PipelinePositionStruct** arrPipelinePosition = pPipelinePlugins->get(nPlugins);
			if ( nPlugins > 0 ) {
				PluginManifestDbStore pluginStore(&tranDbConn);
				for( int i = 0; i < nPlugins; ++i ) {
					PipelinePositionStruct* pPipelinePosition = arrPipelinePosition[i];
					if ( pPipelinePosition == 0 )
						continue;
					const string& stUniquePluginName = toString(pPipelinePosition->PluginGuid);
					const string& stRelativePosition = toString(pPipelinePosition->RelativePosition);
					RunSourcePlugin(stUniquePluginName, stSourceDataFileName, stRelativePosition, context, deHolder, pluginStore, tranDbConn);
				}
			}
		}
	}

	                                                                   
	string toBase64(const string& s) {
		if ( s.empty() ) return string(s);

		return konga::EncodeBase64((const unsigned char*)s.c_str(), s.size());
	}

	                                                   
	string fromBase64(const string& base64Encoded) {
		if ( base64Encoded.empty() ) return string(base64Encoded);

		                            
		try {
			int len = 0;
			boost::scoped_array<unsigned char> p( konga::DecodeBase64(base64Encoded, len) );

			                                          
			return string((const char*)p.get(), len);
		}
		catch( const CrException& e ) {
			UIBACKEND_TRACE(e.GetErrorString());
		}

		                                                                       
		return base64Encoded;
	}

	WsDataFile* CreateWsDataFile(const std::string& stLocalPath_in) {
		string stLocalPath;
		AutoArrayDelete<char> adopt_pCompressedBase64EncodedResult(0);
		if ( konga::NonEmptyFileExists(stLocalPath_in) ) {
			stLocalPath = stLocalPath_in;
			char* pCompressedBase64EncodedResult = 0;
			ZipUtils::ZipAndBase64EncodeFile(stLocalPath, pCompressedBase64EncodedResult, false);
			assert(pCompressedBase64EncodedResult);
			adopt_pCompressedBase64EncodedResult.Reset(pCompressedBase64EncodedResult);
		}

		std::auto_ptr<WsDataFile> wsDataFile( new WsDataFile() );
		wsDataFile->setLocalPath(const_cast<xsd__string>(stLocalPath.c_str()), true);

		if ( adopt_pCompressedBase64EncodedResult.Get() != 0 ) {
			wsDataFile->setCompressedData(adopt_pCompressedBase64EncodedResult.Release(), false);
		}
		else {
			wsDataFile->setCompressedData("", true);
		}

		return wsDataFile.release();
	}
}

WsLoadSourceResult* TreeMapperInfoProviderImpl::loadSourceData(WsLoadSourceDataInput* const pLoadSourceDataInput) {
	loadSourcePreconditions(pLoadSourceDataInput);
	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	const long lTranId = lookupTransformationId(*pTranDbConn, pLoadSourceDataInput);
	const long lSrcId = lookupSourceId(*pTranDbConn, pLoadSourceDataInput);

	string stData, stTmpSourceFileName;
	WriteIncomingSourceDataToFile(pLoadSourceDataInput->getCompressedBase64EncodedData(), stData, stTmpSourceFileName);
	AutoDeleteFileSet auto_delete_tmp_file(false);
	auto_delete_tmp_file.AddFile(stTmpSourceFileName);

	DefaultOperationContext context(m_UserInfo, 0);
	DefaultOperationDataElementHolder deHolder(m_UserInfo);
	runPipelinePluginsOnSourceData(*pTranDbConn, pLoadSourceDataInput, context, deHolder, stTmpSourceFileName);

	TransformationEngine transform(m_UserInfo, &context, &deHolder);
	const unsigned long err = transform.LoadSourceData(lTranId, stTmpSourceFileName, *pTranDbConn);
	if ( err != 0 ) {
		throw CrException(transform.getLastError());
	}

	                                                                                            
	TransformationDataStoreInstance inst;
	inst.transformationId = lTranId;
	inst.originalSourceNumber = 1;
	inst.chunkNumber = 1;
	inst.sourceFile = stTmpSourceFileName;

	TransformationDataStore dataStore(pTranDbConn);
	dataStore.AddFiles(inst);

	std::auto_ptr<WsLoadSourceResult> res( new WsLoadSourceResult() );
	res->setGuid(const_cast<xsd__string>(inst.GUID.c_str()), true);
	res->setFile(CreateWsDataFile(inst.sourceFile));

	return res.release();
}

WsDataFile* TreeMapperInfoProviderImpl::downloadDataFile(const char* const guid, const char* transformationGuid, const char* operationGuid, WsDataFileIdentifier* pDataFileIdentifier) {
	const string stGuid( toString(guid) );
	if ( stGuid.empty() ) throw CrException("Client Error: No GUID was specified in downloadDataFile.");
	const string stTranGuid( toString(transformationGuid) );
	const string stOperationGuid( toString(operationGuid) );

	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	const long lTranId = lookupTransformationId(*pTranDbConn, stTranGuid);
	const long lOperationId = lookupOperationId(*pTranDbConn, stOperationGuid);

	TransformationDataStoreInstance inst;
	inst.GUID = stGuid;
	if ( lOperationId > 0 ) inst.operationId = lOperationId;
	if ( lTranId > 0 ) inst.transformationId = lTranId;
	inst.originalSourceNumber = pDataFileIdentifier->getOriginalFileNumber();
	inst.chunkNumber = pDataFileIdentifier->getChunkNumber();

	TransformationDataStore dataStore(pTranDbConn);
	vector<TransformationDataStoreInstance> files = dataStore.GetFileList(inst);
	if ( files.empty() ) throw CrException("Failed to find a data file for the specified parameters.");

	const TransformationDataStoreInstance& firstInst = *files.begin();
	const TransformationDataStoreInstanceType type = konga::fromInt(pDataFileIdentifier->getType());
	const string& stDataFile = firstInst.getFileOfType(type);
	if ( stDataFile.empty() ) throw CrException("There is no file available for the specified parameters.");
	if ( !FileExists(stDataFile) ) throw CrException("This data file does not exist anymore. It has probably expired and has been deleted to save disk space.");

	return CreateWsDataFile(stDataFile);
}

namespace {

	template<typename T>
	void DeleteAndNull(T*&p) {
		if ( p ) {
			delete p;
			p = 0;
		}
	}

class DebugContext : boost::noncopyable {
public:
	DebugContext(const UserInfo& _userInfo, Debuggee* pDebuggee)
		: pThread(0), pDebuggee(pDebuggee), pOperationEngine(0), pTransformationEngine(0), pOperationContext(0), pDeHolder(0),
		iSourceType(0), userInfo(_userInfo), filesToDelete(true)
	{}

	~DebugContext(void) {
		DeleteAndNull(pThread);
		DeleteAndNull(pDebuggee);
		DeleteAndNull(pOperationEngine);
		DeleteAndNull(pTransformationEngine);
		DeleteAndNull(pOperationContext);
		DeleteAndNull(pDeHolder);
	}

	boost::thread* pThread;
	Debuggee* pDebuggee;

	OperationEngine* pOperationEngine;
	TransformationEngine* pTransformationEngine;
	OperationContext* pOperationContext;
	OperationDataElementHolder* pDeHolder;

	int iSourceType;
	UserInfo userInfo;

	AutoDeleteFileSet filesToDelete;

	string stSourceDataFileName, stTargetDataFileName, stLinkDataFileName;

	string sResult;

	std::string getError() const { return pDebuggee->getError().empty() ? sError : pDebuggee->getError(); }
	std::string getWarnings() const { return pDebuggee->getWarning().empty() ? sWarnings: pDebuggee->getWarning(); }

	void setError(const std::string& s) { sError = s; }
	void setWarning(const std::string& s) { sWarnings = s; }

	string sError, sWarnings;
};

map<string, DebugContext*> g_debuggerThreads;
boost::mutex g_debuggerThreadsMutex;

void RemoveDebugSession(const std::string& sDebugSessionId) {
	boost::mutex::scoped_lock lock(g_debuggerThreadsMutex);
	map<string, DebugContext*>::iterator itCtx = g_debuggerThreads.find(sDebugSessionId);
	if( itCtx == g_debuggerThreads.end() ) {
		                                                                 
	} else {
		delete itCtx->second;
		g_debuggerThreads.erase(itCtx);
	}
}

class AutoRemoveDebugSession {
public:
	AutoRemoveDebugSession(const string& sDebugSessionId) :m_sDebugSessionId(sDebugSessionId) {}
	~AutoRemoveDebugSession(void) {
		RemoveDebugSession(m_sDebugSessionId);
	}

private:
	const string m_sDebugSessionId;
};

void StopDebugSession(const std::string& sDebugSessionId) {
	map<string, DebugContext*>::iterator itCtx = g_debuggerThreads.find(sDebugSessionId);
	if ( itCtx == g_debuggerThreads.end() )
		return;

	DebugContext* pDebugContext = itCtx->second;
	pDebugContext->pDebuggee->shouldStop(true);
	pDebugContext->pDebuggee->DebugContinue();
}

void StopDebugSessionSync(const std::string& sDebugSessionId) {
	boost::mutex::scoped_lock lock(g_debuggerThreadsMutex);
	StopDebugSession(sDebugSessionId);
}

                                 
class TestThreadFunction {
public:
	TestThreadFunction(Debuggee* debuggee_, const string& stDebugSessionId_, string* stError_)
		: stDebugSessionId(stDebugSessionId_), stError(stError_), pDebuggee(debuggee_) {}
	virtual ~TestThreadFunction() { }

protected:
	                                                                            
	virtual void DoTest() = 0;
	virtual void StartThreadImpl(DebugContext* pDebugContext) = 0;

public:
	                                       
	void Start(DebugContext* pDebugContext) {
		assert(pDebugContext);
		StartThreadImpl(pDebugContext);
		g_debuggerThreads[ this->stDebugSessionId ] = pDebugContext;
	}

	                                                   
	void operator()() {
		try {
			DoTest();
			pDebuggee->DebugBreakpointReset();
		}
		catch( const DebugSessionExpired& ) {
			RemoveDebugSession(stDebugSessionId);
		}
		catch( const CancelDebugSession& ) {
			RemoveDebugSession(stDebugSessionId);
		}
		catch(const CrException& e) {
			*stError = e.GetErrorString();
			pDebuggee->DebugBreakpointReset();
		}
		catch( const std::exception& e) {
			*stError = string("Unexpected std::exception: ") + e.what();
			pDebuggee->DebugBreakpointReset();
		}
	}

public:
	const string stDebugSessionId;
	string* stError;

private:
	Debuggee* pDebuggee;
};

class TestExpressionThreadFunction : public TestThreadFunction {
public:
	TestExpressionThreadFunction(TransformationEngine* transform_, Debuggee* debuggee_, const string& stDebugSessionId_, string* stError_,
		const string& sNameValuePairs_, const string& sExpression_, string* stResult_)
		: TestThreadFunction(debuggee_, stDebugSessionId_, stError_),
			transform(transform_), sNameValuePairs(sNameValuePairs_),
			sExpression(sExpression_), stResult(stResult_) {}

protected:
	void StartThreadImpl(DebugContext* pDebugContext) {
		pDebugContext->pThread = new boost::thread(*this);
	}

	void DoTest() {
		*stResult = transform->TestExpression(sNameValuePairs, sExpression, *stError);
		if ( stError->empty() ) *stError = transform->getLastError();
	}

private:
	TransformationEngine* transform;
	const string sNameValuePairs, sExpression;
	string* stResult;
};

class TestTransformationThreadFunction : public TestThreadFunction {
public:
	TestTransformationThreadFunction(TransformationEngine* transform_, Debuggee* debuggee_, const string& stDebugSessionId_,
		string* stError_, string* stWarning_, long lTranId_,
		const string& stSourceDataFileName_, const string& stTargetDataFileName_, const string& stLinkDataFileName_)
		: TestThreadFunction(debuggee_, stDebugSessionId_, stError_),
			transform(transform_), lTranId(lTranId_),
			stSourceDataFileName(stSourceDataFileName_), stTargetDataFileName(stTargetDataFileName_), stLinkDataFileName(stLinkDataFileName_),
			stWarnings(stWarning_) {}

protected:
	void StartThreadImpl(DebugContext* pDebugContext) {
		pDebugContext->pThread = new boost::thread(*this);
	}

	void DoTest() {
		const unsigned long hr =
			transform->TestTransformation(lTranId, stSourceDataFileName, stTargetDataFileName, stLinkDataFileName);
		if ( hr != S_OK ) *stError = transform->getLastError();
		*stWarnings = transform->getErrorsAndWarnings();
	}

private:
	TransformationEngine* transform;
	long lTranId;
	string stSourceDataFileName, stTargetDataFileName, stLinkDataFileName;
	string* stWarnings;
};

class TestOperationThreadFunction : public TestThreadFunction {
public:
	TestOperationThreadFunction(OperationEngine* opEngine_, OperationDataElementHolder* pDeHolder_, Debuggee* debuggee_, const string& stDebugSessionId_, string* stError_,
		long lOperationId_, int* piSourceType_, string* stWarnings_) :
		TestThreadFunction(debuggee_, stDebugSessionId_, stError_),
			opEngine(opEngine_), pDeHolder(pDeHolder_), lOperationId(lOperationId_),
			piSourceType(piSourceType_), stWarnings(stWarnings_)
		{}

protected:
	void StartThreadImpl(DebugContext* pDebugContext) {
		pDebugContext->pThread = new boost::thread(*this);
	}

	void DoTest() {
		opEngine->TestOperation(lOperationId, pDeHolder, *piSourceType, *stWarnings);
	}

private:
	OperationEngine *opEngine;
	OperationDataElementHolder* pDeHolder;
	long lOperationId;
	const string stTargetDataFileName;
	int* piSourceType;
	string* stWarnings;
};

string GetDebuggingSessionId(DebuggingParameters* pDebuggingParams) {	
	if( pDebuggingParams->getDebuggingSessionId() && *pDebuggingParams->getDebuggingSessionId() )
		return pDebuggingParams->getDebuggingSessionId();
	else 
		return GetGUIDString();
}

typedef pair<string, const DataElement*> StrDEPairT;

                                                                             
void filterDEMap(const map<string, DataElement*>& deMap_in, vector<StrDEPairT>& deVector) {
	for( map<string, DataElement*>::const_iterator cit = deMap_in.begin(); cit != deMap_in.end(); ++cit ) {
		const std::string& deName = cit->first;
		if ( deName.empty() )
			continue;
		if ( deName.size() > 1 && deName.substr(0, 2) == "__" )
			continue;

		deVector.push_back( StrDEPairT(deName, cit->second) );
	}
}

ArrayOfWsDataElement* CreateDataElementArrayImpl(const vector<StrDEPairT>& deVector, const std::set<DataElement*>& source_de);

ArrayOfWsDataElement* CreateDataElementArrayImpl(const vector<StrDEPairT>& deVector) {
	const std::set<DataElement*> dummy;
	return CreateDataElementArrayImpl(deVector, dummy);
}

WsDataElement* CreateWsDataElement(const DataElement* pDe, const string& name, const string& scope) {
	std::auto_ptr<WsDataElement> pWsDataElement( new WsDataElement() );
	pWsDataElement->setName(const_cast<axiscpp::xsd__string>(toBase64(name).c_str()), true);
	pWsDataElement->setType(const_cast<axiscpp::xsd__string>((const char*)pDe->getTypeStr()), true);
	
	pWsDataElement->setScope(const_cast<xsd__string>(scope.c_str()), true);

	int err = 0;
	if( pDe->isCollection() ) {
		pWsDataElement->setIsArray(true_);
		vector<StrDEPairT> deArray;
		const size_t size = pDe->getDECollectionSize();
		for( size_t iArr = 0; iArr < size; ++iArr ) {
			deArray.push_back( StrDEPairT(pDe->getName(), pDe->getDECollectionOf(iArr)) );
		}
		pWsDataElement->setArrValue( CreateDataElementArrayImpl(deArray) );
	}
	else {
		pWsDataElement->setIsArray(false_);
		err = 0;
		const string val( pDe->GetString(err) );
		if ( err == 0 && !pDe->IsNull() ) {
			pWsDataElement->setValue(const_cast<axiscpp::xsd__string>(toBase64(val).c_str()), true);
		}
	}
	return pWsDataElement.release();
}

const string DE_SCOPE_SOURCE = "SOURCE";
const string DE_SCOPE_GLOBAL = "GLOBAL";

ArrayOfWsDataElement* CreateDataElementArrayImpl(const vector<StrDEPairT>& deVector, const std::set<DataElement*>& source_de) {
	const size_t nDataElements = deVector.size() + source_de.size();
	if ( nDataElements == 0 ) return 0;

	WsDataElement** ppWsDataElement = allocate_pointer_array<WsDataElement>(nDataElements);
	AutoDeletePointerArray<WsDataElement> adopt_ppWsDataElement(ppWsDataElement, nDataElements);

	size_t iDE = 0;
	for( vector<StrDEPairT>::const_iterator deIt = deVector.begin(); deIt != deVector.end(); ++deIt) {
		const DataElement* const pDe = deIt->second;
		WsDataElement* pWsDataElement = CreateWsDataElement(pDe, deIt->first, DE_SCOPE_GLOBAL);
		ppWsDataElement[iDE++] = pWsDataElement;
	}

	std::set<DataElement*>::const_iterator srcDeIt = source_de.begin();
	for( ; srcDeIt != source_de.end(); ++srcDeIt ) {
		const DataElement* const pDe = *srcDeIt;
		WsDataElement* pWsDataElement = CreateWsDataElement(pDe, pDe->getName(), DE_SCOPE_SOURCE);
		ppWsDataElement[iDE++] = pWsDataElement;
	}

	std::auto_ptr<WsDataElement_Array> pWsDataElement_Array( new WsDataElement_Array() );
	pWsDataElement_Array->set(ppWsDataElement, nDataElements);

	auto_ptr<ArrayOfWsDataElement> pArrayOfWsDataElement( new ArrayOfWsDataElement() );
	pArrayOfWsDataElement->setitem(pWsDataElement_Array.get());

	return pArrayOfWsDataElement.release();
}

ArrayOfWsDataElement* CreateDataElementArray(const map<string, konga::DataElement*>& deMap_in, const std::set<DataElement*>& source_de) {
	vector<StrDEPairT> deVector;
	filterDEMap(deMap_in, deVector);
	return CreateDataElementArrayImpl(deVector, source_de);
}

template<typename T>
void SetDataElementsForTestResult(T& testResult, const OperationDataElementHolder& deHolder, const std::set<DataElement*>& source_de) {
	testResult.setDataElements( CreateDataElementArray(deHolder.getOperationDataElements(), source_de) );
}

BreakLocation* CreateBreakLocation(const konga::IntegrationBreakpointLocation* const pKongaBreakLocation) {
	if( !pKongaBreakLocation ) return 0;
	auto_ptr<BreakLocation> pAxisBreakLocation( new BreakLocation() );

	SmartDbPtr<DbConnection> pTranDB( konga::GetBackEndDbConnection("TranDb") );
	DbGuidToIdMapper guidMapper(pTranDB);
	const string& stOpGuid = guidMapper.GetGuid(pKongaBreakLocation->getOperationId(), OperationEntityId);
	const string& stTranGuid = guidMapper.GetGuid(pKongaBreakLocation->getTransformationId(), TransformationEntityId);
	const string& stScriptGuid = guidMapper.GetGuid(pKongaBreakLocation->getScriptId(), ScriptEntityId);

	pAxisBreakLocation->setOperationGuid(const_cast<axiscpp::xsd__string>(stOpGuid.c_str()), true);
	pAxisBreakLocation->setTransformationGuid(const_cast<axiscpp::xsd__string>(stTranGuid.c_str()), true);
	pAxisBreakLocation->setScriptGuid(const_cast<axiscpp::xsd__string>(stScriptGuid.c_str()), true);
	pAxisBreakLocation->setNodePath(const_cast<axiscpp::xsd__string>(pKongaBreakLocation->getAttributePath().c_str()), true);
	pAxisBreakLocation->setLineNumber(pKongaBreakLocation->getLineNumber());

	return pAxisBreakLocation.release();
}

                                                   
ArrayOfBreakLocation* CreateCallStack(const CallStack* const pCallStack) {
	if ( !pCallStack ) return 0;
	const vector<BreakpointLocation*>& vBreakLocations = pCallStack->getBreakpointLocations();
	const size_t nFrames = vBreakLocations.size();
	if ( nFrames == 0 ) return 0;

	BreakLocation** ppBreakLocation = allocate_pointer_array<BreakLocation>(nFrames);
	AutoDeletePointerArray<BreakLocation> adopt_ppBreakLocation(ppBreakLocation, nFrames);

	                                     
	for( size_t iserver = nFrames-1, iclient = 0; iclient < nFrames; --iserver, ++iclient ) {
		ppBreakLocation[iclient] = CreateBreakLocation( dynamic_cast<IntegrationBreakpointLocation*>(vBreakLocations.at(iserver)) );
	}

	std::auto_ptr<BreakLocation_Array> pBreakLocation_Array( new BreakLocation_Array() );
	pBreakLocation_Array->set(ppBreakLocation, nFrames);

	std::auto_ptr<ArrayOfBreakLocation> pStack( new ArrayOfBreakLocation() );
	pStack->setitem(pBreakLocation_Array.get());
	return pStack.release();
}

                                                                                               
DebugCommand InitializeDebuggingFlags(Debuggee* pDebuggee, DebuggingParameters* pDebuggingParams, OperationDataElementHolder* pDataElementHolder, bool setNull) {
	const DebugCommand command = GetDebugCommand(pDebuggingParams->getCommand());
	pDebuggee->shouldStep( command == DebugCommand_Step );
	pDebuggee->setObserveBreakpoints(pDebuggingParams->getObserveBreakpoints() == axiscpp::true_);
	pDebuggee->breakOnError(pDebuggingParams->getBreakOnError() == axiscpp::true_);
	pDebuggee->breakOnWarning(pDebuggingParams->getBreakOnWarning() == axiscpp::true_);

	                            
	ArrayOfWsDataElement* pArrayOfWsDataElement = pDebuggingParams->getGlobalVariables();
	if( pArrayOfWsDataElement ) {
		WsDataElement_Array* pWsDataElement_Array = pArrayOfWsDataElement->getitem();
		if( pWsDataElement_Array ) {
			int nDataElements = 0;
			WsDataElement** ppWsDataElement = pWsDataElement_Array->get(nDataElements);
			for(int i=0; i<nDataElements; ++i) {
				WsDataElement* const pWsDataElement = ppWsDataElement[i];
				if ( pWsDataElement == 0 ) continue;

				string sDEName(pWsDataElement->getName() ? pWsDataElement->getName() : "");
				if ( sDEName.empty() ) continue;
				sDEName = fromBase64(sDEName);

				const string stScope(pWsDataElement->getScope() ? pWsDataElement->getScope() : "");
				if ( !stScope.empty() && !konga::EqualsIgnoreCase(stScope, DE_SCOPE_GLOBAL) ) continue;

				DataElement* pDe = pDataElementHolder->getOperationDataElement(sDEName);
				if( pWsDataElement->getValue() == 0 ) {
					                                        
					if(setNull)pDe->setNull();
				} else if( pWsDataElement->getIsArray() ) {
					                         
					                                                                                                              
					                                               
				} else {
					const DataElement::Type type( DataElement::get_type(pWsDataElement->getType(), 0) );
					if ( type != DataElement::t_unknown ) pDe->setType(type);
					const string sDEValue( fromBase64(pWsDataElement->getValue()) );
					pDe->setValue(sDEValue.c_str());
				}
			}
		}
	}
	return command;
}

void ValidateCommand(const string& stDebugSessionGuid,
				 DebugCommand command,
				 DebugCommand validCommand,
				 DebugCommand invalidCommand)
{
	if ( command == DebugCommand_Unknown ) {
		StopDebugSession(stDebugSessionGuid);
		throw CrException("Client Error: Unknown command. Session has been terminated.");	                                                                       
	}

	if ( validCommand != DebugCommand_Unknown && command != validCommand ) {
		StopDebugSession(stDebugSessionGuid);
		throw CrException("Client Error: Invalid command (" + ToString(command)
		+ ") for this context. The only valid command is " + ToString(validCommand)
		+ ". Session has been terminated.");
	}

	if ( invalidCommand != DebugCommand_Unknown && command == invalidCommand ) {
		StopDebugSession(stDebugSessionGuid);
		throw CrException("Invalid command (" + ToString(command) + ") for this context. Session has been terminated.");
	}
}

WsKeyValuePairs* CreateWsKeyValuePairs(const KeyValues& props) {
	const int nItems = props.GetSize();
	assert(nItems > 0);

	WsKeyValue** arrWsKeyValue = allocate_pointer_array<WsKeyValue>(nItems);
	AutoDeletePointerArray<WsKeyValue> adopt_arrWsKeyValue(arrWsKeyValue, nItems);

	int i = 0;
	for( KeyValues::const_iterator cit = props.begin(); cit != props.end(); ++cit ) {
		WsKeyValue* const kv = new WsKeyValue();
		const string& key = cit->first, value = cit->second;
		kv->setkey(const_cast<xsd__string>(key.c_str()), true);
		kv->setvalue(const_cast<xsd__string>(value.c_str()), true);
		arrWsKeyValue[i++] = kv;
	}

	std::auto_ptr<WsKeyValue_Array> pInValue( new WsKeyValue_Array() );
	pInValue->set(arrWsKeyValue, nItems);

	std::auto_ptr<ArrayOf_tns2_WsKeyValue> InValue( new ArrayOf_tns2_WsKeyValue() );
	InValue->setitem(pInValue.get());

	std::auto_ptr<WsKeyValuePairs> wsKeyValuePairs( new WsKeyValuePairs() );
	wsKeyValuePairs->setkeyValues(InValue.release());
	return wsKeyValuePairs.release();
}

template<typename T>
void SetTestResultProperties(T& result, const IntegrationBreakpointLocation& breakLocation) {
	KeyValues props;
	breakLocation.getProperties(props);
	if ( props.GetSize() == 0 ) return;
	result.setGeneralKeyValues(CreateWsKeyValuePairs(props));
}

}                       

namespace {
	struct DataFileInfoStruct {
		DataFileInfoStruct() : type(TransformationDataStoreInstanceType_INVALID), iNumberOfChunks(0) {}

		                                                 
		DataFileInfoStruct(long OperationId_, long TransformationId_, TransformationDataStoreInstanceType t) : 
		OperationId(OperationId_), TransformationId(TransformationId_), type(t), iNumberOfChunks(1) {}

		long OperationId, TransformationId;
		TransformationDataStoreInstanceType type;
		int iNumberOfChunks;
	};

	class DataFileInfoStructMapKey {
	public:
		DataFileInfoStructMapKey(long OperationId_, long TransformationId_, TransformationDataStoreInstanceType type_) :
		  OperationId(OperationId_), TransformationId(TransformationId_), type(type_) {}

		  bool operator <(const DataFileInfoStructMapKey& rhs) const {
			  if ( this->OperationId != rhs.OperationId ) {
				  return this->OperationId < rhs.OperationId;
			  }
			  if ( this->TransformationId != rhs.TransformationId ) {
				  return this->TransformationId < rhs.TransformationId;
			  }
			  return this->type < rhs.type;
		  }

	private:
		long OperationId, TransformationId;
		TransformationDataStoreInstanceType type;
	};

	typedef map<DataFileInfoStructMapKey, DataFileInfoStruct> DataFileInfoStructMapT;

	void UpdateDataFileInfoStructMap(DataFileInfoStructMapT& m, const TransformationDataStoreInstance& file, TransformationDataStoreInstanceType type) {
		const DataFileInfoStructMapKey key(file.operationId, file.transformationId, type);
		DataFileInfoStructMapT::iterator it = m.find(key);
		if ( it != m.end() ) {
			++(it->second.iNumberOfChunks);
		}
		else {
			m.insert( DataFileInfoStructMapT::value_type(key, DataFileInfoStruct(file.operationId, file.transformationId, type) ) );
		}
	}

	WsDataFileInfo* CreateWsDataFileInfo(const DataFileInfoStruct& s, GuidToIdMapper& guidToIdMapper) {
		std::auto_ptr<WsDataFileInfo> pWsDataFileInfo( new WsDataFileInfo() );

		const string& stOperationGuid = guidToIdMapper.GetGuid(s.OperationId, OperationEntityId);
		pWsDataFileInfo->setOperationGuid(const_cast<xsd__string>(stOperationGuid.c_str()), true);
		
		const string& stTransformationGuid = guidToIdMapper.GetGuid(s.TransformationId, TransformationEntityId);
		pWsDataFileInfo->setTransformationGuid(const_cast<xsd__string>(stTransformationGuid.c_str()), true);

		pWsDataFileInfo->setType(static_cast<int>(s.type));
		pWsDataFileInfo->setNumberOfChunks(s.iNumberOfChunks);

		return pWsDataFileInfo.release();
	}

	void SetDataFileInfo(WsTestOperationResult& pWsTestOperationResult, const string& stOperationChainInstanceGuid) {
		TransformationDataStoreInstance filter;
		filter.GUID = stOperationChainInstanceGuid;

		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		TransformationDataStore dataStore(pTranDb);
		const vector<TransformationDataStoreInstance>& files = dataStore.GetFileList(filter);
		if ( files.empty() ) {
			throw InternalErrorException("No source or target files were made available for this operation.");
		}

		DbGuidToIdMapper guidToIdMapper__db(pTranDb);
		CachedGuidToIdMapper guidToIdMapper(guidToIdMapper__db);
		DataFileInfoStructMapT m;
		vector<TransformationDataStoreInstance>::const_iterator fileIt = files.begin();
		for( ; fileIt != files.end(); ++fileIt ) {
			const TransformationDataStoreInstance& file = *fileIt;
			if ( file.chunkNumber == 0 ) continue;	                                                    

			if ( !file.sourceFile.empty() ) {
				UpdateDataFileInfoStructMap(m, file, TransformationDataStoreInstanceType_SOURCE);
			}
			if ( !file.targetFile.empty() ) {
				UpdateDataFileInfoStructMap(m, file, TransformationDataStoreInstanceType_TARGET);
			}
			if ( !file.linkFile.empty() ) {
				UpdateDataFileInfoStructMap(m, file, TransformationDataStoreInstanceType_LINK);
			}
		}
		std::auto_ptr<WsDataFilesInfo> pWsDataFilesInfo( new WsDataFilesInfo() );
		pWsDataFilesInfo->setGuid(const_cast<xsd__string>(stOperationChainInstanceGuid.c_str()), true);

		std::auto_ptr<ArrayOfWsDataFileInfo> pArrayOfWsDataFileInfo( new ArrayOfWsDataFileInfo() );

		const int size = m.size();
		if ( size > 0 ) {
			WsDataFileInfo** arrWsDataFileInfo = allocate_pointer_array<WsDataFileInfo>(size);
			AutoDeletePointerArray<WsDataFileInfo> adopt_arrWsDataFileInfo(arrWsDataFileInfo, size);
			DataFileInfoStructMapT::const_iterator cit = m.begin();
			for( int i = 0; i < size; ++i, ++cit ) {
				const DataFileInfoStruct& s = cit->second;
				arrWsDataFileInfo[i] = CreateWsDataFileInfo(s, guidToIdMapper);
			}

			std::auto_ptr<WsDataFileInfo_Array> pWsDataFileInfo_Array(new WsDataFileInfo_Array());
			pWsDataFileInfo_Array->set(arrWsDataFileInfo, size);

			pArrayOfWsDataFileInfo->setitem(pWsDataFileInfo_Array.get());
		}
		pWsDataFilesInfo->setDataFiles(pArrayOfWsDataFileInfo.release());	                          

		pWsTestOperationResult.setDataFileInfo(pWsDataFilesInfo.release());	                         
	}
}

WsTestOperationResult* TreeMapperInfoProviderImpl::testOperation(const char* OperationGuid, const char* TranGuid, DebuggingParameters* pDebuggingParams) {
	                                                                                                                       
	Debuggee* pDebuggee = 0;
	DebugContext* pDebugContext = 0;
	const string& stDebugSessionGuid = GetDebuggingSessionId(pDebuggingParams);

	{
		boost::mutex::scoped_lock lock(g_debuggerThreadsMutex);
		map<string, DebugContext*>::iterator itCtx = g_debuggerThreads.find(stDebugSessionGuid);
		if( itCtx == g_debuggerThreads.end() ) {
			if ( OperationGuid == 0 || *OperationGuid == 0 ) {
				throw CrException("Client Error: The Operation GUID is NULL. A non empty Operation GUID has to be provided.");
			}

			const long lOperationId=GetItemIdForGuid(OperationGuid, OperationEntityId, false, GuidToIdMapper::invalid_id);
			if ( lOperationId == GuidToIdMapper::invalid_id ) {
				throw CrException("The operation has not been deployed to this server.");
			}

			UIBACKEND_TRACE("Test Operation: Started new debug session: " + stDebugSessionGuid);
			pDebugContext = new DebugContext(m_UserInfo, pDebuggee = new Debuggee());
			pDebugContext->pOperationEngine = new OperationEngine(pDebugContext->userInfo, pDebuggee);
			pDebugContext->pDeHolder = new DefaultOperationDataElementHolder(pDebugContext->userInfo);
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, pDebugContext->pDeHolder, false);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Run, DebugCommand_Unknown);

			TestOperationThreadFunction testOpFunc(pDebugContext->pOperationEngine, pDebugContext->pDeHolder, pDebuggee, stDebugSessionGuid, &pDebugContext->sError,
				lOperationId, &pDebugContext->iSourceType, &pDebugContext->sWarnings);
			testOpFunc.Start(pDebugContext);
		} else {
			UIBACKEND_TRACE("Test Operation: Continue old debug session: " + stDebugSessionGuid);
			pDebugContext = itCtx->second;
			pDebuggee = pDebugContext->pDebuggee;
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, &pDebugContext->pOperationEngine->getOperationDataElementHolder(), true);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Unknown, DebugCommand_Run);

			pDebuggee->DebugContinue();
		}
		assert(pDebugContext);
		assert(pDebuggee);
	}

	IntegrationBreakpointLocation* pBreakLocation = static_cast<IntegrationBreakpointLocation*>(pDebuggee->WaitForDebugBreak());

	                              
	std::auto_ptr<WsTestOperationResult> pTestResult( new WsTestOperationResult() );
	std::set<DataElement*> source_de;
	if(pBreakLocation)pBreakLocation->getSourceDataElements(source_de);
	SetDataElementsForTestResult<WsTestOperationResult>(*pTestResult, pDebugContext->pOperationEngine->getOperationDataElementHolder(), source_de);

	if( pBreakLocation ) {
		pTestResult->setCallStack( CreateCallStack(&pDebuggee->getCallStack()) );
		pTestResult->setDebuggingSessionId(const_cast<axiscpp::xsd__string>(stDebugSessionGuid.c_str()), true);
		pTestResult->setIntermediateResult(const_cast<axiscpp::xsd__string>(pBreakLocation->getResult().c_str()), true);

		SetTestResultProperties<WsTestOperationResult>(*pTestResult, *pBreakLocation);

		pDebuggee->DebugBreakpointReset();
	} else {
		AutoRemoveDebugSession remove_session(stDebugSessionGuid);
		if( !pDebugContext->getError().empty() ) throw CrException(pDebugContext->getError());

		string stWarnings = pDebugContext->getWarnings();
		EscapeUnprintable(stWarnings);
		pTestResult->setWarnings(const_cast<xsd__string>(stWarnings.c_str()), true);

		SetDataFileInfo(*pTestResult, pDebugContext->pOperationEngine->getOperationChainInstanceGuid());
	}

	return pTestResult.release();
}

WsTestTransformationResult* TreeMapperInfoProviderImpl::testTransformation(
	const char* const pTranGuid,
	const char* const pLoadedSourceDataGuid,
	const char* const pZippedBase64EncodedData,
	DebuggingParameters* pDebuggingParams)
{
	FILE*debug_log=getDebugLogFile("testTransformation_" + GetGUIDString() + ".log");
	AutoFileHandle adopt_debug_log(debug_log);
	if(debug_log!=0) { fprintf(debug_log,"Entering TestTransformation(...)\n"); fflush(debug_log); }
	const string stTranGuid(toString(pTranGuid));

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	DbGuidToIdMapper guidMapper(pTranDb);
	const long lTranId = guidMapper.GetId(stTranGuid, TransformationEntityId);
	if ( lTranId == GuidToIdMapper::invalid_id ) {
		throw CrException("The transformation has not been deployed to this server.");
	}

	UserAccess accessControl = UserAccess::GetUserAccessWithGroup(m_UserInfo.GetId(), *pTranDb);
	if ( !accessControl.HasExecutePermission(lTranId, TransformationEntityId) ) {
		throw CrException("The current user does not have execute permission for this transformation.");
	}

	TransformationEngine* pTransformationEngine = 0;
	Debuggee* pDebuggee = 0;
	DebugContext* pDebugContext = 0;
	const string& stDebugSessionGuid = GetDebuggingSessionId(pDebuggingParams);

	{
		boost::mutex::scoped_lock lock(g_debuggerThreadsMutex);
		map<string, DebugContext*>::iterator itCtx = g_debuggerThreads.find(stDebugSessionGuid);
		if( itCtx == g_debuggerThreads.end() ) {
			                                                                              
			const string stLoadedSourceDataGuid(toString(pLoadedSourceDataGuid));
			pDebugContext = new DebugContext(m_UserInfo, pDebuggee = new Debuggee());
			DefaultOperationContext* ptmp = new DefaultOperationContext(pDebugContext->userInfo, pDebuggee, -1);
			if ( !stLoadedSourceDataGuid.empty() ) ptmp->setOperationChainInstanceGuid(stLoadedSourceDataGuid);
			pDebugContext->pOperationContext = ptmp;
			pDebugContext->pTransformationEngine = pTransformationEngine = new TransformationEngine(pDebugContext->userInfo, pDebugContext->pOperationContext);
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, pTransformationEngine->getOperationDataElementHolder(), false);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Run, DebugCommand_Unknown);

			                       
			string stData;
			bool bHasSourceData = !stLoadedSourceDataGuid.empty();
			try {
				if ( bHasSourceData ) {
					TransformationDataStoreInstance filter;
					filter.GUID = stLoadedSourceDataGuid;
					filter.transformationId = lTranId;
					filter.originalSourceNumber = 1;
					filter.chunkNumber = 1;
					
					TransformationDataStore dataStore(pTranDb);
					vector<TransformationDataStoreInstance> files = dataStore.GetFileList(filter);
					if ( files.empty() ) {
						bHasSourceData = false;
					}
					else {
						const TransformationDataStoreInstance& sourceFile = *files.begin();

						if ( konga::NonEmptyFileExists(sourceFile.sourceFile) ) {
							konga::read_file_to_string(sourceFile.sourceFile, stData, true);
						}

						if(debug_log!=0){ fprintf(debug_log,"testTransformation(tranId=%ld, dtData=\"%s\"\n",lTranId, stData.c_str()); fflush(debug_log); }
					}
				}
			}
			catch( const CrException& ex ) {
				const string stErr("Failed to retrieve previously loaded source data." \
					" Possible reasons for this could be that the source was loaded a long time ago and has been deleted from the server" \
					" or it was loaded on a different server." \
					" Please try to load the source again. If the problem persists, please contact ETLForge Support." \
					" Underlying error causing this problem:\n" + ex.GetErrorString());
				throw CrException(stErr);
			}

			pTranDb.CloseAndDelete();

			if ( bHasSourceData ) {
				copyDataToTmpFileName(lTranId, stData, pDebugContext->stSourceDataFileName, stLoadedSourceDataGuid, pDebugContext->filesToDelete, ".source");
			}
			createTmpFileName(pDebugContext->stLinkDataFileName, stLoadedSourceDataGuid, pDebugContext->filesToDelete, ".link");
			createTmpFileName(pDebugContext->stTargetDataFileName, stLoadedSourceDataGuid, pDebugContext->filesToDelete, ".target");

			TestTransformationThreadFunction testTranFunc(pTransformationEngine, pDebuggee, stDebugSessionGuid, &pDebugContext->sError, &pDebugContext->sWarnings,
				lTranId, pDebugContext->stSourceDataFileName, pDebugContext->stTargetDataFileName, pDebugContext->stLinkDataFileName);
			testTranFunc.Start(pDebugContext);
		} else {
			pTranDb.CloseAndDelete();
			pDebugContext = itCtx->second;
			pTransformationEngine = pDebugContext->pTransformationEngine;
			pDebuggee = pDebugContext->pDebuggee;
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, pTransformationEngine->getOperationDataElementHolder(), true);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Unknown, DebugCommand_Run);

			pDebuggee->DebugContinue();
		}
		assert(pDebugContext);
		assert(pTransformationEngine);
		assert(pDebuggee);
	}

	IntegrationBreakpointLocation* pBreakLocation = static_cast<IntegrationBreakpointLocation*>(pDebuggee->WaitForDebugBreak());

	                              
	try {
		                                                       
                                                                  

		std::auto_ptr<WsTestTransformationResult> pTestResult( new WsTestTransformationResult() );

		std::set<DataElement*> source_de;
		if(pBreakLocation)pBreakLocation->getSourceDataElements(source_de);

		SetDataElementsForTestResult<WsTestTransformationResult>(*pTestResult, *pTransformationEngine->getOperationDataElementHolder(), source_de);

		if( pBreakLocation ) {
			pTestResult->setCallStack( CreateCallStack(&pDebuggee->getCallStack()) );
			pTestResult->setDebuggingSessionId(const_cast<axiscpp::xsd__string>(stDebugSessionGuid.c_str()), true);
			pTestResult->setIntermediateResult(const_cast<axiscpp::xsd__string>(pBreakLocation->getResult().c_str()), true);

			SetTestResultProperties<WsTestTransformationResult>(*pTestResult, *pBreakLocation);

                                                                                          

			pDebuggee->DebugBreakpointReset();
		} else {
			AutoRemoveDebugSession remove_session(stDebugSessionGuid);
			if( !pDebugContext->getError().empty() ) throw CrException(pDebugContext->getError());

			string stWarnings( pDebugContext->getWarnings() );
			EscapeUnprintable(stWarnings);
			pTestResult->setWarnings(const_cast<xsd__string>(stWarnings.c_str()), true);
			pTestResult->setDataFileGuid(const_cast<xsd__string>(pDebugContext->pOperationContext->getOperationChainInstanceGuid().c_str()));
			
			pTestResult->setTargetFile( CreateWsDataFile(pDebugContext->stTargetDataFileName) );
			pTestResult->setLinkFile( CreateWsDataFile(pDebugContext->stLinkDataFileName) );
		}

		return pTestResult.release();
	}
	catch( const exception& ) {
		if(debug_log!=0){ fprintf(debug_log,"Test Transformation failed to create response\n"); fflush(debug_log); }
		throw CrException("Give a sensible error message.... if any...");
	}
}

WsTestResult* TreeMapperInfoProviderImpl::testExpression(const string& sNameValuePairs_b64, const string& sExpression_b64, DebuggingParameters* pDebuggingParams) {
	FILE*debug_log=0;

	const string sExpression(fromBase64(sExpression_b64));
	const string sNameValuePairs(fromBase64(sNameValuePairs_b64));

	TransformationEngine* pTransform = 0;
	Debuggee* pDebuggee = 0;
	DebugContext* pDebugContext = 0;
	const string& stDebugSessionGuid = GetDebuggingSessionId(pDebuggingParams);

	{
		boost::mutex::scoped_lock lock(g_debuggerThreadsMutex);
		map<string, DebugContext*>::iterator itCtx = g_debuggerThreads.find(stDebugSessionGuid);
		if( itCtx == g_debuggerThreads.end() ) {
			pDebugContext = new DebugContext(m_UserInfo, pDebuggee = new Debuggee());
			pDebugContext->pOperationContext = new DefaultOperationContext(pDebugContext->userInfo, pDebuggee, -1);
			pDebugContext->pTransformationEngine = pTransform = new TransformationEngine(pDebugContext->userInfo, pDebugContext->pOperationContext, 0);
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, pTransform->getOperationDataElementHolder(), false);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Run, DebugCommand_Unknown);

			TestExpressionThreadFunction testExprFunc(pTransform, pDebuggee, stDebugSessionGuid, &pDebugContext->sError,
				sNameValuePairs, sExpression, &pDebugContext->sResult);
			testExprFunc.Start(pDebugContext);
		} else {
			pDebugContext = itCtx->second;
			pTransform = itCtx->second->pTransformationEngine;
			pDebuggee = itCtx->second->pDebuggee;
			DebugCommand command = InitializeDebuggingFlags(pDebuggee, pDebuggingParams, pTransform->getOperationDataElementHolder(), true);
			ValidateCommand(stDebugSessionGuid, command, DebugCommand_Unknown, DebugCommand_Run);
			pDebuggee->DebugContinue();
		}
		debug_log=pTransform->getDebugLog();
		assert(pTransform);
		assert(pDebuggee);
		assert(pDebugContext);
	}

	IntegrationBreakpointLocation* pBreakLocation = static_cast<IntegrationBreakpointLocation*>(pDebuggee->WaitForDebugBreak());
	try {

		std::auto_ptr<WsTestResult> pTestResult( new WsTestResult() );
		std::set<DataElement*> source_de;
		if(pBreakLocation)pBreakLocation->getSourceDataElements(source_de);
		SetDataElementsForTestResult<WsTestResult>(*pTestResult, *pTransform->getOperationDataElementHolder(), source_de);

		string stErrMsg, stResult;
		if( pBreakLocation ) {
			stResult = pBreakLocation->toString();
			stErrMsg = pDebugContext->getError();
			pTestResult->setCallStack( CreateCallStack(&pDebuggee->getCallStack()) );
			pTestResult->setDebuggingSessionId(const_cast<axiscpp::xsd__string>(stDebugSessionGuid.c_str()), true);

			pDebuggee->DebugBreakpointReset();
		} else {
			stErrMsg = pDebugContext->sError;
			stResult = pDebugContext->sResult;
			RemoveDebugSession(stDebugSessionGuid);
			pDebugContext = 0;
			pTransform = 0;
		}

		EscapeUnprintable(stErrMsg);

		stResult = toBase64(stResult);

		if(debug_log){ fprintf(debug_log,"result=\"%s\"\nerror=\"%s\"",stResult.c_str(), stErrMsg.c_str()); fflush(debug_log); }

		pTestResult->setResult(const_cast<axiscpp::xsd__string>(stResult.c_str()), true);
		pTestResult->setErrors(const_cast<axiscpp::xsd__string>(stErrMsg.c_str()), true);

		return pTestResult.release();
	}
	catch( const exception& e) {
		std::string sErr("Test Expression failed with unexpected std::exception, name_value_pairs = \""
			+ sNameValuePairs + "\", expression= \""+sExpression+"\". ");
		const std::string& sLastErr = pTransform->getLastError();
		const string sWhat(e.what());
		if ( sLastErr.empty() || strstr(sLastErr.c_str(), sWhat.c_str()) == 0 ) {
			sErr += "\n" + sWhat;
		}
		if(debug_log){ fprintf(debug_log,"sErr=\"%s\"\n",sErr.c_str()); fflush(debug_log); }

		RemoveDebugSession(stDebugSessionGuid);
		throw CrException(sErr);
	}
	catch(...) {
		                                                              
		RemoveDebugSession(stDebugSessionGuid);
		throw;
	}

}

void TreeMapperInfoProviderImpl::stop(const string& sDebugSessionId) {
	StopDebugSessionSync(sDebugSessionId);
	UIBACKEND_TRACE("Stopped debug session: " + sDebugSessionId);
}

