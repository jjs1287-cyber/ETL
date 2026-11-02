package org.etlforge.integration.activity.client.log;

import com.google.common.base.Strings;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;

public final class MessageFilter extends ActivityEntryFilter {
   private final String filterText;
   private final String lowerCaseFilterText;

   public MessageFilter(String var1) {
      this.filterText = Strings.nullToEmpty(var1);
      this.lowerCaseFilterText = this.filterText.toLowerCase();
   }

   public String getFilterText() {
      return this.filterText;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return this.lowerCaseFilterText.isEmpty() ? true : this.searchShortMessage(var1) || this.searchDetails(var1);
   }

   private boolean searchShortMessage(OperationLogEntry var1) {
      return var1.getShortMessage().toLowerCase().contains(this.lowerCaseFilterText);
   }

   private boolean searchDetails(OperationLogEntry var1) {
      for (OperationLogMessage var3 : var1.getDetails()) {
         if (var3.getEntryName().toLowerCase().contains(this.lowerCaseFilterText)) {
            return true;
         }

         if (var3.getMessage().toLowerCase().contains(this.lowerCaseFilterText)) {
            return true;
         }
      }

      return false;
   }
}
