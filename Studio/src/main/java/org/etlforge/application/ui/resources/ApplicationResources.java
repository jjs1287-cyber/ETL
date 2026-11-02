package org.etlforge.application.ui.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.StringResources;

public final class ApplicationResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;
   public static final MnemonicStringFactory MNEMONIC_FACTORY = MnemonicStringFactory.newCharMarkerFactory('&');

   public static MnemonicString getMnemonicString(String var0) {
      return MNEMONIC_FACTORY.fromString(STRINGS.getString(var0));
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static Icon getIcon(String var0) {
      return ICONS.getIcon(var0);
   }

   public static String format(String var0, Object... var1) {
      String var2 = STRINGS.getString(var0);
      return MessageFormat.format(var2, var1);
   }

   private ApplicationResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.application.ui.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile("org/etlforge/application/ui/resources/Icons.properties", "org.etlforge.application.ui.resources.icons");
      loader = null;
   }
}
