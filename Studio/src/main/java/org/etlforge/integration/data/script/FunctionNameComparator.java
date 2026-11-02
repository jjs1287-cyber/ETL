package org.etlforge.integration.data.script;

import java.util.Comparator;

final class FunctionNameComparator implements Comparator<String> {
   static final FunctionNameComparator INSTANCE = new FunctionNameComparator();

   private FunctionNameComparator() {
   }

   public int compare(String var1, String var2) {
      char var3 = var1.charAt(0);
      char var4 = var2.charAt(0);
      if (var3 == '<') {
         if (var4 != '<') {
            return 1;
         }
      } else if (var4 == '<') {
         return -1;
      }

      return String.CASE_INSENSITIVE_ORDER.compare(var1, var2);
   }
}
