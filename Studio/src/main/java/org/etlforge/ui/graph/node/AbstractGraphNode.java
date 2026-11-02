package org.etlforge.ui.graph.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.etlforge.ui.graph.edge.GraphEdge;

public abstract class AbstractGraphNode {
   private AbstractGraphNode parent;
   private final List<AbstractGraphNode> children;
   private final Object dataObject;
   private final Set<GraphEdge> incoming;
   private final Set<GraphEdge> outgoing;

   public AbstractGraphNode(Object var1, AbstractGraphNode var2) {
      this.dataObject = Preconditions.checkNotNull(var1, "dataObject");
      this.parent = var2;
      this.incoming = Sets.newHashSet();
      this.outgoing = Sets.newHashSet();
      this.children = Lists.newArrayList();
      if (var2 != null) {
         var2.children.add(this);
      }
   }

   public Object getDataObject() {
      return this.dataObject;
   }

   public AbstractGraphNode getParent() {
      return this.parent;
   }

   public List<? extends AbstractGraphNode> getChildren() {
      return new ArrayList<>(this.children);
   }

   public AbstractGraphNode getChildAt(int var1) {
      return this.children.get(var1);
   }

   public final int getChildCount() {
      return this.children.size();
   }

   public final void addChild(AbstractGraphNode var1) {
      Preconditions.checkArgument(var1 != this, "A node cannot be added as a child to itself");
      if (var1.parent != this) {
         var1.removeFromParent();
         var1.parent = this;
         this.children.add(var1);
      }
   }

   public final void removeFromParent() {
      if (this.parent != null) {
         this.parent.children.remove(this);
         this.parent = null;
      }
   }

   public void removeAllChildren() {
      for (AbstractGraphNode var2 : this.children) {
         var2.parent = null;
      }

      this.children.clear();
   }

   public final List<GraphEdge> getIncomingRoutes() {
      return Lists.newArrayList(this.incoming);
   }

   public final void addOutgoingRoute(GraphEdge var1) {
      Preconditions.checkArgument(var1.from() == this, "out does not originate from this node");
      Object var2 = var1.to();
      Preconditions.checkArgument(var2 instanceof AbstractGraphNode, "out does not end in an AbstractGraphNode");
      this.outgoing.add(var1);
      ((AbstractGraphNode)var2).incoming.add(var1);
   }

   public final void removeOutgoingRoute(GraphEdge var1) {
      this.outgoing.remove(var1);
      ((AbstractGraphNode)var1.to()).incoming.remove(var1);
   }

   public final void removeAllOutgoingRoutes() {
      for (GraphEdge var2 : this.getOutgoingRoutes()) {
         this.removeOutgoingRoute(var2);
      }
   }

   public final List<GraphEdge> getOutgoingRoutes() {
      return Lists.newArrayList(this.outgoing);
   }

   @Override
   public String toString() {
      return this.getDataObject().toString();
   }

   public boolean isRenamable() {
      return true;
   }

   public void printToConsole() {
      StringBuilder var1 = new StringBuilder(this.toString());
      var1.append("\nIncoming Routes:");

      for (GraphEdge var3 : this.incoming) {
         var1.append("\n  ").append(var3.from());
      }

      var1.append("\nOutgoing Routes:");

      for (GraphEdge var5 : this.outgoing) {
         var1.append("\n  ").append(var5.to());
      }

      System.out.println(var1.append("\n"));
   }
}
