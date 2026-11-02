  
                                                                               
                                                               
   

#if !defined(__JDBCENGINE_CLIENTSTUB_H__INCLUDED_)
#define __JDBCENGINE_CLIENTSTUB_H__INCLUDED_

#include <axis/client/Stub.hpp>
#include <axis/OtherFaultException.hpp>
#include <axis/ISoapAttachment.hpp>
#include <axis/ISoapFault.hpp>
AXIS_CPP_NAMESPACE_USE
#include "WsDbLookupResult.hpp"
#include "WsResultSetRow.hpp"
#include "ArrayOfWsResultSetRow.hpp"
#include "ArrayOfWsKeyValue.hpp"
#include "WsKeyValue.hpp"
#include "WsJdbcSessionId.hpp"
#include "WsJdbcStartNewSessionResult.hpp"
#include "ArrayOf_xsd_string.hpp"
#include "WsReadSourceParams.hpp"
#include "WsJdbcError.hpp"
#include "WsResultSetRow_Array.hpp"
#include "WsResultSet.hpp"
#include "WsKeyValue_Array.hpp"
#include "WsDbExecuteResult.hpp"
#include "WsDbLookupParams.hpp"
#include "WsWriteTargetParams.hpp"
#include "WsConnectionParams.hpp"

class JdbcEngine :public Stub
{
public:
	STORAGE_CLASS_INFO JdbcEngine(const char* pchEndpointUri, AXIS_PROTOCOL_TYPE eProtocol=APTHTTP1_1);
	STORAGE_CLASS_INFO JdbcEngine();
public:
	STORAGE_CLASS_INFO virtual ~JdbcEngine();
public: 
	STORAGE_CLASS_INFO WsJdbcStartNewSessionResult* startNewSession(xsd__string Value0, xsd__string Value1);
	STORAGE_CLASS_INFO WsJdbcError* closeSession(WsJdbcSessionId* Value0);
	STORAGE_CLASS_INFO WsDbLookupResult* dbLookup(WsJdbcSessionId* Value0, WsDbLookupParams* Value1);
	STORAGE_CLASS_INFO WsDbLookupResult* cachedDbLookup(WsJdbcSessionId* Value0, WsDbLookupParams* Value1);
	STORAGE_CLASS_INFO WsDbExecuteResult* dbExecute(WsJdbcSessionId* Value0, WsDbLookupParams* Value1, xsd__int Value2);
	STORAGE_CLASS_INFO WsJdbcError* commitTransactions(WsJdbcSessionId* Value0);
	STORAGE_CLASS_INFO WsJdbcError* rollbackTransactions(WsJdbcSessionId* Value0);
	STORAGE_CLASS_INFO WsJdbcError* readSource(WsJdbcSessionId* Value0, WsReadSourceParams* Value1);
	STORAGE_CLASS_INFO WsJdbcError* writeTarget(WsJdbcSessionId* Value0, WsWriteTargetParams* Value1);
};

#endif                                                    
