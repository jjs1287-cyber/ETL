package org.etlforge.integration.client.ui.project.copy;

import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

final class DeepCopyReplacer {
   private final DeepCopyResult result;
   private volatile WaitService waitService = WaitService.NONE;

   public DeepCopyReplacer(DeepCopyResult var1) {
      this.result = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void replaceOriginals() {
      Computation var1 = this.createJob();
      var1.swingWorkerBuilder().withWaitService(this.waitService).withErrorLog(ErrorLevel.ATTENTION, this.getClass()).buildAndExecute();
   }

   private Computation<Boolean> createJob() {
      Callable var1 = new Callable<Boolean>() {
         public Boolean call() throws Exception {
            return DeepCopyReplacer.this.result.replaceOriginalItems();
         }
      };
      Receiver var2 = new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            if (var1) {
               Alert.info("The original items have been replaced by their copies.", "Original Items Replaced");
            } else {
               Alert.warning("The original items have not been deleted.", "No Replacement Made");
            }
         }
      };
      return Computation.create(var1, var2);
   }
}
