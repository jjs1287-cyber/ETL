package org.etlforge.integration.client.server.ui.login;

import com.google.common.collect.Maps;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerAddressFactory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;
import org.etlforge.util.net.Port;

public final class LoginModel extends AbstractPropertyBasedModel {
   private static final String DEFAULT_USER = "Admin";
   public static final String SERVER = "server";
   public static final String TEMPORARY_SERVER = "temporaryServer";
   public static final String USER = "user";
   public static final String PASSWORD = "password";
   public static final String REMEMBER_PASSWORD = "rememberPassword";
   public static final String USE_DEFAULT_PORT = "useDefaultPort";
   public static final String PORT = "port";
   public static final String USE_HTTPS = "useHttps";
   private final Property<String> server = this.newProperty("server", "");
   private final Property<String> temporaryServer = this.newProperty("temporaryServer", "");
   private final Property<String> user = this.newProperty("user", "");
   private final Property<String> password = this.newProperty("password", "");
   private final Property<Boolean> rememberPassword = this.newProperty("rememberPassword", false);
   private final Property<Boolean> useDefaultPort = this.newProperty("useDefaultPort", true);
   private final Property<String> port = this.newProperty("port", "");
   private final Property<Boolean> usingHttps = this.newProperty("useHttps", false);
   private final Map<String, String> serverUserMap = Maps.newHashMap();
   private final ServerAddressFactory serverAddressFactory;
   private final KnownServers knownServers;
   private boolean internalUserNameChange;
   private boolean userHasBeenChanged;
   private final boolean allowServerSelection = lookupServerSelectionAllowed();

   public LoginModel(ServerAddressFactory var1, KnownServers var2) {
      this.serverAddressFactory = var1;
      this.knownServers = var2;
      this.populateLastUserMap();
      this.user.addListener(new LoginModel.UserNameListener());
      this.temporaryServer.addListener(new LoginModel.TemporaryServerNameListener());
      this.port.addListener(new LoginModel.PortListener());
      this.useDefaultPort.addListener(new LoginModel.DefaultPortListener());
   }

   private static boolean lookupServerSelectionAllowed() {
      return ClientInfo.isDataLoader() ? ApplicationConfiguration.getBoolean("DataLoader.LoginDialog.AllowServerSelection", false) : true;
   }

   private void populateLastUserMap() {
      for (String var2 : this.knownServers.getServerNames()) {
         KnownServer var3 = this.knownServers.getServer(var2);
         if (var3 != null) {
            this.serverUserMap.put(removePort(var2), var3.getLatestUser());
         }
      }
   }

   private static String removePort(String var0) {
      return StringUtils.substringBefore(var0, ":");
   }

   public boolean isServerSelectionAllowed() {
      return this.allowServerSelection;
   }

   public ServerInfo getServerInfo(String var1) {
      KnownServer var2 = this.knownServers.getServer(var1);
      return var2 != null ? var2.getServerInfo() : null;
   }

   public List<String> getListOfKnownServerNames() {
      List var1 = this.knownServers.getServerNames();
      return var1.isEmpty() ? Collections.singletonList("localhost") : var1;
   }

   public void setServer(String var1) {
      String var2 = var1 == null ? "" : var1.trim();
      this.setTemporaryServer(var2);
      this.server.set(var2);
      if (!this.userHasBeenChanged) {
         this.updateUserAndPasswordForNewServer(var2);
      }

      boolean var3 = this.isServerKnownToUseHttps(var2);
      this.usingHttps.set(var3);
   }

   private void updateUserAndPasswordForNewServer(String var1) {
      this.internalUserNameChange = true;
      String var2 = this.getLastUserForServer(removePort(var1));
      this.user.set(var2);
      String var3 = this.getLastPasswordForServer(var1);
      this.password.set(var3);
      this.rememberPassword.set(StringUtils.isNotEmpty(var3));
      this.internalUserNameChange = false;
   }

   private void setTemporaryServer(String var1) {
      this.temporaryServer.set(var1.trim());
   }

   private String getLastUserForServer(String var1) {
      String var2 = this.serverUserMap.get(var1);
      if (var2 == null) {
         var2 = "Admin";
      }

      return var2;
   }

   private String getLastPasswordForServer(String var1) {
      String var2 = null;
      KnownServer var3 = this.knownServers.getServer(var1);
      if (var3 != null) {
         var2 = var3.getPassword();
      }

      return var2 != null ? var2 : "";
   }

   private boolean isServerKnownToUseHttps(String var1) {
      KnownServer var2 = this.knownServers.getServer(var1);
      return var2 != null ? var2.isUsingHttps() : false;
   }

   public boolean hasServer() {
      return StringUtils.isNotEmpty(this.getServer());
   }

   private String getServer() {
      return this.server.get();
   }

   public boolean hasPassword() {
      return StringUtils.isNotEmpty(this.password.get());
   }

   public boolean isUsingNonDefaultSettings() {
      return !this.useDefaultPort.get() || this.usingHttps.get();
   }

   public LoginCredentials getCredentials() {
      String var1 = this.temporaryServer.get();
      String var2 = this.user.get();
      String var3 = this.password.get();
      boolean var4 = this.usingHttps.get();
      boolean var5 = this.rememberPassword.get();
      return new LoginCredentials(this.serverAddressFactory, var1, var2, var3, var4, var5);
   }

   public boolean isInputValid() {
      if (this.getServer().isEmpty() || this.user.get().isEmpty()) {
         return false;
      } else {
         return this.useDefaultPort.get() ? true : this.isPortNumberValid();
      }
   }

   private boolean isPortNumberValid() {
      String var1 = this.port.get();
      Port var2 = Port.valueOf(var1);
      return var2 != Port.DEFAULT && var2 != Port.INVALID;
   }

   private String getTemporaryServerNameWithoutPort() {
      String var1 = this.temporaryServer.get();
      return removePort(var1);
   }

   private class DefaultPortListener implements PropertyChangeListener {
      private DefaultPortListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Boolean var2 = (Boolean)var1.getNewValue();
         String var3 = LoginModel.this.getTemporaryServerNameWithoutPort();
         if (var2) {
            LoginModel.this.setTemporaryServer(var3);
         } else {
            LoginModel.this.setTemporaryServer(var3 + ":" + LoginModel.this.port.get());
         }
      }
   }

   private class PortListener implements PropertyChangeListener {
      private PortListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = (String)var1.getNewValue();
         if (var2.isEmpty()) {
            String var3 = LoginModel.this.temporaryServer.get();
            if (var3.contains(":")) {
               String var4 = LoginModel.this.getTemporaryServerNameWithoutPort() + ":";
               LoginModel.this.setTemporaryServer(var4);
            }
         } else {
            String var5 = LoginModel.this.getTemporaryServerNameWithoutPort();
            String var6 = var5 + ":" + var2;
            LoginModel.this.setTemporaryServer(var6);
         }
      }
   }

   private class TemporaryServerNameListener implements PropertyChangeListener {
      private TemporaryServerNameListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = (String)var1.getNewValue();
         boolean var3 = var2.contains(":");
         LoginModel.this.useDefaultPort.set(!var3);
         if (var3) {
            String var4 = StringUtils.substringAfter(var2, ":");
            LoginModel.this.port.set(var4);
         }
      }
   }

   private class UserNameListener implements PropertyChangeListener {
      private UserNameListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!LoginModel.this.internalUserNameChange) {
            LoginModel.this.userHasBeenChanged = true;
         }
      }
   }
}
