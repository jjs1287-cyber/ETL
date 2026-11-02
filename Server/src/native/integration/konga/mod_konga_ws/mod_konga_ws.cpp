/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <iostream>	       

#ifdef WIN32
	#include <windows.h>
#endif

                             
#include <httpd.h>
#include <http_config.h>
#include <http_protocol.h>
#include <http_request.h>

                                                                           
#ifdef REG_EXTENDED
#undef REG_EXTENDED
#undef REG_ICASE
#undef REG_NEWLINE
#undef REG_NOTBOL
#undef REG_NOTEOL
#undef REG_NOSUB
#endif

#ifdef WIN32
#include <konga/Log/logging.h>
#endif
#include <konga/Utils/CrException.h>
#include <konga/WinUtils/WinErrorUtils.h>	                             

#include "mod_konga_ws.h"
using namespace konga;

#ifdef WIN32

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
	case DLL_THREAD_ATTACH: {
		KongaExceptionHandler handler( Logger::GetLogFileName("Hosted Web Service") );
		break;
	}
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
    return TRUE;
}
#endif         

namespace {
	int check_user_access(request_rec *r)
	{
		if( !r->user )
			return DECLINED;
		return OK;
	}

	int authenticate_basic_user(request_rec *r)
	{
		int res;
		const char *sent_pw;
		if ((res = ap_get_basic_auth_pw(r, &sent_pw))) {
			return res;
		}

		if( !r->user )
			return DECLINED;

		return OK;
	}
}

extern "C"
{ 

static void module_init(apr_pool_t* p, server_rec* svr_rec) {
}

static int konga_handler(request_rec* r)
{
	try {
		return konga::HandleRequest(r);
	} catch( konga::CrException& e) {
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "An error occurred: %s", e.GetErrorString().c_str());
		return HTTP_INTERNAL_SERVER_ERROR;
	}
#ifndef _DEBUG
	catch(...) {
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "%s", "An unexpected error occurred.");
		return HTTP_INTERNAL_SERVER_ERROR;
	}
#endif
}

static void mod_konga_register_hooks(apr_pool_t* p)
{
    ap_hook_check_user_id(authenticate_basic_user,NULL,NULL,APR_HOOK_MIDDLE);
    ap_hook_auth_checker(check_user_access,NULL,NULL,APR_HOOK_MIDDLE);
    ap_hook_handler(konga_handler, NULL, NULL, APR_HOOK_MIDDLE);
}

module AP_MODULE_DECLARE_DATA konga_ws_module = {
    STANDARD20_MODULE_STUFF,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    mod_konga_register_hooks,
};

}              
