/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>
#include <konga/ScheduleUtils/EnableDisableSchedules.h>
#include <konga/ScheduleUtils/UpdateScheduler.h>
#include "AuditDeployment.h"
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <algorithm>
#include <map>
#include <memory>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	           

#include "ProjectAdministrationImpl.h"

using namespace konga;

ProjectAdministrationImpl::ProjectAdministrationImpl(const char* user, const char* password) : DbConnectionHolder(),
	WsImplBaseAuthenticator(user, password), m_pGuidToIdMapper(0), m_pAutoRollbackTransaction(0)
{
	this->SetInternalDbName("TranDb");
	try {
		init();
	}
	catch(...) {
		                                                                
		cleanup();
		throw;
	}
}

ProjectAdministrationImpl::~ProjectAdministrationImpl(void) {
	cleanup();
}

void ProjectAdministrationImpl::init() {
	DbConnection& dbConn = DbConnectionHolder::GetDbConnection();
	m_pGuidToIdMapper = new DbGuidToIdMapper(&dbConn);
	m_pAutoRollbackTransaction = new AutoRollbackTransaction(&dbConn);
}

void ProjectAdministrationImpl::cleanup() {
	try {
		if ( m_pGuidToIdMapper ) {
			delete m_pGuidToIdMapper;
			m_pGuidToIdMapper = 0;
		}
		if ( m_pAutoRollbackTransaction ) {
			delete m_pAutoRollbackTransaction;
			m_pAutoRollbackTransaction = 0;
		}
	}
	catch(...) {
		                         
	}
}

void ProjectAdministrationImpl::checkAdminUser() {
	if ( !m_UserInfo.IsAdministrator() ) {
		throw CrException("Operation is not allowed. User '" + m_UserInfo.GetLogin() + "' is not an administrator.");
	}
}

namespace {
std::string utf8Name(DynamicDbRecordSet& rows,const char* field) {
 const std::string encoded=rows.GetFieldValue(field);if(encoded.empty())return "";
 size_t size=0;std::unique_ptr<unsigned char[]> bytes(konga::fromHexString(encoded,size));
 return std::string(reinterpret_cast<const char*>(bytes.get()),size);
}
std::vector<long> resolveScheduleItems(xsd__string_Array* input,int type,GuidToIdMapper& mapper) {
 int count=0;const xsd__string* entries=input?input->get(count):0;std::vector<long> ids;for(int i=0;entries && i<count;++i) {
  if(!entries[i] || !*entries[i])continue;const std::string guid(entries[i]);
  long id=mapper.GetId(guid,type);if(id==GuidToIdMapper::invalid_id)throw CrException("The selected schedule or operation has not been deployed.");
  if(std::find(ids.begin(),ids.end(),id)==ids.end())ids.push_back(id);
 }
 return ids;
}
}

FindScheduledOperationsResult* ProjectAdministrationImpl::FindScheduledOperations(xsd__string_Array* schedule_guids,
																				  xsd__string_Array* operation_guids,
																				  xsd__string iso8601StartTime,
																				  xsd__string iso8601EndTime)
{
	const PTIME start=iso8601StartTime && *iso8601StartTime?ParseTime(iso8601StartTime):(CrDateTime::GetCurrentTime)().get_ptime();
	const PTIME end=iso8601EndTime && *iso8601EndTime?ParseTime(iso8601EndTime):start+boost::posix_time::hours(24);
	if(start.is_special() || end.is_special() || end<start || end-start>boost::posix_time::hours(24*31))throw CrException("Select a valid scheduler time range of no more than 31 days.");
	DbConnection& db=this->GetDbConnection();
	const std::vector<long> schedules=resolveScheduleItems(schedule_guids,ScheduleEntityId,*m_pGuidToIdMapper);
	const std::vector<long> operations=resolveScheduleItems(operation_guids,OperationEntityId,*m_pGuidToIdMapper);
	UserAccess access(m_UserInfo,db);ItemProperties properties(&db);
	SmartDbPtr<DynamicDbRecordSet> rows(DynamicDbRecordSet::MakeInstance(&db,"SELECT v.operation_id,v.schedule_id,go.guid AS operation_guid,gs.guid AS schedule_guid,encode(convert_to(o.operation_name,'UTF8'),'hex') AS operation_name,encode(convert_to(v.schedule_name,'UTF8'),'hex') AS schedule_name FROM scheduledoperationview v JOIN operationstab o ON o.operation_id=v.operation_id JOIN guidsforoperationstab go ON go.item_id=v.operation_id JOIN guidsforschedulestab gs ON gs.item_id=v.schedule_id ORDER BY v.schedule_id,v.operation_id"));
	rows->Open();std::map<long,std::vector<PTIME>> forecast;std::vector<ScheduledOperation*> resultRows;AutoPtrDeleteVector<ScheduledOperation> cleanupRows(resultRows);size_t total=0;
	while(rows->Next()) {
	 const long operationId=toLong(rows->GetFieldValue("operation_id")),scheduleId=toLong(rows->GetFieldValue("schedule_id"));
	 if(!schedules.empty() && std::find(schedules.begin(),schedules.end(),scheduleId)==schedules.end())continue;
	 if(!operations.empty() && std::find(operations.begin(),operations.end(),operationId)==operations.end())continue;
	 if(!access.HasReadPermission(operationId,OperationEntityId) || !access.HasReadPermission(scheduleId,ScheduleEntityId))continue;
	 KeyValues scheduleProperties,operationProperties;properties.GetItemProperties(ScheduleEntityId,scheduleId,scheduleProperties);properties.GetItemProperties(OperationEntityId,operationId,operationProperties);
	 if(!scheduleProperties.GetBool(SCHEDULE_ENABLED,true) || !operationProperties.GetBool(OPERATION_SCHEDULE_ENABLED,true))continue;
	 auto found=forecast.find(scheduleId);
	 if(found==forecast.end()) {
	  std::vector<PTIME> times;ScheduledOp schedule(start,scheduleId,&db,0);
	  while(!schedule.getScheduledDateTime().is_special() && schedule.getScheduledDateTime()<=end) {
	   const PTIME time=schedule.getScheduledDateTime();if(time<start)throw CrException("Scheduler returned a time before the requested range.");
	   if(times.size()>=100000)throw CrException("Too many scheduled runs; select a shorter time range.");times.push_back(time);
	   if(!schedule.testOp(time))break;
	   if(!schedule.getScheduledDateTime().is_special() && schedule.getScheduledDateTime()<=time)throw CrException("Scheduler did not advance to its next execution time.");
	  }
	  found=forecast.emplace(scheduleId,std::move(times)).first;
	 }
	 if(found->second.empty())continue;
	 total+=found->second.size();if(total>100000)throw CrException("Too many scheduled runs; select a shorter time range.");
	 std::unique_ptr<ScheduledOperation> row(new ScheduledOperation());row->setScheduleGuid(const_cast<char*>(rows->GetFieldValue("schedule_guid").c_str()),true);row->setOperationGuid(const_cast<char*>(rows->GetFieldValue("operation_guid").c_str()),true);
	 row->setScheduleName(const_cast<char*>(utf8Name(*rows,"schedule_name").c_str()),true);row->setOperationName(const_cast<char*>(utf8Name(*rows,"operation_name").c_str()),true);
	 row->setISO8601ExecutionTimes(AllocateArrayOfISO8601ExecutionTimes(found->second));resultRows.push_back(row.release());
	}
	std::unique_ptr<FindScheduledOperationsResult> result(new FindScheduledOperationsResult());result->setScheduledOperations(allocate_ArrayOfT<ScheduledOperation,ScheduledOperation_Array,ArrayOfScheduledOperation>(resultRows));return result.release();
}

void ProjectAdministrationImpl::SetScheduleEnabledProperty(xsd__string_Array* schedules, const bool bEnabled) {
	checkAdminUser();const auto ids=resolveScheduleItems(schedules,ScheduleEntityId,*m_pGuidToIdMapper);konga::SetSchedulesEnabled(ids,bEnabled,this->GetDbConnection());
}

void ProjectAdministrationImpl::SetOperationScheduleEnabledProperty(xsd__string_Array* operations, const bool bEnabled) {
	checkAdminUser();const auto ids=resolveScheduleItems(operations,OperationEntityId,*m_pGuidToIdMapper);if(ids.empty())return;
	DbConnection& db=this->GetDbConnection();etlforge::audit::Database auditDatabase(db.GetConnectionHandle());
	std::string list;for(long id:ids){if(!list.empty())list+=',';list+=std::to_string(id);}
	const auto projects=auditDatabase.query("SELECT DISTINCT g.guid,p.interchangeproject_name FROM interchangeprojectoperationstab x JOIN interchangeprojectstab p USING(interchangeproject_id) JOIN guidsforinterchangeprojectstab g ON g.item_id=x.interchangeproject_id WHERE x.item_id IN ("+list+") ORDER BY g.guid");
	std::vector<std::unique_ptr<etlforge::audit::DeploymentCapture>> captures;
	for(const auto& project:projects)captures.emplace_back(new etlforge::audit::DeploymentCapture(auditDatabase,*project[0]));
	ItemProperties properties(&db);bool changed=false;for(long id:ids)if(properties.Update(OperationEntityId,id,OPERATION_SCHEDULE_ENABLED,bEnabled?"1":"0"))changed=true;
	for(size_t i=0;i<captures.size();++i)captures[i]->complete(m_UserInfo.GetId(),m_UserInfo.GetLogin(),*projects[i][1]);
	m_pAutoRollbackTransaction->Commit();
	if(changed){try{konga::UpdateScheduler();}catch(const CrException&){}}
}

void ProjectAdministrationImpl::DisableSchedules(xsd__string_Array* schedules) {
	SetScheduleEnabledProperty(schedules,false);
}

void ProjectAdministrationImpl::EnableSchedules(xsd__string_Array* schedules) {
	SetScheduleEnabledProperty(schedules,true);
}

void ProjectAdministrationImpl::DisableScheduleForOperation(xsd__string_Array* operations) {
	SetOperationScheduleEnabledProperty(operations,false);
}

void ProjectAdministrationImpl::EnableScheduleForOperation(xsd__string_Array* operations) {
	SetOperationScheduleEnabledProperty(operations,true);
}

