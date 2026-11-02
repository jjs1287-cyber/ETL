package org.etlforge.integration.client.server.login;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.server.ServerInfo;

public final class LoginInfo {
   private final ServerInfo serverInfo;
   private final LoginCredentials creds;

   public LoginInfo(ServerInfo var1, LoginCredentials var2) {
      Preconditions.checkNotNull(var1, "server");
      Preconditions.checkNotNull(var2, "creds");
      this.serverInfo = var1;
      this.creds = var2;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public LoginCredentials getLoginCredentials() {
      return this.creds;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof LoginInfo)) {
         return false;
      }

      LoginInfo var2 = (LoginInfo)var1;
      return this.serverInfo.equals(var2.serverInfo) && getUserName(this.creds).equalsIgnoreCase(getUserName(var2.getLoginCredentials()));
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.serverInfo.hashCode();
      return 31 * var1 + getUserName(this.creds).hashCode();
   }

   private static String getUserName(LoginCredentials var0) {
      String var1 = var0.getUser();
      return var1 != null ? var1 : "";
   }
}
