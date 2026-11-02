package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;

public final class TreeSelectionSource implements MutableSelectionSource, SelectionSourceWithDefaultAction {
   private final JTree tree;
   private final SelectionSourceWidgetSupport support;
   private OrderedSelection currentSelection;
   private NodeObjectConverter nodeObjectConverter;

   public TreeSelectionSource(JTree var1) {
      this.tree = var1;
      this.nodeObjectConverter = NodeObjectConverter.TRIVIAL;
      this.support = new SelectionSourceWidgetSupport(this, var1);
      this.currentSelection = this.getSelectionFromTree();
      var1.addTreeSelectionListener(new TreeSelectionSource.TreeListener());
   }

   public JTree getTree() {
      return this.tree;
   }

   public void setNodeObjectConverter(NodeObjectConverter var1) {
      Preconditions.checkNotNull(var1, "converter");
      this.nodeObjectConverter = var1;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.support.setDefaultAction(var1);
   }

   public OrderedSelection getSelection() {
      return this.currentSelection;
   }

   @Override
   public void setSelection(Selection var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.setSelectionImpl(var1);
      } else {
         this.currentSelection = OrderedSelection.EMPTY;
         this.tree.clearSelection();
      }
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.support.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.support.removeListener(var1);
   }

   private void setSelectionImpl(Selection var1) {
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.items()) {
         TreeNode var5 = this.nodeObjectConverter.toNode(var4);
         if (var5 != null) {
            TreePath var6 = TreeUtils.getPathForNode(this.tree, var5);
            if (var6 != null) {
               var2.add(var6);
            }
         }
      }

      this.tree.setSelectionPaths((TreePath[]) var2.toArray(new TreePath[var2.size()]));
      ArrayList var7 = Lists.newArrayList(var1.items());
      this.currentSelection = SelectionFactory.newOrderedSelection(var7);
   }

   private OrderedSelection getSelectionFromTree() {
      return this.createSelection(this.tree.getSelectionPaths());
   }

   private OrderedSelection createSelection(TreePath[] var1) {
      if (var1 == null) {
         return OrderedSelection.EMPTY;
      }

      ArrayList var2 = new ArrayList();

      for (TreePath var6 : var1) {
         Object var7 = var6.getLastPathComponent();
         if (var7 instanceof TreeNode) {
            Object var8 = this.nodeObjectConverter.toObject((TreeNode)var7);
            if (var8 != null) {
               var2.add(var8);
            }
         }
      }

      return SelectionFactory.newOrderedSelection(var2);
   }

   private final class TreeListener implements TreeSelectionListener {
      private TreeListener() {
      }

      @Override
      public void valueChanged(TreeSelectionEvent var1) {
         TreeSelectionSource.this.currentSelection = TreeSelectionSource.this.createSelection(TreeSelectionSource.this.tree.getSelectionPaths());
         TreeSelectionSource.this.support.fireSelectionChange(TreeSelectionSource.this.currentSelection);
      }
   }
}
