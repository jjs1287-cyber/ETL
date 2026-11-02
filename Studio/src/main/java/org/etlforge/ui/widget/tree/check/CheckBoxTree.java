package org.etlforge.ui.widget.tree.check;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.BreadthFirstTraverser;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.PreOrderTraverser;

public final class CheckBoxTree implements UiProvider {
   private final KongaTree tree;
   private final CheckBoxTreeListeners listeners;
   private Disposable popupMenuHandler;

   public CheckBoxTree(KongaTree var1) {
      this.tree = var1;
      this.listeners = new CheckBoxTreeListeners();
      this.popupMenuHandler = PopupMenuRequestHandlers.install(var1, new CheckBoxTree.DefaultPopupMenuHandler());
      var1.addMouseListener(new CheckBoxTree.MouseClickHandler());
      this.installKeyBindings(var1);
      this.checkRenderer(var1);
   }

   public KongaTree getAdaptee() {
      return this.tree;
   }

   public void setModel(KongaTreeModel var1) {
      this.tree.setModel(var1);
      this.fireOverallCheckStateChanged();
   }

   public KongaTreeModel getModel() {
      return this.tree.getModel();
   }

   public KongaTreeNode[] getCheckedNodes() {
      return getCheckedNodes(this.tree.getModel());
   }

   public static KongaTreeNode[] getCheckedNodes(KongaTreeModel var0) {
      final ArrayList var1 = new ArrayList();
      visitAllCheckBoxNodesNoFilter(var0, new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1x) {
            if (var1x.isSelected()) {
               var1.add(var1x);
            }
         }
      });
      return (KongaTreeNode[]) var1.toArray(new KongaTreeNode[var1.size()]);
   }

   public boolean hasCheckedNodes() {
      CheckBoxTree.CheckedNodeDetector var1 = new CheckBoxTree.CheckedNodeDetector();
      this.visitAllCheckBoxNodes(var1);
      return var1.isAtLeastOneNodeChecked();
   }

   public void setCheckedNodes(KongaTreeNode[] var1) {
      setCheckedNodesOnModel(this.tree.getModel(), var1);
      this.tree.repaint();
      this.fireOverallCheckStateChanged();
   }

   public static void setCheckedNodesOnModel(KongaTreeModel var0, KongaTreeNode[] var1) {
      final HashSet var2 = Sets.newHashSet(var1);
      visitAllCheckBoxNodes(var0, new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1) {
            boolean var2x = var2.contains(var1);
            var1.setSelected(var2x);
         }
      });
   }

   public void setNodeChecked(CheckBoxNode var1, boolean var2) {
      if (var1.isSelected() != var2) {
         this.toggleNode(var1);
      }
   }

   public void addCheckedNodes(KongaTreeNode[] var1) {
      final HashSet var2 = Sets.newHashSet(var1);
      this.visitAllCheckBoxNodes(new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1) {
            if (var2.contains(var1)) {
               var1.setSelected(true);
            }
         }
      });
      this.tree.repaint();
      this.fireOverallCheckStateChanged();
   }

   public void checkAll() {
      this.visitAllCheckBoxNodes(new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1) {
            var1.setSelected(true);
         }
      });
      this.tree.repaint();
      this.fireOverallCheckStateChanged();
   }

   public void uncheckAll() {
      this.visitAllCheckBoxNodes(new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1) {
            var1.setSelected(false);
         }
      });
      this.tree.repaint();
      this.fireOverallCheckStateChanged();
   }

   public void visitAllCheckBoxNodes(CheckBoxNodeVisitor var1) {
      visitAllCheckBoxNodes(this.tree.getModel(), var1);
   }

   public static void visitAllCheckBoxNodes(KongaTreeModel var0, CheckBoxNodeVisitor var1) {
      if (!var1.isDone()) {
         for (Object var3 : new BreadthFirstTraverser(var0)) {
            if (var3 instanceof CheckBoxNode) {
               var1.visit((CheckBoxNode)var3);
               if (var1.isDone()) {
                  return;
               }
            }
         }
      }
   }

   public static void visitAllCheckBoxNodesNoFilter(KongaTreeModel var0, CheckBoxNodeVisitor var1) {
      KongaTreeNode var2 = var0.getRoot();
      if (var2 != null) {
         visitAllCheckBoxNodesNoFilter(var2, var1);
      }
   }

   public static void visitAllCheckBoxNodesNoFilter(KongaTreeNode var0, CheckBoxNodeVisitor var1) {
      if (var0 instanceof CheckBoxNode) {
         var1.visit((CheckBoxNode)var0);
      }

      if (var0.getAllowsChildren()) {
         for (KongaTreeNode var3 : var0.children()) {
            visitAllCheckBoxNodesNoFilter(var3, var1);
         }
      }
   }

   public void setPopupMenuHandler(PopupMenuRequestHandler var1) {
      if (this.popupMenuHandler != null) {
         this.popupMenuHandler.dispose();
      }

      this.popupMenuHandler = var1 == null ? null : PopupMenuRequestHandlers.install(this.tree, var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public void setRenderer(TreeCellRenderer var1) {
      Preconditions.checkNotNull(var1, "renderer");
      if (var1 instanceof CheckBoxTreeRenderer) {
         this.tree.setCellRenderer(var1);
      } else {
         this.tree.setCellRenderer(new CheckBoxTreeRenderer(var1));
      }
   }

   public Action getCheckAllAction(KongaTreeNode var1) {
      return new CheckBoxTree.ChangeCheckedStateForDescendantsAction("&Select All", var1, true);
   }

   public Action getUncheckAllAction(KongaTreeNode var1) {
      return new CheckBoxTree.ChangeCheckedStateForDescendantsAction("&Unselect All", var1, false);
   }

   public void addListener(CheckBoxTreeListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(CheckBoxTreeListener var1) {
      this.listeners.remove(var1);
   }

   private void checkRenderer(KongaTree var1) {
      TreeCellRenderer var2 = var1.getCellRenderer();
      if (!(var2 instanceof CheckBoxTreeRenderer)) {
         CheckBoxTreeRenderer var3 = var2 != null ? new CheckBoxTreeRenderer(var2) : new CheckBoxTreeRenderer();
         var1.setCellRenderer(var3);
      }
   }

   private void installKeyBindings(KongaTree var1) {
      InputMap var2 = var1.getInputMap(0);
      ActionMap var3 = var1.getActionMap();
      var2.put(KeyUtils.SPACE, "space");
      var3.put("space", new CheckBoxTree.KeyBoardSelectionAction());
   }

   private void fireOverallCheckStateChanged() {
      if (!this.listeners.isEmpty()) {
         this.listeners.call().overallCheckedStateChanged(new CheckBoxTreeEvent(this, null));
      }
   }

   private void fireNodeToggled(CheckBoxNode var1) {
      if (!this.listeners.isEmpty()) {
         this.listeners.call().nodeToggled(new CheckBoxTreeEvent(this, var1));
      }
   }

   public void toggleNode(CheckBoxNode var1) {
      var1.toggle();
      this.fireNodeToggled(var1);
      this.tree.repaint();
   }

   private KongaTreeNode getNodeForEvent(int var1, int var2) {
      TreePath var3 = this.tree.getPathForLocation(var1, var2);
      if (var3 == null) {
         return null;
      }

      Object var4 = var3.getLastPathComponent();
      return var4 instanceof KongaTreeNode ? (KongaTreeNode)var4 : null;
   }

   private class ChangeCheckedStateForDescendantsAction extends AbstractAction {
      private final KongaTreeNode root;
      private final boolean state;

      public ChangeCheckedStateForDescendantsAction(String var2, KongaTreeNode var3, boolean var4) {
         super(var2);
         this.root = var3;
         this.state = var4;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         KongaTreeNode var2 = this.root != null ? this.root : CheckBoxTree.this.tree.getModel().getRoot();
         this.changeCheckedState(CheckBoxTree.this.tree.getModel(), var2);
         CheckBoxTree.this.tree.repaint();
         CheckBoxTree.this.fireOverallCheckStateChanged();
      }

      private void changeCheckedState(KongaTreeModel var1, Object var2) {
         int var3 = 0;

         for (int var4 = var1.getChildCount(var2); var3 < var4; var3++) {
            Object var5 = var1.getChild(var2, var3);
            if (var5 instanceof CheckBoxNode) {
               ((CheckBoxNode)var5).setSelected(this.state);
            }

            this.changeCheckedState(var1, var5);
         }
      }
   }

   private static class CheckedNodeDetector implements CheckBoxNodeVisitor {
      private boolean atLeastOneNodeIsChecked;

      private CheckedNodeDetector() {
      }

      @Override
      public boolean isDone() {
         return this.atLeastOneNodeIsChecked;
      }

      @Override
      public void visit(CheckBoxNode var1) {
         if (var1.isSelected()) {
            this.atLeastOneNodeIsChecked = true;
         }
      }

      public boolean isAtLeastOneNodeChecked() {
         return this.atLeastOneNodeIsChecked;
      }
   }

   private class DefaultPopupMenuHandler implements PopupMenuRequestHandler {
      private DefaultPopupMenuHandler() {
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         KongaTreeNode var3 = CheckBoxTree.this.getNodeForEvent(var2.getX(), var2.getY());
         if (var3 == null) {
            return false;
         }

         if (this.hasCheckBoxNodeDescendant(var3)) {
            JPopupMenu var4 = this.createPopupMenu(var3);
            CheckBoxTree.this.tree.showPopupMenu(var4);
         }

         return true;
      }

      private boolean hasCheckBoxNodeDescendant(KongaTreeNode var1) {
         for (Object var3 : new PreOrderTraverser(CheckBoxTree.this.tree.getModel())) {
            if (var3 != var1 && var3 instanceof CheckBoxNode) {
               return true;
            }
         }

         return false;
      }

      private JPopupMenu createPopupMenu(KongaTreeNode var1) {
         KongaPopupMenu var2 = new KongaPopupMenu();
         var2.add(CheckBoxTree.this.getCheckAllAction(var1));
         var2.add(CheckBoxTree.this.getUncheckAllAction(var1));
         return var2;
      }
   }

   private class KeyBoardSelectionAction extends AbstractAction {
      private KeyBoardSelectionAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TreePath[] var2 = CheckBoxTree.this.tree.getSelectionPaths();

         for (TreePath var6 : var2) {
            Object var7 = var6.getLastPathComponent();
            if (var7 instanceof CheckBoxNode) {
               CheckBoxTree.this.toggleNode((CheckBoxNode)var7);
            }
         }
      }
   }

   private class MouseClickHandler extends MouseAdapter {
      private MouseClickHandler() {
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         KongaTreeNode var2 = CheckBoxTree.this.getNodeForEvent(var1.getX(), var1.getY());
         if (var2 instanceof CheckBoxNode) {
            TreePath var3 = CheckBoxTree.this.tree.getPathForLocation(var1.getX(), var1.getY());
            Rectangle var4 = CheckBoxTree.this.tree.getPathBounds(var3);
            CheckBoxTreeRenderer var5 = (CheckBoxTreeRenderer)CheckBoxTree.this.tree.getCellRenderer();
            if (var5.isInCheckBox(var1.getX() - var4.x, var1.getY() - var4.y)) {
               CheckBoxTree.this.toggleNode((CheckBoxNode)var2);
               var1.consume();
            }
         }
      }
   }
}
