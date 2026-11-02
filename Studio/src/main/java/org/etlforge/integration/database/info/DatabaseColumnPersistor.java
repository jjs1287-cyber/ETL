package org.etlforge.integration.database.info;

import org.etlforge.util.persist.Persistor;

class DatabaseColumnPersistor {
   private static final String NAME = "name";
   private static final String DATATYPE = "dataType";
   private static final String DEFAULT = "defVal";
   private static final String NULLABLE = "nullable";
   private static final String PRIMARY_KEY = "pk";
   private static final String SQLDATATYPE = "sqlDataType";
   private static final String CHARACTER_SIZE = "characterSize";
   private static final String DECIMAL_DIGITS = "decimalDigits";

   public void save(DatabaseColumn var1, Persistor var2) {
      var2.putString("name", var1.getName());
      this.writeOptionalValue("dataType", var1.getDataTypeString(), var2);
      this.writeOptionalValue("defVal", var1.getDefaultValueString(), var2);
      var2.putBoolean("nullable", var1.isNullable());
      var2.putBoolean("pk", var1.isPrimaryKey());
      var2.putInteger("sqlDataType", var1.getSqlDataType());
      var2.putInteger("characterSize", var1.getColumnSize());
      var2.putInteger("decimalDigits", var1.getDecimalDigits());
   }

   public DatabaseColumn restoreFrom(Persistor var1, DatabaseObject var2) {
      Integer var3 = var1.getInteger("sqlDataType");
      Integer var4 = var1.getInteger("characterSize");
      Integer var5 = var1.getInteger("decimalDigits");
      return new DatabaseColumn(
         var2,
         var1.getString("name"),
         var1.getString("dataType"),
         var3 != null ? var3 : 0,
         var1.getString("defVal"),
         var1.getBoolean("nullable"),
         var1.getBoolean("pk"),
         var4 != null ? var4 : 0,
         var5 != null ? var5 : 0
      );
   }

   private void writeOptionalValue(String var1, String var2, Persistor var3) {
      if (var2 != null) {
         var3.putString(var1, var2);
      }
   }
}
