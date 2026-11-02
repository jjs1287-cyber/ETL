package org.etlforge.integration.client.ui.interchange.entity.operation.actions;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SetOperationOptionsItemFactory extends AbstractPopupMenuItemFactory {
   private final ApplicationWindow appWin;
   private final EntityExplorerSupport projectBrowser;
   private final ProjectPersistor persistor;

   public SetOperationOptionsItemFactory(ApplicationWindow var1, EntityExplorerSupport var2, ProjectPersistor var3) {
      this.appWin = var1;
      this.projectBrowser = var2;
      this.persistor = var3;
   }

   @Override
   protected PopupMenuItem createItem() {
      SetOperationOptionsAction var1 = new SetOperationOptionsAction(this.appWin, this.projectBrowser, this.persistor);
      return new SetOperationOptionsItemFactory.Item(var1);
   }

   private static class Item extends AbstractActionBasedPopupMenuItem {
      public Item(Action var1) {
         super(var1);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         if (var1.isTree()) {
            boolean var3 = Iterables.any(var2, Predicates.instanceOf(Operation.class));
            this.setVisible(var3);
            this.setEnabled(this.getAction().isEnabled() && this.isVisible());
         } else {
            this.setVisible(false);
         }
      }
   }
}
