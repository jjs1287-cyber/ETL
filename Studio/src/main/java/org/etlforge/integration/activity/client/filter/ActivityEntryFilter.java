package org.etlforge.integration.activity.client.filter;

import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public abstract class ActivityEntryFilter {
   public boolean accepts(ActivityEntry var1) {
      return var1.passes(this);
   }

   public boolean isValid(OperationLogEntry var1) {
      return false;
   }

   public boolean isValid(SourceHistoryEntry var1) {
      return false;
   }
}
