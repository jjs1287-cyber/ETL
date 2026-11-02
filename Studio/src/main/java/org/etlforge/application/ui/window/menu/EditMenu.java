package org.etlforge.application.ui.window.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class EditMenu extends AbstractMenu {
   private final Action undo;
   private final Action redo;
   private final Action preferences;

   public EditMenu(Action var1, Action var2, Action var3) {
      super("menu.Edit");
      this.undo = var1;
      this.redo = var2;
      this.preferences = var3;
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = new ArrayList();
      var1.add(new KongaMenuItem(this.undo));
      var1.add(new KongaMenuItem(this.redo));
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      if (!ApplicationUi.hasMacMenus()) {
         var1.add(null);
         var1.add(new KongaMenuItem(this.preferences));
      }

      return var1;
   }
}
