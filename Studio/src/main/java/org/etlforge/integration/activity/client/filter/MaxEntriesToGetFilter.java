package org.etlforge.integration.activity.client.filter;

import java.util.List;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class MaxEntriesToGetFilter extends ActivityEntryFilter {
   private final int maxEntriesToGet;

   public MaxEntriesToGetFilter(int var1) {
      this.maxEntriesToGet = var1;
   }

   public int getMaxEntriesToGet() {
      return this.maxEntriesToGet;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return true;
   }

   @Override
   public boolean isValid(SourceHistoryEntry var1) {
      return true;
   }

   public <T> List<T> getFilteredList(List<T> var1) {
      int var2 = var1.size();
      int var3 = this.maxEntriesToGet;
      return var3 >= 0 && var2 > var3 ? var1.subList(0, var3) : var1;
   }
}
