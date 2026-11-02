package org.etlforge.application.ui.window.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;

public final class ActionsMenu extends AbstractMenu {
   public ActionsMenu() {
      super("menu.Actions");
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = new ArrayList();
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_2);
      return var1;
   }
}
