package org.etlforge.integration.client.server.ui.login;

import com.google.common.base.Preconditions;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.IllegalServerAddressException;
import org.etlforge.integration.client.server.ServerAddress;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.user.User;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.ui.util.UiUtils;

public final class LoginServiceUi {
   private final ServerManager serverManager;

   public LoginServiceUi(ServerManager var1) {
      this.serverManager = Preconditions.checkNotNull(var1);
   }

   public void login(LoginCallback var1) {
      this.login(var1, false);
   }

   public void loginWithDialog(LoginCallback var1) {
      this.login(var1, true);
   }

   private void login(LoginCallback var1, boolean var2) {
      SafeCallback var3 = new SafeCallback(var1);
      if (ClientInfo.isDataLoader() && !var2) {
         LoginServiceUi.DataLoaderLogin var4 = new LoginServiceUi.DataLoaderLogin(var3);
         var4.tryLogin();
      } else {
         this.doStandardLogin(var3);
      }
   }

   private void doStandardLogin(final LoginCallback var1) {
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            LoginServiceUi.this.loginImpl(var1);
         }
      };
      UiUtils.runOnEventThreadAndWait(var2, new ExceptionHandler<Throwable>() {
         @Override
         public void caught(Throwable var1x) {
            ErrorLog.log(LoginServiceUi.class, "An unexpected error occurred: " + var1x.getMessage(), var1x);
            var1.caught(var1x);
         }
      });
   }

   private void loginImpl(LoginCallback var1) {
      LoginDialog var2 = new LoginDialog(this.serverManager);
      var2.setVisible(true);
      LoginCredentials var3 = var2.getCredentials();
      if (var3 == null) {
         this.handleLoginCancelled(var1);
      } else {
         this.loginWithCredentials(var3, var1, true);
      }
   }

   private void loginWithCredentials(LoginCredentials var1, LoginCallback var2, boolean var3) {
      try {
         ServerAddress var4 = var1.getServerAddress();
         this.serverManager.getLoginService().getConfiguration().setServerName(var4.toString());
      } catch (IllegalServerAddressException var5) {
         var2.caught(var5);
         return;
      }

      LoginJob var6 = new LoginJob(this.serverManager.getLoginService(), var1, var2, var3);
      var6.submit();
   }

   private void handleLoginCancelled(LoginCallback var1) {
      this.serverManager.getLoginService().cancelled();
      var1.cancelled();
   }

   private class DataLoaderLogin {
      private final LoginCallback externalCallback;

      public DataLoaderLogin(LoginCallback var2) {
         this.externalCallback = var2;
      }

      public void tryLogin() {
         LoginCredentials var1 = this.createCredentials();
         LoginServiceUi.this.loginWithCredentials(var1, new LoginServiceUi.DataLoaderLogin.InternalCallback(), false);
      }

      private LoginCredentials createCredentials() {
         return new LoginCredentials(LoginServiceUi.this.serverManager.getAddressFactory(), ClientInfo.getDataLoaderServer(), "Admin", "", false, false);
      }

      private class InternalCallback implements LoginCallback {
         private InternalCallback() {
         }

         @Override
         public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
            DataLoaderLogin.this.externalCallback.loginSucceeded(var1, var2, var3);
         }

         @Override
         public void invalidCredentials(InvalidCredentialsException var1) {
            LoginServiceUi.this.doStandardLogin(DataLoaderLogin.this.externalCallback);
         }

         @Override
         public void cancelled() {
            DataLoaderLogin.this.externalCallback.cancelled();
         }

         @Override
         public void caught(Throwable var1) {
            DataLoaderLogin.this.externalCallback.caught(var1);
         }
      }
   }
}
