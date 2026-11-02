package org.etlforge.integration.activity.ui.log.tree;

import java.util.Collections;
import java.util.Comparator;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

final class RootNode extends DefaultMutableTreeTableNode {
   public RootNode() {
      super("root", true);
   }

   public void sortChildren(Comparator<OperationLogEntry> var1) {
      Collections.sort(this.children, new RootNode.NodeOrder(var1));
   }

   private static class NodeOrder implements Comparator<MutableTreeTableNode> {
      private final Comparator<OperationLogEntry> impl;

      public NodeOrder(Comparator<OperationLogEntry> var1) {
         this.impl = var1;
      }

      public int compare(MutableTreeTableNode var1, MutableTreeTableNode var2) {
         return this.impl.compare(getLogEntry(var1), getLogEntry(var2));
      }

      private static OperationLogEntry getLogEntry(MutableTreeTableNode var0) {
         return ((OperationNode)var0).getUserObject();
      }
   }
}
