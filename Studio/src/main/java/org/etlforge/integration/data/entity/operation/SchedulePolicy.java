package org.etlforge.integration.data.entity.operation;

import org.etlforge.system.EnumWithId;

public enum SchedulePolicy implements EnumWithId<Integer> {
   YIELD_IF_STILL_RUNNING(0),
   STRICT(1);

   private final int id;

   SchedulePolicy(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }

   public static SchedulePolicy getDefault() {
      return YIELD_IF_STILL_RUNNING;
   }
}
