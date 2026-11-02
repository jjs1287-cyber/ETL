package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Sets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphEdgeOrder;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphModel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;

class InitialLayoutPositioner {
   private static final Comparator<GraphEdge> ROUTE_ORDER = new GraphEdgeOrder(
      new RouteType[]{RouteType.SCRIPTED, RouteType.SUCCESS, RouteType.FAILURE, RouteType.SIBLING}
   );
   private final OperationGraphModel model;
   private final Map<OperationWrapperNode, OperationNodeLayout> opNodeLayouts;
   private final Set<Object> processedNodes;
   private final Map<EmailWrapperNode, Rectangle> emailNodeBounds;

   InitialLayoutPositioner(OperationGraphModel var1, Map<OperationWrapperNode, OperationNodeLayout> var2, Map<EmailWrapperNode, Rectangle> var3) {
      this.model = var1;
      this.opNodeLayouts = var2;
      this.emailNodeBounds = var3;
      this.processedNodes = new HashSet<>();
   }

   void adjustLayout() {
      this.processedNodes.clear();
      this.processNodes(Collections.singletonList(this.model.getStartNode()), null);
   }

   private void processNodes(Iterable<? extends AbstractGraphNode> var1, Rectangle var2) {
      Rectangle var3 = null;

      for (AbstractGraphNode var5 : var1) {
         this.processedNodes.add(var5);
         var3 = this.layoutNode(var5, var2, var3);
      }

      for (AbstractGraphNode var10 : var1) {
         LinkedHashSet var6 = Sets.newLinkedHashSet();

         for (GraphEdge var8 : this.getOutgoingRoutes(var10)) {
            if (!this.processedNodes.contains(var8.to())) {
               var6.add((AbstractGraphNode)var8.to());
            }
         }

         if (!var6.isEmpty()) {
            this.processNodes(var6, this.getBoundsFor(var10));
         }
      }
   }

   private Rectangle getBoundsFor(AbstractGraphNode var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.opNodeLayouts.get(var1).getOperationNodeBounds();
      } else if (var1 instanceof EmailWrapperNode) {
         return this.emailNodeBounds.get(var1);
      } else {
         throw new RuntimeException("Unexpected node type: " + var1);
      }
   }

   private Rectangle layoutNode(AbstractGraphNode var1, Rectangle var2, Rectangle var3) {
      int var4;
      int var5;
      if (var2 == null) {
         var4 = 10;
         var5 = 10;
      } else {
         if (var3 != null) {
            var4 = var3.x + var3.width;
         } else {
            var4 = var2.x;
         }

         var4 += 50;
         var5 = var2.y + var2.height + 50;
      }

      Point var6 = new Point(var4, var5);
      if (var1 instanceof OperationWrapperNode) {
         OperationNodeLayout var9 = this.opNodeLayouts.get(var1);
         var9.setOperationNodeLocation(var6);
         return var9.getOperationNodeBounds();
      } else if (var1 instanceof EmailWrapperNode) {
         Rectangle var7 = this.emailNodeBounds.get(var1);
         var7.setLocation(var6);
         return var7;
      } else {
         throw new RuntimeException("Unexpected node type: " + var1);
      }
   }

   private Iterable<? extends GraphEdge> getOutgoingRoutes(AbstractGraphNode var1) {
      List var2 = var1.getOutgoingRoutes();
      Collections.sort(var2, ROUTE_ORDER);
      return var2;
   }
}
