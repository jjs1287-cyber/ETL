package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public abstract class MenuBasedPagePopupMenuItemFactory extends AbstractPopupMenuItemFactory {
   private final EditorService editors;

   public MenuBasedPagePopupMenuItemFactory(EditorService var1) {
      super(false);
      this.editors = var1;
   }

   @Override
   protected final PopupMenuItem createItem() {
      Editor var1 = this.editors.getActiveEditor();
      if (var1 instanceof TransformationPage) {
         TransformationPage var2 = (TransformationPage)var1;
         Menu var3 = this.createMenu(var2);
         return new MenuBasedPagePopupMenuItem(var3);
      } else {
         return PopupMenuItem.NULL;
      }
   }

   protected abstract Menu createMenu(TransformationPage var1);
}
