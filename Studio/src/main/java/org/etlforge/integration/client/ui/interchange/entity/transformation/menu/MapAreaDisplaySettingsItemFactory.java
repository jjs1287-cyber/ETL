package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class MapAreaDisplaySettingsItemFactory extends MenuBasedPagePopupMenuItemFactory {
   public MapAreaDisplaySettingsItemFactory(EditorService var1) {
      super(var1);
   }

   @Override
   protected Menu createMenu(TransformationPage var1) {
      return new MapAreaSettingsMenu(var1);
   }
}
