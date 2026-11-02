package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

public final class ResetOperationNodeLayoutAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;

   public ResetOperationNodeLayoutAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(Strings.get("ResetOpNodeLayout.Name"));
      this.controller = var1;
      this.opNode = var2;
      this.putValue("ShortDescription", Strings.get("ResetOpNodeLayout.ToolTip"));
      this.putValue("AcceleratorKey", ResetLayoutAction.SHORTCUT);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.resetOperationNodeLayout(this.opNode);
   }
}
