package org.etlforge.ui.graph.model;

import org.etlforge.ui.graph2d.layout.GraphLayout2D;

public enum NodeExpansionState {
   EXPANDED {
      @Override
      public void apply(GraphLayout2D var1, Object var2) {
         var1.expand(var2);
      }
   },
   COLLAPSED {
      @Override
      public void apply(GraphLayout2D var1, Object var2) {
         var1.collapse(var2);
      }
   };

   NodeExpansionState() {
   }

   public static NodeExpansionState get(GraphLayout2D var0, Object var1) {
      return var0.isExpanded(var1) ? EXPANDED : COLLAPSED;
   }

   public abstract void apply(GraphLayout2D var1, Object var2);
}
