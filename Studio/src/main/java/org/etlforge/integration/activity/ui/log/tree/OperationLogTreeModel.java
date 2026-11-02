package org.etlforge.integration.activity.ui.log.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.util.DeletableTableModel;

final class OperationLogTreeModel extends DefaultTreeTableModel implements DeletableTableModel {
   private final OperationLogTableColumn[] columnDescriptors;
   private final List<DeleteSelectionListener> deleteSelectionListeners;

   public static OperationLogTreeModel build(List<OperationLogEntry> var0, OperationLogTableColumn... var1) {
      LogTreeModelBuilder var2 = new LogTreeModelBuilder(var1);
      return var2.build(var0);
   }

   public OperationLogTreeModel(OperationLogTableColumn... var1) {
      this(new DefaultMutableTreeTableNode("Root"), var1);
   }

   public OperationLogTreeModel(TreeTableNode var1, OperationLogTableColumn... var2) {
      super(var1, getColumnNames(var2));
      this.columnDescriptors = (OperationLogTableColumn[])var2.clone();
      this.deleteSelectionListeners = new CopyOnWriteArrayList<>();
   }

   private static List<String> getColumnNames(OperationLogTableColumn... var0) {
      ArrayList var1 = Lists.newArrayList();

      for (OperationLogTableColumn var5 : var0) {
         var1.add(var5.getName());
      }

      return var1;
   }

   public final OperationLogTableColumn[] getColumnDescriptors() {
      return (OperationLogTableColumn[])this.columnDescriptors.clone();
   }

   public int[] getPreferredColumnWidths() {
      int[] var1 = new int[this.columnDescriptors.length];

      for (int var2 = 0; var2 < this.columnDescriptors.length; var2++) {
         var1[var2] = this.columnDescriptors[var2].getWidth();
      }

      return var1;
   }

   @Override
   public int getHierarchicalColumn() {
      for (int var1 = 0; var1 < this.columnDescriptors.length; var1++) {
         if (this.columnDescriptors[var1] == OperationLogTableColumn.OPERATION) {
            return var1;
         }
      }

      return -1;
   }

   @Override
   public boolean isCellEditable(Object var1, int var2) {
      return this.columnDescriptors[var2].isEditable();
   }

   @Override
   public String getColumnName(int var1) {
      return this.columnDescriptors[var1].getName();
   }

   @Override
   public Class<?> getColumnClass(int var1) {
      return this.columnDescriptors[var1].getDataClass();
   }

   @Override
   public void setValueAt(Object var1, Object var2, int var3) {
      super.setValueAt(var1, var2, var3);
      if (this.isCheckSelectMode() && var3 == 0) {
         OperationNode var4 = (OperationNode)var2;
         OperationLogEntry var5 = var4.getUserObject();
         boolean var6 = (Boolean)var1;

         for (DeleteSelectionListener var8 : this.deleteSelectionListeners) {
            var8.selectionChanged(var5, var6);
         }

         for (Object var10 : IterableEnumeration.of(var4.children())) {
            this.setValueAt(var1, var10, var3);
         }
      }
   }

   private boolean isCheckSelectMode() {
      return this.columnDescriptors[0] == OperationLogTableColumn.SELECT_CHECK;
   }

   @Override
   public boolean hasDeletableEntries() {
      return this.getRoot().getChildCount() > 0;
   }

   @Override
   public void selectAll() {
      this.ensureSelectable();
      this.setSelectedRecursively(Boolean.TRUE);
   }

   private void ensureSelectable() {
      Preconditions.checkState(this.isCheckSelectMode(), "The model is not prepared for check select");
   }

   private void setSelectedRecursively(Boolean var1) {
      for (Object var3 : IterableEnumeration.of(this.getRoot().children())) {
         this.setValueAt(var1, var3, 0);
      }
   }

   @Override
   public void unselectAll() {
      this.ensureSelectable();
      this.setSelectedRecursively(Boolean.FALSE);
   }

   public void addDeleteSelectionListener(DeleteSelectionListener var1) {
      this.deleteSelectionListeners.add(var1);
   }

   public void removeDeleteSelectionListener(DeleteSelectionListener var1) {
      this.deleteSelectionListeners.remove(var1);
   }

   public void sortTopLevelNodes(Comparator<OperationLogEntry> var1) {
      ((RootNode)this.getRoot()).sortChildren(var1);
      this.modelSupport.fireTreeStructureChanged(new TreePath(this.getPathToRoot(this.getRoot())));
   }

   public OperationLogTableColumn getColumnDescriptor(int var1) {
      return this.columnDescriptors[var1];
   }

   public int getIndexOfColumn(OperationLogTableColumn var1) {
      for (int var2 = 0; var2 < this.columnDescriptors.length; var2++) {
         if (this.columnDescriptors[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }
}
