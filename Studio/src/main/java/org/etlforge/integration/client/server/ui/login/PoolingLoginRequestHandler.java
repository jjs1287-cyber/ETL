package org.etlforge.integration.client.server.ui.login;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginRequest;
import org.etlforge.integration.client.server.login.LoginRequestHandler;
import org.etlforge.integration.user.User;
import org.etlforge.system.callback.CallbackResult;

public final class PoolingLoginRequestHandler implements LoginRequestHandler {
   private final ServerManager serverManager;
   private final Object loginLock = new Object();
   private final Object queueLock = new Object();
   private final List<LoginRequestImpl> requestPool = Lists.newArrayList();

   public PoolingLoginRequestHandler(ServerManager var1) {
      this.serverManager = Preconditions.checkNotNull(var1, "serverManager");
   }

   @Override
   public LoginRequest loginRequested(CallbackResult var1) {
      if (NonInteractiveMode.on()) {
         return new LoginRequest() {
            @Override
            public boolean isGranted() {
               return false;
            }
         };
      }

      LoginRequestImpl var2 = new LoginRequestImpl(var1);
      boolean var3 = this.queueRequest(var2);
      if (var3) {
         this.login();
      }

      return var2;
   }

   private boolean queueRequest(LoginRequestImpl var1) {
      boolean var2 = false;
      synchronized (this.queueLock) {
         var2 = this.requestPool.isEmpty();
         this.requestPool.add(var1);
         return var2;
      }
   }

   private void login() {
      synchronized (this.loginLock) {
         new PoolingLoginRequestHandler.LoginWorker().start();
      }
   }

   private class LoginWorker implements LoginCallback {
      private final CountDownLatch blocker = new CountDownLatch(1);

      private LoginWorker() {
      }

      public void start() {
         if (this.isLoginNecessary()) {
            this.bringUpDialog();
            this.waitUntilDone();
         }
      }

      private boolean isLoginNecessary() {
         LoginConfiguration var1 = PoolingLoginRequestHandler.this.serverManager.getLoginService().getConfiguration();
         String var2 = var1.getUserName();
         String var3 = var1.getServerName();
         return StringUtils.isEmpty(var2) || StringUtils.isEmpty(var3);
      }

      private void bringUpDialog() {
         LoginServiceUi var1 = new LoginServiceUi(PoolingLoginRequestHandler.this.serverManager);
         var1.login(this);
      }

      private void waitUntilDone() {
         try {
            this.blocker.await();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            var2.printStackTrace();
         }
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         this.notifyQueuedRequestsOnSuccess();
         this.blocker.countDown();
      }

      private void notifyQueuedRequestsOnSuccess() {
         for (LoginRequestImpl var2 : this.getRequestsToNotify()) {
            var2.granted();
         }
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         this.notifyQueuedRequestsOnFailure(var1);
         this.blocker.countDown();
      }

      private void notifyQueuedRequestsOnFailure(Object var1) {
         for (LoginRequestImpl var3 : this.getRequestsToNotify()) {
            var3.denied(var1);
         }
      }

      @Override
      public void caught(Throwable var1) {
         this.notifyQueuedRequestsOnFailure(var1);
         this.blocker.countDown();
      }

      @Override
      public void cancelled() {
         this.notifyQueuedRequestsWhenCancelled();
         this.blocker.countDown();
      }

      private void notifyQueuedRequestsWhenCancelled() {
         for (LoginRequestImpl var2 : this.getRequestsToNotify()) {
            var2.cancelled();
         }
      }

      private List<LoginRequestImpl> getRequestsToNotify() {
         List var1 = null;
         synchronized (PoolingLoginRequestHandler.this.queueLock) {
            var1 = new ArrayList<>(PoolingLoginRequestHandler.this.requestPool);
            PoolingLoginRequestHandler.this.requestPool.clear();
            return var1;
         }
      }
   }
}
