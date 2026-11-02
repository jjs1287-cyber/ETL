package org.etlforge.integration.data.location.ldap;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.data.location.ldap.messages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.data.location.ldap.messages");

   private Messages() {
   }

   public static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }
}
