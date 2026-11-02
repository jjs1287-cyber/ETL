package org.etlforge.integration.activity.client.log;

import java.util.EnumSet;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;

public final class LogStatusFilter extends ActivityEntryFilter {
   private final EnumSet<LogStatus> acceptedStatuses;

   public static LogStatusFilter completed() {
      return new LogStatusFilter(LogStatus.completed());
   }

   public LogStatusFilter(EnumSet<LogStatus> var1) {
      this.acceptedStatuses = var1 == null ? null : EnumSet.copyOf(var1);
   }

   public EnumSet<LogStatus> getLogStatus() {
      return this.acceptedStatuses == null ? EnumSet.allOf(LogStatus.class) : EnumSet.copyOf(this.acceptedStatuses);
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return this.acceptedStatuses == null || this.acceptedStatuses.contains(var1.getLogStatus());
   }
}
