/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <string>
#include <stdio.h>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/ErrorResolver.h>

#include "ThreadManager.h"
#include "engine_util.h"
#include "EngineCore.h"

extern int	 		g_log_type;		                       
extern std::string	g_status_str;	                         
extern EngineCore*	g_engine;		                          

ThreadManager::ThreadManager() {}

ThreadManager::~ThreadManager()
{
	{
		boost::mutex::scoped_lock lock( m_thread_set_lock );
		if( m_threads.size() != 0 )
		{
			                                                                                                                                                 
		}
	}

	{
		boost::mutex::scoped_lock lock( m_task_set_lock );
		if( m_threads.size() != 0 )
		{
			                                                                                                                                             
		}
	}
}

void ThreadManager::AddTask(IncomingMsg* pmsg)
{
	boost::mutex::scoped_lock lock( m_task_set_lock);
	m_tasks[ pmsg ] = true;
}

bool ThreadManager::ReleaseTask(IncomingMsg* pmsg)
{
	boost::mutex::scoped_lock lock( m_task_set_lock);
	TASK_SET_IT it = m_tasks.find(pmsg);
	if( it != m_tasks.end() )
		m_tasks.erase( it );
	else
		return false;
	
	if( m_tasks.size() == 0 )
		m_all_released.notify_one();

	return true;
}

void ThreadManager::KillTasks(const std::set<int>& msgids) {
	boost::mutex::scoped_lock lock( m_task_set_lock);

	TASK_SET_IT itTask = m_tasks.begin();
	for(; itTask != m_tasks.end(); ++itTask) {
		if( msgids.find( itTask->first->m_MsgId ) != msgids.end() )
			itTask->first->Kill();
	}

	boost::mutex::scoped_lock thread_set_lock( m_thread_set_lock );
	THREAD_SET_IT itThread = m_threads.begin();
	for(; itThread != m_threads.end(); ++itThread) {
		if( msgids.find( itThread->second->m_MsgId ) != msgids.end() )
			itThread->second->Kill();
	}
}

void ThreadManager::AddThread(boost::thread* pThread, IncomingMsg* pmsg)
{
	boost::mutex::scoped_lock lock( m_thread_set_lock );

	m_threads[ pThread ] = pmsg;
	ReleaseTask(pmsg);
}

bool ThreadManager::DeleteThread(boost::thread* pThread)
{
	boost::mutex::scoped_lock lock( m_thread_set_lock );

	THREAD_SET_IT it = m_threads.find(pThread);
	if( it != m_threads.end() )
		m_threads.erase( it );
	else
		return false;

	return true;
}

boost::thread** ThreadManager::GetHandles(int& nhandle)
{
	{
		boost::mutex::scoped_lock lock( m_task_set_lock );
		int ntasks;
		if( (ntasks=static_cast<int>(m_tasks.size())) == 0 ) {
			m_all_released.notify_one();
		} else {
			std::string stErr = "[Msg 185] Waiting for " + toString(ntasks) + " " + std::string((ntasks==1)?"task":"tasks") + " to initialize...";
			ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

			                                                               
    
                                                           
                                                                                                                                    
                                                                              
    
     
			g_status_str = stErr;
		}			
		m_all_released.wait(lock);
	}

	boost::mutex::scoped_lock lock( m_thread_set_lock );

	if( !(nhandle = static_cast<int>(m_threads.size())) ) return 0;

	boost::thread** ph = new boost::thread*[ m_threads.size() ];
	int i=0;
	for(THREAD_SET_IT it=m_threads.begin(); it != m_threads.end(); ++it, ++i)
		ph[ i ] = it->first;
	return ph;
}

void ThreadManager::PrintThreadInfo()
{
	                                                  
                                                     
                                                                   
  
                    
                
     
   
        
                                               
                                                                        
   
                           
                       
                                       

                               
                                               
                                           

                
                           
                                                                                    
                                                                                 
                                                                                       
                                                                                     
                                                                                                  
                         

                                                                                
                                                                             
                                                                                   
                                                                                 
                                                                                              
                          
   
      
               
                
                                                              
        

                  
                
     
   
        
                                               
                                                                                          
                                                             
                          
                
    
            
   
                                                                                                 
   
        
                                                                                  
  
   
}

void ThreadManager::GetThreadInfo(std::vector<std::string>& vStartTimes,
								  std::vector<std::string>& vRouteIds,
								  std::vector<std::string>& vDataElements,
								  std::vector<std::string>& vDescriptions,
								  std::vector<long>& vMsgIds,
								  std::vector<long>& vPriorities)
{
	                                                  
                                                     
                     
                                                                   
  
                
     
   
        
                                               
                                                                        
   
                             
                       
                                         

                               
                                               
                                           

                
                             
                                                                                        
                                                                                     
                                                                                           
                                                                                         
                                                                                                      
                           

                                                                                    
                                                                                 
                                                                                       
                                                                                     
                                                                                                  
                            
   
      
                 

                                   
                                                  
                                        
                                                             
                                                               
                                      
                                                
                                           
                                                        
                
                 
        
  
   
}

bool ThreadManager::KillThread(std::string stRoute, KillTask ktKillType)
{
	ReportError( g_log_type, "KillThread() isn't implemented yet. Was called for: " + stRoute );
	                                          
                                                     
                                                                   
  
                                           
   
                                                                                                  
                              
                                                                                                
                                          
    
                                   
                          
                                                       
                                                                      
     
                                                       
                                            
     
                                                                          
                
                
    
       
    
                                
                                                 
                  
    
   
  
   
	return 0;
}

bool ThreadManager::KillAllThreads()
{
	ReportError( g_log_type, "KillAllThread() isn't implemented yet." );
	                                          
                                                     
                                                                   
  
                                                                                                 
                             
                                                                                               
                                         
   
                                  
                         
                                                      
                                  
    
                                                      
                                           
    
                                                                             
               
   
      
   
                               
                                                
                 
   
  
   
	return 0;
}

  
                                                                  
                                                                      
                            
  
bool ThreadManager::SwitchDatabaseServers(const std::string& stDatabaseServerName)
{
	boost::mutex::scoped_lock lock( m_thread_set_lock );
	for(THREAD_SET_IT it=m_threads.begin(); it!=m_threads.end(); ++it)
		it->second->SwitchDatabaseServers(stDatabaseServerName);
	return true;
}
