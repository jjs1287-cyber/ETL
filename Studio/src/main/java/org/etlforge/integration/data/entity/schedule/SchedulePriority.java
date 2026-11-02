package org.etlforge.integration.data.entity.schedule;

import org.etlforge.system.EnumWithId;

public enum SchedulePriority implements EnumWithId<Integer> {
   Lowest("Lowest"),
   Very_Low("Very Low"),
   Lower("Lower"),
   Low("Low"),
   Medium("Medium"),
   High("High"),
   Higher("Higher"),
   Very_High("Very High"),
   Highest("Highest");

   private final String name;

   public Integer getId() {
      return this.ordinal() + 1;
   }

   @Override
   public String toString() {
      return this.name;
   }

   SchedulePriority(String var3) {
      this.name = var3;
   }
}
