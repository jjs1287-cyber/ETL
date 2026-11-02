package org.etlforge.integration.database.driver;

import org.etlforge.integration.database.BeginEndQuote;

public final class OdbcDriverDescriptorFactory {
   private static final String ORACLE_NAME_KEY = "ora";

   private OdbcDriverDescriptorFactory() {
   }

   public static OdbcDriverDescriptor createDescriptor(String var0, String var1, String var2) {
      BeginEndQuote var3 = BeginEndQuote.fromStrings(var1, var2);
      return createDescriptor(var0, var3);
   }

   public static OdbcDriverDescriptor createDescriptor(String var0, BeginEndQuote var1) {
      return isProbablyOracle(var0) ? new OracleOdbcDriverDescriptor(var0, var1) : new OdbcDriverDescriptor(var0, var1);
   }

   private static boolean isProbablyOracle(String var0) {
      return var0.toLowerCase().contains("ora");
   }
}
