package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.base.Predicate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ScriptSourceEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;

class RouteTypeFilter implements Predicate<GraphEdge> {
   private static final Predicate<Object> ACCEPTS_ALL_NODES = new Predicate<Object>() {
      @Override
      public boolean apply(Object var1) {
         return true;
      }
   };
   private final OperationWrapperNode mainNode;
   private final EnumSet<RouteType> types;
   private final boolean isAll;

   public RouteTypeFilter(OperationWrapperNode var1, EnumSet<RouteType> var2) {
      this.mainNode = var1;
      this.types = var2.clone();
      this.isAll = var2.size() == RouteType.values().length;
   }

   public RouteTypeFilter copy(OperationWrapperNode var1) {
      return new RouteTypeFilter(var1, this.types);
   }

   public boolean acceptsAll() {
      return this.isAll;
   }

   public boolean apply(GraphEdge var1) {
      if (this.isAll) {
         return true;
      } else if (var1 instanceof OperationActivityEdge) {
         return this.types.contains(((OperationActivityEdge)var1).getType());
      } else {
         return var1 instanceof ScriptSourceEdge ? this.types.contains(RouteType.SCRIPTED) : true;
      }
   }

   public Predicate<Object> asNodeFilter() {
      return this.isAll ? ACCEPTS_ALL_NODES : new RouteTypeFilter.NodeFilter(this.getReachableNodes());
   }

   public Set<Object> getReachableNodes() {
      RouteTypeFilter.Traverser var1 = new RouteTypeFilter.Traverser();
      return var1.collect();
   }

   public EnumSet<RouteType> getAcceptedTypes() {
      return this.types.clone();
   }

   private static class NodeFilter implements Predicate<Object> {
      private final Set<Object> reachable;

      public NodeFilter(Set<Object> var1) {
         this.reachable = var1;
      }

      @Override
      public boolean apply(Object var1) {
         return var1 instanceof PipelineActivityNode ? this.apply(((PipelineActivityNode)var1).getParent()) : this.reachable.contains(var1);
      }
   }

   private class Traverser {
      private final Set<Object> visited = new HashSet<>();

      public Traverser() {
      }

      public Set<Object> collect() {
         this.traverse(RouteTypeFilter.this.mainNode);
         return this.visited;
      }

      private void traverse(AbstractGraphNode var1) {
         if (!this.visited.contains(var1)) {
            this.visited.add(var1);

            for (GraphEdge var3 : var1.getOutgoingRoutes()) {
               if (RouteTypeFilter.this.apply(var3)) {
                  this.traverse((AbstractGraphNode)var3.to());
               }
            }
         }
      }
   }
}
