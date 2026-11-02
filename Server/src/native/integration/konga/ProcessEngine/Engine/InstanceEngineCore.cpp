/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>	   	                      
#include <konga/Utils/ErrorResolver.h>		                      
#include <konga/Utils/AutoPtrCleanup.h>		                          
#include <konga/Utils/CurrentTime.h>		                     
#include <konga/Utils/CrException.h>		                         

#include "EngineCore.h"
#include "engine_util.h"		                       
#include "resource.h"
#include "IncomingMsg.h"		                                                
#include "TaskDataElement.h"	                        
#include "EngineDebugUtils.h"	                     

#include <konga/Db/IDb/ActivityTab.h>
#include <konga/Db/IDb/InstanceInfoTab.h>
#include <konga/Db/IDb/ProcessTab.h>
#include <konga/Db/IDb/RouteTab.h>
#include <konga/Db/IDb/StatusTab.h>
#include <konga/Db/IDb/TaskTab.h>

#include <konga/Db/IDb/SpInsertTaskTab.h>
#include <konga/Db/IDb/SpInsertProcessTab.h>

#include <konga/Db/PDb/ProcessTab.h>
#include <konga/Db/PDb/TaskTab.h>

#ifdef DS_CLUSTER
extern std::set<long> g_slDistributionJobErrors;	                            
#endif

                                                          
                      
                         
#define USE_NEW_SP_FOR_PIID

namespace {
	time_t get_now() {
		time_t now;
		time(&now);
		return now;
	}
}

bool EngineCore::StartProcessInstance(IncomingMsg* pmsg)
{
	const std::string& stPID = toString(pmsg->m_pid);
	std::string stFilter;
	try 
	{
		  
		                                                                  
		  
		konga::SmartDbPtr<PDb::ProcessTab> pPDBProcessTab( PDb::ProcessTab::MakeInstance(pmsg->GetPDB()));
		stFilter = "WHERE ProcessId = " + stPID + " ORDER BY ProcessVersion DESC";
		pPDBProcessTab->Select(stFilter);
		if ( !pPDBProcessTab->Next() ) {
			std::string stErr = "[Msg 465] [" + pmsg->GetRoute() + "] Invalid ProcessID (" + stPID + ").";
			ReportError( g_log_type, stErr);
			return false;
		}
		pmsg->m_process_version = pPDBProcessTab->GetProcessVersion();
	} 
	catch(konga::DbException& e)
	{
		std::string stErr = "[Msg 116] [" + pmsg->GetRoute() + "] Database error: StartProcessInstance -- (PDBProcessTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		return false;
	}

	const time_t now(get_now());

#if defined USE_SP_FOR_PIID

	CSPInsertIDBProcessTab rs(pmsg->GetIDB());
	rs.m_ProcessId = pmsg->m_pid;
	rs.m_ProcessVersion = pmsg->m_process_version;
	rs.m_InitialTask = pmsg->m_tid;
	rs.m_StartTime = now;
	try 
	{		
		rs.Open(CRecordset::snapshot, _T("{CALL sp_insert_ProcessTab(?,?,?,?,?,?,?,?,?,?)}"));
	}
	catch(konga::DbException& e)
	{
		rs.Close();
		std::string stErr = "[Msg 294] [" + pmsg->GetRoute() + "] Database error: StartProcessInstance -- (IDB sp_insert_ProcessTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_Xdb_connect.VerbosePrettyConnectInfoString();
			pmsg->GetRoute().c_str(), e.GetErrorString(), e.GetStateNativeOrigin(),
			g_idb_connect.VerbosePrettyConnectInfoString().c_str());
		ReportError( g_log_type, stErr);
		
		return false;
	}

	                                                                                        
	                                                                                       
	                                                                                        
	                         
	
	                                                                  
	                                                                                 
	                                                
	
	                                                                   
	                                                                      

	while( rs.FlushResultSet())
	{
		while (!rs.IsEOF())
		{
			rs.MoveNext();
		}
	}
	pmsg->m_piid = rs.m_outputParam;

#elif defined USE_NEW_SP_FOR_PIID
	try {
		konga::SmartDbPtr<IDb::SpInsertProcessTab> pSpInsertProcessTab( IDb::SpInsertProcessTab::MakeInstance(pmsg->GetIDB()));
		pSpInsertProcessTab->SetInitialTask(pmsg->m_tid);
		pSpInsertProcessTab->SetProcessId(pmsg->m_pid);
		pSpInsertProcessTab->SetProcessVersion(pmsg->m_process_version);
		pSpInsertProcessTab->SetStartTime(now);
		pSpInsertProcessTab->SetStatus(STATUS_IN_PROGRESS);
		pSpInsertProcessTab->SetStatusTime(now);
		pSpInsertProcessTab->SetUpdateFlag(0);	                       
		pSpInsertProcessTab->Execute();
		pmsg->m_piid = pSpInsertProcessTab->GetProcessInstanceId();
	}
	catch( const konga::DbException& e ) {
		std::string stErr = "[Msg 117] [" + pmsg->GetRoute()
			+ "] Failed to start new process instance. Database error: "
			+ e.GetErrorString() + " [" + e.GetStateNativeOrigin()
			+ "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);		
		return false;
	}
#else

	try
	{
		konga::SmartDbPtr<IDb::ProcessTab> pIDBProcessTab( IDb::ProcessTab::MakeInstance(pmsg->GetIDB()));
		pIDBProcessTab->AddNew();
		pIDBProcessTab->SetProcessId(pmsg->m_pid);
		#ifndef USE_IDENTITY
			pmsg->m_piid = m_InstanceAlloc.AssignProcessInstanceId(pmsg,pmsg->m_pid,pmsg->m_process_version);
			pIDBProcessTab->SetProcessInstanceId(pmsg->m_piid);
		#endif                 
		pIDBProcessTab->SetProcessVersion(pmsg->m_process_version);
		pIDBProcessTab->SetInitialTask(pmsg->m_tid);
		pIDBProcessTab->SetStartTime(now);

		pIDBProcessTab->Update();
		pIDBProcessTab->Close();

		#ifdef USE_IDENTITY
			  
			                                                                        
			  
			stFilter = "WHERE ProcessId = " + stPID + " ORDER BY ProcessInstanceId DESC";
			pIDBProcessTab->Select(stFilter);
			pIDBProcessTab->Next();
			if( pIDBProcessTab->IsEOF() ) pmsg->m_piid = 1;
			else pmsg->m_piid = pIDBProcessTab->GetProcessInstanceId();
		#endif                
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 117] [" + pmsg->GetRoute() + "] Database error: StartProcessInstance -- (IDBProcessTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		
		return false;
	}
#endif                             

	return true;
}
  
                                                                                 
 
                                                                                                
                      
               

     
  
                                                                                                    
                                                                                       
                                

                                      
                                                  

                       
   
                              
  
                       
                                                                                                                               
                                                                                                                                  
                                   
  
             
 
  

                                                                                                                   
  
 
                                                                                       
                      
               

    
  
                                                                                                                                          
                                    
                             

                                                                      
                                   
                                            

                    
  
                              
  
                                                                                                                            
                                                                                                                                    
                                  
                    
  

             
 
  
  
                                                                    
                         
  
void EngineCore::WriteTaskInstance(IncomingMsg* pmsg)
{
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	char *escal_id=0;

	const time_t now(get_now());
	std::string stFilter;

	try
	{
		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid);
		pTaskTab->Select("WHERE" + stFilter);

		if( !pTaskTab->Next() || pmsg->m_tiid == 0 )	               
		{
			SmartDbPtr<IDb::SpInsertTaskTab> pSpInsertTaskTab( IDb::SpInsertTaskTab::MakeInstance(pmsg->GetIDB()));

			pSpInsertTaskTab->SetProcessId(pmsg->m_pid);
			pSpInsertTaskTab->SetTaskId(pmsg->m_tid);
			pSpInsertTaskTab->SetProcessInstanceId(pmsg->m_piid);
			pSpInsertTaskTab->SetTaskUser(pmsg->m_From);		                                                 
			pSpInsertTaskTab->SetInitTime(now);			                                                                   
			                                                       
			pSpInsertTaskTab->SetFromDocDataElementValueList(pmsg->m_from_doc_de_list);
			pSpInsertTaskTab->SetStatus(STATUS_IN_PROGRESS | STATUS_INITIALIZED);
			pSpInsertTaskTab->SetMsgId(pmsg->m_MsgId);
			pSpInsertTaskTab->SetMsgPriority(pmsg->GetMsgPriority());

			try 
			{
				pSpInsertTaskTab->Execute();
				pmsg->m_tiid = pSpInsertTaskTab->GetTaskInstanceId();
			}
			catch(konga::DbException& e)
			{
				pSpInsertTaskTab->Close();
				std::string stErr = "[Msg 295] [" + pmsg->GetRoute() + "] Database error: WriteTaskInstance -- (IDB sp_insert_TaskTab) " +
					e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
				throw CrFatalException(CR_E_FAIL, stErr);
			}
		}
		else if( pTaskTab->GetStatus() & STATUS_INITIALIZED )                                                                          
		{
			pTaskTab->Edit();
			pTaskTab->SetTaskUser(pmsg->m_From);		                                                 
			pTaskTab->SetStartTime(now);			                                                                   
			                                                                     
			pTaskTab->SetFromDocDataElementValueList(pmsg->m_from_doc_de_list);
			pTaskTab->SetStatus( pTaskTab->GetStatus() | STATUS_IN_PROGRESS );
			if( pTaskTab->GetEscalationId().length() > 0 )
				escal_id = strnewdup( pTaskTab->GetEscalationId().c_str() );
			pTaskTab->SetMsgId(pmsg->m_MsgId);
			pTaskTab->SetMsgPriority(pmsg->GetMsgPriority());
			pTaskTab->Update();
		}
		else
		{
			std::string stErr = "[Msg 120] [" + pmsg->GetRoute() + "] (IDBTaskTab) STATUS_INITIALIZED not set for \"" + stFilter + "\"";
			ReportError( g_log_type, stErr);
		}
		pTaskTab->Close();

	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 121] [" + pmsg->GetRoute() + "] Database error: (IDBTaskTab) (" +stFilter + ") " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	  
	                                                                              
	                                                                             
	                                                                             
	                                                   
	  
	if( escal_id )
	{
		char *tok = new char[256];
		AutoArrayDelete<char> adopt_escal_id(escal_id), adopt_tok(tok);
		std::string stEscalId = escal_id;
		int pid=-1, piid=-1, tid=-1, tiid=-1;
		bool is_scheduled_event = (*escal_id == '-');
		if( is_scheduled_event ) ++escal_id;
		if( !get_token(escal_id, tok, ";") || !(pid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(piid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(tid=atoi(tok)) ||
			!*escal_id || !(tiid=atoi(escal_id)) )
		{
			std::string stErr = "[Msg 122] [" + pmsg->GetRoute() + "] New task instance. Cancelling escallation: invalid escalation id (" +
				( stEscalId.empty() ? std::string("<null>") : stEscalId ) + ")";
			ReportError( g_log_type, stErr);
		}
		else if( !is_scheduled_event && !CancelScheduledEvent(pmsg,pid,piid,tid,tiid) )
		{
			                                                                                        
			                                                                                                         
			                                                
			                                  

			  
			                                                                         
			                                  
			  
			                                   
		}

		pmsg->m_esc_pid = pid;
		pmsg->m_esc_piid = piid;
		pmsg->m_esc_tid = tid;
		pmsg->m_esc_tiid = tiid;
			
	}
}

bool EngineCore::CancelEscalation(IncomingMsg* pmsg, char *escal_id)
{
	if( !escal_id ) return false;

	char *tok = new char[256];
	AutoArrayDelete<char> adopt_escal_id(escal_id), adopt_tok(tok);

	std::string stFilter;

	std::string stEscalId = escal_id;
	int pid=-1, piid=-1, tid=-1, tiid=-1;

	bool is_scheduled_event = (*escal_id == '-');
	if( is_scheduled_event ) ++escal_id;
	if( !get_token(escal_id, tok, ";") || !(pid=atoi(tok)) ||
		!get_token(escal_id, tok, ";") || !(piid=atoi(tok)) ||
		!get_token(escal_id, tok, ";") || !(tid=atoi(tok)) ||
		!get_token(escal_id, tok, ";") || !(tiid=atoi(tok)) )
	{
		std::string stErr = "[Msg 402] [" + pmsg->GetRoute() + "] Cancel escalation: invalid escalation id (" +
			(stEscalId.empty()?std::string("<null>"):stEscalId) + ")";
		ReportError( g_log_type, stErr);
	}
	else if( !is_scheduled_event && !CancelScheduledEvent(pmsg,pid,piid,tid,tiid) )
	{
		  
		                                                                       
		                                                                        
		                                                                      
		                                                                           
		                                                                      
		                                                                         
		                                                    
		  

		  
		                                                                        
		                                                                             
		                                                                    
		  
		int dtiid=-1;
		konga::SmartDbPtr<IDb::RouteTab> pRouteTab( IDb::RouteTab::MakeInstance(pmsg->GetIDB()));
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ") "
			"AND (OriginTaskId = " + toString(tid) + ") AND (OriginTaskInstanceId = " + toString(tiid) +
			") AND (DestinationTaskId = " + toString(pmsg->m_tid) + ")";
		
		try
		{
			pRouteTab->Select(stFilter);
			if(!pRouteTab->Next())
			{
				pRouteTab->Close();
				return false;
			}
			dtiid = pRouteTab->GetDestinationTaskInstanceId();
			pRouteTab->Close();
		}
		catch(konga::DbException& e) 
		{
			std::string stErr = "[Msg 308] [" + pmsg->GetRoute() + "] Database error: CancelEscalation -- (IDBRouteTab) " +
				e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] for " + stFilter + ". " + g_idb_connect.VerbosePrettyConnectInfoString();
			ReportError( g_log_type, stErr);
			pRouteTab->Close();
			return false;
		}
		
		  
		                                                                                              
		                                                                                          
		                                                                                         
		                                                                            
		  
		konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
		std::string stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid);

		try
		{
			pTaskTab->Select("WHERE " + stFilter);

			if( !pTaskTab->Next() )
			{
				pTaskTab->Close();                                  
				return false;
			}

			if( !pTaskTab->IsEscalationIdNull() && pTaskTab->GetEscalationId().length() > 0 )
			{
				char* escid = strnewdup( pTaskTab->GetEscalationId().c_str() );
				pTaskTab->Close();
				return CancelEscalation(pmsg, escid);
			}

			pTaskTab->Close();
		}
		catch(konga::DbException& e) 
		{
			std::string stErr = "[Msg 308] [" + pmsg->GetRoute() + "] Database error: CancelEscalation -- (IDBRouteTab) " +
				e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] for " + stFilter + ". " + g_idb_connect.VerbosePrettyConnectInfoString();
			ReportError( g_log_type, stErr);
			pRouteTab->Close();
			return false;
		}

		return false;
	}

	return true;
}

  
                                                                              
                                                      
  
bool EngineCore::EscalateTask(IncomingMsg* pmsg)
{
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	char *escal_id=0;

	try
	{
		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid);
		pTaskTab->Select("WHERE " + stFilter);

		if( !pTaskTab->Next() || pTaskTab->GetEscalationId().length() == 0)
		{
			pTaskTab->Close();
			return false;
		}
		else
			escal_id = strnewdup( pTaskTab->GetEscalationId().c_str() );

	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 273] [" + pmsg->GetRoute() + "] Database error: (IDBTaskTab) (" + stFilter + ") " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		pTaskTab->Close();
		return false;
	}

	if( escal_id )
	{
		char *escal_id_copy = escal_id, *tok = new char[256];
		int pid=-1, piid=-1, tid=-1, tiid=-1;
		bool is_scheduled_event = (*escal_id == '-');
		if( is_scheduled_event ) ++escal_id;
		if( !get_token(escal_id, tok, ";") || !(pid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(piid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(tid=atoi(tok)) ||
			!*escal_id || !(tiid=atoi(escal_id)) )
		{
			std::string stErr = "[Msg 274] [" + pmsg->GetRoute() + "] BlockEscalatedTask, invalid escalation id (" +
				escal_id + ") for \"" + stFilter + "\"";
			ReportError( g_log_type, stErr);
			delete[] tok;
			delete[] escal_id_copy;
			return false;
		}
			
		delete[] tok;
		delete[] escal_id_copy;

		if( is_scheduled_event )
			UpdateTaskStatus(pmsg, pid, piid, tid, tiid, STATUS_ESCALATED);

			pmsg->m_esc_pid = pid;
			pmsg->m_esc_piid = piid;
			pmsg->m_esc_tid = tid;
			pmsg->m_esc_tiid = tiid;

		return true;
	}
	return false;
}

  
                                                                                  
                                                
  
bool EngineCore::ReadEscalationIds(IncomingMsg* pmsg)
{
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	char *escal_id=0;

	try
	{
		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid);
		pTaskTab->Select("WHERE " + stFilter);

		if( !pTaskTab->Next() || pTaskTab->GetEscalationId().length() == 0)
		{
			pTaskTab->Close();
			return false;
		}
		else
			escal_id = strnewdup( pTaskTab->GetEscalationId().c_str() );

	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 276] [" + pmsg->GetRoute() + "] Database error: (IDBTaskTab) (" + stFilter + ") " + 
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		pTaskTab->Close();
		return false;
	}

	if( escal_id )
	{
		char *escal_id_copy = escal_id, *tok = new char[256];
		int pid=-1, piid=-1, tid=-1, tiid=-1;
		bool is_scheduled_event = (*escal_id == '-');
		if( is_scheduled_event ) ++escal_id;
		if( !get_token(escal_id, tok, ";") || !(pid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(piid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(tid=atoi(tok)) ||
			!get_token(escal_id, tok, ";") || !(tiid=atoi(tok)) )
		{
			std::string stErr = "[Msg 277] [" + pmsg->GetRoute() + "] ReadEscalationIds, invalid escalation id (" + escal_id 
				+ ") for \"" + stFilter + "\"";
			ReportError( g_log_type, stErr);
			delete[] tok;
			delete[] escal_id_copy;
			return false;
		}
			
		delete[] tok;
		delete[] escal_id_copy;

		return true;
	}
	return false;
}

void EngineCore::UpdateProcessStatus(IncomingMsg*  pmsg, int status)
{
	std::string stFilter;
	try
	{
		konga::SmartDbPtr<IDb::ProcessTab> pProcessTab( IDb::ProcessTab::MakeInstance(pmsg->GetIDB()));
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid)
			+ ") AND (ProcessVersion = " + toString(pmsg->m_process_version)
			+ ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ")";
		pProcessTab->Select(stFilter);
		if( !pProcessTab->Next() )
		{
			std::string stErr = "[Msg 124] [" + pmsg->GetRoute() + "] UpdateProcessStatus, IDB Process not found for \"" + stFilter + "\"";
			ReportError( g_log_type, stErr);
			pProcessTab->Close();
			return;
		}

		pProcessTab->Edit();
		if( pProcessTab->IsStatusNull() )
			pProcessTab->SetStatus(status);
		else
			pProcessTab->SetStatus( pProcessTab->GetStatus() | status );

		const time_t now(get_now());
		pProcessTab->SetStatusTime(now);

		if( status & STATUS_COMPLETED )
		{
			pProcessTab->SetEndTime(now);
		}

		                                                          
		                                                                
		if( pProcessTab->IsProcessDataElementValueListNull() )
			pProcessTab->SetProcessDataElementValueList("");
		pProcessTab->Update();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 125] [" + pmsg->GetRoute() + "] Database error: UpdateTaskStatus -- (IDBProcessTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	if( status & STATUS_COMPLETED )
		ArchiveProcessInstance(pmsg);
}

void EngineCore::UpdateTaskStatus(IncomingMsg*  pmsg, int status)
{
	pmsg->TaskStatus(status);
	UpdateTaskStatus(pmsg, pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid, status);
}

void EngineCore::UpdateTaskStatus(IncomingMsg*  pmsg, int pid, int piid, int tid, int tiid, int status)
{
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	std::string sql;
	try
	{
		stFilter = BuildRouteWhereClause(pid, piid, tid, tiid);
		pTaskTab->Select("WHERE " + stFilter);
		if( !pTaskTab->Next() )
		{
                                                                                                                             
                                        
                                     
			pTaskTab->Close();
			return;
		}

		pTaskTab->SetStatus( pTaskTab->GetStatus() | status );
		if( status & STATUS_COMPLETED )
		{
			const time_t endTime(get_now());
			pTaskTab->SetEndTime(endTime);
			sql = "UPDATE TASKTAB SET ENDTIME = " + toString(endTime) + ", STATUS = " + toString(pTaskTab->GetStatus()) + " WHERE " + stFilter;
		}
		else
			sql = "UPDATE TASKTAB SET STATUS = " + toString(pTaskTab->GetStatus()) + " WHERE " + stFilter;
		pmsg->GetIDB()->ExecuteSql(sql);

		pTaskTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 127] [" + pmsg->GetRoute() + "] Database error: UpdateTaskStatus, -- (IDBTaskTab) (" + sql +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	if( status & STATUS_COMPLETED )
	{

		pmsg->WriteDataElementsToDB();

		#ifdef DS_CLUSTER
		if( pmsg->IsMachineSpecificTask() )
		{
			string sJobInstanceId = pmsg->GetDataElementValue("JobInstanceId");
			long lJobInstanceId = atoi( sJobInstanceId.c_str() );

			if( status & STATUS_ERROR )
			{
				std::string stErr = "[Msg 417] [" + pmsg->GetRoute() + "] An error has occurred while processing a machine specific "
					"critical task. These tasks are system tasks that must complete successfully "
					"before any other processes can run. This server will now stop processing of "
					"regular messages until this error state is cleared through the user interface.";
				ReportError( g_log_type, stErr);
				m_ErrorStatus |= esMACHINE_SPECIFIC_MSG_ERROR;

				  
				                                                                         
				                                                                     
				  
				if( lJobInstanceId > 0 )
				{
					boost::mutex::scoped_lock lock(m_MacSpecificCounter_lock);
					g_slDistributionJobErrors.insert(lJobInstanceId);
					::SetDistributionJobErrorList(g_slDistributionJobErrors);                               
				}
			}
			else
			{
				if( lJobInstanceId > 0 )
				{
					boost::mutex::scoped_lock lock(m_MacSpecificCounter_lock);
					std::set<long>::iterator slIt = g_slDistributionJobErrors.find(lJobInstanceId);
					if( slIt != g_slDistributionJobErrors.end() )
						g_slDistributionJobErrors.erase(slIt);
					::SetDistributionJobErrorList(g_slDistributionJobErrors);                               
				}
			}
		}

		if( !(status & STATUS_ERROR) && pmsg->IsCrUpdateTask() )
			m_bCrUpdateMode = false;

		if( (m_ErrorStatus & esWAITING) && GetNumberOfRunningTasks() == 0 )
			g_FaultTolerantObj.SetWaitingIntention();
		#endif

		if( pmsg->IsTerminatingTask() )
			UpdateProcessStatus(pmsg, status);
	}
}

progress_status EngineCore::GetTaskStatus(IncomingMsg* pmsg, int pid, int piid, int tid, int tiid)
{
	progress_status status = STATUS_UNDEFINED;
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	try
	{
		stFilter = BuildRouteWhereClause(pid, piid, tid, tiid);
		pTaskTab->Select("WHERE " + stFilter);
		if( !pTaskTab->Next() )
		{
			std::string stErr = "[Msg 128] [" + pmsg->GetRoute() + "] GetTaskStatus, (IDBTaskTab) IDB Task not found for \"" + stFilter + "\"";
			ReportError( g_log_type, stErr);
			pTaskTab->Close();
			return status;
		}
                                                                                                    
		status = progress_status(pTaskTab->GetStatus());
		pTaskTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 129] [" + pmsg->GetRoute() + "] Database error: UpdateTaskStatus -- (IDBTaskTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		pTaskTab->Close();
	}
	return status;
}

void EngineCore::UpdateActivityStatus(IncomingMsg*  pmsg, int activity_id, int& activity_iid, int status)
{

	konga::SmartDbPtr<IDb::ActivityTab> pActivityTab( IDb::ActivityTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) +
			") AND (ActivityId = " + toString(activity_id) + ") AND (ActivityInstanceId = " + toString(activity_iid) + ")";
		pActivityTab->Select(stFilter);
		if( !pActivityTab->Next() || activity_iid == -1 )                            
		{
			const time_t now(get_now());

			pActivityTab->AddNew();
			pActivityTab->SetProcessId(pmsg->m_pid);
			pActivityTab->SetTaskId(pmsg->m_tid);
			pActivityTab->SetActivityId(activity_id);
			pActivityTab->SetProcessInstanceId(pmsg->m_piid);
			pActivityTab->SetTaskInstanceId(pmsg->m_tiid);
			pActivityTab->SetActivityInstanceId(1);
			pActivityTab->SetStartTime(now);
			pActivityTab->SetStatus(status);

			activity_iid = 1;
		}
		else
		{
			pActivityTab->Edit();
			pActivityTab->SetStatus( pActivityTab->GetStatus() | status );
			if( status & STATUS_COMPLETED )
			{
				pActivityTab->SetEndTime(get_now());
			}
		}
		pActivityTab->Update();
		pActivityTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 130] [" + pmsg->GetRoute() + "] Database error: UpdateActivityStatus -- (IDBActivityTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pActivityTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

  
                                                                         
                                                                            
  
void EngineCore::ArchiveProcessInstance(IncomingMsg* pmsg) const
{
	DumpDataElements(pmsg, "******* END Process ********", false);
	std::string stSql;

	try {
		SmartDbPtr<DbConnection> db_idb(konga::GetDbConnection(g_idb_connect.connectionString()));
		db_idb->SetQueryTimeout( GetODBCTimeOut() );
		
		PurgeRegisteredFilesForProcessInstance(pmsg->m_pid, pmsg->m_piid, db_idb);

		                                                    
		const std::string stProcInstWhere(" WHERE (ProcessId = " + toString(pmsg->m_pid) + 
			") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ")");

		stSql = "INSERT INTO ArchiveActivityTab (ProcessId, TaskId, ActivityId, ProcessInstanceId, TaskInstanceId, ActivityInstanceId, "
			"StartTime, EndTime, StatusTime, Status) SELECT "
			"ProcessId, TaskId, ActivityId, ProcessInstanceId, TaskInstanceId, ActivityInstanceId, "
			"StartTime, EndTime, StatusTime, Status FROM ActivityTab" + stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM ActivityTab" + stProcInstWhere);

		stSql = "INSERT INTO ArchiveProcessTab (ProcessId, ProcessInstanceId, ProcessVersion, InitialTask, StartTime, EndTime, Status, StatusTime, UpdateFlag, ProcessDataElementValueList)"
			" SELECT ProcessId, ProcessInstanceId, ProcessVersion, InitialTask, StartTime, EndTime, Status, StatusTime, "
			"UpdateFlag, ProcessDataElementValueList FROM ProcessTab"
			+ stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM ProcessTab" + stProcInstWhere);

		stSql = "INSERT INTO ArchiveRouteTab (ProcessId, ProcessInstanceId, DestinationTaskId, DestinationTaskInstanceId, OriginProcessId, "
			"OriginProcessInstanceId, OriginTaskId, OriginTaskInstanceId, RouteUser, SentTime, Status, RouteInstanceId)"
			"SELECT ProcessId, ProcessInstanceId, DestinationTaskId, DestinationTaskInstanceId, OriginProcessId, "
			"OriginProcessInstanceId, OriginTaskId, OriginTaskInstanceId, RouteUser, SentTime, Status, RouteInstanceId "
			"FROM RouteTab" + stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM RouteTab" + stProcInstWhere);

		stSql = "INSERT INTO ArchiveTaskTab "
			"(ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, TaskUser, InitTime, "
			"StartTime, EndTime, Status, DataElementValueList, FromDocDataElementValueList, EscalationId, MsgId, MsgPriority) "
			"SELECT ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, TaskUser, InitTime, "
			"StartTime, EndTime, Status, DataElementValueList, FromDocDataElementValueList, EscalationId, MsgId, MsgPriority "
			"FROM TaskTab" + stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM TaskTab" + stProcInstWhere);

		stSql = "INSERT INTO ArchiveDataElementTab "
			"(ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, DataElementId, DataType, "
			"DataElementName, IntValue, DoubleValue, StringValue) "
			"SELECT ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, DataElementId, DataType, "
			"DataElementName, IntValue, DoubleValue, StringValue "
			"FROM DataElementTab" + stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM DataElementTab" + stProcInstWhere);

		stSql = "INSERT INTO ArchiveDataElementTextTab "
			"(ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, DataElementId, StringValue) "
			"SELECT ProcessId, ProcessInstanceId, TaskId, TaskInstanceId, DataElementId, StringValue "
			"FROM DataElementTextTab" + stProcInstWhere;
		db_idb->ExecuteSql(stSql);
		if ( db_idb->GetAffectedRowCount() > 0 )
			db_idb->ExecuteSql("DELETE FROM ArchiveDataElementTextTab" + stProcInstWhere);
	}
	catch(konga::DbException& e )
	{
		std::string stErr = "[Msg 135] [" + pmsg->GetRoute() + "] Database error: ArchiveProcessInstance SQL statement = '" + stSql + "' "
			"-- Error: " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
}

void EngineCore::ReRunTask(IncomingMsg* pmsg) const
{
	  
	                                                                                    
	                                                              
	  
	int tiid = CreateTaskInstance(pmsg, pmsg->m_pid, pmsg->m_piid, pmsg->m_tid);

	DbConnection* pIDB = pmsg->GetIDB();

	  
	                                                                                      
	                                                                                   
	                                                                                     
	                                                                                     
	                                   
	  
	std::string stSqlRT = "{ CALL sp_copy_RouteTab(" + toString(pmsg->m_pid) + "," + toString(pmsg->m_piid) + "," + toString(pmsg->m_tiid) + "," + toString(tiid) + ") }";

	try
	{
		pIDB->ExecuteSql(stSqlRT);
	}
	catch(konga::DbException& e)
	{
		std::string stErr = "[Msg 368] [" + pmsg->GetRoute() + "] Database error: ReRunTask '" + stSqlRT + "' -- Error: " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}

	  
                                   
                                                        
                                                    
                          
                           
                                                                            
   
	char buf[256];
	sprintf(buf,"{%d.%d.%d.%d;}", pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, (pmsg->m_piid==0)?0:tiid);
	std::string stProcVars = buf;
	                            

	time_t t=0;
	  
                                                                                                   
              
                                                                                                                     
                                                                                                
                                      
                                                                           
                 
  
                                                                                                                                               
                                                                                                         
                                                    
                                  
                
                          
   

	  
	                                         
	  
                                                                  
                                                              
                                                                   
                                                                          
                                                          
                                                           
                                                            

                                                                                                                             
	std::string stSql = "{ CALL SpInsertIncomingMsgTab('" + SqlEscape(pmsg->m_From) + "','" + SqlEscape(pmsg->m_To) + "','" + SqlEscape(pmsg->m_Subject) +
		"','" + stProcVars + "'," + toString(time(&t)) + ",0," + toString(pmsg->m_MsgId) + ") }";

	try
	{
		pmsg->GetMDB()->ExecuteSql(stSql);
	}
	catch(konga::DbException& e)
	{
		std::string stErr = "[Msg 447] [" + pmsg->GetRoute() + "] Database error: ReRunTask '" + stSql + "' -- Error: " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}

	std::string stErr = "[Msg 446] Re-running task [" + pmsg->GetRoute() + "] since it has auto re-run enabled and was marked "
		"'In Progress' when the engine started. The new route id will be " + std::string(buf);
	ReportError( g_log_type, stErr, EVENTLOG_WARNING_TYPE);
}

