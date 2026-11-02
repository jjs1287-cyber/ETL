package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ScriptSourceEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.DropBehavior;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.PipelineActivityDropBehavior;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.RejectDrop;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.script.ScriptedLinkSource;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.util.id.KongaGuid;

public abstract class PipelineActivityNode extends AbstractGraphNode {
   public PipelineActivityNode(PipelineActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public final OperationWrapperNode getParent() {
      return (OperationWrapperNode)super.getParent();
   }

   public final KongaGuid getId() {
      return this.getDataObject().getId();
   }

   public PipelineActivity getDataObject() {
      return (PipelineActivity)super.getDataObject();
   }

   public final void removeScriptSourceEdges() {
      if (this.getDataObject().getContent() instanceof ScriptedLinkSource) {
         for (GraphEdge var2 : this.getOutgoingRoutes()) {
            if (var2 instanceof ScriptSourceEdge) {
               this.removeOutgoingRoute(var2);
            }
         }
      }
   }

   public final boolean isReadOnly() {
      PipelineActivity var1 = this.getDataObject();
      return var1.isReadOnly();
   }

   public abstract GraphNodeUi2D createUi(Graph2D var1);

   public DropBehavior getDropBehavior(OperationGraphController var1) {
      return this.isReadOnly() ? new RejectDrop() : new PipelineActivityDropBehavior(var1, this);
   }
}
