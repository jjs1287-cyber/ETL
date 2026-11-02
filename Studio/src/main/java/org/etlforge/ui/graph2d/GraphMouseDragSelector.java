package org.etlforge.ui.graph2d;

import com.google.common.collect.Maps;
import java.awt.Container;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.util.AbstractMouseDragSelector;

public final class GraphMouseDragSelector extends AbstractMouseDragSelector {
   private final GraphController2D controller;
   private final GraphLayout2D layout;

   GraphMouseDragSelector(Container var1, GraphController2D var2, GraphLayout2D var3) {
      super(var1);
      this.controller = var2;
      this.layout = var3;
   }

   @Override
   protected boolean allowDragging() {
      return this.isShiftDown();
   }

   @Override
   protected void onDragging() {
   }

   @Override
   protected void onInitialize() {
   }

   private int getLevelOfNode(Object var1) {
      int var2 = 0;
      GraphModel var3 = this.controller.getGraph().getModel();

      while ((var1 = var3.getParentOf(var1)) != null) {
         var2++;
      }

      return var2;
   }

   private void addAtLevel(Map<Integer, Collection<Object>> var1, Object var2, int var3) {
      Collection var4 = (Collection)var1.get(var3);
      if (var4 == null) {
         var4 = new ArrayList();
         var1.put(var3, var4);
      }

      var4.add(var2);
   }

   private Collection<Object> filterByLevel(Collection<Object> var1) {
      HashMap var2 = Maps.newHashMap();

      for (Object var4 : var1) {
         this.addAtLevel(var2, var4, this.getLevelOfNode(var4));
      }

      Collection var6 = null;
      Collection var8 = null;

      for (int var5 = 0; (var6 = (Collection)var2.get(var5)) != null; var5++) {
         if (var6.size() == 1) {
            var8 = var6;
         } else if (var6.size() > 1) {
            break;
         }
      }

      return var6 == null ? var8 : var6;
   }

   @Override
   protected void onStopDrag() {
      Rectangle var1 = this.getDraggingArea();
      HashSet var2 = new HashSet<>(this.layout.getNodesWithin(var1));
      List var3 = this.controller.getGraph().getSelectionModel().getSelection();
      var2.addAll(var3);
      Collection var4 = this.filterByLevel(var2);
      if (var4 == null) {
         this.controller.getGraph().getSelectionModel().clearSelection();
      } else {
         this.controller.getGraph().getSelectionModel().setSelection(var4);
      }
   }
}
