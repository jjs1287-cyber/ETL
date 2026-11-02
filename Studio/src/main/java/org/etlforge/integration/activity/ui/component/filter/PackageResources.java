package org.etlforge.integration.activity.ui.component.filter;

import java.text.MessageFormat;
import java.util.Date;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.integration.activity.ui.resources.ActivityResources;
import org.etlforge.util.text.ThreadSafeDateFormat;

final class PackageResources {
   private PackageResources() {
   }

   private static final String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   private static final String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   static class CompositeFilter {
      static final String LABEL = PackageResources.getString("CompositeFilter.Label");
   }

   static class FilterDescriptionPanel {
      static final String LABEL = PackageResources.getString("FilterDescriptionPanel.Label");
   }

   static class MaxEntriesFilter {
      static final String LABEL = PackageResources.getString("MaxEntriesFilter.Label");

      static String getFilterString(int var0) {
         return PackageResources.getFormattedMessage("MaxEntriesFilter.String", var0);
      }
   }

   static class NameFilter {
      static final String LABEL = PackageResources.getString("OperationNameFilter.Label");

      static String getFilterString(String var0) {
         return PackageResources.getFormattedMessage("OperationNameFilter.String", var0);
      }
   }

   static class TimeWindowFilter {
      private static final ThreadSafeDateFormat FMT = new ThreadSafeDateFormat(ActivityViewUtils.getTableDateFormat());

      static final String getBefore(Date var0) {
         return PackageResources.getFormattedMessage("TimeWindowFilter.Before", FMT.format(var0));
      }

      static final String getAfter(Date var0) {
         return PackageResources.getFormattedMessage("TimeWindowFilter.After", FMT.format(var0));
      }

      static final String getBetween(Date var0, Date var1) {
         return PackageResources.getFormattedMessage("TimeWindowFilter.Between", FMT.format(var0), FMT.format(var1));
      }
   }

   static class TransferSizeFilter {
      static String getFilterString(String var0, String var1, int var2, String var3) {
         return PackageResources.getFormattedMessage("TransferSizeFilter.String", var0, var1, var2, var3);
      }
   }
}
