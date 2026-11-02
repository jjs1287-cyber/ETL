package org.etlforge.ui.graph.edge;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractGraphEdgeUi implements GraphEdgeUi {
   private final List<GraphEdgeListener> listeners = new CopyOnWriteArrayList<>();

   @Override
   public final void addGraphEdgeListener(GraphEdgeListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public final void removeGraphEdgeListener(GraphEdgeListener var1) {
      this.listeners.add(var1);
   }

   protected final void fireLayoutChanged() {
      GraphEdgeEvent var1 = new GraphEdgeEvent(this);

      for (GraphEdgeListener var3 : this.listeners) {
         var3.edgeLayoutChanged(var1);
      }
   }

   protected final void fireStateChanged() {
      GraphEdgeEvent var1 = new GraphEdgeEvent(this);

      for (GraphEdgeListener var3 : this.listeners) {
         var3.edgeStateChanged(var1);
      }
   }
}
