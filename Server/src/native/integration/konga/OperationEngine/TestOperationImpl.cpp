/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>		                

#include <konga/Utils/FileUtils.h>		               

#include <konga/Debugger/Debuggee.h>

#include <konga/TransformationEngine/TransformationEngine.h>
#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/Transformation.h>
#include <konga/OperationObjects/SourceDataLocation.h>
#include <konga/OperationObjects/SourceDataStructure.h>
#include <konga/OperationObjects/TargetDataStructure.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/OperationEngineContext.h>

#include "OperationScriptRunner.h"
#include "OperationEngine.h"
#include "DebugLog.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	void AppendMessage(string& msg, const string& str) {
		if ( !msg.empty() ) {
			msg += "\n";
		}
		msg += str;
	}
}

void OperationEngine::TestOperation(long lOperationId,
									OperationDataElementHolder* pDeHolder,
									int& iSourceType,
									string& stWarnings)
{
	OP_DEBUG_LOG("OperationEngine::TestOperation called with OperationId = " + toString(lOperationId));
	initContext(pDeHolder);

	std::auto_ptr<Operation> pOperation( getOperation(lOperationId) );
	                                                              
	                                                            
	const OperationType ot = pOperation->getOperationType();
	if ( ot != OPERATION_TRANSFORMATION &&
		ot != OPERATION_WEBSERVICE_CALL ) {
		throw OperationEngineException("Invalid operation type: Only transformation operations and web service calls can be tested.");
	}

	Debuggee* pDebuggee = getOperationContext().getDebuggee();
	if ( pDebuggee ) {
		pDebuggee->InitDebugging();
		pDebuggee->getCallStack().push(
			new IntegrationBreakpointLocation(lOperationId, -1, -1, -1, "")
			);
	}
	AutoPopCallStack pop_stack(pDebuggee);

	m_pContext->setDbConnection(&GetDbConnection());
	m_pContext->setOperation(pOperation.get());
	m_pOperation = pOperation.get();
	m_lOperationId = lOperationId;

	SetCurrentUserIfNeeded();

	boost::shared_ptr<OperationPipeline> pOperationPipeline( konga::OperationPipeline::getOperationPipeline(lOperationId, GetDbConnection()) );
	m_pScriptRunner = new OperationScriptRunner(pOperationPipeline, *m_pContext, *m_pDeHolder, GetDbConnection());

	OP_DEBUG_LOG("TestOperation: About to get the first source file.");
	ProcessedFiles files;
	const SourceDataLocation* pSourceLocation = 0;
	try {
		m_pScriptRunner->RunScriptsBefore(ActivityType_Source);
		pSourceLocation = pOperation->getSource();
		if ( pSourceLocation != 0 ) {
			if ( !pSourceLocation->userHasReadPermission(m_CurrentUser, &GetDbConnection()) ) {
				throw OperationEngineException("The current user does not have read permission for the source '" + pSourceLocation->getSourceName() + "'.");
			}
			pSourceLocation->doPreSourceTransfer(getOperationContext(), getOperationDataElementHolder(), files);
			pSourceLocation->getFirstSourceFile(*m_pContext, files);
			pSourceLocation->doPostSourceTransfer(getOperationContext(), getOperationDataElementHolder(), files);
		}
	}
	catch ( OperationEngineException& ex ) {
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		throw;
	}

	                                  
	try {
		const Transformation* const pTransformation = pOperation->getTransformation();
		if ( pTransformation!= 0 ) {
			                                                               
			OP_DEBUG_LOG("Preparing for transformation with TransformationID = "
				+ toString(pTransformation->getTransformationId()) + ", OperationID = " + toString(lOperationId));
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
		throw;
	}

	                               
	OP_DEBUG_LOG("TestOperation: About to perform the operation.");
	try {
		m_pScriptRunner->RunScriptsBefore(ActivityType_Transformation);
		string tmp;
		m_pScriptRunner->CollectScriptWarnings(tmp);
		if( !tmp.empty() ) {
			AppendMessage(stWarnings, "Messages from pre-transformation scripts:\n" + tmp);
		}
		string stInputFileName;
		if ( !files.empty() ) {
			const ProcessedFile& file = files.getProcessedFilesByRef().at(0);
			stInputFileName = file.getLocalSourceLocation();
			m_pContext->setSourceLocator(file.getRemoteSourceLocation());
			m_pContext->setSourceSize(file.getSourceSize());
		}

		TransformationEngine transform(m_CurrentUser, &getOperationContext(), &getOperationDataElementHolder());
		bool bIsSourceEmpty = true;
		const string stOutputFileName( getTmpFileName() );
		unsigned long err = transform.TestOperation(lOperationId, stInputFileName, stOutputFileName, iSourceType, bIsSourceEmpty);
		if ( err )
			throw OperationEngineException(transform.getLastError());

		tmp = transform.getErrorsAndWarnings();
		if( !tmp.empty() ) {
			AppendMessage(stWarnings, tmp);
		}
	}
	catch ( OperationEngineException& ex ) {
		                                                                          
		getOperationContext().setErrorReporter(&m_ErrorReporter);
		getOperationContext().setLocalTmpDirForOperationDeleteFlag(false);
		ex.setOperationId(lOperationId);
		m_ErrorReporter.addError(ex.GetErrorString());
		throw;
	}

	                                                                       

	                                                            
	m_pScriptRunner->RunScriptsBefore(ActivityType_Target);
	m_pScriptRunner->RunFinalScripts();
	string tmp;
	m_pScriptRunner->CollectScriptWarnings(tmp);
	if ( !tmp.empty() ) {
		AppendMessage(stWarnings, "Messages from post-transformation scripts:\n" +tmp);
	}
}

