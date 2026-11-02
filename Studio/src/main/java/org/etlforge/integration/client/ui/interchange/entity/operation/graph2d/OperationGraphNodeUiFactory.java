package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.CollapsedOperationNodeUi;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailNodeUi2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.ExpandedOperationNodeUi;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.NodeUiFactory2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class OperationGraphNodeUiFactory implements NodeUiFactory2D {
   @Override
   public GraphNodeUi2D createNodeUi(Graph2D var1, Object var2) {
      return this.createNodeUiImpl(var1, var2);
   }

   private GraphNodeUi2D createNodeUiImpl(Graph2D var1, Object var2) {
      if (var2 instanceof OperationWrapperNode) {
         return this.createOperationNodeUi(var1, (OperationWrapperNode)var2);
      } else if (var2 instanceof EmailWrapperNode) {
         return new EmailNodeUi2D(var1, (EmailWrapperNode)var2);
      } else if (var2 instanceof PipelineActivityNode) {
         return ((PipelineActivityNode)var2).createUi(var1);
      } else {
         throw new RuntimeException("unknown node type: " + var2);
      }
   }

   private GraphNodeUi2D createOperationNodeUi(Graph2D var1, OperationWrapperNode var2) {
      boolean var3 = this.shouldOpNodeBeExpanded(var1, var2);
      return var3 ? new ExpandedOperationNodeUi(var1, var2) : new CollapsedOperationNodeUi(var1, var2);
   }

   private boolean shouldOpNodeBeExpanded(Graph2D var1, OperationWrapperNode var2) {
      GraphLayout2D var3 = var1.getLayout();
      boolean var4 = false;
      if (var3 == null) {
         var4 = var2 == ((OperationGraph2D)var1).getModel().getStartNode();
      } else {
         var4 = var3.isExpanded(var2);
      }

      return var4;
   }
}
