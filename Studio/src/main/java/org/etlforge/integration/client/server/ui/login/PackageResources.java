package org.etlforge.integration.client.server.ui.login;

import java.text.MessageFormat;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;

final class PackageResources {
   static final String TITLE = getString("Login.Title");
   static final String SERVER_TOOLTIP = getString("Login.Server.ToolTip");

   private static String getString(String var0) {
      return ServerUiResources.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class Labels {
      static final String ADVANCED = PackageResources.getString("Login.Label.Advanced");
      static final String HTTPS = PackageResources.getString("Login.Label.Https");
      static final String PASSWORD = PackageResources.getString("Login.Password");
      static final String USER = PackageResources.getString("Login.User");
   }

   static class LoginDialog {
      static final String TITLE = PackageResources.getString("Login.Dialog.Title");
   }

   static class LoginJob {
      static final String SENDING_CREDENTIALS = PackageResources.getString("Login.Job.SendingCredentials");
      static final String ERROR = PackageResources.getString("Login.Job.Error");

      static String getName(LoginCredentials var0) {
         return PackageResources.getFormattedMessage("Login.Job.Name", var0.getServer());
      }
   }

   static class PortPanel {
      static final String TITLE = PackageResources.getString("Login.Port.Title");
      static final String DEFAULT = PackageResources.getString("Login.Port.Default");
      static final String MANUAL = PackageResources.getString("Login.Port.Manual");
   }
}
