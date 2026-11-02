package org.etlforge.integration.database.info;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public abstract class DatabaseObject implements Cloneable {
   private String name;
   private String schema;
   private List<DatabaseColumn> columns;

   public DatabaseObject(String var1, String var2) {
      validateName(var1);
      this.name = var1;
      this.schema = var2 == null ? "" : var2;
      this.columns = Lists.newArrayList();
   }

   public DatabaseObject(DatabaseObject var1, String var2) {
      validateName(var2);
      this.name = var2;
      this.schema = var1.schema;
      this.columns = var1.getAllColumns();
   }

   private static void validateName(String var0) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var0), "name cannot be null or blank");
   }

   public abstract DatabaseObjectType getType();

   public final String getName() {
      return this.name;
   }

   public final String getSchema() {
      return this.schema;
   }

   public final void addColumn(DatabaseColumn var1) {
      if (var1 != null) {
         var1.setDatabaseObject(this);
         this.columns.add(var1);
      }
   }

   public final List<DatabaseColumn> getAllColumns() {
      return Lists.newArrayList(this.columns);
   }

   public final int indexOf(DatabaseColumn var1) {
      return this.columns.indexOf(var1);
   }

   public final int getNumberOfColumns() {
      return this.columns.size();
   }

   public final void removeAllColumns() {
      for (DatabaseColumn var2 : this.columns) {
         var2.setDatabaseObject(null);
      }

      this.columns = Lists.newArrayList();
   }

   public final List<DatabaseColumn> getPrimaryKeys() {
      ArrayList var1 = Lists.newArrayList();

      for (DatabaseColumn var3 : this.columns) {
         if (var3.isPrimaryKey()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public final boolean hasPrimaryKeys() {
      for (DatabaseColumn var2 : this.columns) {
         if (var2.isPrimaryKey()) {
            return true;
         }
      }

      return false;
   }

   public final DatabaseColumn getColumn(String var1) {
      DatabaseColumn var2 = this.findColumn(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unknown column: " + var1);
      } else {
         return var2;
      }
   }

   public final DatabaseColumn findColumn(String var1) {
      for (DatabaseColumn var3 : this.columns) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      if (!var1.getClass().equals(this.getClass())) {
         return false;
      }

      DatabaseObject var2 = (DatabaseObject)var1;
      return this.name.equals(var2.name) && this.schema.equals(var2.schema);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.name.hashCode();
      return 31 * var1 + this.schema.hashCode();
   }

   public DatabaseObject clone() {
      try {
         DatabaseObject var1 = (DatabaseObject)super.clone();
         var1.columns = Lists.newArrayList();

         for (DatabaseColumn var3 : this.columns) {
            DatabaseColumn var4 = new DatabaseColumn(
               var1,
               var3.getName(),
               var3.getDataTypeString(),
               var3.getSqlDataType(),
               var3.getDefaultValueString(),
               var3.isNullable(),
               var3.isPrimaryKey(),
               var3.getColumnSize(),
               var3.getDecimalDigits()
            );
            var1.columns.add(var4);
         }

         return var1;
      } catch (CloneNotSupportedException var5) {
         throw new RuntimeException(var5);
      }
   }

   public DatabaseObject createCopy(String var1) {
      validateName(var1);
      DatabaseObject var2 = null;
      var2 = this.clone();
      var2.name = var1;
      return var2;
   }

   public DatabaseObject createCopy(String var1, String var2) {
      validateName(var2);
      DatabaseObject var3 = null;
      var3 = this.clone();
      var3.name = var2;
      var3.schema = var1;
      return var3;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
