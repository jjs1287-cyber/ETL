package org.etlforge.integration.client.ui.interchange.core.action.popup;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.project.IntegrationProject;

public class ProjectActionItemFactory extends AbstractPopupMenuItemFactory {
   private final Action action;

   public ProjectActionItemFactory(Action var1) {
      this.action = var1;
   }

   @Override
   protected final PopupMenuItem createItem() {
      return new ProjectActionItemFactory.Item(this.action);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         this.setVisible(var2.size() == 1 && var2.get(0) instanceof IntegrationProject);
         this.setEnabled(this.isVisible());
      }
   }
}
