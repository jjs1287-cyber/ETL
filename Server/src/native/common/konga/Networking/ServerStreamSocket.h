/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef SERVER_STREAM_SOCKET_H
#define SERVER_STREAM_SOCKET_H

#include "CrStreamSocket.h"
#include <atomic>
#include <mutex>

namespace konga {
namespace networking {

class ServerStreamSocket : public CrStreamSocket {
public:
	ServerStreamSocket(long lListenPort = -1);
	~ServerStreamSocket();

	                                                                                   
	                                               
	CrStreamSocket* Accept(); 
	void Close();
	void SetListeningPort(long lListenPort) { m_lListenPort = lListenPort; }

protected:
	void PrepareToAccept();

private:
	std::atomic<bool> m_bReadyToAccept;
	bool m_closing = false;
	std::mutex m_listenerMutex;
	long m_lListenPort;
	RawSocket* m_pListenSocket;
};

}                        
}                   

#endif                          
