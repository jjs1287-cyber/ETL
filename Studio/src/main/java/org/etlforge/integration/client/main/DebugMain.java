package org.etlforge.integration.client.main;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.RepaintManager;
import org.etlforge.application.DebugMode;
import org.etlforge.integration.client.ClientApplicationProperties;
import org.etlforge.integration.client.runtime.lock.SocketLock;
import org.etlforge.ui.CheckThreadViolationRepaintManager;
import org.etlforge.util.kongaprotocol.KongaCommand;
import org.etlforge.util.kongaprotocol.KongaRequest;
import org.etlforge.util.kongaprotocol.KongaRequestSocketSender;
import org.etlforge.util.net.KongaNetUtils;
import org.etlforge.util.properties.ReadOnlyProperties;

public final class DebugMain {
   private static final int PORT;
   private ServerSocket socket;

   public static void main(String[] var0) {
      DebugMain var1 = new DebugMain();
      LookAndFeelInstaller.install();
      if (DebugMode.IS_ENABLED) {
         System.setProperty("UseSocketLock", String.valueOf(Boolean.TRUE));
      }

      if (var1.acquireLock()) {
         var1.releaseLock();
      } else {
         var1.closeRunningInstance();
      }

      var1.installCheckThreadViolationRepaintManager();
      IntegrationMain.main(var0);
   }

   private boolean acquireLock() {
      try {
         this.socket = new ServerSocket(PORT);
         return true;
      } catch (IOException var2) {
         return false;
      }
   }

   private void releaseLock() {
      if (this.socket != null) {
         try {
            this.socket.close();
         } catch (IOException var5) {
            var5.printStackTrace();
         } finally {
            this.socket = null;
         }
      }
   }

   private void closeRunningInstance() {
      Socket var1 = null;

      try {
         var1 = new Socket("localhost", PORT);
         KongaRequest var2 = this.createExitRequest();
         new KongaRequestSocketSender(var1).send(var2);
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         KongaNetUtils.close(var1);
      }

      try {
         Thread.sleep(250L);
      } catch (InterruptedException var7) {
         Thread.currentThread().interrupt();
         var7.printStackTrace();
      }
   }

   private KongaRequest createExitRequest() {
      KongaRequest var1 = new KongaRequest(SocketLock.MODULE_NAME, "", "");
      var1.addCommand(new KongaCommand("exit"));
      return var1;
   }

   private void installCheckThreadViolationRepaintManager() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CheckThreadViolationRepaintManager var1 = new CheckThreadViolationRepaintManager();
            RepaintManager.setCurrentManager(var1);
         }
      });
   }

   private DebugMain() {
   }

   static {
      DebugSystemPropertiesLoader.load();
      ReadOnlyProperties var1 = ClientApplicationProperties.getInstance();
      PORT = var1.getInt("PORT_NUMBER", 37703);
   }
}
