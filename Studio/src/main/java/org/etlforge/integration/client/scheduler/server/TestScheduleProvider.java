package org.etlforge.integration.client.scheduler.server;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;

public interface TestScheduleProvider extends ServerCall {
   void testSchedule(ScheduleId var1, int var2, TestScheduleCallback var3);

   void testSchedule(Schedule var1, int var2, TestScheduleCallback var3);
}
