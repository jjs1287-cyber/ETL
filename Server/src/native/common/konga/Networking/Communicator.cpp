/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <konga/Log/logging.h>
#include "Communicator.h"

using namespace konga;
using namespace networking;

void Communicator::CommandListenerFunction::operator()() {
	try {
		m_Communicator.m_commandListener.SetListeningPort(m_lListeningPort);
		ReportError( report_file|report_trace, "About to listen for commands on port "
			+ toString(m_lListeningPort) + ".", EVENTLOG_INFORMATION_TYPE);
		m_Communicator.m_commandListener.StartListening();
	} catch(CrException& e) {
		ReportError( m_log_type, "Failed to listen for commands on port "
			+ toString(m_lListeningPort) + ". Reason: " + e.GetErrorString());
	}
}


Communicator::Communicator(const std::string& name,int defaultPort, int log_type) :
m_pCommandListenerThread(0), m_commandListener(name)
{
	std::string path=name;
	long lCommandPort=konga::GetOptionAsInt( path.c_str(), "CommandPort", defaultPort);
	m_pCommandListenerThread = new boost::thread(CommandListenerFunction(*this, lCommandPort, log_type));
}

Communicator::~Communicator()
{
	m_commandListener.StopListening();
	if(m_pCommandListenerThread && m_pCommandListenerThread->joinable()) m_pCommandListenerThread->join();
	delete m_pCommandListenerThread;
}

void Communicator::WaitUntilExit() {
	boost::mutex::scoped_lock lock(m_exitMutex);
	while(!m_exitRequested) m_exitCond.wait(lock);
}

void Communicator::ShouldExit() {
	boost::mutex::scoped_lock lock(m_exitMutex);
	m_exitRequested = true;
	m_exitCond.notify_all();
}

                                                                                              
                                                             
                                                                            
                               
   
