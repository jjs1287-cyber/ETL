package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import java.util.Collections;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;

class JumpOutOfOperationNodeAction extends AbstractSingleObjectSelectionAction<PipelineActivityNode> {
   private final OperationGraph2D graph;

   public JumpOutOfOperationNodeAction(OperationGraph2D var1) {
      super(PipelineActivityNode.class);
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.UP);
   }

   protected void run(PipelineActivityNode var1, ActionEvent var2) {
      this.graph.getSelectionModel().setSelection(Collections.singletonList(var1.getParent()));
   }
}
