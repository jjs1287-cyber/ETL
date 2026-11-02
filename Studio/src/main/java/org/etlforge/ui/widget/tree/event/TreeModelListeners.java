package org.etlforge.ui.widget.tree.event;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class TreeModelListeners extends AbstractListenerManager<TreeModelListener> implements ListenerManager<TreeModelListener> {
   protected TreeModelListener createNotifier() {
      return new TreeModelListener() {
         @Override
         public void treeNodesChanged(TreeModelEvent var1) {
            for (TreeModelListener var3 : TreeModelListeners.this.getListeners()) {
               var3.treeNodesChanged(var1);
            }
         }

         @Override
         public void treeNodesInserted(TreeModelEvent var1) {
            for (TreeModelListener var3 : TreeModelListeners.this.getListeners()) {
               var3.treeNodesInserted(var1);
            }
         }

         @Override
         public void treeNodesRemoved(TreeModelEvent var1) {
            for (TreeModelListener var3 : TreeModelListeners.this.getListeners()) {
               var3.treeNodesRemoved(var1);
            }
         }

         @Override
         public void treeStructureChanged(TreeModelEvent var1) {
            for (TreeModelListener var3 : TreeModelListeners.this.getListeners()) {
               var3.treeStructureChanged(var1);
            }
         }
      };
   }
}
