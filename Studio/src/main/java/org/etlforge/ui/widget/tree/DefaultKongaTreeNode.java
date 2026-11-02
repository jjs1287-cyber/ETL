package org.etlforge.ui.widget.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import org.etlforge.collections.IterableEnumeration;

public class DefaultKongaTreeNode extends DefaultMutableTreeNode implements KongaTreeNode {
   private static final Comparator<KongaTreeNode> CHILDNODE_COMPARATOR = new Comparator<KongaTreeNode>() {
      public int compare(KongaTreeNode var1, KongaTreeNode var2) {
         return var1.getDisplayName().compareTo(var2.getDisplayName());
      }
   };

   public DefaultKongaTreeNode(KongaTreeNode var1, Object var2, boolean var3) {
      super(var2, var3);
      if (var1 != null) {
         var1.addChild(this);
      }
   }

   public DefaultKongaTreeNode(Object var1, boolean var2) {
      this(null, var1, var2);
   }

   @Override
   public KongaTreeNode getParent() {
      return (KongaTreeNode)super.getParent();
   }

   @Override
   public KongaTreeNode getChildAt(int var1) {
      return (KongaTreeNode)super.getChildAt(var1);
   }

   @Override
   public void addChild(KongaTreeNode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot add a null node to a parent node.");
      }

      int var2 = this.getChildCount();
      this.insert(var1, var2);
      var1.setParent(this);
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return null;
   }

   @Override
   public boolean sortChildren() {
      if (this.getChildCount() >= 2) {
         Collections.sort(super.children, this.getNodeComparator());
         return true;
      } else {
         return false;
      }
   }

   protected Comparator<KongaTreeNode> getNodeComparator() {
      return CHILDNODE_COMPARATOR;
   }

   @Override
   public boolean isFolder() {
      return false;
   }

   @Override
   public boolean isLeaf() {
      return super.isLeaf() && !this.getAllowsChildren();
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject() == null ? "" : this.getUserObject().toString();
   }

   @Override
   public IterableEnumeration<KongaTreeNode> breadthFirstEnumeration() {
      return iterableEnum(super.breadthFirstEnumeration());
   }

   private static IterableEnumeration<KongaTreeNode> iterableEnum(Enumeration<?> var0) {
      return new IterableEnumeration<>(new DefaultKongaTreeNode.EnumImpl(var0));
   }

   @Override
   public IterableEnumeration<KongaTreeNode> depthFirstEnumeration() {
      return iterableEnum(super.depthFirstEnumeration());
   }

   @Override
   public IterableEnumeration<KongaTreeNode> postorderEnumeration() {
      return iterableEnum(super.postorderEnumeration());
   }

   @Override
   public IterableEnumeration<KongaTreeNode> preorderEnumeration() {
      return iterableEnum(super.preorderEnumeration());
   }

   @Override
   public IterableEnumeration<? extends KongaTreeNode> children() {
      return iterableEnum(super.children());
   }

   @Override
   public String toString() {
      return this.getDisplayName();
   }

   private static class EnumImpl implements Enumeration<KongaTreeNode> {
      private final Enumeration<?> base;

      EnumImpl(Enumeration<?> var1) {
         this.base = var1;
      }

      @Override
      public boolean hasMoreElements() {
         return this.base.hasMoreElements();
      }

      public KongaTreeNode nextElement() {
         return (KongaTreeNode)this.base.nextElement();
      }
   }
}
