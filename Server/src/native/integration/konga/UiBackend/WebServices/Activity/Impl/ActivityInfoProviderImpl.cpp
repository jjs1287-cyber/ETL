/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <time.h>
#include <ctype.h>

#include <iostream>
#include <algorithm>	             
#include <cassert>

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/TraceLog.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/StringUtils.h>		           

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Db/TranDb/ProjectOperationsView.h>
#include <konga/Db/TranDb/HistoryLogsView.h>
#include <konga/Db/TranDb/OperationLogsView.h>
#include <konga/Db/TranDb/LogAuxTab.h>
#include <konga/Db/TranDb/XferHistoryLogsView.h>

#include <konga/ItemAccess/AccessSqlGenerator.h>
#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/UiBackend/GuidMappings/CachedGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/Authentication/Authentication.h>

#include <konga/OperationLog/OperationLog.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include "ActivityInfoProviderImpl.h"
#include "utils.h"

using namespace konga;
using namespace TranDb;
using std::string;
using std::vector;
using std::map;

namespace {
	string getSortOrder(const string&stPKName, int sortType){
		if(sortType>0)return "";
		return string(" ORDER BY ")+stPKName+" DESC";
	}

	string getCompareString(const string& stKeyName, int sortType){
		return stKeyName+(sortType>0?" > ":" < ");
	}

	                                          
	string getIdFilter(DbConnection*pTranDb, int id,int type_id, bool&bUninitialized){
		if( type_id!=OperationEntityId &&
			type_id!=InterchangeProjectEntityId &&
			type_id!=FolderEntityId )
			return "";
		CollectOpids ids(pTranDb,id,type_id);
		bUninitialized=false;
		return ids.getOpFilter();
	}

	string getIdFilter(DbConnection*pTranDb, int id, int type_id){
		if( type_id!=OperationEntityId &&
			type_id!=InterchangeProjectEntityId &&
			type_id!=FolderEntityId )
			return "";
		CollectOpids ids(pTranDb,id,type_id);
		return ids.getOpFilter();
	}
	
	string getOperationNameFilter(const string& stOperationName_in, bool& bUninitialized) {
		if ( stOperationName_in.empty() ) return "";
		string stOperationNameLower(stOperationName_in);
		ToLower(stOperationNameLower);

		string sql( bUninitialized ? "WHERE (":" AND (" );
		sql += "LOWER(Operation_Name) LIKE '%" + SqlLikeEscape(stOperationNameLower) + "%')";
		bUninitialized=false;
		return sql;
	}

	string to_short_dat_time_string(time_t ltime){
		struct tm* t=gmtime( &ltime );
		char buf[30];
		                                      
		                                                        
		                                                                          
		sprintf(buf,"%4.4d-%2.2d-%2.2d %2.2d:%2.2d:%2.2d",t->tm_year+1900,t->tm_mon+1,t->tm_mday,t->tm_hour,t->tm_min,t->tm_sec);
		return string(buf);
	}

	string toStringTS(TIMESTAMP_STRUCT ts){
		char buf[30];
		                                      
		                                                        
		                                                                          
		sprintf(buf,"%4.4d-%2.2d-%2.2d %2.2d:%2.2d:%2.2d",ts.year,ts.month,ts.day,ts.hour,ts.minute,ts.second);
		return string(buf);
	}

	const char tab[] = "\t";

	string EscapeTabInString(const string& val) {
		if( val.find(tab) == string::npos ) return val;

		string newVal(val);
		konga::ReplaceCharWithChar(newVal, '\t', ' ');
		return newVal;
	}
}

namespace {

	bool IsFieldNull(DynamicDbRecordSet& rs, const string& fieldName) {
		return rs.IsFieldNull(rs.GetFieldIndexByName(fieldName));
	}

	string GetFieldValue(DynamicDbRecordSet& rs, const string& fieldName) {
		if ( IsFieldNull(rs, fieldName) ) return "";
		return rs.GetFieldValue(fieldName);
	}

	long GetFieldValueAsLong(DynamicDbRecordSet& rs, const string& fieldName) {
		const string& tmp = GetFieldValue(rs, fieldName);
		if ( tmp.empty() ) return 0;
		return toLong(tmp);
	}

	long long GetFieldValueAsLongLong(DynamicDbRecordSet& rs, const string& fieldName) {
		const string& tmp = GetFieldValue(rs, fieldName);
		if ( tmp.empty() ) return 0;
		return toLongLong(tmp);
	}

	string GetFieldValueTS(DynamicDbRecordSet& rs, const string& fieldName) {
		if ( IsFieldNull(rs, fieldName) ) return "";
		return toStringTS(rs.GetFieldValueAsTimeStampStruct(rs.GetFieldIndexByName(fieldName)));
	}

	                                
	                                                                                        
	string getXferHistoryLog(DynamicDbRecordSet& rs, GuidToIdMapper& guidMapper){
		if(IsFieldNull(rs, "Operation_ID") || IsFieldNull(rs, "ID") || IsFieldNull(rs, "Operation_Name"))
			throw CrException("get TransferHistoryLog failed, internal error, XferHistoryLogView contains null-value in at least one of the columns: Operation_ID/Operation_Name/ID");
		const int ID=GetFieldValueAsLong(rs, "ID");
		const int interchangeproject_ID = !IsFieldNull(rs, "interchangeproject_id") ? GetFieldValueAsLong(rs, "interchangeproject_id") : ID;

		return string(
			string(interchangeproject_ID!=0 ? konga::toString(interchangeproject_ID) : string("")) + tab +
			guidMapper.GetGuid(GetFieldValueAsLong(rs, "Operation_ID"), OperationEntityId) + tab +
			EscapeTabInString(GetFieldValue(rs, "Operation_Name")) + tab +
			konga::toString(ID) + tab +
			EscapeTabInString(GetFieldValue(rs, "FileName")) + tab +
			GetFieldValue(rs, "FileSize") + tab +
			GetFieldValueTS(rs, "XferDateTime")
			);
	}

	string getXferHisLog_old(XferHistoryLogsView*v,int& ID){
		if(v->IsOperation_IDNull()||v->IsIDNull())
			throw CrException("get TransferHistoryLog failed, internal error, XferHistoryLogView contains null-value in at least one of the columns: Operation_ID/Operation_Name/ID");
		ID=v->GetID();
		const int interchangeproject_ID=!v->Isinterchangeproject_idNull()?v->Getinterchangeproject_id():ID;
		string str=(interchangeproject_ID!=0?konga::toString(interchangeproject_ID):"")+tab+
			GetGuidForId(v->GetOperation_ID(),OperationEntityId)+tab+
			v->GetOperation_Name()+tab+
			konga::toString(ID)+tab;
		if(!v->IsFileNameNull())str+=v->GetFileName();
		str+=tab;
		if(!v->IsFileSizeNull())str+=konga::toString(v->GetFileSize());
		str+=tab;
		if(!v->IsXferDateTimeNull())str+=toStringTS(v->GetXferDateTime());
		return str;
	}
}

konga::OperationMap::OperationMap(DbConnection*pTranDbConn,int id,int entity_type_id, const string& stOperationName)
{
	konga::SmartDbPtr<ProjectOperationsView> view( ProjectOperationsView::MakeInstance(pTranDbConn) );
	bool bUninitialized=true;
	string stFilter( getIdFilter(pTranDbConn,id,entity_type_id,bUninitialized) );
	stFilter+=getOperationNameFilter(stOperationName, bUninitialized);

	view->Select(stFilter);
	while(view->Next()){
		if(view->IsOperation_IDNull()||view->IsOperation_NameNull())
			throw CrException("get Operation Queue failed, internal error, ProjectOperationsView contains null-value in at least one of these columns: Operation_ID/Operation_Name");
		int interchangeproject_ID=view->Isinterchangeproject_idNull()?0:view->Getinterchangeproject_id();
		int Operation_ID=view->GetOperation_ID();
		string Operation_Name( view->IsOperation_NameNull()?"":view->GetOperation_Name() );
		if(op_map.find(Operation_ID)==op_map.end())
			op_map[Operation_ID]=new konga::ProjectOperation(interchangeproject_ID,Operation_ID,Operation_Name);
	}
	view->Close();
}

konga::OperationMap::~OperationMap()
{
	for(map<int, ProjectOperation*>::iterator it=op_map.begin();
		it!=op_map.end();it++){
			delete it->second;
	}
}

namespace {
	                                                                                             
	                                                                       
	struct CharMatchUnprintablePred {
		CharMatchUnprintablePred(void) {}
		bool operator()(char c) {
			unsigned char uc = (unsigned char)c;
			if ( uc > 127 && uc < 255 ) return false;
			switch(uc) {
				case 10:
				case 13:
					return false;
				default:
					return !isprint(uc);
			};
		}
	};

	string getLogAuxTab(LogAuxTab& logAuxTab){
		string line(tab);
		if(!logAuxTab.IsField_NameNull())
			line+=EscapeTabInString(logAuxTab.GetField_Name());
		line+=tab;

		string stLongMsg;
		if(!logAuxTab.IsLong_MessageNull())
			stLongMsg = EscapeTabInString(logAuxTab.GetLong_Message());

		CharMatchUnprintablePred unprintable;
		std::replace_if(stLongMsg.begin(), stLongMsg.end(), unprintable, '?');
		line+=stLongMsg;
		return line;
	}

	                                                             
	                                                                        
	  
                                                                                      
                                                                                              
  
                                               
                            
                                                       
                                                                     
                        
                                  
                                                                                          
                                  
                                                   
                                 
                                                                  
                        
                                                                              
                                               
                                   
       
              
   
        
                              
                                                               
                                                                                                                                                                                                
                                                               
   
                          
                       
                                                 
                                                                       
              
   

                                                                                           
                                    

                                         
                                                   
                
                                                                                         
           
                                                                
                                         
                                               
                                        
   
           
                                                                       
             
  

                                                                                   
                                                         
                                                     
                                                                                 
                 
                                
                        
                                   
          
                                                                                                               
   
                           
                            
                                                                                                
                                                          
  
   
}

ActivityInfoProviderImpl::ActivityInfoProviderImpl(const char* user, const char* password)
: WsImplBaseAuthenticator(user, password), m_sPassword( password == 0 ? "" : password) {}

ActivityInfoProviderImpl::~ActivityInfoProviderImpl(){}

namespace {
	string GetSqlWithPermissionJoin(const string& sMainTable, const string& sMainTableId,
		const string& sWhereFilter, const string& sSortOrder,
		int iEntityTypeId, const UserInfo& userInfo)
	{
		return string(
			"SELECT * from " + sMainTable + " " +
			sWhereFilter +
			string(sWhereFilter.empty() ? " WHERE (" : " AND (") +
			GetAccessControlWhereClause(userInfo, sMainTableId, iEntityTypeId) + ") " +
			sSortOrder
			);
	}

	class OperationLogCompounder {
	public:
		OperationLogCompounder(void) : m_count(0), m_llOperationInstanceId(-1), m_mainEntry(0) {}
		~OperationLogCompounder(void) {
			DeleteAux();
			delete m_mainEntry;
		}

	public:
		void AddEntry(DynamicDbRecordSet& rs, vector<WsOperationLogInstance*>& v) {
			const long long llOperationInstanceId = toLongLong( rs.GetFieldValue("operation_instance_id") );
			FinishCurrent(llOperationInstanceId, v);

			const string stFieldName = GetUtf8Field(rs, "field_name");
			const string stMessage = GetUtf8Field(rs, "message");
			if ( stFieldName.empty() ) {
				const int status = GetFieldValueAsLong(rs, "status");
				const string stOperationName = EscapeTabInString(GetUtf8Field(rs, "operation_name"));

				                  
				m_mainEntry = new WsOperationLogInstance();
				m_mainEntry->setFrom(const_cast<xsd__string>(GetUtf8Field(rs, "received_from").c_str()), true);
				m_mainEntry->setMessage(const_cast<xsd__string>(stMessage.c_str()), true);
				m_mainEntry->setMsgId(GetFieldValueAsLongLong(rs, "msgid"));

				m_mainEntry->setNumberOfSourceFiles( GetFieldValueAsLong(rs, "source_files") );
				m_mainEntry->setNumberOfSourceRecords( GetFieldValueAsLong(rs, "source_records") );
				m_mainEntry->setSourceTransferBytes( GetFieldValueAsLong(rs, "source_size") );

				m_mainEntry->setNumberOfTargetFiles( GetFieldValueAsLong(rs, "target_files") );
				m_mainEntry->setNumberOfTargetRecords( GetFieldValueAsLong(rs, "target_files") );
				m_mainEntry->setTargetTransferBytes( GetFieldValueAsLong(rs, "target_size") );

				m_mainEntry->setOperationGuid(const_cast<xsd__string>(GetFieldValue(rs, "guid").c_str()), true);
				m_mainEntry->setOperationInstanceId(llOperationInstanceId);
				m_mainEntry->setOperationName(const_cast<xsd__string>(stOperationName.c_str()), true);
				m_mainEntry->setParentOperationInstanceId(GetFieldValueAsLongLong(rs, "parent_operation_instance_id"));

				m_mainEntry->setStatus(status);
				m_mainEntry->setReceivedTimeStamp(const_cast<xsd__string>(GetFieldValueTS(rs, "entered_ts").c_str()), true);
				m_mainEntry->setStartTimeStamp(const_cast<xsd__string>(GetFieldValueTS(rs, "started_ts").c_str()), true);
				m_mainEntry->setStatusTimeStamp(const_cast<xsd__string>(GetFieldValueTS(rs, "status_ts").c_str()), true);
			}
			else {
				                   
				WsOperationMessage* opMsg = new WsOperationMessage();
				opMsg->setFieldName(const_cast<xsd__string>(stFieldName.c_str()), true);
				opMsg->setMessage(const_cast<xsd__string>(stMessage.c_str()), true);
				m_aux.push_back(opMsg);
			}
		}

		void FinishCurrent(long long llOperationInstanceId, vector<WsOperationLogInstance*>& v) {
			if ( llOperationInstanceId != m_llOperationInstanceId ) {
				                                         
				if ( m_mainEntry != 0 ) {
					m_count++;
					if ( !m_aux.empty() ) {
						m_mainEntry->setOperationMessages(allocate_ArrayOfT<WsOperationMessage, WsOperationMessage_Array, ArrayOfWsOperationMessage>(m_aux));
					}

					v.push_back(m_mainEntry);
					m_mainEntry = 0;
				}

				DeleteAux();
				m_llOperationInstanceId = llOperationInstanceId;
			}
		}

		long GetCount() const {
			return m_count;
		}

	private:
		static string GetUtf8Field(DynamicDbRecordSet& rs, const string& field) {
			const string encoded = GetFieldValue(rs, "etlforge_utf8_" + field);
			if(encoded.empty())return "";
			if(encoded.size()%2)throw CrException("Invalid operation history UTF-8 transport length");
			string result;result.reserve(encoded.size()/2);
			for(size_t i=0;i<encoded.size();i+=2) {
				auto digit=[](char c)->int {if(c>='0'&&c<='9')return c-'0';if(c>='a'&&c<='f')return c-'a'+10;throw CrException("Invalid operation history UTF-8 transport data");};
				result+=static_cast<char>(digit(encoded[i])*16+digit(encoded[i+1]));
			}
			return result;
		}

		void DeleteAux() {
			for( vector<WsOperationMessage*>::const_iterator cit = m_aux.begin(); cit != m_aux.end(); ++ cit ) {
				delete *cit;
			}
			m_aux.clear();
		}

	private:
		long m_count;
		long long m_llOperationInstanceId;

		WsOperationLogInstance* m_mainEntry;
		vector<WsOperationMessage*> m_aux;
	};
}

namespace {
	enum SqlFilterOperator {
		sqlAND = 1,
		sqlOR = 2
	};

	bool NeedsParanthesis(const string& filter) {
		return !( filter.size() > 2 && *filter.begin() == '(' && *(filter.end()-1) == ')' );
	}

	void AddToSqlFilter(string& sqlFilter, const string& additionalFilter, SqlFilterOperator op) {
		if ( additionalFilter.empty() ) return;

		if ( !sqlFilter.empty() ) {
			if ( NeedsParanthesis(sqlFilter) ) {
				sqlFilter.insert(0, "(").append(")");
			}
			switch( op ) {
			case sqlAND:
				sqlFilter.append(" AND ");
				break;
			case sqlOR:
				sqlFilter.append(" OR ");
				break;
			};
		}

		if ( NeedsParanthesis(additionalFilter) ) {
			sqlFilter.append("(").append(additionalFilter).append(")");
		}
		else {
			sqlFilter.append(additionalFilter);
		}
	}

	                                                     
	template<class T>
	void AddToSqlFilter(string& sqlFilter, const vector<T>& vals, const string& field_name, SqlFilterOperator op, int maxSetSize = -1) {
		                                                                                         
		if ( vals.empty() || (maxSetSize > 0 && vals.size() == (size_t)maxSetSize ) ) return;

		string addlFilter(field_name);
		if ( vals.size() == 1 ) {
			                                            
			addlFilter.append(1, '=').append(toString(*vals.begin()));
		}
		else {
			                                            
			addlFilter.append(" IN (");

			for( typename vector<T>::const_iterator cit = vals.begin(); cit != vals.end(); ++cit ) {
				if ( cit != vals.begin() ) addlFilter.append(",");
				addlFilter.append( konga::toString(*cit) );
			}
			addlFilter.append(")");
		}

		AddToSqlFilter(sqlFilter, addlFilter, op);
	}

	vector<long long> toVectorLong(ArrayOf_xsd_long* wsArray) {
		vector<long long> retVal;
		if ( wsArray == 0 ) return retVal;

		xsd__long_Array* longArr = wsArray->getitem();
		if ( longArr == 0 ) return retVal;

		int size = 0;
		const xsd__long** arr = longArr->get(size);
		if ( arr == 0 || size == 0 ) return retVal;

		for( int i = 0; i < size; ++i ) {
			retVal.push_back( *arr[i] );
		}

		return retVal;
	}

	vector<int> toVectorInt(ArrayOf_xsd_int* wsArray) {
		vector<int> retVal;
		if ( wsArray == 0 ) return retVal;

		xsd__int_Array* intArr = wsArray->getitem();
		if ( intArr == 0 ) return retVal;

		int size = 0;
		const xsd__int** arr = intArr->get(size);
		if ( arr == 0 || size == 0 ) return retVal;

		for( int i = 0; i < size; ++i ) {
			retVal.push_back( *arr[i] );
		}

		return retVal;
	}

	vector<string> toVectorString(ArrayOf_xsd_string* wsArray) {
		vector<string> retVal;
		if ( wsArray == 0 ) return retVal;
		
		xsd__string_Array* strArr = wsArray->getitem();
		if ( strArr == 0 ) return retVal;

		int size;
		const xsd__string* arr = strArr->get(size);
		if ( arr == 0 || size == 0 ) return retVal;

		for( int i = 0; i < size; ++i ) {
			retVal.push_back( toString(arr[i]) );
		}

		return retVal;
	}

	void RemoveInvalid(vector<long long>& operationInstanceIds) {
		for( vector<long long>::iterator it = operationInstanceIds.begin(); it != operationInstanceIds.end(); ) {
			if ( *it < 1 ) {
				it = operationInstanceIds.erase(it);
			}
			else {
				++it;
			}
		}
	}

	string getOperationSqlFilter(WsOperationInstanceFilter& filter, DbConnection* pTranDb, const GuidToIdMapper& guidMapper, const string& timeStampColName) {
		string sqlFilter;
		vector<long long> operationInstanceIds( toVectorLong(filter.getOperationInstances()) );
		RemoveInvalid(operationInstanceIds);
		if ( !operationInstanceIds.empty() ) {
			AddToSqlFilter(sqlFilter, operationInstanceIds, "operation_instance_id", sqlAND);
		}
		else {
			const vector<int>& guidTypes = toVectorInt( filter.getGuidTypes() );
			if ( !guidTypes.empty() ) {
				const vector<string>& guids = toVectorString( filter.getGuids() );
				if ( guidTypes.size() == guids.size() ) {
					const vector<int>::size_type size = guidTypes.size();
					for( size_t i = 0; i < size; ++i ) {
						const int guidType = guidTypes.at(i);
						if ( guidType < 1 ) continue;

						const string& guid = guids.at(i);
						const long id = guidMapper.GetId(guid, guidType);
						if ( id != GuidToIdMapper::invalid_id ) {
							const string& thisFilter = getIdFilter(pTranDb, id, guidType);
							AddToSqlFilter(sqlFilter, thisFilter, sqlOR);
						}
					}
				}
			}
		}

		                      
		if ( !timeStampColName.empty() ) {
			const string& startDate = toString( filter.getStartDate() );
			if ( !startDate.empty() ) {
				AddToSqlFilter(sqlFilter, timeStampColName + " >= '" + startDate + "'", sqlAND);
			}
			const string& endDate = toString( filter.getEndDate() );
			if ( !endDate.empty() ) {
				AddToSqlFilter(sqlFilter, timeStampColName + " <= '" + endDate + "'", sqlAND);
			}
		}

		                         
		const string& searchString = toString(filter.getSearchString());
		if ( !searchString.empty() ) {
			AddToSqlFilter(sqlFilter, "operation_instance_id IN(SELECT operation_instance_id from operationmessagetab WHERE message LIKE '%" + konga::SqlLikeEscape(searchString) + "%')", sqlAND);
		}

		                 
		vector<int> logStatuses = toVectorInt( filter.getLogStatuses() );
		AddToSqlFilter<int>(sqlFilter, logStatuses, "status", sqlAND, NUMBER_OF_VALID_STATUSES);

		                         
		const string& operationName = toString(filter.getOperationName());
		if ( !operationName.empty() ) {
			AddToSqlFilter(sqlFilter, "operation_name LIKE '%" + konga::SqlLikeEscape(operationName) + "%'", sqlAND);
		}

		if  ( !sqlFilter.empty() ) sqlFilter.insert(0, "WHERE ");

		return sqlFilter;
	}
}

WsOperationLogInstance_Array* ActivityInfoProviderImpl::getOperationLog(WsOperationInstanceFilter& filter) {
	try {
		SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	
		DbGuidToIdMapper _dbGuidMapper(pTranDbConn);
		CachedGuidToIdMapper guidMapper(_dbGuidMapper);

		const string& stFilter = getOperationSqlFilter(filter, pTranDbConn, guidMapper, "status_ts");
		const int sortType = filter.getSortType();
		string baseSql = GetSqlWithPermissionJoin("operationlogview", "operation_id", stFilter, getSortOrder("entered_tm", sortType),
			OperationEntityId, m_UserInfo);
		baseSql.replace(0,8,"SELECT operationlogview.*, encode(convert_to(operation_name,'UTF8'),'hex') AS etlforge_utf8_operation_name, encode(convert_to(field_name,'UTF8'),'hex') AS etlforge_utf8_field_name, encode(convert_to(message,'UTF8'),'hex') AS etlforge_utf8_message, encode(convert_to(received_from,'UTF8'),'hex') AS etlforge_utf8_received_from");

		int maxRecords = filter.getMaxRecords();
		if ( maxRecords < 1 ) maxRecords = 1;
		if ( maxRecords > 10000 ) maxRecords = 10000;

		                                                                                            
		const string sql( baseSql + " LIMIT " + konga::toString(maxRecords*10) );

		UIBACKEND_TRACE("[getOperationLog] about to execute SQL: " + sql);
		SmartDbPtr<DynamicDbRecordSet> rsOpLog( DynamicDbRecordSet::MakeInstance(pTranDbConn, sql) );
		rsOpLog->Open();

		vector<WsOperationLogInstance*> logInstances;
		AutoPtrDeleteVector<WsOperationLogInstance> adopt_logInstances(logInstances);
		OperationLogCompounder vOpLog;
		while( rsOpLog->Next() ) {
			vOpLog.AddEntry(*rsOpLog, logInstances);
			if( vOpLog.GetCount() >= maxRecords ) break;
		}
		vOpLog.FinishCurrent(-1, logInstances);

		UIBACKEND_TRACE("[getOperationLog] Found " + konga::toString(vOpLog.GetCount()) + " entries.");

		WsOperationLogInstance_Array* retVal = allocate_T_Array<WsOperationLogInstance, WsOperationLogInstance_Array>(logInstances);

		UIBACKEND_TRACE("[getOperationLog] About to return.");
		return retVal;
	}
	catch( const CrException& e )
	{
		throw CrException("get OperationLog failed: " + e.GetErrorString());
	}
}

namespace {
	WsOperationProgress* createWsOperationProgress(DynamicDbRecordSet& rs) {
		std::auto_ptr<WsOperationProgress> pWsOperationProgress( new WsOperationProgress() );

		pWsOperationProgress->setEstimatedEndTimeUtc(const_cast<xsd__string>(GetFieldValueTS(rs, "estimated_end_time").c_str()), true);
		pWsOperationProgress->setMessage( const_cast<xsd__string>(GetFieldValue(rs, "message").c_str()), true);
		pWsOperationProgress->setOperationGuid( const_cast<xsd__string>(GetFieldValue(rs, "guid").c_str()), true);
		pWsOperationProgress->setOperationInstanceId( GetFieldValueAsLongLong(rs, "operation_instance_id") );
		pWsOperationProgress->setPercentageDone( GetFieldValueAsLong(rs, "percentage_done") );
		pWsOperationProgress->setSection( const_cast<xsd__string>(GetFieldValue(rs, "section").c_str()), true);

		return pWsOperationProgress.release();
	}
}

WsOperationProgress_Array* ActivityInfoProviderImpl::getOperationProgress(WsOperationInstanceFilter& filter) {
	try {
		SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );

		DbGuidToIdMapper _dbGuidMapper(pTranDbConn);
		CachedGuidToIdMapper guidMapper(_dbGuidMapper);

		string stFilter = getOperationSqlFilter(filter, pTranDbConn, guidMapper, "");
		const int sortType = filter.getSortType();
		const string& sql = GetSqlWithPermissionJoin("operationprogressview", "operation_id", stFilter, getSortOrder("operation_instance_id", sortType),
			OperationEntityId, m_UserInfo);

		UIBACKEND_TRACE("[getOperationProgress] about to execute SQL: " + sql);
		SmartDbPtr<DynamicDbRecordSet> rsProg( DynamicDbRecordSet::MakeInstance(pTranDbConn, sql) );
		rsProg->Open();

		const int maxRecords = filter.getMaxRecords() - 1;
		vector<WsOperationProgress*> progInstances;
		AutoPtrDeleteVector<WsOperationProgress> adopt_progInstances(progInstances);
		int i = 0;
		for( ; rsProg->Next(); i++) {
			progInstances.push_back( createWsOperationProgress(*rsProg) );
			if( maxRecords >= 0 && i >= maxRecords ) break;
		}

		UIBACKEND_TRACE("[getOperationProgress] Query returned " + toString(progInstances.size()) + " records. About to return.");
		return allocate_T_Array<WsOperationProgress, WsOperationProgress_Array>(progInstances);
	}
	catch( const CrException& e )
	{
		throw CrException("get OperationLog failed: " + e.GetErrorString());
	}
}
	
xsd__long_Array* ActivityInfoProviderImpl::deleteOperationLogEntry(xsd__long_Array id) {
	throw CrException("ActivityInfoProviderImpl::deleteOperationLogEntry has not been implemented yet!!");
}

xsd__string_Array* ActivityInfoProviderImpl::getSourceFileHistoryLog(WsOperationInstanceFilter& filter) {                                         

	try {
		SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	
		DbGuidToIdMapper _dbGuidMapper(pTranDbConn);
		CachedGuidToIdMapper guidMapper(_dbGuidMapper);

		const string& stFilter = getOperationSqlFilter(filter, pTranDbConn, guidMapper, "XferDateTime");
		const int sortType = filter.getSortType();
		const string& sql = GetSqlWithPermissionJoin("xferhistorylogsview", "operation_id", stFilter, getSortOrder("id", sortType),
			OperationEntityId, m_UserInfo);

		SmartDbPtr<DynamicDbRecordSet> xferHistory( DynamicDbRecordSet::MakeInstance(pTranDbConn, sql) );
		UIBACKEND_TRACE("[getSourceFileHistoryLog] about to execute SQL: " + sql);
		xferHistory->Open();

		vector<string> v;
		const int maxRecords = filter.getMaxRecords();
		for( int i=0; xferHistory->Next(); i++ ){
			v.push_back( getXferHistoryLog(*xferHistory, guidMapper) );
			if ( i+1 >= maxRecords ) break;
		}
		UIBACKEND_TRACE("[getSourceFileHistoryLog] Query returned " + toString(v.size()) + " records.");

		return allocate_xsd__string_Array(v);
	}
	catch( const DbException& e ) {
		throw CrException("get SourceFileHistoryLog failed\n"+e.GetErrorString());
	}
}

xsd__long_Array* ActivityInfoProviderImpl::deleteSourceFileHistory(xsd__long_Array entryIDs){
	DBConnectionInfo dbInfo("TranDb");
	DbConnection* pTranDbConn=0;
	try
	{
		pTranDbConn = GetDbConnection(dbInfo.connectionString());
	}
	catch( const DbException& e )
	{
		throw CrException("Delete SourceFileHistory failed!\n"+e.GetErrorString());
	}
	SmartDbPtr<DbConnection> adopt_dbConn(pTranDbConn);
	vector<long>list;
	string sql;
	long id;

	int entryIDSize = 0;
	const xsd__long** entryIDArr = entryIDs.get(entryIDSize);
	for(int i=0,n=entryIDSize;i<n;i++){
		id=*entryIDArr[i];
		sql="DELETE FROM XferHist WHERE ID = "+konga::toString(id);
		try {
			DynamicDbRecordSet* rs=CreateDynamicDbRecordSet(pTranDbConn, sql.c_str());
			rs->Open();
			list.push_back(id);
			rs->Close();
		}
		catch( const DbException& e )
		{
			throw CrException("delete SourceFileHistory failed!\nsql="+sql+"\n"+e.GetErrorString());
		}
	}

	return allocate_xsd__long_Array(list);
}


                                                                
                               
  
                                                                                                                                                                  
 
                                 
                                                                                                        

                                                                                                                     
          
      
                                                                     
                    
                                                                                              
                                   
                                                 
   
                        
                                
                                                                                   
                                              
                                
                           
  

                                     
    
  
                                                                                     

                                                                                                
                                   
                                                                
                                  
  
                              
  
                                                                                                                                                                                
                                
                           
  
                                

                               
                                               
                                       

      
                                                                       
                                               

                         
                         
                                                                                               
                                                            
                                                                          
                             
                                                  
                                       
                                                                             
                             
                            
                                                                
                                                                       
     
    

                                                                                          
                                                                     
                                   
                                                            
           
                              
                                  
                                                                                                          
                                      
                             
        
                        
                                        
                            
                                                    
                    
    
                             
                                  
                                                                                               
                                          
    
   

                                             
  
                              
  
                                                                                
                                                                                          
  
 
  

