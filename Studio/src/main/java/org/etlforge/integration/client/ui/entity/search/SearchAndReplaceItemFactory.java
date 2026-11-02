package org.etlforge.integration.client.ui.entity.search;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaDataLookup;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class SearchAndReplaceItemFactory extends AbstractPopupMenuItemFactory {
   private final SearchAndReplaceAction action;

   public SearchAndReplaceItemFactory(ApplicationWindow var1, SearchAndReplaceMetaDataLookup var2, EntityPersistor var3) {
      this.action = new SearchAndReplaceAction("search.and.replace", var1.getFrame(), var1.getActiveObjectService(), var2);
      this.action.setPersistor(var3);
   }

   @Override
   protected PopupMenuItem createItem() {
      return new SearchAndReplaceItemFactory.Item(this.action);
   }

   private static final class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(var2.size() == 1 && var2.get(0) instanceof Folder);
         this.setEnabled(this.isVisible());
      }
   }
}
