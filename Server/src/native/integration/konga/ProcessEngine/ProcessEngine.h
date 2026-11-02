/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef PROCESS_ENGINE_H
#define PROCESS_ENGINE_H

#include <boost/thread/condition.hpp>

#include <konga/Log/logging.h>			                        
#include <konga/Networking/CommandListenerUsingSockets.h>

#include "Engine/EngineCore.h"			                   
                                                           

extern EngineCore *g_engine;
                           

class ProcessEngine {
public:
	ProcessEngine();
	virtual ~ProcessEngine();

	void Start();
	void Stop();
	bool IsRunning();

	void WaitUntilExit();
	void ShouldExit();

private:
	struct CommandListenerFunction {
		CommandListenerFunction(ProcessEngine& engine0, long lListeningPort) : 
			m_processEngine(engine0), m_lListeningPort(lListeningPort) {}
		void operator()();

		ProcessEngine& m_processEngine;
		long m_lListeningPort;
	};

private:
	boost::condition m_exitCond;
	boost::mutex m_exitMutex;
	bool m_exitRequested = false;

	boost::thread *m_pCommandListenerThread;
	CommandListenerUsingSockets m_commandListener;
};


#endif                    

