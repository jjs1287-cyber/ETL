/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <cerrno>
#include <iostream>

#ifdef WIN32
	#include <winsock2.h>
#else
	#include <netdb.h>
#endif

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>
#include "SockProtocol.h"

using namespace konga;

SockProtocol::SockProtocol(int port) : m_port(port), m_last_errcode(0)
{
	char host_name[1024];
	if( gethostname(host_name,1024) < 0 ) {
		unsigned long err = GetLastError();
		std::cerr << "Internet Protocol error. Failed to get local host name. Reason: " << 
			ErrorResolver().Resolve(err);
	}
	m_localhost = host_name;
}

SockProtocol::~SockProtocol()
{
	Close();
}

bool SockProtocol::ResetConnection()
{
	m_last_cmd="";
	m_last_error="";
	m_last_response="";

	if( m_sock.IsConnected() )
		Close();

	try {
		m_sock.Connect(m_server, m_port);
	}
	catch( const networking::CrSocketException& ex ) {
		std::string s; 
		if( !m_server.empty() )
		{
			s = "Failed to connect to " + m_server + ':' +toString(m_port) +
				". Socket error: " + ex.GetErrorString();
		}		   
		else
			s = "Server name must be set, before a connection can be established.";
		throw protocol_exception(s, m_last_errcode);
	}
	
	return true;
}

bool SockProtocol::GetResponse(std::string& response)
{
	response = "";
	std::string res;
	do
	{
		m_sock.ReadString(res);
		if( res.length() > 4 )
			response += res.substr(res.length()-4, 4);
		else
			response += res;
	} while( !IsEndOfResponse(res) );

	if( IsErrorResponse(res) )
		throw protocol_exception("Server error.", 0, response);

	return true;
}

bool SockProtocol::SendCommand(const std::string& cmd, std::string& response)
{
	m_last_cmd = cmd;
	m_sock.WriteString(cmd);
	try { GetResponse(response); }
	catch(protocol_exception& e)
	{
		m_last_response = e.m_response;
		m_last_errcode = e.m_errnum;
		m_last_error = e.m_reason;
		e.m_cmd = cmd;
		throw;
	}
	return true;
}

bool SockProtocol::DoAction( void(SockProtocol::*mf)() )
{
	try
	{
		(this->*mf)();
	}
	catch(const protocol_exception& e)
	{
		m_last_response = e.m_response;
		m_last_errcode = e.m_errnum;
		m_last_error = e.m_reason;
		Close();
		return false;
	}
#ifndef _DEBUG
	catch(...)
	{
		Close();
		m_last_error = "Internet Protocol ERROR, Unknown excpetion";
		return false;
	}
#endif           
	return true;
}

