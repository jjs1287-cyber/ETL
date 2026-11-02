package org.etlforge.integration.client.runtime.lock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.ClientApplicationProperties;
import org.etlforge.integration.client.runtime.FileInputArgumentHandler;
import org.etlforge.util.kongaprotocol.KongaCommand;
import org.etlforge.util.kongaprotocol.KongaCommandSocketListener;
import org.etlforge.util.kongaprotocol.KongaProtocolCommandAuthenticator;
import org.etlforge.util.kongaprotocol.KongaRequest;
import org.etlforge.util.kongaprotocol.KongaRequestSocketSender;
import org.etlforge.util.net.Command;
import org.etlforge.util.net.CommandHandler;
import org.etlforge.util.net.CommandResult;
import org.etlforge.util.net.KongaNetUtils;
import org.etlforge.util.properties.ReadOnlyProperties;

public final class SocketLock implements ApplicationInstanceLock {
   private static final int PORT = readPort();
   static final String TO_FRONT = "to_front";
   public static final String EXIT = "exit";
   public static final String MODULE_NAME = ApplicationConfiguration.getProductName() + " - Application Lock";
   private ServerSocket serverSocket;
   private KongaCommandSocketListener listener;
   private FileInputArgumentHandler fileInputHandler;

   private static int readPort() {
      ReadOnlyProperties var1 = ClientApplicationProperties.getInstance();
      return var1.getInt("PORT_NUMBER", 37703);
   }

   public void setFileInputHandler(FileInputArgumentHandler var1) {
      this.fileInputHandler = var1;
   }

   @Override
   public boolean lock(String[] var1) {
      if (this.serverSocket != null) {
         return true;
      }

      try {
         this.createServerSocket();
         this.listen();
         return true;
      } catch (IOException var3) {
         var3.printStackTrace();
         this.notifyRunningInstance(var1);
         return false;
      }
   }

   private boolean notifyRunningInstance(String[] var1) {
      Socket var2 = null;

      try {
         var2 = new Socket("localhost", PORT);
         KongaRequest var3 = new KongaRequest(MODULE_NAME, "", "");
         var3.addCommand(new KongaCommand("to_front", var1));
         new KongaRequestSocketSender(var2).send(var3);
         return true;
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         KongaNetUtils.close(var2);
      }

      return false;
   }

   private void listen() {
      if (this.listener == null) {
         if (this.serverSocket == null) {
            throw new IllegalStateException("The lock must be acquired first.");
         }

         this.listener = this.createListener();

         try {
            this.listener.startListening();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   private KongaCommandSocketListener createListener() {
      KongaCommandSocketListener var1 = new KongaCommandSocketListener(this.serverSocket, MODULE_NAME);
      var1.registerCommandAuthenticator(new KongaProtocolCommandAuthenticator() {
         @Override
         public boolean isValidUser(String var1, String var2) {
            return true;
         }
      });
      RunningInstanceDispatcher var2 = new RunningInstanceDispatcher(this.fileInputHandler);
      var1.registerCommandHandler("to_front", var2);
      var1.registerCommandHandler("exit", new CommandHandler() {
         @Override
         public CommandResult handleCommand(Command var1) {
            System.exit(0);
            return null;
         }
      });
      var1.setThreadPriority(1);
      return var1;
   }

   private void createServerSocket() throws IOException {
      this.serverSocket = new ServerSocket(PORT);
      if (this.socketShouldBeClosedExplicitlyOnExit()) {
         Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
               SocketLock.this.closeServerSocket();
            }
         }));
      }
   }

   private boolean socketShouldBeClosedExplicitlyOnExit() {
      return Boolean.getBoolean("CloseServerSocketOnExit");
   }

   private void closeServerSocket() {
      try {
         this.serverSocket.close();
         this.serverSocket = null;
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }
}
