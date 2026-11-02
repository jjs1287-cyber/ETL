package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DeleteEntityItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public DeleteEntityItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new DeleteEntityItemFactory.Item(this.action);
   }

   private static class Filter implements Predicate<Object> {
      private Filter() {
      }

      @Override
      public boolean apply(Object var1) {
         return var1 instanceof IntegrationEntity && ((IntegrationEntity)var1).isDeletable();
      }
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      private final DeleteEntityItemFactory.Filter filter = new DeleteEntityItemFactory.Filter();

      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         if (this.isSupported(var1)) {
            super.updateState(var1, var2);
            this.setVisible(!var2.isEmpty() && Iterables.all(var2, this.filter));
            this.setEnabled(this.getAction().isEnabled() && this.isVisible() && !var2.isEmpty());
         } else {
            this.setVisible(false);
         }
      }

      private boolean isSupported(PopupMenuSite var1) {
         return var1 != PopupMenuSites.DETACHED_PAGE;
      }
   }
}
