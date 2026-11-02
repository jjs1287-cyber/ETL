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
import org.etlforge.integration.data.project.IntegrationProject;

public final class DeployActiveObjectsItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public DeployActiveObjectsItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new DeployActiveObjectsItemFactory.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         boolean var3 = !var2.isEmpty() && Iterables.all(var2, new DeployActiveObjectsItemFactory.Item.Filter());
         this.setVisible(var3);
         this.setEnabled(this.getAction().isEnabled());
      }

      private static class Filter implements Predicate<Object> {
         private Filter() {
         }

         @Override
         public boolean apply(Object var1) {
            return var1 instanceof IntegrationEntity && !(var1 instanceof IntegrationProject);
         }
      }
   }
}
