  
                                                                               
                                                                        
   

#include "ScriptEngine.hpp"

#include <axis/AxisWrapperAPI.hpp>
#include <axis/Axis.hpp>

extern int Axis_DeSerialize_Result(Result* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_Result(Result *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_Result(Result* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_Result(Result* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_Result();

extern int Axis_DeSerialize_Binding(Binding* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_Binding(Binding *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_Binding(Binding* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_Binding(Binding* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_Binding();

ScriptEngine::ScriptEngine(const char* pcEndpointUri, AXIS_PROTOCOL_TYPE eProtocol):Stub(pcEndpointUri, eProtocol)
{
}

ScriptEngine::ScriptEngine()
:Stub(" ", APTHTTP1_1)
{
	m_pCall->setEndpointURI("http://localhost/axis/konga_script_engine");
}

ScriptEngine::~ScriptEngine()
{
}


                                                    

  
                                                     
   
Result* ScriptEngine::runGroovyScript(xsd__string Value0, xsd__string Value1, xsd__string Value2, Binding_Array* Value3)
{
	Result* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "runGroovyScript");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("runGroovyScript", "http://scriptengine.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[14];
	sprintf( cPrefixAndParamName0, "%s:user", m_pCall->getNamespacePrefix("http://scriptengine.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value0, cPrefixAndParamName0, XSD_STRING);
	char cPrefixAndParamName1[21];
	sprintf( cPrefixAndParamName1, "%s:md5Password", m_pCall->getNamespacePrefix("http://scriptengine.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value1, cPrefixAndParamName1, XSD_STRING);
	char cPrefixAndParamName2[20];
	sprintf( cPrefixAndParamName2, "%s:scriptName", m_pCall->getNamespacePrefix("http://scriptengine.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value2, cPrefixAndParamName2, XSD_STRING);
	char cPrefixAndParamName3[18];
	sprintf( cPrefixAndParamName3, "%s:bindings", m_pCall->getNamespacePrefix("http://scriptengine.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxArrayParameter(Value3, (void*)Axis_Serialize_Binding, (void*)Axis_Delete_Binding, (void*) Axis_GetSize_Binding, "bindings", Axis_URI_Binding);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("runGroovyScriptResponse", "http://scriptengine.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (Result*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_Result, (void*) Axis_Create_Result, (void*) Axis_Delete_Result,"runGroovyScriptReturn", 0);
		}
	}
	m_pCall->unInitialize();
	return pReturn;
	}
	catch(AxisException& e)
	{
		int iExceptionCode = e.getExceptionCode();

		if(AXISC_NODE_VALUE_MISMATCH_EXCEPTION != iExceptionCode)
		{
			m_pCall->unInitialize();
			throw;
		}

		ISoapFault* pSoapFault = (ISoapFault*)
			m_pCall->checkFault("Fault","http://localhost/axis/konga_script_engine" );

		if(pSoapFault)
		{
			const char *detail = pSoapFault->getSimpleFaultDetail();
			bool deleteDetail=false;

			if (NULL==detail || 0==strlen(detail))
			{
				detail=m_pCall->getFaultAsXMLString();
				if (NULL==detail)
				{
					detail="";
				}
				else
				{
					deleteDetail=true;
				}
			}

			OtherFaultException ofe(pSoapFault->getFaultcode(),
				pSoapFault->getFaultstring(), pSoapFault->getFaultactor(),
				detail, iExceptionCode);

			if (deleteDetail && NULL!=detail)
			{
				Axis::AxisDelete( (void *) const_cast<char*>(detail), XSD_STRING);
			}

			m_pCall->unInitialize();
			delete pSoapFault;
			throw ofe;
		}
		else
		{
			m_pCall->unInitialize();
			delete pSoapFault;
			throw;
		}
	}
}

