package org.etlforge.integration.data.entity.document;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class DocumentMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.data.entity.document.DocumentMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.data.entity.document.DocumentMessages");

   private DocumentMessages() {
      throw new UnsupportedOperationException();
   }

   public static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return getMissingResourceString(var0);
      }
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      try {
         String var2 = RESOURCE_BUNDLE.getString(var0);
         return MessageFormat.format(var2, var1);
      } catch (MissingResourceException var3) {
         return getMissingResourceString(var0);
      } catch (IllegalArgumentException var4) {
         return getMissingResourceString(var0);
      }
   }

   private static String getMissingResourceString(String var0) {
      return '!' + var0 + '!';
   }
}
