package org.etlforge.application.ui.action.popup;

import java.util.List;
import javax.swing.JPopupMenu;

final class PopupMenuSeparator implements PopupMenuItem {
   @Override
   public void addToPopupMenu(JPopupMenu var1) {
      var1.addSeparator();
   }

   @Override
   public boolean isVisible() {
      return true;
   }

   @Override
   public void updateState(PopupMenuSite var1, List<?> var2) {
   }
}
