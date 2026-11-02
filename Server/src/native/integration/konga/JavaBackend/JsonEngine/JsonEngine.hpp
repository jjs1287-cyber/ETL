  
                                                                               
                                                               
   

#if !defined(__JSONENGINE_CLIENTSTUB_H__INCLUDED_)
#define __JSONENGINE_CLIENTSTUB_H__INCLUDED_

#include <axis/client/Stub.hpp>
#include <axis/OtherFaultException.hpp>
#include <axis/ISoapAttachment.hpp>
#include <axis/ISoapFault.hpp>
AXIS_CPP_NAMESPACE_USE
#include "WsJsonError.hpp"
#include "WsStartNewJsonSessionResult.hpp"
#include "WsConversionParams.hpp"
#include "WsJsonSessionId.hpp"

class JsonEngine :public Stub
{
public:
	STORAGE_CLASS_INFO JsonEngine(const char* pchEndpointUri, AXIS_PROTOCOL_TYPE eProtocol=APTHTTP1_1);
	STORAGE_CLASS_INFO JsonEngine();
public:
	STORAGE_CLASS_INFO virtual ~JsonEngine();
public: 
	STORAGE_CLASS_INFO WsStartNewJsonSessionResult* startNewJsonSession(xsd__string Value0, xsd__string Value1);
	STORAGE_CLASS_INFO WsJsonError* jsonToXml(WsJsonSessionId* Value0, WsConversionParams* Value1);
	STORAGE_CLASS_INFO WsJsonError* xmlToJson(WsJsonSessionId* Value0, WsConversionParams* Value1);
	STORAGE_CLASS_INFO WsJsonError* closeJsonSession(WsJsonSessionId* Value0);
};

#endif                                                    
