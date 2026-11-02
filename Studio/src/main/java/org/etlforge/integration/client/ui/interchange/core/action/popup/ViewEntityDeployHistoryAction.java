package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ViewEntityDeployHistoryAction extends AbstractPopupMenuItemFactory {
   private final Action action;

   public ViewEntityDeployHistoryAction(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new ViewEntityDeployHistoryAction.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         if (var1 == PopupMenuSites.DETACHED_PAGE) {
            this.setVisible(false);
         } else {
            super.updateState(var1, var2);
            this.setVisible(Iterables.all(var2, Predicates.instanceOf(IntegrationEntity.class)));
            this.setEnabled(this.isVisible() && this.getAction().isEnabled());
         }
      }
   }
}
