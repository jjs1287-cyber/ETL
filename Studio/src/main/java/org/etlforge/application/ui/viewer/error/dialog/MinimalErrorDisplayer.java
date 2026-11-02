package org.etlforge.application.ui.viewer.error.dialog;

import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.text.KongaTextArea;

class MinimalErrorDisplayer extends ErrorDisplayer {
   @Override
   public void show(ErrorLogEntry var1) {
      this.showImpl(var1, false);
   }

   @Override
   public void showAndWait(ErrorLogEntry var1) {
      this.showImpl(var1, true);
   }

   private void showImpl(ErrorLogEntry var1, boolean var2) {
      final Object var3 = this.getErrorMessageObject(var1);
      if (EventQueue.isDispatchThread()) {
         this.displayMessage(var3);
      } else {
         final CountDownLatch var4 = new CountDownLatch(var2 ? 1 : 0);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MinimalErrorDisplayer.this.displayMessage(var3);
               var4.countDown();
            }
         });

         try {
            var4.await();
         } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
            var6.printStackTrace();
         }
      }
   }

   private Object getErrorMessageObject(ErrorLogEntry var1) {
      StringBuilder var2 = new StringBuilder(var1.message());
      var2.append("\n\n---Stacktrace---\n");
      var2.append(this.getStackTrace(var1.cause()));
      KongaTextArea var3 = KongaTextArea.viewer(var2.toString(), Size.rows(30), Size.columns(80));
      return new JScrollPane(var3);
   }

   private String getStackTrace(Throwable var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while (var3 < 5 && var1 != null) {
         if (var3 > 0) {
            var2.append("Caused by: ");
         }

         var2.append(ExceptionUtils.getStackTrace(var1));
         var3++;
         var1 = var1.getCause();
      }

      return var2.toString();
   }

   private void displayMessage(Object var1) {
      JOptionPane.showMessageDialog(null, var1, "Fatal Error", 0);
   }
}
