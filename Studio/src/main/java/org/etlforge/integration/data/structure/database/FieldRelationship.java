package org.etlforge.integration.data.structure.database;

import org.etlforge.integration.database.info.DatabaseColumn;

public final class FieldRelationship {
   private final DatabaseColumn parentColumn;
   private final DatabaseColumn childColumn;

   public FieldRelationship(DatabaseColumn var1, DatabaseColumn var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("parentColumn cannot be null.");
      }

      if (var2 == null) {
         throw new IllegalArgumentException("childColumn cannot be null.");
      }

      this.parentColumn = var1;
      this.childColumn = var2;
   }

   public DatabaseColumn getParentColumn() {
      return this.parentColumn;
   }

   public DatabaseColumn getChildColumn() {
      return this.childColumn;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.parentColumn.getDatabaseObject().getName());
      var1.append(".");
      var1.append(this.parentColumn.getName());
      var1.append(" \u2014 ");
      var1.append(this.childColumn.getDatabaseObject().getName());
      var1.append(".");
      var1.append(this.childColumn.getName());
      return var1.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 instanceof FieldRelationship) {
         FieldRelationship var2 = (FieldRelationship)var1;
         return !areColumnsEqual(this.parentColumn, var2.parentColumn) ? false : areColumnsEqual(this.childColumn, var2.childColumn);
      } else {
         return false;
      }
   }

   private static boolean areColumnsEqual(DatabaseColumn var0, DatabaseColumn var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = var1 * 37 + (this.parentColumn == null ? 0 : this.parentColumn.hashCode());
      return var1 * 37 + (this.childColumn == null ? 0 : this.childColumn.hashCode());
   }
}
