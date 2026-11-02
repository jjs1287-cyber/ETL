package org.etlforge.integration.client.ui.project.copy;

import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

final class DeepCopyUndoer {
   private final DeepCopyResult result;
   private WaitService waitService = WaitService.NONE;

   public DeepCopyUndoer(DeepCopyResult var1) {
      this.result = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void undo() {
      DeepCopyUndoer.Worker var1 = new DeepCopyUndoer.Worker();
      var1.execute();
   }

   private final class Worker extends KongaSwingWorker<Void, Void> {
      private volatile WaitLock waitLock;

      private Worker() {
      }

      protected Void doInBackground() throws Exception {
         this.waitLock = DeepCopyUndoer.this.waitService.startWait();
         DeepCopyUndoer.this.result.undo();
         return null;
      }

      @Override
      protected void finished() {
         this.waitLock.release();
      }

      protected void onSuccess(Void var1) {
         Alert.info("The copies have been deleted from the project.", "Copy Undone");
      }
   }
}
