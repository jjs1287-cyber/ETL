package org.etlforge.util;

import com.google.common.base.Preconditions;

public final class IntervalFactory {
   public static Interval fromStartAndLength(int var0, int var1) {
      Preconditions.checkArgument(var1 >= 0, "length must be >= 0 (value was " + var1 + ")");
      return new IntervalFactory.IntervalImpl(var0, var1);
   }

   public static Interval fromStartAndEnd(int var0, int var1) {
      Preconditions.checkArgument(var1 >= var0, "end must be >= start (start = " + var0 + ", end = " + var1 + ")");
      return new IntervalFactory.IntervalImpl(var0, var1 - var0);
   }

   private IntervalFactory() {
   }

   private static class IntervalImpl extends AbstractInterval {
      private final int start;
      private final int length;

      public IntervalImpl(int var1, int var2) {
         this.start = var1;
         this.length = var2;
      }

      @Override
      public int length() {
         return this.length;
      }

      @Override
      public int startsAt() {
         return this.start;
      }
   }
}
