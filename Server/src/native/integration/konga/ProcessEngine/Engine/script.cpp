/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <stack>			             
#include <string>			              
#include <string.h>

#ifdef WIN32
	#include <windows.h>
	#ifdef min
		#undef min
	#endif
	#ifdef max
		#undef max
	#endif
	#include <comutil.h>	                
#endif

#include <sqlext.h>		                                  

#include <konga/Utils/AutoPtrCleanup.h>	                  
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/date.h>			               
#include <konga/Utils/CrException.h>	                         

#include <konga/Db/PDb/TaskDataElementsTab.h>	                                     

#include <konga/Db/IDb/ProcessTab.h>			                            
#include <konga/Db/IDb/DataElementTab.h>		                                
#include <konga/Db/IDb/DataElementTextTab.h>	                                         

#include "engine_util.h"	                                                       
#include "EngineCore.h"		                   
#include "EngineDebugUtils.h"		                       

#ifdef WIN32
	#include <konga/CrNet/CrNet.h>	                  
#endif

extern EngineCore* g_engine;

using namespace std;
using namespace engine_util;

#define CR_USE_IDB_DATAELEMENTTAB

  
                                                                        
                                                          
  
string ReplaceDE_ExecScripts(IncomingMsg* pmsg, const string& str, const string& stDbConnect       )
{
	string res = ReplaceDataElementWithValue(pmsg, str, stDbConnect);
	return res;
}

  
                                                                          
                                                          
  
string ReplaceDataElementWithValue(IncomingMsg* pmsg, const string& str, const string& stDbConnect       )
{
	string res;
	string::const_iterator kit;
	for(string::const_iterator it = str.begin(); it < str.end() ; ++it)
	{
		if( *it == '[' )
		{
			if( (kit=find(it,str.end(),']')) != str.end() )
			{
				TaskDataElement tde = pmsg->GetDataElement(std::string(it+1, kit));
				if( tde.IsUndefined() )
					res += '[';
				else
				{
					res += tde.m_value;
					it = kit;
				}
			}
		}
		else res += *it;
	}
	return res;
}

std::string ReplaceDataElementWithValueAddEscapes(IncomingMsg* pmsg, const char* str, const std::string& stEscChars)
{
	int k;
	char de_name[256];
	std::string res;
	for(; *str ; ++str)
	{
		if( *str == '[' )
		{
			if( (k=findk(str, ']')) && k<256)
			{
				strncpy(de_name, str+1, k-1);
				de_name[k-1] = 0;
				TaskDataElement tde = pmsg->GetDataElement(de_name);
				if( tde.IsUndefined() )
					res += '[';
				else
				{
					std::string stWithEscapes = tde.m_value;
					AddEscapeSequences(stWithEscapes, stEscChars);
					res += stWithEscapes;
					str += k;
				}
			}
		}
		else res += *str;
	}
	return res;
}

string ReplaceDataElementWithValueDB(IncomingMsg* pmsg, const string& str, const string& stDbConnect,
									 std::vector<CColValType>& paramValues)
{
	string::const_iterator kit;
	string res;
	for(string::const_iterator it = str.begin(); it < str.end() ; ++it)
	{
		if( *it == '[' )
		{
			if( (kit=find(it,str.end(),']')) != str.end() )
			{
				std::string stDeName = std::string(it+1, kit);
				if( stDeName.substr(0,10) == "CRDB_TEXT " )
				{
					stDeName.erase(0,10);
					res += '?';
					it = kit;
					TaskDataElement tde = pmsg->GetDataElement(stDeName.c_str());
					paramValues.push_back(CColValType(tde.m_value.c_str(),SQL_LONGVARCHAR));
					continue;
				}

				TaskDataElement tde = pmsg->GetDataElement(stDeName);
				if( tde.IsUndefined() )
					res += '[';
				else
				{
					switch( tde.m_type )
					{
					case t_date:
						{
						string err;
						CrDateTime date = GetDate(tde.m_value.c_str(), err);
						if( err.length() > 0 )
							ReportError(g_log_type, "[Msg 450] [" + pmsg->GetRoute() + "] Warning data conversion reported an error: " + err, EVENTLOG_WARNING_TYPE);

						  
						                                                                 
						                                                                 
						                                                               
						             
						             
						                  
						                                     
						char cDate = *(kit + 1);
						int iDate;
						switch(cDate)
						{
						case '\'': iDate = SQL_DATE; break;                                             
						case '1': iDate = SQL_DATE; break;
						case '2': iDate = SQL_TIME; break;
						case '3': iDate = SQL_TIMESTAMP; break;
						default: iDate = SQL_DATE;           
						}
						string stErr;
						                                
                                                                        
						++kit;                                     
						break;
						}
					case t_string:
						{
						std::string stValue;
						stValue.swap(tde.m_value);
						AddEscapeSequences(stValue,"'",'\'');
						res += stValue;
						}
						break;
					default:
						res += tde.m_value;
					}
					it = kit;
				}
			}
		}
		else res += *it;
	}
	return res;
}

static bool IsIntegerString(const char*s){
	char c;
	for(int i=0;(c=s[i])!=0&&c!=';';i++){
		if(isdigit(c)==false)return false;
	}
	return true;
}

  
                                                                        
                                                                        
                     
  
TaskDataElement ExecuteScript(IncomingMsg* pmsg, const char* script, long lTimeOut              )
{
	if( !script || !*script )
		return TaskDataElement("0");

	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, std::string("ExecuteScript() ") + script);

	char c, *s = strnewdup( script );
	AutoArrayDelete<char> adopt_s(s);
	TaskDataElement f, f1;
	int i , n = static_cast<int>(strlen(s)), k;
	stack<TaskDataElement> st;

	for(i=0;i<n;i=k+1){
		c=s[i];
		k=i+findk(&s[i],';');

		if(c=='\"'){                                                                     
			int k1 = i+findk(&s[i+1],'\"');
			if( k1>k ) k=k1+2;                                                              
		}

		if( strnicmp(&s[i],"SQL:",4) !=0 ) {                               
			if (strnicmp(&s[i], "SQLSP:", 6) != 0)                               
				s[k]=0;
		}
		if((k-i)>1||c=='\"'){                                                                
			if((k-i)==2&&strcmp(&s[i],"==")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f==f1));
				}
			} else if((k-i)==2&&strcmp(&s[i],"!=")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f!=f1));
				}
			} else if((k-i)==2&&strcmp(&s[i],">=")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f>=f1));
				}
			} else if((k-i)==2&&strcmp(&s[i],"<=")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f<=f1));
				}
			} else if((k-i)==2&&strcmp(&s[i],"&&")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f&&f1));
				}
			} else if((k-i)==2&&strcmp(&s[i],"||")==0){
				if(st.size()>0){
					f1=st.top(); st.pop();
					f=st.top(); st.pop();
					st.push(TaskDataElement(f||f1));
				}
			} else if(isdigit(c)||c=='.'||c=='-'||c=='+')                      
			{
				              
				                                                                         
				if(IsIntegerString(&s[i])){
					long l;
					sscanf(&s[i],"%ld",&l);
					st.push(TaskDataElement(l));
				} else {
					double d;
					sscanf(&s[i],"%lg",&d);
					st.push(TaskDataElement(d));
				}
			}
			else if( isalpha(c) || c == '_' )                                                          
			{
				  
				                           
				  
				if( !strnicmp(&s[i],"SQLSP",5) )                                                                                                         
				{
				#ifdef DS_USE_SP
					std::vector<CColValType> paramValues;
					std::string stS = ReplaceDataElementWithValueDB(pmsg, std::string(&s[i]), std::string(&s[i]), paramValues);
					                                                                     
					if (::IsUsingCRSystemDB(stS)) {
						std::string stErr = "[Msg 365] [" + pmsg->GetRoute() + "] Access denied to read/modify ETLForge system databases.";
						ReportError(g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
						return TaskDataElement("0");
					}

					std::string res;
					try {
						res = SPExecute(pmsg, stS.c_str(), lTimeOut);
					} catch(const char*err) {
						std::string stErr = "[Msg 299] [" + pmsg->GetRoute() + "] Error accessing database in script. Error is: ' " +
							string(err) + "'\nThis script will return false, if there shall be a return value.";
						ReportError( g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
						delete[] err;
						return TaskDataElement("0");
					}
					return TaskDataElement(res.c_str());
				#endif             
				} 
				else if( !strnicmp(&s[i],"SQL:",4) )                                                                                                                             
				{
					std::vector<CColValType> paramValues;
					std::string stS = ReplaceDataElementWithValueDB(pmsg, std::string(&s[i]), std::string(&s[i]), paramValues);
					if (::IsUsingCRSystemDB(stS)) {
						std::string stErr = "[Msg 366] [" + pmsg->GetRoute() + "] Access denied to read/modify ETLForge system databases.";
						ReportError(g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
						return TaskDataElement("0");
					}
					char* ps = strnewdup(stS.c_str());
					AutoArrayDelete<char> adopt_ps(ps);

					int kk = findk(&ps[4],':');                                             
					ps[ 4+kk ] = 0;
					k=findk(&ps[4+kk+1],';');                                                                                                       
					ps[k+4+kk+1]=0;
					string dsn=&ps[4];                                                            
					string stUncUserAndPass = &ps[k+4+kk+1+1];
					if( stUncUserAndPass.length() > 4 )                                                          
					{
						size_t iDomainStartPos = stUncUserAndPass.find(':',5);
						size_t iPassStartPos = stUncUserAndPass.find(':',iDomainStartPos+1);
						size_t iUserStartPos = stUncUserAndPass.find('\\');
						std::string stComputer = stUncUserAndPass.substr(4,iDomainStartPos-4);
						std::string stDomain = stUncUserAndPass.substr(iDomainStartPos+1,iUserStartPos-iDomainStartPos-1);
						std::string stUser = stUncUserAndPass.substr(iUserStartPos+1,iPassStartPos-iUserStartPos-1);
						std::string stPassword = stUncUserAndPass.substr(iPassStartPos+1);
						                                                          
						#ifdef WIN32
							long err = CreateSession(_bstr_t(stComputer.c_str()), _bstr_t(stDomain.c_str()), _bstr_t(stUser.c_str()), _bstr_t(stPassword.c_str()));
							if( err )
							{
								std::string stErr = "[Msg 396] [" + pmsg->GetRoute() + "] Error accessing file-based database in script. "
									"Could not log on to remote computer '" + stComputer + "' as user '" + stDomain + "\\stUser'. Error was: " +
									ErrorResolver().Resolve(err) + "\nThis script will return false, if there shall be a return value.";
								ReportError( g_log_type, stErr);
								pmsg->ErrSet(CR_E_FAIL, stErr);
								return TaskDataElement("0");
							}
						#endif         
					}
					std::vector<std::string> de_vec;
					std::string res;
					try {
						res = DBExecute(pmsg, dsn.c_str(), &ps[4+kk+1], de_vec, paramValues, lTimeOut);
					} catch(const char*err) {
						std::string stErr = "[Msg 219] [" + pmsg->GetRoute() + "] Error accessing database in script. Error is: '" +
							string(err) +"'. This script will return false, if there shall be a return value.";
						ReportError( g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
						return TaskDataElement("0");
					}
					catch(...)
					{
						std::string stErr = "[Msg 287] [" + pmsg->GetRoute() + "] Error accessing database in script. Unknown error. "
							"This script will return false, if there shall be a return value.";
						ReportError( g_log_type, stErr);
						pmsg->ErrSet(CR_E_FAIL, stErr);
						return TaskDataElement("0");
					}
				
					for(size_t i=de_vec.size()-1; i>=0 && st.size(); --i)
					{
						pmsg->SetDataElementValue(st.top().m_name.c_str(), de_vec[i].c_str());
						st.pop();
					}
					return TaskDataElement(res.c_str());
				} else if( !stricmp(&s[i],"neg") ) {
					f=st.top(); st.pop();
					st.push(TaskDataElement(-f));
				} else if( !stricmp(&s[i],"true") ) {
					st.push(TaskDataElement("1","no_name",t_bool));
				} else if( !stricmp(&s[i],"false") ) {
					st.push(TaskDataElement("0","no_name",t_bool));
				} else {
					st.push(pmsg->GetLocalDataElement(&s[i]));
				}
			} else if( c == '$' ) {                               
				st.push(pmsg->GetDataElement(&s[i]));
			} else if( c == '"' ) {                   
				                                                                                                
				                                                                                     
				int k=findk(&s[i+1],"\""); s[i+1+k] = 0;                              
				st.push(TaskDataElement(&s[i+1],"constant",t_string));                 
			} else if( c == '[' ) {                
				char *pc = strchr(&s[i],']');
				if( pc ) *pc=0;
				st.push(pmsg->GetDataElement(&s[i+1]));
			} else                                                           
			{
				std::string stErr = "[Msg 220] [" + pmsg->GetRoute() + "] Unrecognized operator '" + string(&s[i]) + "' in script. "
					"This script will return false, if there shall be a return value.";
				ReportError( g_log_type, stErr);
				return TaskDataElement("0");
			}
		} else {                               
			switch(c){
			case '+':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f+f1));
				break;
			case '-':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f-f1));
				break;
			case '*':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f*f1));
				break;
			case '/':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f/f1));
				break;
			case '^':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(pow(static_cast<double>(f),static_cast<double>(f1))));
				break;
			case '>':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f>f1));
				break;
			case '<':
				f1=st.top(); st.pop();
				f=st.top(); st.pop();
				st.push(TaskDataElement(f<f1));
				break;
			case '!':
				f=st.top(); st.pop();
				st.push(TaskDataElement(!f));
				break;
			case '?': {               
				f=st.top(); st.pop();
				f1=st.top(); st.pop();
				TaskDataElement f2=st.top(); st.pop();				
				st.push(TaskDataElement( f2.is_true() ? f1 : f ));
				break;}
			case '=': {
				f=st.top(); st.pop();
				pmsg->SetDataElementValue(st.top().m_name.c_str(),f.m_value.c_str());
				std::string name = st.top().m_name;
				st.pop();
				if( *name.c_str() == '$' ) st.push( GetProcessDataElement(pmsg,pmsg->m_pid,pmsg->m_piid,pmsg->m_process_version,name.c_str()) );
				else st.push( TaskDataElement( pmsg->GetLocalDataElement(name.c_str()) ));
				break; }
			default:									   
				if(isdigit(c))
				{
					double d;
					sscanf(&s[i],"%lg",&d);
					st.push(TaskDataElement(d));
				}
				else if( c==';' )
					st.push(TaskDataElement(0.));
				else if(isalpha(c))                                           
				{
					st.push(pmsg->GetLocalDataElement(&s[i]));
				}
				else
				{
					if( c )
					{
						std::string stErr = "[Msg 221] [" + pmsg->GetRoute() + "] Unrecognized operator '" + c + "' in script. "
							"This script will return false, if there shall be a return value.";
						ReportError( g_log_type, stErr);
						return TaskDataElement("0");
					}
					else st.push(TaskDataElement("0"));
				}
			}
		}
	}
	if(st.empty()) return TaskDataElement("0");                                                                         
	return st.top();
}

bool ExecuteBooleanScript(IncomingMsg* pmsg, const char* script )
{
	return ExecuteScript(pmsg, script).is_true();
}

void ReportDataElements(IncomingMsg* pmsg,const char *de)
{
	if( de && *de && strnicmp(de,"(null)",6)!=0 )
	{
		string stErr = "[Msg 222] [" + pmsg->GetRoute() + "] Reported Data: " + string(de) + " = " + pmsg->GetDataElementValue(de);
		ReportError( g_log_type, stErr, EVENTLOG_INFORMATION_TYPE);
	}
	else
	{
		string stErr = "[Msg 223] [" + pmsg->GetRoute() + "] Reported Data: " + pmsg->GetDataElementValueList();
		ReportError( g_log_type, stErr, EVENTLOG_INFORMATION_TYPE);
	}
}

  
                                                 
                                                          
                                                                  
  
void GetDataElementTypeAndId(IncomingMsg* pmsg, int pid, int proc_version, int tid, const char*de, data_type& type, int& id, string& initialValue)
{
	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, std::string("GetDataElementTypeAndId() ") + de);

	konga::SmartDbPtr<PDb::TaskDataElementsTab> pTaskDataElementsTab( PDb::TaskDataElementsTab::MakeInstance(pmsg->GetPDB()));
	std::string stFilter;
	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pid) + ") AND (ProcessVersion = " + toString(proc_version) +
			") AND (TaskId = " + toString(tid) + ") AND (UPPER(DataElementName) = UPPER('"
			+ SqlEscape(string(de))+ "'))";
		pTaskDataElementsTab->Select(stFilter);

		if( !pTaskDataElementsTab->Next() ) 
		{
			if( stricmp(de,"OperationId") != 0 && stricmp(de,"NextRouteID") != 0 )
			{
				std::string stErr = "[Msg 224] [" + pmsg->GetRoute() + "] Data element '" + string(de) + "' not defined for this task. " + stFilter;
				ReportError(g_log_type, stErr, EVENTLOG_WARNING_TYPE);
			}
			pTaskDataElementsTab->Close();
			type=t_undefined;
			return;
		}

		if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "int", 3) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "srt", 3) == 0) type = t_int;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "lng", 3) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "long", 4) == 0 ) type = t_long;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "str", 3) == 0 ||
			 strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "var", 3) == 0 ) type = t_string;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "txt", 3) == 0 ) type = t_text;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "bol", 3) == 0	||
			 strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "bool", 4) == 0) type = t_bool;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "float", 5) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "flt", 3) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "single", 6) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "sng", 3) == 0) type = t_float;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "double", 6) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "dbl", 3) == 0)
			type = t_double;
		else if( strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "date", 4) == 0 ||
			strnicmp( pTaskDataElementsTab->GetDataElementType().c_str(), "dte", 3) == 0)
			type = t_date;
		else 
		{
			type = t_undefined;
			string stErr = "[Msg 225] Data element \"" + string(de) + "\" has type = \"" + pTaskDataElementsTab->GetDataElementType() +
					"\", but I don't know that type! (" + stFilter+ ")";
			ReportError(g_log_type, stErr, EVENTLOG_WARNING_TYPE);
		}

		id = pTaskDataElementsTab->GetDataElementId();
		if( pTaskDataElementsTab->IsInitialValueNull() )
			initialValue = "";
		else
			initialValue = pTaskDataElementsTab->GetInitialValue();

		pTaskDataElementsTab->Close();
	}
	catch(konga::DbException& e)
	{
		std::string stErr = "Database error: GetDataElementTypeAndId for " + stFilter + " -- " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

  
                                                                                              
                                                                                           
  
TaskDataElement GetProcessDataElement(IncomingMsg* pmsg, int pid, int piid, int proc_version, const char* de)
{
	if( pid <= 0 || proc_version <= 0 || !de || !*de )
	{
		std::string stErr = "[Msg 226] Illegal: trying to get data element '" + string(de&&*de?de:"<null>") + "' from version " + 
			toString(pid) + " of process " + toString(proc_version) + " (instance " + toString(piid) + ")";
		ReportError( g_log_type, stErr);
		return TaskDataElement();
	}

	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, std::string("GetProcessDataElement() ") + de);

	data_type type;
	int id=-1;
	string initialValue;

	GetDataElementTypeAndId(pmsg,pid,proc_version,0,de,type,id,initialValue);
	if( type == t_text && pmsg->DidSwitchDatabaseServers() )
	{
		std::string stErr = "[Msg 429] [" + pmsg->GetRoute() + "] This task has been terminated "
			"because it required the use of a global data element of type 'long string' after a switch of database "
			"servers had occurred. Such data elements cannot be guaranteed to have been replicated "
			"completely during failure of one of the database servers in the cluster. Since data "
			"integrity cannot be guaranteed the task has been terminated. Regular strings do not "
			"have this limitation and neither do task data elements of any type.";
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	TaskDataElement tde(initialValue.c_str(),de,type,id);

	konga::SmartDbPtr<IDb::DataElementTab> pIDBDataElementTab( IDb::DataElementTab::MakeInstance(pmsg->GetIDB()) );

	string stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " + toString(pmsg->m_piid) + ") AND "
						"(TaskId = 0) AND (TaskInstanceId = 0) AND (DataElementId = " + toString(id) + ")";

	try {
		pIDBDataElementTab->Select(stFilter);	
		if( pIDBDataElementTab->Next() )
		{
			char sBuf[50]; sBuf[0]=0;
			std::string stValue;
			switch( pIDBDataElementTab->GetDataType() )
			{
			case t_int:
			case t_long:				
				stValue = toString(pIDBDataElementTab->GetIntValue());
				break;
			case t_single:
			case t_float:
			case t_double:
				sprintf(sBuf,"%.14g",pIDBDataElementTab->GetDoubleValue());
				stValue = sBuf;
				break;
			case t_text:
				{
				konga::SmartDbPtr<IDb::DataElementTextTab> pIDBDataElementTextTab( IDb::DataElementTextTab::MakeInstance(pmsg->GetIDB()) );
				try {
					pIDBDataElementTextTab->Select(stFilter);
					if( pIDBDataElementTextTab->Next() )
						stValue = pIDBDataElementTextTab->GetStringValue();
					pIDBDataElementTextTab->Close();
				} catch(DbException& e) {
					std::string stErr = "[Msg 449] [" + pmsg->GetRoute() + "] Database error when reading data element '" + string(de) +
						"' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
						"information was: " + g_idb_connect.PrettyConnectInfoString();
					pIDBDataElementTextTab->Close();
					throw CrFatalException(CR_E_FAIL, stErr);
				}
				break;
				}
			default:
				if( pIDBDataElementTab->IsStringValueNull() )
				{
					tde.set_null(true);
					stValue = "";
				}
				else
					stValue = pIDBDataElementTab->GetStringValue();
			}
	
			tde.m_value.swap(stValue);
			pIDBDataElementTab->Close();
		}
	} catch(DbException& e) {
		std::string stErr = "[Msg 385] [" + pmsg->GetRoute() + "] Database error when reading data element '" + string(de) +
			"' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
			"information was: " + g_idb_connect.PrettyConnectInfoString();
		pIDBDataElementTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}											   

	return tde;
}

  
                                                                                                
                                                                                 
  
void SetProcessDataElement(IncomingMsg* pmsg, int pid, int piid, int proc_version, const char* de, const char* cval, TaskDataElement* pTde      )
{
	DUMPTIMINGS AutoDebugDumper auto_dump(pmsg, std::string("SetProcessDataElement() ") + de);

	TaskDataElement tde;
	if( !pTde )
	{
		data_type type;
		int id=-1;
		string initialValue;
		GetDataElementTypeAndId(pmsg, pid, proc_version, 0, de, type, id, initialValue);
		if( id == -1 ) return;              
		tde = TaskDataElement( cval, de, type, id );
		pTde = &tde;
	}

	DumpOneDataElement(pmsg, *pTde);

	konga::SmartDbPtr<IDb::DataElementTab> pDataElementTab( IDb::DataElementTab::MakeInstance(pmsg->GetIDB()));
	string stFilter = "WHERE (ProcessId = " + toString(pid) + ") AND (ProcessInstanceId = " + toString(piid) + ")"
		" AND (TaskId = 0) AND (TaskInstanceId = 0) AND (DataElementId = " + toString(pTde->GetId()) + ")";

	try
	{
		pDataElementTab->Select(stFilter);

		bool bInsert = !pDataElementTab->Next();

		if( bInsert )
			pDataElementTab->AddNew();
		else
			pDataElementTab->Edit();

		pDataElementTab->SetProcessInstanceId(pid);
		pDataElementTab->SetTaskId(0);
		pDataElementTab->SetTaskInstanceId(0);
		pDataElementTab->SetDataElementId(pTde->GetId());
		pDataElementTab->SetDataType(pTde->GetType());
		pDataElementTab->SetDataElementName(de);

		switch( pTde->GetType() )
		{
		case t_int:
		case t_long:
			pDataElementTab->SetIntValue(static_cast<int>(*pTde));
			break;
		case t_single:
		case t_float:
		case t_double:
			pDataElementTab->SetDoubleValue(static_cast<double>(*pTde));
			break;
		case t_text:
			{
				konga::SmartDbPtr<IDb::DataElementTextTab> pDataElementTextTab( IDb::DataElementTextTab::MakeInstance(pmsg->GetIDB()));
				try {
					if( bInsert )
						pDataElementTextTab->AddNew();
					else
						pDataElementTextTab->Edit();

					pDataElementTextTab->SetProcessInstanceId(pid);
					pDataElementTextTab->SetTaskId(0);
					pDataElementTextTab->SetTaskInstanceId(0);
					pDataElementTextTab->SetDataElementId(pTde->GetId());
					pDataElementTextTab->SetStringValue(pTde->m_value);

					pDataElementTextTab->Update();
					pDataElementTextTab->Close();
				} catch(DbException& e) {
					std::string stErr = "[Msg 386] [" + pmsg->GetRoute() + "] Database error when writing data element '" + string(de) +
						"' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
						"information was: " + g_idb_connect.PrettyConnectInfoString();
					ReportError( g_log_type, stErr);
					pDataElementTextTab->Close();
					pDataElementTab->Close();
					return;
				}
			break;
			}
		case t_string:
		case t_bool:
		default:
			{
				pDataElementTab->SetStringValue(pTde->m_value);
			}
		}

		pDataElementTab->Update();
		pDataElementTab->Close();
	}
	catch(DbException& e)
	{
		std::string stErr = "[Msg 328] [" + pmsg->GetRoute() + "] Database error when writing data element '" + string(de) +
			"' to DB for " + stFilter + " -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " +
			g_idb_connect.VerbosePrettyConnectInfoString();
		pDataElementTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}
}

  
                                                                                                  
                                                        
  
void GetProcessDataElementList(IncomingMsg* pmsg, int pid, int piid, int proc_version, std::vector<TaskDataElement>& vecTde)
{
	if( pid <= 0 || proc_version <= 0 )
	{
		std::string stErr = "[Msg 354] Illegal: trying to get data element list from version " + toString(pid) +
			" of process " + toString(proc_version) + " (instance " + toString(piid) + ")";
		ReportError( g_log_type, stErr);
		return;
	}

	string stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessInstanceId = " +
		toString(pmsg->m_piid) + ") AND (TaskId = 0) AND (TaskInstanceId = 0)";

	konga::SmartDbPtr<IDb::DataElementTab> pIDBDataElementTab( IDb::DataElementTab::MakeInstance(pmsg->GetIDB()) );
	try {
		pIDBDataElementTab->Select(stFilter);

		while(pIDBDataElementTab->Next())
		{
			char sBuf[50]; sBuf[0]=0;
			std::string stValue;
			bool bIsNull = false;
			switch( pIDBDataElementTab->GetDataType() )
			{
			case t_int:
			case t_long:
				stValue = toString(pIDBDataElementTab->GetIntValue());
				break;
			case t_single:
			case t_float:
			case t_double:
				stValue = toString(pIDBDataElementTab->GetDoubleValue());
				break;
			case t_text:
				{
					konga::SmartDbPtr<IDb::DataElementTextTab> pIDBDataElementTextTab( IDb::DataElementTextTab::MakeInstance(pmsg->GetIDB()) );
					try {
						pIDBDataElementTextTab->Select(stFilter);
						if( pIDBDataElementTextTab->Next() )
							stValue = pIDBDataElementTextTab->GetStringValue();
						pIDBDataElementTextTab->Close();
					} catch(DbException& e) {
						std::string stErr = "[Msg 449] [" + pmsg->GetRoute() + "] Database error when reading data element '" + 
							pIDBDataElementTab->GetDataElementName() +
							"' from DB. " + e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
							"information was: " + g_idb_connect.PrettyConnectInfoString();
						pIDBDataElementTextTab->Close();
						throw CrFatalException(CR_E_FAIL, stErr);
					}
					break;
				}
			default:
				if( pIDBDataElementTab->IsStringValueNull() )
				{
					bIsNull = true;
					stValue = "";
				}
				else
					stValue = pIDBDataElementTab->GetStringValue();
			}

			vecTde.push_back( TaskDataElement( stValue.c_str(),
				pIDBDataElementTab->GetDataElementName().c_str(), data_type(pIDBDataElementTab->GetDataType()),
				pIDBDataElementTab->GetDataElementId(), bIsNull ) );
		}

		pIDBDataElementTab->Close();
	} catch(DbException& e) {
		std::string stErr = "[Msg 355] [" + pmsg->GetRoute() + "] Database error when reading data element list from DB. " + 
			e.GetErrorString() + ". [" + e.GetStateNativeOrigin() + "] The Connection "
			"information was: " + g_idb_connect.PrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		pIDBDataElementTab->Close();
		return;
	}											   
}
