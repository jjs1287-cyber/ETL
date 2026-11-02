package org.etlforge.ui.graph.edge;

public class DefaultGraphEdge implements GraphEdge {
   private final Object from;
   private final Object to;

   public DefaultGraphEdge(Object var1, Object var2) {
      this.from = var1;
      this.to = var2;
   }

   @Override
   public boolean connects(Object var1) {
      return var1 != null && (var1 == this.from || var1 == this.to);
   }

   @Override
   public Object from() {
      return this.from;
   }

   @Override
   public Object to() {
      return this.to;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof GraphEdge ? this.equals((GraphEdge)var1) : false;
      }
   }

   private boolean equals(GraphEdge var1) {
      return this.from == var1.from() && this.to == var1.to();
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + System.identityHashCode(this.from);
      return 31 * var1 + System.identityHashCode(this.to);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Edge [");
      var1.append(this.from).append(" ->").append(this.to).append("]");
      return var1.toString();
   }
}
