package org.etlforge.integration.data.entity.source;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class SourceMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.data.entity.source.SourceMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.data.entity.source.SourceMessages");

   private SourceMessages() {
      throw new UnsupportedOperationException();
   }

   public static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }
}
