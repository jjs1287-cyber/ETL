package org.etlforge.integration.client.scheduler;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;

public final class ScheduledOperation {
   private final Schedule schedule;
   private final Operation operation;

   public ScheduledOperation(Schedule var1, Operation var2) {
      this.schedule = Preconditions.checkNotNull(var1, "schedule");
      this.operation = Preconditions.checkNotNull(var2, "operation");
   }

   public Schedule getSchedule() {
      return this.schedule;
   }

   public Operation getOperation() {
      return this.operation;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ScheduledOperation)) {
         return false;
      }

      ScheduledOperation var2 = (ScheduledOperation)var1;
      return this.schedule.equals(var2.schedule) && this.operation.equals(var2.operation);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.schedule, this.operation);
   }
}
