package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;

public final class DefaultSingleNodeAction extends AbstractSingleObjectSelectionAction<AbstractGraphNode> {
   private final OperationGraphController controller;

   public DefaultSingleNodeAction(OperationGraphController var1) {
      super(AbstractGraphNode.class);
      this.controller = var1;
      this.putValue("AcceleratorKey", KeyUtils.ENTER);
   }

   protected void run(AbstractGraphNode var1, ActionEvent var2) {
      if (var1 instanceof OperationWrapperNode) {
         this.processOperationNode((OperationWrapperNode)var1, var2);
      } else if (var1 instanceof EmailWrapperNode) {
         this.processEmailNode((EmailWrapperNode)var1);
      } else if (var1 instanceof PipelineActivityNode) {
         this.processPipelineActivityNode((PipelineActivityNode)var1);
      }
   }

   private void processOperationNode(OperationWrapperNode var1, ActionEvent var2) {
      if (this.controller.getGraph().getLayout().isExpanded(var1)) {
         new ShowExpandedOptionsAction(this.controller, var1).actionPerformed(var2);
      } else {
         this.controller.toggleNodeExpansion(var1);
      }
   }

   private void processEmailNode(EmailWrapperNode var1) {
      this.controller.openEditor(var1.getDataObject().getActivity());
   }

   private void processPipelineActivityNode(PipelineActivityNode var1) {
      PipelineActivity var2 = var1.getDataObject();
      if (var2.isEmpty()) {
         this.controller.selectActivity(var1);
      } else {
         EntityEditorLauncher.openEditor(var1);
      }
   }
}
