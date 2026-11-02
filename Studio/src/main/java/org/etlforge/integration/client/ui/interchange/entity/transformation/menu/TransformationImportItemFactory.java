package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class TransformationImportItemFactory extends MenuBasedPagePopupMenuItemFactory {
   public TransformationImportItemFactory(EditorService var1) {
      super(var1);
   }

   @Override
   protected Menu createMenu(TransformationPage var1) {
      return new ImportMenu(var1);
   }
}
