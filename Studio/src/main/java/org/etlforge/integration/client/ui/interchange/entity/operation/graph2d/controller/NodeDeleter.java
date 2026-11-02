package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.graph.node.AbstractGraphNode;

final class NodeDeleter {
   private final OperationGraphController controller;

   public NodeDeleter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean delete(AbstractGraphNode var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.deleteOperationNode((OperationWrapperNode)var1);
      } else if (var1 instanceof EmailWrapperNode) {
         return this.deleteEmailNode((EmailWrapperNode)var1);
      } else {
         return var1 instanceof PipelineActivityNode ? this.deletePipelineActivityNode((PipelineActivityNode)var1) : false;
      }
   }

   private boolean deleteOperationNode(OperationWrapperNode var1) {
      return new OperationNodeDeleter(this.controller, var1).delete();
   }

   private boolean deleteEmailNode(EmailWrapperNode var1) {
      return new EmailNodeDeleter(this.controller).delete(var1);
   }

   private boolean deletePipelineActivityNode(PipelineActivityNode var1) {
      return new PipelineActivityRemover(this.controller).removeActivity(var1);
   }
}
