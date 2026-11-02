package org.etlforge.ui.graph;

import java.util.EventObject;

public class GraphSelectionEvent extends EventObject {
   private final Object[] newSelection;

   public GraphSelectionEvent(GraphSelectionModel var1, Object[] var2) {
      super(var1);
      this.newSelection = clone(var2, false);
   }

   public GraphSelectionModel getSource() {
      return (GraphSelectionModel)super.getSource();
   }

   public Object[] getNewSelection() {
      return clone(this.newSelection, true);
   }

   private static Object[] clone(Object[] var0, boolean var1) {
      if (var0 == null) {
         return var1 ? new Object[0] : null;
      }

      int var2 = var0.length;
      if (var2 == 0) {
         return var0;
      }

      Object[] var3 = new Object[var2];
      System.arraycopy(var0, 0, var3, 0, var2);
      return var3;
   }
}
