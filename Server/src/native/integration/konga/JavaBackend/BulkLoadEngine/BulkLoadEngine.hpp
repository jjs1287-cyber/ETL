  
                                                                               
                                                               
   

#if !defined(__BULKLOADENGINE_CLIENTSTUB_H__INCLUDED_)
#define __BULKLOADENGINE_CLIENTSTUB_H__INCLUDED_

#include <axis/client/Stub.hpp>
#include <axis/OtherFaultException.hpp>
#include <axis/ISoapAttachment.hpp>
#include <axis/ISoapFault.hpp>
AXIS_CPP_NAMESPACE_USE
#include "WsStartNewBulkLoadSessionResult.hpp"
#include "WsSalesforceEndpoint.hpp"
#include "WsKeyValue.hpp"
#include "WsBulkLoadResult.hpp"
#include "WsKeyValue_Array.hpp"
#include "WsBulkLoadSessionId.hpp"
#include "WsBulkLoadParams.hpp"
#include "WsBulkLoadError.hpp"

class BulkLoadEngine :public Stub
{
public:
	STORAGE_CLASS_INFO BulkLoadEngine(const char* pchEndpointUri, AXIS_PROTOCOL_TYPE eProtocol=APTHTTP1_1);
	STORAGE_CLASS_INFO BulkLoadEngine();
public:
	STORAGE_CLASS_INFO virtual ~BulkLoadEngine();
public: 
	STORAGE_CLASS_INFO WsStartNewBulkLoadSessionResult* startNewBulkLoadSession(xsd__string Value0, xsd__string Value1);
	STORAGE_CLASS_INFO WsBulkLoadResult* runBulkLoad(WsBulkLoadSessionId* Value0, WsBulkLoadParams* Value1, WsSalesforceEndpoint* Value2, WsKeyValue_Array * Value3);
	STORAGE_CLASS_INFO WsBulkLoadError* closeBulkLoadSession(WsBulkLoadSessionId* Value0);
};

#endif                                                        
