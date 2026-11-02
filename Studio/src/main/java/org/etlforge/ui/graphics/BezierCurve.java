package org.etlforge.ui.graphics;

import com.google.common.base.Preconditions;
import java.awt.Graphics;

public final class BezierCurve {
   private static final int PIXELS_PER_POLYGON_SEGMENT = 4;
   private final int[] cp_x;
   private final int[] cp_y;
   private double length = 0.0;
   private final double[] param_x = new double[4];
   private final double[] param_y = new double[4];

   public BezierCurve(int[] var1, int[] var2) {
      Preconditions.checkArgument(var1.length == 4);
      Preconditions.checkArgument(var2.length == 4);
      this.cp_x = (int[])var1.clone();
      this.cp_y = (int[])var2.clone();
      calc_coef_a(this.cp_x, this.param_x);
      calc_coef_a(this.cp_y, this.param_y);
      this.length = 0.0;
      this.calculateLength();
   }

   private static void calc_coef_a(int[] var0, double[] var1) {
      var1[0] = var0[3] - 3 * var0[2] + 3 * var0[1] - var0[0];
      var1[1] = 3 * var0[2] - 6 * var0[1] + 3 * var0[0];
      var1[2] = 3 * (var0[1] - var0[0]);
      var1[3] = var0[0];
   }

   private void calculateLength() {
      if (!(this.length > 0.0)) {
         byte var1 = 20;
         double var2 = 0.0;
         double var4 = 1.0 / var1;
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         this.length = 0.0;

         for (int var10 = 0; var10 < var1 + 1; var2 += var4) {
            var6 = ft(var2, this.param_x);
            var7 = ft(var2, this.param_y);
            if (var10 > 0) {
               this.length = this.length + Math.sqrt(Math.pow(var8 - var6, 2.0) + Math.pow(var9 - var7, 2.0));
            }

            var8 = var6;
            var9 = var7;
            var10++;
         }
      }
   }

   private static int ft(double var0, double[] var2) {
      return (int)(var2[3] + var0 * (var2[2] + var0 * (var2[1] + var0 * var2[0])) + 0.5);
   }

   public void paint(Graphics var1) {
      int var2 = 10;
      if (this.length > 0.0) {
         var2 = (int)Math.round(this.length / 4.0);
      }

      if (var2 < 6) {
         var2 = 6;
      }

      double var3 = 0.0;
      double var5 = 1.0 / var2;
      int[] var7 = new int[var2 + 1];
      int[] var8 = new int[var2 + 1];
      int var9 = 0;
      int var10 = 0;
      this.length = 0.0;

      for (int var11 = 0; var11 < var2 + 1; var3 += var5) {
         var7[var11] = ft(var3, this.param_x);
         var8[var11] = ft(var3, this.param_y);
         if (var11 > 0) {
            this.length = this.length + Math.sqrt(Math.pow(var9 - var7[var11], 2.0) + Math.pow(var10 - var8[var11], 2.0));
         }

         var9 = var7[var11];
         var10 = var8[var11];
         var11++;
      }

      var1.drawPolyline(var7, var8, var2 + 1);
   }
}
