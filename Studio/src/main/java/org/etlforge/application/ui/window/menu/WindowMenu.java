package org.etlforge.application.ui.window.menu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.window.editor.service.RecentlyClosedEditors;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class WindowMenu extends AbstractMenu {
   private final List<? extends Action> actions;

   public WindowMenu(List<? extends Action> var1, RecentlyClosedEditors var2) {
      super("menu.Windows");
      this.actions = var1;
      this.addExtension(MenuExtensionPoint.EXTENSION_POINT_1, var2.getMenuContribution());
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = new ArrayList();

      for (Action var3 : this.actions) {
         var1.add(new KongaMenuItem(var3));
      }

      var1.add(new WindowDisplayModeMenu().getMenu());
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_2);
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_4);
      return var1;
   }
}
