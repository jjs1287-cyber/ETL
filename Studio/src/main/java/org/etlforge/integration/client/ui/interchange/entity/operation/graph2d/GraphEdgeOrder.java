package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public class GraphEdgeOrder implements Comparator<GraphEdge>, Serializable {
   private final RouteType[] routeTypeOrder;

   public GraphEdgeOrder() {
      this.routeTypeOrder = RouteType.values();
   }

   public GraphEdgeOrder(RouteType[] var1) {
      this.routeTypeOrder = (RouteType[])var1.clone();
   }

   public int compare(GraphEdge var1, GraphEdge var2) {
      if (var1 instanceof OperationActivityEdge) {
         return var2 instanceof OperationActivityEdge ? this.compareRouteTypes((OperationActivityEdge)var1, (OperationActivityEdge)var2) : -1;
      } else {
         return var2 instanceof OperationActivityEdge ? 1 : 0;
      }
   }

   private int compareRouteTypes(OperationActivityEdge var1, OperationActivityEdge var2) {
      AbstractGraphNode var3 = var1.to();
      AbstractGraphNode var4 = var2.to();
      if (var3 instanceof OperationWrapperNode) {
         if (!(var4 instanceof OperationWrapperNode)) {
            return -1;
         }
      } else if (var4 instanceof OperationWrapperNode) {
         return 1;
      }

      int var5 = ArrayUtils.indexOf(this.routeTypeOrder, var1.getType());
      int var6 = ArrayUtils.indexOf(this.routeTypeOrder, var2.getType());
      int var7 = var5 - var6;
      if (var7 == 0) {
         var7 = var3.toString().compareToIgnoreCase(var4.toString());
      }

      return var7;
   }
}
