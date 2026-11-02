package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.ui.graph.edge.GraphEdge;

public final class PipelineActivityEdge implements GraphEdge {
   private final PipelineActivityNode from;
   private final PipelineActivityNode to;

   public PipelineActivityEdge(PipelineActivityNode var1, PipelineActivityNode var2) {
      if (var1 == null) {
         throw new NullPointerException("from");
      }

      if (var2 == null) {
         throw new NullPointerException("to");
      }

      this.from = var1;
      this.to = var2;
   }

   public PipelineActivityNode from() {
      return this.from;
   }

   public PipelineActivityNode to() {
      return this.to;
   }

   public ActivityConnection asConnectionPoint() {
      return new ActivityConnection(this.from.getDataObject(), this.to.getDataObject());
   }

   @Override
   public boolean connects(Object var1) {
      return var1 == this.from || var1 == this.to;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof PipelineActivityEdge ? this.equals((PipelineActivityEdge)var1) : false;
      }
   }

   private boolean equals(PipelineActivityEdge var1) {
      return this.from == var1.from && this.to == var1.to;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + System.identityHashCode(this.from);
      return 31 * var1 + System.identityHashCode(this.to);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("ActivityEdge ");
      var1.append(" [").append(this.from).append(" ->").append(this.to).append("]");
      return var1.toString();
   }
}
