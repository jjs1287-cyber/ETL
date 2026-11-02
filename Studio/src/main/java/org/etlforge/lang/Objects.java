package org.etlforge.lang;

import java.util.Arrays;

public final class Objects {
   public static boolean equal(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static int hashCode(Object... var0) {
      return Arrays.hashCode(var0);
   }

   private Objects() {
   }
}
