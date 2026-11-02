package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import java.util.Collections;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;

final class JumpIntoOperationNodeAction extends AbstractSingleObjectSelectionAction<OperationWrapperNode> {
   private final OperationGraph2D graph;

   public JumpIntoOperationNodeAction(OperationGraph2D var1) {
      super(OperationWrapperNode.class);
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.DOWN);
   }

   protected void run(OperationWrapperNode var1, ActionEvent var2) {
      PipelineActivityNode var3 = var1.getChildFor(var1.getPipeline().getFirstActivity());
      if (var3 != null) {
         this.graph.getLayout().expand(var1);
         this.graph.getSelectionModel().setSelection(Collections.singletonList(var3));
      }
   }
}
