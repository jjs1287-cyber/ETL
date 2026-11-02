package org.etlforge.application.ui.window.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class FileMenu extends AbstractMenu {
   private final List<? extends Action> actions;
   private final Action exit;

   public FileMenu(List<? extends Action> var1, Action var2) {
      super("menu.File");
      this.actions = var1;
      this.exit = var2;
   }

   @Override
   protected List<JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_1);
      var1.add(null);

      for (Action var3 : this.actions) {
         if (var3 == null) {
            var1.add(null);
         } else {
            var1.add(new KongaMenuItem(var3));
         }
      }

      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_2);
      var1.add(null);
      this.addExtensionItems(var1, MenuExtensionPoint.EXTENSION_POINT_3);
      if (!ApplicationUi.hasMacMenus()) {
         var1.add(null);
         var1.add(new KongaMenuItem(this.exit));
      }

      return var1;
   }
}
