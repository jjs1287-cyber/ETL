  
                                                                               
                                                                        
   

#include "JsonEngine.hpp"

#include <axis/AxisWrapperAPI.hpp>
#include <axis/Axis.hpp>

extern int Axis_DeSerialize_WsJsonError(WsJsonError* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsJsonError(WsJsonError *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsJsonError(WsJsonError* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsJsonError(WsJsonError* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsJsonError();

extern int Axis_DeSerialize_WsStartNewJsonSessionResult(WsStartNewJsonSessionResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsStartNewJsonSessionResult(WsStartNewJsonSessionResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsStartNewJsonSessionResult(WsStartNewJsonSessionResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsStartNewJsonSessionResult(WsStartNewJsonSessionResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsStartNewJsonSessionResult();

extern int Axis_DeSerialize_WsConversionParams(WsConversionParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsConversionParams(WsConversionParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsConversionParams(WsConversionParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsConversionParams(WsConversionParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsConversionParams();

extern int Axis_DeSerialize_WsJsonSessionId(WsJsonSessionId* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsJsonSessionId(WsJsonSessionId *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsJsonSessionId(WsJsonSessionId* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsJsonSessionId(WsJsonSessionId* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsJsonSessionId();

JsonEngine::JsonEngine(const char* pcEndpointUri, AXIS_PROTOCOL_TYPE eProtocol):Stub(pcEndpointUri, eProtocol)
{
}

JsonEngine::JsonEngine()
:Stub(" ", APTHTTP1_1)
{
	m_pCall->setEndpointURI("http://localhost/axis/konga_json_engine");
}

JsonEngine::~JsonEngine()
{
}


                                                    

  
                                                         
   
WsStartNewJsonSessionResult* JsonEngine::startNewJsonSession(xsd__string Value0, xsd__string Value1)
{
	WsStartNewJsonSessionResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "startNewJsonSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("startNewJsonSession", "http://json.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[23];
	sprintf( cPrefixAndParamName0, "%s:ETLForgeUser", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value0, cPrefixAndParamName0, XSD_STRING);
	char cPrefixAndParamName1[16];
	sprintf( cPrefixAndParamName1, "%s:Md5Pwd", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value1, cPrefixAndParamName1, XSD_STRING);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("startNewJsonSessionResponse", "http://json.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsStartNewJsonSessionResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsStartNewJsonSessionResult, (void*) Axis_Create_WsStartNewJsonSessionResult, (void*) Axis_Delete_WsStartNewJsonSessionResult,"startNewJsonSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_json_engine" );

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


  
                                               
   
WsJsonError* JsonEngine::jsonToXml(WsJsonSessionId* Value0, WsConversionParams* Value1)
{
	WsJsonError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "jsonToXml");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("jsonToXml", "http://json.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[19];
	sprintf( cPrefixAndParamName0, "%s:SessionId", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJsonSessionId, (void*)Axis_Delete_WsJsonSessionId, cPrefixAndParamName0, Axis_URI_WsJsonSessionId);
	char cPrefixAndParamName1[26];
	sprintf( cPrefixAndParamName1, "%s:ConversionParams", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsConversionParams, (void*)Axis_Delete_WsConversionParams, cPrefixAndParamName1, Axis_URI_WsConversionParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("jsonToXmlResponse", "http://json.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJsonError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJsonError, (void*) Axis_Create_WsJsonError, (void*) Axis_Delete_WsJsonError,"jsonToXmlReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_json_engine" );

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


  
                                               
   
WsJsonError* JsonEngine::xmlToJson(WsJsonSessionId* Value0, WsConversionParams* Value1)
{
	WsJsonError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "xmlToJson");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("xmlToJson", "http://json.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[19];
	sprintf( cPrefixAndParamName0, "%s:SessionId", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJsonSessionId, (void*)Axis_Delete_WsJsonSessionId, cPrefixAndParamName0, Axis_URI_WsJsonSessionId);
	char cPrefixAndParamName1[26];
	sprintf( cPrefixAndParamName1, "%s:ConversionParams", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsConversionParams, (void*)Axis_Delete_WsConversionParams, cPrefixAndParamName1, Axis_URI_WsConversionParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("xmlToJsonResponse", "http://json.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJsonError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJsonError, (void*) Axis_Create_WsJsonError, (void*) Axis_Delete_WsJsonError,"xmlToJsonReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_json_engine" );

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


  
                                                      
   
WsJsonError* JsonEngine::closeJsonSession(WsJsonSessionId* Value0)
{
	WsJsonError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "closeJsonSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("closeJsonSession", "http://json.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[19];
	sprintf( cPrefixAndParamName0, "%s:SessionId", m_pCall->getNamespacePrefix("http://json.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJsonSessionId, (void*)Axis_Delete_WsJsonSessionId, cPrefixAndParamName0, Axis_URI_WsJsonSessionId);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("closeJsonSessionResponse", "http://json.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJsonError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJsonError, (void*) Axis_Create_WsJsonError, (void*) Axis_Delete_WsJsonError,"closeJsonSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_json_engine" );

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

