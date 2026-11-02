#ifndef KONGA_SERVER_H
#define KONGA_SERVER_H

#include <string>

#include <konga/Networking/CommandListenerUsingSockets.h>

extern const unsigned long lCOMMAND_PORT;
extern std::string sSERVER_MODULE;

namespace konga {
namespace CrNetworking {

class Command;
class Result;

Result* gStop(const Command& cmd);
Result* gGetStatus(const Command& cmd);

struct CommandListenerFunctor {
	CommandListenerFunctor(long lListeningPort, CommandListenerUsingSockets& commandListener) :
		m_commandListener(commandListener), m_lListeningPort(lListeningPort) {}

	void operator()() {
		m_commandListener.SetListeningPort(m_lListeningPort);
		m_commandListener.AddCommandFunction("Stop", gStop);
		m_commandListener.AddCommandFunction("GetStatus", gGetStatus);
		m_commandListener.StartListening();
	}

	CommandListenerUsingSockets& m_commandListener;
	long m_lListeningPort;
};

}                   
}                          

#endif                  
