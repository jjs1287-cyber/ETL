package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class NewFolderItemFactory extends AbstractPopupMenuItemFactory {
   private final EntityConfig folderConfig;

   public NewFolderItemFactory(EntityConfig var1) {
      this.folderConfig = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      ApplicationAction var1 = this.folderConfig.newEntityAction();
      return new NewFolderItemFactory.Item(var1);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(this.isVisibleFor(var1, var2));
         this.setEnabled(this.getAction().isEnabled() && this.isVisible());
      }

      private boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
         if (!var1.isTree()) {
            return false;
         }

         IntegrationEntity var3 = this.getEntityActedOn(var2);
         if (var3 == null) {
            return false;
         }

         boolean var4 = false;
         if (var3 instanceof IntegrationProject) {
            var4 = false;
         } else if (var3 instanceof Folder) {
            var4 = true;
         }

         return var4;
      }

      private IntegrationEntity getEntityActedOn(List<?> var1) {
         if (var1 != null && var1.size() == 1) {
            Object var2 = var1.get(0);
            if (var2 instanceof IntegrationEntity) {
               return (IntegrationEntity)var2;
            }
         }

         return null;
      }
   }
}
