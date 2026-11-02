package org.etlforge.integration.database.info;

public enum DatabaseObjectType {
   TABLE("TABLE"),
   SYSTEM_TABLE("SYSTEM TABLE"),
   VIEW("VIEW"),
   SYNONYM("SYNONYM"),
   STORED_PROCEDURE("STORED PROCEDURE"),
   MANUAL_SQL("MANUAL SQL");

   private final String stringVal;

   DatabaseObjectType(String var3) {
      this.stringVal = var3;
   }

   @Override
   public String toString() {
      return this.stringVal;
   }

   public static DatabaseObjectType fromString(String var0) {
      for (DatabaseObjectType var4 : values()) {
         if (var4.name().equals(var0) || var4.stringVal.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("No matching DatabaseObjectType " + var0);
   }
}
