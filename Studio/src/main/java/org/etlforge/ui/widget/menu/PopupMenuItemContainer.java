package org.etlforge.ui.widget.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupMenuItemContainer implements MenuItemContainer {
   private final JPopupMenu popup;

   public PopupMenuItemContainer(JPopupMenu var1) {
      this.popup = var1;
   }

   @Override
   public void add(JMenuItem var1) {
      this.popup.add(var1);
   }

   @Override
   public void addSeparator() {
      this.popup.addSeparator();
   }

   @Override
   public void removeAll() {
      this.popup.removeAll();
   }
}
