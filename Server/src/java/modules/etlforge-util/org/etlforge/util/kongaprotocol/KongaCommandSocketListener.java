package org.etlforge.util.kongaprotocol;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.net.Command;
import org.etlforge.util.net.CommandHandler;
import org.etlforge.util.net.CommandListener;
import org.etlforge.util.net.CommandResult;
import org.etlforge.util.net.KongaNetUtils;

public final class KongaCommandSocketListener implements CommandListener {
   private final int listenPort;
   private final String moduleName;
   private Thread listenerThread;
   private int threadPriority;
   private ServerSocket serverSocket;
   private KongaCommandSocketListener.SocketListener socketListener;
   private final Map<String, CommandHandler> commandHandlers;
   private CommandHandler unknownCommandHandler;
   private KongaProtocolCommandAuthenticator authenticator;

   public KongaCommandSocketListener(int var1, String var2) {
      this.listenPort = var1;
      this.moduleName = var2;
      this.commandHandlers = Maps.newHashMap();
      this.threadPriority = -1;
   }

   public KongaCommandSocketListener(ServerSocket var1, String var2) {
      this(var1.getLocalPort(), var2);
      this.serverSocket = var1;
   }

   public void registerCommandAuthenticator(KongaProtocolCommandAuthenticator var1) {
      this.authenticator = var1;
   }

   @Override
   public synchronized void registerCommandHandler(String var1, CommandHandler var2) {
      Preconditions.checkNotNull(var1, "commaneName");
      if (var2 == null) {
         if (this.commandHandlers.containsKey(var1)) {
            this.commandHandlers.remove(var1);
         }
      } else {
         this.commandHandlers.put(var1, var2);
      }
   }

   public synchronized void registerUnknownCommandHandler(CommandHandler var1) {
      this.unknownCommandHandler = var1;
   }

   public synchronized void setThreadPriority(int var1) {
      this.threadPriority = var1;
      if (this.listenerThread != null) {
         this.listenerThread.setPriority(var1);
      }
   }

   @Override
   public synchronized void startListening() throws IOException {
      if (this.listenerThread == null) {
         if (this.serverSocket == null) {
            this.serverSocket = new ServerSocket(this.listenPort);
         }

         try {
            this.socketListener = new KongaCommandSocketListener.SocketListener();
            this.listenerThread = new Thread(this.socketListener, this.moduleName + " command listener");
            this.listenerThread.setDaemon(true);
            if (this.threadPriority >= 0) {
               this.listenerThread.setPriority(this.threadPriority);
            }

            this.listenerThread.start();
         } catch (RuntimeException var2) {
            if (this.socketListener != null) {
               this.socketListener.stopListening();
               this.serverSocket.close();
               this.serverSocket = null;
               this.socketListener = null;
            }

            this.listenerThread = null;
            var2.printStackTrace();
            throw var2;
         }
      }
   }

   @Override
   public synchronized void stopListening() throws IOException {
      if (this.listenerThread != null) {
         try {
            this.socketListener.stopListening();
            this.listenerThread.interrupt();
            this.serverSocket.close();
            this.listenerThread.join();
         } catch (InterruptedException var7) {
            Thread.currentThread().interrupt();
         } catch (SecurityException var8) {
            System.err.println("Failed to interrupt listener thread: " + var8.getMessage());
            var8.printStackTrace(System.err);
            throw var8;
         } catch (RuntimeException var9) {
            System.err.println("Unexcepted error: " + var9.getMessage());
            var9.printStackTrace(System.err);
            throw var9;
         } finally {
            this.serverSocket = null;
            this.listenerThread = null;
            this.socketListener = null;
         }
      }
   }

   private class SocketListener implements Runnable {
      private volatile boolean shouldStopListening;

      private SocketListener() {
      }

      private void authenticateRequest(KongaRequest var1) throws KongaProtocolException {
         if (KongaCommandSocketListener.this.authenticator == null) {
            throw new KongaProtocolException("No request authenticator has been registered.");
         }

         if (!KongaCommandSocketListener.this.authenticator.isValidUser(var1.getUserName(), var1.getPassword())) {
            throw new KongaProtocolException("Failed to authenticate user \"" + var1.getUserName() + "\".");
         }
      }

      private KongaResponse handleRequest(KongaRequest var1) throws KongaProtocolException {
         if (var1 == null) {
            throw new NullPointerException("request");
         }

         Preconditions.checkNotNull(var1, "request");
         this.authenticateRequest(var1);
         KongaResponse var2 = new KongaResponse(KongaCommandSocketListener.this.moduleName);

         for (Command var4 : var1) {
            String var5 = var4.getCommandName();

            try {
               if (KongaCommandSocketListener.this.commandHandlers.containsKey(var5)) {
                  CommandHandler var6 = KongaCommandSocketListener.this.commandHandlers.get(var5);
                  CommandResult var7 = var6.handleCommand(var4);
                  var2.addCommandResult(var7);
               } else if (KongaCommandSocketListener.this.unknownCommandHandler != null) {
                  CommandResult var9 = KongaCommandSocketListener.this.unknownCommandHandler.handleCommand(var4);
                  var2.addCommandResult(var9);
               } else {
                  System.err.println("Received unknown command \"" + var5 + "\"");
               }
            } catch (RuntimeException var8) {
               throw new KongaProtocolException("A command handler threw a runtime exception. " + var8.getMessage(), var8);
            }
         }

         return var2;
      }

      void stopListening() {
         this.shouldStopListening = true;
      }

      private void listen() {
         while (!this.shouldStopListening) {
            try {
               Socket var1 = null;
               InputStream var2 = null;
               OutputStream var3 = null;

               try {
                  var1 = KongaCommandSocketListener.this.serverSocket.accept();
                  var2 = var1.getInputStream();
                  KongaRequest var4 = KongaProtocolUtils.deserializeKongaRequest(var2);
                  KongaResponse var5 = this.handleRequest(var4);
                  var3 = var1.getOutputStream();
                  KongaProtocolUtils.serializeKongaRequest(var3, var5);
               } catch (KongaProtocolException var14) {
                  System.err.println(var14.getMessage());
                  var14.printStackTrace(System.err);
               } finally {
                  KongaIoUtils.close(var2);
                  KongaIoUtils.close(var3);
                  KongaNetUtils.close(var1);
               }
            } catch (SocketException var16) {
               if (!this.shouldStopListening) {
                  System.err.println(var16.getMessage());
                  var16.printStackTrace(System.err);
               }
            } catch (SocketTimeoutException var17) {
            } catch (IOException var18) {
            } catch (InterruptedException var19) {
            } catch (RuntimeException var20) {
               System.err.println(var20.getMessage());
               var20.printStackTrace(System.err);
            }
         }
      }

      @Override
      public void run() {
         Preconditions.checkState(KongaCommandSocketListener.this.serverSocket != null, "No socket has been created to listen to!!");
         Preconditions.checkState(!KongaCommandSocketListener.this.serverSocket.isClosed(), "The socket is closed!");
         Preconditions.checkState(KongaCommandSocketListener.this.serverSocket.isBound(), "The socket is not bound to any port!");
         this.listen();
      }
   }
}
