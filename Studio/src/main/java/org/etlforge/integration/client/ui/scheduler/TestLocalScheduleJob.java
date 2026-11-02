package org.etlforge.integration.client.ui.scheduler;

import org.etlforge.integration.client.scheduler.server.TestScheduleCallback;
import org.etlforge.integration.client.scheduler.server.TestScheduleProvider;
import org.etlforge.integration.data.entity.Schedule;

public class TestLocalScheduleJob extends AbstractTestScheduleJob {
   public TestLocalScheduleJob(Schedule var1, TestScheduleProvider var2) {
      super(var1, var2);
   }

   @Override
   protected void callServer(Schedule var1, TestScheduleProvider var2, TestScheduleCallback var3, int var4) {
      var2.testSchedule(var1, var4, var3);
   }
}
