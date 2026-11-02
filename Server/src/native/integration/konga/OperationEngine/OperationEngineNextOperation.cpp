/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>

#include <boost/scoped_ptr.hpp>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>		                                         
#include <konga/Utils/util.h>			                  
#include <konga/Utils/ThreadUtils.h>

#include <konga/Messaging/OperationMessageUtils.h>

#include <konga/DataElement/DataElement.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/OperationLog/OperationInstance.h>

#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/PreviousTargetAsSource.h>
#include <konga/OperationObjects/ProcessedFiles.h>

#include "OperationCreatorDb.h"
#include "OperationEngine.h"
#include "DebugLog.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	                                                      
	                                                              
	               
	string GetTempDirectoryForTargetFiles(long lOperationId) {
		string dir(konga::getTmpDir());
		dir += "PreviousSourcesDir";
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		dir += "Op_";
		dir += toString(lOperationId);
		dir += "_";
		dir += GetGUIDString();
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		return dir;
	}
}

namespace {
	class SynchronousNextOperationImpl {
	public:
		SynchronousNextOperationImpl(const UserInfo& currentUser, konga::Debuggee* pDebuggee, long lNextOperationId, long long llParentOperationInstanceId,
			OperationDataElementHolder* pDeHolder, DbConnection& dbConnection,
			const string& stAlsoWriteFinalTargetToThisFile, const string& stFrom) :
		m_lNextOperationId(lNextOperationId),
		m_llParentOperationInstanceId(llParentOperationInstanceId),
		m_pDeHolder(pDeHolder),
		m_CurrentUser(currentUser),
		m_pDebuggee(pDebuggee),
		m_dbConnection(dbConnection),
		m_stAlsoWriteFinalTargetToThisFile(stAlsoWriteFinalTargetToThisFile),
		m_stFrom(stFrom)
		{}
		~SynchronousNextOperationImpl(void) {}

		void operator()() {
			                                                      
			try {
				OperationEngine opEngine(m_CurrentUser, m_pDebuggee, &m_dbConnection);
				opEngine.saveFinalTargetToThisFile(m_stAlsoWriteFinalTargetToThisFile);
				const long long llNextOperationInstanceId = NewOperationInstance(m_dbConnection, m_lNextOperationId, m_stFrom, m_llParentOperationInstanceId);
				opEngine.RunOperation(m_lNextOperationId, -1, llNextOperationInstanceId, m_pDeHolder, true);
			}
			catch ( const OperationEngineException& ex) {
				                                                                                           
				                                                               
				m_sError = ex.GetErrorString();
			}
			catch( const CrException& ex ) {
				m_sError = ex.GetErrorString();
			}
			catch( const std::exception& ex ) {
				m_sError = string("Unexpected std::exception, ") + ex.what();
			}
			catch(...) {
				m_sError = "Unknown exception.";
			}
		}

		string getError() const {
			return m_sError;
		}

	private:
		const long m_lNextOperationId;
		const long m_llParentOperationInstanceId;
		OperationDataElementHolder* m_pDeHolder;
		const UserInfo& m_CurrentUser;
		konga::Debuggee* m_pDebuggee;
		DbConnection& m_dbConnection;
		string m_sError;
		string m_stAlsoWriteFinalTargetToThisFile;
		string m_stFrom;
	};
}

namespace {
	const string stPREVIOUS_ERROR_DE_NAME("etlforge.operation.previous.error");
}

void OperationEngine::StartNextOperation(bool bIsSuccessOperation, const ProcessedFiles& files, const string& sError) {
	if ( m_pOperation == 0 )
		throw OperationEngineException("Internal Error: no operation was set when trying to start next operation, this is definately a bug.");

	long lNextOperationId = bIsSuccessOperation ? m_pOperation->getSuccessOperationId() : m_pOperation->getFailureOperationId();
	if ( lNextOperationId < 1 )
		return;

	                                                                                            
	if ( !bIsSuccessOperation ) {
		DataElement* errorDE = getOperationDataElementHolder().getOperationDataElement(stPREVIOUS_ERROR_DE_NAME);
		errorDE->setType(DataElement::t_string);
		errorDE->setValue(sError.c_str());
	}
	else {
		getOperationDataElementHolder().removeOperationDataElement(stPREVIOUS_ERROR_DE_NAME);
	}

	OperationCreatorDb operationCreator;
	if ( operationCreator.usesPreviousTargetAsSource(lNextOperationId, GetDbConnection()) ) {
		                                                              
		const string& dir = GetTempDirectoryForTargetFiles(m_lOperationId);
		const vector<ProcessedFile>& processedFiles = files.getProcessedFilesByRef();
		for( vector<ProcessedFile>::const_iterator cit = processedFiles.begin();
			cit != processedFiles.end(); ++cit )
		{
			const ProcessedFile& file = *cit;
			if ( file.hasError() )
				continue;
			const string& stLocalTargetFile = file.getLocalTargetLocation();
			if ( FileExists(stLocalTargetFile) ) {
				const string& leafName = konga::GetLeafPath(stLocalTargetFile);
				if ( leafName.empty() )
					continue;
				kCopyFile(stLocalTargetFile, dir + leafName);
			}
		}

		                        
		DataElement* const de = getOperationDataElementHolder().getOperationDataElement(PreviousTargetAsSource::GLOBAL_DE_NAME_FOR_SOURCE_PATH);
		de->setType(DataElement::t_string);
		de->setValue(dir.c_str());
	}

	if ( m_bSynchronous ) {
		SynchronousNextOperationImpl impl(m_CurrentUser, getOperationContext().getDebuggee(),
			lNextOperationId, m_llOperationInstanceId, m_pDeHolder, GetDbConnection(), m_stAlsoWriteFinalTargetToThisFile, bIsSuccessOperation ? "Success Operation" : "Failure Operation");
		konga::RunInNewThread(impl);
	}
	else {
		                                                                                       
		SetOperationDataElements();
		string stSubj( bIsSuccessOperation ? "Success " : "Failure " );
		stSubj += "Operation for Operation \"" + m_pOperation->getName() + "\"";

		OP_DEBUG_LOG(stSubj);
		konga::AddOperationToQueue(0, &GetDbConnection(), lNextOperationId, m_lSessionId, getOperationContext().getOperationInstanceId(), "OperationEngine", stSubj, m_CurrentUser.GetId());
		m_lSessionId = -1;
	}
	m_bStartedOtherOperation = true;
}

void OperationEngine::RunOperationAsync(long lOperationId, OperationDataElementHolder* pDeHolder, const string& subject) {
	if ( pDeHolder != 0 ) {
		if ( m_pDeHolder != 0 ) {
			delete m_pDeHolder;
		}
		m_pDeHolder = pDeHolder;
		m_bCreatedDeHolder = false;
	}
	else {
		if ( m_pDeHolder == 0 ) {
			m_pDeHolder = new DefaultOperationDataElementHolder(m_CurrentUser);
			m_bCreatedDeHolder = true;
		}
	}

	CheckForSourceOverride();
	SetOperationDataElements();
	konga::AddOperationToQueue(0, &GetDbConnection(), lOperationId, m_lSessionId, UNDEFINED_OPERATION_INSTANCE_ID, "Operation Engine", subject);
}

