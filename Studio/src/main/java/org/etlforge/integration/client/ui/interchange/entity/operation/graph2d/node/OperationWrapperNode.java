package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationActivityEdge;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public final class OperationWrapperNode extends AbstractGraphNode {
   private OperationNodeController nodeController;

   public OperationWrapperNode(OperationWrapper var1) {
      super(var1, null);
   }

   public OperationWrapper getDataObject() {
      return (OperationWrapper)super.getDataObject();
   }

   public OperationPipeline getPipeline() {
      return this.getDataObject().getPipeline();
   }

   public OperationType getOperationType() {
      return this.getPipeline().getType();
   }

   public Operation getOperation() {
      return this.getDataObject().getActivity();
   }

   public OperationId getOperationId() {
      return this.getOperation().getID();
   }

   @Override
   public List<PipelineActivityNode> getChildren() {
      ArrayList var1 = Lists.newArrayList();

      for (AbstractGraphNode var3 : super.getChildren()) {
         var1.add((PipelineActivityNode)var3);
      }

      return var1;
   }

   public PipelineActivityNode getChildAt(int var1) {
      return (PipelineActivityNode)super.getChildAt(var1);
   }

   public PipelineActivityNode getChildFor(PipelineActivity var1) {
      for (AbstractGraphNode var3 : super.getChildren()) {
         if (var3.getDataObject() == var1) {
            return (PipelineActivityNode)var3;
         }
      }

      return null;
   }

   @Override
   public void removeAllChildren() {
      for (AbstractGraphNode var2 : super.getChildren()) {
         ((PipelineActivityNode)var2).removeScriptSourceEdges();
      }

      super.removeAllChildren();
   }

   public OperationNodeController getNodeController() {
      return this.nodeController;
   }

   public void setNodeController(OperationNodeController var1) {
      this.nodeController = var1;
   }

   public void addOutgoingRoute(Route var1, AbstractGraphNode var2) {
      this.addOutgoingRoute(new OperationActivityEdge(this, var2, var1));
   }

   public boolean isOperationTypeChangeable() {
      return this.getDataObject().isOperationTypeChangeable();
   }
}
