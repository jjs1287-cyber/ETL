package org.etlforge.integration.activity.ui.files;

import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class ViewResultFileWorker {
   private final Callable<ResultFileSummary> producer;
   private final Receiver<ResultFileSummary> publisher;

   public ViewResultFileWorker(Callable<ResultFileSummary> var1, Receiver<ResultFileSummary> var2) {
      this.producer = var1;
      this.publisher = new ViewResultFileWorker.NullHandlingResultPublisher(var2);
   }

   public void executeWithWaitService(WaitService var1) {
      Computation var2 = Computation.create(this.producer, this.publisher);
      var2.swingWorkerBuilder().withWaitService(var1).withErrorLog(ErrorLevel.ATTENTION, this.getClass()).buildAndExecute();
   }

   private static class NullHandlingResultPublisher implements Receiver<ResultFileSummary> {
      private final Receiver<ResultFileSummary> impl;

      public NullHandlingResultPublisher(Receiver<ResultFileSummary> var1) {
         this.impl = var1;
      }

      public void handle(ResultFileSummary var1) {
         if (var1 != null) {
            this.impl.handle(var1);
         }
      }
   }
}
