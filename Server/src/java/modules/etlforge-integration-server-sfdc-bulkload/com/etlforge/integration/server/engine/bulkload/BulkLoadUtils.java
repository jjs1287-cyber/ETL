package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.CharMatcher;
import java.util.List;

public final class BulkLoadUtils {
   public static boolean isBlankRecord(List<String> var0) {
      if (var0.size() == 1) {
         String var1 = (String)var0.get(0);
         return isBlank(var1);
      } else {
         return false;
      }
   }

   public static boolean isBlank(String var0) {
      return CharMatcher.WHITESPACE.matchesAllOf(var0);
   }

   private BulkLoadUtils() {
   }
}
