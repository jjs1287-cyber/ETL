/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <fstream>

#include <time.h>	         

#include <boost/scoped_array.hpp>

#include <konga/FileTransfer/FileRetriever.h>		                      

#include <konga/Utils/CrException.h>		                         
#include <konga/Utils/StringUtils.h>		                                    
#include <konga/Utils/AutoPtrCleanup.h>		                
#include <konga/Utils/ErrorResolver.h>		                
#include <konga/Utils/Settings.h>			                      
#include <konga/Utils/FileUtils.h>			                   

#include <konga/Db/IDb/RouteDataElementTab.h>		                                          
#include <konga/Db/IDb/RouteDataElementTextTab.h>	                                         
#include <konga/Db/IDb/RouteTab.h>					                          
#include <konga/Db/IDb/TaskTab.h>					                         
#include <konga/Db/IDb/SpInsertTaskTab.h>			                                 

#include <konga/Db/MsgDb/OutgoingMsgTab.h>		                                  

#include <konga/Db/PDb/ProcessTab.h>			                            
#include <konga/Db/PDb/DocumentTab.h>			                             
#include <konga/Db/PDb/RouteTab.h>				                          
#include <konga/Db/PDb/RoutingRuleTab.h>		                                
#include <konga/Db/PDb/TaskTab.h>				                         
#include <konga/Db/PDb/TaskDataElementsTab.h>	                                     
#include <konga/Db/PDb/AttachmentTab.h>			                              

#include <konga/Db/MsgDb/AttachmentTab.h>		                              

#include <konga/Workflow/RuleTree.h>			                 
#include <konga/Workflow/node.h>				             

#include "EngineCore.h"
#include "engine_util.h"		                       

#include "IncomingMsg.h"		                                                
#include "TaskDataElement.h"	                        

extern std::string g_machine_name;

  
                                                             
                                                                         
                                                                          
  
bool EngineCore::RoutingRuleComplete(IncomingMsg* pmsg, int dtid, int dtiid)
{
	int rrid = GetRoutingRuleId(pmsg);
	if( rrid == -1 ) return true;

	  
	                                                                                
	                                                        
	  
	konga::SmartDbPtr<PDb::RoutingRuleTab> pRoutingRuleTab( PDb::RoutingRuleTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
						") AND (RoutingRuleId = " + toString(rrid) + ")";
		pRoutingRuleTab->Select(stFilter);

		if( !pRoutingRuleTab->Next() ) 
		{
			std::string stErr = "[Msg 152] [" + pmsg->GetRoute() + "] Failed to find RoutingRuleId " + toString(rrid) +
				" for process " + toString(pmsg->m_pid) + " and process version " + toString(pmsg->m_process_version) + " in PDB";
			ReportError( g_log_type, stErr);
			pRoutingRuleTab->Close();
			return false;
		}

		std::string sstack = pRoutingRuleTab->GetSequenceStack();
		pRoutingRuleTab->Close();
		if( EvaluateRoutingRule( pmsg, dtid, dtiid, sstack ) )
			return true;
	
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 153] [" + pmsg->GetRoute() + "] Database error: RoutingRuleComplete() II -- (PDBRoutingRuleTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pRoutingRuleTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
						
	pRoutingRuleTab->Close();
	return false;
}

  
                                                    
  
int EngineCore::GetRoutingRuleId(IncomingMsg* pmsg)
{
	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;
	int rrid;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
							") AND (TaskId = " + toString(pmsg->m_tid) + ")";
		pTaskTab->Select(stFilter);
		if( !pTaskTab->Next() ) 
		{
			OnError(-1);
			pTaskTab->Close();
			return -1;
		}

		rrid = pTaskTab->GetRoutingRuleId();
			
		pTaskTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 154] [" + pmsg->GetRoute() + "] Database error: GetRoutingRuleId -- (PDBTaskTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	return rrid;
}

bool EngineCore::EvaluateRoutingRule(IncomingMsg *pmsg, int dtid, int dtiid, const std::string& stack_str)	const
{

	  
	                                          
	  
	                                   

	if( !stack_str.length() ) return true;


	RuleTree rule_tree;

	try
	{
		rule_tree.parseStack(stack_str);
	}
	catch(char* s)
	{
		std::string stErr = "[Msg 155] [" + pmsg->GetRoute() + "] EvaluateRoutingRule(" + stack_str + "): " + std::string(s);
		ReportError( g_log_type, stErr);
		OnError(-1);
		return false;
	}

	  
	                                                                   
	                                                                      
	                               
	  
	konga::SmartDbPtr<IDb::RouteTab> pIDBRouteTab( IDb::RouteTab::MakeInstance(pmsg->GetIDB()));
	return EvaluateRoutingRule( pmsg, dtid, dtiid, rule_tree.getRoot(), pIDBRouteTab );
}

  
                                                              
  
bool EngineCore::EvaluateRoutingRule(IncomingMsg *pmsg, int dtid, int dtiid, node* pn, konga::SmartDbPtr<IDb::RouteTab>& pIDBRouteTab) const
{
	switch( pn->op() )
	{
	case node::op_and:
		{
			for(int i=0; i<pn->fanout(); ++i)
			{	
				if( !EvaluateRoutingRule(pmsg, dtid, dtiid, pn->get_node(i), pIDBRouteTab) ) return false;
			}
			return true;                              
		}
	case node::op_or:
		{
			for(int i=0; i<pn->fanout(); ++i)
			{
				if( EvaluateRoutingRule(pmsg, dtid, dtiid, pn->get_node(i), pIDBRouteTab) ) return true;
			}
			return false;                               
		}
	case node::op_not:
		{
			return !EvaluateRoutingRule(pmsg, dtid, dtiid, pn->get_node(0), pIDBRouteTab);
		}
	default:
		{
			std::string stFilter;
			try
			{
				int otid = atoi( pn->data().c_str() );

				if( otid == pmsg->m_tid ) return true;                                                                                                                    

				stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) +
					") AND (OriginTaskId = " + toString(otid) + ") AND (DestinationTaskId = " + toString(dtid) +
					") AND (DestinationTaskInstanceId = " + toString(dtiid) + ") ";

				if( !otid ) 
				{
					std::string stErr = "[Msg 156] [Rotue " + pmsg->GetRoute() + "] EvaluateRoutingRule, Incomplete routing rule for \"" + stFilter + "\"";
					ReportError( g_log_type, stErr);
					pIDBRouteTab->Close();
					return false;
				}

				pIDBRouteTab->Select(stFilter);

				bool res = pIDBRouteTab->Next();
				pIDBRouteTab->Close();

				return res;
			} 
			catch(konga::DbException& e) 
			{
				std::string stErr = "[Msg 157] [" + pmsg->GetRoute() + "] Database error: EvaluateRoutingRule -- (IDBRouteTab) " +
					e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
				pIDBRouteTab->Close();
				throw CrFatalException(CR_E_FAIL, stErr);
			}
		}
	}
	pIDBRouteTab->Close();
	return false;
}

int EngineCore::GetEscalationTaskId(IncomingMsg *nextTask, int& time)
{
	int rrid = GetRoutingRuleId( nextTask );
	if( rrid == -1 ) return -1;                                          

	konga::SmartDbPtr<PDb::RoutingRuleTab> pRoutingRuleTab( PDb::RoutingRuleTab::MakeInstance(nextTask->GetPDB()));
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(nextTask->m_pid) + ") AND (ProcessVersion = " + 
			toString(nextTask->m_process_version) + ") AND (RoutingRuleId = " + toString(rrid) + ")"; 

		pRoutingRuleTab->Select(stFilter);
		if( !pRoutingRuleTab->Next() ) 
		{
			std::string stErr = "[Msg 158] [" + nextTask->GetRoute() + "] CheckEscalation, Failed to find RoutingRuleId " + 
				toString(rrid) + " for process " + toString(nextTask->m_pid) + " and process version " +
				toString(nextTask->m_process_version) + " in PDB";
			ReportError( g_log_type, stErr);
			return -1;
		}

		char* time_str = strnewdup(pRoutingRuleTab->GetTimeToEscalation().c_str());
		char* time_str_copy = time_str;
		int len = pRoutingRuleTab->GetTimeToEscalation().length();

		if( *time_str == '[' )
		{
			++time_str;
			--len;
			int k = findk(time_str,']');
			if( time_str[k] )
			{
				time_str[k] = ';';
				time_str[k+1] = 0;
				time = nextTask->GetDataElement(time_str);
			}
			else time = 0;
		}
		else time = atoi( time_str );

		delete[] time_str_copy;
		return pRoutingRuleTab->GetEscalationDestinationTaskId();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 159] [Rotue " + nextTask->GetRoute() + "] Database error: CheckEscalation -- (PDBRoutingRuleTab) for " +
			stFilter + " -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	return -1;
}

  
                                                                              
                                                     
                                                                          
                                 
  
void EngineCore::CreateEscalation(IncomingMsg *pmsg, int dtid, int dtiid, int esctid, int esctiid, int time)
{
	CreateScheduledEvent(pmsg, esctid, esctiid, 60 * time);

	pmsg->m_esc_pid = pmsg->m_pid;
	pmsg->m_esc_piid = pmsg->m_piid;
	pmsg->m_esc_tid = esctid;
	pmsg->m_esc_tiid = esctiid;

	  
	                                                                                   
	                                                                              
	                     
	  
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;
	try
	{
		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, dtid, dtiid);
		pTaskTab->Select("WHERE " + stFilter);

		if( !pTaskTab->Next() )
		{
			std::string stErr = "[Msg 160] [" + pmsg->GetRoute() + "] CheckEscalation -- (IDBTaskTab) " + stFilter +
				" not found, escalation will not be in effect.";
			ReportError( g_log_type, stErr);
		}

		std::string esc_id = toString(pmsg->m_pid) + ';' + toString(pmsg->m_piid) + ';' + toString(esctid) + ';' + toString(esctiid) + ';';

		pTaskTab->Edit();
		pTaskTab->SetEscalationId(esc_id);
		pTaskTab->Update();
		pTaskTab->Close();
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 161] [" + pmsg->GetRoute() + "] CheckEscalation -- (IDBTaskTab) for " + stFilter + " -- " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}

	try
	{
		stFilter = BuildRouteWhereClause(pmsg->m_pid, pmsg->m_piid, esctid, esctiid);
		pTaskTab->Select("WHERE " + stFilter);

		if( !pTaskTab->Next() )
		{
			std::string stErr = "[Msg 162] [" + pmsg->GetRoute() + "] CheckEscalation -- (IDBTaskTab) " + stFilter + " not found, "
				"Will not be able to block the escalated task (when it arrives after escalation).";
			ReportError( g_log_type, stErr);
		}

		std::string esc_id = "-";                                                                                
		esc_id += toString(pmsg->m_pid) + ';' + toString(pmsg->m_piid) + ';' + toString(dtid) + ';' + toString(dtiid) + ';';

		pTaskTab->Edit();
		pTaskTab->SetEscalationId(esc_id);
		pTaskTab->Update();
		pTaskTab->Close();
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 163] [" + pmsg->GetRoute() + "] CheckEscalation -- (IDBTaskTab) for " + stFilter + " -- " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

void EngineCore::ResolveRole(const std::string& role_str, std::vector< std::string >& email_list)
{
	if( !role_str.length() ) return;
	char* emails = 0;
	if( strchr( role_str.c_str(), '@' ) == 0 )
	{		
		                                                      

		                                                       
		                                                                                          
		                                     
		return;
	}
	else
	{
		emails = strnewdup( role_str.c_str() );
	}

	char* emails_copy = emails;
	int k, len = strlen(emails);
	for(int i=0; len > 0; ++i)
	{
		k = findk( emails, ';');
		emails[k] = 0;
		email_list.push_back(emails);
		len -= k+1;
		if( len > 0 ) emails += k + 1;
	}
	delete []emails_copy;
}

  
                                                               
                                                     
                                                              
  
std::string EngineCore::GetDestinationRoleString(IncomingMsg* pmsg, int dtid)
{
	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;
	std::string role_str;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
						") AND (TaskId = " + toString(dtid) + ")";
		pTaskTab->Select(stFilter);

		if( !pTaskTab->Next() ) 
		{
			std::string stErr = "[Msg 167] [" + pmsg->GetRoute() + "] GetDestinationRoleString, Task not found for " + stFilter;
			ReportError( g_log_type, stErr);
			pTaskTab->Close();
			return 0;
		}

		if( pTaskTab->GetTaskRole().length() <= 0 )
		{
			  
			                                                                      
			  
			pTaskTab->Close();
			return std::string("no_role");

			                                                                                                          
			                                       
			                                  
		}

		  
		                                                      
		  
		if( pTaskTab->GetTaskRole()[0] == '"' )
			role_str = pTaskTab->GetTaskRole().substr(1,pTaskTab->GetTaskRole().length()-2);
		else role_str = pTaskTab->GetTaskRole();

		pTaskTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 169] [" + pmsg->GetRoute() + "] Database error: GetDestinationRoleString -- (PDBTaskTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	return role_str;
}

  
                                                                       
  
int EngineCore::GetDestinationDocumentId(IncomingMsg* pmsg, int dtid)
{
	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
				") AND (TaskId = " + toString(dtid) + ")";
		pTaskTab->Select(stFilter);

		if( !pTaskTab->Next() ) 
		{
			std::string stErr = "[Msg 170] [" + pmsg->GetRoute() + "] GetDestinationDocumentId, Task not found for " + stFilter;
			ReportError( g_log_type, stErr);
			pTaskTab->Close();
			return -1;
		}

		  
		                                                                 
		  
		int docId = atoi(pTaskTab->GetDocumentIdList().c_str());
		pTaskTab->Close();
		return docId;
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 171] [" + pmsg->GetRoute() + "] Database error: GetDestinationDocumentId() -- (PDBTaskTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

void EngineCore::ExecuteRoutingRule(IncomingMsg* pmsg, int routingRuleid )	                                              
{
	konga::SmartDbPtr<PDb::RoutingRuleTab> pRoutingRuleTab( PDb::RoutingRuleTab::MakeInstance(pmsg->GetPDB()));
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;

	int destination_task, dtiid;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			") AND (RoutingRuleId = " + toString(routingRuleid) + ")";
		pRoutingRuleTab->Select(stFilter);

		if( !pRoutingRuleTab->Next() ) 
		{
			std::string stErr = "[Msg 172] [" + pmsg->GetRoute() + "] ExecuteRoutingRule() routing rule not found for " + stFilter;
			ReportError( g_log_type, stErr);
			return;
		}

		destination_task = pRoutingRuleTab->GetDestinationTaskId();

		  
		                                          
		  
		                                                                                           

		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) +
						") AND (TaskId = " + toString(destination_task) + ") ORDER BY TaskInstanceId DESC";
		pTaskTab->Select(stFilter);
		                                                               

                                                     
               
                                                                    
                    
  
		bool found = false;
		while( pTaskTab->Next() )
		{
			  
			                                                                                                
			                                                                                               
			  
			                                     
			  
			if( pTaskTab->GetStatus() & STATUS_INITIALIZED &&
				!(pTaskTab->GetStatus() & (STATUS_IN_PROGRESS|STATUS_BLOCKED)) )
			{
				found = true;
				break;
			}
		}

		if( found )
		{
			dtiid = pTaskTab->GetTaskInstanceId();
		}
		else
		{
			time_t now;
			time(&now);

			SmartDbPtr<IDb::SpInsertTaskTab> pSpInsertTaskTab( IDb::SpInsertTaskTab::MakeInstance(pmsg->GetIDB()));

			pSpInsertTaskTab->SetProcessId(pmsg->m_pid);
			pSpInsertTaskTab->SetTaskId(destination_task);
			pSpInsertTaskTab->SetProcessInstanceId(pmsg->m_piid);
			pSpInsertTaskTab->SetTaskUser("unkown");
			pSpInsertTaskTab->SetInitTime(now);
			pSpInsertTaskTab->SetStatus(STATUS_INITIALIZED);

			try 
			{
				pSpInsertTaskTab->Execute();
			}
			catch(konga::DbException& e)
			{
				pSpInsertTaskTab->Close();
				std::string stErr = "[Msg 296] [" + pmsg->GetRoute() + "] Database error: WriteTaskInstance -- (IDBProcessTab) " +
					e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
				throw CrFatalException(CR_E_FAIL, stErr);
			}

			dtiid = 1;                                          
		}

		pTaskTab->Close();

		  
		                                                                    
		                                                                                      
		  
		PrepareRouteToTask(pmsg, destination_task, dtiid);

		char pcNextRoute[200];
		sprintf(pcNextRoute,"%d.%d.%d.%d", pmsg->m_pid, pmsg->m_piid, destination_task, dtiid);
		pmsg->SetDataElementValue("NextRouteID", pcNextRoute);
	
		if( !EvaluateRoutingRule( pmsg, destination_task, dtiid, pRoutingRuleTab->GetSequenceStack() ) )
		{
			pRoutingRuleTab->Close();
			return;
		}

		pRoutingRuleTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 173] [" + pmsg->GetRoute() + "] Database error: ExecuteRoutingRule -- (IDBTaskTab) SQL = '" +
			pTaskTab->GetWhereClause() + "', " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " +
			g_idb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		pRoutingRuleTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	  
	                                                                       
	                                                                               
	  
	RouteToTask(pmsg, destination_task, dtiid);
}

  
                                                                                    
                                                                                      
                                                                                        
                                                                                     
                                                                                     
                         
  
void EngineCore::PrepareRouteToTask(IncomingMsg* pmsg, int dtid, int dtiid)
{
	konga::SmartDbPtr<PDb::RouteTab> pPDBRouteTab( PDb::RouteTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;

	char* s_data_elements = 0;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			") AND (OriginTaskId = " + toString(pmsg->m_tid) + ") AND (DestinationTaskId = " + toString(dtid) + ")";
		pPDBRouteTab->Select(stFilter);

		if( !pPDBRouteTab->Next() || pPDBRouteTab->IsDataElementListNull() ) 
		{
			  
			                                                         
			  
			s_data_elements = new char[1];
			*s_data_elements = 0;
		}
		else
		{
			  
			                                                                       
			                                                          
			  
			s_data_elements = strnewdup(pPDBRouteTab->GetDataElementList().c_str());
		}

		pPDBRouteTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 174] [" + pmsg->GetRoute() + "] Database error: PrepareRouteToTask -- (PDBRouteTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pPDBRouteTab->Close();
		if( s_data_elements ) delete []s_data_elements;
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	AutoArrayDelete<char> adopt_s_data_elements(s_data_elements);

	konga::SmartDbPtr<IDb::RouteTab> pIDBRouteTab( IDb::RouteTab::MakeInstance(pmsg->GetIDB()));

	time_t now;
	time(&now);

	try
	{
		std::string stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ") "
			"AND (OriginTaskId = " + toString(pmsg->m_tid) + ") AND (OriginTaskInstanceId = " + toString(pmsg->m_tiid) + ") "
			"AND (DestinationTaskId = " + toString(dtid) + ") AND (DestinationTaskInstanceId =" + toString(dtiid) + ")";

		pIDBRouteTab->Select(stFilter);
		if( !pIDBRouteTab->Next() )
		{
			pIDBRouteTab->AddNew();

			pIDBRouteTab->SetProcessId(pmsg->m_pid);
			pIDBRouteTab->SetProcessInstanceId(pmsg->m_piid);
			pIDBRouteTab->SetDestinationTaskId(dtid);
			pIDBRouteTab->SetDestinationTaskInstanceId(dtiid);

			pIDBRouteTab->SetOriginProcessId(pmsg->m_pid);
			pIDBRouteTab->SetOriginProcessInstanceId(pmsg->m_piid);
			pIDBRouteTab->SetOriginTaskId(pmsg->m_tid);
			pIDBRouteTab->SetOriginTaskInstanceId(pmsg->m_tiid);

			pIDBRouteTab->SetSentTime(now);
			pIDBRouteTab->SetStatus(STATUS_INITIALIZED);
		}
		else
		{
			pIDBRouteTab->Edit();
			pIDBRouteTab->SetProcessId(pmsg->m_pid);
			pIDBRouteTab->SetProcessInstanceId(pmsg->m_piid);
			pIDBRouteTab->SetDestinationTaskId(dtid);
			pIDBRouteTab->SetDestinationTaskInstanceId(dtiid);

			pIDBRouteTab->SetOriginProcessId(pmsg->m_pid);
			pIDBRouteTab->SetOriginProcessInstanceId(pmsg->m_piid);
			pIDBRouteTab->SetOriginTaskId(pmsg->m_tid);
			pIDBRouteTab->SetOriginTaskInstanceId(pmsg->m_tiid);

			pIDBRouteTab->SetSentTime(now);
			pIDBRouteTab->SetStatus(STATUS_INITIALIZED);
		}
		pIDBRouteTab->Update();

		pIDBRouteTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 175] [" + pmsg->GetRoute() + "] Database error: PrepareRouteToTask -- (IDBRouteTab) " +
			e.GetErrorString() + "  [" + e.GetStateNativeOrigin() + "] ((P:" + toString(pmsg->m_pid) + ",PI:" + toString(pmsg->m_piid) +
			") from(" + toString(pmsg->m_tid) + "," + toString(pmsg->m_tiid) + ") to(" + toString(dtid) + "," + toString(dtiid) + ")) " +
			g_idb_connect.VerbosePrettyConnectInfoString();
		pIDBRouteTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

                                       
#if !defined(CR_USE_OLEDB_FOR_IDB_ROUTETAB)

	konga::SmartDbPtr<IDb::RouteDataElementTab> pRouteDataElementTab( IDb::RouteDataElementTab::MakeInstance(pmsg->GetIDB()));
	try
	{
		  
		                                                                              
		  
		pIDBRouteTab->Select();
		if(!pIDBRouteTab->Next())
		{
			                                                         
		}
		int iRouteInstanceId = pIDBRouteTab->GetRouteInstanceId();
		pIDBRouteTab->Close();

                                 

		int k, len = strlen(s_data_elements);
		for(; len>0; )
		{
			k = findk( s_data_elements, ';');
			s_data_elements[k] = 0;

			                                                                                
			IncomingMsg::name2de_constit cit = pmsg->m_elements.find(s_data_elements);
			TaskDataElement tde;
			if( cit == pmsg->m_elements.end() )
				tde = pmsg->GetDataElement(std::string("T0.")+s_data_elements);
			else
				tde = *cit->second;

			{	
				                                                                        
                                                                                                   
				pRouteDataElementTab->AddNew();
				pRouteDataElementTab->SetRouteInstanceId(iRouteInstanceId);
				pRouteDataElementTab->SetDataElementId(tde.GetId());
				pRouteDataElementTab->SetDataType(tde.GetType());
				pRouteDataElementTab->SetDataElementName(tde.m_name);

				switch( tde.GetType() )
				{
				case t_int:
				case t_long:
					pRouteDataElementTab->SetIntValue(static_cast<int>(tde));
					break;
				case t_single:
				case t_float:
				case t_double:
					pRouteDataElementTab->SetDoubleValue(tde);
					break;
				case t_text:
					{
					try {
						konga::SmartDbPtr<IDb::RouteDataElementTextTab> pRouteDataElementTextTab( IDb::RouteDataElementTextTab::MakeInstance(pmsg->GetIDB()));
						pRouteDataElementTextTab->AddNew();
						pRouteDataElementTextTab->SetDataElementId(tde.GetId());
						pRouteDataElementTextTab->SetRouteInstanceId(iRouteInstanceId);
						pRouteDataElementTextTab->SetStringValue(tde.m_value);
						pRouteDataElementTextTab->Update();
					} catch( const DbException& e ) {
						std::string stErr = "[Msg 421] [" + pmsg->GetRoute() + "] Database error when writing data element '" +
							cit->first + "' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
							"information was: " + g_idb_connect.PrettyConnectInfoString();
						ReportError( g_log_type, stErr);
                                          
						pRouteDataElementTab->Close();
						return;
					}
					break;
					}
				case t_string:
				case t_bool:
				default:
					pRouteDataElementTab->SetStringValue(tde.m_value);
				}

				pRouteDataElementTab->Update();
				pRouteDataElementTab->Close();
			}

			if( (len -= k+1) > 0 ) s_data_elements += k + 1;
		}
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 339] [" + pmsg->GetRoute() + "] Database error: PrepareRouteToTask -- (IDBRouteTab/IDBRouteDataElementTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] ((P:" + toString(pmsg->m_pid) + ",PI:" + toString(pmsg->m_piid) +
			") from(" + toString(pmsg->m_tid) + "," + toString(pmsg->m_tiid) + ") to(" + toString(dtid) + "," + toString(dtiid) + ")) " +
			g_idb_connect.VerbosePrettyConnectInfoString();
		pIDBRouteTab->Close();
		pRouteDataElementTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

#else
#error this part is not used
	int iRouteInstanceId = -1;
	try
	{
		  
		                                                                              
		  
		pRouteTab->Select();
		if(!pRouteTab->Next())
		{
			                                                         
		}
		iRouteInstanceId = pRouteTab->GetRouteInstanceId();
		pRouteTab->Close();
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 339] [" + pmsg->GetRoute() + "] Database error: PrepareRouteToTask -- (IDBRouteTab) %s  [%s] ((P:" + toString(xyx) + ",PI:" + toString(xyx) + ") from(" + toString(xyx) + "," + toString(xyx) + ") to(" + toString(xyx) + "," + toString(xyx) + ")) %s",
			pmsg->GetRoute().c_str(), e.GetErrorString(), e.GetStateNativeOrigin(),pmsg->m_pid,pmsg->m_piid,pmsg->m_tid,pmsg->m_tiid,dtid,dtiid,
			g_idb_connect.VerbosePrettyConnectInfoString().c_str());

		
		pRouteTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	
	ColedbIDBRouteDataElementTab  oledbIDBRouteDataElementTab;
	HRESULT hr = oledbpRouteDataElementTab->Open("SELECT RouteInstanceId, DataElementId, DataType, DataElementName, "
		"IntValue, DoubleValue, StringValue FROM RouteTab",0,DBGUID_DEFAULT,false);
	if( hr != S_OK )
	{
		std::string stErr = "[Msg 383] [" + pmsg->GetRoute() + "] Database error when preparing route. (%x). %s. The Connection "
			"information was: %s", pmsg->GetRoute().c_str(), hr, ErrorResolver().Resolve(hr,true), g_idb_connect.PrettyConnectInfoString().c_str());
		ReportError( g_log_type, stErr);
		oledbpRouteDataElementTab->Close();
		return;
	}											   

	int k, len = strlen(s_data_elements);
	for(; len>0; )
	{
		k = findk( s_data_elements, ';');
		s_data_elements[k] = 0;

		                                                                                
		IncomingMsgDb::name2de_constit cit = pmsg->m_elements.find(s_data_elements);
		if( cit != pmsg->m_elements.end() )
		{	
			pIDBRouteDataElementTab->SetRouteInstanceId(iRouteInstanceId)
			pIDBRouteDataElementTab->SetDataElementId(tde.GetId())
			pIDBRouteDataElementTab->SetDataType(tde.GetType())
			strcpy(pIDBRouteDataElementTab->GetDataElementName(), tde.m_name.c_str());

			switch( tde.GetType() )
			{
			case t_int:
			case t_long:
				pIDBRouteDataElementTab->SetIntValue(static_cast<int>(tde))
				pIDBRouteDataElementTab->GetIntValue()_status = DBSTATUS_S_OK;
				pIDBRouteDataElementTab->GetDoubleValue()_status = DBSTATUS_S_ISNULL;
				pIDBRouteDataElementTab->GetStringValue()_status = DBSTATUS_S_ISNULL;
				break;
			case t_single:
			case t_float:
			case t_double:
				pIDBRouteDataElementTab->SetDoubleValue(tde)
				pIDBRouteDataElementTab->GetDoubleValue()_status = DBSTATUS_S_OK;
				pIDBRouteDataElementTab->GetIntValue()_status = DBSTATUS_S_ISNULL;
				pIDBRouteDataElementTab->GetStringValue()_status = DBSTATUS_S_ISNULL;
				break;
			case t_string:
			case t_bool:
			default:
				pIDBRouteDataElementTab->GetStringValue()_status = DBSTATUS_S_OK;
				pIDBRouteDataElementTab->GetDoubleValue()_status = DBSTATUS_S_ISNULL;
				pIDBRouteDataElementTab->GetIntValue()_status = DBSTATUS_S_ISNULL;
				const char* cval = tde.m_value.c_str();
				DWORD cb, pos=0, len=strlen(cval), chunklen;
				do
				{
					if( len > pos ) break;
					if( len > (pos+4096) ) chunklen = 4096;
					else chunklen = len - pos;
					pIDBRouteDataElementTab->GetpStreamStringValue()->Write(cval+pos, chunklen, &cb);
					pos += cb;
				} while (cb > 0);
			}

			hr = oledbpRouteDataElementTab->Update();
			if( hr != S_OK )
			{
				std::string stErr = "[Msg 384] [" + pmsg->GetRoute() + "] Database error when preparing route. (%x). %s. The Connection "
					"information was: %s", pmsg->GetRoute().c_str(), hr, ErrorResolver().Resolve(hr,true), g_idb_connect.PrettyConnectInfoString().c_str());
				ReportError( g_log_type, stErr);
				oledbpRouteDataElementTab->Close();
				return;
			}											   
		}

		if( (len -= k+1) > 0 ) s_data_elements += k + 1;
	}
	oledbpRouteDataElementTab->Close();

#endif                                                

}

void EngineCore::RouteToTask(IncomingMsg* pmsg, int dtid, int dtiid)
{
	int document_id = GetDestinationDocumentId(pmsg, dtid);

	IncomingMsg *nextTask = ExecutePreRoleActivities(pmsg,dtid,dtiid);
	if( !nextTask ) return;
	AutoPtrDelete<IncomingMsg> adopted_nextTask(nextTask);

	std::string roleString = ReplaceDE_ExecScripts(nextTask,GetDestinationRoleString(nextTask, dtid));

	if( roleString.empty() || roleString == "-1" ) return;                                                                                 

	if( stricmp(roleString.c_str(), "no_role_wait") == 0 )
		return;

	  
	                                                                           
	  
	if( document_id <= 0 || roleString == "no_role" )
	{
		CreateFakeIncomingMessage(pmsg,pmsg->m_pid,pmsg->m_piid,dtid,dtiid);
		return;
	}

	std::vector< std::string > vec_to;
	try {
		ResolveRole( roleString.c_str(), vec_to );
	} catch (...) { return; }

	char* s_data_elements = 0;
	std::string msg_template_name, msg_template_path, msg_format, 
		msg_subject, msg_cc, msg_bcc, msg_URL, msg_replyTo, msg_from;
	int msg_priority;

	if( !GetDocumentDetails(pmsg, msg_template_name, msg_template_path, msg_format, msg_subject, msg_cc, msg_bcc,
					msg_replyTo, msg_from, msg_priority, msg_URL, s_data_elements, dtid, document_id) )
		return;
	boost::scoped_array<char> adopt_s_data_elements(s_data_elements);
	
	if( msg_from.empty() ) {
		msg_from = "ETLForge_Workflow@" + g_machine_name;
	}

	bool b_MHTML = false;
	if( strnicmp( msg_format.c_str(), "MHTML", 5 ) == 0 ) b_MHTML = true;
	
	  
	                                                                          
	                                                                     
	                                                                             
	  
	if( vec_to.size() == 0 )
	{
		std::string stErr = "[Msg 178] [" + pmsg->GetRoute() +
							"] RouteToTask: -- No valid destination email address found. Role = ' " + roleString + " '";			
		ReportError( g_log_type, stErr);
		return;
	}

	std::string resolved_role = "ResolvedRole=";
	resolved_role += vec_to[0];
	resolved_role += ';';
	nextTask->UpdateDataElements( resolved_role.c_str() ); 
	nextTask->WriteDataElementsToDB();


	                                                                                                       
	std::string query_string;
	char *s_data_elements_copy = s_data_elements;
	if( konga::GetOptionAsInt("Settings","RouteCompat",0) )
	{
		char buf[64];
		query_string += "pid=";
		query_string += konga::itoa(pmsg->m_pid, buf, 10);
		std::string route = buf;
		query_string += "&piid=";
		query_string += konga::itoa(pmsg->m_piid, buf, 10);
		route += '.'; route += buf;
		query_string += "&tid=";
		query_string += konga::itoa(dtid, buf, 10);
		route += '.'; route += buf;
		query_string += "&tiid=";
		query_string += konga::itoa(dtiid, buf, 10);
		route += '.'; route += buf;                  
		query_string += "&route=";
		query_string += route;
	}
	else
	{
		char buf[64];
		query_string += "route=";
		query_string += konga::itoa(pmsg->m_pid, buf, 10);
		query_string += '.';
		query_string += konga::itoa(pmsg->m_piid, buf, 10);
		query_string += '.';
		query_string += konga::itoa(dtid, buf, 10);
		query_string += '.';
		query_string += konga::itoa(dtiid, buf, 10);							  
	}
	if( s_data_elements ) query_string += "&";

	  
	                                                  
	                                                              
	  
	                                                                        
	                                                                              
	                                                                            
	                                                                           
	                                                                           
	                                        
	  

	std::map< std::string, std::string > local_elements;
	char* local_de_name;

	  
	                                                                                                     
	                                                                             
	  
	int k, len = s_data_elements ? strlen(s_data_elements) : 0;
	for(; len>0; )
	{
		                                                    
		k = findk( s_data_elements, '=');
		s_data_elements[k] = 0;                           
		local_de_name = s_data_elements;

		query_string += s_data_elements;
		query_string += '=';

		s_data_elements += k+1;                                                 
		len -= k+1;

		k = findk( s_data_elements, ';');
		s_data_elements[k] = 0;                                     

		std::string de_value;
		if( s_data_elements[0] == '[' )
		{
			++s_data_elements, --len, --k;
			int l = findk( s_data_elements, ']');
			if( len > 0 ) s_data_elements[l] = ';';                           
			de_value = nextTask->GetDataElement(s_data_elements).m_value;
		}
		else de_value = s_data_elements;

		query_string += de_value;
		local_elements[ local_de_name ] = de_value;

		len -= k+1;
		if( (len) > 0 )
		{
			s_data_elements += k+1;                                   
			if( *s_data_elements == ';' ) { ++s_data_elements; --len;}
			query_string += '&';
		}
	}

	delete []s_data_elements_copy;

	  
	                                                                                      
	  
	char QestionMarkOrAmpersand = ( msg_URL.find('?') == std::string::npos ) ? '?' : '&';
	std::string stMsgCompleteUrl = msg_URL + QestionMarkOrAmpersand + query_string;

	  
	                                                                                             
	  
	{
		bool replacedUrl=false;
		std::string tmpURL = stMsgCompleteUrl; stMsgCompleteUrl = "";
		ReplaceDataElementsForDocument(nextTask, tmpURL, stMsgCompleteUrl, local_elements, replacedUrl);
	}

	  
	                                                                             
	                                             
	  
	                                                                                
	                                                          
	  
	if( GetOptionAsInt("Settings", "OutlookWorkaround", 0) != 0 )
	{
		std::string tmp_url = stMsgCompleteUrl;
		stMsgCompleteUrl = "";
		ReplaceCharWithString(tmp_url, stMsgCompleteUrl, ' ', "%20");
	}
	else
	{
		#ifdef UrlEscape
		#undef UrlEscape
		#endif
		konga::UrlEscape(stMsgCompleteUrl);
	}

	if( stMsgCompleteUrl[ stMsgCompleteUrl.length() - 1 ] == '/' )
		stMsgCompleteUrl.erase( stMsgCompleteUrl.length() - 1 );

	if( stMsgCompleteUrl.length() )
		local_elements[ "URL" ] = stMsgCompleteUrl;

	                                                                                                       
	  
	                                                                                     
	  
	std::string prefix = "cr_";
	prefix += nextTask->GetRouteId();
	std::string filename = getTmpFileName(prefix);

	std::ofstream out(filename.c_str(), std::ios::out);
	if( !out.is_open() )
	{
		std::string stErr = "[Msg 176] [" + pmsg->GetRoute() + "] RouteToTask: Failed to open \" " + filename +
			" \" for writing (" + std::string( ErrorResolver().Resolve(GetLastError()) ) + ")";
		ReportError( g_log_type, stErr);			
		return;
	}

	std::string mhtmlUrl, dest;
	bool replacedUrl=false;
	  
	                                                                            
	                                        
	  
	if( strnicmp(msg_template_path.c_str(),"Inline",6) == 0 )
	{
		  
		                                                                             
		  
		ReplaceDataElementsForDocument(nextTask, msg_template_name, dest, local_elements, replacedUrl);
		                              
		                                                                                      
	}
	else                                              
	{
		std::string html_page = "";
		if( b_MHTML ) mhtmlUrl = msg_template_path;

		FileRetriever fr;
		if( !(html_page = fr.GetFileAsString(msg_template_path)).length() )
		{
			std::string stErr = "[Msg 177] [" + pmsg->GetRoute() + "] RouteToTask: Failed to retrieve \" " +
				msg_template_path + " \", err=" + toString(fr.LastErrorNumber());
			ReportError( g_log_type, stErr);			
			return;
		}

		ReplaceDataElementsForDocument(nextTask, html_page, dest, local_elements, replacedUrl);
	}

	
	if( b_MHTML )
	{
		std::string::size_type pos = dest.rfind("</html>");
		if( !dest.empty() ) 
			out << dest.substr(0,pos).c_str();
		if( pos == std::string::npos && (pos = dest.rfind("</HTML>")) == std::string::npos )
			out << "<html><body>";
		if( !replacedUrl && msg_URL.length() && stMsgCompleteUrl.length() ) 
			out << "<br><a href=\"" << stMsgCompleteUrl << "\"> Workflow URL </a>";
		if( pos == std::string::npos  )
			out << "</body></html>";
		if( pos != std::string::npos && !dest.substr(pos).empty() ) 
			out << dest.substr(pos).c_str();
	}
	else
	{
		if( !dest.empty() )
			out << dest.c_str();
		if( !replacedUrl && msg_URL.length() && stMsgCompleteUrl.length() ) out << ' ' << stMsgCompleteUrl;
	}
	if( out.is_open() ) out.close();

	  
	                                                
	  
	replacedUrl=false;
	std::string tmp = msg_subject; msg_subject = "";
	ReplaceDataElementsForDocument(nextTask, tmp, msg_subject, local_elements, replacedUrl);
	
	                                                                                                       
	  
	                                                                                                          
	                                                                                            
	  


	std::string cc, bcc, subject, URL, replyTo, from;
	if( msg_cc.length() ) cc = ReplaceDataElementWithValue(nextTask, msg_cc);
	if( msg_bcc.length() ) bcc = ReplaceDataElementWithValue(nextTask, msg_bcc);
	if( msg_subject.length() ) subject = ReplaceDataElementWithValue(nextTask, msg_subject);
	if( msg_URL.length() && stMsgCompleteUrl.length() ) URL = ReplaceDataElementWithValue(nextTask, stMsgCompleteUrl.c_str());
	if( msg_replyTo.length() ) { replyTo = ReplaceDataElementWithValue(nextTask, msg_replyTo); }
	if( msg_from.length() ) from = ReplaceDataElementWithValue(nextTask, msg_from);
	std::vector< std::string > vFileNames;

	konga::SmartDbPtr<PDb::AttachmentTab> pAttachmentTab( PDb::AttachmentTab::MakeInstance(pmsg->GetPDB()));
	try
	{		
		pAttachmentTab->Select("WHERE (ProcessId=" + toString(nextTask->m_pid) + ") AND (ProcessVersion=" + toString(nextTask->m_process_version) +
							    ") AND (DocumentId=" + toString(document_id) + ")");
		while(pAttachmentTab->Next())
			vFileNames.push_back( ReplaceDataElementWithValue(nextTask, pAttachmentTab->GetFileName() ));
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 360] [" + pmsg->GetRoute() + "] Database error: Failed to determine attachments. (PDB AttachmentTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pAttachmentTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	pAttachmentTab->Close();

	{
	konga::SmartDbPtr<MsgDb::OutgoingMsgTab> pOutgoingMsgTab( MsgDb::OutgoingMsgTab::MakeInstance(pmsg->GetMDB()));
	konga::SmartDbPtr<MsgDb::AttachmentTab> pAttachmentTab( MsgDb::AttachmentTab::MakeInstance(pmsg->GetMDB()));

	time_t now;
	time(&now);

	try
	{
		std::string to="";
		                                                       
		bool firstTime=true;
		for(std::vector<std::string >::const_iterator it = vec_to.begin() ; it != vec_to.end(); ++it)
		{
			if(!firstTime)
			{
				to += ';';				
				                                                                              
				nextTask->m_tiid = CreateTaskInstance(pmsg,nextTask->m_pid,nextTask->m_piid,nextTask->m_tid);
				                                                                   
				PrepareRouteToTask(pmsg, dtid, nextTask->m_tiid);
				std::string resolved_role = "ResolvedRole=";
				resolved_role += *it; resolved_role += ';';
				nextTask->UpdateDataElements( resolved_role.c_str() ); 
				nextTask->WriteDataElementsToDB();
			}
			firstTime = false;
			to += *it;
		}

		std::vector< std::string >::const_iterator itFileName = vFileNames.begin();
		for(int i=1; itFileName != vFileNames.end(); ++itFileName,++i)
		{
			if( itFileName->empty() )
				continue;

			  
			                                                                 
			                                                        
			  


                                                                   
                                                                        
    
                                
                                                                                                                  
                                                                                                                    
                                    
             
    
                                                                                                   
                                                                                
                                                      
  			
			bool bDeleteAfterAttached = IsFileRegisteredForProcessInstance(nextTask->m_pid, nextTask->m_piid, *itFileName, nextTask->GetIDB());
			RenameRegisteredFileForProcessInstance(nextTask->m_pid, nextTask->m_piid, nextTask->GetIDB(), *itFileName, "");

			pAttachmentTab->AddNew();
			pAttachmentTab->SetProcessId(nextTask->m_pid);
			pAttachmentTab->SetProcessInstanceId(nextTask->m_piid);
			pAttachmentTab->SetTaskId(nextTask->m_tid);
			pAttachmentTab->SetTaskInstanceId(nextTask->m_tiid);
			pAttachmentTab->SetAttachmentId(i);
			pAttachmentTab->SetFileName(*itFileName);
			pAttachmentTab->SetDeleteAfterAttached(bDeleteAfterAttached);
			pAttachmentTab->Update();
		}

		pOutgoingMsgTab->AddNew();

		pOutgoingMsgTab->SetMsgFrom(from);
		pOutgoingMsgTab->SetMsgTo(to);
		if( cc.length() ) pOutgoingMsgTab->SetMsgCC(cc);
		if( bcc.length() ) pOutgoingMsgTab->SetMsgBCC(bcc);
		if( replyTo.length() ) pOutgoingMsgTab->SetMsgReplyTo(replyTo);
		if( subject.length() ) pOutgoingMsgTab->SetMsgSubject(subject);
		pOutgoingMsgTab->SetMsgPriority(msg_priority);

		pOutgoingMsgTab->SetMessageFileName(filename);
		pOutgoingMsgTab->SetFormat(msg_format);
		pOutgoingMsgTab->SetSentAt("Not Sent");
		pOutgoingMsgTab->SetURL(URL);
		if( mhtmlUrl.length() ) pOutgoingMsgTab->SetMhtmlUrl(mhtmlUrl);

		pOutgoingMsgTab->SetDocumentId(document_id);
		pOutgoingMsgTab->SetProcessId(nextTask->m_pid);
		pOutgoingMsgTab->SetProcessVersion(nextTask->m_process_version);
		pOutgoingMsgTab->SetProcessInstanceId(nextTask->m_piid);
		pOutgoingMsgTab->SetTaskId(nextTask->m_tid);
		pOutgoingMsgTab->SetTaskInstanceId(nextTask->m_tiid);

		pOutgoingMsgTab->SetOwner(g_machine_name);

		pOutgoingMsgTab->Update();
		pOutgoingMsgTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 179] [" + pmsg->GetRoute() + "] Database error: (MDB OutgoingMsgTab/AttachmentTab) " + 
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		pOutgoingMsgTab->Close();
		pAttachmentTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	}

	if( nextTask->IsNotificationTask() )
	{
		                                                                                    
		                                                                                      
		                                                                                
		                                                                                 
		                    
		if( GetActivityRuleId(nextTask) > 0 )
			CreateFakeIncomingMessage(pmsg,nextTask->m_pid,nextTask->m_piid,nextTask->m_tid,nextTask->m_tiid);
		else
		{	
			UpdateTaskStatus(nextTask, STATUS_COMPLETED|STATUS_IN_PROGRESS);
			if( nextTask->IsTerminatingTask() ) pmsg->SetTaskType( konga::ttTERMINATING );
		}
	}
}

void EngineCore::ReplaceDataElementsForDocument(IncomingMsg* pmsg, const std::string& source, std::string& dest,
												const 	std::map< std::string, std::string >& local_elements, bool& replacedUrl)
{
	assert( &source != &dest );
	dest = "";
	TaskDataElement tmpDE;
	std::string stDeName;
	std::string::const_iterator kit;
	for( std::string::const_iterator it = source.begin(); it != source.end(); ++it)
	{
		if( *it == '[' )
		{
			if( (kit=std::find(it, source.end(), ']')) != source.end())
			{
				stDeName = std::string(it+1,kit);
				if( local_elements.find( stDeName ) != local_elements.end() )
				{
					if( stricmp(stDeName.c_str(),"url") == 0 ) replacedUrl = true;
					dest += local_elements.find( stDeName )->second;                       
					it = kit;
				}
				else if( (tmpDE=pmsg->GetDataElement( stDeName )).m_type != t_undefined )
				{
					dest += tmpDE.m_value;                       
					it = kit;
				}
				else dest += *it;
			}
		}
		else dest += *it;
	}
}

bool EngineCore::GetDocumentDetails(IncomingMsg* pmsg, std::string& msg_template_name, std::string& msg_template_path, std::string& msg_format,
	std::string& msg_subject, std::string& msg_cc, std::string& msg_bcc, std::string& msg_replyTo, std::string& msg_from,
	int& msg_priority, std::string& msg_URL, char*& s_data_elements, int dtid, int document_id)
{
	                                                                                                       
	  
	                                                                  
	                                                                                      
	                                                                                    
	                                                                         
	                                                      
	                                                                                           
	                                                                                               
	                                                                                         
	  

	konga::SmartDbPtr<PDb::DocumentTab> pDocumentTab( PDb::DocumentTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) + ") AND "
			"(TaskId = " + toString(dtid) + ") AND (DocumentId = " + toString(document_id) + ")";
		pDocumentTab->Select(stFilter);

		if( !pDocumentTab->Next() ) 
		{
			std::string stErr = "[Msg 180] [" + pmsg->GetRoute() + "] GetDocumentDetails: Document not found for " + stFilter;
			ReportError( g_log_type, stErr);
			pDocumentTab->Close();
			return false;
		}

		pDocumentTab->GetMsgTemplateLocation();
		pDocumentTab->GetMsgTemplateName();
		pDocumentTab->GetSendFormat();
		pDocumentTab->GetMsgSubject();
		pDocumentTab->GetURL();
		pDocumentTab->GetMsgCC();
		pDocumentTab->GetMsgBCC();
		pDocumentTab->GetMsgReplyTo();
		pDocumentTab->GetToDocDataElementList();

		msg_template_path = pDocumentTab->GetMsgTemplateLocation();
		msg_template_name = pDocumentTab->GetMsgTemplateName();
		msg_format = pDocumentTab->GetSendFormat();
		msg_subject = pDocumentTab->GetMsgSubject();		

		if( !pDocumentTab->IsURLNull() ) msg_URL = pDocumentTab->GetURL();
		if( !pDocumentTab->IsMsgCCNull() ) msg_cc = pDocumentTab->GetMsgCC();
		if( !pDocumentTab->IsMsgBCCNull() ) msg_bcc = pDocumentTab->GetMsgBCC();
		if( !pDocumentTab->IsMsgReplyToNull() ) msg_replyTo = pDocumentTab->GetMsgReplyTo();
		if( !pDocumentTab->IsMsgFromNull() ) msg_from = pDocumentTab->GetMsgFrom();
		if( !pDocumentTab->IsMsgPriorityNull() ) msg_priority = pDocumentTab->GetMsgPriority();
		else msg_priority = 3;

		if( pDocumentTab->IsToDocDataElementListNull() ) s_data_elements = 0;
		else s_data_elements = strnewdup(pDocumentTab->GetToDocDataElementList().c_str());

		pDocumentTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 181] [" + pmsg->GetRoute() + "] Database error: GetDocumentDetails -- (PDBDocumentTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pDocumentTab->Close();
		if( s_data_elements ) delete []s_data_elements;
		s_data_elements = 0;
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	return true;
}

  
                                                                 
                                                                                              
         
     
std::string EngineCore::GetDataElementsForRoute(IncomingMsg* pmsg, int dtid, int dtiid, int document_id, char sep_c)
{
	if( document_id <= 0 ) return "";
	std::string query_string="";
	konga::SmartDbPtr<PDb::DocumentTab> pDocumentTab( PDb::DocumentTab::MakeInstance(pmsg->GetPDB()));
	char* s_data_elements = 0;
	std::string stFilter;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) + ") AND "
			"(TaskId = " + toString(dtid) + ") AND (DocumentId = " + toString(document_id) + ")";
		pDocumentTab->Select(stFilter);

		if( !pDocumentTab->Next() ) 
		{
			std::string stErr = "[Msg 182] [" + pmsg->GetRoute() + "] GetDataElementsForRoute, Document not found for " + stFilter;
			ReportError( g_log_type, stErr);
			pDocumentTab->Close();
			return query_string;
		}

		if( pDocumentTab->IsToDocDataElementListNull() ) s_data_elements = 0;
		else s_data_elements = strnewdup(pDocumentTab->GetToDocDataElementList().c_str());

		pDocumentTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 183] [" + pmsg->GetRoute() + "] GetDataElementsForRoute -- (PDBDocumentTab) for " + stFilter +
			" -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		if( s_data_elements ) delete []s_data_elements;
		pDocumentTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	char *s_data_elements_copy = s_data_elements;

	if( s_data_elements ) query_string += sep_c;

	  
	                                                                       
	                                                                   
	                       
	  
	                                                                        
	                                                                              
	                                                                             
	                                                                            
	                                                                           
	                                  
	  
	std::map< int, std::map< std::string, TaskDataElement > > elements;

	std::map< std::string, std::string > local_elements;
	char* local_de_name;

	  
	                                                                                                             
	                                                                             
	  
	int k, len = s_data_elements ? strlen(s_data_elements) : 0;
	for(; len>0; )
	{
		                                                        
		k = findk( s_data_elements, '=');
		s_data_elements[k] = 0;                           
		local_de_name = s_data_elements;

		query_string += s_data_elements;
		query_string += '=';

		s_data_elements += k+1;                                                     
		len -= k+1;

		if( *s_data_elements == '[' )
		{
			++s_data_elements, --len;
			if( len > 0 )
			{
				k = findk(s_data_elements,']');
				s_data_elements[k] = ';';
			}
		}

		query_string += pmsg->GetDataElement(s_data_elements).m_value;

		if( len > 0 )
		{
			s_data_elements += k+1;                                   
			len -= k+1;
			if( *s_data_elements == ';' ) ++s_data_elements, --len;
			if( len > 0 ) query_string += sep_c;
		}
	}

	delete []s_data_elements_copy;
	return query_string;
}

