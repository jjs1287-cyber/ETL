package org.etlforge.integration.client.server.login;

import com.google.common.base.Strings;
import org.etlforge.integration.client.server.ServerAddress;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.User;

public final class LoginConfiguration {
   private String serverName;
   private ServerGuid serverGuid;
   private String userName;
   private String password;
   private String passwordHash;
   private boolean useHttps;
   private final Object lock = new Object();

   public void setDisconnected() {
      synchronized (this.lock) {
         this.serverName = "";
         this.userName = "";
         this.password = "";
         this.passwordHash = "";
         this.serverGuid = null;
      }
   }

   public void setConnected(ServerAddress var1, ServerGuid var2, LoginCredentials var3) {
      synchronized (this.lock) {
         this.serverName = var1.toString();
         this.serverGuid = var2;
         this.userName = var3.getUser();
         this.password = var3.getPassword();
         this.passwordHash = var3.getPasswordHash();
      }
   }

   public void updateUserProfile(User var1) {
      synchronized (this.lock) {
         this.userName = var1.getUserName();
         if (var1.getPasswordHash() != null) {
            this.passwordHash = var1.getPasswordHash();
            this.password = var1.getPassword();
         }
      }
   }

   public void setServerName(String var1) {
      synchronized (this.lock) {
         this.serverName = var1;
      }
   }

   public String getServerName() {
      synchronized (this.lock) {
         return this.serverName;
      }
   }

   public ConnectionSnapshot getConnectionSnapshot() {
      synchronized (this.lock) {
         return new ConnectionSnapshot(this.serverName, this.userName, this.passwordHash, this.useHttps);
      }
   }

   public static final class ConnectionSnapshot {
      public final String serverName, userName, passwordHash;
      public final boolean useHttps;
      private ConnectionSnapshot(String serverName, String userName, String passwordHash, boolean useHttps) {
         this.serverName=serverName;this.userName=userName;this.passwordHash=passwordHash;this.useHttps=useHttps;
      }
   }

   public ServerGuid getServerId() {
      synchronized (this.lock) {
         return this.serverGuid;
      }
   }

   public String getUserName() {
      synchronized (this.lock) {
         return Strings.nullToEmpty(this.userName);
      }
   }

   public String getPassword() {
      synchronized (this.lock) {
         return Strings.nullToEmpty(this.password);
      }
   }

   public String getPasswordHash() {
      synchronized (this.lock) {
         return Strings.nullToEmpty(this.passwordHash);
      }
   }

   public void setUseHttps(boolean var1) {
      synchronized (this.lock) {
         this.useHttps = var1;
      }
   }

   public boolean isUsingHttps() {
      synchronized (this.lock) {
         return this.useHttps;
      }
   }
}
