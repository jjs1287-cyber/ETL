/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_FILETRANSFER_HTTP_VERB_ENUM_H
#define ETLFORGE_FILETRANSFER_HTTP_VERB_ENUM_H

#include <string>

namespace konga {

	enum HttpVerb {
		HttpGet = 0,
		HttpHead = 1,
		HttpPost = 2,
		HttpPut = 3,
		HttpDelete = 4,
		HttpTrace = 5,
		HttpConnect = 6
	};

	namespace {
		std::string toString(const HttpVerb v ) {
			switch(v) {
				case HttpGet:
					return "GET";
				case HttpHead:
					return "HEAD";
				case HttpPost:
					return "POST";
				case HttpPut:
					return "PUT";
				case HttpDelete:
					return "DELETE";
				case HttpTrace:
					return "TRACE";
				case HttpConnect:
					return "CONNECT";
				default:
					return "UNKNOWN";
			}
		}
	}

}
#endif                                          

