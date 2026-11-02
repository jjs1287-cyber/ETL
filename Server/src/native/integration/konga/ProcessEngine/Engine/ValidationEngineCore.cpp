/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/IDb/ActivityTab.h>		                             
#include <konga/Db/IDb/ArchiveProcessTab.h>                                   
#include <konga/Db/IDb/ProcessTab.h>		                            
#include <konga/Db/IDb/StatusTab.h>		                           
#include <konga/Db/IDb/TaskTab.h>			                         

#include <konga/Db/PDb/ProcessTab.h>		                            
#include <konga/Db/PDb/TaskTab.h>			                         
#include <konga/Db/PDb/TaskDataElementsTab.h>                                     

#include "EngineCore.h"
#include "engine_util.h"	                          
#include "IncomingMsg.h"	                                                

                                                                       
bool EngineCore::VerifyValidProcess(IncomingMsg* pmsg)
{
	                                 
	if ( pmsg->m_process_version > -1 ) return true;

	std::string stFilter;
	try {
		konga::SmartDbPtr<PDb::ProcessTab> pPDbProcessTab( PDb::ProcessTab::MakeInstance(pmsg->GetPDB()));
		konga::SmartDbPtr<IDb::ProcessTab> pIDbProcessTab( IDb::ProcessTab::MakeInstance(pmsg->GetIDB()));

		stFilter = "WHERE ProcessId = " + konga::toString(pmsg->m_pid) + " ORDER BY ProcessVersion DESC";
		pPDbProcessTab->Select(stFilter);
		if( !pPDbProcessTab->Next() )
		{			
			std::string stErr = "[Msg 79] [" + pmsg->GetRoute() + "] Process (" + toString(pmsg->m_pid) + ") not found in PDB";
			ReportError( g_log_type, stErr);
			MoveMessageToErrorQueue(pmsg, 79, stErr);
			return false;
		}

		pmsg->m_ProcessName = pPDbProcessTab->GetProcessName();

		if( !pPDbProcessTab->IsProcessTypeNull() )
			pmsg->SetProcessType( pPDbProcessTab->GetProcessType() );
		
		pIDbProcessTab->Select("WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ")");
		if( pIDbProcessTab->Next() && pIDbProcessTab->GetProcessVersion()>0 )
			pmsg->m_process_version = pIDbProcessTab->GetProcessVersion();
		else
			pmsg->m_process_version = pPDbProcessTab->GetProcessVersion();

		return true;
	} 
	catch(konga::DbException& e)  {
		std::string stErr = "[Msg 188] [" + pmsg->GetRoute() + "] Database error: VerifyValidProcess -- (PDB ProcessTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw;
	}
}

  
                                                                           
                                                                         
                                                                           
                                                                          
                                                                       
                                                           
  
bool EngineCore::ContinueProcess(IncomingMsg* pmsg)
{
	return true;
}
  
                                                                          
                                                                          
                                                                        
                                                                         
                                                                          
                   
                                                                          
  
bool EngineCore::VerifyValidProcessInstance(IncomingMsg* pmsg) const
{
	std::string stFilter;
	try  {
		konga::SmartDbPtr<PDb::ProcessTab> pPDBProcessTab( PDb::ProcessTab::MakeInstance(pmsg->GetPDB()));
		stFilter = "WHERE ProcessId = " + toString(pmsg->m_pid) + " ORDER BY ProcessVersion DESC";
		pPDBProcessTab->Select(stFilter);
		if( !pPDBProcessTab->Next() )
		{
			                                                                                
			return false;
		}
		if( pmsg->m_process_version == -1 )
			pmsg->m_process_version = pPDBProcessTab->GetProcessVersion();
	}
	catch( const konga::DbException& e ) {
		std::string stErr = "[Msg 191] [" + pmsg->GetRoute() + "] Database error: VerifyValidProcessInstance -- (PDBProcessTab) (" +
			stFilter + ") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw;
	}

	  
	                                                                                 
	  
	if( pmsg->IsAutoReRun() ) return true;

	try  {
		konga::SmartDbPtr<IDb::ProcessTab> pIDBProcessTab( IDb::ProcessTab::MakeInstance(pmsg->GetIDB()));
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ")";
		pIDBProcessTab->Select(stFilter);
		bool isValid = pIDBProcessTab->Next();
		if(!isValid)
		{
			try
			{
				konga::SmartDbPtr<IDb::ArchiveProcessTab> pArchiveProcessTab( IDb::ArchiveProcessTab::MakeInstance(pmsg->GetIDB()));
				pArchiveProcessTab->Select(stFilter);
				  
				                                                                             
				                                        
				  
				if( pArchiveProcessTab->Next() )
				{
					std::string stErr = "[Msg 311] [" + pmsg->GetRoute() + "] Instance " + toString(pmsg->m_piid) +
						" of Process '" + pmsg->GetProcessName() + "' was previously terminated."
						" No more work will be done for this instance of the process.";
					ReportError( g_log_type, stErr, EVENTLOG_WARNING_TYPE);
					return false;
				}
			} 
			catch(konga::DbException& e) 
			{
				std::string stErr = "[Msg 310] [" + pmsg->GetRoute() +
					"] Database error: VerifyValidProcessInstance -- (IDB ArchiveProcessTab) (" + stFilter + ") " +
					e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
				ReportError( g_log_type, stErr);
				throw;
			}
			std::string stErr = "[Msg 80] [" + pmsg->GetRoute() + "] Process " + toString(pmsg->m_pid) + " Instance " +
				toString(pmsg->m_piid) + " not found in IDB";
			ReportError( g_log_type, stErr);
		}
		return isValid;
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 192] [" + pmsg->GetRoute() + "] Database error: VerifyValidProcessInstance -- (IDBProcessTab) (" +
			stFilter + ") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw;
	}
	return false;
}

  
                                                                          
                                                                          
                                              
                                                             
                                                                       
                                                                          
                                                                     
                       
  
bool EngineCore::VerifyValidTask(IncomingMsg* pmsg, bool& bSuppressError) const
{

	std::string stFilter;

	try  {
		konga::SmartDbPtr<PDb::TaskTab> pPDBTaskTab( PDb::TaskTab::MakeInstance(pmsg->GetPDB()));
		konga::SmartDbPtr<IDb::TaskTab> pIDBTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));

		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			 ") AND (TaskId = " + toString(pmsg->m_tid) + ")";
		pPDBTaskTab->Select(stFilter);

		  
		                                                                        
		                   
		  
		if( !pPDBTaskTab->Next() )
		{
			if( pmsg->m_TaskName.length() )
			{
				std::string stErr = "[Msg 193] [" + pmsg->GetRoute() + "] Task " + pmsg->m_TaskName +
					" (id=" + toString(pmsg->m_tid) + ") in Process " + toString(pmsg->m_pid) + " not found in PDB";
				ReportError( g_log_type, stErr);
			}
			else
			{
				std::string stErr = "[Msg 194] [" + pmsg->GetRoute() + "] Task " + toString(pmsg->m_tid) +
					" in Process " + toString(pmsg->m_pid) + " not found in PDB";
				ReportError( g_log_type, stErr);
			}
			return false;
		}

		pmsg->m_TaskName = pPDBTaskTab->GetTaskName();

		if( !pPDBTaskTab->IsTaskTypeNull() ) pmsg->SetTaskType( pPDBTaskTab->GetTaskType() );
		else pmsg->SetTaskType( 0 );

		  
		                                                                                 
		  
		if( pmsg->IsAutoReRun() ) return true;

		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, pmsg->m_tid, pmsg->m_tiid); 
		                                                                                                       
		                                                                                                      
		                                                                                
		pIDBTaskTab->Select("WHERE " + stFilter);
		  
		                                                             
		                                                                       
		                                                                          
		                                                                     
		                       
		  
		if( !pIDBTaskTab->Next() )
		{
			return true;
		}
		if( !pIDBTaskTab->IsStatusNull() )
		{
			if( pIDBTaskTab->GetStatus() & STATUS_RERUN ) 
			{
				ReRunTask(pmsg);
				bSuppressError = true;                                                                                     
				return false;
			}
			else if( pIDBTaskTab->GetStatus() & STATUS_IN_PROGRESS ) 
			{
				std::string stErr = "[Msg 195] [" + pmsg->GetRoute() + "] Instance " + toString(pmsg->m_tiid) + 
					" of task \"" + pmsg->GetTaskName() + "\" in process \"" + pmsg->GetProcessName() +
					"\" is already in progress, but it was submitted again.";
				ReportError( g_log_type, stErr);
				return false;
			}
			else if( pIDBTaskTab->GetStatus() & STATUS_BLOCKED ) 
			{
				std::string stErr = "[Msg 196] [" + pmsg->GetRoute() + "] Instance " + toString(pmsg->m_tiid) + " of task \"" +
					pmsg->GetTaskName() + "\" in instance " + toString(pmsg->m_piid) + " of process \"" + pmsg->GetProcessName() + "\" is blocked.";
				ReportError( g_log_type, stErr, EVENTLOG_INFORMATION_TYPE);
				return false;
			}
		}
	} 
	catch( const konga::DbException& e )
	{
		std::string stErr = "[Msg 197] [" + pmsg->GetRoute() + "] Database error: VerifyValidTask -- (PDB or IDB TaskTab) (" +
			stFilter + ") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString(); 			
		ReportError( g_log_type, stErr);
		throw;
	}
	return true;
}
