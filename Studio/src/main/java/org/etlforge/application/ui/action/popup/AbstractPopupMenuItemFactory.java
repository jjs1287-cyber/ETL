package org.etlforge.application.ui.action.popup;

import java.util.List;

public abstract class AbstractPopupMenuItemFactory implements PopupMenuItemFactory {
   private PopupMenuItem item;
   private final boolean recycleItem;

   public AbstractPopupMenuItemFactory() {
      this(true);
   }

   public AbstractPopupMenuItemFactory(boolean var1) {
      this.recycleItem = var1;
   }

   @Override
   public final PopupMenuItem getMenuItem(PopupMenuSite var1, List<?> var2) {
      if (this.item == null || !this.recycleItem) {
         this.item = this.createItem();
      }

      this.item.updateState(var1, var2);
      return this.item.isVisible() ? this.item : null;
   }

   @Override
   public void reset() {
      this.item = null;
   }

   protected abstract PopupMenuItem createItem();
}
