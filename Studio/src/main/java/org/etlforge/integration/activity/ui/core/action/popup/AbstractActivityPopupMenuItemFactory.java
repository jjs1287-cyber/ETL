package org.etlforge.integration.activity.ui.core.action.popup;

import com.google.common.base.Predicate;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.integration.activity.ui.core.action.ActivityAction;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class AbstractActivityPopupMenuItemFactory extends AbstractPopupMenuItemFactory {
   private final ActivityAction action;

   protected AbstractActivityPopupMenuItemFactory(ActivityAction var1) {
      this.action = var1;
   }

   protected final void setPageDisplayer(EditorDisplayer var1) {
      this.action.setPageDisplayer(var1);
   }

   @Override
   protected final PopupMenuItem createItem() {
      return new AbstractActivityPopupMenuItemFactory.Item(this.action);
   }

   private static final class Item extends AbstractActionBasedPopupMenuItem {
      private static final AbstractActivityPopupMenuItemFactory.Item.Filter FILTER = new AbstractActivityPopupMenuItemFactory.Item.Filter();

      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         List var3 = KongaListUtils.filter(var2, FILTER);
         this.setVisible(!var3.isEmpty());
         this.setEnabled(var3.size() == 1 && var2.size() == 1);
      }

      private static final class Filter implements Predicate<Object> {
         private Filter() {
         }

         @Override
         public boolean apply(Object var1) {
            return var1 instanceof IntegrationEntity && ActivityViewUtils.isValidType((IntegrationEntity)var1);
         }
      }
   }
}
