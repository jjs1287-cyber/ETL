package org.etlforge.ui.widget.table;

public class TableColumnMetaData implements Cloneable {
   private final String name;
   private final Class<?> dataClass;
   private final boolean editable;
   private final int width;
   private final boolean resizable;

   public TableColumnMetaData(String var1, Class<?> var2, boolean var3, int var4) {
      this(var1, var2, var3, var4, true);
   }

   public TableColumnMetaData(String var1, Class<?> var2, boolean var3, int var4, boolean var5) {
      this.name = var1;
      this.dataClass = var2;
      this.editable = var3;
      this.width = var4;
      this.resizable = var5;
   }

   public final String getName() {
      return this.name;
   }

   public final Class<?> getDataClass() {
      return this.dataClass;
   }

   public final boolean isEditable() {
      return this.editable;
   }

   public final int getWidth() {
      return this.width;
   }

   public final boolean isResizable() {
      return this.resizable;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public final boolean isNaturallyComparable() {
      return Comparable.class.isAssignableFrom(this.dataClass);
   }
}
