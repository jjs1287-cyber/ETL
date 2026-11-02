package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class NewIntegrationEntityItemFactory extends AbstractPopupMenuItemFactory {
   private final EntityConfigLookup configLookup;

   public NewIntegrationEntityItemFactory(EntityConfigLookup var1) {
      this.configLookup = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new NewIntegrationEntityItemFactory.Item(this.configLookup);
   }

   private static class Item implements PopupMenuItem {
      private final EntityConfigLookup configLookup;
      private Action action;

      public Item(EntityConfigLookup var1) {
         this.configLookup = var1;
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         var1.add(new KongaMenuItem(this.action));
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.action = null;
         if (var2.size() == 1) {
            Object var3 = var2.get(0);
            if (var3 instanceof Folder) {
               EntityType var4 = ((Folder)var3).getItemType();
               EntityConfig var5 = this.configLookup.getEntityConfig(var4);
               this.action = var5.newEntityAction();
            }
         }
      }

      @Override
      public boolean isVisible() {
         return this.action != null;
      }
   }
}
