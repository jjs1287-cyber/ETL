/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/date.h>
#include <konga/Utils/KeyValues.h>

#include <konga/Db/TranDbUtils/TranDbUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	           

#include "ProjectAdministrationImpl.h"

using namespace konga;
using std::vector;

namespace {

	PTIME* getPtimeNow() {
		const CrDateTime& cr = (CrDateTime::GetCurrentTime)();
		return new PTIME(cr.get_ptime());
	}

	void CheckTestScheduleInput(std::auto_ptr<PTIME>& pStartTime, std::auto_ptr<PTIME>& pEndTime,
		xsd__string iso8601StartTime, xsd__string iso8601EndTime)
	{
		if ( iso8601StartTime == 0 || *iso8601StartTime == 0 ) {
			pStartTime.reset( getPtimeNow() );
		}
		else {
			pStartTime.reset( new PTIME(ProjectAdministrationImpl::ParseTime(iso8601StartTime)) );
		}

		if ( iso8601EndTime == 0 || *iso8601EndTime == 0 ) {
			pEndTime.reset( 0 );
		}
		else {
			pEndTime.reset( new PTIME(ProjectAdministrationImpl::ParseTime(iso8601EndTime)) );
		}
	}

	void ParseKeyValues(KeyValues& keyValues, WsKeyValuePairs& properties) {
		ArrayOf_tns2_WsKeyValue* pArrayOf_tns2_WsKeyValue = properties.getkeyValues();
		if ( pArrayOf_tns2_WsKeyValue == 0 ) return;
		
		WsKeyValue_Array* pWsKeyValue_Array = pArrayOf_tns2_WsKeyValue->getitem();
		if ( pWsKeyValue_Array == 0 ) return;

		int size = 0;
		WsKeyValue** arr = pWsKeyValue_Array->get(size);
		if ( arr == 0 || size == 0 ) return;

		for( int i = 0; i < size; ++i ) {
			WsKeyValue* pKeyValue = arr[i];
			if ( pKeyValue == 0 ) continue;
			const string& key = toString( pKeyValue->getkey() );
			if ( key.empty() ) continue;
			const string& value = toString( pKeyValue->getvalue() );
			keyValues.AddString(key, value);
		}
	}

	                                               
	void FilterPtimeList(vector<PTIME>& start_times, PTIME* pEndTime) {
		if ( pEndTime == 0 ) {
			return;
		}
		else {
			vector<PTIME> tmp;
			for( vector<PTIME>::const_iterator cit = start_times.begin(); cit != start_times.end(); ++cit ) {
				if ( *cit > *pEndTime )
					break;
				tmp.push_back(*cit);
			}
			start_times = tmp;
		}
	}

	FindScheduledOperationsResult* AllocateFindScheduledOperationsResult(const vector<PTIME>& start_times,
		const string& scheduleGuid, const string& scheduleName)
	{
		const int size = 1;

		ScheduledOperation** arrScheduledOperation = allocate_pointer_array<ScheduledOperation>(size);
		AutoDeletePointerArray<ScheduledOperation> adopt_arrScheduledOperation(arrScheduledOperation, size);

		std::auto_ptr<ScheduledOperation> pScheduledOperation(new ScheduledOperation());
		pScheduledOperation->setScheduleGuid(const_cast<char*>(scheduleGuid.c_str()), true);
		pScheduledOperation->setScheduleName(const_cast<char*>(scheduleName.c_str()), true);
		pScheduledOperation->setISO8601ExecutionTimes( ProjectAdministrationImpl::AllocateArrayOfISO8601ExecutionTimes(start_times) );	                         
		arrScheduledOperation[0] = pScheduledOperation.release();	                          

		std::auto_ptr<ScheduledOperation_Array> pScheduledOperation_Array( new ScheduledOperation_Array() );
		pScheduledOperation_Array->set(arrScheduledOperation, size);

		std::auto_ptr<ArrayOfScheduledOperation> pArrayOfScheduledOperation( new ArrayOfScheduledOperation() );
		pArrayOfScheduledOperation->setitem(pScheduledOperation_Array.get());	                                  

		std::auto_ptr<FindScheduledOperationsResult> retVal( new FindScheduledOperationsResult() );
		retVal->setScheduledOperations(pArrayOfScheduledOperation.release());	                          
		return retVal.release();
	}

	vector<string> GetDateTimeStringVector(const vector<PTIME>& v) {
		vector<string> retVal;
		for( vector<PTIME>::const_iterator cit = v.begin(); cit != v.end(); ++cit ) {
			CrDateTime crdt(*cit);
			retVal.push_back( CVTDate(crdt, "yyyy-mm-dd HH:MM:SS") );
		}
		return retVal;
	}

}                       

PTIME ProjectAdministrationImpl::ParseTime(const char* str) {
	string stErrMsg;
	CrDateTime crt = konga::GetDate(str, stErrMsg);
	if ( !stErrMsg.empty() ) throw InternalErrorException("Failed to parse time format '" + string(str)
		+ ". Reason: " + stErrMsg);
	return crt.get_ptime();
}

ArrayOf_xsd_string* ProjectAdministrationImpl::AllocateArrayOfISO8601ExecutionTimes(const vector<PTIME>& v) {
	const int size = v.size();
	if ( size == 0 ) return new ArrayOf_xsd_string();

	std::auto_ptr<xsd__string_Array> pxsd__string_Array( konga::allocate_xsd__string_Array(GetDateTimeStringVector(v)) );

	std::auto_ptr<ArrayOf_xsd_string> pArrayOf_xsd_string( new ArrayOf_xsd_string() );

	pArrayOf_xsd_string->setitem(pxsd__string_Array.get());	                                  
	return pArrayOf_xsd_string.release();
}

FindScheduledOperationsResult* ProjectAdministrationImpl::TestSchedule(WsKeyValuePairs* scheduleProperties,
																	   xsd__string iso8601StartTime,
																	   xsd__string iso8601EndTime,
																	   xsd__int maxInstances)
{
	if ( scheduleProperties == 0 ) throw InternalErrorException("Schedule Properties is null.");
	if ( maxInstances < 1 ) throw InternalErrorException("maxInstances has to be a positive number.");

	                                                        
	std::auto_ptr<PTIME> pStartTime(0), pEndTime(0);
	CheckTestScheduleInput(pStartTime, pEndTime, iso8601StartTime, iso8601EndTime);

	KeyValues properties;
	ParseKeyValues(properties, *scheduleProperties);

	vector<PTIME> start_times;
	konga::QueueTimeList(start_times, *pStartTime, properties, maxInstances);

	FilterPtimeList(start_times, pEndTime.get());

	const string& name = properties.GetString("schedule_name", "");
	const string& guid = properties.GetString("schedule_id", "");

	return AllocateFindScheduledOperationsResult(start_times, guid, name);
}

FindScheduledOperationsResult* ProjectAdministrationImpl::TestSchedule(xsd__string scheduleGuid,
																	   xsd__string iso8601StartTime,
																	   xsd__string iso8601EndTime,
																	   xsd__int maxInstances)
{
	const string sScheduleGuid( toString(scheduleGuid) );
	if ( sScheduleGuid.empty() ) throw InternalErrorException("Schedule GUID is NULL or empty.");
	if ( maxInstances < 1 ) throw InternalErrorException("maxInstances has to be a positive number.");

	                                                        
	std::auto_ptr<PTIME> pStartTime(0), pEndTime(0);
	CheckTestScheduleInput(pStartTime, pEndTime, iso8601StartTime, iso8601EndTime);

	const long lScheduleId = m_pGuidToIdMapper->GetId(sScheduleGuid, ScheduleEntityId);
	if ( lScheduleId == GuidToIdMapper::invalid_id ) {
		throw CrException("The schedule has not been deployed to this server.");
	}

	                    
	const string& scheduleName = konga::getItemName(lScheduleId, ScheduleEntityId, this->GetDbConnection());
	UserAccess userAccess(m_UserInfo, this->GetDbConnection());
	if ( !userAccess.HasReadPermission(lScheduleId, ScheduleEntityId) ) {
		throw CrException("The current user does not have read permission for the schedule '" + scheduleName + "'.");
	}

	vector<PTIME> start_times;
	konga::QueueTimeList(start_times, *pStartTime, lScheduleId, maxInstances);
	FilterPtimeList(start_times, pEndTime.get());

	return AllocateFindScheduledOperationsResult(start_times, sScheduleGuid, scheduleName);
}

