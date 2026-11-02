package org.etlforge.integration.data.structure.database;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.database.BeginEndQuote;

public final class DbTables implements Cloneable {
   public static final String TABLE_SEPARATOR = ",";
   public static final int TYPE_SOURCE = 1;
   public static final int TYPE_TARGET = 2;
   public static final String DB_TABLE_ID = "dbtableid";
   public static final String TABLES = "tables";
   public static final String TABLES_TYPE = "tablestype";
   private String tables;

   public DbTables() {
   }

   public DbTables(long var1, String var3) {
      this.tables = var3;
   }

   public TableUsage getTableUsage(BeginEndQuote var1) {
      return new TableUsage(this, var1 != null ? var1 : BeginEndQuote.BLANK);
   }

   public long getId() {
      return 0L;
   }

   public int getNumberOfTables() {
      int var1 = 0;
      if (this.tables != null && this.tables.trim().length() > 0) {
         var1 = this.tables.split(",").length;
      }

      return var1;
   }

   public void setTables(String var1) {
      this.tables = var1;
   }

   public String getTables() {
      return this.tables;
   }

   public boolean isAlias(String var1) {
      int var2 = this.tables.indexOf(var1);

      while (var2 != -1) {
         int var3 = var2 + var1.length();
         if (var2 > 0) {
            char var4 = this.tables.charAt(var2 - 1);
            if (var4 == ' ') {
               if (var3 >= this.tables.length()) {
                  return true;
               }

               char var5 = this.tables.charAt(var3);
               if (var5 == ",".charAt(0)) {
                  return true;
               }
            }
         }

         var2 = this.tables.indexOf(var1, var3);
      }

      return false;
   }

   public DbTables getCopy() {
      try {
         return (DbTables)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   public String toString() {
      return this.getClass().getName() + "[" + this.getTables() + "]";
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof DbTables ? areEqual(this.tables, ((DbTables)var1).tables) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.tables == null ? 0 : this.tables.hashCode();
   }

   private static boolean areEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static String[] getColumnNames() {
      return new String[]{"transformation_id", "tables", "tablestype"};
   }

   public static DbTables fromColumnsValues(StringKeyValuePairs var0) {
      DbTables var1 = new DbTables();
      var1.setTables(var0.get("tables"));
      return var1;
   }

   public String[] getColumnValues(String var1, boolean var2) {
      return new String[]{var1, this.getTables(), Integer.toString(var2 ? 1 : 2)};
   }
}
