package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractMenuBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class CopyEntityItemFactory extends AbstractPopupMenuItemFactory {
   private final List<? extends Action> actions;

   public CopyEntityItemFactory(List<? extends Action> var1) {
      this.actions = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      KongaMenu var1 = new KongaMenu("&Copy");
      return new CopyEntityItemFactory.ItemImpl(var1);
   }

   private static class Filter implements Predicate<Object> {
      private Filter() {
      }

      @Override
      public boolean apply(Object var1) {
         if (!(var1 instanceof IntegrationEntity)) {
            return false;
         }

         IntegrationEntity var2 = (IntegrationEntity)var1;
         return var2.isCopyable() || var2.isDeepCopyable();
      }
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setEnabled(this.getAction().isEnabled() && this.isVisible());
      }
   }

   private final class ItemImpl extends AbstractMenuBasedPopupMenuItem {
      private final List<PopupMenuItem> items = Lists.newArrayList();

      public ItemImpl(KongaMenu var2) {
         super(var2);
         this.addItems();
         var2.disableIfAllItemsAreDisabled();
      }

      private void addItems() {
         for (Action var2 : CopyEntityItemFactory.this.actions) {
            this.addMenuItem(new KongaMenuItem(var2));
            this.items.add(new CopyEntityItemFactory.Item(var2));
         }
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         boolean var3 = Iterables.any(var2, new CopyEntityItemFactory.Filter());
         this.setVisible(var3);

         for (PopupMenuItem var5 : this.items) {
            var5.updateState(var1, var2);
         }
      }
   }
}
