package org.etlforge.integration.client.applications.deployer;

import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.server.ServerAddressFactory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.user.User;

final class ServerConnector {
   private final ServerManager serverManager;
   private final ConsoleWriter consoleWriter;

   public ServerConnector(ServerManager var1, ConsoleWriter var2) {
      this.serverManager = var1;
      this.consoleWriter = var2;
   }

   public void connect(String var1, String var2, String var3, boolean var4) throws Throwable {
      this.consoleWriter.log("Connecting to the server " + var1 + "...");
      ServerConnector.Callback var5 = new ServerConnector.Callback(this.consoleWriter);
      LoginService var6 = this.serverManager.getLoginService();
      var6.addCallback(var5);
      LoginCredentials var7 = this.getLoginCredentials(var1, var2, var3, var4);
      var6.login(var7);
      var5.waitUntilDone();
      var5.checkSuccess();
   }

   private LoginCredentials getLoginCredentials(String var1, String var2, String var3, boolean var4) {
      ServerAddressFactory var5 = this.serverManager.getAddressFactory();
      return var4
         ? new LoginCredentials(var5, var1, var2, new LoginCredentials.PasswordHash(var3), false)
         : new LoginCredentials(var5, var1, var2, var3, false, false);
   }

   private static final class Callback implements LoginCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      private Throwable failureReason;
      private final ConsoleWriter consoleWriter;

      Callback(ConsoleWriter var1) {
         this.consoleWriter = var1;
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         this.consoleWriter.log("Successfully connected to the server: " + var1);
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         Exception var1 = new Exception("Login was cancelled.");
         this.caught(var1);
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         this.caught(var1);
      }

      @Override
      public void caught(Throwable var1) {
         this.failureReason = var1;
         this.latch.countDown();
      }

      public void waitUntilDone() throws InterruptedException {
         this.latch.await();
      }

      private void checkSuccess() throws Throwable {
         if (this.failureReason != null) {
            throw this.failureReason;
         }
      }
   }
}
