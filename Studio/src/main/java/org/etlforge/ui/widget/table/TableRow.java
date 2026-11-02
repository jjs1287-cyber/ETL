package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;

class TableRow {
   private final BiMap<TableColumnMetaData, Integer> columnMetaData;
   private final TableRow.TableColumn[] columns;
   private final Object object;

   public TableRow(BiMap<TableColumnMetaData, Integer> var1, Object var2) {
      Preconditions.checkNotNull(var1, "columnMetaData");
      this.columnMetaData = var1;
      this.columns = new TableRow.TableColumn[var1.size()];
      this.object = var2;
   }

   public final Object getRowObject() {
      return this.object;
   }

   public void setColumnValue(int var1, Object var2) {
      this.checkIndex(var1);
      this.columns[var1] = new TableRow.TableColumn(this.columnMetaData.inverse().get(var1), var2);
   }

   public void setColumnValue(TableColumnMetaData var1, Object var2) {
      int var3 = this.columnMetaData.get(var1);
      this.columns[var3] = new TableRow.TableColumn(var1, var2);
   }

   public final TableRow.TableColumn getColumn(int var1) {
      this.checkIndex(var1);
      return this.columns[var1];
   }

   public final TableRow.TableColumn getColumn(TableColumnMetaData var1) {
      int var2 = this.columnMetaData.get(var1);
      return this.columns[var2];
   }

   public final int getColumnCount() {
      return this.columns.length;
   }

   private void checkIndex(int var1) {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("index must be >= 0 (supplied index was " + var1 + ").");
      }

      if (var1 >= this.getColumnCount()) {
         throw new IndexOutOfBoundsException(var1 + " >= " + this.getColumnCount());
      }
   }

   static class TableColumn {
      private final TableColumnMetaData metaData;
      private final Object value;

      public TableColumn(TableColumnMetaData var1, Object var2) {
         Preconditions.checkNotNull(var1, "metaData");
         if (var2 != null && !var1.getDataClass().isAssignableFrom(var2.getClass())) {
            String var3 = "The column "
               + var1.getName()
               + " is assigned to hold values of class "
               + var1.getDataClass().getName()
               + ". The assigned value is of class "
               + var2.getClass().getName()
               + " which is an incompatible type.";
            throw new IllegalArgumentException(var3);
         }

         this.metaData = var1;
         this.value = var2;
      }

      public TableColumnMetaData getMetaData() {
         return this.metaData;
      }

      public Object getValue() {
         return this.value;
      }
   }
}
