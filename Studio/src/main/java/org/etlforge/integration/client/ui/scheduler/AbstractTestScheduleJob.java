package org.etlforge.integration.client.ui.scheduler;

import java.util.Date;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.integration.client.scheduler.server.TestScheduleCallback;
import org.etlforge.integration.client.scheduler.server.TestScheduleProvider;
import org.etlforge.integration.data.entity.Schedule;

abstract class AbstractTestScheduleJob extends ResultProducingUiJob<Date[]> {
   private final Schedule schedule;
   private final TestScheduleProvider provider;
   private final AbstractTestScheduleJob.Callback callback;
   private int numberOfRuns;

   protected AbstractTestScheduleJob(Schedule var1, TestScheduleProvider var2) {
      super("Test Schedule");
      this.schedule = var1;
      this.provider = var2;
      this.callback = new AbstractTestScheduleJob.Callback();
      this.numberOfRuns = 10;
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   public final void setNumberOfRuns(int var1) {
      this.numberOfRuns = var1;
   }

   @Override
   protected final void runImpl() throws InterruptedException {
      this.callServer(this.schedule, this.provider, this.callback, this.numberOfRuns);
   }

   protected abstract void callServer(Schedule var1, TestScheduleProvider var2, TestScheduleCallback var3, int var4);

   @Override
   protected final void onJobCancelled() {
      this.callback.cancelled();
   }

   private class Callback implements TestScheduleCallback {
      private Callback() {
      }

      @Override
      public void testCompleted(Date[] var1) {
         AbstractTestScheduleJob.this.resultWasComputed(var1);
      }

      @Override
      public void cancelled() {
         AbstractTestScheduleJob.this.resultWasComputed(null);
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(AbstractTestScheduleJob.this.getClass(), "An error occurred while testing the schedule: " + var1.getMessage(), var1);
         AbstractTestScheduleJob.this.resultWasComputed(null);
      }
   }
}
