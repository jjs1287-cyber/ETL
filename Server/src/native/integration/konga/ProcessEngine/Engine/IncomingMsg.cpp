/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                           

#include <memory>	                                 
#include <complex>	                                 

#include <konga/Utils/AutoPtrCleanup.h>	                    
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/CrException.h>	                         
#include <konga/Utils/CurrentTime.h>	                     
#include <konga/Utils/Settings.h>

#include <konga/Db/IDb/DataElementTab.h>		                                
#include <konga/Db/IDb/DataElementTextTab.h>	                                    
#include <konga/Db/IDb/RouteTab.h>				                          
#include <konga/Db/IDb/RouteDataElementTab.h>	                                          
#include <konga/Db/IDb/RouteDataElementTextTab.h>	                                              
#include <konga/Db/IDb/TaskTab.h>				                         

#include <konga/Db/MsgDb/EventMsgTab.h>			                               
#include <konga/Db/MsgDb/IncomingMsgTab.h>		                                  
#include <konga/Db/MsgDb/DataElementTab.h>		                                       
#include <konga/Db/MsgDb/DataElementTextTab.h>	                                           

#include <konga/Db/PDb/ProcessTab.h>			                            
#include <konga/Db/PDb/TaskTab.h>				                         

#include <konga/OperationLog/OperationLog.h>	                     

#include "IncomingMsg.h"
#include "EngineCore.h"				                   
#include "engine_util.h"			                                                      
#include "EngineDebugUtils.h"		                       
#include "../ComponentInfo.h"		                                 

extern int g_log_type;
extern EngineCore* g_engine;

#define CR_USE_IDB_DATAELEMENTTAB

using namespace konga;
using namespace std;

IncomingMsg::IncomingMsg() :
	m_pid(-1), m_piid(-1), m_tid(-1), m_tiid(-1),
	m_opid(-1), m_opiid(-1), m_otid(-1), m_otiid(-1),
	m_esc_pid(-1), m_esc_piid(-1), m_esc_tid(-1), m_esc_tiid(-1),
	m_process_version(-1), m_oprocess_version(-1), m_MsgId(0),
	m_ReceivedAt(0), m_TaskType(-1), m_iPriority(0),
	m_iMsgPriority(5), m_bSingleThreadMode(false), m_bRemoveFromQueueWhenDone(true)
{
	Init();
}

IncomingMsg::IncomingMsg(int pid, int piid, int tid, int tiid, IncomingMsg*pmsg) :
	m_ProcessName( pmsg->m_ProcessName ),
	m_pid(pid), m_piid(piid), m_tid(tid), m_tiid(tiid),
	m_opid(pmsg->m_pid), m_opiid(pmsg->m_piid), m_otid(pmsg->m_tid), m_otiid(pmsg->m_tiid),
	m_esc_pid(pmsg->m_esc_pid), m_esc_piid(pmsg->m_esc_piid), m_esc_tid(pmsg->m_esc_tid), m_esc_tiid(pmsg->m_esc_tiid),
	m_process_version(pmsg->m_process_version), m_oprocess_version(pmsg->m_process_version),
	m_ReceivedAt(0), m_MsgId(pmsg->m_MsgId), m_TaskType(-1), m_iPriority(0), m_iMsgPriority(5),
	m_bSingleThreadMode(false), m_bRemoveFromQueueWhenDone(true), m_ProcessType(pmsg->m_ProcessType)
{
	Init();
}

IncomingMsg::IncomingMsg(konga::SmartDbPtr<MsgDb::IncomingMsgTab>& pIncomingMsgTab) :
	m_From( pIncomingMsgTab->GetMsgFrom() ),
	m_To( pIncomingMsgTab->GetMsgTo() ),
	m_Subject( pIncomingMsgTab->GetMsgSubject() ),
	m_ProcessVariables( pIncomingMsgTab->GetProcessVariables() ),
	m_ReceivedAt( pIncomingMsgTab->GetReceivedAt() ),
	m_MsgId(pIncomingMsgTab->GetMsgId()),
	m_pid(-1), m_piid(-1), m_tid(-1), m_tiid(-1),
	m_opid(-1), m_opiid(-1), m_otid(-1), m_otiid(-1),
	m_esc_pid(-1), m_esc_piid(-1), m_esc_tid(-1), m_esc_tiid(-1),
	m_process_version(-1), m_oprocess_version(-1),
	m_TaskType(-1), m_iPriority( MsgPriorityToThreadPriority(pIncomingMsgTab->GetMsgPriority() )),
	m_iMsgPriority(pIncomingMsgTab->GetMsgPriority()), m_bSingleThreadMode(false),
	m_bRemoveFromQueueWhenDone(true)
{
	Init();
}

IncomingMsg::IncomingMsg(const std::string& MsgFrom, const std::string& MsgTo, const std::string& MsgSubject, 
			const std::string& ProcessVariables, long ReceivedAt, long MsgId, long iMsgPriority, bool bSingleThreadMode) :
	m_From( MsgFrom ), m_To( MsgTo ), m_Subject( MsgSubject ), m_ProcessVariables( ProcessVariables ),
	m_ReceivedAt( ReceivedAt ), m_MsgId(MsgId),
	m_pid(-1), m_piid(-1), m_tid(-1), m_tiid(-1),
	m_opid(-1), m_opiid(-1), m_otid(-1), m_otiid(-1),
	m_esc_pid(-1), m_esc_piid(-1), m_esc_tid(-1), m_esc_tiid(-1),
	m_process_version(-1), m_oprocess_version(-1),
	m_TaskType(-1), m_iPriority(MsgPriorityToThreadPriority(iMsgPriority)),
	m_iMsgPriority(iMsgPriority), m_bSingleThreadMode(bSingleThreadMode),
	m_bRemoveFromQueueWhenDone(true)
{
	Init();
}

IncomingMsg::IncomingMsg(konga::SmartDbPtr<MsgDb::EventMsgTab>& pEventMsgTab) :
	m_From( pEventMsgTab->GetMsgFrom() ),
	m_To( pEventMsgTab->GetMsgTo() ),
	m_Subject( pEventMsgTab->GetMsgSubject() ),
	m_ProcessVariables( pEventMsgTab->GetProcessVariables() ),
	m_ReceivedAt( pEventMsgTab->GetReceivedAt() ), m_MsgId(-2),
	m_pid(-1), m_piid(-1), m_tid(-1), m_tiid(-1),
	m_opid(-1), m_opiid(-1), m_otid(-1), m_otiid(-1),
	m_esc_pid(-1), m_esc_piid(-1), m_esc_tid(-1), m_esc_tiid(-1),
	m_process_version(-1), m_oprocess_version(-1),
	m_TaskType(-1), m_iPriority(0),
	m_iMsgPriority(5), m_bSingleThreadMode(false), m_bRemoveFromQueueWhenDone(true)
{
	Init();
}

IncomingMsg::~IncomingMsg()
{
	if( m_bSingleThreadMode )
		g_engine->ExitSingleThreadMode();

	  
	                                                                       
	                                                                        
	            
	  
	g_thread_manager.ReleaseTask(this);
	g_thread_manager.DeleteThread( GetThreadObject() );

	for(name2de_it it = m_elements.begin(), end = m_elements.end();	it != end; ++it)
	{
		delete it->second;
	}
	m_elements.clear();

	#ifdef DS_USE_COM
	for(name2COM_it it = m_COMObjects.begin(), end = m_COMObjects.end();	it != end; ++it)
	{
		it->second->Release();
	}
	m_COMObjects.clear();

	for(name2ComToken_it it = m_ComTokens.begin(), end = m_ComTokens.end();	it != end; ++it)
	{
		try
		{
			GetExternalComCaller()->ReleaseInstance(it->second);
		}
		catch(_com_error&)
		{
		}
	}
	m_ComTokens.clear();
	#endif              

	for(name2db_it it = m_DBConnections.begin(), end = m_DBConnections.end();	it != end; ++it)
	{
		if( it->second->IsOpen() ) it->second->Close();
		delete it->second;
	}
	m_DBConnections.clear();

	delete m_hCancelEvent;
	delete m_hCancelCompletedEvent;
	delete m_pThreadObject;
}

void IncomingMsg::ParseProcessVariables()
{
	if( m_ProcessVariables == "Not Found" )
	{
		std::string stErr = "[Msg 268] [MsgId=" + toString(m_MsgId) + "] No process variables found!";
		throw stErr;
	}

	  
	                                
	                                                     
	  
	char* pv = strnewdup( m_ProcessVariables.c_str() );
	AutoArrayDelete<char> adopted_pv(pv);

	if( pv[0] != '{' )
	{
		std::string stErr = "[Msg 267] [MsgId=" + toString(m_MsgId) + 
				"] Syntax error while parsing incoming message (expecting '{'). String was '" + m_ProcessVariables + "'";
		throw stErr;
	}

	++pv;	           

	if( !pv || !*pv ) 
	{
		std::string stErr = "[Msg 266] [MsgId=" + toString(m_MsgId) + "] No process variables found!";
		throw stErr;
	}

	std::string proc_name, task_name;
	if( !parse_route_string(pv, m_pid, m_piid, m_tid, m_tiid, proc_name, task_name) )
	{
		std::string stErr;
		stErr = "[Msg 265] [MsgId=" + toString(m_MsgId) + "] Error parsing route '" + m_ProcessVariables + "'";
		throw stErr;
	}

	if( proc_name.length() )
	{
		konga::SmartDbPtr<PDb::ProcessTab> pProcessTab( PDb::ProcessTab::MakeInstance(GetPDB()));
		try 
		{
			pProcessTab->Select("WHERE UPPER(ProcessName) = UPPER('"
				+ SqlEscape(proc_name) + "') ORDER BY ProcessVersion DESC");
			pProcessTab->Next();
			if( !pProcessTab->IsEOF() )
			{
				if( 0 == (m_pid = pProcessTab->GetProcessId()) ) { pProcessTab->Close(); return; }
				m_ProcessName = pProcessTab->GetProcessName();
			}
			else
			{
				std::string stErr = "[Msg 403] [MsgId=" + toString(m_MsgId) + "] Could not find process \"" + 
					proc_name + "\" in PDB ProcessTab.";
				throw stErr;
			}
		} 
		catch(const konga::DbException& e) 
		{
			std::string stErr = "[Msg 106] [" + GetRoute() + "] Database error: ParseProcessVariables -- (PDBProcessTab) " + e.GetErrorString() + 
				" [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
			throw stErr;
		}
	}

	if ( !g_engine->VerifyValidProcess(this) ) {	                                                                                                      
		return;
	}

	if( task_name.length() )
	{
		konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(GetPDB()));
		std::string stFilter;
		try 
		{
			const std::string stFilter(
				"WHERE ProcessId=" + konga::toString(m_pid) + " AND ProcessVersion=" +
				konga::toString(m_process_version) + " AND UPPER(TaskName) = UPPER('"
				+ SqlEscape(task_name) + "')"
				);
			pTaskTab->Select(stFilter);
			if( pTaskTab->Next() )
			{
				m_tid = pTaskTab->GetTaskId();
				m_TaskName = pTaskTab->GetTaskName();
			}
			else
			{
				string s("[Msg 302] [" + GetRoute() + "] ParseProcessVariables, Could not find task '" + task_name
					+ "' in PDB TaskTab. Message was '" + m_ProcessVariables + "'");
				throw s;
			}
		} 
		catch( const konga::DbException& e )
		{
			std::string stErr = "[Msg 303] [" + GetRoute() + "] Database error: ParseProcessVariables -- (PDBTaskTab) " +
				e.GetErrorString() + "  [" + e.GetStateNativeOrigin() + "] Message was '" + m_ProcessVariables + "'. " + 
				g_pdb_connect.VerbosePrettyConnectInfoString();
			throw CrFatalException(CR_E_FAIL, stErr);
		}
	}

	char* pc=strrchr(pv,'}');
	if( !pc )
	{
		char s[256];
		sprintf(s,"[Msg 304] [%s] SYNTAX ERROR while parsing incoming message (expecting '}'). Message was '%s'",
			GetRoute().c_str(), m_ProcessVariables.c_str());
		throw s;
	}
	
	*pc = 0;

	m_from_doc_de_list = pv;                                                                                                    

}

void IncomingMsg::ReadMsgDataElements()
{
	konga::SmartDbPtr<MsgDb::DataElementTab> pMDBDataElementTab( MsgDb::DataElementTab::MakeInstance(GetMDB()) );

	std::string stWhereClause = "WHERE (MsgId = " + konga::toString(m_MsgId) + ")";
	try {
		pMDBDataElementTab->Select(stWhereClause);
	
		while(pMDBDataElementTab->Next())
		{
			char sBuf[50]; sBuf[0]=0;
			const std::string stDataType( pMDBDataElementTab->IsDataTypeNull() ? "" : pMDBDataElementTab->GetDataType() );
			std::string stValue;
			if( (strnicmp(stDataType.c_str(),"int",3) == 0 ||
				strnicmp(stDataType.c_str(),"lng",3) ==0 ||
				strnicmp(stDataType.c_str(),"srt",3) == 0) 
				&& !pMDBDataElementTab->IsIntValueNull() )
			{
				stValue = konga::toString(pMDBDataElementTab->GetIntValue());
			}
			else if( (strnicmp(stDataType.c_str(),"flt",3) == 0 ||
				strnicmp(stDataType.c_str(),"dbl",3) == 0) 
				&& !pMDBDataElementTab->IsDoubleValueNull() )
			{
				stValue = konga::toString(pMDBDataElementTab->GetDoubleValue());
			}
			else if( strnicmp(stDataType.c_str(),"str",3) == 0 )
			{
				if( pMDBDataElementTab->IsStringValueNull() )
				{
					                                                                                                      
					                            
					                                                                                 
					                                           
					stValue = "";
				}
				else
					stValue = pMDBDataElementTab->GetStringValue();
			}
			else if( strnicmp(stDataType.c_str(),"txt",3) == 0 )
			{
				konga::SmartDbPtr<MsgDb::DataElementTextTab> pMDBDataElementTextTab( MsgDb::DataElementTextTab::MakeInstance(GetMDB()) );
				pMDBDataElementTextTab->Select(stWhereClause);

				if( pMDBDataElementTextTab->Next() ) {
					stValue = pMDBDataElementTextTab->GetStringValue();
				}
			}
			else
			{
				std::string stErr = "[Msg 409] [" + GetRoute() + "] Unknown data type ('" + stDataType 
				+ "') when reading data element '" + pMDBDataElementTab->GetDataElementName() + "' from DB. %s. The Connection "
					"information was: " + g_mdb_connect.PrettyConnectInfoString();
				throw CrFatalException(CR_E_FAIL, stErr);
			}

			data_type type;
			int id=-1;
			std::string initialValue;

			GetDataElementTypeAndId(this,m_pid,m_process_version,*pMDBDataElementTab->GetDataElementName().c_str()=='$'?0:m_tid,
				pMDBDataElementTab->GetDataElementName().c_str(),type,id,initialValue);

			m_elements[ pMDBDataElementTab->GetDataElementName() ] = new TaskDataElement( stValue.c_str(),
				pMDBDataElementTab->GetDataElementName().c_str(), type, id );	
		}
	} catch(konga::DbException& e) {
		std::string stErr = "[Msg 337] [" + GetRoute() + "] Database error when reading data elements from DB. " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] The Connection information was: " +
			g_mdb_connect.PrettyConnectInfoString();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

void IncomingMsg::ReadDataElementsFromDB()
{
	konga::SmartDbPtr<IDb::DataElementTab> pIDBDataElementTab( IDb::DataElementTab::MakeInstance(GetIDB()) );

	std::string stFilter = BuildRouteWhereClause(m_pid, m_piid, m_tid, m_tiid);

	try {
		pIDBDataElementTab->Select("WHERE" + stFilter);

		while(pIDBDataElementTab->Next())
		{
			std::string stValue;
			bool bIsNull = false;
			switch( pIDBDataElementTab->GetDataType() )
			{
			case t_int:
			case t_long:
				stValue = konga::toString(pIDBDataElementTab->GetIntValue());
				break;
			case t_single:
			case t_float:
			case t_double:
				stValue = konga::toString(pIDBDataElementTab->GetDoubleValue());
				break;
			case t_text:
				{
				konga::SmartDbPtr<IDb::DataElementTextTab> pIDBDataElementTextTab( IDb::DataElementTextTab::MakeInstance(GetIDB()) );
				try {
					pIDBDataElementTextTab->Select(stFilter);

					if( pIDBDataElementTextTab->Next() ) {
						stValue = pIDBDataElementTextTab->GetStringValue();
					}
				} catch(const konga::DbException& e) {
					std::string stErr = "[Msg 419] [" + GetRoute() + "] Database error when reading data elements from DB. " +
						e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection information was: " + 
						g_idb_connect.PrettyConnectInfoString();
					throw CrFatalException(CR_E_FAIL, stErr);
				}
				}
				break;
			default:
				if( pIDBDataElementTab->IsStringValueNull() )
				{
					bIsNull = true;
					stValue.clear();
				}
				else
					stValue = pIDBDataElementTab->GetStringValue();
			}
			m_elements[ pIDBDataElementTab->GetDataElementName() ] = new TaskDataElement( stValue.c_str(),
				pIDBDataElementTab->GetDataElementName().c_str(), data_type(pIDBDataElementTab->GetDataType()),
				pIDBDataElementTab->GetDataElementId(), bIsNull );
		}
	} catch(konga::DbException& e) {
		std::string stErr = "[Msg 327] [" + GetRoute() + "] Database error when reading data elements from DB. " + 
			e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection information was: " +
			g_idb_connect.PrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		return;
	}

	pIDBDataElementTab->Close();

	UpdateDataElements(m_from_doc_de_list);                                                       

	  
	                                                                         
	  
	ReadMsgDataElements();
}

std::string IncomingMsg::GetDataElementValueList()
{
	std::string s = "";
	for(name2de_constit it = m_elements.begin(); it != m_elements.end(); ++it)
	{
		s += it->second->m_name + '=' + it->second->m_value + ';' ;
	}
	return s;		
}

void IncomingMsg::UpdateDataElements(const std::string& strPv1)
{
	if( strPv1.empty() ) return;
	std::string strPv;
	if( *(strPv1.end()-1) != ';' ) strPv = strPv1 + ';';                                            
	else strPv = strPv1;

	  
	                                                     
	                                                                          
	   
	int i = 0;
	int j = 0;
	while ((j = strPv.find("=", i)) < strPv.length())
	{
		std::string de;
		std::string val;

		for (int k = i; k < j; k++)	
			de += strPv.at(k); 	
		de += '\0';

		i = j + 1 ;                
		
		j = strPv.find("\"", i);

		int iend = 0;
		while ((iend = strPv.find(";", i)) > 0) 
		{
			                                                                                           
			if (iend < 3 || iend == i)   
				break;
			if (strPv.at(iend - 1) == '\"')
			{
				if (strPv.at(iend - 2) != '\\')      
					break;
				else if (strPv.at(iend - 3) == '\\')        
					break;
			}
			else if (j < 0 || j > iend)
				break;

			i = iend + 1;
		}

		if (iend < 0 || iend == (strPv.length() - 1))                                                               
		{
			                  
			if (iend < 0)
				iend = strPv.length() - 1;

			if (j < 0)               
			{
				for (int k = i; k < iend; k++)	
					val += strPv.at(k); 	
				val += '\0';
			}
			else                 
			{
				j++;                 
				for (int k = j; k < strPv.length(); k++)
				{
					if (strPv.at(k) == '\\')
						k++;                           
					val += strPv.at(k);
				}
				val += '\0';
				if (val.at(val.length() - 2) == '\"')                 
					val[val.length() - 2] = '\0';
			}
			i = strPv.length() - 1;
		}
		else if (j > iend || j < 0)                                                                        
		{
			for (int k = i; k < iend; k++)	
				val += strPv.at(k); 	
			i = iend + 1;
			val += '\0';
		}
		else                                               
		{
			j++;                 
			for (int k = j; k < iend && strPv.at(k) != '\"'; k++)
			{
				if (strPv.at(k) == '\\')
					k++;                           
				val += strPv.at(k);
			}
			val += '\0';
			if (val.at(val.length() - 2) == '\"')
				val[val.length() - 2] = '\0';
			i = iend + 1;
		}

		data_type type;
		int id=-1;
		string initialValue;
		GetDataElementTypeAndId(this, m_pid, m_process_version, (de.at(0) == '$') ? 0 : m_tid, de.c_str(), type, id, initialValue);

		TaskDataElement* pTde = new TaskDataElement( val.c_str(), de.c_str(), type, id );

		  
		                                                                        
		                                                                   
		  
		if (de.at(0) == '$' )
		{
			SetProcessDataElement(this, m_pid, m_piid, m_process_version, de.c_str(), val.c_str(), pTde);
			delete pTde;
			continue;
		}

		  
		                                                                                          
		  
		name2de_it it = m_elements.find(de.c_str());
		if( it != m_elements.end() )
		{
			delete it->second;
			m_elements.erase(it);
		}

		m_elements[ de ] = pTde;

		                                                                                                  

		if( id <= 0 )
		{
			                                                                    
			                                                                                                                                
			                                                           
			continue;
		}
	}
}

bool IncomingMsg::IsNonProcessDataElementDefined(const char* de_name) const {
	if ( de_name == 0 || *de_name == 0 )
		return false;
	return ( m_elements.find( string(de_name) ) != m_elements.end() );
}

  
                                                                           
                                                                                 
                           
  
string IncomingMsg::GetDataElementValue(const char* de_name)
{
	if( !de_name ) return "<undefined>";
	if( *de_name == '$' ) return GetProcessDataElement(this,m_pid,m_piid,m_process_version,de_name).m_value;

	if( m_elements.find( de_name ) != m_elements.end() )
		return m_elements.find( de_name )->second->m_value;
	else 
		return "0";
}

void IncomingMsg::SetDataElementValue(const char* de_name, const char* de_value)
{
	if( !de_name ) return;

	if( *de_name == '$' )
	{
		SetProcessDataElement(this,m_pid,m_piid,m_process_version,de_name,de_value);
		return;
	}

	name2de_it it = m_elements.find( de_name );
	if( it != m_elements.end() )
	{
		if( !de_value || !de_value[0] )
		{
			if( it->second->m_type == t_string )
				it->second->m_value = "";
			else
				it->second->m_value = "0";
		}
		else {
			it->second->m_value = de_value;
		}
	}
	else
	{
		try
		{
			  
			                                                                                  
			                                            
			  
			GetLocalDataElement(de_name);
			it = m_elements.find( de_name );
			if( it != m_elements.end() )
			{
				if( !de_value || !de_value[0] )
					it->second->m_value = "0";
				else
					it->second->m_value = de_value;
			}
		}
		catch(const char*)
		{
		}
	}

	if( it != m_elements.end() )
		DumpOneDataElement(this, *it->second);
}

TaskDataElement IncomingMsg::GetLocalDataElement(const char* de)
{
	name2de_it it=m_elements.find( de );
	if(  it!= m_elements.end() )
	{
		return *it->second;
	}
	else
	{
		if( strnicmp(de,"pid",3) == 0 )
		{
			char buf[20];
			konga::itoa(m_pid,buf,10);
			return TaskDataElement( buf, de, t_int, 0 );
		}
		else if( strnicmp(de,"piid",4) == 0 )
		{
			char buf[20];
			konga::itoa(m_piid,buf,10);
			return TaskDataElement( buf, de, t_int, 0 );
		}
		else if( strnicmp(de,"tid",3) == 0 )
		{
			char buf[20];
			konga::itoa(m_tid,buf,10);
			return TaskDataElement( buf, de, t_int, 0 );
		}
		else if( strnicmp(de,"tiid",4) == 0 )
		{
			char buf[20];
			konga::itoa(m_tiid,buf,10);
			return TaskDataElement( buf, de, t_int, 0 );
		}
		else if( stricmp(de,"GetRouteId()"       ) == 0 )
			return TaskDataElement( GetRouteId().c_str(), de, t_string, 0 );
		else if( stricmp(de,"GetTaskName()"       ) == 0 )
			return TaskDataElement(GetTaskName(),"GetTaskName()",t_string);
		else if( stricmp(de,"GetProcessName()"       ) == 0 )
			return TaskDataElement(GetProcessName(),"GetProcessName()",t_string);
		else if( stricmp(de,"GetLastError()") == 0 )
			return TaskDataElement(m_err.FullError(),"GetLastError()",t_string);

		  
		                                                                  
		                                                                                
		                                                          
		                                                                  
		  
		data_type type;
		int id=-1;
		string initialValue;
		GetDataElementTypeAndId(this,m_pid, m_process_version, m_tid,de,type,id,initialValue);

		if( id <= 0 )
		{
			         
			                                                                         
			                                        
			  
			if( strnicmp(de,"route",5) == 0 || strnicmp(de,"routeid",7) == 0 || strnicmp(de,"GetRouteId",10) == 0 )
				return TaskDataElement( GetRouteId().c_str(), de, t_string, 0 );
			else
				return TaskDataElement();
		}

		  
		                                                                                
		  
		it = m_elements.find(de);
		if( it == m_elements.end() )
		{
			TaskDataElement *pDE;
			m_elements[ de ] = pDE = new TaskDataElement( "0", de, type, id );
			return *pDE;
		}
		else return *it->second;
	}
}

  
                                                                                     
                                 
                                                                                                   
  
TaskDataElement IncomingMsg::GetDataElement(const std::string& str)
{
	char* s_data_elements = strnewdup(str.c_str());
	AutoArrayDelete<char> adapt(s_data_elements);

	  
	                                                            
	                                                                             
	  
	int k=-1, l, len = s_data_elements ? strlen(s_data_elements) : 0;
	for(; len>0; )
	{

RepeatForPipe:                                      
		if( !s_data_elements || !*s_data_elements )
			break;
		  
		                                                                                   
		  
		switch( s_data_elements[0] )
		{
		case 'T':
			{
			int tid = atoi(s_data_elements + 1);                      

			if( (l = findk( s_data_elements, '.' )) >= len ) break;
			s_data_elements += l+1;                                              
			len -= l+1;
			
			k = findk( s_data_elements, "|^;");
			char c_saved=s_data_elements[k];
			s_data_elements[k] = 0;	                          
			len -= (k+1);

			if( *s_data_elements == '$' )
				return GetProcessDataElement(this,m_pid,m_piid,m_process_version,s_data_elements);

			  
			                                                           
			                                                             
			                                                     
			  
			                                                           
			                                                             
			                                         
			  
			if( tid == 0 )                           
				return GetLocalDataElement(s_data_elements);
			else if( m_searched_tids.find( tid ) == m_searched_tids.end() )                                      
				LoadSourceDataElements(tid);

			tid2de_const_it tidmap_it = m_source_elements.find(tid);
			if( tidmap_it != m_source_elements.end() && 
				tidmap_it->second.find( s_data_elements ) != tidmap_it->second.end() )
			{
				return tidmap_it->second.find( s_data_elements )->second;
			}
			else if(c_saved == '|'||c_saved=='^')
			{
				s_data_elements += (k+1);
				goto RepeatForPipe;
			}
			else                                 
			{
				std::string stErr = "[Msg 111] [" + GetRoute() + "] None of the data elements (\"";
				stErr += s_data_elements;
				stErr += "\") found in IDBRouteTab";
				ReportError( g_log_type, stErr);
			}

			break;
			}
		case 'C':
			if( len>1 ) { s_data_elements += 2; len-=2; }              
			data_type type;
			if( len>1 && *s_data_elements == '"' )
			{
				type = t_string;
				++s_data_elements;
				k = findk( s_data_elements, '"');
				s_data_elements[k] = 0;	                          
			}
			else type = t_double;
			return TaskDataElement(s_data_elements, "constant", type);
		case '$':
			{
			int l = findk(s_data_elements,';');
			s_data_elements[l] = 0;
			return GetProcessDataElement(this,m_pid,m_piid,m_process_version,s_data_elements);
			}
		default:
			                                                                                         
			                                                                                       
			                                                                                       
			                            
			                                         

			                                               
			k = findk( s_data_elements, "|^;");
			s_data_elements[k] = 0;	                          
			len -= (k+1);
		}

		if( (len) > 0 && k > 0 )
		{
			s_data_elements += k+1;                                   
		}
	}
	return TaskDataElement();                                               
}

void IncomingMsg::LoadSourceDataElements(int stid)
{
	std::string stFilter = "WHERE (ProcessId = " + toString(m_pid) + ") AND (ProcessInstanceId = " + toString(m_piid) + ") AND "
		"(DestinationTaskId = " + toString(m_tid) + ") AND (DestinationTaskInstanceId = " + toString(m_tiid) + ") AND "
		"(OriginTaskId = " + toString(stid) + ")"; 


	int iRouteInstanceId = 0;
	{
	konga::SmartDbPtr<IDb::RouteTab> pRouteTab( IDb::RouteTab::MakeInstance(GetIDB()));
	try
	{
		pRouteTab->Select(stFilter);

		if( pRouteTab->Next() ) iRouteInstanceId = pRouteTab->GetRouteInstanceId();
		else
		{
			                           
			                                                                                                                   
			                                   
			return;
		}
	} 
	catch( const konga::DbException& e) 
	{
		std::string stErr = "[Msg 114] [" + GetRoute() + "] Database error: reading IDB RouteTab DataElementValueList -- (IDBRouteTab) " +
			 e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pRouteTab->Close();		
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	}

	std::map< std::string, TaskDataElement, std::less< const std::string& > > task_elements;

	stFilter = "WHERE RouteInstanceId = " + toString(iRouteInstanceId);

	konga::SmartDbPtr<IDb::RouteDataElementTab> pIDBRouteDataElementTab( IDb::RouteDataElementTab::MakeInstance(GetIDB()) );
	try {
		pIDBRouteDataElementTab->Select(stFilter);

		while(pIDBRouteDataElementTab->Next())
		{
			bool bIsNull = false;
			std::string stValue;
			switch( pIDBRouteDataElementTab->GetDataType() )
			{
			case t_int:
			case t_long:
				stValue = toString(pIDBRouteDataElementTab->GetIntValue());
				break;
			case t_single:
			case t_float:
			case t_double:
				stValue = toString(pIDBRouteDataElementTab->GetDoubleValue());
				break;
			case t_text:
				{
				std::string stSql = "SELECT StringValue FROM RouteDataElementTextTab ";
				konga::SmartDbPtr<IDb::RouteDataElementTextTab> pIDBRouteDataElementTextTab( IDb::RouteDataElementTextTab::MakeInstance(GetIDB()) );
				try {
					pIDBRouteDataElementTextTab->Select(stFilter);

					if( pIDBRouteDataElementTextTab->Next() )
						stValue = pIDBRouteDataElementTextTab->GetStringValue();
				} catch(konga::DbException& e) {
					std::string stErr = "[Msg 448] [" + GetRoute() + "] Database error when reading data elements from DB. (RouteDataElementTextTab)" +
						e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection information was: " + 
						g_idb_connect.PrettyConnectInfoString();
					throw CrFatalException(CR_E_FAIL, stErr);
				}
				break;
				}
			default:
				if( pIDBRouteDataElementTab->IsStringValueNull() )
				{
					bIsNull = true;
					stValue = "";
				}
				else
					stValue = pIDBRouteDataElementTab->GetStringValue();
			}

			data_type type;
			int id=-1;
			string initialValue;
			  
			                                                                                                
			  
			GetDataElementTypeAndId(this,m_pid,m_process_version,*pIDBRouteDataElementTab->GetDataElementName().c_str()=='$'?0:stid,
				pIDBRouteDataElementTab->GetDataElementName().c_str(),type,id,initialValue);

			task_elements[ pIDBRouteDataElementTab->GetDataElementName() ] = 
				 TaskDataElement(stValue.c_str(), pIDBRouteDataElementTab->GetDataElementName().c_str(), type, id, bIsNull );
		}
	} catch(DbException& e) {
		std::string stErr = "[Msg 338] [" + GetRoute() + "] Database error when reading route data elements from DB. " +
			e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection information was: " +
			g_idb_connect.PrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		return;
	}

	m_source_elements[ stid ] = task_elements;
	m_searched_tids.insert(stid);                                                                 
}

  
                                                                 
                                            
  
void IncomingMsg::WriteDataElementsToDB()
{
	  
	                                                                                    
	  
	for(name2de_constit cit = m_elements.begin(); cit != m_elements.end(); ++cit)
	{
		if( cit->second->GetId() <= 0 )                                          
			continue;

		konga::SmartDbPtr<IDb::DataElementTab> pDataElementTab( IDb::DataElementTab::MakeInstance(GetIDB()));
		std::string stFilter = BuildRouteWhereClause(m_pid, m_piid, m_tid, m_tiid) + 
								" AND (DataElementId = " + toString(cit->second->GetId()) + ")";
		try
		{
			pDataElementTab->Select("WHERE " + stFilter);
			bool bInsert = !pDataElementTab->Next();

			if( bInsert )
				pDataElementTab->AddNew();
			else
				pDataElementTab->Edit();

			pDataElementTab->SetProcessId(m_pid);
			pDataElementTab->SetProcessInstanceId(m_piid);
			pDataElementTab->SetTaskId(m_tid);
			pDataElementTab->SetTaskInstanceId(m_tiid);
			pDataElementTab->SetDataElementId(cit->second->GetId());
			pDataElementTab->SetDataType(cit->second->GetType());
			pDataElementTab->SetDataElementName(cit->first);
			switch( cit->second->GetType() )
			{
			case t_int:
			case t_long:
				pDataElementTab->SetIntValue(static_cast<int>(*cit->second));
				break;
			case t_single:
			case t_float:
			case t_double:
				pDataElementTab->SetDoubleValue(*cit->second);
				break;
			case t_text:
				{
				try {
					konga::SmartDbPtr<IDb::DataElementTextTab> pDataElementTextTab( IDb::DataElementTextTab::MakeInstance(GetIDB()));
					if( bInsert )
						pDataElementTextTab->AddNew();
					else
						pDataElementTextTab->Edit();

					pDataElementTextTab->SetProcessId(m_pid);
					pDataElementTextTab->SetProcessInstanceId(m_piid);
					pDataElementTextTab->SetTaskId(m_tid);
					pDataElementTextTab->SetTaskInstanceId(m_tiid);
					pDataElementTextTab->SetDataElementId(cit->second->GetId());
					pDataElementTextTab->SetStringValue(cit->second->m_value);
					pDataElementTextTab->Update();

				} catch(DbException& e) {
					std::string stErr = "[Msg 395] [" + GetRoute() + "] Database error when writing data element '" +
						cit->first + "' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() +
						 "]The Connection information was: " + g_idb_connect.PrettyConnectInfoString();
					ReportError( g_log_type, stErr);
					return;
				}
				break;
				}
			case t_string:
			case t_bool:
			default:
				pDataElementTab->SetStringValue(cit->second->m_value);
			}

			pDataElementTab->Update();
			pDataElementTab->Close();
		}
		catch(konga::DbException& e)
		{
			konga::SmartDbPtr<IDb::DataElementTab> pIDBDataElementTab( IDb::DataElementTab::MakeInstance(GetIDB()) );
			  
                                                                                                                                
    
                                                                                                                             
                                                                    
                                                                                                           
    
                             
                                             
    
     
			std::string stErr = "[Msg 326] [" + GetRoute() + "] Database error when writing data element '" + cit->first +
				"' to DB for " + stFilter + " -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " +
				g_idb_connect.VerbosePrettyConnectInfoString();
			throw CrFatalException(CR_E_FAIL, stErr);
		}
	}
	return;
}

#ifdef DS_USE_COM
void IncomingMsg::SetCOMObject(const char* obj_name, IDispatch* pdisp)
{
	name2COM_constit it = m_COMObjects.find( obj_name );
	                                                        
	if( it == m_COMObjects.end() ) 
		m_COMObjects[ obj_name ] = pdisp;
}

IDispatch* IncomingMsg::GetCOMObject(const char* obj_name)
{
	if( !obj_name ) return 0;
	name2COM_constit it = m_COMObjects.find( obj_name );
	if( it != m_COMObjects.end() )
		return it->second;
	return 0;                              
}

void IncomingMsg::SetCOMObjectToken(const char* obj_name, long lToken)
{
	name2ComToken_constit it = m_ComTokens.find( obj_name );
	if( it == m_ComTokens.end() ) 
		m_ComTokens[ obj_name ] = lToken;
}

long IncomingMsg::GetCOMObjectToken(const char* obj_name)
{
	if( !obj_name ) return 0;
	name2ComToken_constit it = m_ComTokens.find( obj_name );
	if( it != m_ComTokens.end() )
		return it->second;
	return 0;                              
}
#endif              

void IncomingMsg::SetDBConnection(const char* connect_str, DbConnection* pDb)
{
	name2db_it it = m_DBConnections.find( connect_str );
	if( it != m_DBConnections.end() )
	{
		if( pDb && it->second->IsOpen() )
		{
			it->second->Close();
			delete it->second;
		}
		m_DBConnections.erase(it);
	}
	if( it == m_DBConnections.end() && pDb ) 
	{
		if( &pDb == &it->second ) return;
		m_DBConnections[ connect_str ] = pDb;
	}
}

DbConnection* IncomingMsg::GetDBConnection(const char* connect_str)
{
	if( !connect_str ) return 0;
	name2db_constit it = m_DBConnections.find( connect_str );
	if( it != m_DBConnections.end() )
		return it->second;
	return 0;                              
}

namespace {
	const char nameUnknown[] = "Name_Unknown";
}
	
const char* IncomingMsg::GetTaskName()
{
	if( !m_TaskName.empty() ) return m_TaskName.c_str();
	  
	                                                                                     
	                               
	  
	if( g_exiting || !GetPDB() || !GetPDB()->IsOpen() ) return nameUnknown;

	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(GetPDB()));

	std::string stFilter = "WHERE (ProcessId = " + toString(m_pid) + ") AND (ProcessVersion = " + toString(m_process_version) +
								") AND (TaskId = " + toString(m_tid) + ")";
	try
	{
		pTaskTab->Select(stFilter);
		if( !pTaskTab->Next() )
		{
			return nameUnknown;
		}
		m_TaskName = pTaskTab->GetTaskName();
	}
	catch(const konga::DbException&)
	{
		return nameUnknown;
	}
	return !m_TaskName.empty() ? m_TaskName.c_str() : nameUnknown;
}

const char* IncomingMsg::GetProcessName()
{
	if( !m_ProcessName.empty() && m_ProcessName != "(null)" )
		return m_ProcessName.c_str();

	  
	                                                                                     
	                               
	  
	if( g_exiting || !GetPDB() || !GetPDB()->IsOpen() ) return nameUnknown;

	konga::SmartDbPtr<PDb::ProcessTab> pProcessTab( PDb::ProcessTab::MakeInstance(GetPDB()));
	std::string stFilter = "WHERE (ProcessId = " + toString(m_pid) + ") AND (ProcessVersion = " + toString(m_process_version) + ")";

	try
	{
		pProcessTab->Select(stFilter);
		if( !pProcessTab->Next() )
		{
			return nameUnknown;
		}
		m_ProcessName = pProcessTab->GetProcessName();
	}
	catch(const konga::DbException&)
	{		
		return nameUnknown;
	}
	return !m_ProcessName.empty() ? m_ProcessName.c_str() : nameUnknown;
}

std::string IncomingMsg::GetRouteId()
{
	return toString(m_pid) + '.' + toString(m_piid) + '.' + toString(m_tid) + '.' + toString(m_tiid);
}

std::string IncomingMsg::GetRoute()
{
	std::string route = "Process '";
	route += GetProcessName();
	route += "' (V.";
	route += toString(m_process_version);
	route += "), Task '";
	route += GetTaskName();
	route += "', route id = [";
	route += GetRouteId();
	route += "], MsgId=";
	route += toString(m_MsgId);

	return route;
}

void IncomingMsg::SetTaskType(long tt)
{ 
	m_TaskType = tt | ((m_TaskType<0 && m_TaskType!=-1) ? m_TaskType * -1 : 0); 
}

void IncomingMsg::ReadTaskType()
{
	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(GetPDB()));
	std::string stFilter;

	try 
	{
		stFilter = "WHERE (ProcessId = " + toString(m_pid) + ") AND (ProcessVersion = " + toString(m_process_version) +
			 ") AND (TaskId = " + toString(m_tid) + ")";
			
		pTaskTab->Select(stFilter);

		if( !pTaskTab->Next() )
		{
			if( m_TaskName.length() )
			{
				std::string stErr = "[Msg 270] [" + GetRoute() + "] Task " + m_TaskName + " (id=" + toString(m_tid) +
					") in Process " + toString(m_pid) + " not found in PDB";					
				ReportError( g_log_type, stErr);
			}
			else
			{
				std::string stErr = "[Msg 271] [" + GetRoute() + "] Task " + toString(m_tid) + " in Process " + 
					toString(m_pid) + " not found in PDB";
				ReportError( g_log_type, stErr);
			}
			pTaskTab->Close();
			SetTaskType( 0 );
			return;
		}

		if( !pTaskTab->IsTaskTypeNull() ) SetTaskType( pTaskTab->GetTaskType() );
		else SetTaskType( 0 );
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 272] [" + GetRoute() + "] Database error: ReadTaskType() -- (PDB TaskTab) (" + stFilter +
			") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString(); 
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

bool IncomingMsg::IsDebugEnabled() {
	static int lEnableDump = -1;
	if ( lEnableDump == -1 ) {
		lEnableDump = (::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "EnableDebugDump", 0)==0) ? 0 : 1;
	}
	                                                  
	if( lEnableDump == 1 ) return true;

	if( m_ProcessType & konga::ttDEBUG )
		return true; 
	if(m_TaskType<0) 
		ReadTaskType(); 
	return (m_TaskType & konga::ttDEBUG) == konga::ttDEBUG;
}

void IncomingMsg::Kill() {
	if( m_osProcId > 0 ) {

		                                                                                                                          
		m_wasKilled = true;
		KillProc(m_osProcId);

		string sMsg("[Msg 468] [" + GetRoute() + "] Task terminated by user. Task data: " + GetDataElementValueList());
		ReportError( g_log_type, sMsg, EVENTLOG_WARNING_TYPE);
	}
}
