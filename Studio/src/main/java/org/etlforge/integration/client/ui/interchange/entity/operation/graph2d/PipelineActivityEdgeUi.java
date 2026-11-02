package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.graph2d.edge.LineSegmentGraphEdgeUi;

final class PipelineActivityEdgeUi extends LineSegmentGraphEdgeUi {
   private final InsertionPointIcon insertionPointIcon;

   public PipelineActivityEdgeUi(OperationGraph2D var1, PipelineActivityEdge var2) {
      super(var1, var2);
      if (this.isInsertionPoint(var2)) {
         this.insertionPointIcon = new InsertionPointIcon(var1);
         this.setIcon(this.insertionPointIcon);
         this.setIconToolTip("This is an insertion point. Right-click to see the options for inserting new items here.");
      } else {
         this.insertionPointIcon = null;
      }
   }

   private boolean isInsertionPoint(PipelineActivityEdge var1) {
      OperationWrapperNode var2 = var1.from().getParent();
      OperationPipeline var3 = var2.getPipeline();
      ActivityInsertionPolicy var4 = var3.getInsertionPolicy();
      return !var4.getAllowedActivities(var1.asConnectionPoint()).isEmpty();
   }

   @Override
   public void setDropStatus(DropStatus var1) {
      if (this.insertionPointIcon != null) {
         this.insertionPointIcon.setDropStatus(var1);
      }
   }
}
