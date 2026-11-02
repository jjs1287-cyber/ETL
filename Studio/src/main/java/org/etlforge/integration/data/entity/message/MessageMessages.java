package org.etlforge.integration.data.entity.message;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class MessageMessages {
   private static final String BUNDLE_NAME = "org.etlforge.integration.data.entity.message.MessageMessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.data.entity.message.MessageMessages");

   public static String getString(String var0) {
      try {
         return RESOURCE_BUNDLE.getString(var0);
      } catch (MissingResourceException var2) {
         return '!' + var0 + '!';
      }
   }

   private MessageMessages() {
   }
}
