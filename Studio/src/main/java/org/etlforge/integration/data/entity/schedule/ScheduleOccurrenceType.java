package org.etlforge.integration.data.entity.schedule;

public enum ScheduleOccurrenceType {
   DAILY(1),
   WEEKLY(2),
   MONTHLY(3);

   private final int id;

   ScheduleOccurrenceType(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }

   public static ScheduleOccurrenceType fromId(int var0) {
      for (ScheduleOccurrenceType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid ID: " + var0);
   }
}
