package org.etlforge.integration.client.server;

import java.util.Map;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.login.AuthenticateResult;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.util.version.Version;

final class ServerInfoFactory {
   public static ServerInfo create(AuthenticateResult var0, LoginCredentials var1) {
      ServerPlatform var2 = getServerPlatform(var0);
      Version var3 = getVersion(var0.versionString());
      return new ServerInfo(var0.serverGuid(), var1.getServer(), var3, var2);
   }

   private static ServerPlatform getServerPlatform(AuthenticateResult var0) {
      Map var1 = var0.keyValues();
      return ServerPlatform.fromOSAndSystemInfo((String)var1.get("OS"), getSysInfo(var1));
   }

   private static String getSysInfo(Map<String, String> var0) {
      String var1 = (String)var0.get("SysInfo");
      if (var1 == null) {
         var1 = "";
      }

      return var1;
   }

   private static Version getVersion(String var0) {
      if (var0 == null) {
         return null;
      }

      try {
         return VersionFactory.fromString(var0);
      } catch (Exception var2) {
         ErrorLog.attention(LoginService.class, ServerMessages.getString("Login.IllegalServerVersion"), var2);
         return null;
      }
   }

   private ServerInfoFactory() {
   }
}
