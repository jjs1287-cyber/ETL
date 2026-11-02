package org.etlforge.integration.database;

public final class SchemaAndTableNameSeparator {
   private final String schema;
   private final String table;

   public SchemaAndTableNameSeparator(String var1, BeginEndQuote var2) {
      String var3 = var2.unescapeName(var1);
      String var4 = var3.equals(var1) ? "." : var2.begin + "." + var2.end;
      int var5 = var3.indexOf(var4);
      if (var5 >= 0) {
         this.schema = var3.substring(0, var5);
         this.table = var3.substring(var5 + var4.length());
      } else {
         this.schema = null;
         this.table = var3;
      }
   }

   public String getSchema() {
      return this.schema;
   }

   public String getTable() {
      return this.table;
   }
}
