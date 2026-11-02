package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class EmailContextMenuFactory {
   private final OperationGraphController controller;

   public EmailContextMenuFactory(OperationGraphController var1) {
      this.controller = var1;
   }

   public KongaPopupMenu createMenu(EmailWrapperNode var1) {
      DeleteNodeAction var2 = new DeleteNodeAction(this.controller);
      var2.setSelection(SelectionFactory.newOrderedSelection(var1));
      KongaPopupMenu var3 = new KongaPopupMenu();
      var3.addActions(this.getEditEmailActions(var1));
      var3.addActions(
         new RenameNodeAction(this.controller, var1),
         new LocateInTreeAction(var1),
         null,
         this.controller.getDeploySelectedItemsAction(),
         new ViewValidationMessagesAction(this.controller.getGraph(), var1),
         this.controller.getSetPermissionsAction(),
         null,
         new SetNodeCoordinatesAction(this.controller, var1),
         null,
         var2
      );
      return var3;
   }

   private List<Action> getEditEmailActions(EmailWrapperNode var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new EditEmailAction(this.controller, var1));
      return var2;
   }
}
