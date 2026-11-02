package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

class ClearActivityContentAction extends AbstractAction {
   private final OperationGraphController controller;
   private final PipelineActivityNode node;

   public ClearActivityContentAction(OperationGraphController var1, PipelineActivityNode var2) {
      super("Clear");
      this.controller = var1;
      this.node = var2;
      PipelineActivity var3 = var2.getDataObject();
      this.setEnabled(!var3.isReadOnly() && !var3.isEmpty());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.clearNodeContent(this.node);
   }
}
