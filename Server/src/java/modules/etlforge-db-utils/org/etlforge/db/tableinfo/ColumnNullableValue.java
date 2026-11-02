package org.etlforge.db.tableinfo;

public enum ColumnNullableValue {
   NO_NULLS(0),
   NULLABLE(1),
   UNKNOWN(2);

   private final int javaSqlValue;

   ColumnNullableValue(int var3) {
      this.javaSqlValue = var3;
   }

   public int getJavaSqlValue() {
      return this.javaSqlValue;
   }

   public static ColumnNullableValue fromJavaSqlValue(int var0) {
      for (ColumnNullableValue var4 : values()) {
         if (var4.javaSqlValue == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid value: " + var0);
   }
}
