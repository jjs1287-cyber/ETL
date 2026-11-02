/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <winsock2.h>
	#include <Ws2tcpip.h>
	#include <Wspiapi.h>	           
	#define EAI_SYSTEM -11
#else
	#include <sys/types.h>
	#include <sys/socket.h>
	#include <netdb.h>
	#include <netinet/in.h>
	#include <arpa/inet.h>

	#include <sys/select.h>
	#include <sys/time.h>
	#define closesocket close
	#ifndef SOCKET_ERROR
		#define SOCKET_ERROR (-1)
	#endif
	#ifndef SOCKET
		#define SOCKET int
	#endif
#endif

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>		                   

#include "ServerStreamSocket.h"
#include "RawSocket.h"

using namespace konga;
using namespace konga::networking;

ServerStreamSocket::ServerStreamSocket(long lListenPort) : 
	m_bReadyToAccept(false), m_lListenPort(lListenPort), m_pListenSocket(new RawSocket(-1))
{
}

ServerStreamSocket::~ServerStreamSocket() {
	Close();
	delete m_pListenSocket;
}

void ServerStreamSocket::Close() {
	std::lock_guard<std::mutex> lock(m_listenerMutex);
	m_closing = true;
	m_bReadyToAccept = false;
	const KongaSocketT socket = m_pListenSocket->TakeSocket();
	if(socket != static_cast<KongaSocketT>(-1)) {
		shutdown(socket, 2);
		closesocket(socket);
	}
}

void ServerStreamSocket::PrepareToAccept() {
	std::lock_guard<std::mutex> lock(m_listenerMutex);
	if(m_closing) return;
	if( m_bReadyToAccept ) return;                       

	m_pListenSocket->SetSocket( socket(AF_INET, SOCK_STREAM, IPPROTO_TCP) );
	if( m_pListenSocket->GetSocket() == -1 ) {
		SetLastErrorNumber(::GetLastError());
		SetLastErrorString("Failed to create socket. Reason: " + ErrorResolver().Resolve(GetLastErrorNumber()));
		throw CrSocketException(GetLastError(), GetLastErrorNumber());
	}

	                                                          
	                                                           
	sockaddr_in service = {0};
	service.sin_family = AF_INET;
	service.sin_addr.s_addr = inet_addr("127.0.0.1");
	service.sin_port = htons(m_lListenPort);

	if(bind( m_pListenSocket->GetSocket(), (sockaddr*) &service, sizeof(service)) == SOCKET_ERROR) {
		SetLastErrorNumber(::GetLastError());
		SetLastErrorString("Failed to bind to socket (" + toString(m_lListenPort) + 
			"). Reason: " + ErrorResolver().Resolve(GetLastErrorNumber()));
		closesocket(m_pListenSocket->GetSocket());
		m_pListenSocket->SetSocket(-1);
		throw CrSocketException(GetLastError(), GetLastErrorNumber());
	}

	                                
	if (listen( m_pListenSocket->GetSocket(), 1 ) == SOCKET_ERROR) {
		SetLastErrorNumber(::GetLastError());
		SetLastErrorString("Failed to listen to socket (" + toString(m_lListenPort) + 
			"). Reason: " + ErrorResolver().Resolve(GetLastErrorNumber()));
		closesocket(m_pListenSocket->GetSocket());
		m_pListenSocket->SetSocket(-1);
		throw CrSocketException(GetLastError(), GetLastErrorNumber());
	}

	m_bReadyToAccept = true;
}

CrStreamSocket* ServerStreamSocket::Accept() {

	PrepareToAccept();

	while(m_bReadyToAccept) {
		const SOCKET accepted = accept(m_pListenSocket->GetSocket(), 0, 0);
		if(accepted != static_cast<SOCKET>(-1)) return new CrStreamSocket(new RawSocket(accepted));
		if(!m_bReadyToAccept) return 0;
		throw CrSocketException("Failed to accept a command connection.", ::GetLastError());
	}
	return 0;
}
