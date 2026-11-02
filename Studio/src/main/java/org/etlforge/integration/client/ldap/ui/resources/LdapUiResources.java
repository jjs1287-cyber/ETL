package org.etlforge.integration.client.ldap.ui.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class LdapUiResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private LdapUiResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.ldap.ui.resources.Strings");
      loader = null;
   }
}
