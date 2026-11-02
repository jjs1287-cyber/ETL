package org.etlforge.ui.graph.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractGraphLayout implements GraphLayout {
   private final List<GraphLayoutListener> listeners = new CopyOnWriteArrayList<>();

   protected AbstractGraphLayout() {
   }

   @Override
   public void addGraphLayoutListener(GraphLayoutListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeGraphLayoutListener(GraphLayoutListener var1) {
      this.listeners.remove(var1);
   }

   protected void fireLayoutChanged() {
      GraphLayoutEvent var1 = new GraphLayoutEvent(this);
      this.fireLayoutChanged(var1);
   }

   protected void fireLayoutChanged(Object var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      this.fireLayoutChanged(var2);
   }

   protected void fireLayoutChanged(List var1) {
      GraphLayoutEvent var2 = new GraphLayoutEvent(this, var1);
      this.fireLayoutChanged(var2);
   }

   protected void fireLayoutChanged(GraphLayoutEvent var1) {
      for (GraphLayoutListener var3 : this.listeners) {
         var3.layoutChanged(var1);
      }
   }

   protected void fireZOrderChanged() {
      GraphLayoutEvent var1 = new GraphLayoutEvent(this);

      for (GraphLayoutListener var3 : this.listeners) {
         var3.zOrderChanged(var1);
      }
   }
}
