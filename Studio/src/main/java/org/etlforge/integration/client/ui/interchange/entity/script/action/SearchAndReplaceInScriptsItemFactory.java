package org.etlforge.integration.client.ui.interchange.entity.script.action;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;

public final class SearchAndReplaceInScriptsItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public SearchAndReplaceInScriptsItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new SearchAndReplaceInScriptsItemFactory.Item(this.action);
   }

   private static final class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         boolean var3 = this.getAction().isEnabled();
         this.setVisible(var3);
         this.setEnabled(var3);
      }
   }
}
