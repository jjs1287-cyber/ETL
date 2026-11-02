package org.etlforge.integration.data.entity.schedule;

import java.util.concurrent.TimeUnit;

public enum OccursEvery {
   SECOND(2, 1),
   MINUTE(1, 60),
   HOUR(60, 3600);

   private final int code;
   private final int seconds;

   OccursEvery(int var3, int var4) {
      this.code = var3;
      this.seconds = var4;
   }

   public int getCode() {
      return this.code;
   }

   public static OccursEvery fromCode(int var0) {
      for (OccursEvery var4 : values()) {
         if (var4.getCode() == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid code: " + var0);
   }

   public boolean isValidValue(int var1) {
      return var1 >= 1 && var1 < this.getMaxAllowedValue() || var1 == ScheduleDailyFrequency.MAGIC_VALUE;
   }

   public int getMaxAllowedValue() {
      long var1 = TimeUnit.SECONDS.convert(1L, TimeUnit.DAYS);
      return (int)var1 / this.seconds;
   }
}
