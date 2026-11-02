package org.etlforge.integration.activity.ui.history;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.ui.DeleteSelectionListener;
import org.etlforge.integration.activity.ui.util.DeletableTableModel;
import org.etlforge.ui.widget.table.AbstractKongaTableModel;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class SourceHistoryTableModel extends AbstractKongaTableModel implements DeletableTableModel {
   public static final TableColumnMetaData CHECK_SELECTED = new TableColumnMetaData(" ", Boolean.class, true, 40, false);
   public static final TableColumnMetaData OPERATION = new TableColumnMetaData(PackageResources.SourceTable.Columns.OPERATION, String.class, false, 250);
   public static final TableColumnMetaData SOURCE_FILES = new TableColumnMetaData(PackageResources.SourceTable.Columns.SOURCE_FILES, String.class, false, 300);
   public static final TableColumnMetaData FILE_SIZE = new TableColumnMetaData(PackageResources.SourceTable.Columns.FILE_SIZE, Integer.class, false, 60);
   public static final TableColumnMetaData TIMESTAMP = new TableColumnMetaData(PackageResources.SourceTable.Columns.TIME_STAMP, Date.class, false, 90);
   private final List<DeleteSelectionListener> deleteSelectionListeners;
   private final boolean deleteSupported;

   private SourceHistoryTableModel(boolean var1) {
      super(columnsToInclude(var1));
      this.deleteSupported = var1;
      this.deleteSelectionListeners = new CopyOnWriteArrayList<>();
   }

   private static TableColumnMetaData[] columnsToInclude(boolean var0) {
      ArrayList var1 = Lists.newArrayList();
      if (var0) {
         var1.add(CHECK_SELECTED);
      }

      KongaListUtils.add(var1, OPERATION, SOURCE_FILES, FILE_SIZE, TIMESTAMP);
      return (TableColumnMetaData[]) var1.toArray(new TableColumnMetaData[var1.size()]);
   }

   public static SourceHistoryTableModel emptyModel(boolean var0) {
      return new SourceHistoryTableModel(var0);
   }

   public void addEntries(List<SourceHistoryEntry> var1) {
      for (SourceHistoryEntry var3 : var1) {
         this.addRow(var3);
      }
   }

   @Override
   protected Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof SourceHistoryEntry) {
         SourceHistoryEntry var2 = (SourceHistoryEntry)var1;
         ArrayList var3 = Lists.newArrayList();
         if (this.deleteSupported) {
            var3.add(Boolean.FALSE);
         }

         KongaListUtils.add(var3, var2.getOperationName(), var2.getSourceFiles(), var2.getFileSizeInBytes(), var2.getFileTimestamp());
         return var3.toArray();
      } else {
         this.throwIllegalRowObject(var1, SourceHistoryEntry.class);
         return null;
      }
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      return this.deleteSupported && var2 == this.indexOfColumn(CHECK_SELECTED) && this.getHistoryEntry(var1) == null
         ? false
         : super.isCellEditable(var1, var2);
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
      SourceHistoryEntry var3 = this.getHistoryEntry(var1);
      if (var3 != null) {
         for (DeleteSelectionListener var5 : this.deleteSelectionListeners) {
            var5.selectionChanged(var3, var2);
         }
      }
   }

   private void onRowUnselected(int var1) {
      this.notifyDeleteSelectionListeners(var1, false);
   }

   public SourceHistoryEntry getHistoryEntry(int var1) {
      return (SourceHistoryEntry)this.getRowObjectAt(var1);
   }

   @Override
   public boolean hasDeletableEntries() {
      int var1 = 0;

      for (int var2 = this.getRowCount(); var1 < var2; var1++) {
         if (this.getHistoryEntry(var1) != null) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void selectAll() {
      int var1 = this.indexOfColumn(CHECK_SELECTED);
      int var2 = 0;

      for (int var3 = this.getRowCount(); var2 < var3; var2++) {
         this.setValueAt(Boolean.TRUE, var2, var1);
      }
   }

   @Override
   public void unselectAll() {
      int var1 = this.indexOfColumn(CHECK_SELECTED);
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
}
