package org.etlforge.integration.client.ui.admin.util;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.client.ui.operation.jobcontrol.RunAllScheduledOperationsCallback;

public class AllScheduledOperationsRunner {
   private final ApplicationWindow window;

   public AllScheduledOperationsRunner(ApplicationWindow var1) {
      this.window = var1;
   }

   public void start() {
      AllScheduledOperationsRunner.Submitter var1 = new AllScheduledOperationsRunner.Submitter();
      var1.submit();
   }

   private class Submitter extends UiJob {
      private final RunAllScheduledOperationsCallback callback = new AllScheduledOperationsRunner.Submitter.Callback();

      public Submitter() {
         super("Start all scheduled operations");
         this.setUncaughtExceptionHandler(this.callback);
         this.setBlockUntilDone(true);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         this.setMessage("Sending the request to the server");
         IntegrationServer var1 = IntegrationServer.getInstance();
         JobControl var2 = var1.getServerCall(JobControl.class);
         var2.runAllScheduledOperations(this.callback);
      }

      @Override
      protected void onJobCancelled() {
         this.callback.cancelled();
      }

      private class Callback implements RunAllScheduledOperationsCallback {
         private Callback() {
         }

         @Override
         public void done() {
            Submitter.this.setMessage(UiJobMessages.DONE);
            Alert.info(AllScheduledOperationsRunner.this.window.getFrame(), "The operations have been submitted to the queue.", "Done");
         }

         @Override
         public void cancelled() {
            Submitter.this.setMessage(UiJobMessages.CANCELLED);
         }

         @Override
         public void caught(Throwable var1) {
            Submitter.this.setStatus(JobStatus.ERROR);
            Submitter.this.setMessage(UiJobMessages.ERROR);
            ErrorLog.attention(AllScheduledOperationsRunner.class, "Failed to submit the operations to the queue.", var1);
         }
      }
   }
}
