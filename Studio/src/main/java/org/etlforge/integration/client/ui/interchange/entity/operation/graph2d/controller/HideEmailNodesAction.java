package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphModel;
import org.etlforge.ui.action.ToggleAction;

public final class HideEmailNodesAction extends AbstractAction implements ToggleAction {
   private final OperationGraphController controller;

   public HideEmailNodesAction(OperationGraphController var1) {
      super("Hide Email Nodes");
      this.controller = var1;
      this.checkEnabled();
   }

   @Override
   public boolean isSelected() {
      return this.controller.getGraph().getLayout().isHidingEmailNodes();
   }

   @Override
   public void setSelected(boolean var1) {
      this.controller.getGraph().getLayout().setHideEmailNodes(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.setSelected(!this.isSelected());
      this.controller.getGraph().repaint();
   }

   private void checkEnabled() {
      OperationGraphModel var1 = this.controller.getGraph().getModel();
      this.setEnabled(!var1.getEmailNodes().isEmpty());
   }
}
