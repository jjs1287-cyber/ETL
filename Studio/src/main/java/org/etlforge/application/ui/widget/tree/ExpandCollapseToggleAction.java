package org.etlforge.application.ui.widget.tree;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class ExpandCollapseToggleAction extends AbstractAction implements Disposable {
   private final KongaTree tree;
   private ExpandCollapseToggleAction.Mode mode;
   private final ExpandCollapseToggleAction.Listener treeListener = new ExpandCollapseToggleAction.Listener();
   private boolean useRootChildren;

   public ExpandCollapseToggleAction(KongaTree var1) {
      this.tree = var1;
      this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(111));
      this.listenToTree(var1);
      this.updateMode();
   }

   private void listenToTree(KongaTree var1) {
      var1.addTreeExpansionListener(this.treeListener);
      var1.addPropertyChangeListener("model", this.treeListener);
   }

   private void updateMode() {
      if (this.isSomethingExpanded()) {
         this.mode = ExpandCollapseToggleAction.Mode.COLLAPSE;
      } else {
         this.mode = ExpandCollapseToggleAction.Mode.EXPAND;
      }

      this.putValue("SmallIcon", this.mode.getIcon());
   }

   private boolean isSomethingExpanded() {
      KongaTreeModel var1 = this.tree.getModel();
      KongaTreeNode var2 = var1.getRoot();
      if (this.useRootChildren) {
         int var3 = var1.getChildCount(var2);

         for (int var4 = 0; var4 < var3; var4++) {
            Object var5 = var1.getChild(var2, var4);
            if (this.isSomethingExpanded(var1, var5)) {
               return true;
            }
         }

         return false;
      } else {
         return this.isSomethingExpanded(var1, var2);
      }
   }

   private boolean isSomethingExpanded(KongaTreeModel var1, Object var2) {
      int var3 = var1.getChildCount(var2);

      for (int var4 = 0; var4 < var3; var4++) {
         Object var5 = var1.getChild(var2, var4);
         TreePath var6 = this.tree.getPath(var5);
         if (this.tree.isExpanded(var6)) {
            return true;
         }
      }

      return false;
   }

   public void setUseRootChildren(boolean var1) {
      if (var1 != this.useRootChildren) {
         this.useRootChildren = var1;
         this.updateMode();
      }
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.tree.removeTreeExpansionListener(this.treeListener);
      this.mode.apply(this.tree, this.useRootChildren);
      this.tree.addTreeExpansionListener(this.treeListener);
      this.updateMode();
      this.tree.requestFocusInWindow();
   }

   @Override
   public void dispose() {
      this.tree.removeTreeExpansionListener(this.treeListener);
      this.tree.removePropertyChangeListener("model", this.treeListener);
   }

   private class Listener implements PropertyChangeListener, TreeExpansionListener {
      private Listener() {
      }

      @Override
      public void treeExpanded(TreeExpansionEvent var1) {
         ExpandCollapseToggleAction.this.updateMode();
      }

      @Override
      public void treeCollapsed(TreeExpansionEvent var1) {
         ExpandCollapseToggleAction.this.updateMode();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ExpandCollapseToggleAction.this.updateMode();
      }
   }

   private enum Mode {
      EXPAND,
      COLLAPSE;

      public Icon getIcon() {
         switch (this) {
            case EXPAND:
               return ApplicationResources.ICONS.getIcon("ExpandTree.16");
            case COLLAPSE:
               return ApplicationResources.ICONS.getIcon("CollapseTree.16");
            default:
               throw new RuntimeException("Unexpected mode: " + this);
         }
      }

      public void apply(KongaTree var1, boolean var2) {
         switch (this) {
            case EXPAND:
               var1.expandAll();
               break;
            case COLLAPSE:
               var1.collapseAll();
               if (var2) {
                  var1.expandRow(0);
               }

               var1.selectFirstRow();
               var1.scrollToFirstRow();
         }
      }
   }
}
