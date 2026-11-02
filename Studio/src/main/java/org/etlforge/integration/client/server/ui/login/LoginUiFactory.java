package org.etlforge.integration.client.server.ui.login;

import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.client.server.ServerAddressFactory;
import org.etlforge.integration.client.server.ServerManager;

public final class LoginUiFactory {
   public static LoginUi getUi(ServerManager var0) {
      ServerAddressFactory var1 = var0.getAddressFactory();
      KnownServersImpl var2 = new KnownServersImpl(var0.getServerInfoStore());
      LoginModel var3 = new LoginModel(var1, var2);
      setPreSelectedServer(var0, var3);
      return SystemUtils.IS_OS_WINDOWS ? new WindowsLoginUi(var3) : new StandardLoginUi(var3);
   }

   private static void setPreSelectedServer(ServerManager var0, LoginModel var1) {
      String var2 = getPreferredServer(var0);
      if (var2 == null) {
         var2 = var1.getListOfKnownServerNames().get(0);
      }

      var1.setServer(var2);
   }

   private static String getPreferredServer(ServerManager var0) {
      String var1 = var0.getLoginService().getNextServerToLoginTo();
      if (var1 == null) {
         var1 = var0.getCurrentServerName();
      }

      return var1;
   }

   private LoginUiFactory() {
   }
}
