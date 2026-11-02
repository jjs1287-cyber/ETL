package org.etlforge.integration.database.driver;

public enum DriverType {
   ODBC(1),
   JDBC(2);

   private final int id;

   DriverType(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }

   public static DriverType fromId(int var0) {
      for (DriverType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid DriverType ID: " + var0);
   }
}
