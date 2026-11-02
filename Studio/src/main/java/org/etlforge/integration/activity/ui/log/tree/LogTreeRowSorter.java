package org.etlforge.integration.activity.ui.log.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.log.SortStatus;
import org.etlforge.integration.activity.ui.log.SortStatusTranslator;
import org.etlforge.lang.Objects;

public final class LogTreeRowSorter extends RowSorter<TableModel> {
   private final OperationLogTree tree;
   private final OperationLogTreeModel treeModel;
   private final TableModel tableModel;
   private List<SortKey> sortKeys = Collections.emptyList();

   public LogTreeRowSorter(OperationLogTree var1) {
      this.tree = var1;
      this.treeModel = (OperationLogTreeModel)var1.getTreeTableModel();
      this.tableModel = var1.getModel();
   }

   public TableModel getModel() {
      return this.tableModel;
   }

   @Override
   public void toggleSortOrder(int var1) {
      boolean var2 = true;
      if (this.sortKeys.size() == 1) {
         SortKey var3 = this.sortKeys.get(0);
         if (var3.getColumn() == var1) {
            this.sortKeys = Collections.singletonList(toggle(var3));
            var2 = false;
         }
      }

      if (var2) {
         this.sortKeys = Arrays.asList(new SortKey(var1, SortOrder.ASCENDING));
      }

      this.doSort();
      this.fireSortOrderChanged();
   }

   private static SortKey toggle(SortKey var0) {
      return var0.getSortOrder() == SortOrder.ASCENDING
         ? new SortKey(var0.getColumn(), SortOrder.DESCENDING)
         : new SortKey(var0.getColumn(), SortOrder.ASCENDING);
   }

   @Override
   public int convertRowIndexToModel(int var1) {
      return var1;
   }

   @Override
   public int convertRowIndexToView(int var1) {
      return var1;
   }

   public void setCurrentSortStatus(SortStatus var1) {
      if (var1 != null) {
         int var2 = this.treeModel.getIndexOfColumn(var1.column);
         if (var2 != -1) {
            this.sortKeys = Arrays.asList(new SortKey(var2, var1.sortOrder));
            return;
         }
      }

      this.sortKeys = Collections.emptyList();
   }

   public SortStatus getSortStatus() {
      if (this.sortKeys.isEmpty()) {
         return null;
      }

      SortKey var1 = this.sortKeys.get(0);
      OperationLogTableColumn var2 = this.treeModel.getColumnDescriptor(var1.getColumn());
      return new SortStatus(var2, var1.getSortOrder());
   }

   @Override
   public void setSortKeys(List<? extends SortKey> var1) {
      if (!Objects.equal(var1, this.sortKeys)) {
         if (var1 == null) {
            this.sortKeys = Collections.emptyList();
         } else {
            Preconditions.checkArgument(var1.size() < 2, "At most one SortKey is supported");
            this.sortKeys = Lists.newArrayList(var1);
         }

         if (!this.sortKeys.isEmpty()) {
            this.doSort();
         }

         this.fireSortOrderChanged();
      }
   }

   private void doSort() {
      Comparator var1 = this.createComparator();
      if (var1 != null) {
         ExpansionMemento var2 = new ExpansionMemento(this.tree);
         this.treeModel.sortTopLevelNodes(var1);
         var2.restore(this.tree);
      }
   }

   private Comparator<OperationLogEntry> createComparator() {
      SortKey var1 = this.sortKeys.get(0);
      int var2 = var1.getColumn();
      OperationLogTableColumn var3 = this.treeModel.getColumnDescriptor(var2);
      SortStatus var4 = new SortStatus(var3, var1.getSortOrder());
      return SortStatusTranslator.toComparator(var4);
   }

   @Override
   public List<? extends SortKey> getSortKeys() {
      return Lists.newArrayList(this.sortKeys);
   }

   @Override
   public int getViewRowCount() {
      return this.tableModel.getRowCount();
   }

   @Override
   public int getModelRowCount() {
      return this.tableModel.getRowCount();
   }

   @Override
   public void modelStructureChanged() {
   }

   @Override
   public void allRowsChanged() {
   }

   @Override
   public void rowsInserted(int var1, int var2) {
   }

   @Override
   public void rowsDeleted(int var1, int var2) {
   }

   @Override
   public void rowsUpdated(int var1, int var2) {
   }

   @Override
   public void rowsUpdated(int var1, int var2, int var3) {
   }
}
