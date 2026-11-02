package org.etlforge.integration.activity.ui.log.tree;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.swing.tree.TreePath;
import org.etlforge.collections.IterableEnumeration;

final class ExpansionMemento {
   private final Set<Object> expandedEntryIds = Sets.newHashSet();

   public ExpansionMemento(OperationLogTree var1) {
      for (Object var3 : var1.getExpandedNodes()) {
         if (var3 instanceof OperationNode) {
            this.expandedEntryIds.add(((OperationNode)var3).getUserObject().getID());
         }
      }
   }

   public void restore(OperationLogTree var1) {
      RootNode var2 = (RootNode)var1.getTreeTableModel().getRoot();
      OperationLogTreeModel var3 = (OperationLogTreeModel)var1.getTreeTableModel();

      for (Object var5 : IterableEnumeration.of(var2.children())) {
         this.checkExpansionState(var1, var3, (OperationNode)var5);
      }
   }

   private void checkExpansionState(OperationLogTree var1, OperationLogTreeModel var2, OperationNode var3) {
      if (this.expandedEntryIds.contains(var3.getUserObject().getID())) {
         var1.expandPath(new TreePath(var2.getPathToRoot(var3)));

         for (Object var5 : IterableEnumeration.of(var3.children())) {
            this.checkExpansionState(var1, var2, (OperationNode)var5);
         }
      }
   }
}
