package org.etlforge.integration.client.server.ui;

import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ServerRenderer {
   public static Icon getIconForConnectionState(ServerConnectionEvent var0) {
      return var0.isConnected() && var0.getCredentials().isUsingHttps() ? ClientIcons.SERVER_HTTPS_16 : ClientIcons.SERVER_16;
   }

   public static JLabel getServerCaptionLabel(TextStyle var0) {
      return getServerCaptionLabel(var0, null, null);
   }

   public static JLabel getServerCaptionLabel(TextStyle var0, String var1, String var2) {
      if (var0 == null) {
         var0 = TextStyles.DefaultText;
      }

      KongaLabel var3 = var0.makeLabel(getServerString(var1, var2));
      var3.setIcon(ClientIcons.SERVER_16);
      return var3;
   }

   private static String getServerString(String var0, String var1) {
      String var2 = var0 == null ? "" : var0;
      var2 = var2 + ServerUiResources.getString("Server");
      if (var1 != null) {
         var2 = var2 + var1;
      }

      return var2 + ":";
   }

   public static JLabel getEtlforgeServerCaptionLabel(TextStyle var0, String var1) {
      if (var0 == null) {
         var0 = TextStyles.DefaultText;
      }

      if (var1 == null) {
         var1 = ApplicationConfiguration.getCompanyName() + " " + ServerUiResources.getString("Server") + ":";
      }

      KongaLabel var2 = var0.makeLabel(var1);
      var2.setIcon(ClientIcons.APPLICATION_16);
      return var2;
   }

   private ServerRenderer() {
   }
}
