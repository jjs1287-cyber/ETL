package org.etlforge.lang;

public final class Intervals {
   public static int valueWithin(int var0, int var1, int var2) {
      return Math.max(Math.min(var2, var0), var1);
   }

   public static double valueWithin(double var0, double var2, double var4) {
      return Math.max(Math.min(var4, var0), var2);
   }

   private Intervals() {
   }
}
