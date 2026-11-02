package org.etlforge.integration.client.ui.interchange.component.menu.resources;

import java.util.ListResourceBundle;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.util.ImageLoader;

public final class MenuResources extends ListResourceBundle {
   private static final String ICON_PACKAGE = "org.etlforge.integration.client.ui.interchange.component.menu.resources.images";
   private static final String NAME = ".Name";
   private static final String ICON = ".Icon";
   private static final Object[][] contents = new Object[][]{
      {"File.New.Name", "&New"},
      {"File.New.Icon", ApplicationResources.getIcon("New.16")},
      {"menu.ic.Transformation.Name", "&Transformation"},
      {"menu.ic.tx.Tools.Name", "&Advanced"},
      {"menu.ic.tx.Tools.Icon", getIcon("TransformationToolkit16.gif")},
      {"menu.ic.tx.ShowData.Name", "&Show Only Data"},
      {"menu.ic.tx.Edit.Name", "&Edit"},
      {"menu.ic.tx.Export.Name", "E&xport"},
      {"menu.ic.tx.Import.Name", "&Import"},
      {"menu.ic.tx.NodeExpansion.Name", "XML Node E&xpansion Mode"},
      {"menu.ic.tx.MapArea.Name", "Map &Area Settings"},
      {"menu.ic.tx.SimpleXmlElementDisplay.Name", "Simple XML Element Display"},
      {"menu.ic.tx.SimpleNodeDisplay.Name", "Simple Node Display"},
      {"menu.ic.tx.ShowMappedNodes.Name", "Hide Unmapped Nodes"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.interchange.component.menu.resources.images", var0);
   }
}
