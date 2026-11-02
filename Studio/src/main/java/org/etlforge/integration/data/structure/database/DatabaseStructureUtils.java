package org.etlforge.integration.data.structure.database;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.lang.KongaStringUtils;

public final class DatabaseStructureUtils {
   public static String escape(String var0, BeginEndQuote var1) {
      String var2 = var1.begin;
      String var3 = var1.end;
      if (var0.indexOf(32) >= 0 && !var0.startsWith(var2) && !var0.endsWith(var3)) {
         var0 = var2 + var0 + var3;
      }

      return var0;
   }

   public static String unescape(String var0) {
      return var0.startsWith("\"") && var0.endsWith("\"") && var0.contains(" ") ? KongaStringUtils.unwrap(var0) : var0;
   }

   private DatabaseStructureUtils() {
   }
}
