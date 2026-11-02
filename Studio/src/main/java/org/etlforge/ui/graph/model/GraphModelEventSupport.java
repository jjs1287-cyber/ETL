package org.etlforge.ui.graph.model;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.ui.graph.edge.GraphEdge;

public final class GraphModelEventSupport {
   private final GraphModel model;
   private final List<GraphModelListener> listeners = new CopyOnWriteArrayList<>();
   private Function<Iterable<? extends GraphEdge>, Iterable<? extends GraphEdge>> addEdgesOrderingStrategy = Functions.identity();
   private GraphModelState modelState;
   private int stateChangeRequests;
   private boolean coalesceRemovedNodes = true;
   private boolean blocked;

   public GraphModelEventSupport(GraphModel var1) {
      this.model = Preconditions.checkNotNull(var1, "model");
   }

   public void setBlocked(boolean var1) {
      this.blocked = var1;
   }

   public void setAddEdgesOrderingStrategy(Function<Iterable<? extends GraphEdge>, Iterable<? extends GraphEdge>> var1) {
      this.addEdgesOrderingStrategy = Preconditions.checkNotNull(var1, "s");
   }

   public void setCoalesceRemovedNodes(boolean var1) {
      this.coalesceRemovedNodes = var1;
   }

   public void startChange() {
      if (!this.blocked) {
         if (this.stateChangeRequests == 0) {
            this.modelState = new GraphModelState(this.model);
            this.modelState.setAddEdgesOrderingStrategy(this.addEdgesOrderingStrategy);
         }

         this.stateChangeRequests++;
      }
   }

   public void endChangeAndSendEvents() {
      if (!this.blocked) {
         Preconditions.checkState(this.stateChangeRequests > 0, "startChange must be called first");
         this.stateChangeRequests--;
         if (this.stateChangeRequests == 0) {
            this.modelState.calculateDiffAndSendEvents(this, this.coalesceRemovedNodes);
            this.modelState = null;
         }
      }
   }

   public void addListener(GraphModelListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(GraphModelListener var1) {
      this.listeners.remove(var1);
   }

   public GraphModelListener[] getAllListeners() {
      return this.listeners.toArray(new GraphModelListener[this.listeners.size()]);
   }

   public void fireModelChanged() {
      GraphModelEvent var1 = new GraphModelEvent(this.model);

      for (GraphModelListener var3 : this.listeners) {
         var3.modelChanged(var1);
      }
   }

   public void fireModelChanged(GraphModelEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.modelChanged(var1);
      }
   }

   public void fireNodeInserted(Object var1, Object var2, Object var3) {
      this.fireNodesInserted(var1, var2, Collections.singletonList(var3));
   }

   public void fireNodesInserted(Object var1, Object var2, List<?> var3) {
      NodesInsertedEvent var4 = new NodesInsertedEvent(this.model, var1, var2, var3);

      for (GraphModelListener var6 : this.listeners) {
         var6.nodesInserted(var4);
      }
   }

   public void fireNodesInserted(NodesInsertedEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.nodesInserted(var1);
      }
   }

   public void fireNodeRemoved(Object var1) {
      this.fireNodesRemoved(Collections.singletonList(var1));
   }

   public void fireNodesRemoved(List<? extends Object> var1) {
      NodesRemovedEvent var2 = new NodesRemovedEvent(this.model, var1);
      this.fireNodesRemoved(var2);
   }

   public void fireNodesRemoved(NodesRemovedEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.nodesRemoved(var1);
      }
   }

   public void fireNodesConnected(GraphEdge var1) {
      NodesConnectedEvent var2 = new NodesConnectedEvent(this.model, var1);
      this.fireNodesConnected(var2);
   }

   public void fireNodesConnected(NodesConnectedEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.nodesConnected(var1);
      }
   }

   public void fireNodesDisconnected(GraphEdge var1) {
      NodesDisconnectedEvent var2 = new NodesDisconnectedEvent(this.model, var1);
      this.fireNodesDisconnected(var2);
   }

   public void fireNodesDisconnected(NodesDisconnectedEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.nodesDisconnected(var1);
      }
   }

   public void fireNodeChanged(Object var1) {
      NodeChangedEvent var2 = new NodeChangedEvent(this.model, var1);
      this.fireNodeChanged(var2);
   }

   public void fireNodeChanged(NodeChangedEvent var1) {
      for (GraphModelListener var3 : this.listeners) {
         var3.nodeChanged(var1);
      }
   }
}
