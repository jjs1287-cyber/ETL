/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#ifdef DS_USE_COM

#include <string>
#include <strstream>

#include <string.h>

#include "IncomingMsg.h"	                    
#include "EngineCore.h"		                   
#include "engine_util.h"	                                          
#include "util.h"			                             
#include "AutoCleanup.h"	                    
#include "ErrorResolver.h"	                      

using namespace std;

extern int g_log_type;
extern EngineCore *g_engine;
extern std::string g_machine_name;

enum ComCleanup { AssignAndDelete, JustDelete };
struct ComCleanupDetail
{ 
	ComCleanupDetail(ComCleanup cc,int w) : how(cc), which(w) {} 
	ComCleanupDetail(){}
	ComCleanup how; int which;
};
typedef map<string, ComCleanupDetail> RetDEmap;

void SetVariantTypeAndValue(IncomingMsg* pmsg, VARIANT& var, std::string& sVal, const char* de)
{
	TaskDataElement tde = pmsg->GetDataElement(de);
	switch( tde.GetType() )
	{
	case t_bool:
		var.pvarVal->vt = VT_BOOL;
		var.pvarVal->boolVal = atoi(sVal.c_str());
		break;
	case t_int:
	case t_long:
		var.pvarVal->vt = VT_I4;
		var.pvarVal->iVal = atoi(sVal.c_str());
		break;
	case t_double:
		var.pvarVal->vt = VT_R8;
		var.pvarVal->dblVal = atof(sVal.c_str());
		break;
	case t_float:
	case t_single:
		var.pvarVal->vt = VT_R4;
		var.pvarVal->fltVal = static_cast<float>(atof(sVal.c_str()));
		break;
	case t_string:
	case t_variant:
	default:
		                   
		var.pvarVal->vt = VT_BSTR;
		                                                                
		::AnsiToBstr(sVal.c_str(), var.pvarVal->bstrVal);
	}
}

  
                                                                    
                                              
  
VARIANTARG* BuildVariantArgs(IncomingMsg* pmsg, const char* args, int& numberOfArgs, RetDEmap& returned_DEs, string& return_value_de)
{
	numberOfArgs = 0;
	if( !args || !*args ) return 0;
	char *s = strnewdup(args);
	char *s_copy = s;
	AutoArrayDelete<char> adopt_s_copy(s_copy);
	char *de_name=0;
	char *de_value=0;
	char c;

	USES_CONVERSION;

	int len = strlen(s);
	if( strnicmp(s,"Ret/",4) == 0 )                          
	{
		s += 4, len -= 4;		              
		int k = findk(s,';');
		s[k] = 0;
		return_value_de = s;
		if( (len-k) > 0 ) s += k+1;
	}

	for(char*ts=s;ts[0]!=0;++ts)
	{
		if(*ts==';') 
			++numberOfArgs;
	}

	int iActualNumArgs=0;
	char itoaBuf[64];
	VARIANTARG* pv = new VARIANTARG[ numberOfArgs ];
	for(int i=numberOfArgs-1; i>=0 && *s; --i, ++iActualNumArgs)
	{
		VariantInit(&pv[i]);				                                          
		if( strnicmp(s,"Int",3) == 0 || strnicmp(s,"srt",3) == 0 ) {
			pv[i].vt = VT_I2;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                           

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if(c=='i') pv[i].iVal = atoi(sVal.c_str());
			else {
				pv[i].vt |= VT_BYREF;
				pv[i].piVal = new short(atoi(sVal.c_str()));
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			if( de_name ) { delete[] de_name; de_name=0; }
			while(*s!=';')++s;++s;                 
		} else if( strnicmp(s,"Lng",3) == 0 ) {
			pv[i].vt = VT_I4;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                           

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if(c=='i') pv[i].lVal = atoi(sVal.c_str());
			else {
				pv[i].vt |= VT_BYREF;
				pv[i].plVal = new long(atoi(sVal.c_str()));
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			if( de_name ) { delete[] de_name; de_name=0; }
			while(*s!=';')++s;++s;                 
		} else if( strnicmp(s,"Dbl",3) == 0 ) {
			pv[i].vt = VT_R8;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if(c=='i') pv[i].dblVal = atof(sVal.c_str());
			else {
				pv[i].vt |= VT_BYREF;
				pv[i].pdblVal = new double(atof(sVal.c_str()));
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			if( de_name ) { delete[] de_name; de_name=0; }
			while(*s!=';')++s;++s;                    
		} else if( strnicmp(s,"Flt",3) == 0 || strnicmp(s,"sng",3) == 0 ) {
			pv[i].vt = VT_R4;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if( c=='i' ) { pv[i].fltVal = static_cast<float>(atof(sVal.c_str())); }
			else {
				pv[i].vt |= VT_BYREF;
				pv[i].pfltVal = new float(static_cast<float>(atof(sVal.c_str())));
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			if( de_name ) { delete[] de_name; de_name=0; }
			while(*s!=';')++s;++s;                    
		} else if( strnicmp(s,"Str",3) == 0 ) {
			pv[i].vt = VT_BSTR;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			char*ts=s; while(*ts!=';')++ts; *ts=0;
			if(c=='i')
			{
				                                                         
				long lRet = ::AnsiToBstr(sVal.c_str(), pv[i].bstrVal);
			}
			else {
				pv[i].vt |= VT_BYREF;
				                                                                          
				BSTR bsTemp = 0;
				long lRet = ::AnsiToBstr(sVal.c_str(), bsTemp);
				pv[i].pbstrVal = new LPOLESTR( bsTemp );
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			s=ts+1;                      
			if( de_name ) { delete[] de_name; de_name=0; }
		} else if( strnicmp(s,"Bol",3) == 0 ) {
			pv[i].vt = VT_BOOL;
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if(c=='i') { pv[i].boolVal = strnicmp(sVal.c_str(),"true",4)==0 || 
				('0'<*sVal.c_str()&&*sVal.c_str()<='9')?TRUE:FALSE; 
			} else {
				pv[i].vt |= VT_BYREF;
				pv[i].pboolVal = strnicmp(sVal.c_str(),"true",4)==0 || 
					('0'<*sVal.c_str()&&*sVal.c_str()<='9')?new short(TRUE):new short(FALSE);
				if( de_name && c != 'i' && c != 'I' ) returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			}
			if( de_name ) { delete[] de_name; de_name=0; }
			while(*s!=';')++s;++s;                 
		} else if( strnicmp(s,"Var",3) == 0 ) {
			pv[i].vt = VT_VARIANT | VT_BYREF;                                            
			s += 4;                
			if( *s && *s != ',' ) get_token(s,de_name,",");                              
			else { de_name=0; ++s; }            
			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			if( *s == '"' || *s == '[')
				GetDataElementValue(pmsg, s, sVal);
			else
			{
				char* sEnd = strchr(s,';');
				if( sEnd && *s != ';' )
				{
					*sEnd = 0;
					sVal = s;
					s = sEnd + 1;
				}
				else
					sVal = "";
			}

			if( *sVal.c_str() && *sVal.c_str() != ';' )
			{
				pv[i].pvarVal = new VARIANT;
				::VariantInit(pv[i].pvarVal);
				SetVariantTypeAndValue(pmsg, pv[i], sVal, de_name?de_name:"");
			}
			else
			{
				pv[i].vt = VT_EMPTY;
			}
			if( de_name && c != 'i' && c != 'I' )
			{
				returned_DEs[de_name] = ComCleanupDetail(AssignAndDelete,i);
				delete[] de_name; de_name=0;
			}
			else returned_DEs[de_name?de_name:itoa(i,itoaBuf,10)] = ComCleanupDetail(JustDelete,i);
			while(*s!=';')++s;++s;                          
		}
		else
		{
			std::string stErr = "[Msg 318] [" + pmsg->GetRoute() + "] Invoke COM: Unknown type '%s', in '%s'",
				pmsg->GetRoute().c_str(), s, args);
			throw InvokeErr(log_str,E_FAIL);
		}		
	}

	if( numberOfArgs != iActualNumArgs )
	{
		VARIANTARG* pv2 = new VARIANTARG[ iActualNumArgs ];
		for(int i=0; i<iActualNumArgs; ++i)
			pv2[i] = pv[i + numberOfArgs - iActualNumArgs];
		numberOfArgs = iActualNumArgs;
		std::swap<VARIANTARG*>(pv,pv2);
		delete[] pv2;
	}

	return pv;
}

  
                                                                 
                                  
  
string ParseVarArgs(VARIANTARG& var, ComCleanup how=AssignAndDelete)
{
	string resultStr = "";
	char str[256];
	switch(var.vt)
	{
	case VT_I2: sprintf(str,"" + toString(xyx) + "",var.iVal); resultStr += str; break;
	case VT_I4: sprintf(str,"" + toString(xyx) + "",var.lVal); resultStr += str; break;
		  
		                                                                         
		                            
		  
	case VT_R4: {                                                          
			std::strstream ioStr;
			ioStr.precision(15);
			ioStr << static_cast<float>(var.fltVal) << '\0';
			resultStr += ioStr.str(); 
			ioStr.freeze(false);
			break;
	}
	case VT_R8: {                                                          
			std::strstream ioStr;
			ioStr.precision(15);
			ioStr << static_cast<double>(var.dblVal) << '\0';
			resultStr += ioStr.str(); 
			ioStr.freeze(false);
			break;
	}
	case VT_BOOL: resultStr += var.boolVal?'1':'0'; break;
	case VT_BSTR: {
		KString s = var.bstrVal;
		resultStr += (LPCSTR)s;
		break;
		}
	case VT_I2|VT_BYREF: 
		if( how==AssignAndDelete ) { sprintf(str,"" + toString(xyx) + "",*var.piVal); resultStr += str; } 
		delete var.piVal; break;
	case VT_I4|VT_BYREF: 
		if( how==AssignAndDelete ) { sprintf(str,"" + toString(xyx) + "",*var.plVal); resultStr += str; }
		delete var.plVal; break;
	case VT_R4|VT_BYREF: 
		if( how==AssignAndDelete ) {                                                      
			std::strstream ioStr;
			ioStr.precision(15);
			ioStr << static_cast<float>(*var.pfltVal) << '\0';
			resultStr += ioStr.str(); 
			ioStr.freeze(false);
		}
		delete var.pfltVal; break;
	case VT_R8|VT_BYREF: if( how==AssignAndDelete ) { 
                                                       
			std::strstream ioStr;
			ioStr.precision(15);
			ioStr << static_cast<double>(*var.pdblVal) << '\0';
			resultStr += ioStr.str(); 
			ioStr.freeze(false);
		}
		delete var.pdblVal; break;
	case VT_BOOL|VT_BYREF: 
		if( how==AssignAndDelete ) { resultStr += *var.pboolVal?'1':'0'; }
		delete var.pboolVal; break;
	case VT_BSTR|VT_BYREF: {
		if( how==AssignAndDelete ) { KString s = *var.pbstrVal;	resultStr += (LPCSTR)s; }
		delete var.pbstrVal;
		break;
		}
	case VT_VARIANT|VT_BYREF: 
		if( how==AssignAndDelete ) { resultStr += ParseVarArgs(*var.pvarVal); }
		delete var.pvarVal; break;
	case VT_EMPTY: break;
	default: resultStr += "<error: unknown type>";                                             
	}
	return resultStr;
}

  
                                                         
  
void com_inv_cleanup(VARIANT*Args, int numberOfArgs, VARIANT Result)
{
	for(int i=0;i<numberOfArgs;++i) VariantClear(&Args[i]);
	delete[] Args;
	VariantClear(&Result);
}

  
                                                                             
                                                              
  
struct ComCallInfo
{
	ComCallInfo(IDispatch* pDispatch_, DISPID dispid_, WORD dispatch_kind_, DISPPARAMS& MyParams_, 
		VARIANT& Result_, EXCEPINFO& ExcepInfo_, HRESULT& hr_) :
		pDispatch(pDispatch_), dispid(dispid_), dispatch_kind(dispatch_kind_), MyParams(MyParams_), 
		Result(Result_), ExcepInfo(ExcepInfo_), hr(hr_) {}		
	IDispatch* pDispatch;
	DISPID dispid;
	WORD dispatch_kind;
	DISPPARAMS& MyParams;
	VARIANT& Result;
	EXCEPINFO& ExcepInfo;
	HRESULT& hr;
};

  
                                                              
                                                   
  
void DoCOMInvoke(void* p)
{
	ComCallInfo* pci = (ComCallInfo*)p;
	pci->hr = ::CoInitializeEx(0,COINIT_MULTITHREADED);
	pci->hr = pci->pDispatch->Invoke(pci->dispid, IID_NULL, GetUserDefaultLCID(), pci->dispatch_kind, 
		&pci->MyParams, &pci->Result, &pci->ExcepInfo, 0);
	::CoUninitialize();
}

  
                                             
                                                    
                                                                                       
                                                  
                                     
                              
                                
                                
  
KString InvokeCOM(IncomingMsg* pmsg, const KString& progId, KString args)
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

	LPOLESTR name = A2OLE(progIdStr);

	HRESULT hr;	
	IDispatch* pDispatch;
	
	if( (pDispatch = pmsg->GetCOMObject(CLSID_Str)) == 0 )
	{
		_GUID CLSID_myCOM;
		if( *clsid_str == '{' )
			hr = CLSIDFromString(clsid_str, &CLSID_myCOM);
		else
			hr = CLSIDFromProgID(clsid_str, &CLSID_myCOM);

		if( FAILED(hr) )
		{
			char thelog_str[4096];
			if( hr == CO_E_CLASSSTRING )
			{
				sprintf(thelog_str, "[Msg 404] [" + pmsg->GetRoute() + "] InvokeCOM: failed to convert CLSID string %s to GUID, Reason: %s, "
								"A likely cause is that '%s' isn't registered on machine '%s'.",
					pmsg->GetRoute().c_str(), CLSID_Str, _com_error(hr).ErrorMessage(), CLSID_Str, g_machine_name.c_str() );
			}
			else
			{
				sprintf(thelog_str, "[Msg 198] [" + pmsg->GetRoute() + "] InvokeCOM: failed to convert CLSID string %s to GUID, Reason: %s",
					pmsg->GetRoute().c_str(), CLSID_Str, _com_error(hr).ErrorMessage() );
			}

			throw InvokeErr(thelog_str,CO_E_CLASSSTRING);
		}

		                                
		HRESULT hr_inproc=S_OK;
		HRESULT hr_local = CoCreateInstance((REFCLSID)CLSID_myCOM, 0, CLSCTX_LOCAL_SERVER | CLSCTX_REMOTE_SERVER, IID_IDispatch, (void**)&pDispatch);
		hr = hr_local;

		if( FAILED(hr_local) )
		{
			hr_inproc = CoCreateInstance((REFCLSID)CLSID_myCOM, 0, CLSCTX_INPROC_SERVER, IID_IDispatch, (void**)&pDispatch);
			if( hr_inproc != REGDB_E_CLASSNOTREG )
				hr = hr_inproc;
			else
				hr = hr_local;
		}

		if( FAILED(hr) )
		{
			std::string stErr = "[Msg 201] [" + pmsg->GetRoute() + "] Invoke COM: failed to create instance of %s, Reason: %s",
				pmsg->GetRoute().c_str(), CLSID_Str, _com_error(hr).ErrorMessage() );
			throw InvokeErr(log_str,hr);
		}
	}

	DISPID dispid;                                 
	hr = pDispatch->GetIDsOfNames(IID_NULL, &name, 1, GetUserDefaultLCID(), &dispid);

	if( FAILED(hr) )
	{
		std::string stErr = "[Msg 202] [" + pmsg->GetRoute() + "] InvokeCOM: failed to get function dispatch id for %s, Reason: %s",
			pmsg->GetRoute().c_str(), progId, _com_error(hr).ErrorMessage() );
		pDispatch->Release();
		throw InvokeErr(log_str,hr);
	}
	
	DISPPARAMS MyParams = {0, 0, 0, 0};
	DISPID putdispid;

	WORD dispatch_kind = DISPATCH_METHOD;
	if( strncmp((LPCSTR)args,"get;", 4) == 0 )
	{
		args = args.Right( args.GetLength() - 4 );
		dispatch_kind = DISPATCH_PROPERTYGET;
	}
	else if( strncmp((LPCSTR)args,"put;", 4) == 0 )
	{
		args = args.Right( args.GetLength() - 4 );
		dispatch_kind = DISPATCH_PROPERTYPUT;
		putdispid = DISPID_PROPERTYPUT;		
		MyParams.cNamedArgs = 1;
		MyParams.rgdispidNamedArgs = &putdispid; 
	}
	else if( strncmp((LPCSTR)args,"PUT;", 4) == 0 )
	{
		args = args.Right( args.GetLength() - 4 );
		dispatch_kind = DISPATCH_PROPERTYPUTREF; 
		putdispid = DISPID_PROPERTYPUT;		
		MyParams.cNamedArgs = 1;
		MyParams.rgdispidNamedArgs = &putdispid; 
	}

	                 
	int numberOfArgs=0;
	RetDEmap returned_DEs;                                                
	string return_value_de;
	VARIANTARG *Args = BuildVariantArgs( pmsg, args, numberOfArgs, returned_DEs, return_value_de);

	MyParams.rgvarg = Args;
	MyParams.cArgs = numberOfArgs;

	VARIANT Result;
	VariantInit(&Result);

	EXCEPINFO ExcepInfo;
	memset(&ExcepInfo,0,sizeof EXCEPINFO);

	ComCallInfo callInfo(pDispatch,dispid,dispatch_kind,MyParams,Result,ExcepInfo,hr);

	unsigned long hThread = ::_beginthread(DoCOMInvoke,0,(void*)&callInfo);
	if( hThread <= 0 )
	{
		                                      
		long err = ::GetLastError();
		std::string stErr = "[Msg 342] [" + pmsg->GetRoute() + "] InvokeCOM: Failed to create the invocation thread for %s. %s.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId, ErrorResolver().Resolve(err));
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		throw InvokeErr(log_str,hr);
	}

	HANDLE hCancelCompletedEvent;
	try {
	switch( pmsg->WaitForThreadAndEvent((HANDLE)hThread, iTimeOut, hCancelCompletedEvent) )
	{
	case WAIT_TIMEOUT: 
		{
		long err = ::GetLastError();
		std::string stErr = "[Msg 343] [" + pmsg->GetRoute() + "] InvokeCOM: Timeout (" + toString(xyx) + " seconds) expired for %s. Call terminated.",
			pmsg->GetRoute().c_str(), iTimeOut/1000, (iTimeOut/1000)==1?"":"s", (LPCTSTR)progId);
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		throw InvokeErr(log_str,hr);
		}
	case WAIT_FAILED:
		{
		long err = ::GetLastError();
		if( err == E_HANDLE ) throw 0;                                             
		std::string stErr = "[Msg 344] [" + pmsg->GetRoute() + "] InvokeCOM: Failed to wait for the invocation thread for %s. %s. Call terminated.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId, ErrorResolver().Resolve(err));
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		throw InvokeErr(log_str,hr);
		}
	case WAIT_OBJECT_0:                                                                        
		{
		std::string stErr = "[Msg 359] [" + pmsg->GetRoute() + "] InvokeCOM: Task was cancelled while calling %s. Call terminated.",
			pmsg->GetRoute().c_str(), (LPCTSTR)progId);
		com_inv_cleanup(Args, numberOfArgs, Result);
		ReportError(g_log_type, stErr);
		try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
		  
		                                                     
		  
		if( strnicmp(CLSID_Str,"OperationEngine.Resolve",25) == 0 )
		{
			_bstr_t btName("Cancel");
			BSTR bsName = btName;
			hr = pDispatch->GetIDsOfNames(IID_NULL, &bsName, 1, GetUserDefaultLCID(), &dispid);
			if( !FAILED(hr) )
			{
				memset(&MyParams,0,sizeof(MyParams));
				hr = pDispatch->Invoke(dispid, IID_NULL, GetUserDefaultLCID(), DISPATCH_METHOD,
					&MyParams, &Result, &ExcepInfo, 0);
			}
		}
		pDispatch->Release();
		::SetEvent( hCancelCompletedEvent );                                                      
		::Sleep(5000);                            
		throw InvokeErr(log_str,hr);
		break;
		}
	case WAIT_OBJECT_0+1:                                                     
		break;
	default:
		break;
	}
	} catch(InvokeErr&){ throw; } 

	hr = callInfo.hr;
	Result = callInfo.Result;
	ExcepInfo = callInfo.ExcepInfo;

	if( hr == DISP_E_TYPEMISMATCH )
	{
		std::string stErr = "[Msg 204] [" + pmsg->GetRoute() + "] InvokeCOM: Type mismatch when calling %s.", pmsg->GetRoute().c_str(), progId);
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		throw InvokeErr(log_str,hr);
	}
	else if( hr == DISP_E_EXCEPTION )                                           
	{
		KString estr = ExcepInfo.bstrDescription;
		std::string stErr = "[Msg 205] [" + pmsg->GetRoute() + "] InvokeCOM: failed to invoke %s, Reason=%s (%p), the exception was: %s", 
			pmsg->GetRoute().c_str(), progId, _com_error(hr).ErrorMessage(), hr, estr );
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		throw InvokeErr(log_str,hr);
	}
	else if( FAILED(hr) )													   
	{
		std::string stErr = "[Msg 206] [" + pmsg->GetRoute() + "] InvokeCOM: failed to invoke %s, Reason=%s (%p)", 
			pmsg->GetRoute().c_str(), progId, _com_error(hr).ErrorMessage(), hr );
		pDispatch->Release();
		com_inv_cleanup(Args, numberOfArgs, Result);
		throw InvokeErr(log_str,hr);
	}	

	pmsg->SetCOMObject(CLSID_Str, pDispatch);

	  
	                                                                        
	  
	string resultStr = "";
	std::string stValue;
	{for(RetDEmap::const_iterator it= returned_DEs.begin(); it!=returned_DEs.end(); ++it)
	{
		if( it->second.how == AssignAndDelete )
		{
			stValue = ParseVarArgs( Args[it->second.which], AssignAndDelete );
			resultStr += it->first;
			resultStr += '=';
			resultStr += stValue;
			resultStr += ';';
			pmsg->SetDataElementValue(it->first.c_str(), stValue.c_str());
		}
		else
			ParseVarArgs( Args[it->second.which], JustDelete );
	}}

	if( return_value_de.size() > 0 )
	{
		stValue = ParseVarArgs( Result, AssignAndDelete );
		resultStr += return_value_de;
		resultStr += '=';
		resultStr += stValue;
		resultStr += ';';
		pmsg->SetDataElementValue(return_value_de.c_str(), stValue.c_str());
	}

	com_inv_cleanup(Args, numberOfArgs, Result);
	return resultStr.c_str();
}

#endif         
#endif              
