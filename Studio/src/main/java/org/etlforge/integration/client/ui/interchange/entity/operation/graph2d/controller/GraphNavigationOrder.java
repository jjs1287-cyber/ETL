package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.graph.NavigationOrder;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.ListSelectorWindow;
import org.etlforge.ui.widget.ListSelectorWindowListener;
import org.etlforge.ui.widget.label.KongaLabel;

final class GraphNavigationOrder implements NavigationOrder {
   private final OperationGraph2D graph;

   public GraphNavigationOrder(OperationGraph2D var1) {
      this.graph = var1;
   }

   @Override
   public Object next(Object var1) {
      if (var1 == null) {
         return this.getStartNode();
      } else if (var1 instanceof OperationWrapperNode) {
         return this.getNodeAfterOperationNode((OperationWrapperNode)var1);
      } else if (var1 instanceof EmailWrapperNode) {
         return var1;
      } else if (var1 instanceof PipelineActivityNode) {
         return this.getNextActivityNode(var1);
      } else {
         throw new RuntimeException("Unexpected node encountered: " + var1);
      }
   }

   @Override
   public Object previous(Object var1) {
      if (var1 == null) {
         return this.getStartNode();
      } else if (var1 instanceof OperationWrapperNode) {
         return this.getPreviousTopLevelNode((OperationWrapperNode)var1);
      } else if (var1 instanceof EmailWrapperNode) {
         return this.getPreviousTopLevelNode((EmailWrapperNode)var1);
      } else if (var1 instanceof PipelineActivityNode) {
         return this.getPreviousActivityNode(var1);
      } else {
         throw new RuntimeException("Unexpected node encountered: " + var1);
      }
   }

   private Object getNodeAfterOperationNode(OperationWrapperNode var1) {
      List var2 = KongaListUtils.filter(var1.getOutgoingRoutes(), Predicates.instanceOf(OperationActivityEdge.class));
      if (var2.isEmpty()) {
         return this.getStartNode();
      }

      HashSet var3 = Sets.newHashSet();

      for (Object __etlItem83 : var2) {
         GraphEdge var5 = (GraphEdge) __etlItem83;
         Object var6 = var5.to();
         if (var6 != var1) {
            var3.add(var6);
         }
      }

      return this.getNodeImpl(var1, var3);
   }

   private OperationWrapperNode getStartNode() {
      return this.graph.getModel().getStartNode();
   }

   private Object getPreviousTopLevelNode(AbstractGraphNode var1) {
      List var2 = KongaListUtils.filter(var1.getIncomingRoutes(), Predicates.instanceOf(OperationActivityEdge.class));
      if (var2.isEmpty()) {
         return var1;
      }

      HashSet var3 = Sets.newHashSet();

      for (Object __etlItem105 : var2) {
         GraphEdge var5 = (GraphEdge) __etlItem105;
         Object var6 = var5.from();
         if (var6 != var1) {
            var3.add(var6);
         }
      }

      return this.getNodeImpl(var1, var3);
   }

   private Object getNodeImpl(Object var1, Set<Object> var2) {
      switch (var2.size()) {
         case 0:
            return this.getStartNode();
         case 1:
            return var2.iterator().next();
         default:
            new GraphNavigationOrder.NextNodeSelector(this.graph, var2).display();
            return var1;
      }
   }

   private Object getNextActivityNode(Object var1) {
      PipelineActivityNode var2 = (PipelineActivityNode)var1;
      OperationWrapperNode var3 = var2.getParent();
      PipelineActivity var4 = var3.getPipeline().getNextActivity(var2.getDataObject());
      if (var4 == null) {
         var4 = var3.getPipeline().getFirstActivity();
      }

      PipelineActivityNode var5 = var3.getChildFor(var4);
      return var5 != null ? var5 : var1;
   }

   private Object getPreviousActivityNode(Object var1) {
      PipelineActivityNode var2 = (PipelineActivityNode)var1;
      OperationWrapperNode var3 = var2.getParent();
      PipelineActivity var4 = var3.getPipeline().getPreviousActivity(var2.getDataObject());
      if (var4 == null) {
         var4 = var3.getPipeline().getLastActivity();
      }

      PipelineActivityNode var5 = var3.getChildFor(var4);
      return var5 != null ? var5 : var1;
   }

   private static class NextNodeSelector implements ListSelectorWindowListener<GraphNavigationOrder.NextNodeSelector.Item> {
      private final OperationGraph2D graph;
      private final ListSelectorWindow<GraphNavigationOrder.NextNodeSelector.Item> window;

      public NextNodeSelector(OperationGraph2D var1, Set<Object> var2) {
         this.graph = var1;
         ArrayList var3 = Lists.newArrayList();

         for (Object var5 : var2) {
            var3.add(new GraphNavigationOrder.NextNodeSelector.Item(var5));
         }

         Collections.sort(var3, new GraphNavigationOrder.NextNodeSelector.ItemOrder());
         this.window = new ListSelectorWindow<>((GraphNavigationOrder.NextNodeSelector.Item[]) var3.toArray(new GraphNavigationOrder.NextNodeSelector.Item[var3.size()]));
         this.window.setListFactory(ComponentFactories.newKongaListFactory());
         this.window.addListener(this);
         this.window.addMoveDownKey(KeyUtils.RIGHT);
         this.window.addMoveDownKey(KeyUtils.TAB);
         this.window.addMoveUpKey(KeyUtils.LEFT);
         this.window.addMoveUpKey(KeyUtils.getShiftDown(9));
         KongaLabel var6 = TextStyles.AdminHint.makeLabel(Strings.get("NavigationOrder.Next.Select"));
         var6.setBorder(new BorderBuilder().bevel(0).empty(3));
         this.window.setCaption(var6);
      }

      public void display() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               NextNodeSelector.this.window.display(ListSelectorWindow.Direction.NEXT);
            }
         });
      }

      public void done(GraphNavigationOrder.NextNodeSelector.Item var1) {
         if (var1 != null) {
            this.graph.getSelectionModel().setSelection(Collections.singletonList(var1.node));
            this.graph.getLayout().moveToTop(var1.node);
            this.graph.ensureNodeIsVisible(var1.node);
         } else {
            this.graph.getSelectionModel().clearSelection();
         }

         this.graph.requestFocus();
      }

      @Override
      public void cancelled() {
         this.graph.requestFocus();
      }

      public void selectionChanged(GraphNavigationOrder.NextNodeSelector.Item var1) {
      }

      private static class Item implements ListDecorator {
         public final Object node;
         private final Icon icon;
         private final String text;
         private final EntityType type;

         public Item(Object var1) {
            this.node = var1;
            if (var1 instanceof OperationWrapperNode) {
               this.type = EntityType.Operation;
               this.text = ((OperationWrapperNode)var1).getDataObject().getName();
            } else {
               if (!(var1 instanceof EmailWrapperNode)) {
                  throw new RuntimeException("Unexpected node: " + var1);
               }

               this.type = EntityType.EmailMessage;
               this.text = ((EmailWrapperNode)var1).getDataObject().getActivity().getName();
            }

            this.icon = EntityIcons.forType(this.type).getDefaultIcon(EntityIcons.Size.SMALL);
         }

         @Override
         public void decorate(ListDecor var1, boolean var2) {
            var1.setText(this.text);
            var1.setToolTip(this.text);
            var1.setIcon(this.icon);
         }

         @Override
         public String toString() {
            return this.text;
         }
      }

      private static class ItemOrder implements Comparator<GraphNavigationOrder.NextNodeSelector.Item>, Serializable {
         private ItemOrder() {
         }

         public int compare(GraphNavigationOrder.NextNodeSelector.Item var1, GraphNavigationOrder.NextNodeSelector.Item var2) {
            return ComparisonChain.start().compare(var1.type, var2.type).compare(var1.text, var2.text, String.CASE_INSENSITIVE_ORDER).result();
         }
      }
   }
}
