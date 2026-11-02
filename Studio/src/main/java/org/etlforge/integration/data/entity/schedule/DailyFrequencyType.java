package org.etlforge.integration.data.entity.schedule;

public enum DailyFrequencyType {
   ONCE(1),
   EVERY_X(0);

   private final int id;

   DailyFrequencyType(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }

   public static DailyFrequencyType fromId(int var0) {
      for (DailyFrequencyType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid ID: " + var0);
   }
}
