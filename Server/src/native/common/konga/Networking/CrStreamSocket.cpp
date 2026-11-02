/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
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
	#include <sys/select.h>
	#include <sys/time.h>
#endif

#include <cassert>

#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/PortDefs.h>		          
#include <konga/Utils/StringUtils.h>	       

#include "CrStreamSocket.h"
#include "AutoCleanupNetworking.h"
#include "RawSocket.h"

using namespace konga;
using namespace konga::networking;

const long iReadSleep = 20;                                                        

namespace {
	bool g_bWSAStarted = false;
}

CrStreamSocket::CrStreamSocket() : m_bConnected(false), m_lPort(0), m_lLastError(0), m_iReadTimeOutMillis(300), m_pSock(0)
{
	init();
}

CrStreamSocket::CrStreamSocket(RawSocket* ps) : m_bConnected(true), 
	m_lPort(0), m_lLastError(0), m_iReadTimeOutMillis(300), m_pSock(ps)
{
	assert(ps && ps->GetSocket() != -1 && "s must be a connected socket");
	init();
}

CrStreamSocket::~CrStreamSocket() {
	if ( IsConnected() ) {
		Close();
	}
	if( m_pSock ) delete m_pSock;
}

void CrStreamSocket::init() {
	if ( !IsAPIInitialized() )
		InitSocketAPI();
	if( !m_pSock )
		m_pSock = new RawSocket();
}

std::string CrStreamSocket::GetLastError() { 
	return m_stLastError.empty() ? m_stLastError=ErrorResolver().Resolve(m_lLastError) : m_stLastError;
}

void CrStreamSocket::HandleError(long lErr, const std::string& sErr) {
	m_lLastError = lErr;
	m_stLastError = sErr;
	m_stLastError += ErrorResolver().Resolve(m_lLastError);
	throw CrSocketException(m_stLastError, m_lLastError);
}

long CrStreamSocket::InitSocketAPI()
{
#ifdef WIN32
                         
	WSADATA wsaData;
	WORD wVersion = MAKEWORD(2,0);
	if ( ::WSAStartup(wVersion, &wsaData) )
	{
		g_bWSAStarted = false;
		long lErr = ::GetLastError();
		return ( lErr != 0 ) ? lErr : E_FAIL;
	}
	else
	{
		g_bWSAStarted = true;
		return 0;
	}
#else
	g_bWSAStarted = true;
	return 0;
#endif         
}

long CrStreamSocket::DisableSocketAPI()
{
#ifdef WIN32
	if ( WSACleanup() )
	{
		long lErr = ::WSAGetLastError();
		return ( lErr != 0 ) ? lErr : E_FAIL;
	}
	else
	{
		g_bWSAStarted = false;
		return 0;
	}
#else
	g_bWSAStarted = false;
	return 0;
#endif         
}

bool CrStreamSocket::IsAPIInitialized() { return g_bWSAStarted; }

void CrStreamSocket::Connect(const std::string& stHostName, unsigned long lPort)
{	
	if( stHostName.empty() )
		throw CrSocketException(m_stLastError = "No host name specified.", -1);
	if( lPort <= 0 )
		throw CrSocketException(m_stLastError = "Port must not be zero or negative.", -1);
  
                           
                                                              
                    
  
                       
                                                      
                                                            
                                                       
  
                           
                 
                                          

                             
                               
                                                                            
                                           
                                                        
                                                                     
                                                  
  
                                     
                                                     
                                                         
                                                       
  
  

	                                                             
	struct addrinfo hints;
	memset(&hints, 0, sizeof(hints));
	hints.ai_family = PF_UNSPEC;                
	hints.ai_socktype = SOCK_STREAM;

	struct addrinfo* pAddrInfo = 0;
	char buf[32];
	int retval = getaddrinfo(stHostName.c_str(), itoa(lPort, buf, 10), &hints, &pAddrInfo);                                                                                
	if ( retval != 0 ) {
		m_lLastError = retval;
		m_stLastError = "Failed to verify the host name.\n";
		if ( retval == EAI_SYSTEM) {
			m_stLastError += ErrorResolver().Resolve(::GetLastError());
		}
		else {
			m_stLastError = gai_strerror(retval);
		}
		throw CrSocketException(m_stLastError, m_lLastError);
	}
	                                                 
	auto_free_addrinfo adopt_pAddrInfo(pAddrInfo);

	                                     
	m_pSock->SetSocket(-1);
	m_stLastError.clear();
	m_lLastError = 0;
	for ( struct addrinfo* res = pAddrInfo ; res != 0 ;  res = res->ai_next) {
		int sock = static_cast<int>(socket(res->ai_family, res->ai_socktype, res->ai_protocol));
		if ( sock < 0 ) 
		{
			m_lLastError = ::GetLastError();
			m_stLastError += "Socket failed: ";
			m_stLastError += ErrorResolver().Resolve(m_lLastError);
			continue;
		}
		if ( connect(sock, res->ai_addr, res->ai_addrlen) < 0 ) {
			m_lLastError = ::GetLastError();
			m_stLastError += "Connect failed: ";
			m_stLastError += ErrorResolver().Resolve(m_lLastError);
			continue;
		}
		m_pSock->SetSocket(sock);
		m_lPort = lPort;
		m_stLastError.clear();
		m_lLastError = 0;

		char host[100];
		getnameinfo(res->ai_addr, res->ai_addrlen,
				    host, sizeof(host),
				    0, 0, 
				    NI_NUMERICHOST|NI_NUMERICSERV);
		m_stIpAddress = host;
		break;
	}
	if ( m_pSock->GetSocket() == -1 ) {
		throw CrSocketException(m_stLastError, m_lLastError);
	}

	m_bConnected = true;
	m_lLastError = 0;
	m_stLastError.clear();
}

void CrStreamSocket::Close()
{
	if( m_pSock->GetSocket() == -1 )
		return;
		const int iSEND_REMAING_DATA = 1;
		shutdown(m_pSock->GetSocket(), iSEND_REMAING_DATA);
#ifdef WIN32
	if ( closesocket(m_pSock->GetSocket()) != 0 ) {
#else
	if ( close(m_pSock->GetSocket()) != 0 ) {
#endif
		m_lLastError = ::GetLastError();
		m_stLastError = "Failed to close the socket.\n";
		m_stLastError += ErrorResolver().Resolve(m_lLastError);
	}
	m_pSock->SetSocket(-1);
	m_bConnected = false;
}

bool CrStreamSocket::IsReceivedDataAvailable(bool bPoll          , bool bBlock           )
{
	                                                                                             

	fd_set fdSet;
	FD_ZERO(&fdSet);
	FD_SET(m_pSock->GetSocket(), &fdSet);
	struct timeval tv = {0, 0};                         
	struct timeval* pTV = &tv;
	if ( bPoll ) {
                     
	}
	else if ( bBlock )
		pTV = 0;
	else
		tv.tv_sec = m_iReadTimeOutMillis;

	int retval = select(m_pSock->GetSocket() + 1, &fdSet, 0, 0, pTV);
	if ( retval == -1 ) {
		m_lLastError = ::GetLastError();
		m_stLastError = "Failed to check for received data.\n";
		m_stLastError += ErrorResolver().Resolve(m_lLastError);
		throw CrSocketException(m_stLastError, m_lLastError);
	}
	return ( retval == 1 );
}

long CrStreamSocket::RECV(char* buf, const unsigned long len, const int flags) {
	return recv(m_pSock->GetSocket(), buf, len, flags);
}

long CrStreamSocket::SEND(const char* buf, const unsigned long len, const int flags) {
	return send(m_pSock->GetSocket(), buf, len, flags);
}

void CrStreamSocket::ReadString(std::string& stData)
{
	Read<std::string>(stData);
}

void CrStreamSocket::WriteString(const std::string& stData)
{
	Write<char>(stData.c_str(), stData.length());
}

