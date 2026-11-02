/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifdef DS_USE_COM

#include "StdAfx.h"
#include <string>

#include "util.h"			                             
#include "AutoCleanup.h"	                    
#include "ErrorResolver.h"	                      

#include "IncomingMsg.h"	                    
#include "EngineCore.h"		                   
#include "engine_util.h"	                                          
#include "safearray.H"		                             

extern int g_log_type;
extern EngineCore *g_engine;
extern std::string g_machine_name;

struct RemoteComCallInfo
{
	RemoteComCallInfo(ICrComCallerPtr& pComCaller_, long lToken_, const std::string& stFunc_, const char* args_,
		_variant_t& vTimeOut_, SafeArray<BSTR,VT_BSTR>& saResultArr_, _com_error& e_) :
			pComCaller(pComCaller_), lToken(lToken_), stFunc(stFunc_), args(args_),
			vTimeOut(vTimeOut_), saResultArr(saResultArr_), e(e_) {}

	ICrComCallerPtr& pComCaller;
	long lToken;
	const std::string& stFunc;
	const char* args;
	_variant_t& vTimeOut;
	SafeArray<BSTR,VT_BSTR>& saResultArr;
	_com_error& e;
};

void DoRemoteCOMInvoke(void* p)
{
	RemoteComCallInfo* pci = (RemoteComCallInfo*)p;
	pci->e = _com_error(::CoInitializeEx(0,COINIT_MULTITHREADED));
	try
	{
		                                                                                                        
		BSTR bsTemp = 0;
		long lRet = ::AnsiToBstr(pci->args, bsTemp);
		pci->saResultArr = pci->pComCaller->Call(pci->lToken, pci->stFunc.c_str(), bsTemp, pci->vTimeOut);
		::SysFreeString(bsTemp);
	}
	catch(_com_error& ce)
	{
		pci->e=ce;
	}
	::CoUninitialize();
}
  
                                                                       
 
                           
              
                     
                                  
          

                                
  
  

              
 
  
  
                                             
                                                    
                                                                                       
                                                  
                                     
                              
                                
                                
  
KString InvokeComRemote(IncomingMsg* pmsg, const KString& progId, KString args)
{
	if( !progId.GetLength() )
	{
		std::string stErr = "[Msg 375] [" + pmsg->GetRoute() + "] Invoke COM: ProgId missing", pmsg->GetRoute().c_str());
		throw InvokeErr(log_str,CO_E_CLASSSTRING);
	}

	char *progIdStr = strnewdup(progId);
	AutoArrayDelete<char> adapt_progIdStr(progIdStr);

	USES_CONVERSION;
	char *CLSID_Str=0;
	CLSID_Str = get_token(progIdStr,CLSID_Str,";");
	AutoArrayDelete<char> adapt_CLSID_Str(CLSID_Str);

	LPOLESTR clsid_str = A2OLE(CLSID_Str);	                                    

	  
	                                                   
	  
	if( !g_engine->IsObjectAccessAllowed(CLSID_Str) )
	{
		std::string stErr = "[Msg 356] [" + pmsg->GetRoute() + "] Invoke COM: Object call to %s is not allowed because "
			"of current security policy.", pmsg->GetRoute().c_str(), CLSID_Str );
		throw InvokeErr(log_str,ERROR_ACCESS_DENIED);
	}

	  
	                                                                                  
	                                                                                  
	  
	char* sTimeOut = strchr(progIdStr,';');
	int iTimeOut = INFINITE;
	if( sTimeOut )
	{
		*sTimeOut=0;                      
		++sTimeOut;	                
		if( sTimeOut )
			iTimeOut = atoi(sTimeOut)*1000;
		if( iTimeOut <= 0 ) iTimeOut = INFINITE;
	}
	std::string stFunc = progIdStr;

	ICrComCallerPtr pComCaller;
	try
	{
		pComCaller = pmsg->GetExternalComCaller();
	}
	catch(_com_error& e)
	{
		std::string stErr = "[Msg 377] [" + pmsg->GetRoute() + "] Invoke COM: Failed to connect to remote COM "
			"execution object when trying to invoke %s. Reason: %s \n\n%s",
			pmsg->GetRoute().c_str(), CLSID_Str, 
			e.Description().length() ? (LPCTSTR) e.Description() : " ",
			e.Error() == E_FAIL ? " " : ErrorResolver().Resolve(e.Error()) );
		throw InvokeErr(log_str,e.Error());
	}

	long lToken;
	if( (lToken = pmsg->GetCOMObjectToken(CLSID_Str)) == 0 )
	{
		try
		{
			_variant_t vEnableCancel(true);
			lToken = pComCaller->CreateInstance(CLSID_Str, vEnableCancel);
		}
		catch(_com_error& e)
		{
			_bstr_t bsErr;
			try {
				bsErr = pComCaller->LastError;
			} catch(...) {}

			std::string stErr = "[Msg 397] [" + pmsg->GetRoute() + "] Invoke COM: Failed to create instance of "
				"%s. Reason: %s - %s", pmsg->GetRoute().c_str(), CLSID_Str,
				bsErr.length()?(LPCTSTR)bsErr:"", ErrorResolver().Resolve(e.Error()) );
			throw InvokeErr(log_str,e.Error());
		}
	}

	pmsg->SetCOMObjectToken(CLSID_Str, lToken);

	std::string stArgs = ReplaceDataElementWithValueAddEscapes(pmsg,(LPCTSTR)args,"\"\\");

	SafeArray<BSTR,VT_BSTR> saResultArr;
	_variant_t vTimeOut(static_cast<long>(iTimeOut),VT_I4);
	_com_error ceErr(0);
	RemoteComCallInfo callInfo(pComCaller, lToken, stFunc, stArgs.c_str(), vTimeOut, saResultArr, ceErr);

	unsigned long hThread = ::_beginthread(DoRemoteCOMInvoke,0,(void*)&callInfo);
	if( hThread <= 0 )
	{
		                                      
		long err = ::GetLastError();
		std::string stErr = "[Msg 342] [" + pmsg->GetRoute() + "] InvokeCOM: Failed to create the invocation thread for %s. %s.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId, ErrorResolver().Resolve(err));
		                                                                     
		throw InvokeErr(log_str,E_FAIL);
	}

	HANDLE hCancelCompletedEvent;
	try {
	switch( pmsg->WaitForThreadAndEvent((HANDLE)hThread, iTimeOut, hCancelCompletedEvent) )
	{
	case WAIT_TIMEOUT: 
		{
		long err = ::GetLastError();
		std::string stErr = "[Msg 343] [" + pmsg->GetRoute() + "] InvokeCOM: Timeout (%d seconds) expired for %s. Call terminated.",
			pmsg->GetRoute().c_str(), iTimeOut/1000, (iTimeOut/1000)==1?"":"s", (LPCTSTR)progId);
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		                                                                     
		throw InvokeErr(log_str,E_FAIL);
		}
	case WAIT_FAILED:
		{
		long err = ::GetLastError();
		if( err == E_HANDLE ) throw 0;                                             
		std::string stErr = "[Msg 344] [" + pmsg->GetRoute() + "] InvokeCOM: Failed to wait for the invocation thread for %s. %s Call terminated.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId, ErrorResolver().Resolve(err));
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		                                                                     
		throw InvokeErr(log_str,E_FAIL);
		}
	case WAIT_OBJECT_0:                                                                        
		{
		std::string stErr = "[Msg 359] [" + pmsg->GetRoute() + "] InvokeCOM: Task was cancelled while calling %s. Call terminated.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId);
		ReportError(g_log_type, stErr);
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		  
		                                                     
		  
		if( strnicmp(CLSID_Str,"OperationEngine.Resolve",25) == 0 )
		{
			try
			{		
				pComCaller->Call(lToken, "Cancel", "", vTimeOut);
			}
			catch(_com_error&)
			{
			}
		}
		::SetEvent( hCancelCompletedEvent );                                                      
		::Sleep(5000);                            
		                                                                     
		throw InvokeErr(log_str,E_FAIL);
		break;
		}
	case WAIT_OBJECT_0+1:                                                     
		break;
	default:
		break;
	}
	} catch(InvokeErr&){ throw; } 

	if( ceErr.Error() != S_OK )
	{
		std::string stErr;
		try
		{
			stErr = (LPCTSTR)pComCaller->LastError;
		}
		catch(_com_error& e)
		{
			stErr = "Failed to retrieve error from COM caller. Reason: ";
			stErr += e.Description().length()>0 ? (LPCTSTR)e.Description() : ErrorResolver().Resolve(e.Error());
		}
		catch(...)
		{
			stErr = "Failed to retrieve error from COM caller.";
		}
		std::string stErr = "[Msg 376] [" + pmsg->GetRoute() + "] Invoke COM: Object call to %s failed. %s \n%s",
			pmsg->GetRoute().c_str(), CLSID_Str, 
			ceErr.Error() == E_FAIL ? " " : ErrorResolver().Resolve(ceErr.Error()),
			stErr.empty()?"":stErr.c_str() );
		                                                                     
		throw InvokeErr(log_str,ceErr.Error());
	}

	std::string stResult="", stTmp;
	int nElements = saResultArr.Elements();
	for(int i=0; i<nElements; ++i)
	{
		                                            
		char* pc = 0;
		long lRet = ::BstrToAnsi(saResultArr[i], pc);
		AutoArrayDelete<char> adopt_pc(pc);
		stTmp = pc;
		stResult += stTmp;
		stResult += ';';
		long pos = stTmp.find('=');
		pmsg->SetDataElementValue(stTmp.substr(0,pos).c_str(), stTmp.substr(pos+1).c_str() );
	}

	return stResult.c_str();
}

#endif              

