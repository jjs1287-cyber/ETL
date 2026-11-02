package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.etlforge.ui.util.ImageLoader;

public final class TreeMapperIcons {
   public static final Icon LEAF_NODE = get("ftdoedi_t.gif");
   public static final Icon CLOSED_FOLDER_NODE = get("folder_closed_t.gif");
   public static final Icon OPEN_FOLDER_NODE = get("folder_open_t.gif");
   public static final Icon FUNCTION_NODE = get("fx.gif");
   public static final Icon CHECK = get("map_icon_t.gif");
   public static final Icon EXPAND = get("expand_t.gif");
   public static final Icon PARTIAL_EXPAND = get("partial_t.gif");
   public static final Icon COLLAPSE = get("collapse_t.gif");
   public static final Icon ZOOM_IN = get("zoomin_t.gif");
   public static final Icon ZOOM_OUT = get("zoomout_t.gif");
   public static final Icon FORMULA = get("formula_on_t.gif");
   public static final Icon MAP = get("map_on_t.gif");
   public static final Icon MULTIPLE_MAP = get("multiple_t.gif");
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images";

   private static ImageIcon get(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images", var0);
   }

   private TreeMapperIcons() {
   }
}
