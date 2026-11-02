package org.etlforge.application.ui.viewer.error.dialog;

import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

class DefaultErrorDisplayer extends ErrorDisplayer {
   private final ExecutorService threadPool;
   private final Semaphore displayLock = new Semaphore(1);

   DefaultErrorDisplayer() {
      this.threadPool = Executors.newFixedThreadPool(1);
   }

   @Override
   public void show(ErrorLogEntry var1) {
      this.showImpl(var1, null);
   }

   @Override
   public void showAndWait(ErrorLogEntry var1) throws InterruptedException {
      CountDownLatch var2 = new CountDownLatch(1);
      this.showImpl(var1, var2);
      var2.await();
   }

   private void showImpl(ErrorLogEntry var1, CountDownLatch var2) {
      DefaultErrorDisplayer.DialogDisplayer var3 = new DefaultErrorDisplayer.DialogDisplayer(var1, var2);
      this.threadPool.submit(var3);
   }

   private class DialogDisplayer implements Runnable {
      private final ErrorLogEntry entry;
      private final CountDownLatch waitLatch;

      public DialogDisplayer(ErrorLogEntry var2, CountDownLatch var3) {
         this.entry = var2;
         this.waitLatch = var3;
      }

      @Override
      public void run() {
         try {
            this.tryDefaultDisplayer();
         } catch (Throwable var2) {
            var2.printStackTrace();
            this.useBackupDisplayer();
         }
      }

      private void tryDefaultDisplayer() throws Exception {
         KongaDialog var1 = this.createDialog();

         try {
            DefaultErrorDisplayer.this.displayLock.acquire();
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            var3.printStackTrace();
         }

         this.showDialog(var1);
      }

      private void useBackupDisplayer() {
         ErrorDisplayer var1 = new MinimalErrorDisplayer();
         if (this.waitLatch != null) {
            try {
               var1.showAndWait(this.entry);
            } catch (InterruptedException var3) {
               Thread.currentThread().interrupt();
               this.waitLatch.countDown();
               var3.printStackTrace();
            }
         } else {
            var1.show(this.entry);
         }
      }

      private void showDialog(final KongaDialog var1) {
         Runnable var2 = new Runnable() {
            @Override
            public void run() {
               var1.setLocationRelativeTo(null);
               var1.setVisible(true);
               DefaultErrorDisplayer.this.displayLock.release();
            }
         };
         UiUtils.runOnEventThread(var2);
      }

      private KongaDialog createDialog() throws Exception {
         final KongaDialog[] var1 = new KongaDialog[1];
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               JFrame var1x = ApplicationUi.getAppWinFrame();
               var1[0] = new ErrorDialog(var1x, DialogDisplayer.this.entry) {
                  @Override
                  protected void onDialogClose() {
                     if (DialogDisplayer.this.waitLatch != null) {
                        DialogDisplayer.this.waitLatch.countDown();
                     }

                     super.onDialogClose();
                  }
               };
            }
         });
         return var1[0];
      }
   }
}

