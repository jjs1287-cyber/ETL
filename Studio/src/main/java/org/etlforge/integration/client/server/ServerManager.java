package org.etlforge.integration.client.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.client.server.login.Authenticate;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginInfo;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginEvent;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.User;
import org.etlforge.util.id.KongaID;

public final class ServerManager implements ServerConnectionService {
   private final ServerInformationStore serverStore;
   private final ServerAddressFactory addressFactory;
   private final UserManager userManager;
   private volatile UserAdmin userAdmin;
   private final List<ServerConnectionListener> connectionListeners;
   private ConnectionStatus connectionStatus;
   private ServerInfo serverInfo;
   private User user;
   private LoginCredentials currentCredentials;
   private final LoginServiceImpl loginService;

   public ServerManager(ServerInformationStore var1, ServerPorts var2, UserManager var3) {
      this.serverStore = var1;
      this.addressFactory = new ServerAddressFactory(var2);
      this.connectionStatus = ConnectionStatus.DISCONNECTED;
      this.connectionListeners = new CopyOnWriteArrayList<>();
      this.userManager = var3;
      this.loginService = this.createLoginService();
   }

   private LoginServiceImpl createLoginService() {
      LoginServiceImpl var1 = new LoginServiceImpl(this);
      var1.addLoginListener(new LoginListener.Adapter() {
         @Override
         public void postLogin(LoginEvent var1) {
            this.registerNewServer(var1);
            this.updateUsersAndGroups();
         }

         private void registerNewServer(LoginEvent var1) {
            LoginInfo var2 = var1.getInfo();
            ServerInfo var3 = var2.getServerInfo();
            LoginCredentials var4 = var2.getLoginCredentials();
            String var5 = var4.storePasswordOnClient() ? var4.getPassword() : null;
            ServerManager.this.serverStore.storeNewLogin(var3, var4.getUser(), var5, var4.isUsingHttps());
         }

         private void updateUsersAndGroups() {
            if (ServerManager.this.userAdmin != null) {
               UpdateUsersAndGroupsJob var1x = new UpdateUsersAndGroupsJob(ServerManager.this.userManager, ServerManager.this.userAdmin);
               var1x.execute();
            }
         }
      });
      return var1;
   }

   public LoginService getLoginService() {
      return this.loginService;
   }

   public ServerAddressFactory getAddressFactory() {
      return this.addressFactory;
   }

   @Override
   public synchronized ConnectionStatus getConnectionStatus() {
      return this.connectionStatus;
   }

   @Override
   public synchronized String getCurrentServerName() {
      return this.serverInfo != null ? this.serverInfo.getName() : null;
   }

   @Override
   public synchronized ServerGuid getCurrentServerGuid() {
      return this.serverInfo != null ? this.serverInfo.getGuid() : null;
   }

   @Override
   public synchronized ServerInfo getCurrentServerInfo() {
      return this.connectionStatus == ConnectionStatus.CONNECTED ? this.serverInfo : null;
   }

   public synchronized LoginCredentials getCurrentLoginCredentials() {
      return this.currentCredentials;
   }

   public synchronized String getCurrentUserName() {
      return this.currentCredentials != null ? this.currentCredentials.getUser() : null;
   }

   public UserManager getUserManager() {
      return this.userManager;
   }

   @Override
   public synchronized User getCurrentUser() {
      return this.user;
   }

   public synchronized void logOff() {
      this.setDisconnected();
   }

   public ServerInformationStore getServerInfoStore() {
      return new ServerInformationStore() {
         @Override
         public void storeNewLogin(ServerInfo var1, String var2, String var3, boolean var4) {
            ServerManager.this.serverStore.storeNewLogin(var1, var2, var3, var4);
         }

         @Override
         public boolean removeServerRegistration(ServerGuid var1) {
            ServerPreferences.delete(var1);
            return ServerManager.this.serverStore.removeServerRegistration(var1);
         }

         @Override
         public List<KnownServer> getRegisteredServers() {
            return ServerManager.this.serverStore.getRegisteredServers();
         }
      };
   }

   public List<KnownServer> getRegisteredServers() {
      return this.serverStore.getRegisteredServers();
   }

   public KnownServer getKnownServer(ServerGuid var1) {
      for (KnownServer var3 : this.serverStore.getRegisteredServers()) {
         if (KongaID.areEqual(var1, var3.getServerInfo().getGuid())) {
            return var3;
         }
      }

      return null;
   }

   synchronized void setDisconnected() {
      if (this.connectionStatus != ConnectionStatus.DISCONNECTED) {
         this.connectionStatus = ConnectionStatus.DISCONNECTED;
         this.serverInfo = null;
         IntegrationServer.getInstance().setServerInfo(null);
         this.user = null;
         this.currentCredentials = null;
         this.fireConnectionChanged();
      }
   }

   synchronized void setConnected(ServerInfo var1, LoginCredentials var2, User var3) {
      boolean var4 = this.connectionStatus == ConnectionStatus.DISCONNECTED || differs(var1, this.serverInfo) || differs(var2, this.currentCredentials);
      this.connectionStatus = ConnectionStatus.CONNECTED;
      this.serverInfo = var1;
      IntegrationServer.getInstance().setServerInfo(var1);
      this.user = var3;
      this.currentCredentials = var2;
      if (var4 && this.connectionStatus == ConnectionStatus.CONNECTED) {
         this.fireConnectionChanged();
      }
   }

   @Override
   public void addServerConnectionListener(ServerConnectionListener var1) {
      if (var1 != null) {
         this.connectionListeners.add(var1);
      }
   }

   @Override
   public void removeServerConnectionListener(ServerConnectionListener var1) {
      this.connectionListeners.remove(var1);
   }

   public void setUserAdmin(UserAdmin var1) {
      this.userAdmin = var1;
   }

   public void setLoginApi(Authenticate var1) {
      this.loginService.setServerLoginApi(var1);
   }

   private void fireConnectionChanged() {
      ServerConnectionEvent var1 = new ServerConnectionEvent(this, this.connectionStatus, this.serverInfo, this.currentCredentials, this.user);

      for (ServerConnectionListener var3 : this.connectionListeners) {
         var3.serverConnectionChanged(var1);
      }
   }

   private static boolean differs(Object var0, Object var1) {
      return var0 == null ? var1 != null : !var0.equals(var1);
   }

   private static boolean differs(LoginCredentials var0, LoginCredentials var1) {
      if (var0 == null) {
         return var1 != null;
      } else {
         return var1 == null
            ? true
            : !var0.getUser().equals(var1.getUser())
               || var0.isUsingHttps() != var1.isUsingHttps()
               || var0.storePasswordOnClient() != var1.storePasswordOnClient();
      }
   }
}
