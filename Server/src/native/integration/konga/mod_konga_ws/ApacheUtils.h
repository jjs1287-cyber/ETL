/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_APACHE_UTILS_H
#define KONGA_APACHE_UTILS_H

#include <string>
#include <map>

                             
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

#include <konga/FileTransfer/HttpUtils.h>

namespace konga {

	class FailedToHandleRequest {
	public:
		FailedToHandleRequest(const std::string& msg, int res) : m_res(res), m_msg(msg) {
		}
		~FailedToHandleRequest(void) {}

		int getResult() const {
			return m_res;
		}

		std::string getMessage() const {
			return m_msg;
		}

	private:
		int m_res;
		std::string m_msg;
	};

	                                                                                                   
	                                             
	                                                                                                            
	                                  
	int FillRequest(HttpRequest& request, request_rec* r);

	struct HttpRequestUser {
		bool IsAnonymous;
		std::string UserName;
		std::string Password;
	};

	                                                                                               
	int GetHttpRequestUser(request_rec* r, HttpRequestUser& user);

	typedef std::map<std::string, std::string> HeadersT;

	void AddOutputHeaders(const HeadersT& vHeaders, request_rec* r);
}

#endif                        

