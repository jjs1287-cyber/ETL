package com.etlforge.integration.client.ui.admin.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class EnterpriseAdminResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static Icon getIcon(String var0) {
      return ICONS.getIcon(var0);
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static MnemonicString getMnemonicString(String var0) {
      return ApplicationResources.MNEMONIC_FACTORY.fromString(getString(var0));
   }

   private EnterpriseAdminResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("com.etlforge.integration.client.ui.admin.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile(
         "com/etlforge/integration/client/ui/admin/resources/Icons.properties", "com.etlforge.integration.client.ui.admin.resources.icons"
      );
      loader = null;
   }
}
