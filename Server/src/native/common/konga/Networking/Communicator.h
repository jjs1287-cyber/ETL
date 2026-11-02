/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_COMMUNICATOR_H
#define KONGA_COMMUNICATOR_H

#include <string>

#include <konga/Networking/CommandListenerUsingSockets.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Result.h>
#include <konga/Utils/Settings.h>	                                                
#include <boost/thread/thread.hpp>
#include <boost/thread/condition.hpp>

namespace konga
{
	class Communicator {
	public:
		Communicator(const std::string& name, int defaultPort, int log_type);
		virtual ~Communicator();
		CommandListenerUsingSockets*getCommandListener(){return &m_commandListener;}
		void WaitUntilExit();
		void ShouldExit();

	private:
		struct CommandListenerFunction {
			CommandListenerFunction(Communicator& communicator, long lListeningPort, int log_type) : 
				m_log_type(log_type),
				m_lListeningPort(lListeningPort),
				m_Communicator(communicator)
				{}
			void operator()();
			int m_log_type;
			long m_lListeningPort;
			Communicator& m_Communicator;
		};

		boost::thread *m_pCommandListenerThread;
		CommandListenerUsingSockets m_commandListener;

		boost::condition m_exitCond;
		boost::mutex m_exitMutex;
		bool m_exitRequested = false;
	};
}

#endif                        

