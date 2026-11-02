package org.etlforge.integration.data.location;

import org.etlforge.util.persist.Persistor;

public final class DataLocationPersistor {
   private static final String TYPE = "type";
   private static final String DATA = "Data";

   public static void save(DataLocation var0, Persistor var1) {
      var1.putString("type", var0.getDataLocationType().name());
      var0.writeTo(var1.createChild("Data"));
   }

   public static DataLocation restore(Persistor var0) {
      if (var0 == null) {
         throw new NullPointerException("p");
      }

      try {
         DataLocationType var1 = DataLocationType.valueOf(var0.getString("type"));
         DataLocation var2 = var1.createLocation();
         var2.restoreFrom(var0.getFirstChild("Data"));
         return var2;
      } catch (RuntimeException var3) {
         throw new IllegalArgumentException("Invalid persistor");
      }
   }

   private DataLocationPersistor() {
   }
}
