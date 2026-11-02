package org.etlforge.db.tableinfo;

public enum ColumnIsNullableValue {
   NO("NO"),
   YES("YES"),
   UNKNOWN("");

   private final String javaSqlValue;

   ColumnIsNullableValue(String var3) {
      this.javaSqlValue = var3;
   }

   public String getJavaSqlValue() {
      return this.javaSqlValue;
   }

   public static ColumnIsNullableValue fromJavaSqlValue(String var0) {
      for (ColumnIsNullableValue var4 : values()) {
         if (var4.javaSqlValue.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid value: " + var0);
   }
}
