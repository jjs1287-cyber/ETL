package org.etlforge.integration.client.main;

import java.awt.EventQueue;
import java.awt.SplashScreen;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import org.etlforge.application.Application;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.integration.client.info.BuildProperties;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.runtime.StartupProgressMonitor;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.text.KongaTextArea;
import sun.awt.AppContext;

public final class IntegrationMain {
   public static void main(String[] var0) {
      StartupProgressMonitor var1 = StartupProgressMonitor.CONSOLE;

      try {
         Application.getStartupTime();
         BuildProperties.init();
         LookAndFeelInstaller.install();
         installPatchForSwingWorker();
         var1 = createStartupProgressMonitor();
         ClientPlatform.start(var1, var0);
      } catch (Throwable var3) {
         var1.publishMessage("Error");
         if (NonInteractiveMode.off()) {
            showStartupError(var3);
         }

         var3.printStackTrace();
      }
   }

   private static void installPatchForSwingWorker() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               AppContext.getAppContext().put(SwingWorker.class, Executors.newCachedThreadPool());
            } catch (Throwable var2) {
               var2.printStackTrace();
            }
         }
      });
   }

   private static StartupProgressMonitor createStartupProgressMonitor() {
      SplashScreen var0 = SplashScreen.getSplashScreen();
      return var0 == null ? StartupProgressMonitor.CONSOLE : new SplashscreenProgressMonitor(var0);
   }

   private static void showStartupError(final Throwable var0) {
      try {
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               KongaTextArea var1 = KongaTextArea.viewer(Size.rows(10), Size.columns(60));
               var1.setText(var0.getMessage() + "\n\n" + IntegrationMain.getStackTrace(var0));
               JOptionPane.showMessageDialog(null, new JScrollPane(var1), "Startup Error", 0);
            }
         });
      } catch (Exception var2) {
      }
   }

   private static String getStackTrace(Throwable var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      while (var2 < 5 && var0 != null) {
         if (var2 > 0) {
            var1.append("Caused by: ");
         }

         var1.append(ExceptionUtils.getStackTrace(var0));
         var2++;
         var0 = var0.getCause();
      }

      return var1.toString();
   }

   private IntegrationMain() {
   }
}
