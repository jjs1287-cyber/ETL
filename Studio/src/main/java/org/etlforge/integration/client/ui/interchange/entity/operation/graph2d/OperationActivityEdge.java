package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public final class OperationActivityEdge implements GraphEdge {
   private final OperationWrapperNode from;
   private final AbstractGraphNode to;
   private final Route route;

   public OperationActivityEdge(OperationWrapperNode var1, AbstractGraphNode var2, Route var3) {
      this.from = Preconditions.checkNotNull(var1, "from");
      this.to = Preconditions.checkNotNull(var2, "to");
      this.route = Preconditions.checkNotNull(var3, "route");
   }

   public OperationWrapperNode from() {
      return this.from;
   }

   public AbstractGraphNode to() {
      return this.to;
   }

   @Override
   public boolean connects(Object var1) {
      return var1 == this.from || var1 == this.to;
   }

   public Route getRoute() {
      return this.route;
   }

   public RouteType getType() {
      return this.route.getType();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof OperationActivityEdge ? this.equals((OperationActivityEdge)var1) : false;
      }
   }

   private boolean equals(OperationActivityEdge var1) {
      return this.getType() == var1.getType() && this.from == var1.from && this.to == var1.to;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.getType().hashCode();
      var1 = 31 * var1 + System.identityHashCode(this.from);
      return 31 * var1 + System.identityHashCode(this.to);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getType().toString());
      var1.append(" [").append(this.from).append(" ->").append(this.to).append("]");
      return var1.toString();
   }
}
