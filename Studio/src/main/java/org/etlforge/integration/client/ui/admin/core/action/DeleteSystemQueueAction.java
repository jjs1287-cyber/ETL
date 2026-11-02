package org.etlforge.integration.client.ui.admin.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractApplicationWindowAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.activity.serverapi.queue.DeleteSystemQueueCallback;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.ui.admin.prefs.DeleteSystemQueueWarningPreference;

public final class DeleteSystemQueueAction extends AbstractApplicationWindowAction {
   private final OperationQueueProvider serverApi;

   public DeleteSystemQueueAction(ApplicationWindow var1, OperationQueueProvider var2) {
      super(var1, "admin.action.DeleteQueue", AdminActionDictionary.DICTIONARY);
      this.serverApi = var2;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteSystemQueueAction.this.submitDeleteRequest();
         }
      });
   }

   private void submitDeleteRequest() {
      if (this.isGreenlighted()) {
         DeleteSystemQueueAction.DeleteSystemQueueJob var1 = new DeleteSystemQueueAction.DeleteSystemQueueJob(this.serverApi);
         var1.setUseProgressDialog(0);
         var1.submit();
      }
   }

   private boolean isGreenlighted() {
      DoNotShowAgainQuestion var1 = new DoNotShowAgainQuestion(
         "Are you sure you want to delete all entries from the system queue?",
         "Are you sure?",
         DeleteSystemQueueWarningPreference.INSTANCE,
         Alert.Option.YES_NO,
         Alert.Option.YES,
         Alert.Type.WARNING
      );
      return var1.ask(null);
   }

   private static final class DeleteSystemQueueJob extends UiJob {
      private final OperationQueueProvider qProvider;
      private final DeleteSystemQueueAction.DeleteSystemQueueJob.CallbackImpl callback = new DeleteSystemQueueAction.DeleteSystemQueueJob.CallbackImpl();

      public DeleteSystemQueueJob(OperationQueueProvider var1) {
         super("Delete System Queue");
         this.qProvider = var1;
         this.setCanBeCancelled(false);
         this.setUncaughtExceptionHandler(this.callback);
         this.setMessage(UiJobMessages.WAITING);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         if (!this.isCancelled()) {
            this.setMessage("Sending the request to the server");
            Runnable var1 = new Runnable() {
               @Override
               public void run() {
                  DeleteSystemQueueJob.this.qProvider.deleteSystemQueue(DeleteSystemQueueJob.this.callback);
               }
            };
            ApplicationWorker var2 = Application.getWorker();
            var2.submitForParallelAndWait(var1, this.callback);
         }
      }

      private static final class CallbackImpl extends AbstractServerCallback implements DeleteSystemQueueCallback {
         private CallbackImpl() {
         }

         @Override
         public void systemQueueWasDeleted() {
         }
      }
   }
}
