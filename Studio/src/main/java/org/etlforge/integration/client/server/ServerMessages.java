package org.etlforge.integration.client.server;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class ServerMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.client.server.ServerMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.client.server.ServerMessages");

   private ServerMessages() {
      throw new UnsupportedOperationException();
   }

   static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }

   static String getFormattedMessage(String var0, Object[] var1) {
      try {
         String var2 = RESOURCE_BUNDLE.getString(var0);
         return MessageFormat.format(var2, var1);
      } catch (Exception var3) {
         return '!' + var0 + '!';
      }
   }
}
