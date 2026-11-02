package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

public class StringAsValue {
   public static Boolean valueAsBoolean(String var0) {
      return var0 == null ? null : var0.equals("1") || Boolean.parseBoolean(var0);
   }

   public static Integer valueAsInteger(String var0) {
      return var0 == null ? null : Integer.valueOf(var0);
   }

   public static Long valueAsLong(String var0) {
      return var0 == null ? null : Long.valueOf(var0);
   }

   public static Double valueAsDouble(String var0) {
      return var0 == null ? null : Double.valueOf(var0);
   }
}
