package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;

public final class RunAllOperationsItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public RunAllOperationsItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new RunAllOperationsItemFactory.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(this.isVisibleFor(var2));
         this.setEnabled(this.isVisible() && this.getEnabledState(var2));
      }

      private boolean isVisibleFor(List<?> var1) {
         int var2 = var1.size();
         if (var2 == 1) {
            return this.checkVisibilityForSingleObject(var1.get(0));
         } else {
            return var2 > 1 ? this.checkVisibilityForMultipleObjects(var1) : false;
         }
      }

      private boolean checkVisibilityForSingleObject(Object var1) {
         return this.isObjectOperationHolder(var1);
      }

      public boolean checkVisibilityForMultipleObjects(List<?> var1) {
         for (Object var3 : var1) {
            if (!this.isValidObject(var3)) {
               return false;
            }
         }

         return true;
      }

      private boolean isObjectOperationHolder(Object var1) {
         return var1 instanceof Folder && ((Folder)var1).getItemType() == EntityType.Operation;
      }

      private boolean isValidObject(Object var1) {
         return var1 instanceof Operation || var1 instanceof IntegrationProject || this.isObjectOperationHolder(var1);
      }

      public boolean getEnabledState(List<?> var1) {
         boolean var2 = this.getAction().isEnabled();
         if (var2 && var1.size() == 1) {
            var2 = !(var1.get(0) instanceof Operation);
         }

         return var2;
      }
   }
}
