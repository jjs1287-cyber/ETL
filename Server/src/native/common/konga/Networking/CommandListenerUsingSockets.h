/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef COMMANDLISTENERUSINGSOCKETS_H
#define COMMANDLISTENERUSINGSOCKETS_H

#include <map>
#include <memory>
#include <string>
#include <atomic>

#include <konga/Networking/CrStreamSocket.h>
#include <konga/Networking/ServerStreamSocket.h>
#include <konga/Utils/CommandListener.h>

namespace konga {

class CommandListenerUsingSockets : public CommandListener {
public:
	CommandListenerUsingSockets(const std::string& sModuleName, long lListenPort = -1) : CommandListener(sModuleName),
		m_bShouldExit(false), m_lListenPort(lListenPort), m_sock(lListenPort) {}
	~CommandListenerUsingSockets() {}

	virtual void AddCommandFunction(const std::string& sCommand, const CommandFunctionT f);
	virtual void StartListening();
	virtual void StopListening();

	void SetListeningPort(long lListenPort) { m_lListenPort = lListenPort; }

protected:
	void HandleCommand(std::auto_ptr<konga::networking::CrStreamSocket>& pSock);

private:
	std::atomic<bool> m_bShouldExit;
	long m_lListenPort;

	typedef std::map<std::string, CommandFunctionT > CommandToFunctionMapT;
	CommandToFunctionMapT m_mFunctions;

	networking::ServerStreamSocket m_sock;
};

}                   

#endif                                 

