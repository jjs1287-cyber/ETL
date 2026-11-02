  
                                                                               
                                                                        
   

#include "BulkLoadEngine.hpp"

#include <axis/AxisWrapperAPI.hpp>
#include <axis/Axis.hpp>

extern int Axis_DeSerialize_WsStartNewBulkLoadSessionResult(WsStartNewBulkLoadSessionResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsStartNewBulkLoadSessionResult(WsStartNewBulkLoadSessionResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsStartNewBulkLoadSessionResult(WsStartNewBulkLoadSessionResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsStartNewBulkLoadSessionResult(WsStartNewBulkLoadSessionResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsStartNewBulkLoadSessionResult();

extern int Axis_DeSerialize_WsSalesforceEndpoint(WsSalesforceEndpoint* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsSalesforceEndpoint(WsSalesforceEndpoint *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsSalesforceEndpoint(WsSalesforceEndpoint* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsSalesforceEndpoint(WsSalesforceEndpoint* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsSalesforceEndpoint();

extern int Axis_DeSerialize_WsKeyValue(WsKeyValue* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsKeyValue(WsKeyValue *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsKeyValue(WsKeyValue* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsKeyValue(WsKeyValue* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsKeyValue();

extern int Axis_DeSerialize_WsBulkLoadResult(WsBulkLoadResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsBulkLoadResult(WsBulkLoadResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsBulkLoadResult(WsBulkLoadResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsBulkLoadResult(WsBulkLoadResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsBulkLoadResult();

extern int Axis_DeSerialize_WsBulkLoadSessionId(WsBulkLoadSessionId* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsBulkLoadSessionId(WsBulkLoadSessionId *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsBulkLoadSessionId(WsBulkLoadSessionId* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsBulkLoadSessionId(WsBulkLoadSessionId* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsBulkLoadSessionId();

extern int Axis_DeSerialize_WsBulkLoadParams(WsBulkLoadParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsBulkLoadParams(WsBulkLoadParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsBulkLoadParams(WsBulkLoadParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsBulkLoadParams(WsBulkLoadParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsBulkLoadParams();

extern int Axis_DeSerialize_WsBulkLoadError(WsBulkLoadError* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsBulkLoadError(WsBulkLoadError *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsBulkLoadError(WsBulkLoadError* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsBulkLoadError(WsBulkLoadError* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsBulkLoadError();

BulkLoadEngine::BulkLoadEngine(const char* pcEndpointUri, AXIS_PROTOCOL_TYPE eProtocol):Stub(pcEndpointUri, eProtocol)
{
}

BulkLoadEngine::BulkLoadEngine()
:Stub(" ", APTHTTP1_1)
{
	m_pCall->setEndpointURI("http://localhost/axis/konga_bulkload_engine");
}

BulkLoadEngine::~BulkLoadEngine()
{
}


                                                    

  
                                                             
   
WsStartNewBulkLoadSessionResult* BulkLoadEngine::startNewBulkLoadSession(xsd__string Value0, xsd__string Value1)
{
	WsStartNewBulkLoadSessionResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "startNewBulkLoadSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("startNewBulkLoadSession", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[23];
	sprintf( cPrefixAndParamName0, "%s:etlforgeUser", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value0, cPrefixAndParamName0, XSD_STRING);
	char cPrefixAndParamName1[16];
	sprintf( cPrefixAndParamName1, "%s:md5Pwd", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value1, cPrefixAndParamName1, XSD_STRING);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("startNewBulkLoadSessionResponse", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid"))
		{
			pReturn = (WsStartNewBulkLoadSessionResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsStartNewBulkLoadSessionResult, (void*) Axis_Create_WsStartNewBulkLoadSessionResult, (void*) Axis_Delete_WsStartNewBulkLoadSessionResult,"startNewBulkLoadSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_bulkload_engine" );

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


  
                                                 
   
WsBulkLoadResult* BulkLoadEngine::runBulkLoad(WsBulkLoadSessionId* Value0, WsBulkLoadParams* Value1, WsSalesforceEndpoint* Value2, WsKeyValue_Array* Value3)
{
	WsBulkLoadResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "runBulkLoad");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("runBulkLoad", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[19];
	sprintf( cPrefixAndParamName0, "%s:sessionId", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsBulkLoadSessionId, (void*)Axis_Delete_WsBulkLoadSessionId, cPrefixAndParamName0, Axis_URI_WsBulkLoadSessionId);
	char cPrefixAndParamName1[24];
	sprintf( cPrefixAndParamName1, "%s:bulkloadParams", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsBulkLoadParams, (void*)Axis_Delete_WsBulkLoadParams, cPrefixAndParamName1, Axis_URI_WsBulkLoadParams);
	char cPrefixAndParamName2[28];
	sprintf( cPrefixAndParamName2, "%s:salesforceEndpoint", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value2, (void*)Axis_Serialize_WsSalesforceEndpoint, (void*)Axis_Delete_WsSalesforceEndpoint, cPrefixAndParamName2, Axis_URI_WsSalesforceEndpoint);
	char cPrefixAndParamName3[13];
	sprintf( cPrefixAndParamName3, "%s:in3", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addCmplxArrayParameter(Value3, (void*)Axis_Serialize_WsKeyValue, (void*)Axis_Delete_WsKeyValue, (void*) Axis_GetSize_WsKeyValue, "in3", Axis_URI_WsKeyValue);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("runBulkLoadResponse", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid"))
		{
			pReturn = (WsBulkLoadResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsBulkLoadResult, (void*) Axis_Create_WsBulkLoadResult, (void*) Axis_Delete_WsBulkLoadResult,"runBulkLoadReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_bulkload_engine" );

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


  
                                                          
   
WsBulkLoadError* BulkLoadEngine::closeBulkLoadSession(WsBulkLoadSessionId* Value0)
{
	WsBulkLoadError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "closeBulkLoadSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("closeBulkLoadSession", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[19];
	sprintf( cPrefixAndParamName0, "%s:sessionId", m_pCall->getNamespacePrefix("http://bulkload.webservice.implementation.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsBulkLoadSessionId, (void*)Axis_Delete_WsBulkLoadSessionId, cPrefixAndParamName0, Axis_URI_WsBulkLoadSessionId);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("closeBulkLoadSessionResponse", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid"))
		{
			pReturn = (WsBulkLoadError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsBulkLoadError, (void*) Axis_Create_WsBulkLoadError, (void*) Axis_Delete_WsBulkLoadError,"closeBulkLoadSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_bulkload_engine" );

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

