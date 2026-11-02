package org.etlforge.integration.client.scheduler.server;

import java.util.Calendar;
import java.util.Date;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;

public class MockTestScheduleProvider implements TestScheduleProvider {
   @Override
   public void testSchedule(ScheduleId var1, int var2, TestScheduleCallback var3) {
      this.run(var2, var3);
   }

   @Override
   public void testSchedule(Schedule var1, int var2, TestScheduleCallback var3) {
      this.run(var2, var3);
   }

   private void run(int var1, TestScheduleCallback var2) {
      if (Math.random() < 0.1) {
         var2.caught(new Exception("Mock error to test error handling"));
      } else {
         Calendar var3 = Calendar.getInstance();
         Date[] var4 = new Date[var1];

         for (int var5 = 0; var5 < var1; var5++) {
            var4[var5] = var3.getTime();
            var3.add(6, 1);
         }

         var2.testCompleted(var4);
      }
   }
}
