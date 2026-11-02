/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef MOD_KONGA_WS_H
#define MOD_KONGA_WS_H

                             
#include <httpd.h>
#include <http_protocol.h>
#include <http_log.h>

#include <http_config.h>
#include <http_main.h>
#include <http_core.h>

                                                                           
#ifdef REG_EXTENDED
#undef REG_EXTENDED
#undef REG_ICASE
#undef REG_NEWLINE
#undef REG_NOTBOL
#undef REG_NOTEOL
#undef REG_NOSUB
#endif

#ifdef WIN32
	                                                                                         
	                                                                                           
	                                                                                       
	                                                                                           
	                                                                                         
	                                             
	#ifdef MOD_KONGA_WS_EXPORTS
		#define MOD_KONGA_WS_API __declspec(dllexport)
	#else
		#define MOD_KONGA_WS_API __declspec(dllimport)
	#endif
#else       
	#ifdef MOD_KONGA_WS_EXPORTS
		#define MOD_KONGA_WS_API
	#else
		#define MOD_KONGA_WS_API
	#endif
#endif

namespace konga {

	class DbConnection;

	int HandleRequest(request_rec* r);

	                                                                              
	bool HandleHttpTrigger(request_rec* r, int& res, DbConnection& dbConn);

	                                                                
	                                
	bool HandleHostedWebService(request_rec* r, int& res);

	                                                             
	bool HandleHttpEndpoint(request_rec* r, int& res, DbConnection& dbConn, long HttpEndpointId);
}

                                             
                                             

#endif                  

