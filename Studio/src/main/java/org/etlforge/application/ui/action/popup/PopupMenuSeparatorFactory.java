package org.etlforge.application.ui.action.popup;

import java.util.List;

public final class PopupMenuSeparatorFactory implements PopupMenuItemFactory {
   public static final PopupMenuItemFactory FACTORY = new PopupMenuSeparatorFactory();

   public static PopupMenuItemFactory instance() {
      return FACTORY;
   }

   private PopupMenuSeparatorFactory() {
   }

   @Override
   public PopupMenuItem getMenuItem(PopupMenuSite var1, List<?> var2) {
      return new PopupMenuSeparator();
   }

   @Override
   public void reset() {
   }
}
