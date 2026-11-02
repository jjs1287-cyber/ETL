package org.etlforge.integration.client.ui.icons;

import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class AboutIcons {
   private static final String PACKAGE = "org.etlforge.integration.client.ui.icons";
   public static final Icon MAIN_AUTHOR = loadIcon("mainauthor.jpg");

   public static Icon loadIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.icons", var0);
   }

   private AboutIcons() {
   }
}
