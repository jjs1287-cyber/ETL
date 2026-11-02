package org.etlforge.ui;

import java.awt.Dimension;

public final class DimensionUtils {
   public static Dimension max(Dimension... var0) {
      int var1 = 0;
      int var2 = 0;

      for (Dimension var6 : var0) {
         var1 = Math.max(var1, var6.width);
         var2 = Math.max(var2, var6.height);
      }

      return new Dimension(var1, var2);
   }

   public static boolean isEmpty(Dimension var0) {
      return var0.width == 0 && var0.height == 0;
   }

   public static void ensureMinimumWidth(Dimension var0, int var1) {
      var0.width = Math.max(var0.width, var1);
   }

   public static Dimension withWidthAtLeast(Dimension var0, int var1) {
      return var0.width >= var1 ? var0 : new Dimension(var1, var0.height);
   }

   public static void ensureMinimumHeight(Dimension var0, int var1) {
      var0.height = Math.max(var0.height, var1);
   }

   public static Dimension withHeightAtLeast(Dimension var0, int var1) {
      return var0.height >= var1 ? var0 : new Dimension(var0.width, var1);
   }

   public static void ensureMinimumSize(Dimension var0, int var1, int var2) {
      var0.width = Math.max(var0.width, var1);
      var0.height = Math.max(var0.height, var2);
   }

   public static Dimension withMinimumSize(Dimension var0, int var1, int var2) {
      if (var0.width >= var1 && var0.height >= var2) {
         return var0;
      }

      Dimension var3 = new Dimension(var0);
      ensureMinimumSize(var3, var1, var2);
      return var3;
   }

   private DimensionUtils() {
   }
}
