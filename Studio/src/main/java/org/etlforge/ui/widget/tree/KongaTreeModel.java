package org.etlforge.ui.widget.tree;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public interface KongaTreeModel extends TreeModel {
   KongaTreeNode getRoot();

   TreePath getPath(Object var1);

   void setFilter(KongaTreeFilter var1);

   KongaTreeFilter getFilter();

   void nodeChanged(KongaTreeNode var1);
}
