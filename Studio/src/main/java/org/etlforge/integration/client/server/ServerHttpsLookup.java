package org.etlforge.integration.client.server;

import org.etlforge.util.preference.KongaPreferences;

                  
final class ServerHttpsLookup {
   private static final KongaPreferences PREFS = KongaPreferences.getPreferences(ServerHttpsLookup.class);

   public static boolean isUsingHttps(String var0) {
      return PREFS.getBoolean(var0, false);
   }

   public static void setUseHttps(String var0, boolean var1) {
      PREFS.putBoolean(var0, var1);
   }

   private ServerHttpsLookup() {
   }
}
