package org.etlforge.integration.activity.client.filter;

import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class OperationNameFilter extends ActivityEntryFilter {
   private final String text;

   public OperationNameFilter(String var1) {
      this.text = var1;
   }

   public String getFilterText() {
      return this.text;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return this.isValid(var1.getOperationName());
   }

   @Override
   public boolean isValid(SourceHistoryEntry var1) {
      return this.isValid(var1.getOperationName());
   }

   private boolean isValid(String var1) {
      return this.text == null || var1.toLowerCase().contains(this.text.toLowerCase());
   }
}
