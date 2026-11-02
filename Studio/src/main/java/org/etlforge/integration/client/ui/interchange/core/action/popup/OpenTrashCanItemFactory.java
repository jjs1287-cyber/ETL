package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.client.ui.interchange.ShowTrashCanInProjectTreeProperty;
import org.etlforge.integration.data.project.IntegrationProject;

public final class OpenTrashCanItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public OpenTrashCanItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected PopupMenuItem createItem() {
      return new OpenTrashCanItemFactory.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         if (ShowTrashCanInProjectTreeProperty.ENABLED) {
            this.setVisible(false);
         } else {
            super.updateState(var1, var2);
            this.setVisible(var2.size() == 1 && var2.get(0) instanceof IntegrationProject);
            this.setEnabled(this.isVisible());
         }
      }
   }
}
