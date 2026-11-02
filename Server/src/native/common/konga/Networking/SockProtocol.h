/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef SOCK_PROTOCOL_H
#define SOCK_PROTOCOL_H

#include <string>
#include <boost/noncopyable.hpp>

#include "CrStreamSocket.h"

namespace konga {

	struct protocol_exception
	{
		protocol_exception(std::string reason = "", int errnum = 0, std::string response = "", std::string cmd = "") :
			m_errnum(errnum), m_reason(reason), m_cmd(cmd), m_response(response) {}
		int m_errnum;
		std::string m_reason;
		std::string m_cmd;
		std::string m_response;
	};
	
	class SockProtocol : boost::noncopyable
	{
	public:
		SockProtocol(int port);
		virtual ~SockProtocol();
	
		virtual void Server(std::string server) { m_server = server; }
	
		virtual std::string LastError() const { return m_last_error; }
		virtual int LastErrorCode() const { return m_last_errcode; }
		virtual std::string LastCommand() const { return m_last_cmd; }
		virtual std::string LastResponse() const { return m_last_response; }
	
		virtual std::string LocalHost() const { return m_localhost; }
		virtual std::string Server() const { return m_server; }
		virtual int Port() const { return m_port;}
	
	protected:
		virtual bool SendCommand(const std::string& cmd, std::string& response);
		virtual bool GetResponse(std::string& response);
		virtual void ReadString(std::string& res) { m_sock.ReadString(res); }
	
		virtual bool IsErrorResponse(const std::string& res) = 0;                  
		virtual bool IsEndOfResponse(const std::string& res) = 0;                                      
                 
     
	
		virtual bool ResetConnection();
		virtual void Close() { m_sock.Close(); }
	
		virtual bool DoAction( void(SockProtocol::*mf)() );
	
		networking::CrStreamSocket& GetSocket() { return m_sock; }
	
	private:
		int m_port;
		std::string m_server, m_localhost;
	
		int m_last_errcode;
		std::string m_last_response, m_last_error, m_last_cmd;
	
		networking::CrStreamSocket m_sock;
	
	};
}                   

#endif                   

