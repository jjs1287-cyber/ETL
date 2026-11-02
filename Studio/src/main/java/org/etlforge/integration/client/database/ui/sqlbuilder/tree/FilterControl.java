package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.tree.TreePath;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsViewFilter;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsViewStringFilter;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class FilterControl implements UiProvider {
   private final JXTreeTable treeTable;
   private final KongaTextField filterField = new KongaTextField(20);
   private DbObjectsViewFilter filter;

   public FilterControl(JXTreeTable var1) {
      this.treeTable = var1;
      this.listenToTreeTable(var1);
      this.installAutoUpdate();
      ActionKeyBinding.install(this.filterField, KeyUtils.DOWN, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            FilterControl.this.focusTree();
         }
      });
   }

   private void listenToTreeTable(JXTreeTable var1) {
      var1.addPropertyChangeListener("treeTableModel", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            FilterControl.this.applyFilter();
         }
      });
   }

   private void installAutoUpdate() {
      DelayedDocumentContentChangeReaction var1 = new DelayedDocumentContentChangeReaction() {
         @Override
         public void run() {
            FilterControl.this.applyFilter();
         }
      };
      var1.install(this.filterField);
   }

   private void applyFilter() {
      this.filter = this.createFilter();
      DbObjectsTreeModel var1 = this.getModel();
      if (var1 != null) {
         var1.setFilter(this.filter);
         this.checkExpansionState(var1);
      }
   }

   private DbObjectsViewFilter createFilter() {
      String var1 = this.filterField.getText();
      return StringUtils.isBlank(var1) ? null : new DbObjectsViewStringFilter(var1);
   }

   private DbObjectsTreeModel getModel() {
      TreeTableModel var1 = this.treeTable.getTreeTableModel();
      return var1 instanceof DbObjectsTreeModel ? (DbObjectsTreeModel)var1 : null;
   }

   private void checkExpansionState(DbObjectsTreeModel var1) {
      int var2 = var1.getChildCount(var1.getRoot());
      if (var2 == 1) {
         TreeTableNode var3 = (TreeTableNode)var1.getChild(var1.getRoot(), 0);
         if (var3 instanceof DatabaseTableNode) {
            this.treeTable.expandPath(new TreePath(new Object[]{var1.getRoot(), var3}));
         }
      }
   }

   private void focusTree() {
      this.treeTable.requestFocusInWindow();
      if (this.treeTable.getSelectedRowCount() == 0) {
         this.treeTable.getSelectionModel().setSelectionInterval(0, 0);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("&Filter: ");
      var2.setLabelFor(this.filterField);
      Horizontal.box().nonOpaque().of(var2, this.filterField).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.filterField.requestFocusInWindow();
   }

   public TableSearchFilter getInfoProviderFilter() {
      return this.filter == null ? TableSearchFilter.TABLE_AND_VIEWS : this.filter.asInfoProviderFilter();
   }

   public void installDefaultAction(Action var1) {
      this.filterField.addActionListener(var1);
   }
}
