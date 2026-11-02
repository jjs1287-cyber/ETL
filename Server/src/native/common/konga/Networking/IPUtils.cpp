/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <WinSock2.h>
#else
	#include <sys/socket.h>
	#include <netinet/in.h>
	#include <arpa/inet.h>
	#include <netdb.h>
	extern int h_errno;

	#include <konga/Utils/PortDefs.h>	                                          
#endif

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>

#include "IPUtils.h"

using namespace konga;
using std::string;

namespace {
	void init_networking() {
	#ifdef WIN32
		WSADATA wsaData;
		const int res = WSAStartup(MAKEWORD(2, 2), &wsaData);
		if(res)
			throw CrException(res, "Failed to initialize Winsock. Reason: " + ErrorResolver().Resolve(res));
	#endif
	}
}

string konga::GetHostByIp(const string& ip_addr) {

	init_networking();

	struct in_addr addr;
	addr.s_addr = inet_addr(ip_addr.c_str());
	if(addr.s_addr == INADDR_NONE) {
		long err = GetLastError();
		throw CrException("'" + ip_addr + "' is not a valid ip address. Reason: " + ErrorResolver().Resolve(err));
	}

	struct hostent *remoteHost = gethostbyaddr((char *) &addr, 4, AF_INET);
	if( !remoteHost ) {
#ifdef WIN32
		const int err = WSAGetLastError();
#else
		const int err = h_errno;
#endif
		throw CrException("Failed to resolve '" + ip_addr + "' to a host name. Reason: " + ErrorResolver().Resolve(err));
	}
	return string (remoteHost->h_name ? remoteHost->h_name : ip_addr);
}

