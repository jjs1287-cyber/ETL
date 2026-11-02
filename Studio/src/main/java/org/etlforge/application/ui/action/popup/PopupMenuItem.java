package org.etlforge.application.ui.action.popup;

import java.util.List;
import javax.swing.JPopupMenu;

public interface PopupMenuItem {
   PopupMenuItem NULL = new PopupMenuItem() {
      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
      }

      @Override
      public boolean isVisible() {
         return false;
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
      }
   };

   void addToPopupMenu(JPopupMenu var1);

   void updateState(PopupMenuSite var1, List<?> var2);

   boolean isVisible();
}
