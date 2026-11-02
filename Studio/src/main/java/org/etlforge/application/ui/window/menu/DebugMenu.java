package org.etlforge.application.ui.window.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.ui.widget.menu.KongaMenuItem;

final class DebugMenu extends AbstractMenu {
   private final List<? extends Action> actions;

   public DebugMenu(List<? extends Action> var1) {
      super("menu.Debug");
      this.actions = var1;
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = new ArrayList();

      for (Action var3 : this.actions) {
         var1.add(new KongaMenuItem(var3));
      }

      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_2);
      return var1;
   }

   @Override
   public boolean isSupported() {
      return DebugMode.IS_ENABLED;
   }
}
