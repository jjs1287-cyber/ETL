package com.etlforge.integration.client.ui.sfdc.action;

import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.action.ActiveObjectsAction;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class CopySfdcWizardItemFactory extends AbstractPopupMenuItemFactory {
   private final ActiveObjectsAction deepCopyAction;

   public CopySfdcWizardItemFactory(ActiveObjectsAction var1) {
      this.deepCopyAction = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new CopySfdcWizardItemFactory.Item(this.deepCopyAction);
   }

   private static class Item implements PopupMenuItem {
      private final ActiveObjectsAction deepCopyAction;
      private boolean visible;

      public Item(ActiveObjectsAction var1) {
         this.deepCopyAction = var1;
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         KongaMenuItem var2 = new KongaMenuItem(this.deepCopyAction);
         var2.setText("Copy");
         var1.add(var2);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.visible = var2.size() == 1 && var2.get(0) instanceof SalesforceWizardEntity;
      }

      @Override
      public boolean isVisible() {
         return this.visible;
      }
   }
}
