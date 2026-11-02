package org.etlforge.integration.server.implementation.webservice.interchange.db;

final class ColumnDefaultValueGuard {
   public static String guard(String var0) {
      if (var0 == null) {
         return null;
      }

      for (char var4 : var0.toCharArray()) {
         if (!allowed(var4)) {
            return null;
         }
      }

      return var0;
   }

   private static boolean allowed(char var0) {
      if (var0 < ' ') {
         return var0 == '\n' || var0 == '\r';
      } else {
         return var0 <= '\ud7ff' ? true : var0 >= '\ue000' && var0 <= '�';
      }
   }
}
