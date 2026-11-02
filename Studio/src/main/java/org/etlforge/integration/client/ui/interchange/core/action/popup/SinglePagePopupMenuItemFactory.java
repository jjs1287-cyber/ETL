package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class SinglePagePopupMenuItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public SinglePagePopupMenuItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new SinglePagePopupMenuItemFactory.Item(this.action);
   }

   protected final Action getAction() {
      return this.action;
   }

   protected static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(this.isVisibleFor(var1, var2));
         this.setEnabled(this.isVisible() && this.getAction().isEnabled());
      }

      protected boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         if (!isSupportedSite(var1)) {
            return false;
         } else {
            return var2.size() != 1 ? false : var2.get(0) instanceof IntegrationEntity;
         }
      }

      private static boolean isSupportedSite(PopupMenuSite var0) {
         return var0 == PopupMenuSites.PAGE || var0 == PopupMenuSites.DETACHED_PAGE;
      }
   }
}
