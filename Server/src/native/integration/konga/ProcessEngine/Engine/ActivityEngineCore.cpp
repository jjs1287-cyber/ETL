/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/algorithm/string/trim.hpp>
#include <boost/thread/thread.hpp>
#include <boost/scoped_array.hpp>

#include <konga/FileTransfer/FileRetriever.h>		                    
#include <konga/CertManagement/CertInfo.h>			                

#include <konga/Utils/AutoFileCleanup.h>                            
#include <konga/Utils/AutoPtrCleanup.h>	                        
#include <konga/Utils/CrException.h>	                         
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/FileUtils.h>		                     
#include <konga/Utils/ProcessUtils.h>	                           
#include <konga/Utils/Settings.h>		                   

#include <konga/Log/logging.h>				                 

#include <konga/Db/IDb/ProcessTab.h>		                            
#include <konga/Db/IDb/RouteTab.h>			                          
#include <konga/Db/IDb/StatusTab.h>			                           
#include <konga/Db/IDb/TaskTab.h>			                         
#include <konga/Db/IDb/SpInsertTaskTab.h>	                                 

#include <konga/Db/PDb/ProcessTab.h>		                            
#include <konga/Db/PDb/DocumentTab.h>		                             
#include <konga/Db/PDb/ActivityRuleTab.h>	                                 
#include <konga/Db/PDb/ActivityTab.h>		                             
#include <konga/Db/PDb/TaskTab.h>			                         
#include <konga/Db/PDb/TaskDataElementsTab.h>                                     
#include <konga/Db/PDb/ActivityTab.h>  		                                  

#include <konga/ItemAccess/UserInfo.h>		                     
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Workflow/RuleTree.h>		                 
#include <konga/Workflow/node.h>			             

#include <konga/OperationEngine/OperationEngine.h>		                        
#include <konga/OperationObjects/OperationEngineException.h>	                                 

#include <konga/OperationLog/OperationLog.h>				                     

#include "engine_util.h"		                        

#include "EngineCore.h"

#include "IncomingMsg.h"		                                                
#include "TaskDataElement.h"	                        
#include "EngineDebugUtils.h"	                                 

#ifdef DS_USE_LDAP
 #include "LDAPWrapper.h"		                     
#endif

#include "SoapCaller.h"			                    
#include "SoapException.h"		                       

#include "../ComponentInfo.h"

using namespace konga;
using std::string;

int EngineCore::GetActivityRuleId( IncomingMsg *pmsg, tagWHICH_ACTIVITY which                       )
{
	AutoDebugDumper auto_dump(pmsg, "GetActivityRuleId() (ODBC)");

	konga::SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;

	int activityRuleId=0;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			") AND (TaskId = " + toString(pmsg->m_tid) + ")";

		pTaskTab->Select(stFilter);

		if( !pTaskTab->Next() ) 
		{
			std::string stErr = "[Msg 16] [" + pmsg->GetRoute() + "] " + std::string(which==PostRoleActivity?"Post":"Pre") +
				 "-role activity rule not found";
			throw CrFatalException(CR_E_FAIL, stErr);
		}

		activityRuleId = which==PostRoleActivity?pTaskTab->GetActivityRuleId():pTaskTab->GetPreRoleActivityRuleId();
	} 
	catch(const konga::DbException& e) 
	{
		std::string stErr = "[Msg 17] [" + pmsg->GetRoute() + "] Database error: (PDBTaskTab) -- " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	return activityRuleId;
}

  
                                                                           
                                                                   
                                                                           
                                   
  
void EngineCore::ExecuteActivities( IncomingMsg *pmsg )
{
	int activityRuleId = GetActivityRuleId( pmsg );
	if( activityRuleId <= 0 ) return;

	DumpDataElements(pmsg, "BEGIN Closing script");

	try
	{
		ExecuteActivityRule( pmsg, activityRuleId );
	}
	catch(InstanceTerminated&) {}                                                                                            

	DumpDataElements(pmsg, "END Closing script");
}

  
                                                                         
                                                                   
                                                                 
                                                                           
  
IncomingMsg * EngineCore::ExecutePreRoleActivities(IncomingMsg* pmsg, int dtid, int dtiid)
{
	IncomingMsg *nextTask = new IncomingMsg(pmsg->m_pid,pmsg->m_piid,dtid,dtiid,pmsg);
	AutoPtrDelete<IncomingMsg> adopted_nextTask(nextTask);
	nextTask->m_thread_data = pmsg->m_thread_data;
	nextTask->m_esc_pid = pmsg->m_pid;
	nextTask->m_esc_piid = pmsg->m_piid;
	nextTask->m_esc_tid = pmsg->m_esc_tid;
	nextTask->m_esc_tiid = pmsg->m_esc_tiid;

	int preRoleActivityRuleId = GetActivityRuleId( nextTask, PreRoleActivity );

	if( preRoleActivityRuleId > 0 )
	{
		bool bTerminated = false;

		DumpDataElements(nextTask, "BEGIN Opening script");

		try
		{
			ExecuteActivityRule( nextTask, preRoleActivityRuleId );
		}
		catch(InstanceTerminated&)
		{
			bTerminated = true;                                                                                            
		} 
		                                                    
		nextTask->WriteDataElementsToDB();

		DumpDataElements(nextTask, "END Opening script");

		if( bTerminated ) return 0;
	}

	                                                            
	int time;                                           
	int esc_tid = GetEscalationTaskId(nextTask, time);
	if( esc_tid > 0 && time > 0 )
	{
		int esc_tiid = CreateTaskInstance(pmsg, pmsg->m_pid,pmsg->m_piid,esc_tid,false);
		nextTask->m_esc_pid = pmsg->m_pid;
		nextTask->m_esc_piid = pmsg->m_piid;
		nextTask->m_esc_tid = esc_tid;
		nextTask->m_esc_tiid = esc_tiid;
		PrepareRouteToTask(nextTask,esc_tid,esc_tiid);
		CreateEscalation(pmsg,dtid,dtiid,esc_tid,esc_tiid,time);
	}

	adopted_nextTask.Release();                                                                         
	return nextTask;
}

  
                                                                                    
                                                                            
                                                                        
                                                                       
                                                                           
                                                                          
                                                                          
                                                                           
                                                                         
                                                                     
                                                 
                                                                          
                                                                             
                                                
  
void EngineCore::ExecuteActivityRule(IncomingMsg* pmsg, int activityRuleId )
{
	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "ExecuteActivityRule() ");

	if( activityRuleId <= 0 ) return;
	std::string stFilter;

	char* activityRules=0;

	try
	{
		konga::SmartDbPtr<PDb::ActivityRuleTab> pActivityRuleTab( PDb::ActivityRuleTab::MakeInstance(pmsg->GetPDB()));

		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + " ) AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			") AND (ActivityRuleId = " + toString(activityRuleId) + ")";
		pActivityRuleTab->Select(stFilter);

		if( !pActivityRuleTab->Next() ) 
		{
			std::string stErr = "[Msg 20] [" + pmsg->GetRoute() + "] ExecuteActivityRule: activity rule not found";
			ReportError( g_log_type, stErr);
			return;
		}

		std::string stTrueActivityList = boost::trim_right_copy(pActivityRuleTab->GetTrueActivityList());		
		std::string stFalseActivityList = pActivityRuleTab->IsFalseActivityListNull() ? "" : boost::trim_right_copy(pActivityRuleTab->GetFalseActivityList());

		if( pActivityRuleTab->IsActivityIdNull() || pActivityRuleTab->GetActivityId() <= 0 )
		{
			activityRules = strnewdup( stTrueActivityList.c_str() );
		}
		else if( ExecuteBooleanActivity( pmsg, pActivityRuleTab->GetActivityId() ) == true )
		{
			activityRules = strnewdup( stTrueActivityList.c_str() );
		}
		else
		{
			activityRules = strnewdup( stFalseActivityList.c_str() );
		}
	} 
	catch(konga::DbException& e)
	{
		std::string stErr = "[Msg 21] [" + pmsg->GetRoute() + "] Database error: ExecuteActivityRule: -- (PDBActivityRuleTab) (" + stFilter + ") " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		delete []activityRules;
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	AutoArrayDelete<char> adopt_activityRules(activityRules);
	if( !activityRules || activityRules == "" || strcmp(activityRules,"(null)") ==0 ) return;

	  
	                                                        
	                                                         
	                                                       
	                    
	  
	while( activityRules[0] )
	{
		switch( activityRules[0] )
		{
		case 'R':
			{
			++activityRules;								           
			int routineRuleId = atoi(activityRules);		                                                                           
			activityRules += findk( activityRules, ';' );
			ExecuteRoutingRule( pmsg, routineRuleId );
			if( activityRules[0] ) ++activityRules;			           
			}
			break;
		case 'A':
			{
			++activityRules;								           
			int activityRuleId1 = atoi(activityRules);		                             
			activityRules += findk( activityRules, ';' );
			ExecuteActivityRule( pmsg, activityRuleId1 );	            
			if( activityRules[0] ) ++activityRules;			           
			}
			break;
		default:
			{
			int activityId = atoi(activityRules);		                                
			activityRules += findk( activityRules, ';' );
			#ifdef DS_USE_CORBA
			CheckBreakpoint( pmsg, activityId );
			#endif
			ExecuteActivity( pmsg, activityId );
			if( activityRules[0] ) ++activityRules;			           
			}
		}
	}
}

  
                                                                                    
                                                              
  
struct FileTransferInfo
{
	FileTransferInfo(FileRetriever* pfr_, int fileMode_, FileRetriever::FileMode mode_,
		int responseMode_, FileRetriever::ResponseMode rMode_, IncomingMsg *pmsg_, char *Url_,
		char *FileOrString_, char *RespName_, int& Result_, std::string& Str_) :
		pfr(pfr_), fileMode(fileMode_), mode(mode_), responseMode(responseMode_), 
		rMode(rMode_), pmsg(pmsg_), Url(Url_), FileOrString(FileOrString_), RespName(RespName_),
		Result(Result_), Str(Str_) {}

	FileRetriever* pfr;
	int fileMode;
	FileRetriever::FileMode mode;
	int responseMode;
	FileRetriever::ResponseMode rMode;
	IncomingMsg *pmsg;
	char *Url;
	char *FileOrString;
	char *RespName;
	int& Result;
	std::string Str;
};

extern "C" void DoFileTransfer(void*p)
{
	FileTransferInfo* pFti = (FileTransferInfo*)p;
	try
	{
	if((pFti->mode == FileRetriever::Get_ASCII_File) 
		||(pFti->mode == FileRetriever::Get_ASCII_String) 
		|| (pFti->mode == FileRetriever::Get_UNICODE_File) 
		|| (pFti->mode == FileRetriever::Get_UNICODE_String))
	{
		if((pFti->mode == FileRetriever::Get_ASCII_File) 
			|| (pFti->mode == FileRetriever::Get_ASCII_String))
			pFti->pfr->PutResponseType(1);
		else
			pFti->pfr->PutResponseType(0);

		if((pFti->mode == FileRetriever::Get_ASCII_File) 
			|| (pFti->mode == FileRetriever::Get_UNICODE_File))
		{
			  
			                                                                      
			                                                                  
			  
			                                                               
			                                     
			                                   
			                                                         
			  
			                                                                   
			                                                                   
			             
			  

			std::string stLocalFileName = GetRegisteredTmpFileName(pFti->pmsg->m_pid, pFti->pmsg->m_piid, 
																				pFti->pmsg->GetIDB());
			std::string stOriginalLocalFileName = stLocalFileName;

			pFti->Result = pFti->pfr->GetAsFile(pFti->Url, stLocalFileName);

			  
			                                                                             
			                                                                                              
			  
			int pos1 = stLocalFileName.find_last_of('\\');
			int pos2 = stLocalFileName.find_last_of('/');
			int pos = pos1 > pos2 ? pos1 : pos2;
			                                
			   
				std::string stDir = ::getTmpFileName();
				kCreateDirectory(stDir);
				std::string stNewName = stDir + '\\' + stLocalFileName.substr(pos+1);
				if( konga::kMoveFile(stLocalFileName, stNewName) )
					stLocalFileName.swap(stNewName);
			   

			pFti->pmsg->SetDataElementValue(pFti->FileOrString, stLocalFileName.c_str());

			if( stOriginalLocalFileName != stLocalFileName )
				RenameRegisteredFileForProcessInstance(pFti->pmsg->m_pid, pFti->pmsg->m_piid, 
					pFti->pmsg->GetIDB(), stOriginalLocalFileName, stLocalFileName);

		}
		else{
			if((pFti->Result = pFti->pfr->GetAsString(pFti->Url, pFti->Str)) == Protocol::SUCCESS)
				pFti->pmsg->SetDataElementValue(pFti->FileOrString, pFti->Str.c_str());
		}
	}
	else if((pFti->mode == FileRetriever::Put_ASCII_File) 
		||(pFti->mode == FileRetriever::Put_ASCII_String) 
		|| (pFti->mode == FileRetriever::Put_UNICODE_File) 
		|| (pFti->mode == FileRetriever::Put_UNICODE_String))
	{
		if((pFti->mode == FileRetriever::Put_ASCII_File) 
			|| (pFti->mode == FileRetriever::Put_ASCII_String))
			pFti->pfr->PutResponseType(1);
		else
			pFti->pfr->PutResponseType(0);

		if((pFti->mode == FileRetriever::Put_ASCII_File) || 
			(pFti->mode == FileRetriever::Put_UNICODE_File))
		{
			string respStr = pFti->pmsg->GetDataElementValue(pFti->RespName);
			string pcStr = pFti->pmsg->GetDataElementValue(pFti->FileOrString);

			switch (pFti->rMode)
			{
			case FileRetriever::DATA:                          
				{
					pFti->Result = pFti->pfr->PutAsFile(pFti->Url, pcStr, respStr, FileRetriever::aString); 
					pFti->pmsg->SetDataElementValue(pFti->RespName, respStr.c_str());
					break;
				}
			case FileRetriever::FILENAME:                        
				{
					if( !respStr.empty() && respStr != "(null)" )
					{
						respStr = ::GetRegisteredTmpFileName(pFti->pmsg->m_pid, pFti->pmsg->m_piid, pFti->pmsg->GetIDB());
						pFti->pmsg->SetDataElementValue(pFti->RespName,respStr.c_str());
					}

					pFti->Result = pFti->pfr->PutAsFile(pFti->Url, pcStr, respStr, FileRetriever::aFile); 
					break;
				}
			default:
				{
					std::string stErr = "[Msg 325] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: no response string/file will be stored.";
					pFti->Result = pFti->pfr->PutAsFile(pFti->Url, pcStr);
					break;
				}
			}
		}
		else
		{
			string tmpStr = pFti->pmsg->GetDataElementValue(pFti->FileOrString);
			switch (pFti->rMode)
			{
			case 0:
				pFti->Result = pFti->pfr->PutAsString(pFti->Url, tmpStr);
				break;
			case FileRetriever::aString:                          
				{
					std::string respStr;
					pFti->Result = pFti->pfr->PutAsString(pFti->Url, tmpStr, respStr, FileRetriever::aString); 
					pFti->pmsg->SetDataElementValue(pFti->RespName, respStr.c_str());
					break;
				}
			case FileRetriever::aFile:                        
				{
					string respStr = pFti->pmsg->GetDataElementValue(pFti->RespName);

					if( respStr.empty() || respStr == "(null)" )
					{
						respStr = ::GetRegisteredTmpFileName(pFti->pmsg->m_pid, pFti->pmsg->m_piid, pFti->pmsg->GetIDB());
						pFti->pmsg->SetDataElementValue(pFti->RespName,respStr.c_str());
					}

					pFti->Result = pFti->pfr->PutAsString(pFti->Url, tmpStr, respStr, FileRetriever::aFile); 
					break;
				}
			default:
				{
					std::string stErr = "[Msg 351] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: \"" + pFti->Url +
						"\" failed to store the response string or file, for the wrong save response mode, should be Data/FileName";
					ReportError( g_log_type, stErr);
					pFti->Result = pFti->pfr->PutAsString(pFti->Url, tmpStr);
					break;
				}
			}
		}
	}
	else
	{
		                                      
		std::string stErr = "[Msg 300] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: \"" + pFti->Url +
			"\" failed. Error is: Wrong mode for FileTransfer. Mode given was " + toString(pFti->fileMode) +
			". Accepted pFti->modes are:\n1 - Getting an ASCII file\n2 - Getting an ASCII string\n3 - Getting a UNICODE file\n4 - Getting a UNICODE string\n5 - Putting an ASCII file\n6 - Putting an ASCII string\n7 - Putting a UNICODE file\n8 - Putting a UNICODE string";
		ReportError( g_log_type, stErr);
		pFti->pmsg->ErrSet(CR_E_FAIL, stErr);
	}
	}
	catch(CrFatalException& e)
	{
		std::string stErr = "[Msg 400] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: \"" + pFti->Url + "\" failed. " + e.GetErrorString();
		ReportError( g_log_type, stErr);
		pFti->pmsg->ErrSet(CR_E_FAIL, stErr);
	}
	catch(const char* s)
	{
		std::string stErr = "[Msg 400] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: \"" + pFti->Url + "\" failed.  " + std::string(s);
		ReportError( g_log_type, stErr);
		pFti->pmsg->ErrSet(CR_E_FAIL, stErr);
	}
#ifndef _DEBUG
	catch(...)
	{
		std::string stErr = "[Msg 400] [" + pFti->pmsg->GetRoute() + "] ExecuteActivity: \"" + pFti->Url + "\" failed. Unknown error.";
		ReportError( g_log_type, stErr);
		pFti->pmsg->ErrSet(CR_E_FAIL, stErr);
	}
#endif
}

namespace {
	void LogFatalToOperationLog(const string& stMsg, long long llOperationInstanceId, long lErr) {
		const string& dir = GetServerHomeWithSlash();
		string stErr("The operation failed unexpectedly. Please report this problem to support@etlforge.invalid or http://www.etlforge.invalid/forums. " \
			"Include as much information as you can to your bug report, such as '" +
			dir + "log/OperationEngine.log' and files in '" +
			dir + "DataInterchange/Temp/LOG/' so that the problem can be diagnosed.");
		stErr += "\n\n";
		stErr += stMsg;

		try {
			OperationLog log(*g_trandb);

			log.LogMessageUpdateStatus(llOperationInstanceId, OperationLogStatus_Error, "Unexpected Error", stErr);
		}
		catch( const CrException& ex ) {
			ReportError(g_log_type, ex.GetErrorString() + "\n" + stErr, EVENTLOG_ERROR_TYPE);
		}
	}
}

namespace {
	const char __SESSION_ID[] = "__SessionID";
	const char __OP_INSTANCE_ID[] = "__OperationInstanceID";
	const char __USER_ID[] = "__UserID";
}

void EngineCore::ExecuteActivity(IncomingMsg* pmsg, int activity_id )
{
	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "ExecuteActivity() ");

	konga::SmartDbPtr<PDb::ActivityTab> pPDBActivityTab( PDb::ActivityTab::MakeInstance(pmsg->GetPDB()) );
	std::string stSql = "SELECT ActivityName, ActivityMethod, ActivityLocation, HasInstance FROM ActivityTab WHERE ";
	std::string stFilter;

	std::string stSavedError = pmsg->ErrFull()?pmsg->ErrFull():"";
	std::string stActivityMethod;

	try
	{
		DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "ExecuteActivity() try block ");

		{
		DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "ExecuteActivity() DB open ");

		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
						") AND (ActivityId = " + toString(activity_id) + ")";			

		pPDBActivityTab->Select(stFilter);
		if( !pPDBActivityTab->Next() )
		{
			std::string stErr = "[Msg 22] [" + pmsg->GetRoute() + "] ExecuteActivity: Activity not found for " + stFilter;
			pPDBActivityTab->Close();
			throw CrFatalException(CR_E_FAIL, stErr);
		}
		}

		std::string stActivityName = pPDBActivityTab->IsActivityNameNull() ? "" : pPDBActivityTab->GetActivityName();
		std::string stActivityLocation = pPDBActivityTab->IsActivityLocationNull() ? "" : pPDBActivityTab->GetActivityLocation();
		stActivityMethod = pPDBActivityTab->GetActivityMethod();
		bool bHasInstance = pPDBActivityTab->GetHasInstance() != 0;
		pPDBActivityTab->Close();

		if( pmsg->IsDebugEnabled() && 
			strnicmp( stActivityMethod.c_str(), "Script", 6 ) != 0 &&
			strnicmp( stActivityMethod.c_str(), "Boolean Script", 14 ) != 0 )
		{
			DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "ExecuteActivity() DumpActivity section ");

			std::string stArgs = stActivityName.empty()?"no arguments":ReplaceDE_ExecScripts(pmsg, stActivityName);

			std::string stActivity = "About to invoke " + stActivityMethod + ": ";
			if( !stActivityLocation.empty() )
				stActivity += stActivityLocation + " with ";
			stActivity += stArgs;
			DumpActivity(pmsg, stActivity);
		}

		int aiid = -1;
		progress_status status = STATUS_IN_PROGRESS;

		if( bHasInstance ) UpdateActivityStatus(pmsg, activity_id, aiid, status);

		if( strnicmp( stActivityMethod.c_str(), "Integration", 11 ) == 0 ) {                 
			                           
			string sOperationId = pmsg->GetDataElementValue("__OperationId");
			if(sOperationId.empty() || sOperationId == "0" )
				sOperationId = pmsg->GetDataElementValue("OperationId");
			const long lOpId = atol(sOperationId.c_str());

			DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "Execute Activity: Integration with Operation_ID=" + sOperationId);

			string sSessionId, sOperationInstanceId;
			if( pmsg->IsNonProcessDataElementDefined(__SESSION_ID) ) {
				sSessionId = pmsg->GetDataElementValue(__SESSION_ID);
			}
			if( pmsg->IsNonProcessDataElementDefined(__OP_INSTANCE_ID) ) {
				sOperationInstanceId = pmsg->GetDataElementValue(__OP_INSTANCE_ID);
			}

			                                            
			string sUserGuid;
			if( pmsg->IsNonProcessDataElementDefined(__USER_ID) )
				sUserGuid = pmsg->GetDataElementValue(__USER_ID);
			long lUserId = UserInfo::UNDEFINED_UID;
			if( IsGUIDString(sUserGuid) ) {
				DbGuidToIdMapper guidMapper(g_trandb);
				lUserId = guidMapper.GetId(sUserGuid, UserEntityId);
				if ( lUserId == GuidToIdMapper::invalid_id )
					throw CrException("There is no user defined with GUID=" + sUserGuid);
			}

			if( GetOptionAsBoolean(stPROCESSENGINE_COMPONENT_NAME, "RunOperationsInSeparateProcess", true) ) {
				try {
					#ifdef WIN32
						string sSuffix = ".exe";
					#else
						string sSuffix;
					#endif
					const string& sOpEnginePath = GetOptionAsString(stPROCESSENGINE_COMPONENT_NAME, "OperationEngineFileName", GetServerHomeWithSlash() + "bin/ETLForgeOperationEngineProc" + sSuffix);
					string sCmdLine("--operation-id " + sOperationId);
					if ( !sSessionId.empty() )
						sCmdLine += (" --session-id " + sSessionId);
					if ( !sOperationInstanceId.empty() )
						sCmdLine += (" --operation-iid " + sOperationInstanceId);
					if ( lUserId != UserInfo::UNDEFINED_UID )
						sCmdLine += (" --user-id " + toString(lUserId));
					
					DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, "Executing command: ETLForgeOperationEngineProc " + sCmdLine);

					const ProcessIdT pid = RunProc(sOpEnginePath, sCmdLine);
					pmsg->SetOsProcessId(pid);
					const int iExitCode = WaitForProc(pid);
#ifdef WIN32
					                                                                                       
					                                                                                            
					if ( iExitCode == -529697949 ) {
						const string sMsg("[Msg 469] [" + pmsg->GetRoute() + "] Execution of Operation Engine ('" + sOpEnginePath +
							"') with OperationId=" + sOperationId + ", SessionId=" + sSessionId +
							", Command line: " +  sCmdLine + 
							" returned -529697949 (0xE06D7363) but the operation probably completed successfully");
						ReportError(g_log_type, sMsg, EVENTLOG_WARNING_TYPE);
					} else
#endif         
					if ( pmsg->WasKilled() ) {
						const string stErr("Operation cancelled by user.");
						try {
							OperationLog log(*g_trandb);
							log.LogMessageUpdateStatus(toLongLong(sOperationInstanceId), OperationLogStatus_Cancelled, "Operation cancelled.", stErr);
						}
						catch( const CrException& ex ) {
							ReportError(g_log_type, ex.GetErrorString() + "\n" + stErr, EVENTLOG_ERROR_TYPE);
						}
					}
					else if( iExitCode != 0 ) {
						const string sErr("[Msg 461] [" + pmsg->GetRoute() + "] Execution of Operation Engine ('" + sOpEnginePath +
							"') with OperationId=" + sOperationId + ", SessionId=" + sSessionId +
							", Command line: " +  sCmdLine + 
							" returned a non-zero exit code: " + toString(iExitCode));
						ReportError(g_log_type, sErr);
						LogFatalToOperationLog(sErr, toLongLong(sOperationInstanceId), iExitCode);
						status = STATUS_ERROR;
						pmsg->ErrSet(CR_E_FAIL, sErr);
					}
				} catch( const CrException& e) {
					std::string stErr = "[Msg 461] [" + pmsg->GetRoute() + "] Error invoking Integration Operation " +
						sOperationId + ". " + e.GetErrorString();
					status = STATUS_ERROR;
					ReportError(g_log_type, stErr);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}

			} else {
				try {
#ifdef _DEBUG
					string stInfo = "[Msg 452] [" + pmsg->GetRoute() + "] About to invoke Integration Operation " + sOperationId;
					ReportError(g_log_type, stInfo, EVENTLOG_INFORMATION_TYPE);
#endif         
					UserInfo currentUser;
					if( lUserId != UserInfo::UNDEFINED_UID ) {
						currentUser = konga::GetUserInfo(lUserId, *g_trandb, true);
					}
					OperationEngine opEngine(currentUser, 0);
					const long lSessionId = sSessionId.empty() ? -1 : atol(sSessionId.c_str());
					opEngine.RunOperation(lOpId, lSessionId, toLong(sOperationInstanceId));
#ifdef _DEBUG
					stInfo = "[Msg 453] [" + pmsg->GetRoute() + "] Invoked Integration Operation " + sOperationId;
					ReportError(g_log_type, stInfo, EVENTLOG_INFORMATION_TYPE);
#endif         
				} catch(OperationEngineException& e) {
					string stErr = "[Msg 454] [" + pmsg->GetRoute() + "] Error invoking Integration Operation " +
						sOperationId + ". The error was: " + e.GetErrorString();
					status = STATUS_ERROR;
					ReportError(g_log_type, stErr);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}
				#ifndef _DEBUG
				catch( const CrException& ex ) {
					string stErr = "[Msg 454] [" + pmsg->GetRoute() + "] Error invoking Integration Operation " +
						sOperationId + ". An unexpected CrException occurred.\n" + ex.GetErrorString();
					status = STATUS_ERROR;
					ReportError(g_log_type, stErr);
					LogFatalToOperationLog("Unexpected exception thrown by Operation Engine.\n"
						+ ex.GetErrorString(), lOpId, -1);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}
				catch( const std::exception& ex ) {
					string stErr = "[Msg 454] [" + pmsg->GetRoute() + "] Error invoking Integration Operation " +
						sOperationId + ". An unexpected std::exception occurred.\n" + string(ex.what());
					status = STATUS_ERROR;
					ReportError(g_log_type, stErr);
					LogFatalToOperationLog("Unexpected exception thrown by Operation Engine.\n"
						+ string(ex.what()), lOpId, -1);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}
				catch(...) {
					string stErr = "[Msg 454] [" + pmsg->GetRoute() + "] Error invoking Integration Operation " +
						sOperationId + ". An unknown error occurred.";
					status = STATUS_ERROR;
					ReportError(g_log_type, stErr);
					LogFatalToOperationLog("Unexpected exception thrown by Operation Engine.", lOpId, -1);
					pmsg->ErrSet(CR_E_FAIL, stErr);
					throw;
				}
				#endif          
			}
		}
		else if( strnicmp( stActivityMethod.c_str(), "Command", 7 ) == 0 ) {
			                                                                                       
			AutoDeleteFileSet adopt_stdouterr;

			string sFileName(stActivityName);
			try {
				const string sStdOutFileName = getTmpFileName("Command_" + stActivityName + "_stdout_");
				const string sStdErrFileName = getTmpFileName("Command_" + stActivityName + "_stderr_");
				const string sETLForge("ETLForge");
				if ( EqualsIgnoreCase(sETLForge, sFileName.substr(0, sETLForge.length())) ) {
					sFileName = GetServerHomeWithSlash() + "bin/" + sFileName;
				}

				RunProcFileHandles handles;
				ProcessIdT pid = RunProc(sFileName, stActivityLocation, "", sStdOutFileName, sStdErrFileName, &handles);
				const int oneHourTimeout = 1000*60*60;
				const int iExitCode = WaitForProc(pid, oneHourTimeout);
				handles.Close();

				if( FileExists(sStdOutFileName) ) {
					adopt_stdouterr.AddFile(sStdOutFileName);
				}

				string sErr;
				if( FileExists(sStdErrFileName) ) {
					read_file_to_string(sStdErrFileName, sErr);
					adopt_stdouterr.AddFile(sStdErrFileName);
				}

				if( iExitCode || !sErr.empty() )
					throw CrException(sErr);
			} catch(const CrException& e) {
				string stErr = "[Msg 470] [" + pmsg->GetRoute() + "] Error invoking the command '" +
					sFileName + "' with the arg(s) '" + stActivityLocation + "': " + e.GetErrorString();
				status = STATUS_ERROR;
				ReportError(g_log_type, stErr);
				pmsg->ErrSet(CR_E_FAIL, stErr);
			}
		}
		#ifdef DS_USE_COM
		else if( strnicmp( stActivityMethod.c_str(), "ActiveX", 7 ) == 0 ||                  
			strnicmp( stActivityMethod.c_str(), "COM", 3 ) == 0 )
		{
			string res;
			try
			{
				if( strnicmp(stActivityLocation.c_str(),"OperationEngine.Resolve",25) == 0 )
				{
					char *psVal = pmsg->GetDataElementValue("__OperationId");
					if(!psVal || !*psVal || *psVal == '0' ) psVal = pmsg->GetDataElementValue("OperationId");
					AutoArrayDelete<char> adopt_psVal(psVal);

					std::string stLogMsg1, stLogMsg2;
					if( strstr(stActivityLocation.c_str(), "ResolveOperation") )
					{
						stLogMsg1 = "About to invoke Web Data Interchange operation";
						stLogMsg2 = "Invoked Web Data Interchange operation";
					}
					else if( strstr(stActivityLocation.c_str(), "LogAuxTabStr") )
					{
						stLogMsg1 = "Getting auxiliary log message for Web Data Interchange operation";
						stLogMsg2 = "Got auxiliary log message for Web Data Interchange operation";
					}
					else if( strstr(stActivityLocation.c_str(), "LogTabStr") )
					{
						stLogMsg1 = "Getting log message for Web Data Interchange operation";
						stLogMsg2 = "Got log message for Web Data Interchange operation";
					}
					else if( strstr(stActivityLocation.c_str(), "SourceFilesNotCopied") )
					{
						stLogMsg1 = "Getting list of source files not copied by Web Data Interchange operation";
						stLogMsg2 = "Got list of source files not copied by Web Data Interchange operation";
					}
					else if( strstr(stActivityLocation.c_str(), "SourceToWorkDirCopied") )
					{
						stLogMsg1 = "Getting list of source files copied by Web Data Interchange operation";
						stLogMsg2 = "Got list of source files copied by Web Data Interchange operation";
					}
					else if( strstr(stActivityLocation.c_str(), "TargetFilesCopied") )
					{
						stLogMsg1 = "Getting list of target files copied by Web Data Interchange operation";
						stLogMsg2 = "Got list of target files copied by Web Data Interchange operation";
					}
					else
					{
						stLogMsg1 = "About to get data related to Web Data Interchange operation";
						stLogMsg2 = "Got data related to Web Data Interchange operation";
					}

					std::string stErr = "[Msg 23] [" + pmsg->GetRoute() + "] " + stLogMsg1 + " " + std::string(psVal);
					ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

					res = InvokeCOM(pmsg, stActivityLocation.c_str(), stActivityName.c_str());
					status = STATUS_COMPLETED;

					stErr = "[Msg 24] [" + pmsg->GetRoute() + "] %s %s. Result: %s", 
						pmsg->GetRoute().c_str(), stLogMsg2.c_str(), psVal, (LPCSTR)res);
					ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE); 
				}
				else
				{
					std::string stErr = "[Msg 23] [" + pmsg->GetRoute() + "] About to invoke COM object \"%s\", \"%s\"", 
						pmsg->GetRoute().c_str(), stActivityLocation.c_str(), stActivityName.c_str());
					ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

					res = InvokeComRemote(pmsg, stActivityLocation.c_str(), stActivityName.c_str());				
					status = STATUS_COMPLETED;

					stErr = "[Msg 24] [" + pmsg->GetRoute() + "] Invoked COM object \"%s\", \"%s\" result '%s'", 
						pmsg->GetRoute().c_str(), stActivityLocation.c_str(), stActivityName.c_str(), (LPCSTR)res);
					ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE); 
				}
			}
			catch(InvokeErr& e)
			{
				status = STATUS_ERROR;
				ReportError(g_log_type, e.m_err);
				pmsg->ErrSet(e.m_hr, e.m_err);
			}
		}
		#endif              
		
		#if defined(DS_USE_CORBA)
		else if( strnicmp( stActivityMethod.c_str(), "CORBA", 5 ) == 0 )         
		{
			std::string stErr = "[Msg 25] [" + pmsg->GetRoute() + "] About to invoke CORBA server \"%s\", \"%s\"", 
				pmsg->GetRoute().c_str(), stActivityLocation.c_str(), stActivityName.c_str());
			ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

			string res;
			try
			{
				res = InvokeCORBA(pmsg, stActivityLocation.c_str(), stActivityName.c_str());
			}
			catch(const char* s)
			{
				status = STATUS_ERROR;
				ReportError(g_log_type, s);
				pmsg->ErrSet(E_FAIL,s);
			}

			if( status != STATUS_ERROR )
			{
				status = STATUS_COMPLETED;
				std::string stErr = "[Msg 26] [" + pmsg->GetRoute() + "] Invoked CORBA server \"%s\", \"%s\" result '%s'", 
					pmsg->GetRoute().c_str(), stActivityLocation.c_str(), stActivityName.c_str(), (LPCSTR)res);
				ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE); 
			}

		}
		else if( strnicmp( stActivityMethod.c_str(), "EJB", 3 ) == 0 )                        
		{
			std::string de_str = ReplaceDE_ExecScripts(pmsg, stActivityName);

			std::string stErr = "[Msg 278] [" + pmsg->GetRoute() + "] About to invoke EJB server \"%s%s\", \"%s\"", pmsg->GetRoute().c_str(),
				stActivityLocation.c_str(), stActivityName.c_str(), de_str.c_str());
			ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

			string res;
			try
			{
				res = InvokeEJB(pmsg, stActivityLocation, de_str.c_str());
			}
			catch(InvokeErr& e)
			{
				status = STATUS_ERROR;
				ReportError(g_log_type, e.m_err.c_str());
				pmsg->ErrSet(e.m_hr, e.m_err.c_str());
			}
			catch(const char* s)
			{
				status = STATUS_ERROR;
				ReportError(g_log_type, s);
				pmsg->ErrSet(E_FAIL,s);
			}
			catch(string& s)
			{
				status = STATUS_ERROR;
				ReportError(g_log_type, (LPCSTR)s);
				pmsg->ErrSet(E_FAIL,(LPCSTR)s);
			}

			if( res.GetLength() > 0 )
				pmsg->UpdateDataElements((LPCTSTR)res);

			if( status != STATUS_ERROR )
			{
				status = STATUS_COMPLETED;
				std::string stErr = "[Msg 279] [" + pmsg->GetRoute() + "] Invoked EJB server \"%s%s\", \"%s\" result '%s'", pmsg->GetRoute().c_str(), 
					stActivityLocation.c_str(), stActivityName.c_str(), de_str.c_str(), (LPCSTR)res);
				ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE); 
			}
		}
		#endif                         

		#ifdef DS_USE_SOAP
		else if( strnicmp( stActivityMethod.c_str(), "SOAP", 4 ) == 0 )         
		{
			std::string stErr = "[Msg 361] [" + pmsg->GetRoute() + "] About to invoke SOAP object \"" + 
												stActivityLocation + "\", \"" + stActivityName + "\"";				
			ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE);

			std::string res;
			CSoapCaller* soapCaller = new CSoapCaller();
			try
			{
				res = soapCaller->InvokeSoapCall(pmsg, stActivityLocation, stActivityName);
			}
			catch (CSoapException se) {
				delete soapCaller; soapCaller = 0;
				status = STATUS_ERROR;
				std::string errMsg = se.GetErrorMessage();
				std::string stErr = "[Msg 362] [" + pmsg->GetRoute() + "] Invocation of SOAP object failed with the following message:\n" +
					errMsg + "\nActivity location string: " + stActivityLocation + "\nActivity DE string: " + stActivityName + "\n";					
				ReportError(g_log_type, stErr);
				pmsg->ErrSet(CR_E_FAIL, stErr);
			}
			catch(...)
			{
				delete soapCaller; soapCaller = 0;
				status = STATUS_ERROR;
				std::string stErr = "[Msg 363] [" + pmsg->GetRoute() + 
					"] An unknown error occurred while invoking SOAP object:\nActivity location string: " + 
					stActivityLocation + "\nActivity DE string: " + stActivityName + "\n";
				ReportError(g_log_type, stErr);
				pmsg->ErrSet(CR_E_FAIL, stErr);
			}

			if (soapCaller) delete soapCaller;
			if( status != STATUS_ERROR )
			{
				status = STATUS_COMPLETED;
				std::string stErr = "[Msg 364] [" + pmsg->GetRoute() + "] Invoked SOAP object \"" + stActivityLocation 
					+ "\", \"" + stActivityName + "\" result '" + res + "'"; 
				ReportError( g_log_type, stErr, EVENTLOG_DEBUG|EVENTLOG_INFORMATION_TYPE); 
			}

		}
		#endif               
		
		else if( strnicmp( stActivityMethod.c_str(), "Script", 6 ) == 0 )          
		{
			long lTimeOut = -1;
			if( !stActivityLocation.empty() )
				lTimeOut = atoi(stActivityLocation.c_str());
			if( !lTimeOut ) lTimeOut = -1;
			try {
				ExecuteScript(pmsg, stActivityName.c_str(), lTimeOut);
			} catch(const char* s)
			{
				std::string stErr = "[Msg 27] [" + pmsg->GetRoute() + "] ExecuteScript: " + std::string(s);
				ReportError( g_log_type, stErr);
				pmsg->ErrSet(CR_E_FAIL,s);
				status = STATUS_ERROR;
			}
			status = STATUS_COMPLETED;
		}
		else if( strnicmp( stActivityMethod.c_str(), "SQL", 3 ) == 0 ||
				 strnicmp( stActivityMethod.c_str(), "ODBC", 4 ) == 0 )       
		{
			std::string dsn_str = ReplaceDE_ExecScripts(pmsg, stActivityLocation);
			std::string sql_str = ReplaceDE_ExecScripts(pmsg, stActivityName);
			try { ExecuteSql(dsn_str, sql_str); }
			catch(const char*s)
			{ 
				std::string stErr = "[Msg 309] [" + pmsg->GetRoute() + "] ExecuteSql: \"" + std::string(s) + "\"";
				ReportError( g_log_type, stErr);
				pmsg->ErrSet(CR_E_FAIL,s);
				status = STATUS_ERROR;
			}
			if( status != STATUS_ERROR ) status = STATUS_COMPLETED;
		}
		else if( strnicmp( stActivityMethod.c_str(), "Internal", 8 ) == 0 )            
		{
			if( strnicmp( stActivityName.c_str(), "TerminateProcess", 16) == 0 ||
				strnicmp( stActivityName.c_str(), "EndProcess", 10) == 0 )
			{
				UpdateTaskStatus( pmsg, STATUS_COMPLETED );
				UpdateProcessStatus( pmsg, STATUS_COMPLETED );
				status = STATUS_COMPLETED;
				if( bHasInstance ) UpdateActivityStatus(pmsg, activity_id, aiid, status);
				string stErr = "[Msg 455] Process terminated through a call to " + stActivityName + ".";
				ReportError( g_log_type, stErr, EVENTLOG_INFORMATION_TYPE);
				throw InstanceTerminated();
			}
			else if( strnicmp( stActivityName.c_str(), "TerminateTask", 13) == 0 ||
				strnicmp( stActivityName.c_str(), "EndTask", 7) == 0 )
			{
				UpdateTaskStatus( pmsg, STATUS_COMPLETED );
				status = STATUS_COMPLETED;
				if( bHasInstance ) UpdateActivityStatus(pmsg, activity_id, aiid, status);
				string stErr = "[Msg 456] Task terminated through a call to " + stActivityName + ".";
				ReportError( g_log_type, stErr, EVENTLOG_INFORMATION_TYPE);
				throw InstanceTerminated();
			}
			else if( strnicmp( stActivityName.c_str(), "RaiseError", 10 ) == 0 )
			{
				int iErrorLevel = atoi(stActivityName.substr(11).c_str());
				if( stActivityName[ stActivityName.length() - 1 ] == ')' )	                                    
					stActivityName[ stActivityName.length() - 1 ] = 0;		                                               
				                                                                                                                 
				std::string stError = "[Msg 405] [" + pmsg->GetRoute() + "] The following error was raised: \n";
				if( stActivityName[13] == '"' )
					stError += stActivityName.substr(13);
				else
					stError += pmsg->GetDataElement(stActivityName.substr(13).c_str()).m_value;

				ReportError(g_log_type, stError);

				if( iErrorLevel > 0 )
					UpdateTaskStatus( pmsg, STATUS_COMPLETED | STATUS_ERROR );

				if( iErrorLevel == 3 )                           
				{
					m_viTasksToReRun.push_back( pmsg->m_MsgId );
					UpdateTaskStatus( pmsg, STATUS_RERUN );
					pmsg->StayInQueue(true);                                                            
				}

				status = STATUS_COMPLETED;
				if( bHasInstance ) UpdateActivityStatus(pmsg, activity_id, aiid, status);

				if( iErrorLevel > 0 )
					throw InstanceTerminated();
			}
			else if( strnicmp( stActivityName.c_str(), "ReportDataElements", 18 ) == 0 )
			{
				char *str = strnewdup(&(stActivityName.c_str())[19]);
				char *str_copy = str, *de=0;
				get_token(str, de, ")");
				ReportDataElements(pmsg,de);
				delete []de; delete []str_copy;
				status = STATUS_COMPLETED;
			}
			else if( strnicmp( stActivityName.c_str(), "BlockEscalatedTask", 18 ) == 0 ||
					 strnicmp( stActivityName.c_str(), "BlockEscalationTask", 19 ) == 0 )
			{
				progress_status task_stat = GetTaskStatus(pmsg, pmsg->m_esc_pid, pmsg->m_esc_piid, pmsg->m_esc_tid, pmsg->m_esc_tiid);
				if( (task_stat == (STATUS_INITIALIZED|STATUS_IN_PROGRESS)) || task_stat == STATUS_UNDEFINED )
					status = STATUS_ERROR;
				else
				{
					UpdateTaskStatus(pmsg, pmsg->m_esc_pid, pmsg->m_esc_piid, pmsg->m_esc_tid, pmsg->m_esc_tiid, STATUS_BLOCKED);
					status = STATUS_COMPLETED;
				}
			}
			else if( strnicmp( stActivityName.c_str(), "BlockTaskInstance(",18  ) == 0 )
			{				
				char* route = strnewdup( ReplaceDE_ExecScripts(pmsg, stActivityName.substr(18)).c_str() );
                               
				AutoPtrDelete<char> adopt_route(route);
				int pid, piid, tid, tiid;
				std::string proc_name, task_name;
				if( parse_route_string(route, pid, piid, tid, tiid, proc_name, task_name) <= 0 )
					status = STATUS_ERROR;
				else if( (GetTaskStatus(pmsg, pid, piid, tid, tiid) & STATUS_IN_PROGRESS) == STATUS_IN_PROGRESS )
					status = STATUS_ERROR;
				else
				{
					UpdateTaskStatus(pmsg, pid, piid, tid, tiid, STATUS_BLOCKED);
					status = STATUS_COMPLETED;
				}
			}			
			else if( strnicmp( stActivityName.c_str(), "CreateScheduledEvent", 20 ) == 0 )                                       
			{
				if( stActivityName.length() <= 21 ) status = STATUS_ERROR;         
				else
				{
					                                  
					char *token=new char[128], *str = strnewdup( ReplaceDE_ExecScripts(pmsg, stActivityName.substr(21)).c_str() );
					AutoArrayDelete<char> adopt_token(token), adopt_str(str);
					char *de_tiid=0;
					int tid,when,overwrite;
					if( !get_token(str, token, ",;.") || !(tid=atoi(token)) ||
						!get_token(str, token, ",;.") || ((when=atoi(token))<1) ||
						!get_token(str, de_tiid, ",;.") ||
						!get_token(str, token, ",;.") )
					{
						status = STATUS_ERROR;         
					}
					else
					{
						overwrite=atoi(token);
						int tiid = CreateTaskInstance(pmsg,pmsg->m_pid,pmsg->m_piid,tid,overwrite && tid == pmsg->m_tid);
						PrepareRouteToTask(pmsg,tid,tiid);
						IncomingMsg *nextTask = ExecutePreRoleActivities(pmsg, tid, tiid);
						AutoPtrDelete<IncomingMsg> adopt_nextTask(nextTask);
						CreateScheduledEvent(pmsg,tid,tiid,60*when);                                                                                      

						char stiid[20];
						sprintf(stiid,"%d",tiid);
						pmsg->SetDataElementValue(de_tiid,stiid);
						status = STATUS_COMPLETED;
					}
				}
			}
			else if( strnicmp( stActivityName.c_str(), "CancelScheduledEvent", 20 ) == 0 )
			{
				if( stActivityName.length() <= 21 )
					status = STATUS_ERROR;         
				else
				{
					char *token=new char[128], *str = strnewdup( ReplaceDE_ExecScripts(pmsg, stActivityName.substr(21)).c_str() );
					AutoArrayDelete<char> adopt_token(token), adopt_str(str);
					int pid,piid,tid,tiid;
					if( !get_token(str, token, ",;.") || !(pid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(piid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(tid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(tiid=atoi(token)) )
					{
						status = STATUS_ERROR;         
					}
					else
					{
						CancelScheduledEvent(pmsg,pid,piid,tid,tiid);
						status = STATUS_COMPLETED;
					}
				}
			}
			else if( strnicmp( stActivityName.c_str(), "ResetLastError", 14 ) == 0 )
			{
				pmsg->ErrSet(0,"");
				status = STATUS_COMPLETED;
			}
			else if( strnicmp( stActivityName.c_str(), "LDAPSearch", 10 ) == 0 )
			{
			#ifdef DS_USE_LDAP
				status = STATUS_ERROR;                                                
				if( stActivityName.length() > 10 )
				{
					char *baseStr=0, *searchStr=0, *server=0, *portStr=0, *outAttrStr=0, *de=0;
					char *user=0, *passwd=0;
					char *str = strnewdup( ReplaceDE_ExecScripts(pmsg, stActivityName.substr(11)).c_str() );
					char *str_copy = str;

					if( get_token(str, server, ",") &&
						get_token(str, portStr, ",") &&
						get_token(str, baseStr, ",") &&
						get_token(str, searchStr, ",") &&
						get_token(str, outAttrStr, ",") &&
						get_token(str, de, ",") &&		   
						get_token(str, user, ",") &&
						get_token(str, passwd, ")")	)
                                 
					{
						if( strcmp(user,"(null)") == 0 ) *user=0;
						if( strcmp(passwd,"(null)") == 0 ) *passwd=0;
						if( strcmp(baseStr,"(null)") == 0 ) *baseStr=0;
						int i=1, port=atoi(portStr);
						CLDAPWrapper ldap;
						string res;
						if( ldap.GetSearchResults(server, port, baseStr, searchStr, outAttrStr, res, user, passwd) )
						{
							pmsg->SetDataElementValue(de,(LPCTSTR)res);
							status = STATUS_COMPLETED;
						}
						else
						{
							std::string err=(LPCTSTR)ldap.GetLDAPError();
							std::string stErr = "[Msg 288] [" + pmsg->GetRoute() + "] ExecuteActivity: \"%s\" failed. Error is: %s",
								pmsg->GetRoute().c_str(), stActivityName.c_str(), err.length()?err.c_str():"unknown error");
							ReportError( g_log_type, stErr);
							pmsg->ErrSet(CR_E_FAIL, stErr);
						}
					}
					delete[] baseStr;
					delete[] searchStr;
					delete[] server;
					delete[] portStr;
					delete[] outAttrStr;
					delete[] de;
					delete[] user;
					delete[] passwd;
					delete[] str_copy;
				}
				else
				{
					std::string stErr = "[Msg 292] [" + pmsg->GetRoute() + "] ExecuteActivity: \"" + 
												stActivityName + "\" failed. Syntax error.";
					ReportError( g_log_type, stErr);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}
			#endif               
			}
			else if( strnicmp( stActivityName.c_str(), "FileXfer", 8 ) == 0 )
			{
				status = STATUS_ERROR;                                                
				if( stActivityName.length() > 9 )
				{
					char *Url = 0, *FileOrString = 0, *FMd = 0;
					char *RespMd = 0,  *RespName = 0;
					char *UserName = 0, *PassWd = 0, *CertName = 0;
					char *TimeOut = 0;
 
					char *str = strnewdup( ReplaceDE_ExecScripts(pmsg, stActivityName.substr(9)).c_str() );
					AutoArrayDelete<char> adopted_str_copy(str);

					  
					                                                            
					  
					if( *str == '\"' )
					{
						++str;
						char *pc = strchr(str,'\"');
						if( pc )
						{
							*pc = 0;
							Url = strnewdup(str);
							str = pc + 2;
						}
						else
							Url = strnewdup(str);
					}
					else 
						get_token(str, Url, ",");                                                                                

					int Result;
					std::string Str;
					if( get_token(str, FileOrString, ",") &&
						get_token(str, FMd, ",") &&
						get_token(str, RespMd, ",") &&
						get_token(str, RespName, ",") &&
						get_token(str, UserName, ",") &&
						get_token(str, PassWd, ",") &&
						get_token(str, CertName, ",") &&
						get_token(str, TimeOut, ")") )
					{
						int fileMode;
						FileRetriever::FileMode mode;
						
						if( (fileMode = atoi(FMd)) ) {
							mode = FileRetriever::FileMode(fileMode);

							int responseMode;
							FileRetriever::ResponseMode rMode;
							
							responseMode = atoi(RespMd);
							rMode = FileRetriever::ResponseMode(responseMode);

							FileRetriever fr;

							if(UserName && strnicmp(UserName,"(null)",6)!=0 ) fr.SetUserName(UserName);
							if(PassWd && strnicmp(PassWd,"(null)",6)!=0 ) fr.SetPassWord(PassWd);

							  
							                                                                               
							                                                                    
							  
							if(CertName && strnicmp(CertName,"(null)",6)!=0 )
							{
								CrSecurity::CertInfo certInfo;
								certInfo.CertificateName(CertName);
								fr.SetCertificateInfo(certInfo);
							}

							                                                              
							                                              
                                                              
                                                                  
                                                                  

							FileTransferInfo fti(&fr, fileMode, mode, responseMode, rMode, pmsg, Url,
								FileOrString, RespName, Result, Str);

							DoFileTransfer((void*)&fti);

							                                                                                             
                               
                    
        
                                      
                                                
        
                                                                            
                         
        
                                              
                                    
                                                                                                                          
                                                         
                                        
                                       
        

                                                                
        
                          
         
                                    
                                                                                                                                        
                                                                              
                                                                                      
                                        
                                       
                                                                                     
              
         
                        
         
                                    
                                                                                                                              
                                    
                                                                               
                                                                                      
                                        
                                       
                                                                                     
              
         
                                                                               
         
        
         
						
							if(Result != Protocol::SUCCESS){
								std::string err = fr.LastError();
								std::string stErr = "[Msg 290] [" + pmsg->GetRoute() + "] ExecuteActivity: " +
									std::string(mode<FileRetriever::Put_ASCII_File?"get":"put") + " \"" + Url + "\" failed. Error is: \"" + err 
									+ "\" " + std::string(fr.LastErrorNumber()?ErrorResolver().Resolve(fr.LastErrorNumber()):"");
								ReportError( g_log_type, stErr);
								pmsg->ErrSet(CR_E_FAIL, stErr);
							}
							else{
								status = STATUS_COMPLETED;
							}
						} 
						else
						{
							                                      
							std::string stErr = "[Msg 293] [" + pmsg->GetRoute() + "] ExecuteActivity: \"" + stActivityName 
								+ "\" failed. Error is: Wrong Mode for FileXfer. Mode Given was " + toString(fileMode) +
								". Accepted Modes are:\n1 - Getting an ASCII file\n2 - Getting an ASCII string\n3 - Getting a UNICODE file\n4 - Getting a UNICODE string\n5 - Putting an ASCII file\n6 - Putting an ASCII string\n7 - Putting a UNICODE file\n8 - Putting a UNICODE string";
							ReportError( g_log_type, stErr);
							pmsg->ErrSet(CR_E_FAIL, stErr);
						}
					}
					else
					{
						std::string stErr = "[Msg 341] [" + pmsg->GetRoute() + "] ExecuteActivity: \"" + Url +
							 "\" failed. Wrong number of arguments. Expecting 9.";
						ReportError( g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
					}
					if (Url) delete[] Url;
					if (FileOrString) delete[] FileOrString;
					if (FMd) delete[] FMd;
					if (RespMd) delete[] RespMd;
					if (RespName) delete[] RespName;
					if (UserName) delete[] UserName;
					if (PassWd) delete[] PassWd;
					if (CertName) delete[] CertName;
					if (TimeOut) delete [] TimeOut;
				}
				else
				{
					std::string stErr = "[Msg 291] [" + pmsg->GetRoute() + "] ExecuteActivity: \"" + stActivityName +
						 "\" failed. Syntax error.";
					ReportError( g_log_type, stErr);
					pmsg->ErrSet(CR_E_FAIL, stErr);
				}
			}
			else
			{
				std::string stErr = "[Msg 28] [" + pmsg->GetRoute() + "] ExecuteActivity: unrecognized internal function \"" +
					stActivityName + "\" for " + stFilter;
				ReportError( g_log_type, stErr);
				status = STATUS_ERROR;
			}
		}
		else
		{
			std::string stErr = "[Msg 29] [" + pmsg->GetRoute() + "] ExecuteActivity: unknown method \"" +
				stActivityMethod + "\" for " + stFilter;
			ReportError( g_log_type, stErr);
			status = STATUS_ERROR;
		}

		if( bHasInstance ) UpdateActivityStatus(pmsg, activity_id, aiid, status); 

	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 30] [" + pmsg->GetRoute() + "] Database error: ExecuteActivity II -- (PDBActivityTab) " + 
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pPDBActivityTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	if( pmsg->IsDebugEnabled() && 
		strnicmp( stActivityMethod.c_str(), "Script", 6 ) != 0 &&
		strnicmp( stActivityMethod.c_str(), "Boolean Script", 14 ) != 0 )
	{
		std::string stActivity = "Invoked " + stActivityMethod + ". ";
		if( !stSavedError.empty() &&  stSavedError != pmsg->ErrFull())
		{
			stActivity += "An error occurred: ";
			stActivity += pmsg->ErrFull();
		}
		DumpActivity(pmsg, stActivity);
	}
}

bool EngineCore::ExecuteBooleanActivity(IncomingMsg* pmsg, int activity_id )
{
	konga::SmartDbPtr<PDb::ActivityTab> pPDBActivityTab( PDb::ActivityTab::MakeInstance(pmsg->GetPDB()) );
	std::string stFilter;
	bool b_res = false;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			 ") AND (ActivityId = " + toString(activity_id) + ")";

		pPDBActivityTab->Select(stFilter);
		if( !pPDBActivityTab->Next() )
		{
			std::string stErr = "[Msg 31] [" + pmsg->GetRoute() + "] ExecuteBooleanActivity: Activity not found for " + stFilter; 
			OnError(-1);
			pPDBActivityTab->Close();
			throw CrFatalException(CR_E_FAIL, stErr);
		}

		std::string ActivityName = ReplaceDE_ExecScripts(pmsg, pPDBActivityTab->GetActivityName());

		progress_status status = STATUS_IN_PROGRESS;
		int aiid = -1;                                        

		if( pPDBActivityTab->GetHasInstance() )
		{
			                                  
			UpdateActivityStatus(pmsg, activity_id, aiid, status); 
		}

		if( strnicmp( pPDBActivityTab->GetActivityMethod().c_str(), "Internal", 8 ) == 0 )            
		{
			if( strnicmp( ActivityName.c_str(), "CancelScheduledEvent", 20 ) == 0 )
			{
				if( ActivityName.length() <= 21 )
				{
					status = STATUS_ERROR;         
					b_res = false;
				}
				else
				{
					char *token=new char[128], *str = strnewdup(&(ActivityName.c_str())[21]);
					char *str_copy = str;
					int pid,piid,tid,tiid;
					if( !get_token(str, token, ",;.") || !(pid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(piid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(tid=atoi(token)) ||
						!get_token(str, token, ",;.") || !(tiid=atoi(token)) )
					{
						delete[] token;
						delete[] str_copy;
						status = STATUS_ERROR;         
						b_res = false;
					}
					else
					{
						delete[] token;
						delete[] str_copy;
						b_res = CancelScheduledEvent(pmsg,pid,piid,tid,tiid);
					}
				}
			}
			else if( strnicmp( ActivityName.c_str(), "IsTaskInProgress",16  ) == 0 )
			{
				char *token=new char[128];
				char *str = strnewdup(&(ActivityName.c_str())[17]);
				boost::scoped_array<char> adopt_token(token), adopt_str(str);
				int pid,piid,tid,tiid;
				if( !get_token(str, token, ",;.") || !(pid=atoi(token)) ||
					!get_token(str, token, ",;.") || !(piid=atoi(token)) ||
					!get_token(str, token, ",;.") || !(tid=atoi(token)) ||
					!get_token(str, token, ",;.") || !(tiid=atoi(token)) )
				{
					status = STATUS_ERROR;         
					b_res = false;
				}
				else 
				{
					b_res = ( (GetTaskStatus(pmsg, pid, piid, tid, tiid) & STATUS_IN_PROGRESS) == STATUS_IN_PROGRESS );
					if( !pPDBActivityTab->IsActivityLocationNull() && pPDBActivityTab->GetActivityLocation().length() > 0 )
					{
						std::string de = pPDBActivityTab->GetActivityLocation();
						de += b_res?"=1;":"=0;";
						pmsg->UpdateDataElements(de.c_str());
					}
				}
			}
			else
			{
				std::string stErr = "[Msg 32] [" + pmsg->GetRoute() + "] ExecuteBooleanActivity() unrecognized internal function \"" +
					ActivityName + "\" for " + stFilter;
				ReportError( g_log_type, stErr);
				OnError(-1);
				status = STATUS_ERROR;
			}
		}
		else if( strnicmp( pPDBActivityTab->GetActivityMethod().c_str(), "Script", 6 ) == 0 ||
			strnicmp( pPDBActivityTab->GetActivityMethod().c_str(), "Boolean Script", 14 ) == 0 )
		{
			b_res = ExecuteBooleanScript(pmsg, ActivityName.c_str());
		}
		else
		{
			std::string stErr = "[Msg 33] [" + pmsg->GetRoute() + "] ExecuteBooleanActivity() unknown method \"" +
				pPDBActivityTab->GetActivityMethod() + "\" for " + stFilter;
			ReportError( g_log_type, stErr);
			OnError(-1);
			status = STATUS_ERROR;
			b_res = false;
		}

		if( pPDBActivityTab->GetHasInstance() && status != STATUS_IN_PROGRESS)
		{
			                                    
			UpdateActivityStatus(pmsg, activity_id, aiid, status);
		}

		pPDBActivityTab->Close();
	} 
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 34] [" + pmsg->GetRoute() + "] Database error: ExecuteBooleanActivity() II -- (PDBActivityTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pPDBActivityTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	return b_res;
}

  
                                                                                   
                                                                                  
            
                                                                                       
                                                        
  
int EngineCore::CreateScheduledEvent(IncomingMsg* pmsg, int tid, int tiid, int when)
{

	time_t now;
	time(&now);

	int document_id = GetDestinationDocumentId(pmsg, tid);

	std::string proc_vars = "{";
	proc_vars += toString(pmsg->m_pid);
	proc_vars += ';';
	proc_vars += toString(pmsg->m_piid);
	proc_vars += ';';
	proc_vars += toString(tid);
	proc_vars += ';';
	proc_vars += toString(tiid);
	std::string de_list = GetDataElementsForRoute(pmsg, tid, tiid, document_id, ';');
	if( de_list.length() > 0  )
	{
		proc_vars += de_list.c_str();
		proc_vars += '}';
	}
	else proc_vars += ";}";

	std::string from_proc_vars;
	from_proc_vars += toString(pmsg->m_pid);
	from_proc_vars += ';';
	from_proc_vars += toString(pmsg->m_piid);
	from_proc_vars += ';';
	from_proc_vars += toString(pmsg->m_tid);
	from_proc_vars += ';';
	from_proc_vars += toString(pmsg->m_tiid);
	from_proc_vars += ';';

	time_t t=0;
	try 
	{
                                             
                           
             
                                
                                      
		konga::SmartDbPtr<MsgDb::EventMsgTab> pEventMsgTab( MsgDb::EventMsgTab::MakeInstance(pmsg->GetMDB()));
		pEventMsgTab->AddNew();
		pEventMsgTab->SetMsgFrom(from_proc_vars);
		pEventMsgTab->SetMsgTo(GetDestinationRoleString(pmsg, tid));
		pEventMsgTab->SetMsgSubject("Event");
		pEventMsgTab->SetProcessVariables(proc_vars);
		pEventMsgTab->SetScheduledAt(now + when);
		pEventMsgTab->SetReceivedAt(time(&t));
		pEventMsgTab->SetProcessId(pmsg->m_pid);
		pEventMsgTab->SetProcessInstanceId(pmsg->m_piid);
		pEventMsgTab->SetTaskId(tid);
		pEventMsgTab->SetTaskInstanceId(tiid);
                               
		pEventMsgTab->Update();
                          
	}
	catch( const konga::DbException& e )
	{
		std::string stErr = "[Msg 38] [" + pmsg->GetRoute() + "] Database error: CreateScheduledEvent() " + 
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
                          
		throw CrFatalException(CR_E_FAIL, stErr);
	}
	return tiid;
}

bool EngineCore::CancelScheduledEvent(IncomingMsg* pmsg,int pid, int piid, int tid, int tiid)
{
	konga::SmartDbPtr<MsgDb::EventMsgTab> pEventMsgTab( MsgDb::EventMsgTab::MakeInstance(pmsg->GetMDB()));
	std::string stFilter = BuildRouteWhereClause(pid, piid, tid, tiid);

	try 
	{
		pEventMsgTab->Select("WHERE " + stFilter);
		if( !pEventMsgTab->Next() )
		{
			pEventMsgTab->Close();
			return false;
		}
		pEventMsgTab->Delete();
		pEventMsgTab->Close();
		UpdateTaskStatus(pmsg, pid, piid, tid, tiid, STATUS_CANCELLED);
		return true;
	}
	catch(konga::DbException& e )
	{
		std::string stErr = "[Msg 39] [" + pmsg->GetRoute() + "] Database error: CancelScheduledEvent() for " +
			stFilter + " -- " + e.GetErrorString() + "  [" + e.GetStateNativeOrigin() + "], " + 
			g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	pEventMsgTab->Close();
	return false;
}

int EngineCore::CreateTaskInstance(IncomingMsg* pmsg, int pid, int piid, int tid, bool overwrite          ) const
{
	konga::SmartDbPtr<IDb::TaskTab> pTaskTab( IDb::TaskTab::MakeInstance(pmsg->GetIDB()));
	std::string stFilter;
	int tiid = -1;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pid) + ") AND (ProcessInstanceId = " + toString(piid) +
			 ") AND (TaskId = " + toString(tid) + ") ORDER BY TaskInstanceId DESC";
		pTaskTab->Select(stFilter);

		time_t now;
		time(&now);

                                                                                    
                                   
                                                                
                

                         
   
                    
                                   
                               
                                          
                      
                     
   
      
   
          
  
			SmartDbPtr<IDb::SpInsertTaskTab> pSpInsertTaskTab( IDb::SpInsertTaskTab::MakeInstance(pmsg->GetIDB()));

			pSpInsertTaskTab->SetProcessId(pid);
			pSpInsertTaskTab->SetTaskId(tid);
			pSpInsertTaskTab->SetProcessInstanceId(piid);
			pSpInsertTaskTab->SetTaskUser("unknown");
			pSpInsertTaskTab->SetInitTime(now);			                                                                   
			                                                                            
			pSpInsertTaskTab->SetStatus(STATUS_INITIALIZED);
                                                                                                     

			try 
			{
				pSpInsertTaskTab->Execute();
			}
			catch(konga::DbException& e)
			{
				pSpInsertTaskTab->Close();
				std::string stErr = "[Msg 297] [" + pmsg->GetRoute() + "] Database error: CreateTaskInstance -- (IDB sp_insert_TaskTab) " +
					e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString(); 
				throw CrFatalException(CR_E_FAIL, stErr);
			}
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 40] [" + pmsg->GetRoute() + "] Database error: CreateTaskInstance() -- (IDBTaskTab) for " + stFilter + "-- " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_idb_connect.VerbosePrettyConnectInfoString();
		pTaskTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	return tiid;
}

