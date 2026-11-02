  
                                                                               
                                                                        
   

#include "JdbcEngine.hpp"

#include <axis/AxisWrapperAPI.hpp>
#include <axis/Axis.hpp>

extern int Axis_DeSerialize_WsDbLookupResult(WsDbLookupResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsDbLookupResult(WsDbLookupResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsDbLookupResult(WsDbLookupResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsDbLookupResult(WsDbLookupResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsDbLookupResult();

extern int Axis_DeSerialize_WsResultSetRow(WsResultSetRow* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsResultSetRow(WsResultSetRow *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsResultSetRow(WsResultSetRow* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsResultSetRow(WsResultSetRow* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsResultSetRow();

extern int Axis_DeSerialize_ArrayOfWsResultSetRow(ArrayOfWsResultSetRow* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_ArrayOfWsResultSetRow(ArrayOfWsResultSetRow *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_ArrayOfWsResultSetRow(ArrayOfWsResultSetRow* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_ArrayOfWsResultSetRow(ArrayOfWsResultSetRow* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_ArrayOfWsResultSetRow();

extern int Axis_DeSerialize_ArrayOfWsKeyValue(ArrayOfWsKeyValue* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_ArrayOfWsKeyValue(ArrayOfWsKeyValue *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_ArrayOfWsKeyValue(ArrayOfWsKeyValue* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_ArrayOfWsKeyValue(ArrayOfWsKeyValue* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_ArrayOfWsKeyValue();

extern int Axis_DeSerialize_WsKeyValue(WsKeyValue* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsKeyValue(WsKeyValue *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsKeyValue(WsKeyValue* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsKeyValue(WsKeyValue* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsKeyValue();

extern int Axis_DeSerialize_WsJdbcSessionId(WsJdbcSessionId* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsJdbcSessionId(WsJdbcSessionId *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsJdbcSessionId(WsJdbcSessionId* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsJdbcSessionId(WsJdbcSessionId* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsJdbcSessionId();

extern int Axis_DeSerialize_WsJdbcStartNewSessionResult(WsJdbcStartNewSessionResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsJdbcStartNewSessionResult(WsJdbcStartNewSessionResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsJdbcStartNewSessionResult(WsJdbcStartNewSessionResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsJdbcStartNewSessionResult(WsJdbcStartNewSessionResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsJdbcStartNewSessionResult();

extern int Axis_DeSerialize_ArrayOf_xsd_string(ArrayOf_xsd_string* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_ArrayOf_xsd_string(ArrayOf_xsd_string *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_ArrayOf_xsd_string(ArrayOf_xsd_string* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_ArrayOf_xsd_string(ArrayOf_xsd_string* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_ArrayOf_xsd_string();

extern int Axis_DeSerialize_WsReadSourceParams(WsReadSourceParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsReadSourceParams(WsReadSourceParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsReadSourceParams(WsReadSourceParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsReadSourceParams(WsReadSourceParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsReadSourceParams();

extern int Axis_DeSerialize_WsJdbcError(WsJdbcError* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsJdbcError(WsJdbcError *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsJdbcError(WsJdbcError* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsJdbcError(WsJdbcError* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsJdbcError();

extern int Axis_DeSerialize_WsResultSet(WsResultSet* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsResultSet(WsResultSet *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsResultSet(WsResultSet* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsResultSet(WsResultSet* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsResultSet();

extern int Axis_DeSerialize_WsDbExecuteResult(WsDbExecuteResult* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsDbExecuteResult(WsDbExecuteResult *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsDbExecuteResult(WsDbExecuteResult* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsDbExecuteResult(WsDbExecuteResult* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsDbExecuteResult();

extern int Axis_DeSerialize_WsDbLookupParams(WsDbLookupParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsDbLookupParams(WsDbLookupParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsDbLookupParams(WsDbLookupParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsDbLookupParams(WsDbLookupParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsDbLookupParams();

extern int Axis_DeSerialize_WsWriteTargetParams(WsWriteTargetParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsWriteTargetParams(WsWriteTargetParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsWriteTargetParams(WsWriteTargetParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsWriteTargetParams(WsWriteTargetParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsWriteTargetParams();

extern int Axis_DeSerialize_WsConnectionParams(WsConnectionParams* param, IWrapperSoapDeSerializer* pDZ);
extern void* Axis_Create_WsConnectionParams(WsConnectionParams *Obj, bool bArray = false, int nSize=0);
extern void Axis_Delete_WsConnectionParams(WsConnectionParams* param, bool bArray = false, int nSize=0);
extern int Axis_Serialize_WsConnectionParams(WsConnectionParams* param, IWrapperSoapSerializer* pSZ, bool bArray = false);
extern int Axis_GetSize_WsConnectionParams();

JdbcEngine::JdbcEngine(const char* pcEndpointUri, AXIS_PROTOCOL_TYPE eProtocol):Stub(pcEndpointUri, eProtocol)
{
}

JdbcEngine::JdbcEngine()
:Stub(" ", APTHTTP1_1)
{
	m_pCall->setEndpointURI("http://localhost/axis/konga_jdbc_engine");
}

JdbcEngine::~JdbcEngine()
{
}


                                                    

  
                                                     
   
WsJdbcStartNewSessionResult* JdbcEngine::startNewSession(xsd__string Value0, xsd__string Value1)
{
	WsJdbcStartNewSessionResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "startNewSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("startNewSession", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value0, cPrefixAndParamName0, XSD_STRING);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)Value1, cPrefixAndParamName1, XSD_STRING);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("startNewSessionResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcStartNewSessionResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcStartNewSessionResult, (void*) Axis_Create_WsJdbcStartNewSessionResult, (void*) Axis_Delete_WsJdbcStartNewSessionResult,"startNewSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                  
   
WsJdbcError* JdbcEngine::closeSession(WsJdbcSessionId* Value0)
{
	WsJdbcError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "closeSession");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("closeSession", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("closeSessionResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcError, (void*) Axis_Create_WsJdbcError, (void*) Axis_Delete_WsJdbcError,"closeSessionReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                              
   
WsDbLookupResult* JdbcEngine::dbLookup(WsJdbcSessionId* Value0, WsDbLookupParams* Value1)
{
	WsDbLookupResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "dbLookup");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("dbLookup", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsDbLookupParams, (void*)Axis_Delete_WsDbLookupParams, cPrefixAndParamName1, Axis_URI_WsDbLookupParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("dbLookupResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsDbLookupResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsDbLookupResult, (void*) Axis_Create_WsDbLookupResult, (void*) Axis_Delete_WsDbLookupResult,"dbLookupReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                    
   
WsDbLookupResult* JdbcEngine::cachedDbLookup(WsJdbcSessionId* Value0, WsDbLookupParams* Value1)
{
	WsDbLookupResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "cachedDbLookup");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("cachedDbLookup", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsDbLookupParams, (void*)Axis_Delete_WsDbLookupParams, cPrefixAndParamName1, Axis_URI_WsDbLookupParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("cachedDbLookupResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsDbLookupResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsDbLookupResult, (void*) Axis_Create_WsDbLookupResult, (void*) Axis_Delete_WsDbLookupResult,"cachedDbLookupReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                               
   
WsDbExecuteResult* JdbcEngine::dbExecute(WsJdbcSessionId* Value0, WsDbLookupParams* Value1, xsd__int Value2)
{
	WsDbExecuteResult* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "dbExecute");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("dbExecute", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsDbLookupParams, (void*)Axis_Delete_WsDbLookupParams, cPrefixAndParamName1, Axis_URI_WsDbLookupParams);
	char cPrefixAndParamName2[13];
	sprintf( cPrefixAndParamName2, "%s:in2", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addParameter((void*)&Value2, cPrefixAndParamName2, XSD_INT);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("dbExecuteResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsDbExecuteResult*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsDbExecuteResult, (void*) Axis_Create_WsDbExecuteResult, (void*) Axis_Delete_WsDbExecuteResult,"dbExecuteReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                        
   
WsJdbcError* JdbcEngine::commitTransactions(WsJdbcSessionId* Value0)
{
	WsJdbcError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "commitTransactions");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("commitTransactions", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("commitTransactionsResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcError, (void*) Axis_Create_WsJdbcError, (void*) Axis_Delete_WsJdbcError,"commitTransactionsReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                          
   
WsJdbcError* JdbcEngine::rollbackTransactions(WsJdbcSessionId* Value0)
{
	WsJdbcError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "rollbackTransactions");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("rollbackTransactions", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("rollbackTransactionsResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcError, (void*) Axis_Create_WsJdbcError, (void*) Axis_Delete_WsJdbcError,"rollbackTransactionsReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                
   
WsJdbcError* JdbcEngine::readSource(WsJdbcSessionId* Value0, WsReadSourceParams* Value1)
{
	WsJdbcError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "readSource");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("readSource", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsReadSourceParams, (void*)Axis_Delete_WsReadSourceParams, cPrefixAndParamName1, Axis_URI_WsReadSourceParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("readSourceResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcError, (void*) Axis_Create_WsJdbcError, (void*) Axis_Delete_WsJdbcError,"readSourceReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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


  
                                                 
   
WsJdbcError* JdbcEngine::writeTarget(WsJdbcSessionId* Value0, WsWriteTargetParams* Value1)
{
	WsJdbcError* pReturn = NULL;
	const char* pcCmplxFaultName;
	pcCmplxFaultName = NULL;
	try
	{	if (AXIS_SUCCESS != m_pCall->initialize(CPP_DOC_PROVIDER)) return pReturn;
		if (NULL==m_pCall->getTransportProperty("SOAPAction",false))
	{
		m_pCall->setTransportProperty(SOAPACTION_HEADER , "writeTarget");
	}
	m_pCall->setSOAPVersion(SOAP_VER_1_1);
	m_pCall->setOperation("writeTarget", "http://jdbc.webservice.backend.server.integration.etlforge.invalid");
	includeSecure();
	applyUserPreferences();
	char cPrefixAndParamName0[13];
	sprintf( cPrefixAndParamName0, "%s:in0", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value0, (void*)Axis_Serialize_WsJdbcSessionId, (void*)Axis_Delete_WsJdbcSessionId, cPrefixAndParamName0, Axis_URI_WsJdbcSessionId);
	char cPrefixAndParamName1[13];
	sprintf( cPrefixAndParamName1, "%s:in1", m_pCall->getNamespacePrefix("http://jdbc.webservice.backend.server.integration.etlforge.invalid"));
	m_pCall->addCmplxParameter(Value1, (void*)Axis_Serialize_WsWriteTargetParams, (void*)Axis_Delete_WsWriteTargetParams, cPrefixAndParamName1, Axis_URI_WsWriteTargetParams);
	if (AXIS_SUCCESS == m_pCall->invoke())
	{
		if(AXIS_SUCCESS == m_pCall->checkMessage("writeTargetResponse", "http://jdbc.webservice.backend.server.integration.etlforge.invalid"))
		{
			pReturn = (WsJdbcError*)m_pCall->getCmplxObject((void*) Axis_DeSerialize_WsJdbcError, (void*) Axis_Create_WsJdbcError, (void*) Axis_Delete_WsJdbcError,"writeTargetReturn", 0);
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
			m_pCall->checkFault("Fault","http://localhost/axis/konga_jdbc_engine" );

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

