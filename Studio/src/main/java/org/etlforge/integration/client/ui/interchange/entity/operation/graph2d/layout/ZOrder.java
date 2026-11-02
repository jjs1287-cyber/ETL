package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

final class ZOrder {
   private final OperationGraphLayout graphLayout;
   private final LinkedList<Object> layers;

   public ZOrder(OperationGraphLayout var1) {
      this.graphLayout = var1;
      this.layers = new LinkedList<>();
   }

   public void clear() {
      this.layers.clear();
   }

   public void add(EmailWrapperNode var1) {
      this.layers.add(var1);
   }

   public void add(OperationWrapperNode var1, List<?> var2) {
      ZOrder.OperationLayer var3 = this.getOperationLayer(var1);
      if (var3 == null) {
         var3 = new ZOrder.OperationLayer(var1);
      }

      var3.addNodes(var2);
      this.moveOperationToTop(var3);
   }

   public int getZOrder(Object var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.getOperationNodeZOrder((OperationWrapperNode)var1);
      } else {
         return var1 instanceof EmailWrapperNode ? this.getEmailZOrder((EmailWrapperNode)var1) : this.getPipelineNodeZOrder(var1);
      }
   }

   public List<Object> getZOrder() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.layers) {
         if (var3 instanceof ZOrder.OperationLayer) {
            ZOrder.OperationLayer var4 = (ZOrder.OperationLayer)var3;
            if (!this.graphLayout.isCollapsed(var4.opNode)) {
               var1.addAll(var4.parts);
            }

            var1.add(var4.opNode);
         } else {
            var1.add(var3);
         }
      }

      return var1;
   }

   public boolean moveToTop(Object var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.moveOperationToTop((OperationWrapperNode)var1);
      } else {
         return var1 instanceof EmailWrapperNode ? this.moveEmailNodeToTop((EmailWrapperNode)var1) : this.movePipelineNodeToTop(var1);
      }
   }

   public void moveNodeToTop(OperationWrapperNode var1, Object var2) {
      ZOrder.OperationLayer var3 = this.getOperationLayer(var1);
      var3.moveNodeToTop(var2);
      this.moveOperationToTop(var3);
   }

   public void removeOperation(OperationWrapperNode var1) {
      ZOrder.OperationLayer var2 = this.getOperationLayer(var1);
      this.layers.remove(var2);
   }

   public void removeEmail(EmailWrapperNode var1) {
      this.layers.remove(var1);
   }

   public void removePipelineNodes(List<Object> var1) {
      for (Object var3 : this.layers) {
         if (var3 instanceof ZOrder.OperationLayer) {
            ((ZOrder.OperationLayer)var3).removeNodes(var1);
         }
      }
   }

   private boolean moveEmailNodeToTop(EmailWrapperNode var1) {
      if (this.layers.getFirst() == var1) {
         return false;
      }

      this.layers.remove(var1);
      this.layers.addFirst(var1);
      return true;
   }

   private boolean moveOperationToTop(OperationWrapperNode var1) {
      ZOrder.OperationLayer var2 = this.getOperationLayer(var1);
      return var2 != null && this.moveOperationToTop(var2);
   }

   private ZOrder.OperationLayer getOperationLayer(OperationWrapperNode var1) {
      for (Object var3 : this.layers) {
         if (var3 instanceof ZOrder.OperationLayer && ((ZOrder.OperationLayer)var3).opNode == var1) {
            return (ZOrder.OperationLayer)var3;
         }
      }

      return null;
   }

   private boolean moveOperationToTop(ZOrder.OperationLayer var1) {
      if (!this.layers.isEmpty() && this.layers.getFirst() == var1) {
         return false;
      }

      this.layers.remove(var1);
      this.layers.addFirst(var1);
      return true;
   }

   private boolean movePipelineNodeToTop(Object var1) {
      if (!this.layers.isEmpty()) {
         Object var2 = this.layers.getFirst();
         if (var2 instanceof ZOrder.OperationLayer && ((ZOrder.OperationLayer)var2).top() == var1) {
            return false;
         }
      }

      for (Object var3 : this.layers) {
         if (var3 instanceof ZOrder.OperationLayer) {
            ZOrder.OperationLayer var4 = (ZOrder.OperationLayer)var3;
            if (var4.contains(var1)) {
               var4.moveToTop(var1);
               this.moveOperationToTop(var4);
               return true;
            }
         }
      }

      return false;
   }

   private int getOperationNodeZOrder(OperationWrapperNode var1) {
      int var2 = 0;

      for (Object var4 : this.layers) {
         if (var4 instanceof ZOrder.OperationLayer) {
            ZOrder.OperationLayer var5 = (ZOrder.OperationLayer)var4;
            if (!this.graphLayout.isCollapsed(var5.opNode)) {
               var2 += var5.size();
            }

            if (var5.opNode == var1) {
               return this.graphLayout.isCollapsed(var1) ? var2 : var2 - 1;
            }
         } else {
            var2++;
         }
      }

      return -1;
   }

   private int getPipelineNodeZOrder(Object var1) {
      int var2 = 0;

      for (Object var4 : this.layers) {
         if (var4 instanceof ZOrder.OperationLayer) {
            ZOrder.OperationLayer var5 = (ZOrder.OperationLayer)var4;
            int var6 = var5.getZOrder(var1);
            if (var6 >= 0) {
               return var6 + var2;
            }

            if (!this.graphLayout.isCollapsed(var5.opNode)) {
               var2 += var5.size();
            }
         } else {
            var2++;
         }
      }

      return -1;
   }

   private int getEmailZOrder(EmailWrapperNode var1) {
      int var2 = 0;

      for (Object var4 : this.layers) {
         if (var4 == var1) {
            return var2;
         }

         if (var4 instanceof ZOrder.OperationLayer) {
            var2 += ((ZOrder.OperationLayer)var4).size();
         } else {
            var2++;
         }
      }

      return -1;
   }

   private static class OperationLayer {
      private final OperationWrapperNode opNode;
      private final LinkedList<Object> parts;

      public OperationLayer(OperationWrapperNode var1) {
         this.opNode = var1;
         this.parts = new LinkedList<>(var1.getChildren());
      }

      public Object top() {
         return this.parts.isEmpty() ? null : this.parts.getFirst();
      }

      public int size() {
         return this.parts.size() + 1;
      }

      public void addNodes(List<?> var1) {
         for (Object var3 : var1) {
            this.moveNodeToTop(var3);
         }
      }

      public void moveToTop(Object var1) {
         if (!(var1 instanceof OperationWrapperNode)) {
            this.moveNodeToTop(var1);
         }
      }

      public void removeNodes(List<Object> var1) {
         this.parts.removeAll(var1);
      }

      private void moveNodeToTop(Object var1) {
         this.parts.remove(var1);
         this.parts.addFirst(var1);
      }

      public int getZOrder(Object var1) {
         return this.parts.indexOf(var1);
      }

      public boolean contains(Object var1) {
         return this.parts.contains(var1);
      }

      @Override
      public String toString() {
         return this.opNode.getDataObject().getName() + " [" + this.parts.size() + "]";
      }
   }
}
