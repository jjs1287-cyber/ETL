package org.etlforge.integration.data.structure.database;

import org.etlforge.integration.database.info.DatabaseColumn;

public final class UpdateValue {
   private final DatabaseColumn column;
   private final String value;

   UpdateValue(DatabaseColumn var1, String var2) {
      assert var1 != null : "column may not be null";
      assert var2 != null : "value may not be null";
      this.column = var1;
      this.value = var2;
   }

   public DatabaseColumn getColumn() {
      return this.column;
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "Column: " + this.column.getName() + " - Value: " + this.value;
   }
}
