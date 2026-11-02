package org.etlforge.application.ui.action.popup;

import com.google.common.base.Preconditions;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public abstract class AbstractMenuBasedPopupMenuItem implements PopupMenuItem {
   private final JMenu menu;
   private boolean visible;

   public AbstractMenuBasedPopupMenuItem(JMenu var1) {
      Preconditions.checkNotNull(var1, "menu");
      this.menu = var1;
   }

   protected final void addMenuItem(JMenuItem var1) {
      this.menu.add(var1);
   }

   @Override
   public void addToPopupMenu(JPopupMenu var1) {
      var1.add(this.menu);
   }

   public void setEnabled(boolean var1) {
      this.menu.setEnabled(var1);
   }

   @Override
   public boolean isVisible() {
      return this.visible;
   }

   protected final void setVisible(boolean var1) {
      this.visible = var1;
   }
}
