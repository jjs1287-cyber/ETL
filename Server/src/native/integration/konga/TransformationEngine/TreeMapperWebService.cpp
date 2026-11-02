/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                                           

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>
#include <konga/Utils/PortDefs.h>			                                          
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>

#include <konga/Soap/SoapFault_11.h>
#include <konga/Soap/SoapEnvelope.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/TransformationChainLinksTab.h>
#include <konga/Db/TranDb/TransformationFunctionTab.h>

#include <konga/OperationObjects/OperationType.h>

#include <konga/Server/Server.h>
#include <konga/Server/WDILogFileUtils.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ViewLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "WebServiceHandler.h"
#include "misc.h"

using namespace konga;
using namespace konga::xml;
using namespace TranDb;
using std::string;


namespace {
	const string NRETRIES_SYSTEM_VARIABLE("etlforge.web_service_call.number_of_retries");
	const string RETRY_WAIT_SYSTEM_VARIABLE("etlforge.web_service_call.retry_wait_seconds");
	const string MAX_REDIR_SYSTEM_VARIABLE("etlforge.web_service_call.max_redirs");
	const string TIME_OUT_SYSTEM_VARIABLE("etlforge.web_service_call.time_out");
}

unsigned long TreeMapper::CallWebService(int iWebServiceId,
								   const string& stInputDataFilename,
								   const string& stOutputDataFilename)
{
	assert(m_TransformationWebServiceInfo);
	if ( !m_TransformationWebServiceInfo ) {
		throw InternalErrorException("No web service info is set. Is the Web Service Url field empty?");
	}

	string stTempFilename( stInputDataFilename );
	if( stTempFilename.empty() )
	{
		stTempFilename = getOperationContext()->getLocalTmpDirForOperation() + "_empty_input";
		FILE* f = fopen( stTempFilename.c_str(),"w+" );
		fclose(f);
	}

	const string& stUrl = m_TransformationWebServiceInfo->Url;
	const string stRequestXmlFileName(stTempFilename + "_req.xml");
	const string stResponseXmlFileName(stTempFilename + "_res.xml");

	OperationDataElementHolder* const deHolder = getOperationDataElementHolder();
	const int nRetries = konga::GetSystemVariableAsLong(NRETRIES_SYSTEM_VARIABLE, deHolder, 0);
	const int retryWaitInSeconds = konga::GetSystemVariableAsLong(RETRY_WAIT_SYSTEM_VARIABLE, deHolder, 0);
	const int max_redirs = konga::GetSystemVariableAsLong(MAX_REDIR_SYSTEM_VARIABLE, deHolder, 0);
	const int time_out_secs = konga::GetSystemVariableAsLong(TIME_OUT_SYSTEM_VARIABLE, deHolder, 3600);

	                                      
	WebServiceInfo wsInfo(stUrl,
						m_TransformationWebServiceInfo->SoapAction,
						m_TransformationWebServiceInfo->TargetNamespace,
						m_TransformationWebServiceInfo->OtherNamespaces,
						m_TransformationWebServiceInfo->SoapStyle,
						m_TransformationWebServiceInfo->SoapBodyUse,
						iWebServiceId,
						nRetries, retryWaitInSeconds, max_redirs, time_out_secs,
						m_TransformationWebServiceInfo->HttpUserName,
						m_TransformationWebServiceInfo->HttpPassword);

	WebServiceIO wsIO(stInputDataFilename,
						stOutputDataFilename,
						stRequestXmlFileName,
						stResponseXmlFileName);

	WebServiceRestrictions wsRestrictions(m_TransformationWebServiceInfo->DelayMS,
										  m_TransformationWebServiceInfo->MaxSizeBytes);

	WebServiceHandler wsHandler(this,
						wsInfo, 
						wsIO, 
						*getWebServiceStructure(), 
						wsRestrictions,
						m_TransformationWebServiceInfo->HttpPostOptions,
						*getErrorLog(),
						*getViewLog());

	                       
	unsigned long hr = wsHandler.CallWebService();
	if (FAILED(hr)) {
		const string& stUrlReplaced = getOperationDataElementHolder()->replaceOperationDataElementInString(stUrl);
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_WSTRANSFORMATION_FAILED, stUrlReplaced.c_str(), wsHandler.GetLastError().c_str());
		if (m_bTestOperation) {
			AppendErrorMessage(wsHandler.GetLastError().c_str());
		}
		return hr;
	}

	                                                                    
	  

	if ( wsHandler.HasEmptyInput() )
		return 1;

	return hr;
}


unsigned long TreeMapper::GetHostedWebServiceInfo(TreeMapper::TransformationPoint transPoint, 
										    DbConnection* pTranDbConn)
{
	unsigned long hr = S_OK;
	try
	{
        SmartDbPtr<TransformationChainLinksTab> linksTab( TransformationChainLinksTab::MakeInstance(pTranDbConn) );
		string stLinksTabFilter;
		if( transPoint == TreeMapper::Source )
			stLinksTabFilter = "WHERE NextTransformation_ID = " + konga::toString(m_TransformationID);
		else
			stLinksTabFilter = "WHERE Transformation_ID = " + konga::toString(m_TransformationID);
		linksTab->Select(stLinksTabFilter);
		if( linksTab->Next() )
		{
			int iSearchedForTransID = (transPoint == TreeMapper::Source) ? 
					linksTab->GetTransformation_ID() : linksTab->GetNextTransformation_ID();

            SmartDbPtr<TransformationFunctionTab> funcTab( TransformationFunctionTab::MakeInstance(pTranDbConn) );
			string stFuncTabFilter = "WHERE Transformation_ID = " + konga::toString(iSearchedForTransID);
			funcTab->Select(stFuncTabFilter);
			if( funcTab->Next() )
			{
				int iFunctionID = funcTab->GetFunction_ID();
				if( iFunctionID > 0 )
				{
					hr = GetWebServiceInfo(pTranDbConn, iFunctionID);
					if( FAILED(hr) )
						return hr;
				}
				else
				{
					AppendErrorMessage(KString("The transformation with ID = ") + konga::toString(iSearchedForTransID) + " is not a webservice");
					return E_FAIL;
				}
			}
			else
			{
				AppendErrorMessage(KString("The transformation with ID = ") + konga::toString(iSearchedForTransID)+ " is not a webservice");
				return E_FAIL;
			}
		}
		else 
		{
			AppendErrorMessage(KString("There is no webservice transformation in the chain leading to or from the ") +
				"transformation with ID = " + konga::toString(m_TransformationID));
			return E_FAIL;
		}
	}
	catch( const DbException& e )
	{
		AppendErrorMessage(e.GetErrorString().c_str());
		return E_FAIL;
	}	

	return S_OK;
}

unsigned long TreeMapper::PrepareHostedWebServiceInput()
{
	assert(m_TransformationWebServiceInfo);
	PROFILING doPrf(this,"PrepareHostedWebServiceInput");
	if ( !m_TransformationWebServiceInfo ) {
		throw CrException("No web service info is set. Is the Web Service Url field empty?");
	}
	                                                                                         
	                                                                                   
	                                               
	                                      

	                                                                              
	                                                                              
	                                                                             
	                                                                             
	                                                                               
	                        
	bool isHostedWebServiceOp = (m_pTransformationEngine->getOperationType() == OPERATION_HOSTED_WEBSERVICE);

	WebServiceInfo wsInfo(m_TransformationWebServiceInfo->Url,
						  m_TransformationWebServiceInfo->SoapAction,
						  m_TransformationWebServiceInfo->TargetNamespace,
						  m_TransformationWebServiceInfo->OtherNamespaces,
						  m_TransformationWebServiceInfo->SoapStyle,
						  m_TransformationWebServiceInfo->SoapBodyUse,
						  -1, 0, 0, 0, 0,                   
						  m_TransformationWebServiceInfo->HttpUserName,
						  m_TransformationWebServiceInfo->HttpPassword);

	WebServiceIO wsIO((const char*)m_inputDataFileName,
					  (const char*)m_outputDataFileName,
					  "",
					  "");

	const long nextTranId = (m_NextTransformation_ID > 0) ? m_NextTransformation_ID : m_TransformationID;

	WebServiceStructure wsStructure(this,
						m_TransformationWebServiceInfo->RequestXsd,
						konga::GetSourceJtrFileName(m_TransformationID),
						m_TransformationWebServiceInfo->ResponseXsd,
						konga::GetTargetJtrFileName(nextTranId));
	WebServiceRestrictions wsRestrictions(m_TransformationWebServiceInfo->DelayMS,
										  m_TransformationWebServiceInfo->MaxSizeBytes);

	                                                         
	WebServiceHandler wsHandler(this,
								wsInfo, 
								wsIO, 
								wsStructure, 
								wsRestrictions,
								m_TransformationWebServiceInfo->HttpPostOptions,
								*getErrorLog(),
								*getViewLog());
	wsHandler.SetCreateSoapFaultOnError(true);

	unsigned long hr = wsHandler.ScaleDownSoapEnvelope(WebServiceHandler::Request, isHostedWebServiceOp, (const char*)m_inputDataFileName);
	if( FAILED(hr) )
	{
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_PROCESS_HOSTED_WS_INPUT_FAILED, wsHandler.GetLastError().c_str());
		m_lastErr=getErrorLog()->GetLastErr();
		m_pSoapFault = wsHandler.GetSoapFault(true);
		return hr;
	}

	return S_OK;
}

unsigned long TreeMapper::ProcessHostedWebServiceOutput()
{
	assert(m_TransformationWebServiceInfo);
	PROFILING doPrf(this,"ProcessHostedWebServiceOutput");

	                                                                                    
	                                                                   

	if ( !m_TransformationWebServiceInfo) {
		throw CrException("No web service info is set. Is the Web Service Url field empty?");
	}

	                                      
	WebServiceInfo wsInfo(m_TransformationWebServiceInfo->Url,
						  m_TransformationWebServiceInfo->SoapAction,
						  m_TransformationWebServiceInfo->TargetNamespace,
						  m_TransformationWebServiceInfo->OtherNamespaces,
						  m_TransformationWebServiceInfo->SoapStyle,
						  m_TransformationWebServiceInfo->SoapBodyUse,
						  -1, 0, 0, 0, 0,                   
						  m_TransformationWebServiceInfo->HttpUserName,
						  m_TransformationWebServiceInfo->HttpPassword);

	WebServiceIO wsIO((const char*)m_inputDataFileName,
					  (const char*)m_outputDataFileName,
					  "",
					  "");

	const long prevTranId = (m_PreviousTransformation_ID >= 0) ? m_PreviousTransformation_ID : m_TransformationID;

	WebServiceStructure wsStructure(this,
							m_TransformationWebServiceInfo->RequestXsd,
							GetSourceJtrFileName(prevTranId),
							m_TransformationWebServiceInfo->ResponseXsd,
							GetTargetJtrFileName(m_TransformationID));

	WebServiceRestrictions wsRestrictions(m_TransformationWebServiceInfo->DelayMS,
										  m_TransformationWebServiceInfo->MaxSizeBytes);

	                                                              
	WebServiceHandler wsHandler(this,
								wsInfo, 
								wsIO, 
								wsStructure, 
								wsRestrictions,
								m_TransformationWebServiceInfo->HttpPostOptions,
								*getErrorLog(),
								*getViewLog());
	wsHandler.SetCreateSoapFaultOnError(true);
	unsigned long hr = wsHandler.CreateSoapEnvelopeForHostedResponse((const char*)m_outputDataFileName);
	if( FAILED(hr) )
	{
		hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_PROCESS_HOSTED_WS_OUTPUT_FAILED, wsHandler.GetLastError().c_str());
		m_lastErr=getErrorLog()->GetLastErr();
		m_pSoapFault = wsHandler.GetSoapFault(true);
		return hr;
	}
	return S_OK;
}

string SetSpecificSoapFault(const konga::SoapFault& soapFault)
{
	                                        
	string stErr;
	DOMDocument2Ptr pEnvelope = SoapEnvelope::CreateSoapEnvelopeWithSoapFault(
		SoapConstants::Version_11, soapFault, stErr);
	return pEnvelope->getXml();
}

string SetSpecificSoapFault(FaultCodeValue_11 faultCode, const string& stFaultString)
{
	                        
	SoapFault_11 soapFault(faultCode, stFaultString);
	return SetSpecificSoapFault(soapFault);
}

string SetGenericSoapFault(FaultCodeValue_11 faultCode, long lErrorCode)
{
	string stFaultString = "The server failed to process the webservice call [Code:";
	stFaultString += konga::toString(lErrorCode);
	stFaultString += "]";
	return SetSpecificSoapFault(faultCode, stFaultString);
}


unsigned long TreeMapper::GetWebServiceInfo(DbConnection* pTranDbConn, int iFunctionID)
{
	try
	{
		if ( m_TransformationWebServiceInfo ) {
			delete m_TransformationWebServiceInfo;
		}

		m_TransformationWebServiceInfo = new konga::TransformationWebServiceInfo();
		konga::GetWebServiceInfo(iFunctionID, *m_TransformationWebServiceInfo, *getOperationDataElementHolder(), *pTranDbConn);

		return S_OK;
	}
	catch( const DbException& e )
	{
		AppendErrorMessage(e.GetPrettyErrorMessage().c_str());
		return E_FAIL;
	}
	catch( const CrException& e ) {
		AppendErrorMessage(e.GetErrorString());
		return E_FAIL;
	}
}
