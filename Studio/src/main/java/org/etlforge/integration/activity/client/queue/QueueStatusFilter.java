package org.etlforge.integration.activity.client.queue;

import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class QueueStatusFilter extends ActivityEntryFilter {
   private final QueueStatus status;

   public QueueStatusFilter(QueueStatus var1) {
      this.status = var1;
   }

   public QueueStatus getQueueStatus() {
      return this.status;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      QueueStatus var2 = this.getQueueStatus();
      return var2 == null ? true : var2.toLogStatus() == var1.getLogStatus();
   }
}
