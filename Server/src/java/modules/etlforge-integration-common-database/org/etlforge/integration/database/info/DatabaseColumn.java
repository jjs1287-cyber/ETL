package org.etlforge.integration.database.info;

public final class DatabaseColumn {
   private final String name;
   private final String dataType;
   private final int sqlDataType;
   private final String defaultValue;
   private final boolean nullable;
   private DatabaseObject dbObject;
   private final boolean primaryKey;
   private final int columnSize;
   private final int decimalDigits;

   public DatabaseColumn(DatabaseObject var1, String var2, String var3, String var4, boolean var5, boolean var6) {
      this(var1, var2, var3, 0, var4, var5, var6, 0, 0);
   }

   public DatabaseColumn(DatabaseObject var1, String var2, String var3, int var4, String var5, boolean var6, boolean var7) {
      this(var1, var2, var3, var4, var5, var6, var7, 0, 0);
   }

   public DatabaseColumn(DatabaseObject var1, String var2, String var3, int var4, String var5, boolean var6, boolean var7, int var8, int var9) {
      if (var2 != null && var2.trim().length() != 0) {
         this.dbObject = var1;
         this.name = var2;
         this.dataType = var3;
         this.sqlDataType = var4;
         this.defaultValue = var5;
         this.nullable = var6;
         this.primaryKey = var7;
         this.columnSize = var8;
         this.decimalDigits = var9;
      } else {
         throw new IllegalArgumentException("name cannot be null.");
      }
   }

   public DatabaseObject getDatabaseObject() {
      return this.dbObject;
   }

   public void setDatabaseObject(DatabaseObject var1) {
      this.dbObject = var1;
   }

   public String getName() {
      return this.name;
   }

   public String getDataTypeString() {
      return this.dataType;
   }

   public int getSqlDataType() {
      return this.sqlDataType;
   }

   public int getColumnSize() {
      return this.columnSize;
   }

   public int getDecimalDigits() {
      return this.decimalDigits;
   }

   public String getDefaultValueString() {
      return this.defaultValue;
   }

   public boolean isNullable() {
      return this.nullable;
   }

   public String getQualifiedName() {
      return this.dbObject.getName() + "." + this.getName();
   }

   public boolean isPrimaryKey() {
      return this.primaryKey;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 instanceof DatabaseColumn) {
         DatabaseColumn var2 = (DatabaseColumn)var1;
         if (this.primaryKey != var2.primaryKey) {
            return false;
         } else {
            return !areEqual(this.name, var2.name) ? false : areEqual(this.dbObject, var2.dbObject);
         }
      } else {
         return false;
      }
   }

   private static boolean areEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = var1 * 37 + (this.name == null ? 0 : this.name.hashCode());
      var1 = var1 * 37 + (this.dbObject == null ? 0 : this.dbObject.hashCode());
      return var1 * 37 + (this.primaryKey ? 1 : 0);
   }

   @Override
   public String toString() {
      return this.name;
   }
}
