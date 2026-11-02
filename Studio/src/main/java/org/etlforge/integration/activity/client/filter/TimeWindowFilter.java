package org.etlforge.integration.activity.client.filter;

import java.util.Date;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class TimeWindowFilter extends ActivityEntryFilter {
   private final Long startDate;
   private final Long endDate;

   public TimeWindowFilter(Date var1, Date var2) {
      this.startDate = var1 != null ? var1.getTime() : null;
      this.endDate = var2 != null ? var2.getTime() : null;
   }

   public Date getStartDate() {
      return this.startDate != null ? new Date(this.startDate) : null;
   }

   public Date getEndDate() {
      return this.endDate != null ? new Date(this.endDate) : null;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return var1.occurredWithinTimeWindow(this.getStartDate(), this.getEndDate());
   }

   @Override
   public boolean isValid(SourceHistoryEntry var1) {
      return var1.occurredWithinTimeWindow(this.getStartDate(), this.getEndDate());
   }
}
