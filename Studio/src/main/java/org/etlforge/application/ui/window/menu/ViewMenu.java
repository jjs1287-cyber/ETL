package org.etlforge.application.ui.window.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class ViewMenu extends AbstractMenu {
   private final List<? extends Action> actions;

   public ViewMenu(List<? extends Action> var1) {
      super("menu.View");
      this.actions = var1;
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = new ArrayList();
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      var1.add(null);

      for (Action var3 : this.actions) {
         var1.add(new KongaMenuItem(var3));
      }

      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_2);
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_3);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_4);
      return var1;
   }
}
