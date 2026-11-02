package org.etlforge.integration.activity.ui.log;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.integration.activity.ui.util.DeletableTableModel;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class OperationLogTableModel extends TypedAbstractKongaTableModel<OperationLogEntry> implements DeletableTableModel {
   private final OperationLogTableColumn[] columns;
   private final List<DeleteSelectionListener> deleteSelectionListeners;

   public OperationLogTableModel() {
      this(OperationLogTableColumn.allColumns());
   }

   public OperationLogTableModel(OperationLogTableColumn... var1) {
      super(OperationLogEntry.class, var1);
      this.columns = (OperationLogTableColumn[])var1.clone();
      this.deleteSelectionListeners = new CopyOnWriteArrayList<>();
   }

   public OperationLogTableModel(Iterable<OperationLogEntry> var1, OperationLogTableColumn... var2) {
      this(var2);

      for (OperationLogEntry var4 : var1) {
         this.addRow(var4);
      }
   }

   protected Object[] getColumnDataForTypedObject(OperationLogEntry var1) {
      ArrayList var2 = Lists.newArrayList();

      for (OperationLogTableColumn var6 : this.columns) {
         var2.add(var6.createColumnValue(var1));
      }

      return var2.toArray();
   }

   @Override
   public boolean hasDeletableEntries() {
      return this.getRowCount() > 0;
   }

   @Override
   public void selectAll() {
      int var1 = this.indexOfColumn(OperationLogTableColumn.SELECT_CHECK);
      int var2 = 0;

      for (int var3 = this.getRowCount(); var2 < var3; var2++) {
         this.setValueAt(Boolean.TRUE, var2, var1);
      }
   }

   @Override
   public void unselectAll() {
      int var1 = this.indexOfColumn(OperationLogTableColumn.SELECT_CHECK);
      int var2 = 0;

      for (int var3 = this.getRowCount(); var2 < var3; var2++) {
         this.setValueAt(Boolean.FALSE, var2, var1);
      }
   }

   public void addDeleteSelectionListener(DeleteSelectionListener var1) {
      this.deleteSelectionListeners.add(var1);
   }

   public void removeDeleteSelectionListener(DeleteSelectionListener var1) {
      this.deleteSelectionListeners.remove(var1);
   }

   @Override
   protected boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
      if (var3 == 0 && var4 instanceof Boolean) {
         this.onSelectionChanged(var2, (Boolean)var4);
         return true;
      } else {
         return false;
      }
   }

   private void onSelectionChanged(int var1, boolean var2) {
      if (var2) {
         this.onRowSelected(var1);
      } else {
         this.onRowUnselected(var1);
      }
   }

   private void onRowSelected(int var1) {
      this.notifyDeleteSelectionListeners(var1, true);
   }

   private void notifyDeleteSelectionListeners(int var1, boolean var2) {
      OperationLogEntry var3 = this.getRowObjectAt(var1);

      for (DeleteSelectionListener var5 : this.deleteSelectionListeners) {
         var5.selectionChanged(var3, var2);
      }
   }

   private void onRowUnselected(int var1) {
      this.notifyDeleteSelectionListeners(var1, false);
   }
}
