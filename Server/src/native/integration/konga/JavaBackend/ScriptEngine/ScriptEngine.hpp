  
                                                                               
                                                               
   

#if !defined(__SCRIPTENGINE_CLIENTSTUB_H__INCLUDED_)
#define __SCRIPTENGINE_CLIENTSTUB_H__INCLUDED_

#include <axis/client/Stub.hpp>
#include <axis/OtherFaultException.hpp>
#include <axis/ISoapAttachment.hpp>
#include <axis/ISoapFault.hpp>
AXIS_CPP_NAMESPACE_USE
#include "Binding_Array.hpp"
#include "Result.hpp"
#include "Binding.hpp"

class ScriptEngine :public Stub
{
public:
	STORAGE_CLASS_INFO ScriptEngine(const char* pchEndpointUri, AXIS_PROTOCOL_TYPE eProtocol=APTHTTP1_1);
	STORAGE_CLASS_INFO ScriptEngine();
public:
	STORAGE_CLASS_INFO virtual ~ScriptEngine();
public: 
	STORAGE_CLASS_INFO Result* runGroovyScript(xsd__string Value0, xsd__string Value1, xsd__string Value2, Binding_Array * Value3);
};

#endif                                                      
