package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.ui.util.ImageLoader;

final class PackageResources {
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images";
   static final Icon HELP_ICON = CommonIcons.HELP_16;
   static final Icon SEARCH_ICON = ApplicationResources.ICONS.getIcon("Find.16");
   static final Icon TRANSFER_LEFT = Icons.TRANSFER_LEFT_ICON;
   static final Icon EXPAND_ICON = ImageLoader.getImageIcon(
      "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images", "expand_t.gif"
   );
   static final Icon COLLAPSE_ICON = ImageLoader.getImageIcon(
      "org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.images", "collapse_t.gif"
   );

   private PackageResources() {
   }
}
