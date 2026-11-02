/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <sstream>
#include <cassert>
#include <memory>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/Settings.h>		                     
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/FileUtils.h>		                                  

#include <konga/Debugger/Debuggee.h>

#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/lmcheck.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DataElement/DataElement.h>
#include <konga/ItemAccess/UserAccess.h>

#include <konga/PluginManager/PipelinePluginSelector.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include <konga/OperationLog/OperationLog.h>
#include <konga/OperationLog/OperationInstance.h>
#include <konga/OperationLog/OperationProgress.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>
#include <konga/OperationObjects/OperationEngineContext.h>

#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/Transformation.h>
#include <konga/OperationObjects/SourceDataStructure.h>
#include <konga/OperationObjects/TargetDataStructure.h>
#include <konga/OperationObjects/SourceDataLocation.h>
#include <konga/OperationObjects/TargetDataLocation.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/TransferHistory.h>
#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/OperationSummaryWriter.h>
#include <konga/OperationObjects/OperationEngineContext.h>
#include <konga/OperationObjects/FileTypeTargetLocation.h>
#include <konga/OperationObjects/OverrideFileTypeSourceLocation.h>

#include <konga/OperationContext/ProjectVariableDefaults.h>
#include "OperationScriptRunner.h"
#include "OperationCreatorDb.h"
#include "TransformationOperation.h"
#include "OperationEngine.h"
#include "DebugLog.h"

using namespace std;
using namespace boost;
using namespace konga;

const string konga::OperationEngine::stOP_LOG_SOURCE("OperationEngine");

OperationEngine::OperationEngine(const UserInfo& currentUser, Debuggee* pDebuggee, DbConnection* pTranDbConnection) : DbConnectionHolder(pTranDbConnection),
m_bIsDebugEnabled(IsGlobalDebugLoggingEnabled()),
m_bSynchronous(false),
m_bStartedOtherOperation(false),
m_bExclusiveOverrideSourceFileName(true),
m_lOperationId(0), m_lSessionId(0),
m_CurrentUser(currentUser),
m_pOperation(0),
m_pDeHolder(0), m_bCreatedDeHolder(false),
m_Logger(stOP_LOG_SOURCE), m_pContext(0),
m_pScriptRunner(0), m_pDebuggee(pDebuggee),
m_pPipelinePluginSelector(0)
{
	this->SetInternalDbName("TranDb");
	m_pPipelinePluginSelector = new PipelinePluginSelector(&this->GetDbConnection());
	konga::SetClientEncoding(this->GetDbConnection());
	EnableDebugging(m_bIsDebugEnabled);
}

OperationEngine::~OperationEngine(void) {

	delete m_pPipelinePluginSelector;
	m_pPipelinePluginSelector = 0;

	OP_DEBUG_LOG("Entering OperationEngine::~OperationEngine");
	if ( m_pContext ) m_pContext->setErrorReporter(0);

	delete m_pScriptRunner;
	m_pScriptRunner = 0;

	               
	      
	delete m_pContext;
	m_pContext = 0;

	if ( m_bCreatedDeHolder )
		delete m_pDeHolder;
	m_pDeHolder = 0;
	      

	OP_DEBUG_LOG("Exiting OperationEngine::~OperationEngine");
}

bool OperationEngine::IsGlobalDebugLoggingEnabled() const {
	if( WDILogFileUtils::GenerateLogFile() )
		return true;
	return GetOptionAsBoolean("OperationEngine", "Debug", false);
}

void OperationEngine::saveTargetToThisFile(const std::string& fileName) {
	m_stAlsoWriteTargetToThisFile = fileName;
}

void OperationEngine::saveFinalTargetToThisFile(const std::string& fileName) {
	m_stAlsoWriteFinalTargetToThisFile = fileName;
}

void OperationEngine::overrideSourceFile(const std::string& fileName, bool isExclusive) {
	m_stOverrideSourceFileName = fileName;
	m_bExclusiveOverrideSourceFileName = isExclusive;
}

void OperationEngine::EnableDebugging(bool bDebug) {
	m_bIsDebugEnabled = bDebug;
	if ( m_bIsDebugEnabled ) {
		m_Logger.AddSeverityBit(KSEVERITY_DEBUG);
	}
	if ( m_pContext != 0 ) {
		m_pContext->setLocalTmpDirForOperationDeleteFlag(!m_bIsDebugEnabled);
		m_pContext->setDebugFlag(bDebug);
	}
}

bool OperationEngine::shouldWriteHistory() const {
	const string nd("nd");
	const string& s = konga::GetSystemVariable("etlforge.operation.write_history", &getOperationDataElementHolder(), nd);
	if ( s != nd ) {
		return konga::toBool(s);
	}

	return ( m_pOperation && m_pOperation->shouldWriteHistoryLog() );
}

bool OperationEngine::shouldLog(OperationLogStatus status) const {
	int logLevel = 1;
	const string nd("nd");
	const string& s = konga::GetSystemVariable("etlforge.operation.log_level", &getOperationDataElementHolder(), nd);
	if ( s != nd ) {
		const int itmp = atoi(s.c_str());
		if ( itmp >= 1 && itmp <= 3 ) logLevel = itmp;
	}
	else {
		logLevel = ( m_pOperation != 0 ) ? m_pOperation->getOperationLogLevel() : 1;
	}

	             
	                    
	                             
	                      
	if ( logLevel == 1 ) {
		return true;
	}
	else if ( logLevel == 2 ) {
		return status >= OperationLogStatus_Success_With_Info;
	}
	else {
		return status == OperationLogStatus_Error;
	}
}

int OperationEngine::getFinalTargetType() const {
	if (m_pDeHolder) {
		if ( m_pDeHolder->isOperationDataElementDefined(TransformationOperation::FINAL_TARGET_TYPE_DE) ) {
			DataElement* de = m_pDeHolder->getOperationDataElement(TransformationOperation::FINAL_TARGET_TYPE_DE);
			int err = 0;
			const int t = de->GetInteger(err);
			if ( err == 0 ) {
				return t;
			}
		}
	}
	return 0;
}

OperationDataElementHolder& OperationEngine::getOperationDataElementHolder() const {
	assert(m_pDeHolder);
	return *m_pDeHolder;
}

OperationContext& OperationEngine::getOperationContext() const {
	assert(m_pContext);
	return *m_pContext;
}

void OperationEngine::RunOperationInit(long lOperationId, long lSessionId, long long llOperationInstanceId,
									   OperationDataElementHolder* pDeHolder, std::auto_ptr<Operation>& pOperation,
									   boost::shared_ptr<OperationPipeline>& pOperationPipeline) {
	m_lOperationId = lOperationId;

	if ( llOperationInstanceId > 0 ) {
		m_llOperationInstanceId = llOperationInstanceId;
		OperationLog(GetDbConnection()).UpdateLogStatus(llOperationInstanceId, OperationLogStatus_Running);
	}
	else {
		m_llOperationInstanceId = llOperationInstanceId = konga::NewOperationInstance(GetDbConnection(), lOperationId, "Run Now", -1);
		OperationLog(GetDbConnection()).CreateRunningEntry(lOperationId, llOperationInstanceId);
	}

	initContext(pDeHolder);
	etlforge::loadProjectVariableDefaults(GetDbConnection(), lOperationId, *m_pDeHolder);
	GetOperationDataElements(lSessionId);

	CheckForSourceOverride();

	pOperation.reset( getOperation(lOperationId) );

	                                                                                                          
	m_pOperation = pOperation.get();

	m_pContext->setDbConnection(&GetDbConnection());
	m_pContext->setOperation(pOperation.get());

	SetCurrentUserIfNeeded();

	pOperationPipeline.reset( konga::OperationPipeline::getOperationPipeline(lOperationId, GetDbConnection()) );
	m_pScriptRunner = new OperationScriptRunner(pOperationPipeline, *m_pContext, *m_pDeHolder, GetDbConnection());
}

void OperationEngine::CheckForSourceOverride() const {
	if ( !m_stOverrideSourceFileName.empty() && m_pDeHolder ) {
		DataElement* de = m_pDeHolder->getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME);
		de->setType(DataElement::t_string);
		de->setValue(m_stOverrideSourceFileName.c_str());

		de = m_pDeHolder->getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE);
		de->setType(DataElement::t_bool);
		de->setTypedValue(m_bExclusiveOverrideSourceFileName);
	}
}

void OperationEngine::RunOperation(long lOperationId, long lSessionId, long long llOperationInstanceId,
								   OperationDataElementHolder* pDeHolder,
								   bool bSynchronous)
{
	OP_DEBUG_LOG("OperationEngine::RunOperation called with OperationId = " + toString(lOperationId) + ", SessionId = " + toString(lSessionId));

	m_bSynchronous = bSynchronous;

	auto_ptr<Operation> pOperation(0);
	boost::shared_ptr<OperationPipeline> pOperationPipeline;
	ProcessedFiles files;
	try {
		RunOperationInit(lOperationId, lSessionId, llOperationInstanceId, pDeHolder, pOperation, pOperationPipeline);
	}
	catch( OperationEngineException& ex ) {
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		FinishOperation(files, &ex);
		throw;
	}
	OperationProgress progress(GetDbConnection(), getOperationContext().getOperationInstanceId());

	assert(m_pOperation);
	OP_DEBUG_LOG("Initialized operation \"" + m_pOperation->getName() + "\".");

	Debuggee* pDebuggee = getOperationContext().getDebuggee();
	if ( pDebuggee ) {
		pDebuggee->getCallStack().push(
			new IntegrationBreakpointLocation(lOperationId, -1, -1, -1, "")
			);
	}
	AutoPopCallStack pop_stack(pDebuggee);

	                        
	OP_DEBUG_LOG("About to get the source files.");
	const SourceDataLocation* pSourceLocation = 0;
	try {
		this->Check();

		OP_DEBUG_LOG("About to run pre-source scripts");
		m_pScriptRunner->RunScriptsBefore(ActivityType_Source);

		pSourceLocation = pOperation->getSource();
		if ( pSourceLocation != 0 ) {
			if ( !pSourceLocation->userHasReadPermission(m_CurrentUser, &GetDbConnection()) ) {
				throw OperationEngineException("The current user does not have read permission for the source '" + pSourceLocation->getSourceName() + "'.");
			}
			pSourceLocation->doPreSourceTransfer(*m_pContext, *m_pDeHolder, files);
			pSourceLocation->doTransfer(*m_pContext, files, progress);
			pSourceLocation->doPostSourceTransfer(*m_pContext, *m_pDeHolder, files);
		}
		DoPostSourceProcessing(files);
	}
	catch ( OperationEngineException& ex ) {
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		m_pScriptRunner->CollectScriptWarnings(m_sPreTranWarnings);
		FinishOperation(files, &ex);
		if ( ex.isFatal() )
			throw;
		else
			return;
	}

	                                  
	try {
		const Transformation* const pTransformation = pOperation->getTransformation();
		if ( pTransformation!= 0 ) {
			                                                               
			OP_DEBUG_LOG("Preparing for transformation with TransformationId = "
				+ toString(pTransformation->getTransformationId()) + ", OperationId = " + toString(lOperationId));
			const SourceDataStructure* const pSourceStructure = pTransformation->getSourceStructure();
			if ( pSourceStructure != 0 )
				pSourceStructure->doPreTransformation(*m_pContext);
	
			const TargetDataStructure* const pTargetStructure = pTransformation->getTargetStructure();
			if ( pTargetStructure != 0 )
				pTargetStructure->doPreTransformation(*m_pContext);
		}
	}
	catch ( OperationEngineException& ex ) {
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		m_pScriptRunner->CollectScriptWarnings(m_sPreTranWarnings);
		FinishOperation(files, &ex);
		if ( ex.isFatal() )
			throw;
		else
			return;
	}

	                               
	try {
		OP_DEBUG_LOG("About to run pre-transformation scripts.");
		m_pScriptRunner->RunScriptsBefore(pOperation->getActivityType());
		m_pScriptRunner->CollectScriptWarnings(m_sPreTranWarnings);

		OP_DEBUG_LOG("About to perform the operation.");
		pOperation->doOperation(files, *m_pContext, getOperationDataElementHolder());

		OP_DEBUG_LOG("About to do post transformation processing.");
		DoPostTransformationProcessing(files);
	}
	catch ( OperationEngineException& ex ) {
		OP_DEBUG_LOG("Exception caught: " + ex.GetErrorString());
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		m_pScriptRunner->CollectScriptWarnings(m_sPreTranWarnings);
		FinishOperation(files, &ex);
		if ( ex.isFatal() )
			throw;
		else
			return;
	}

	                                                            
	try {
		OP_DEBUG_LOG("About to run pre-target scripts.");
		m_pScriptRunner->RunScriptsBefore(ActivityType_Target);

		OP_DEBUG_LOG("About to transfer the target files.");
		const TargetDataLocation* const pTargetLocation = pOperation->getTarget();
		if ( pTargetLocation != 0 ) {
			if ( !pTargetLocation->userHasReadPermission(m_CurrentUser, &GetDbConnection()) ) {
				throw OperationEngineException("The current user does not have write permission for the target '" + pTargetLocation->getTargetName() + "'.");
			}
			pTargetLocation->doPreTargetTransfer(getOperationContext(), getOperationDataElementHolder(), files);
			pTargetLocation->doTransfer(getOperationDataElementHolder(), files);
			pTargetLocation->doPostTargetTransfer(getOperationContext(), getOperationDataElementHolder(), files);
		}

		if ( pSourceLocation != 0 )
			pSourceLocation->finishSourceFiles(*m_pContext, files);

		OP_DEBUG_LOG("About to run post-target scripts.");
		m_pScriptRunner->RunFinalScripts();
		m_pScriptRunner->CollectScriptWarnings(m_sPostTranWarnings);
	}
	catch ( OperationEngineException& ex ) {
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		m_pScriptRunner->CollectScriptWarnings(m_sPostTranWarnings);
		FinishOperation(files, &ex);
		if ( ex.isFatal() )
			throw;
		else
			return;
	}

	                                                                                     
	if ( shouldWriteHistory() ) {
		try {
			OP_DEBUG_LOG("About to write operation summary.");
			OperationSummaryWriter writer;
			writer.Write(getOperationContext().getOperationInstanceId(), files, GetDbConnection());
		}
		catch( const CrException& ex ) {
			m_Logger.Log("Failed to write operation summary but operation was successful: " + ex.GetErrorString());
		}
	}

	FinishOperation(files, 0);

	if ( this->shouldWriteHistory() ) {
		OP_DEBUG_LOG("About to write transfer history.");
		AddToTransferHistory(files);
	}

	OP_DEBUG_LOG("OperationEngine::RunOperation returned success.");
}

void OperationEngine::initContext(OperationDataElementHolder* pDeHolder) {
	                                    
	if ( pDeHolder == 0 ) {
		m_pDeHolder = new DefaultOperationDataElementHolder(m_CurrentUser);
		m_bCreatedDeHolder = true;
	}
	else {
		m_pDeHolder = pDeHolder;
		m_bCreatedDeHolder = false;
	}

	                               
	m_pContext = new OperationEngineContext(m_CurrentUser, m_pDebuggee, *m_pDeHolder);
	m_pContext->setErrorReporter(&m_ErrorReporter);
	m_pContext->setLocalTmpDirForOperationDeleteFlag(!m_bIsDebugEnabled);
	m_pContext->setOperationInstanceId(m_llOperationInstanceId);
}

Operation* OperationEngine::getOperation(long lOperationId) {
	try {
		OperationCreatorDb objectCreator;
		Operation* op = objectCreator.getOperation(lOperationId, GetDbConnection(), getOperationDataElementHolder(), getOperationContext(), m_pPipelinePluginSelector);
		m_lOperationId = op->getId();
		                                                             
		if ( op->isDebugOn() ) {
			EnableDebugging(true);
		}
		return op;
	}
	catch( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to create operation with OperationID = " << lOperationId << ". " << endl
			 << "A database exception occurred." << endl << ex.GetErrorString();
		m_Logger.Log(sErr.str());
		throw OperationEngineException(sErr.str(), lOperationId);
	}
}

long OperationEngine::getOperationId() const {
	if ( m_pOperation )
		return m_pOperation->getId();
	else
		return -1;
}

void OperationEngine::AddToTransferHistory(ProcessedFiles& files) {
	TransferHistory(&GetDbConnection()).AddToTransferHistory(getOperationId(), files);
}

void OperationEngine::AddToTransferHistory(const ProcessedFile& file) {
	TransferHistory(&GetDbConnection()).AddToTransferHistory(getOperationId(), file);
}

void OperationEngine::SetNetworkingInfo(const HttpRequest& request, OperationDataElementHolder& deHolder) {
	HttpRequest::HeaderMapT::const_iterator citHeader = request.Headers.begin();
	for (; citHeader != request.Headers.end(); ++citHeader) {
		DataElement *pDe = deHolder.getOperationDataElement("etlforge.networking.http.request.header." + citHeader->first);
		pDe->setType(DataElement::t_string);
		pDe->setValue(citHeader->second.c_str());
	}

	DataElement *pDe = deHolder.getOperationDataElement("etlforge.networking.peer.ip");
	pDe->setType(DataElement::t_string);
	pDe->setValue(request.IpAddress.c_str());

	if ( !request.Query.empty() ) {
		for ( HttpRequest::UrlQueryMapT::const_iterator cit = request.Query.begin(); cit != request.Query.end(); ++cit ) {
			string name(cit->first);
			if ( name.empty() )
				continue;
			DecodeQueryString(name);

			DataElement* de = deHolder.getOperationDataElement("etlforge.networking.http.query." + name);
			de->setType(DataElement::t_string);
			string value(cit->second);
			DecodeQueryString(value);
			de->setValue(value.c_str());
		}
	}
}

void OperationEngine::HandleHostedWebServiceCall(long lOperationId,
												 const HttpRequest& request,
												 HttpResponse& response,
												 string& sWarnings)
{
	initContext(0);
	etlforge::loadProjectVariableDefaults(GetDbConnection(), lOperationId, *m_pDeHolder);

	auto_ptr<Operation> pOperation( getOperation(lOperationId) );
	m_lOperationId = lOperationId;
	m_pOperation = pOperation.get();

	m_pContext->setDbConnection(&GetDbConnection());
	m_pContext->setOperation(pOperation.get());
	m_pContext->setSourceSize(request.Body.size());

	SetCurrentUserIfNeeded();

	SetNetworkingInfo(request, *m_pDeHolder);

	boost::shared_ptr<OperationPipeline> pOperationPipeline( konga::OperationPipeline::getOperationPipeline(lOperationId, GetDbConnection()) );
	m_pScriptRunner = new OperationScriptRunner(pOperationPipeline, *m_pContext, *m_pDeHolder, GetDbConnection());

	HandleHostedWebServiceCallImpl(request, response, sWarnings);
}

void OperationEngine::SetCurrentUserIfNeeded() {
	assert( m_pOperation );
	if ( m_pOperation == 0 )
		throw InternalErrorException("No operation was defined when OperationEngine::SetCurrentUserIfNeeded() was called.");

	const long lOperationId = m_pOperation->getId();

	                                                                         
	if ( !m_CurrentUser.isInitialized() ) {
		try {
			m_CurrentUser = konga::GetItemOwner(GetDbConnection(), lOperationId, OperationEntityId);
			m_pContext->setCurrentUser(m_CurrentUser);
			m_pDeHolder->setCurrentUser(m_CurrentUser);
		}
		catch( const CrException& e ) {
			throw OperationEngineException("Failed to get the current user: " + e.GetErrorString(), lOperationId, "Checking permissions");
		}
	}

	                                                          
	                                                                             
	UserAccess accessCheck(m_CurrentUser, GetDbConnection());
	if ( !accessCheck.HasExecutePermission(lOperationId, OperationEntityId) ) {
		throw OperationEngineException("The user " + m_CurrentUser.GetLogin() + " does not have execute permission for the operation '"
			+ m_pOperation->getName() + "'.", lOperationId, "Checking permissions");
	}
}

namespace {
	                                     
	boost::mutex g_lmlock;
	bool g_lm_check_done = false;
}

void OperationEngine::Check(bool skip) {
	boost::mutex::scoped_lock lock(g_lmlock);
	if ( g_lm_check_done ) return;
	if ( skip ) {
		g_lm_check_done = true;
		return;
	}

	try {
		lm* plm = konga::get_lm(&GetDbConnection());
		if ( plm != 0 ) {
			std::auto_ptr<lm> adopt_plm(plm);
			string warning;
			plm->check(warning, konga::GetOptionAsInt("Settings", "LicenseWarningDays", 10));
			if ( !warning.empty() ) {
				m_Logger.LogWarning(warning);
				m_sPreTranWarnings += warning;
			}
		}
		g_lm_check_done = true;
	}
	catch( const CrException& ex ) {
		m_Logger.LogError(ex.GetErrorString());
		throw OperationEngineException(ex.GetErrorString());
	}
}

void OperationEngine::DoPostSourceProcessing(const ProcessedFiles& files) {
	const vector<ProcessedFile>& vProcessedFiles = files.getProcessedFilesByRef();
	vector<string> vLeafNames, vLocators;
	vector<long long> vFileSizes;
	for( vector<ProcessedFile>::const_iterator cit = vProcessedFiles.begin(); cit != vProcessedFiles.end(); ++cit ) {
		const ProcessedFile& file = *cit;
		string leafName( file.getRemoteSourceLeafName() );
		if ( leafName.empty() ) {
			leafName = file.getLocalSourceLeafName();
		}
		if ( leafName.empty() || file.isInHistory() )
			continue;

		vLeafNames.push_back(leafName);
		vLocators.push_back(file.getRemoteSourceLocation());
		vFileSizes.push_back(file.getSourceSize());
	}

	assert( vLeafNames.size() == vLocators.size() );
	assert( vLeafNames.size() == vFileSizes.size() );

	DataElement* deFileNames = getOperationDataElementHolder().getOperationDataElement("etlforge.source.filenames");
	DataElement* deLocators = getOperationDataElementHolder().getOperationDataElement("etlforge.source.locators");
	DataElement* deSizes = getOperationDataElementHolder().getOperationDataElement("etlforge.source.sizes");
	deFileNames->initCollection();
	deLocators->initCollection();
	deSizes->initCollection();
	if ( !vLeafNames.empty() ) {
		const vector<string>::size_type size = vLeafNames.size();
		for( vector<string>::size_type i = 0; i < size; ++i ) {
			deFileNames->addToCollection( new DataElement(DataElement::t_string, vLeafNames.at(i).c_str()) );
			deLocators->addToCollection( new DataElement(DataElement::t_string, vLocators.at(i).c_str()) );
			deSizes->addToCollection( new DataElement(DataElement::t_long, vFileSizes.at(i)) );
		}
	}
}

void OperationEngine::DoPostTransformationProcessing(const ProcessedFiles& files) {
	int nTargetFiles = 0;
	const vector<ProcessedFile>& vProcessedFiles = files.getProcessedFilesByRef();
	vector<string> vLeafNames, vLocators;
	for( vector<ProcessedFile>::const_iterator cit = vProcessedFiles.begin(); cit != vProcessedFiles.end(); ++cit ) {
		const ProcessedFile& file = *cit;
		if ( !FileTypeTargetLocation::doNotTransfer(file) ) {
			++nTargetFiles;
		}
	}

	DataElement* deFileNames = getOperationDataElementHolder().getOperationDataElement("etlforge.target.file_count");
	deFileNames->setType(DataElement::t_int);
	deFileNames->setTypedValue(static_cast<double>(nTargetFiles));
}

string OperationEngine::getOperationChainInstanceGuid() const {
	return getOperationContext().getOperationChainInstanceGuid();
}

