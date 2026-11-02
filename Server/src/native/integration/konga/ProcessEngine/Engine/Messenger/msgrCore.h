/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef MSGR_CORE_H
#define MSGR_CORE_H

#include <boost/thread/thread.hpp>

                                                       
                                                       

#include <konga/Db/MsgDb/TransientErrorMsgTab.h>	                                  
#include <konga/Db/MsgDb/OutgoingMsgTab.h>		                            
#include <konga/Db/DbUtils/DbUtils.h>	               

#define MSG_MESSENGER_START_TIMERS (WM_USER+102)

class Messenger;

extern int g_log_type;
                                 
                           

namespace konga {
	class Timer;
}

class Messenger
{
public:
	Messenger();
	virtual ~Messenger();

	bool ConnectDataSources();
	bool DisconnectDataSources();
	void ReconnectMDB();

	void CheckIncoming();
                        
                              

	bool Start();
	bool Stop();
	bool IsRunning() { return m_is_running; }

	void SetLogType(int lt) { g_log_type = lt; }
	int GetLogType() const { return g_log_type; }

                   
                    

                    

	void StartTimers();
	void Setup();
private:

	struct MessengerTimerFunction {
		MessengerTimerFunction(Messenger* pMessenger0) : pMessenger(pMessenger0) {}
		void operator()();
		Messenger* pMessenger;
	};

	static const unsigned int INITIAL_INCOMING_TIME_INTERVAL;	           
	static const unsigned int INITIAL_OUTGOING_TIME_INTERVAL;	           

	unsigned int INITIAL_TRANSIENTERROR_TIME_INTERVAL;               

	int outgoing_time_interval, transienterror_time_interval;
	int m_incoming_timer_id, m_outgoing_timer_id, m_transienterror_timer_id;

	bool m_is_running;
	bool m_bEmailDeleteIfPermFailure;

                      
                       

	konga::DbConnection *m_mdb, *m_pdb;

	boost::thread *m_pThread;
	konga::Timer *m_pTimer;

protected:
                                                     
	void CheckIncomingDir(bool& longer_time_interval);

                                                                                                                         
                                                                                                                         
                                                                                                                                      
};

#endif               

