/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UIBACKEND_PROJECT_ADMINISTRATION_IMPL_H
#define KONGA_UIBACKEND_PROJECT_ADMINISTRATION_IMPL_H

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include <konga/Scheduler/ScheduledOpCollector.h>

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../FindScheduledOperationsResult.hpp"
#include "../WsKeyValuePairs.hpp"

AXIS_CPP_NAMESPACE_USE

namespace konga {

	class GuidToIdMapper;
	class AutoRollbackTransaction;

	class ProjectAdministrationImpl : private DbConnectionHolder, public WsImplBaseAuthenticator {
	public:
		ProjectAdministrationImpl(const char* user, const char* password);
		virtual ~ProjectAdministrationImpl(void);

		                                              
		void DisableSchedules(xsd__string_Array* schedules);
		void EnableSchedules(xsd__string_Array* schedules);

		                                                              
		void DisableScheduleForOperation(xsd__string_Array* operations);
		void EnableScheduleForOperation(xsd__string_Array* operations);

		                                            
		FindScheduledOperationsResult* FindScheduledOperations(xsd__string_Array* schedules, xsd__string_Array* operations,
			xsd__string iso8601StartTime,xsd__string iso8601EndTime);

		            
		FindScheduledOperationsResult* TestSchedule(WsKeyValuePairs* scheduleProperties,
			xsd__string iso8601StartTime, xsd__string iso8601EndTime, xsd__int maxInstances);

		                                                                        
		FindScheduledOperationsResult* TestSchedule(xsd__string scheduleGuid,
			xsd__string iso8601StartTime, xsd__string iso8601EndTime, xsd__int maxInstances);

		static PTIME ParseTime(const char* str);
		static ArrayOf_xsd_string* AllocateArrayOfISO8601ExecutionTimes(const vector<PTIME>& v);

	private:
		void init();
		void cleanup();
		void checkAdminUser();

		void SetScheduleEnabledProperty(xsd__string_Array* schedules, const bool bEnabled);
		void SetOperationScheduleEnabledProperty(xsd__string_Array* operations, const bool bEnabled);

	private:
		GuidToIdMapper* m_pGuidToIdMapper;
		AutoRollbackTransaction* m_pAutoRollbackTransaction;
	};

}

#endif                                                 

