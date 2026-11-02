package org.etlforge.ui.widget.tree;

import java.awt.EventQueue;
import java.awt.Rectangle;
import javax.swing.Icon;
import org.etlforge.ui.util.rename.ItemRenamedEvent;
import org.etlforge.ui.util.rename.ItemRenamedListener;
import org.etlforge.ui.util.rename.ItemRenamer;

final class RenameNodeHandler implements ItemRenamedListener {
   private final KongaTree tree;
   private ItemRenamer nodeRenamer;
   private RenameNodeStrategy renameStrategy;
   private KongaTreeNode node;

   public RenameNodeHandler(KongaTree var1) {
      this.tree = var1;
   }

   public void onRenameNodeRequested(KongaTreeNode var1) {
      if (this.renameStrategy != null && this.renameStrategy.isRenamable(var1)) {
         this.initializeRenamer(var1);
      }
   }

   private void initializeRenamer(KongaTreeNode var1) {
      this.node = var1;
      this.nodeRenamer = new ItemRenamer(var1, this);
      int var2 = this.tree.getRowForPath(this.tree.getSelectionPath());
      Rectangle var3 = this.tree.getRowBounds(var2);
      if (var3 != null) {
         int var4 = var3.x;
         Icon var5 = var1.getNodeIcon(this.tree.isNodeExpanded(var1) ? NodeStatus.OPENED : NodeStatus.CLOSED);
         if (var5 != null) {
            var4 += var5.getIconWidth();
         }

         int var6 = var3.y + (var3.height - this.nodeRenamer.getHeight()) / 2;
         this.nodeRenamer.setLocation(var4, var6);
         this.tree.add(this.nodeRenamer);
         this.nodeRenamer.requestFocus();
      }
   }

   public void setRenameNodeStrategy(RenameNodeStrategy var1) {
      this.renameStrategy = var1;
   }

   @Override
   public String getItemName(Object var1) {
      if (var1 instanceof KongaTreeNode) {
         return ((KongaTreeNode)var1).getDisplayName();
      } else {
         return var1 != null ? var1.toString() : "";
      }
   }

   @Override
   public void itemRenameCancelled() {
      this.removeNodeRenamer();
   }

   @Override
   public void itemRenamed(ItemRenamedEvent var1) {
      try {
         this.itemRenamedImpl(var1);
      } finally {
         this.removeNodeRenamer();
      }
   }

   private void itemRenamedImpl(ItemRenamedEvent var1) {
      if (this.renameStrategy != null) {
         Object var2 = var1.getItem();
         if (var2 instanceof KongaTreeNode) {
            KongaTreeNode var3 = (KongaTreeNode)var2;
            if (this.renameStrategy.rename(this.tree, var3, var1.getNewName())) {
               this.tree.fireNodePropertyChanged(var3);
            }
         }
      }
   }

   private void removeNodeRenamer() {
      if (this.nodeRenamer != null) {
         this.tree.remove(this.nodeRenamer);
         this.nodeRenamer.setVisible(false);
         this.nodeRenamer = null;
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (RenameNodeHandler.this.tree.getSelectionCount() == 0) {
                  RenameNodeHandler.this.tree.makeNodeSelected(RenameNodeHandler.this.node, false);
               }

               RenameNodeHandler.this.tree.requestFocusInWindow();
               RenameNodeHandler.this.tree.repaint();
            }
         });
      }
   }
}
