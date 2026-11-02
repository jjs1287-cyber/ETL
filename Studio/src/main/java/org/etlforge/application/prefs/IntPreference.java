package org.etlforge.application.prefs;

import org.etlforge.lang.Intervals;

public final class IntPreference extends Preference<Integer> {
   private final int min;
   private final int max;

   public IntPreference(Class<?> var1, String[] var2, String var3, Integer var4, Integer var5, Integer var6) {
      super(var1, var2, var3, var6 != null ? var6 : 0);
      this.min = var4 != null ? var4 : Integer.MIN_VALUE;
      this.max = var5 != null ? var5 : Integer.MAX_VALUE;
      this.ensureDefaultIsWithinLimits();
   }

   public int get() {
      return this.getNode().getInt(this.getKey(), this.getDefaultValue());
   }

   public void set(int var1) {
      int var2 = this.get();
      int var3 = Intervals.valueWithin(var1, this.min, this.max);
      this.getNode().putInt(this.getKey(), var3);
      this.fireValueChanged(var2, var1);
   }

   private void ensureDefaultIsWithinLimits() {
      if (this.min > this.getDefaultValue()) {
         throw new IllegalArgumentException("min cannot be > def");
      }

      if (this.max < this.getDefaultValue()) {
         throw new IllegalArgumentException("max cannot be < def");
      }
   }

   public int min() {
      return this.min;
   }

   public int max() {
      return this.max;
   }
}
