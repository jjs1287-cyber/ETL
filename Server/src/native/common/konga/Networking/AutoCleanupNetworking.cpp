/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <winsock2.h>
	#include <Ws2tcpip.h>
	#include <Wspiapi.h>	                 
#else
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <netdb.h>
#endif

#include "AutoCleanupNetworking.h"

using namespace konga;
using namespace konga::networking;

auto_free_addrinfo::~auto_free_addrinfo(void) {
	if ( m_p != 0 )
		freeaddrinfo(m_p);
}
