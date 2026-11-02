package org.etlforge.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PrimitiveConversions {
   public static int[] toIntArray(Collection<Integer> var0) {
      int[] var1 = new int[var0.size()];
      int var2 = 0;

      for (Integer var4 : var0) {
         var1[var2++] = var4;
      }

      return var1;
   }

   public static List<Integer> toIntegerList(int... var0) {
      ArrayList var1 = new ArrayList();

      for (int var5 : var0) {
         var1.add(var5);
      }

      return var1;
   }

   public static long[] toLongArray(Collection<Long> var0) {
      long[] var1 = new long[var0.size()];
      int var2 = 0;

      for (Long var4 : var0) {
         var1[var2++] = var4;
      }

      return var1;
   }

   private PrimitiveConversions() {
   }
}
