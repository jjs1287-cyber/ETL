package org.etlforge.ui.widget.table;

public abstract class TableColumnMetaData2<T> extends TableColumnMetaData {
   public TableColumnMetaData2(String var1, Class<?> var2, boolean var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public TableColumnMetaData2(String var1, Class<?> var2, boolean var3, int var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
   }

   public abstract Object createColumnValue(T var1);
}
