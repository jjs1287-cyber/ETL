package org.etlforge.integration.client.database;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class DatabaseMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.client.database.DatabaseMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.client.database.DatabaseMessages");

   private DatabaseMessages() {
   }

   public static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      try {
         String var2 = RESOURCE_BUNDLE.getString(var0);
         return MessageFormat.format(var2, var1);
      } catch (Exception var3) {
         return '!' + var0 + '!';
      }
   }
}
