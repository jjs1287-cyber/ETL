package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources;

import java.awt.Image;
import java.util.ListResourceBundle;
import org.etlforge.ui.util.ImageLoader;

public final class Icons extends ListResourceBundle {
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images";
   private static final Object[][] contents = new Object[][]{
      {"Collapse", get("collapse_t.gif")},
      {"Expand", get("expand_t.gif")},
      {"MapMode", get("mapmode_t.gif")},
      {"Multiple", get("multiple_t.gif")},
      {"Optional", get("optional.gif")},
      {"PartExpand", get("partial_t.gif")},
      {"ResolveMode", get("resolvemode_t.gif")},
      {"UnMap", get("unmap_t.gif")}
   };

   private static Image get(String var0) {
      return ImageLoader.getImage("org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images", var0);
   }

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
