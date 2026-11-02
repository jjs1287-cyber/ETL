package org.etlforge.ui.widget.tree;

import javax.swing.Icon;
import javax.swing.tree.MutableTreeNode;
import org.etlforge.collections.IterableEnumeration;

public interface KongaTreeNode extends MutableTreeNode {
   KongaTreeNode getParent();

   String getDisplayName();

   boolean isFolder();

   Object getUserObject();

   void addChild(KongaTreeNode var1);

   KongaTreeNode getChildAt(int var1);

   Icon getNodeIcon(NodeStatus var1);

   IterableEnumeration<KongaTreeNode> preorderEnumeration();

   IterableEnumeration<KongaTreeNode> postorderEnumeration();

   IterableEnumeration<KongaTreeNode> breadthFirstEnumeration();

   IterableEnumeration<KongaTreeNode> depthFirstEnumeration();

   boolean sortChildren();

   IterableEnumeration<? extends KongaTreeNode> children();

   int getLevel();
}
