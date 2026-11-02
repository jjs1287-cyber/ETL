package org.etlforge.integration.client.admin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class AdminMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.client.admin.AdminMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.client.admin.AdminMessages");

   private AdminMessages() {
      throw new UnsupportedOperationException();
   }

   static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }
}
