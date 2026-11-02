/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_UTILS_H
#define KONGA_HTTP_UTILS_H

#include <map>
#include <string>

namespace konga {

	struct HeadersAndBody {
		std::string Body;
		std::string BodyFileName;

		typedef std::map<std::string, std::string> HeaderMapT;
		HeaderMapT Headers;
	};

	struct HttpRequest : public HeadersAndBody {
		std::string IpAddress;

		typedef std::map<std::string, std::string> UrlQueryMapT;
		UrlQueryMapT Query;
	};

	struct HttpResponse : public HeadersAndBody {};

}                   

#endif                      
