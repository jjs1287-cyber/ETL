package org.etlforge.integration.activity.ui.monitor;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.activity.client.progress.OperationProgressMessage;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressCallback;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressProvider;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressRequest;
import org.etlforge.integration.activity.ui.progress.ProgressSnapshotUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

final class ProgressTracker implements UiProvider {
   private final OperationProgressProvider provider;
   private final ProgressSnapshotUi snapshotUi;

   public ProgressTracker(OperationProgressProvider var1) {
      this.provider = var1;
      this.snapshotUi = new ProgressSnapshotUi();
   }

   public void update(long var1) {
      OperationProgressRequest var3 = new OperationProgressRequest();
      var3.opInstanceId = var1;
      this.provider.getOperationProgress(var3, new ProgressTracker.RefreshCallback());
   }

   public void setVisible(final boolean var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressTracker.this.snapshotUi.setVisible(var1);
         }
      });
   }

   public void stop() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressTracker.this.snapshotUi.stop();
         }
      });
   }

   public void reset() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressTracker.this.snapshotUi.update(null);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.snapshotUi.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   private class RefreshCallback implements OperationProgressCallback {
      private RefreshCallback() {
      }

      @Override
      public void handleProgressMessages(List<OperationProgressMessage> var1) {
         if (var1.size() == 1) {
            OperationProgressMessage var2 = (OperationProgressMessage)var1.get(0);
            this.updateUi(var2);
         }
      }

      private void updateUi(final OperationProgressMessage var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ProgressTracker.this.snapshotUi.update(var1.getSnapshot());
            }
         });
      }

      @Override
      public void cancelled() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ProgressTracker.this.snapshotUi.update(null);
            }
         });
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.log(ProgressTracker.class, null, var1);
      }
   }
}
