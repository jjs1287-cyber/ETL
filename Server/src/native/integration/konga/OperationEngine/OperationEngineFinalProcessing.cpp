/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <vector>

#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>

#include <konga/OperationLog/OperationLog.h>

#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineContext.h>
#include <konga/OperationObjects/EmailMessageSender.h>

#include "OperationEngine.h"
#include "DebugLog.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;

void OperationEngine::FinishOperation(ProcessedFiles& files, OperationEngineException* const pEx) {
	OP_DEBUG_LOG("About to finish operation (logging and starting next operation).");
	string sMsg;
	if ( m_pOperation )
		sMsg = "The operation \"" + m_pOperation->getName() + "\"";
	else
		sMsg = "The operation";

	OperationLogStatus status = OperationLogStatus_Success;

	                                                                                            
	bool bOperationProducedNoResults = false;
	long lErrorNumber = 0;
	if ( pEx != 0 ) {
		bOperationProducedNoResults = true;
		lErrorNumber = pEx->GetErrorNumber();
		if ( pEx->isFatal() ) {
			status = OperationLogStatus_Error;
			sMsg += " failed.";
		}
		else {
			status = OperationLogStatus_Success_With_Warning;
			sMsg += " was not executed because no input files were found.";
		}
	}
	else {
		                                                                           
		                                                
		if ( files.getNumberOfProcessedFiles() > 0 ) {
			                                           
			if ( files.hasErrors() ) {
				status = OperationLogStatus_Error;
				sMsg += " was partly successful. Some files were not processed successfully.";
			}
			else if ( files.hasWarnings() ) {
				status = konga::most_severe(status, OperationLogStatus_Success_With_Warning);
				sMsg += " completed with warnings.";
			}
			else if ( files.filesInHistory() > 0 ) {
				status = konga::most_severe(status, OperationLogStatus_Success_With_Info);
				if ( files.filesInHistory() == files.getNumberOfProcessedFiles() ) {
					bOperationProducedNoResults = true;
					sMsg += " produced no results because all the source files were found in the operation history.";
				}
				else
					sMsg += " completed successfully but some source files were not processed because they were found in the operation history.";
			}
			else
				sMsg += " completed successfully.";
		}
		else {
			                                                        
			sMsg += " completed successfully.";
		}
	}

	OperationLog opLog(GetDbConnection());

	if ( pEx  ) {
		if ( pEx->isFatal() ) {
			opLog.LogMessage(m_llOperationInstanceId, "Fatal Error", pEx->GetErrorString());
		}
		else if ( shouldLog(status) ) {
			opLog.LogMessage(m_llOperationInstanceId, "Warning", pEx->GetErrorString());
		}
	}

	                                          
	                                               
	if ( !m_sPreTranWarnings.empty() ) {
		status = konga::most_severe(status, OperationLogStatus_Success_With_Info);
		if(shouldLog(status)) opLog.LogMessage( m_llOperationInstanceId, "Messages logged in pre-transformation scripts", m_sPreTranWarnings);
	}

	                                                      
	const vector<ProcessedFile>& vFiles = files.getProcessedFilesByRef();
	vector<ProcessedFile>::const_iterator it = vFiles.begin();
	for( ; it != vFiles.end(); ++it ) {
		const ProcessedFile& file = *it;
		const string stRemoteLocation( file.getRemoteSourceLocation() );
		if ( file.isInHistory() ) {
			status = konga::most_severe(status, OperationLogStatus_Success_With_Info);
			if ( shouldLog(status) ) opLog.LogMessage(m_llOperationInstanceId, "Source found in history", stRemoteLocation);
		}
		if ( file.hasError() ) {
			status = konga::most_severe(status, OperationLogStatus_Error);
			const string stShort( stRemoteLocation.empty() ?
				"Error" : "Error(s) while processing " + stRemoteLocation );
			opLog.LogMessage(m_llOperationInstanceId, stShort, file.getError());
		}
		if ( file.hasWarning() ) {
			status = konga::most_severe(status, OperationLogStatus_Success_With_Warning);
			const string stShort( stRemoteLocation.empty() ? "Warning" : "Warning(s) while processing " + stRemoteLocation);
			if ( shouldLog(status) ) opLog.LogMessage(m_llOperationInstanceId, stShort, file.getWarning());
		}
	}

	                                           
	                                  
	if ( !m_sPostTranWarnings.empty() ) {
		status = konga::most_severe(status, OperationLogStatus_Success_With_Info);
		if ( shouldLog(status) ) opLog.LogMessage(m_llOperationInstanceId, "Messages logged in post-transformation scripts", m_sPostTranWarnings);
	}

	if ( pEx || files.hasErrors() || files.hasWarnings() ) {
		m_ErrorReporter.addError(sMsg);	                                                    
		if ( pEx ) pEx->AppendErrorString(sMsg);
	}

	const bool bSuccessOperation = !(pEx != 0 && pEx->isFatal());

	const bool bAlwaysRunSuccessOperation =
		konga::GetOptionAsBoolean(stOP_LOG_SOURCE, "AlwaysRunSuccessOperation", false);

	string stOperationChainCancellationMsg;
	const bool bCancelOperationChain = m_pContext->wasOperationChainCancelled(stOperationChainCancellationMsg);

	                                     
	if ( !m_stAlsoWriteTargetToThisFile.empty() ) {
		if ( !files.empty() ) {
			const string& localFile = files.getProcessedFilesByRef().at(0).getLocalTargetLocation();
			if ( FileExists(localFile) ) {
				kCopyFile(localFile, m_stAlsoWriteTargetToThisFile);
			}
		}
	}

	                                                          
	try {
		if ( !bCancelOperationChain && m_pOperation != 0 ) {
			                                                                                               
			                                                                    
			if ( bAlwaysRunSuccessOperation || (!bSuccessOperation || !bOperationProducedNoResults) )
				StartNextOperation(bSuccessOperation, files, sMsg);
		}
	}
	catch ( const CrException& ex ) {
		const string sTitle("Failed to start " + string(bSuccessOperation?"success":"failure")
			+ " operation.");
		const string stMsg(ex.GetErrorString());
		m_Logger.LogError(stMsg);
		m_ErrorReporter.addError(stMsg);
		status = konga::most_severe(status, OperationLogStatus_Success_With_Warning);
		opLog.LogMessage(m_llOperationInstanceId, sTitle, stMsg);
	}

	                                                                                       
	if ( !m_bStartedOtherOperation && !m_stAlsoWriteFinalTargetToThisFile.empty() ) {
		if ( !files.empty() ) {
			const string& localFile = files.getProcessedFilesByRef().at(0).getLocalTargetLocation();
			if ( FileExists(localFile) ) {
				kCopyFile(localFile, m_stAlsoWriteFinalTargetToThisFile);
			}
		}
	}

	                                             
	if ( bCancelOperationChain ) {
		status = konga::most_severe(status, OperationLogStatus_Success_With_Info);
		if ( shouldLog(status) ) {
			if ( !stOperationChainCancellationMsg.empty() )
				opLog.LogMessage(m_llOperationInstanceId, "Operation Chain was cancelled", stOperationChainCancellationMsg);
			else 
				opLog.LogMessage(m_llOperationInstanceId, "Operation Chain was cancelled", " ");
		}
	}

	                                
	try {
		                                                                                         
		if ( (!bSuccessOperation || !bOperationProducedNoResults) && m_pOperation != 0)
			SendOperationNotification(bSuccessOperation);
	}
	catch( const CrException& ex ) {
		const string sTitle("Failed to send " + string(bSuccessOperation?"success":"failure") + " message.");
		const string sMsg(ex.GetErrorString());
		m_Logger.LogError(sMsg);
		m_ErrorReporter.addError(sMsg);
		status = konga::most_severe(status, OperationLogStatus_Success_With_Warning);
		opLog.LogMessage(m_llOperationInstanceId, sTitle, sMsg);
	}

	                                         
	if ( shouldLog(status) ) {
		opLog.LogMessageUpdateStatus(m_llOperationInstanceId, status, "", sMsg);
	}

	                                                                     
	if (status != OperationLogStatus_Error) {
		m_pContext->completeSuccessfulOperation();
	}
	else if ( !m_bIsDebugEnabled ) {
		getOperationContext().setLocalTmpDirForOperationDeleteFlag( status != OperationLogStatus_Error );
	}
}

void OperationEngine::SendOperationNotification(bool bIsSuccessOperation) {
	if ( !m_pOperation )
		return;

	const string sMessageType( bIsSuccessOperation ? "success" : "failure" );
	const long lMessageId = bIsSuccessOperation ? m_pOperation->getSuccessMessageId() :
		m_pOperation->getFailureMessageId();

	if ( lMessageId < 1 )
		return;

	OP_DEBUG_LOG("About to send " + sMessageType + " email for operation \"" + m_pOperation->getName() + "\".");
	try {
		konga::SendEmailMessage(lMessageId, GetDbConnection(), getOperationDataElementHolder(), true);
	}
	catch ( const CrException& ex ) {
		throw OperationEngineException("Failed to send " + sMessageType + " message. " + ex.GetErrorString());
	}
}

