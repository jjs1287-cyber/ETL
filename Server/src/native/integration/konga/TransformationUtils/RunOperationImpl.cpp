/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                                             

#include <exception>
#include <vector>
#include <map>

#include <konga/Utils/StringUtils.h>	                   
#include <konga/Utils/ThreadUtils.h>	                 
#include <konga/Utils/CrException.h>

#include <konga/Messaging/OperationMessageUtils.h>	                          

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>                     

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/TransException.h>

#include <konga/OperationDataElements/OperationDataElementsManager.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/OperationLog/OperationInstance.h>
#include <konga/OperationLog/OperationLog.h>

#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationEngine/OperationEngine.h>

#include "RunOperationImpl.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

namespace {

	void RunAsyncOp(const long lOperationId, const long long llOperationInstanceId, OperationDataElementHolder* const deHolder, const UserInfo& currentUser) {
		try {
			long lSessionId = -1;
			if( deHolder )
				lSessionId = OperationDataElementsManager().AddDataElements( *deHolder );
			konga::AddOperationToQueue(lOperationId, lSessionId, llOperationInstanceId, "TransformationEngine", "RunOperation()", currentUser.GetId());
		} catch( const CrException& e ) {
			throw CrException("RunOperation failed to add operation to the queue. Reason: " + e.GetErrorString());
		}

	}

	class RunOperationImplFnc {
	public:
		RunOperationImplFnc(OperationEngine& engine, OperationDataElementHolder& deHolder, long lOperationId, long long llOperationInstanceId, string& sError)
			: m_engine(engine), m_deHolder(deHolder), m_lOperationId(lOperationId), m_llOperationInstanceId(llOperationInstanceId), m_sError(sError) {}

		void operator()() {
			try {
				m_engine.RunOperation(m_lOperationId, -1, m_llOperationInstanceId, &m_deHolder, true);
			}
			catch( const konga::CrException& e ) {
				m_sError = e.GetErrorString();
			}
			catch( const std::exception& e ) {
				m_sError = string("Unexpected std::exception: ") + e.what();
			}
			catch(...) {
				                                                                      
				m_sError = "Unexpected exception thrown in RunOperationImpl. This is most likely a bug, check the operation log for the operation that was executed using RunOperation";
			}
		}

	private:
		OperationEngine& m_engine;
		OperationDataElementHolder& m_deHolder;
		const long m_lOperationId;
		const long long m_llOperationInstanceId;
		string& m_sError;
	};

	void RunSyncOp(long lOperationId, long long llOperationInstanceId, OperationDataElementHolder* const deHolder, const OperationContext& context, Debuggee* pDebuggee) {
		string sError;
		try {
			OperationEngine opEngine(context.getCurrentUser(), pDebuggee);
			konga::RunInNewThread( RunOperationImplFnc(opEngine, *deHolder, lOperationId, llOperationInstanceId, sError) );
		} catch( const CrException& ex ) {
			sError = ex.GetErrorString();
		}
		if( !sError.empty() ) throw CrException("RunOperation failed: " + sError);
	}
}

namespace {
	string GetOperationGuidFromTagLocal(const std::string& op_tag) {
		if( op_tag.length() < 4 || !EqualsIgnoreCase(op_tag.substr(0, 3), "op.") )
			throw konga::ExeFcnException("Invalid Operation GUID (" + op_tag + ") passed to RunOperation(). Expected: 'op.guid'");

		return op_tag.substr(3);                 
	}

	                                                                                    
	                                                                                         
	class PreserveDataElements {
	public:
		PreserveDataElements(OperationDataElementHolder* const pDeHolder) : m_pDeHolder(pDeHolder) {
			                                           
			m_dataElementsToPreserve.push_back("etlforge.source.filenames");
			m_dataElementsToPreserve.push_back("etlforge.source.locators");
			m_dataElementsToPreserve.push_back("etlforge.source.sizes");
			m_dataElementsToPreserve.push_back("etlforge.target.file_count");
			copy();
		}

		~PreserveDataElements(void) {
			restore();
			clean();
		}

	private:
		void copy() {
			m_dataElementValues.clear();
			for( vector<string>::const_iterator cit = m_dataElementsToPreserve.begin(); cit != m_dataElementsToPreserve.end(); ++cit) {
				const string& name = *cit;
				if( m_pDeHolder->isOperationDataElementDefined(name) ) {
					DataElement* de = m_pDeHolder->getOperationDataElement(name);
					m_dataElementValues.insert( deMapT::value_type(name, new DataElement(*de)) );
				}
			}
		}

		void restore() {
			if ( m_pDeHolder == 0 || m_dataElementsToPreserve.empty() || m_dataElementValues.empty() ) return;

			for( vector<string>::const_iterator cit = m_dataElementsToPreserve.begin(); cit != m_dataElementsToPreserve.end(); ++cit ) {
				const string& name = *cit;
				deMapT::const_iterator deIt = m_dataElementValues.find(name);
				if ( deIt != m_dataElementValues.end() ) {
					DataElement* de = m_pDeHolder->getOperationDataElement(name);
					de->set(deIt->second);
				}
				else {
					m_pDeHolder->removeOperationDataElement(name);
				}
			}
		}

		void clean() {
			for( deMapT::const_iterator cit = m_dataElementValues.begin(); cit != m_dataElementValues.end(); ++cit ) {
				delete cit->second;
			}
			m_dataElementValues.clear();
		}

	private:
		OperationDataElementHolder* const m_pDeHolder;

		typedef map<string, DataElement*> deMapT;
		deMapT m_dataElementValues;
		vector<string> m_dataElementsToPreserve;
	};
}

bool konga::RunOperationImpl(const string& op_tag,
							 const bool bSync,
							 OperationDataElementHolder* const deHolder,
							 const OperationContext& context,
							 Debuggee* pDebuggee,
							 string& sError)
{
	const string& stOperationGuid = GetOperationGuidFromTagLocal(op_tag);
	try {
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		DbGuidToIdMapper idMapper( pTranDb );
		const long lOperationId = idMapper.GetId(stOperationGuid, OperationEntityId);		                                                
		if ( lOperationId == GuidToIdMapper::invalid_id )
			throw CrException("The requested operation (GUID=" + stOperationGuid + ") has not been deployed to this server.");

		if ( bSync ) {
			const long long llOperationInstanceId = konga::NewOperationInstance(*pTranDb, lOperationId, "RunOperation", context.getOperationInstanceId());
			OperationLog(*pTranDb).CreatePendingEntry(lOperationId, llOperationInstanceId);
			pTranDb.CloseAndDelete();

			PreserveDataElements dePreserve(deHolder);
			RunSyncOp(lOperationId, llOperationInstanceId, deHolder, context, pDebuggee);
		}
		else {
			RunAsyncOp(lOperationId, context.getOperationInstanceId(), deHolder, context.getCurrentUser());
		}

		sError.clear();
		return true;
	} catch( const CrException& e ) {
		sError = e.GetErrorString();
		return false;
	} catch( const std::exception& e ) {
		sError = string("Unexpected std::exception encountered in call to RunOperation. ") + e.what();
		return false;
	}
}

