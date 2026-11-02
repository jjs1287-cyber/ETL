package com.etlforge.integration.client.ui.sfdc.soql.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class ExistingLimitFinder {
   public static ExistingLimit extract(String var0) {
      if (var0.isEmpty()) {
         return null;
      } else {
         String var1 = var0.toLowerCase();
         if (var1.trim().endsWith("limit")) {
            return new ExistingLimit(var0, var1.lastIndexOf("limit"), var0.length());
         } else {
            String var2 = ".*\\s+(limit\\s+-?\\d*)((\\s*\\z)|(\\s+offset\\s+-?\\d*\\s*\\z))";
            Pattern var3 = Pattern.compile(var2, 32);
            Matcher var4 = var3.matcher(var1);
            if (var4.matches()) {
               int var5 = var4.start(1);
               int var6 = var4.end(1);
               return new ExistingLimit(var0, var5, var6);
            } else {
               return null;
            }
         }
      }
   }

   private ExistingLimitFinder() {
   }
}
