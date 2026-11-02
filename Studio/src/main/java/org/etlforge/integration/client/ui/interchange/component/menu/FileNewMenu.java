package org.etlforge.integration.client.ui.interchange.component.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class FileNewMenu {
   private final EntityConfigLookup configLookup;
   private final NewEntityMenuFactory menuFactory;

   public FileNewMenu(EntityConfigLookup var1, NewEntityMenuFactory var2) {
      this.configLookup = var1;
      this.menuFactory = var2;
   }

   public final JPopupMenu asPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();

      for (JMenuItem var4 : this.menuFactory.createMenuItems(this.configLookup)) {
         if (var4 == null) {
            var1.addSeparator();
         } else {
            var1.add(var4);
         }
      }

      return var1;
   }
}
