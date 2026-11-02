package org.etlforge.application.ui.action.popup;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.swing.Action;

public class SingleItemPopupMenuFactory extends AbstractPopupMenuItemFactory {
   private final Action action;
   private final Predicate<Object> filter;
   private Set<PopupMenuSite> unsupportedSites;

   public SingleItemPopupMenuFactory(Action var1, Class<?> var2) {
      this(var1, Predicates.instanceOf(var2));
   }

   public SingleItemPopupMenuFactory(Action var1, Predicate<Object> var2) {
      this.action = var1;
      this.filter = var2;
   }

   @Override
   protected final PopupMenuItem createItem() {
      return new SingleItemPopupMenuFactory.Item(this.action, this.filter, this.unsupportedSites);
   }

   protected final void addUnsupportedSite(PopupMenuSite var1) {
      if (this.unsupportedSites == null) {
         this.unsupportedSites = Sets.newHashSet();
      }

      this.unsupportedSites.add(var1);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      private final Predicate<Object> filter;
      private final Set<PopupMenuSite> unsupportedSites;

      public Item(Action var1, Predicate<Object> var2, Set<PopupMenuSite> var3) {
         super(var1);
         this.filter = var2;
         this.unsupportedSites = var3;
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         if (this.isSupported(var1)) {
            super.updateState(var1, var2);
            this.setVisible(Iterables.any(var2, this.filter));
            this.setEnabled(this.getAction().isEnabled() && this.isVisible() && var2.size() == 1);
         } else {
            this.setVisible(false);
         }
      }

      private boolean isSupported(PopupMenuSite var1) {
         return this.unsupportedSites == null || !this.unsupportedSites.contains(var1);
      }
   }
}
