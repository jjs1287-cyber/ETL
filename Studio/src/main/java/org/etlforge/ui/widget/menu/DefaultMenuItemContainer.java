package org.etlforge.ui.widget.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public final class DefaultMenuItemContainer implements MenuItemContainer {
   private final JMenu menu;

   public DefaultMenuItemContainer(JMenu var1) {
      this.menu = var1;
   }

   @Override
   public void add(JMenuItem var1) {
      this.menu.add(var1);
   }

   @Override
   public void addSeparator() {
      this.menu.addSeparator();
   }

   @Override
   public void removeAll() {
      this.menu.removeAll();
   }
}
