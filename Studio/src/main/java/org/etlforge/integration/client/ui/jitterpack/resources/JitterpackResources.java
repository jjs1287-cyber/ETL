package org.etlforge.integration.client.ui.jitterpack.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.StringResources;

public final class JitterpackResources {
   public static final StringResources STRINGS = loadStrings();
   public static final Icon EXPORT_16 = ClientIcons.getIcon("Jitterpak.Export.16");
   public static final Icon EXPORT_24 = ClientIcons.getIcon("Jitterpak.Export.24");
   public static final Icon EXPORT_32 = ClientIcons.getIcon("Jitterpak.Export.32");
   public static final Icon IMPORT_16 = ClientIcons.getIcon("Jitterpak.Import.16");
   public static final Icon IMPORT_24 = ClientIcons.getIcon("Jitterpak.Import.24");
   public static final Icon IMPORT_32 = ClientIcons.getIcon("Jitterpak.Import.32");

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.jitterpack.resources.Strings");
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String formatMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private JitterpackResources() {
   }
}
