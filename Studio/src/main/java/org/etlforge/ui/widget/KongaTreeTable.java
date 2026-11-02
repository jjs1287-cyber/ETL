package org.etlforge.ui.widget;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.ui.selection.ListSelectionDispatcher;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.AbstractTableStyle;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public class KongaTreeTable extends JXTreeTable implements SelectionSource {
   private TableRendererPreparer rendererPreparer;
   private final KongaTreeTable.SelectionDispatcher selectionDispatcher = new KongaTreeTable.SelectionDispatcher(this);

   public KongaTreeTable() {
      this.customizeTableHeader();
   }

   public KongaTreeTable(TreeTableModel var1) {
      super(var1);
      this.customizeTableHeader();
      this.installExpansionKeys();
   }

   private void customizeTableHeader() {
      JTableHeader var1 = this.getTableHeader();
      var1.setReorderingAllowed(false);
      this.installExpansionKeys();
   }

   private void installExpansionKeys() {
      KongaTreeTable.ArrowLeftAction var1 = new KongaTreeTable.ArrowLeftAction(this);
      var1.install(KeyUtils.LEFT);
      KongaTreeTable.ArrowRightAction var2 = new KongaTreeTable.ArrowRightAction(this);
      var2.install(KeyUtils.RIGHT);
   }

   public final void setRendererPreparer(TableRendererPreparer var1) {
      this.rendererPreparer = var1;
   }

   public final void setStyle(TableStyle var1) {
      if (var1 instanceof AbstractTableStyle) {
         this.rendererPreparer = ((AbstractTableStyle)var1).getRendererPreparer();
      }

      var1.makeOver(this);
   }

   @Override
   public final Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
      Component var4 = super.prepareRenderer(var1, var2, var3);
      if (this.rendererPreparer != null) {
         this.rendererPreparer.prepare(this, var1, var4, var2, var3);
      }

      return var4;
   }

   public final void updateColumnWidths(TableColumnMetaData[] var1) {
      TableColumnModel var2 = this.getColumnModel();

      for (int var3 = 0; var3 < var1.length; var3++) {
         TableColumn var4 = var2.getColumn(var3);
         TableColumnMetaData var5 = var1[var3];
         var4.setResizable(var5.isResizable());
         var4.setPreferredWidth(var5.getWidth());
         if (!var5.isResizable()) {
            var4.setMinWidth(var5.getWidth());
            var4.setMaxWidth(var5.getWidth());
         }
      }
   }

   public final void setFixedColumnWidth(int var1, int var2) {
      TableColumn var3 = this.getColumnModel().getColumn(var1);
      var3.setWidth(var2);
      var3.setPreferredWidth(var2);
      var3.setMinWidth(var2);
      var3.setMaxWidth(var2);
   }

   public final Set<Object> getExpandedNodes() {
      HashSet var1 = Sets.newHashSet();
      TreePath var2 = new TreePath(this.getTreeTableModel().getRoot());
      Enumeration var3 = this.getExpandedDescendants(var2);
      if (var3 != null) {
         for (Object var5 : IterableEnumeration.of(var3)) {
            TreePath var6 = (TreePath)var5;
            var1.add(var6.getLastPathComponent());
         }
      }

      return var1;
   }

   public final OrderedSelection getSelection() {
      return this.selectionDispatcher.getSelection();
   }

   @Override
   public final void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public final void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   public final void setNodeToObjectConversion(Function<Object, ?> var1) {
      this.selectionDispatcher.nodeFunction = var1;
   }

   private static class ArrowLeftAction extends KongaTreeTable.ExpansionAction {
      public ArrowLeftAction(KongaTreeTable var1) {
         super(var1);
      }

      @Override
      protected boolean isApplicable(KongaTreeTable var1, int var2) {
         return var1.isExpanded(var2);
      }

      @Override
      protected void execute(KongaTreeTable var1, int var2) {
         var1.collapseRow(var2);
      }
   }

   private static class ArrowRightAction extends KongaTreeTable.ExpansionAction {
      public ArrowRightAction(KongaTreeTable var1) {
         super(var1);
      }

      @Override
      protected boolean isApplicable(KongaTreeTable var1, int var2) {
         return !var1.isExpanded(var2);
      }

      @Override
      protected void execute(KongaTreeTable var1, int var2) {
         var1.expandRow(var2);
      }
   }

   private abstract static class ExpansionAction extends AbstractAction {
      private final KongaTreeTable tree;
      private Optional<Action> defaultBehavior = Optional.absent();

      public ExpansionAction(KongaTreeTable var1) {
         this.tree = var1;
      }

      public final void install(KeyStroke var1) {
         InputMap var2 = this.tree.getInputMap(1);
         ActionMap var3 = this.tree.getActionMap();
         Object var4 = var2.get(var1);
         if (var4 == null) {
            var4 = var1.toString();
         } else {
            this.defaultBehavior = Optional.fromNullable(var3.get(var4));
         }

         var3.put(var4, this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int[] var2 = this.tree.getSelectedRows();
         if (var2.length == 1) {
            int[] var3 = this.tree.getSelectedColumns();
            if (var3.length == 1) {
               int var4 = var3[0];
               if (var4 == this.tree.getHierarchicalColumn()) {
                  int var5 = var2[0];
                  if (this.isApplicable(this.tree, var5)) {
                     this.execute(this.tree, var5);
                     return;
                  }

                  return;
               }
            }
         }

         if (this.defaultBehavior.isPresent()) {
            this.defaultBehavior.get().actionPerformed(var1);
         }
      }

      protected abstract boolean isApplicable(KongaTreeTable var1, int var2);

      protected abstract void execute(KongaTreeTable var1, int var2);
   }

   private static class SelectionDispatcher extends ListSelectionDispatcher {
      private final KongaTreeTable tree;
      public Function<Object, ?> nodeFunction = Functions.identity();

      public SelectionDispatcher(KongaTreeTable var1) {
         this.tree = var1;
         var1.getSelectionModel().addListSelectionListener(this);
      }

      protected OrderedSelection getSelection(ListSelectionEvent var1) {
         return this.getSelection();
      }

      private OrderedSelection getSelection() {
         ArrayList var1 = Lists.newArrayList();

         for (int var5 : this.tree.getSelectedRows()) {
            TreePath var6 = this.tree.getPathForRow(var5);
            Object var7 = this.nodeFunction.apply(var6.getLastPathComponent());
            if (var7 != null) {
               var1.add(var7);
            }
         }

         return var1.isEmpty() ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
      }
   }
}
