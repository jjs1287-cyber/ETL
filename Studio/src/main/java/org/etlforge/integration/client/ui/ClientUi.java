package org.etlforge.integration.client.ui;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.application.Application;
import org.etlforge.application.ui.window.FullScreenSupport;
import org.etlforge.application.ui.window.NoFullScreenSupport;
import org.etlforge.integration.client.runtime.StartupProgressMonitor;
import org.etlforge.system.annotations.InternalMethod;

public final class ClientUi {
   private static volatile String startupView;
   private static volatile FullScreenSupport fullScreenSupport = new NoFullScreenSupport();

   public static void setFullScreenSupport(FullScreenSupport var0) {
      fullScreenSupport = Preconditions.checkNotNull(var0);
   }

   @InternalMethod
   public static void publishUi(StartupProgressMonitor var0) {
      var0.publishMessage("Preparing the main window");
      createAndDisplayMainWindow(var0);
   }

   private static void createAndDisplayMainWindow(final StartupProgressMonitor var0) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ClientUi.lookForStartupView();
            ApplicationWindowPublisher var1 = Application.getBean("ui.AppWin.Publisher", ApplicationWindowPublisher.class);
            var1.setFullScreenSupport(ClientUi.fullScreenSupport);
            var1.setStartupMonitor(var0);
            var1.wireUpApplicationWindow();
            var1.publishUi(ClientUi.startupView);
         }
      });
   }

   private static void lookForStartupView() {
      String var0 = "ui.StartupView";
      if (startupView == null && Application.containsBean(var0)) {
         startupView = Application.getBean(var0, String.class);
      }
   }

   @InternalMethod
   public static synchronized void setStartupView(String var0) {
      startupView = var0;
   }

   private ClientUi() {
   }
}
