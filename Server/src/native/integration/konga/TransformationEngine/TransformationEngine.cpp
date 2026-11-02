/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>		              
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/Token.h>

#include <konga/Server/WDILogFileUtils.h>

#include <konga/ItemProperties/Definitions.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/SystemVariables/SystemVariables.h>	                       

#include <konga/TransformationDataStore/TransformationDataStore.h>

#include <konga/TransformationUtils/DbLookupCache.h>
#include <konga/TransformationUtils/LdapExecutor.h>

#include "DETransform.h"
#include "TreeMapper.h"
#include "RunScript.h"
#include "ErrorCodes/resource.h"
#include "TransformationEngine.h"

using namespace konga;
using std::map;
using std::string;

TransformationEngine::TransformationEngine(const UserInfo& currentUser, konga::OperationContext* context, konga::OperationDataElementHolder* deHolder) :
	m_CurrentUser(currentUser),
	m_hr(S_OK),
	m_bGlobalDataElementInitialized(false),
	m_TransformationID(0),
	m_originalSourceNumber(0),
	m_nTransformation(0),
	m_nChunkSize(0),
	m_OperationType(-1),
	m_bTrace(false),
	m_bHostedWebServiceOperation(false),
	m_bChunking(false),
	m_pTransformationChain(0),
	m_sourceType(UnknownStructureType),
	m_targetType(UnknownStructureType),
	m_nChunk(0), m_iChunk_next(1),
	m_bTestOperation(false), m_bTestTransformation(false),
	m_pSoapFault(0),
	m_log_guid(GetGUIDString()),
	m_debug_log(0),
	m_cacheLookup_CTransform(0),
	m_pProfilingKit(0),
	m_pTransformationDataHolder(0),
	m_bWriteLog(false),
	m_chunking_MAX_THREADS(0),
	m_pLdap_exec(0), m_pClassFilter(0)
{
	m_ErrorReporter.setTransformationEngine(this);

	const bool bBinaryTarget=konga::GetOptionAsBoolean("TransformationEngine","TargetWriteModeBinary",false);
	m_TargetWriteMode=bBinaryTarget?BINARY:ASCII;

	m_pTransformationDataHolder = new TransformationDataHolder(m_CurrentUser,
		context!=0 ? context : 0, context!=0 ? NCT_REFERENCE : NCT_CREATE,
		deHolder!=0 ? deHolder : 0, deHolder!=0 ? CT_REFERENCE : CT_CREATE,
		0, 0, CT_CREATE,
		0, CT_CREATE, 0, NCT_CREATE,
		0, NCT_CREATE, 0, NCT_CREATE);
	m_bWriteLog=WDILogFileUtils::GenerateLogFile();
}

TransformationEngine::~TransformationEngine()
{
	{                                
		PROFILING doPrf(this, "TransformationEngineDtor");

		delete m_pSoapFault;
		m_pSoapFault = 0;

		this->closeDbLookup();	                                             
		deleteClonedTreeMappers();

		delete m_pTransformationDataHolder;
		m_pTransformationDataHolder = 0;

		delete m_pTransformationChain;
		m_pTransformationChain = 0;
		
		if(m_pLdap_exec)delete m_pLdap_exec;
		m_pLdap_exec=0;
	}

	                                       
	delete m_pProfilingKit;
	m_pProfilingKit = 0;
}

std::string TransformationEngineErrorReporter::getLastError() const {
	if ( m_pTransformationEngine )
		return m_pTransformationEngine->getLastError();
	return std::string();
}

std::string TransformationEngineErrorReporter::getError() const {
	if ( m_pTransformationEngine ) {
		CViewLog* pViewLog = m_pTransformationEngine->getViewLog();
		if ( pViewLog ) {
			std::string stWarnings;
			pViewLog->GetLogContents(stWarnings);
			return stWarnings;
		}
	}
	return std::string();
}

void TransformationEngine::initPROFILING(const std::string& prefix, int id)
{
	initPROFILING(prefix.c_str(), id);
}

void TransformationEngine::initPROFILING(const char*prefix, int id)
{
	if(m_pProfilingKit)return;
	const bool bDefault = konga::GetOptionAsBoolean("TransformationEngine", "TimingOn", false);
	bool bTimingOn = konga::GetSystemVariableAsBool("etlforge.transformation.timing_on", getOperationDataElementHolder(), bDefault);
	if(!bTimingOn)return;
	string stFilePath(konga::WDILogFileUtils::getWDILogDir() + "/" + prefix);
	if(id>0)stFilePath+=("_"+konga::toString(id));
	stFilePath+=("_"+konga::GetGUIDString().substr(0,8) + ".prf.xml");
	m_pProfilingKit=new ProfilingKit(stFilePath);
}

void TransformationEngine::enterProfilingSection(const string& sWhere, const string& sExtra)
{
	if(m_pProfilingKit==0)return;
	m_pProfilingKit->start(sWhere, sExtra);
}

void TransformationEngine::exitProfilingSection(const string& sWhere)
{
	if(m_pProfilingKit==0)return;
	m_pProfilingKit->end(sWhere);
}

namespace {
	const char cDeDelimiter='&';                                                                            
}

DataElement* TransformationEngine::getGlobalDataElement(const char* tag)
{
	return getOperationDataElementHolder()->getOperationDataElement(tag);
}


void addToSourceCromTable(CROM*node,std::map<std::string,CROM*>&table, bool bRecursive)
{
	if(node->IsLeaf()){
                                                                                                                          
                                     
		{
			table[(const char*)node->get_Path()]=node;
		}
	}
	if(bRecursive){
		for(CROM*child=node->GetFirstChild();child;child=child->GetNextSibling()){
			addToSourceCromTable(child,table,bRecursive);
		}
	}
}

TransformationStatistics TransformationEngine::getTransformationStatistics() const {
	return m_statistics;
}

std::string TransformationEngine::getLastError() const {
	if ( !m_lastErr.IsEmpty() )
		return (const char*)m_lastErr;

	return getViewLog()->GetLastLogMsg();
}

std::string TransformationEngine::getErrorsAndWarnings() {
	string stLastError( this->getLastError() );

	CViewLog* viewLog = getViewLog();
	string stLogContents;
	viewLog->GetLogContents(stLogContents);

	                                   
	if ( stLogContents.find(stLastError) != string::npos )
		stLastError.clear();
	else if ( stLastError.find(stLogContents) != string::npos )
		stLogContents.clear();


	string retVal(stLastError);

	if ( !stLogContents.empty() ) {
		if ( !retVal.empty() ) {
			retVal += "\n";
		}
		retVal += stLogContents;
	}
	return retVal;
}


std::string CTransform::updateFlatDatabaseExpr(const char*strTemplate){
	int err=0;
	int i0=0,i1=0,i2=0;
	const char *t;
	if(strTemplate==0){
		return "";
	}
	std::string str;
	while((t=strstr(&strTemplate[i0],TRANS))!=0){
		i2=find_END_KEY_position(strTemplate, i0);
		if(i2>=0){
			i1=(int)(t-strTemplate);                                    
			i2+=strlen(END_TRANS);                                   
			std::string str0(&strTemplate[i0],i1-i0);                                         
			std::string str1=updateTokenList(&strTemplate[i1],i2-i1);                                                                           
			str+=str0+str1;
			i0=i2;
			if(err)break;
		} else {
			i1=i0, i2=(int)strlen(strTemplate);
			throw konga::CrException(std::string("Update flat db expression failed, cannot find the matching closing tag </trans>. expr=\"")+
				strTemplate+"\". The processed string is \""+str+"\"");
		}
	}
	if(i0 < (int)strlen(strTemplate)){
		str+=&strTemplate[i0];
	}
	return str;
}

std::string CTransform::updateTokenList(const char*str0, int n){
                                                    
	if(str0[0]==0)return 0;
	bool isAtStart=true, hasValidLHS=false;
	int n1=(int)strlen(TRANS), n2=(int)strlen(END_TRANS);
	if(strncmp(str0,TRANS,n1)!=0){
		throw konga::CrException(std::string("Update token list failed, the expression should start with ")+TRANS+". The string is \""+
			str0+"\"");
	}
	if(n2>n||strncmp(&str0[n-n2],END_TRANS,n2)!=0){
		throw konga::CrException(std::string("Update token list failed, the expression should end with ")+END_TRANS+". The string is \""+
			str0+"\"");
	}
	int err=0;
	std::string str(TRANS);
	int i=n1;
	n=n-n2;
	while(i<n){
		Token*token=new Token(str0,i,isAtStart,hasValidLHS, konga::Token::fnc, source_de_set,isFlatSource,err);
		konga::Token::Type type=token->getType();
		if(type==Token::IDE){
			std::string name(token->GetName());
			int k=(int)name.find('.');
			name=name.substr(k+1);
			str+=name;
		}
		else {
			str+=token->GetName();
		}
		delete token;
		if(type!=Token::WhiteSpaces&&type!=Token::Comment){
			hasValidLHS=(type==Token::Set||type==Token::GlobalDE);
			isAtStart=type==Token::Unknown||type==Token::Set||
				type==Token::LP||type==Token::Func||type==Token::And||
				type==Token::Or||type==Token::NE||type==Token::EQ||
				type==Token::GT||type==Token::GE||type==Token::LT||
				type==Token::LE||type==Token::Comma||type==Token::Semicolon;
		}
	}
	str+=END_TRANS;

	return str;
}

void TransformationEngine::closeDbLookup(){
	PROFILING doPrf(this, "CloseDbLookup");
	try {
		m_pTransformationDataHolder->getDbLookupCache()->closeAll();
		                                 
		for( std::vector<konga::TransformationDataHolder*>::const_iterator cit =  m_vpTransformationDataHolder.begin();
			cit != m_vpTransformationDataHolder.end(); ++cit )
		{
			TransformationDataHolder* p = *cit;
			if ( p ) {
				DbLookupCache* p2 = p->getDbLookupCache();
				if ( p2 ) p2->closeAll();
			}
		}
	}
	catch( const CrException& ex ) {
		                                 
		getViewLog()->LogMessage("Failed to close connections used for DbLookup: %s", ex.GetErrorString().c_str());
	}
	catch( ... ) {
		                                 
		getViewLog()->LogMessage("Failed to close connections used for DbLookup: Unknown exception (should never happen!)");
	}
}

namespace {
	const char wbplus[]="wb+";
	const char wplus[]="w+";
}

                
const char* TransformationEngine::getWriteModeStr(TargetWriteMode wm) {
	return wm==BINARY?wbplus:wplus;
}

const char* TransformationEngine::getWriteModeStr() const {
	return getWriteModeStr(m_TargetWriteMode);
}

void TransformationEngine::AppendErrorMessage(const KString& csError)
{
	if ( csError.IsEmpty() )
		return;

	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	if( !m_lastErr.IsEmpty() ) {
		if ( m_lastErr.Find(csError) >= 0 ) return;
		m_lastErr += "\n";
	}
	m_lastErr += csError;
}

unsigned long TransformationEngine::CreateJtrFromMappings(long lTransformationID, const string& stSourceJtrFN, const string& stTargetJtrFN) {
	TreeMapper treeMapper(this, m_pTransformationDataHolder, false, false, false, true);
	const unsigned long hr = treeMapper.CreateJtrFromMappings(lTransformationID, stSourceJtrFN, stTargetJtrFN);
	AppendErrorMessage( treeMapper.get_LastError() );
	return hr;
}

void TransformationEngine::HandleWebServiceCall(const string& sRequest, string& sResponse, bool& returnedSoapFault) {
	returnedSoapFault = false;
	unsigned long hr = S_OK;
	std::string stMessage;
	sResponse.clear();

	                                                                                                  
	const string stInputDataFileName(this->getOperationContext()->getLocalTmpDirForOperation() + "Request-" + GetGUIDString() + ".xml");

	FILE* requestFile = fopen(stInputDataFileName.c_str(), "wb");
	if( !requestFile ) {
        const long lErr = GetLastError();
        stMessage = ErrorResolver().Resolve(lErr);
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_OPEN_HOSTED_WS_REQUEST_FILE_FAILED, stInputDataFileName.c_str(), stMessage.c_str());
		                     
		sResponse = SetGenericSoapFault(FaultCodeValue_11::Server, ID_OPEN_HOSTED_WS_REQUEST_FILE_FAILED);
		returnedSoapFault = true;
		return;
	}
	AutoFileHandle adopt_requestFile(requestFile);

    const size_t len0=sRequest.length();
    const size_t len=fwrite(sRequest.c_str(), 1, len0, requestFile);
    if(len0>len){
        const int err = ferror(requestFile);
        if(err!=0){
            stMessage = ErrorResolver().Resolve(err);
            hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_READ_HOSTED_WS_INPUT_FAILED, stMessage.c_str());
                                 
            sResponse = SetGenericSoapFault(FaultCodeValue_11::Server, ID_READ_HOSTED_WS_INPUT_FAILED);
			returnedSoapFault = true;
            return;
        }
    }
	adopt_requestFile.Close();

                                   
	const string stOutputDataFileName(this->getOperationContext()->getLocalTmpDirForOperation() + "Response-" + GetGUIDString() + ".xml");

                                        
    int nTransformationStatus = 0;
	int iSourceType;
	bool bIsSourceEmpty;
	hr = doOperation(stInputDataFileName,
                     stOutputDataFileName,
					 1,
                     nTransformationStatus,
					 iSourceType,
					 bIsSourceEmpty);

    if( FAILED(hr) ) {
                                                    
		const konga::SoapFault* const pSoapFault = getSoapFault();
		if( pSoapFault ) {
            sResponse = SetSpecificSoapFault(*pSoapFault);
		}
        else {
                                                                                                
                                                                                             
                                                           
			stMessage = getErrorsAndWarnings();
			sResponse = SetSpecificSoapFault(FaultCodeValue_11::Server, stMessage.empty() ? "Unknown error" : stMessage);
        }
		returnedSoapFault = true;
        return;
    }

	                            
    long lErr = 0;
    const long lOutputSize = konga::GetFileSize(stOutputDataFileName, lErr);
    if( lErr ) {
        stMessage = ErrorResolver().Resolve(lErr);
        hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_GET_HOSTED_WS_RESPONSE_SIZE_FAILED, stOutputDataFileName.c_str(), stMessage.c_str());
                             
        sResponse = SetGenericSoapFault(FaultCodeValue_11::Server, ID_GET_HOSTED_WS_RESPONSE_SIZE_FAILED);
		returnedSoapFault = true;
        return;
    }
	if ( lOutputSize == 0 ) {
		return;
	}

                                                        
    FILE* fOutput = fopen(stOutputDataFileName.c_str(), "rb");
    if( fOutput == 0 ) {
		lErr = GetLastError();
        stMessage = ErrorResolver().Resolve(lErr);
        hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_OPEN_HOSTED_WS_RESPONSE_FILE_FAILED, stOutputDataFileName.c_str(), stMessage.c_str());
                             
        sResponse = SetGenericSoapFault(FaultCodeValue_11::Server, ID_OPEN_HOSTED_WS_RESPONSE_FILE_FAILED);
		returnedSoapFault = true;
        return;
    }
    AutoFileHandle adopt_fOutput(fOutput);

    char* tmpstr = new char[lOutputSize + 1];
    AutoArrayDelete<char> adopt_tmpstr(tmpstr);
    if( fread(tmpstr, 1, lOutputSize, fOutput) != lOutputSize ) {
		lErr = GetLastError();
        stMessage = ErrorResolver().Resolve(lErr);
        hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_CONSTRUCT_HOSTED_WS_RESPONSE_STRING_FAILED, stMessage.c_str());
                             
        sResponse = SetGenericSoapFault(FaultCodeValue_11::Server, ID_CONSTRUCT_HOSTED_WS_RESPONSE_STRING_FAILED);
		returnedSoapFault = true;
        return;
    }
    tmpstr[lOutputSize] = 0;
    sResponse = tmpstr;
}

void TransformationEngine::InitDebugging() {
	Debuggee* const pDebuggee = getOperationContext()->getDebuggee();
	if ( pDebuggee ) pDebuggee->InitDebugging();
}

unsigned long TransformationEngine::TestOperation(long lOperationID, const string& stInputFN, const string& stOutputFN,
												  int &iSourceType, bool& bIsSourceEmpty)
{
	m_bTestOperation=true;
	m_bTestTransformation=true;

	int	nTransformationStatus = 0;
	return doOperation(stInputFN, stOutputFN, 1, nTransformationStatus, iSourceType, bIsSourceEmpty);
}

unsigned long TransformationEngine::LoadSourceData(long lTransformationID,
												   const string& inputFileName,
												   konga::DbConnection& tranDb)
{
	m_TransformationID = lTransformationID;
	TreeMapper treeMapper(this, m_pTransformationDataHolder, true, false, false, true);
	treeMapper.getViewLog()->Initialize("LoadSourceData", lTransformationID, m_debug_log, false, m_bWriteLog, 0, 0, false, konga::GetGUIDString());
	int nTransformationStatus;

	unsigned long hr = treeMapper.InitTransformation(tranDb, lTransformationID, nTransformationStatus, false);
	if(FAILED(hr)) {
		AppendErrorMessage(treeMapper.get_LastError());
		return hr;
	}
	m_sourceType = treeMapper.GetSourceType();
	HandleSpecialSourceStructures(inputFileName, false, &tranDb);

	hr = treeMapper.LoadSource(inputFileName);
	if(FAILED(hr)) {
		AppendErrorMessage(treeMapper.get_LastError());
	}
	return hr;
}

unsigned long TransformationEngine::TestTransformation(long lTransformationID,
													   string& stInputFileName,
													   string& stOutputFileName,
													   string& stLinkFileName)
{
	m_bTestTransformation = true;
	InitDebugging();
	TreeMapper treeMapper(this, m_pTransformationDataHolder, false, false, true, true);
	const unsigned long hr = treeMapper.TestTransformation(lTransformationID, stInputFileName, stOutputFileName, stLinkFileName);
	if(FAILED(hr)) {
		AppendErrorMessage(treeMapper.get_LastError());
		return hr;
	}

	                 
	TransformationDataStoreInstance inst;
	inst.GUID = getOperationContext()->getOperationChainInstanceGuid();
	inst.originalSourceNumber = 1;
	inst.chunkNumber = 1;
	inst.sourceFile = stInputFileName;
	inst.linkFile = stLinkFileName;
	inst.targetFile = stOutputFileName;

	TransformationDataStore store;
	store.AddFiles(inst);

	stInputFileName = inst.sourceFile;
	stLinkFileName = inst.linkFile;
	stOutputFileName = inst.targetFile;

	return hr;
}

std::string TransformationEngine::TestExpression(const std::string& stNameValuePairs, const std::string& stExpression, std::string& stWarnings) {
	InitDebugging();
	TreeMapper treeMapper(this, m_pTransformationDataHolder, false, false, false, false);
	if(m_bWriteLog){
		FILE*debug_log=0;
		CViewLog* view=treeMapper.getViewLog();
		std::string guid=getOperationContext()->getLocalTmpDirForOperation();
		view->Initialize("TestExp_", -1, debug_log, !false, true, 0, 0, false, guid.substr(guid.size()-9,8));
		view->DebugString("testExpression(sNameValuePairs=\"%s\", sExpression=\"%s\"\n", stNameValuePairs.c_str(), stExpression.c_str());
	}
	DETransform transform(&treeMapper, 0, 0);
	const string& sResult = transform.testExpression(stNameValuePairs, stExpression, stWarnings);
	return sResult;
}

DataElement* TransformationEngine::RunScript(long scriptId, ScriptCache& scriptCache) {
	TreeMapper treeMapper(this, m_pTransformationDataHolder, false, false, false, false);
	return konga::RunScript(scriptId, scriptCache, &treeMapper, 0);
}

bool TransformationEngine::autoQueryMore() const {
	                                  
	boost::mutex::scoped_lock lock(m_ThreadSyncLock);
	return konga::EqualsIgnoreCase(m_xoperationProperties.GetString(WIZARD_SALESFORCE_OPERATION), "query");
}

PROFILING::PROFILING(TransformationEngine*p0,const string&sWhere0,const std::string&sExtra0):
	pTransformationEngine(p0), pTreeMapper(0), sWhere(sWhere0), sExtra(sExtra0)
{
	pTransformationEngine->enterProfilingSection(sWhere, sExtra);
}

PROFILING::PROFILING(TreeMapper*p0,const string&sWhere0,const std::string&sExtra0):
	pTransformationEngine(0), pTreeMapper(p0), sWhere(sWhere0), sExtra(sExtra0)
{
	if(pTreeMapper)pTreeMapper->enterProfilingSection(sWhere, sExtra);
}

PROFILING::~PROFILING()
{
	if(pTransformationEngine)pTransformationEngine->exitProfilingSection(sWhere);
	else if(pTreeMapper) pTreeMapper->exitProfilingSection(sWhere);
}
