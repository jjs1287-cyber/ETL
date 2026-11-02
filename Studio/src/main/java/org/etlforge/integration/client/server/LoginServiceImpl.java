package org.etlforge.integration.client.server;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.info.ProductVersions;
import org.etlforge.integration.client.server.login.Authenticate;
import org.etlforge.integration.client.server.login.AuthenticateResult;
import org.etlforge.integration.client.server.login.ConnectionChangeVetoException;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginInfo;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginEvent;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.client.server.login.event.LoginRequestEvent;
import org.etlforge.integration.client.server.login.event.LogoutRequestEvent;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

final class LoginServiceImpl implements LoginService {
   private final ServerManager serverManager;
   private final ServerVersionCompatibilityChecker versionChecker;
   private final LoginConfiguration loginConfig;
   private String nextServerToLoginTo;
   private final Object lock;
   private volatile Authenticate serverApi;
   private final List<LoginListener> loginListeners;
   private final List<LoginCallback> externalCallbacks;
   private final LoginCallback internalCallback;

   public LoginServiceImpl(ServerManager var1) {
      this.serverManager = var1;
      this.versionChecker = new ServerVersionCompatibilityChecker(lookupRequiredVersion());
      this.loginConfig = new LoginConfiguration();
      var1.addServerConnectionListener(new LoginServiceImpl.LoginConfigurationUpdater());
      this.loginListeners = new CopyOnWriteArrayList<>();
      this.externalCallbacks = new CopyOnWriteArrayList<>();
      this.lock = new Object();
      this.internalCallback = new LoginServiceImpl.FixedCallback();
   }

   private static Version lookupRequiredVersion() {
      try {
         return ApplicationConfiguration.getVersion("RequiredServerVersion");
      } catch (Exception var1) {
         ErrorLog.log(LoginService.class, "The client configuration is invalid. The required server version could not be read from the properties file.", var1);
         return VersionFactory.newVersion(4, 0, 0, 1);
      }
   }

   void setServerLoginApi(Authenticate var1) {
      this.serverApi = var1;
   }

   @Override
   public void cancelled() {
      this.internalCallback.cancelled();
   }

   @Override
   public void login(LoginCredentials var1) {
      boolean var2 = this.doPreLoginProcessing(var1);
      if (var2) {
         this.loginImpl(var1);
      } else {
         this.internalCallback.cancelled();
      }
   }

   private void loginImpl(LoginCredentials var1) {
      Preconditions.checkState(this.serverApi != null, "The login server API has not been provided. This is a bug.");
      this.loginConfig.setUseHttps(var1.isUsingHttps());

      try {
         ServerAddress var2 = var1.getServerAddress();
         AuthenticateResult var3 = this.serverApi.login(var1);
         if (this.checkServerVersion(var2, var3)) {
            this.onSuccessFullLogin(var2, var3, var1);
         }
      } catch (InvalidCredentialsException var4) {
         this.internalCallback.invalidCredentials(var4);
      } catch (IntegrationServerException var5) {
         if (var5 instanceof ServerLicenseException) {
            this.internalCallback.cancelled();
         } else {
            this.internalCallback.caught(var5);
         }
      }
   }

   private boolean checkServerVersion(ServerAddress var1, AuthenticateResult var2) {
      try {
         this.versionChecker.checkServerVersion(var1.toString(), var2.versionString());
         return true;
      } catch (UnsupportedVersionException var5) {
         this.internalCallback.caught(var5);
      } catch (IllegalVersionException var6) {
         String var4 = ServerMessages.getFormattedMessage("Login.UnexpectedResponse", new Object[]{var6.getMessage()});
         this.internalCallback.caught(new IntegrationServerException(var4, var6));
      }

      return false;
   }

   @Override
   public LoginConfiguration getConfiguration() {
      return this.loginConfig;
   }

   @Override
   public synchronized void setNextServerToLoginTo(String var1) {
      this.nextServerToLoginTo = var1;
   }

   @Override
   public synchronized String getNextServerToLoginTo() {
      return this.nextServerToLoginTo;
   }

   @Override
   public void addLoginListener(LoginListener var1) {
      this.loginListeners.add(var1);
   }

   @Override
   public void removeLoginListener(LoginListener var1) {
      this.loginListeners.remove(var1);
   }

   @Override
   public void addCallback(LoginCallback var1) {
      synchronized (this.lock) {
         this.externalCallbacks.add(var1);
      }
   }

   @Override
   public void removeCallback(LoginCallback var1) {
      synchronized (this.lock) {
         this.externalCallbacks.remove(var1);
      }
   }

   private boolean doPreLoginProcessing(LoginCredentials var1) {
      try {
         LoginRequestEvent var2 = new LoginRequestEvent(this, this.serverManager.getCurrentServerInfo(), this.serverManager.getCurrentLoginCredentials(), var1);

         for (LoginListener var4 : this.loginListeners) {
            var4.loginRequested(var2);
         }

         return true;
      } catch (ConnectionChangeVetoException var5) {
         return false;
      }
   }

   @Override
   public void logout() {
      if (this.isConnected() && this.isLogoutAllowed()) {
         this.serverManager.setDisconnected();
      }
   }

   private boolean isConnected() {
      return this.serverManager.getConnectionStatus() == ConnectionStatus.CONNECTED;
   }

   private boolean isLogoutAllowed() {
      try {
         LogoutRequestEvent var1 = new LogoutRequestEvent(this, this.serverManager.getCurrentServerInfo(), this.serverManager.getCurrentLoginCredentials());

         for (LoginListener var3 : this.loginListeners) {
            var3.logoutRequested(var1);
         }

         return true;
      } catch (ConnectionChangeVetoException var4) {
         return false;
      }
   }

   private void onSuccessFullLogin(ServerAddress var1, AuthenticateResult var2, LoginCredentials var3) {
      this.loginConfig.setConnected(var1, var2.serverGuid(), var3);
      ServerInfo var4 = ServerInfoFactory.create(var2, var3);
      LoginServiceImpl.UserImpl var5 = new LoginServiceImpl.UserImpl(var2.userGuid(), var3.getUser(), var2.isUserAdmin());
      this.internalCallback.loginSucceeded(var4, var3, var5);
      this.notifyListenersOnLogin(var4, var3);
      ProductVersions.update(var2.keyValues());
   }

   private void notifyListenersOnLogin(ServerInfo var1, LoginCredentials var2) {
      LoginEvent var3 = new LoginEvent(this, new LoginInfo(var1, var2));

      for (LoginListener var5 : this.loginListeners) {
         try {
            var5.postLogin(var3);
         } catch (Exception var7) {
            this.logPostLoginError(var5, var7);
         }
      }
   }

   private void logPostLoginError(LoginListener var1, Exception var2) {
      String var3 = "An unexpected error occurred in the LoginListener " + var1.getClass().getName() + ". The error was: " + var2.getMessage();
      ErrorLog.log(this.getClass(), var3, var2);
   }

   private final class FixedCallback implements LoginCallback {
      private FixedCallback() {
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         LoginServiceImpl.this.serverManager.setConnected(var1, var2, var3);

         try {
            for (LoginCallback var5 : LoginServiceImpl.this.externalCallbacks) {
               var5.loginSucceeded(var1, var2, var3);
            }
         } finally {
            this.cleanUp();
         }
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         LoginServiceImpl.this.serverManager.setDisconnected();

         try {
            for (LoginCallback var3 : LoginServiceImpl.this.externalCallbacks) {
               var3.invalidCredentials(var1);
            }
         } finally {
            this.cleanUp();
         }
      }

      @Override
      public void caught(Throwable var1) {
         LoginServiceImpl.this.serverManager.setDisconnected();

         try {
            for (LoginCallback var3 : LoginServiceImpl.this.externalCallbacks) {
               var3.caught(var1);
            }
         } finally {
            this.cleanUp();
         }
      }

      @Override
      public void cancelled() {
         try {
            for (LoginCallback var2 : LoginServiceImpl.this.externalCallbacks) {
               var2.cancelled();
            }
         } finally {
            this.cleanUp();
         }
      }

      private void cleanUp() {
         LoginServiceImpl.this.externalCallbacks.clear();
         LoginServiceImpl.this.setNextServerToLoginTo(null);
      }
   }

   private class LoginConfigurationUpdater implements ServerConnectionListener {
      private LoginConfigurationUpdater() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         if (!var1.isConnected()) {
            LoginServiceImpl.this.loginConfig.setDisconnected();
         }
      }
   }

   private static final class UserImpl extends User {
      private final boolean admin;

      public UserImpl(UserId var1, String var2, boolean var3) {
         super(var1, var2);
         this.admin = var3;
      }

      @Override
      public boolean isAdmin() {
         return this.admin;
      }

      @Override
      public void setDescription(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setEmail(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setFirstName(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setLastName(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setPassword(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setPasswordHash(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void setUserName(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean equals(Object var1) {
         return super.equals(var1);
      }

      @Override
      public int hashCode() {
         return super.hashCode();
      }
   }
}
