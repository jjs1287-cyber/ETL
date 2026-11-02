package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

final class ShowOptionsAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;

   public ShowOptionsAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(Strings.get("Options.Menu.Name"), ClientIcons.getIcon("Options.16"));
      this.controller = var1;
      this.node = var2;
      this.putValue("ShortDescription", Strings.get("Options.Menu.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowOptionsAction.this.controller.openOptionsUi(ShowOptionsAction.this.node);
         }
      });
   }
}
