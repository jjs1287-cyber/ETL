/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if defined(DS_USE_CORBA)

#include <fstream>			                

WINBASEAPI BOOL WINAPI TryEnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection);

#include "corba_util.h"

#if defined(TAO)
                   
                                                
    
#endif

#if defined(DS_USE_CORBA)
	CORBA::ORB_ptr			g_orb;
	PortableServer::POA_ptr	g_poa;
#endif                         

extern int g_log_type;																						

void CORBA_Init()
{
	if( g_orb ) return;                       

                                                
                
	char* argv[] = {"engine",0};
	int argc = 1;

	g_orb = CORBA::ORB_init(argc, argv);

	CORBA::Object_var poa_obj = g_orb->resolve_initial_references("RootPOA");

	g_poa = PortableServer::POA::_narrow( poa_obj );
                                                                              
                          
                  
                                                                            
                                                                                     
  
	PortableServer::POAManager_var mgr = g_poa->the_POAManager();
	mgr->activate();
}

void CORBA_CleanUp()
{
	if( g_poa && g_orb )
	{
		try {
			PortableServer::POAManager_var mgr = g_poa->the_POAManager();
			mgr->deactivate(false,false);
			                                                                         
			CORBA::release(g_poa);
			g_orb->destroy();
			CORBA::release(g_orb);
		} catch(CORBA::SystemException& e) {
			std::string stErr;
			stErr += e._to_string();
			stErr += "--";
		} catch(...) {}
	}
	g_orb = 0;
}

std::string CORBA_TypeKindToString(CORBA_TCKind tk)
{
	std::string type;
	switch(tk)
	{
		case CORBA_tk_null: type = "null"; break;
		case CORBA_tk_void: type = "void"; break;
		case CORBA_tk_short: type = "short"; break;
		case CORBA_tk_long: type = "long"; break;
		case CORBA_tk_ushort: type = "unsigned short"; break;
		case CORBA_tk_ulong: type = "unsigned long"; break;
		case CORBA_tk_float: type = "float"; break;
		case CORBA_tk_double: type = "double"; break;
		case CORBA_tk_boolean: type = "boolean"; break;
		case CORBA_tk_char: type = "char"; break;
		case CORBA_tk_octet: type = "octet"; break;
		case CORBA_tk_any: type = "any"; break;
		case CORBA_tk_TypeCode: type = "TypeCode"; break;
		case CORBA_tk_Principal: type = "Principal"; break;
		case CORBA_tk_string: type = "string"; break;
		#if !defined(ORBACUS3)
			case CORBA_tk_longlong: type = "long long"; break;
			case CORBA_tk_ulonglong: type = "unsigned long long"; break;
			case CORBA_tk_longdouble: type = "long double"; break;
			case CORBA_tk_wchar: type = "wchar"; break;
			case CORBA_tk_wstring: type = "wstring"; break;
		#endif                      
		default: type = "unknown type";
	}
	return type;
}


#endif                         

