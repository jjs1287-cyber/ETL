package org.etlforge.ui.widget.table;

public abstract class TypedAbstractKongaTableModel<T> extends AbstractKongaTableModel {
   private final Class<T> rowObjectClass;
   private boolean allowNullRowObjects;

   public TypedAbstractKongaTableModel(Class<T> var1, TableColumnMetaData... var2) {
      super(var2);
      this.rowObjectClass = var1;
   }

   public TypedAbstractKongaTableModel(Class<T> var1, TableColumnMetaData[] var2, Iterable<T> var3) {
      this(var1, var2);

      for (Object var5 : var3) {
         this.addRow(var5);
      }
   }

   public TypedAbstractKongaTableModel(Class<T> var1, TableColumnMetaData[] var2, T... var3) {
      this(var1, var2);
      this.addRows(var3);
   }

   @Override
   protected final Object[] getColumnDataFromObject(Object var1) {
      if (var1 == null) {
         if (this.allowNullRowObjects) {
            return this.getColumnDataForTypedObject(null);
         } else {
            throw new NullPointerException();
         }
      } else {
         if (this.rowObjectClass.isInstance(var1)) {
            return this.getColumnDataForTypedObject(this.rowObjectClass.cast(var1));
         }

         this.throwIllegalRowObject(var1, this.rowObjectClass);
         return null;
      }
   }

   protected abstract Object[] getColumnDataForTypedObject(T var1);

   @Override
   public final T getRowObjectAt(int var1) {
      return this.rowObjectClass.cast(super.getRowObjectAt(var1));
   }

   public final void setAllowNullRowObjects(boolean var1) {
      this.allowNullRowObjects = var1;
   }
}
