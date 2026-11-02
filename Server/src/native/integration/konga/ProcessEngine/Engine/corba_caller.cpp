/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if defined(DS_USE_CORBA)

#include <iostream>
#include <strstream>
#include <fstream>                 
#include <string>
#include <string.h>

#include "corba_util.h"

#include "FileRetriever.h"	                      
#include "IncomingMsg.h"	                    
#include "engine_util.h"	                                          
#include "util.h"			                             
#include "AutoCleanup.h"	                         

typedef std::map<std::string,int> RetDEmap;

extern int g_log_type;																						
 
std::string CORBA_get_IOR(const char* url)
{
	if( strnicmp(url,"IOR:",4) == 0 ||
		strncmp(url,"iioploc",7) == 0 ||
		strncmp(url,"corbaloc",8) == 0 )
	{									
										                                                       
										                                                       
		return url;						                            
	}

	std::string ret;
	FileRetriever fr;
	long res = fr.GetAsString(url,ret);
	if( res == Protocol::FAILURE )
	{
		std::string s = "[Msg 301] Failed to retrieve: '";
		s += url; s += "'. The error was: " + fr.LastError();
	}
	return ret;
}

  
                                              
  
void CORBA_BuildArgs(IncomingMsg* pmsg, CORBA_NVList_ptr& arguments, const char* args, int& numberOfArgs, RetDEmap& returned_DEs, std::string& return_value_de)
{
	numberOfArgs = 0;
	char *s = strnewdup(args);
	char *s_copy = s;
	char *de_name=0;
	char *de_value=0;
	char c;

	for(char*ts=s;ts[0]!=0;++ts)
	{
		if(*ts==';') 
			++numberOfArgs;
	}

	int iNumArgs=0;
	for(int i=0; i<numberOfArgs && *s; ++i, ++iNumArgs)                                        
	{
		CORBA_Any any;
		if( strnicmp(s,"Int",3) == 0 || strnicmp(s,"Lng",3) == 0 ) {                                      

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= static_cast<CORBA_Long>(atoi(sVal.c_str()));
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= static_cast<CORBA_Long>(atoi(sVal.c_str()));
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= static_cast<CORBA_Long>(atoi(sVal.c_str()));

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                
			delete[] de_name; de_name=0;

		} else if( strnicmp(s,"srt",3) == 0 ) {                                      

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= static_cast<CORBA::Short>(atoi(sVal.c_str()));
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= static_cast<CORBA::Short>(atoi(sVal.c_str()));
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= static_cast<CORBA::Short>(atoi(sVal.c_str()));

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                
			delete[] de_name; de_name=0;

		} else if( strnicmp(s,"Dbl",3) == 0 ) {                                         

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= static_cast<CORBA::Double>(atof(sVal.c_str()));
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= static_cast<CORBA::Double>(atof(sVal.c_str()));
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= static_cast<CORBA::Double>(atof(sVal.c_str()));

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                   
			delete[] de_name; de_name=0;

		} else if( strnicmp(s,"Flt",3) == 0 || strnicmp(s,"sng",3) == 0 ) {                                         

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= static_cast<CORBA::Float>(atof(sVal.c_str()));
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= static_cast<CORBA::Float>(atof(sVal.c_str()));
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= static_cast<CORBA::Float>(atof(sVal.c_str()));

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                   
			delete[] de_name; de_name=0;

		} else if( strnicmp(s,"Str",3) == 0 ) {                                            

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			std::string sVal = s;
			GetDataElementValue(pmsg, s, sVal);

                                                                                
                                                                                                
                                                                                                      
                                                                                                        

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= sVal.c_str();
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= sVal.c_str();
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= sVal.c_str();

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                      
			delete[] de_name; de_name=0;

		}
		else if( strnicmp(s,"Bol",3) == 0 ) {                                      

			s += 4;                
			get_token(s,de_name,",");                              

			if( (c = *s) == 'O' ) ++s; else	s += 2;                            

			TaskDataElement tde;
			if( *s == '[' )
			{
				char* sEnd = strchr(s,']');
				if( sEnd ) *sEnd = 0;
				++s;
				tde = pmsg->GetDataElement(s);
			}
			else
			{
				char* pc = strchr(s,';');
				if( pc ) *pc = 0;
				tde = TaskDataElement(s,"no_name",t_bool);
			}

			if( c=='i' ) *(arguments->add(CORBA::ARG_IN)->value()) <<= CORBA_Any::from_boolean(tde.is_true());
			else if( c=='O' ) *(arguments->add(CORBA::ARG_OUT)->value()) <<= CORBA_Any::from_boolean(tde.is_true());
			else if( c=='B' ) *(arguments->add(CORBA::ARG_INOUT)->value()) <<= CORBA_Any::from_boolean(tde.is_true());

			if( c!='i' ) returned_DEs[de_name] = i;
			while(*s!=';')++s;++s;                
			delete[] de_name; de_name=0;
		} else {
			std::string stErr = "[Msg 312] InvokeCORBA: Unknown type in '%s'", s);
			char serr[10001];
			strncpy(serr,(LPCTSTR)log_str,10000);
			serr[10000]=0;
			throw serr;
		}		
	}
	numberOfArgs=iNumArgs;

	delete[] s_copy;
}

std::string CORBA_ParseAny(CORBA::Any* arg)
{
	std::string resultStr = "";
	char str[1024];

	if( arg->type()->equal(CORBA::_tc_long) )
	{
		CORBA::Long l;
		*arg >>= l;
		sprintf(str,"" + toString(xyx) + "",l);
		resultStr += str;
	}
	else if( arg->type()->equal(CORBA::_tc_short) )
	{
		CORBA::Short srt;
		*arg >>= srt;
		sprintf(str,"" + toString(xyx) + "",srt);
		resultStr += str;
	}
	else if( arg->type()->equal(CORBA::_tc_double) )
	{
		CORBA::Double d;
		*arg >>= d;
		sprintf(str,"%.14g",d);
		resultStr += str;
	}
	else if( arg->type()->equal(CORBA::_tc_float) )
	{
		CORBA::Float f;
		*arg >>= f;
		sprintf(str,"%.14g",f);
		resultStr += str;
	}
	else if( arg->type()->equal(CORBA::_tc_string) )
	{
		char* anys_str;
		if( !(*arg >>= anys_str) )
			TRACE("EERORORORORO");
		resultStr += anys_str;
	}
	else if( arg->type()->equal(CORBA::_tc_boolean) )
	{
		CORBA::Boolean b; 
		*arg >>= CORBA::Any::to_boolean(b);
		sprintf(str,"" + toString(xyx) + "",b);
		resultStr += str;
	}
	else if( arg->type()->equal(CORBA::_tc_null) )
	{
		resultStr += "0";
	}
	else
	{
		try {
			std::string stErr = "[Msg 313] InvokeCORBA: Unknown type '%s' in return value. Ignored.",
				arg->type()->name()?arg->type()->name():"<null>");
			ReportError(g_log_type,log_str,EVENTLOG_WARNING_TYPE);
		}catch(CORBA::TypeCode::BadKind){
			std::string stErr = "[Msg 313] InvokeCORBA: Unknown type <null> in return value. Ignored.");
			ReportError(g_log_type,log_str,EVENTLOG_WARNING_TYPE);
		}
	}

	return resultStr;
}

void DoCORBAInvoke(void* p)
{
	CORBA::Request_ptr request = (CORBA::Request_ptr)p;
	request->invoke();
}

  
                                                  
                                                   
                                                                                              
                                                  
  
KString InvokeCORBA(IncomingMsg* pmsg, const char* locationStr, KString args)
{
	char *location = strnewdup(locationStr);
	AutoArrayDelete<char> adapt_location_copy(location);

	char* ior_location = get_token(location,";");
	char* method_name = get_token(location,";");
	AutoArrayDelete<char> adapt_ior_location(ior_location);
	AutoArrayDelete<char> adapt_method_name(method_name);

	  
	                                                                                    
	                                                                                  
	  
	char* sTimeOut = strchr(locationStr,';');
	int iTimeOut = INFINITE;
	if( sTimeOut )
	{
		*sTimeOut=0;                      
		++sTimeOut;	                
		if( sTimeOut )
			iTimeOut = atoi(sTimeOut)*1000;
		if( iTimeOut <= 0 ) iTimeOut = INFINITE;
	}

	CORBA_Object_var obj;

	try {
		std::string ior = CORBA_get_IOR(ior_location);
		if( ior.length() == 0 )
		{
			std::string stErr = "[Msg 208] [" + pmsg->GetRoute() + "] InvokeCORBA: Failed to open %s", pmsg->GetRoute().c_str(), ior_location);
			char serr[10001];
			strncpy(serr,(LPCTSTR)log_str,10000);
			serr[10000]=0;
			throw serr;
		}

		obj = g_orb->string_to_object(ior.c_str());

		if( CORBA_is_nil(obj) )
		{
			std::string stErr = "[Msg 209] [" + pmsg->GetRoute() + "] InvokeCORBA: Failed to bind to %s (string_to_object returned nil object)", 
				pmsg->GetRoute().c_str(), ior_location);
			char serr[10001];
			strncpy(serr,(LPCTSTR)log_str,10000);
			serr[10000]=0;
			throw serr;
		}
	}
	catch (CORBA::SystemException& sysEx)
	{
		std::string stErr = "[Msg 210] [" + pmsg->GetRoute() + "] InvokeCORBA: Unexpected CORBA system exception when binding to %s. ",
			pmsg->GetRoute().c_str(), locationStr);
		std::string stErr = log_str;
		stErr += sysEx._to_string();
		char serr[10001];
		strncpy(serr,stErr.c_str(),10000);
		serr[10000]=0;
		throw serr;
	}
	catch(const char*) {throw;}
	catch(...)
	{
		std::string stErr = "[Msg 211] [" + pmsg->GetRoute() + "] InvokeCORBA: Unknown exception when binding to %s",
			pmsg->GetRoute().c_str(), locationStr);
		char serr[10001];
		strncpy(serr,(LPCTSTR)log_str,10000);
		serr[10000]=0;
		throw serr;
	}
	
	                 
	int numberOfArgs=0;
	RetDEmap returned_DEs;                                                
	std::string return_value_de;

	CORBA::NVList_var arguments;
	g_orb->create_list(0,arguments);

    CORBA::NamedValue_var resultVal;
	g_orb->create_named_value(resultVal); 

	char *s=strnewdup(args);
	AutoArrayDelete<char> adopt_s(s);
	int len = strlen(s);
	CORBA_TypeCode_ptr return_type = CORBA__tc_void;
	if( strnicmp(s,"Ret/",4) == 0 )                          
	{
		s += 4, len -= 4;		              
		int k = findk(s,',');
		s[k] = 0;
		const char*strDataType = s;

		if( (len-k) > 0 ) s += k+1;
		k = findk(s,',');
		s[k] = 0;
		if( k ) return_value_de = s;

		if( strnicmp(strDataType,"Int",3) == 0 ) resultVal->value()->replace(CORBA::_tc_long, &resultVal);
		else if( strnicmp(strDataType,"srt",3) == 0 ) resultVal->value()->replace(CORBA::_tc_short, &resultVal);
		else if( strnicmp(strDataType,"Lng",3) == 0 ) resultVal->value()->replace(CORBA::_tc_long, &resultVal);
		else if( strnicmp(strDataType,"Dbl",3) == 0 ) resultVal->value()->replace(CORBA::_tc_double, &resultVal);
		else if( strnicmp(strDataType,"Flt",3) == 0 || strnicmp(strDataType,"sng",3) == 0 ) resultVal->value()->replace(CORBA::_tc_float, &resultVal);
		else if( strnicmp(strDataType,"Str",3) == 0 ) resultVal->value()->replace(CORBA::_tc_string, 0, true);
		else if( strnicmp(strDataType,"Bol",3) == 0 ) resultVal->value()->replace(CORBA::_tc_boolean, &resultVal);
		else {
			std::string stErr = "[Msg 314] [" + pmsg->GetRoute() + "] InvokeCORBA: Unknown type '%s' in return value. Ignored.",
				pmsg->GetRoute().c_str(), strDataType);
			ReportError(g_log_type,log_str,EVENTLOG_WARNING_TYPE);
		}		

		if( (len-k) > 0 ) s += k+1;
		k = findk(s,';');
		if( (len-k) > 0 ) s += k+1;
	}

	try
	{
		CORBA_BuildArgs(pmsg, arguments, s, numberOfArgs, returned_DEs, return_value_de);
	}
	catch (CORBA_SystemException& sysEx)
	{
		std::string stErr = "[Msg 212] [" + pmsg->GetRoute() + "] InvokeCORBA: Unexpected CORBA system exception when building arguments for %s. ",
			pmsg->GetRoute().c_str(), locationStr);
		std::string stErr = log_str;
		stErr += sysEx._to_string();
		char serr[10001];
		strncpy(serr,stErr.c_str(),10000);
		serr[10000]=0;
		throw serr;
	}
	catch(const char*)
	{
		throw;
	}
	catch(...)
	{
		std::string stErr = "[Msg 213] [" + pmsg->GetRoute() + "] InvokeCORBA: unknown exception when building arguments for %s",
			pmsg->GetRoute().c_str(), locationStr);
		char serr[10001];
		strncpy(serr,(LPCTSTR)log_str,10000);
		serr[10000]=0;
		throw serr;
	}

	std::string resultStr = "";
	CORBA::Request_var request;
	obj->_create_request(CORBA::Context::_nil(),method_name,arguments,resultVal,request.out(),0);

	  
	                                                              
	  
        
                                                              
                                
                                                                                                                       
                                                                                                                                                        
                       
  
                                     
  
                                                                                                                           
                                          
                              
                              
                   
                                    
                
             
  
  
	try
	{
		unsigned long hThread = ::_beginthread(DoCORBAInvoke,0,(void*)request.ptr());
		if( hThread <= 0 )
		{
			                                      
			long err = ::GetLastError();
			std::string stErr = "[Msg 345] [" + pmsg->GetRoute() + "] Invoke CORBA: Failed to create the invocation thread for %s. %s.",
				pmsg->GetRoute().c_str(), locationStr, ErrorResolver().Resolve(err));
			throw InvokeErr(log_str,E_FAIL);
		}

		switch( ::WaitForSingleObject((HANDLE)hThread,iTimeOut) )
		{
		case WAIT_TIMEOUT: 
			{
			long err = ::GetLastError();
			std::string stErr = "[Msg 346] [" + pmsg->GetRoute() + "] Invoke CORBA: Timeout (" + toString(xyx) + " second%s) expired for %s. Call terminated.",
				pmsg->GetRoute().c_str(), iTimeOut, iTimeOut==1?"":"s", locationStr);
			try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
			throw InvokeErr(log_str,E_FAIL);
			}
		case WAIT_FAILED:
			{
			long err = ::GetLastError();
			std::string stErr = "[Msg 347] [" + pmsg->GetRoute() + "] Invoke CORBA: Failed to wait for the invocation thread for %s. %s. Call terminated.",
				pmsg->GetRoute().c_str(), locationStr, ErrorResolver().Resolve(err));
			try { ::TerminateThread(reinterpret_cast<HANDLE>(hThread), -1); } catch(...){}
			throw InvokeErr(log_str,E_FAIL);
			}
		case WAIT_OBJECT_0:                                                     
			;
		}
		CORBA::Environment_ptr env = request->env();
		strstream str;
		if( env->exception() )
		{
        
    
                                                                                                        
                                                                                                            
                                                                                                                                    
                                                                                  
             
    
                                        
    
                                                                                                                                        
                                            
                                
                                
                     
                                      
                  
                 
    
  
			str << "The exception was: " << *env->exception() << endl << '\0';
			std::string stErr = "[Msg 214] [" + pmsg->GetRoute() + "] InvokeCORBA: An exception was raised when invoking the %s "
				"method of %s with arguments %s. \n%s",
				pmsg->GetRoute().c_str(), method_name, locationStr,
				args.GetLength()?(LPCTSTR)args:"<none>",str.str());
			char serr[10001];
			strncpy(serr,(LPCTSTR)log_str,10000);
			serr[10000]=0;
			throw serr;
		}
	}
	catch (CORBA_SystemException& sysEx)
	{
		strstream str;
		str << "The exception was: " << sysEx << endl << '\0';
		std::string stErr = "[Msg 215] [" + pmsg->GetRoute() + "] InvokeCORBA: Unexpected CORBA system exception when invoking the %s "
				"method of %s with arguments %s. \n%s",
				pmsg->GetRoute().c_str(), method_name, locationStr,
				args.GetLength()?(LPCTSTR)args:"<none>",str.str());
		std::string stErr = log_str;
		stErr += sysEx._to_string();
		char serr[10001];
		strncpy(serr,stErr.c_str(),10000);
		serr[10000]=0;
		throw serr;
	}
	catch (CORBA_Exception& Ex)
	{
		strstream str;
		str << "The exception was: " << Ex << endl << '\0';
		std::string stErr = "[Msg 216] [" + pmsg->GetRoute() + "] InvokeCORBA: Unexpected CORBA exception when invoking %s. %s. ",
			pmsg->GetRoute().c_str(), locationStr, str.str());
		std::string stErr = log_str;
		stErr += Ex._to_string();
		char serr[10001];
		strncpy(serr,stErr.c_str(),10000);
		serr[10000]=0;
		throw serr;
	}
	catch(const char*) {throw;}
	catch(...)
	{
		std::string stErr = "[Msg 217] [" + pmsg->GetRoute() + "] InvokeCORBA: Unknown exception when invoking %s",
			pmsg->GetRoute().c_str(), locationStr);
		char serr[10001];
		strncpy(serr,(LPCTSTR)log_str,10000);
		serr[10000]=0;
		throw serr;
	}

	std::string stValue;
	{for(RetDEmap::const_iterator it= returned_DEs.begin(); it!=returned_DEs.end(); ++it)
	{
		stValue = CORBA_ParseAny( arguments->item(it->second)->value() );
		resultStr += it->first;
		resultStr += '=';
		resultStr += stValue;
		resultStr += ';';
		pmsg->SetDataElementValue(it->first.c_str(), stValue.c_str());
	}}

	if( !return_value_de.empty() )
	{
		stValue = CORBA_ParseAny( resultVal->value() );
		resultStr += return_value_de;
		resultStr += '=';
		resultStr += stValue;
		resultStr += ';';
		pmsg->SetDataElementValue(return_value_de.c_str(), stValue.c_str());
	}

	return resultStr.c_str();
}

  

                        
                        
                         
                        
                          
                         
                         
                          
                           
                        
                         
                       
                            
                             
                          
                          
                            
                             
                              
                         
                           

  

#endif                         

