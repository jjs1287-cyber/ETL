package org.etlforge.integration.activity.ui.history;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ui.resources.ActivityResources;

final class PackageResources {
   static final String OPERATION_TITLE = getString("History.Operation.Title");
   static final String SOURCE_TITLE = getString("History.Source.Title");

   private PackageResources() {
   }

   private static String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private static Icon getIcon(String var0) {
      return ActivityResources.ICONS.getIcon(var0);
   }

   static class OperationFilter {
      static final String TITLE = PackageResources.getString("History.OperationFilter.Title");
      static final String TRANSFER_SIZE = PackageResources.getString("History.OperationFilter.TransferSize");
   }

   static class OperationJob {
      static final String ERROR_TITLE = PackageResources.getString("History.OperationJob.ErrorTitle");

      static String getName(ActivitySubject var0) {
         return PackageResources.getFormattedMessage("History.OperationJob.Name", var0.getJobName());
      }
   }

   static class OperationPage {
      static final Icon ICON = PackageResources.getIcon("History.OperationPage");

      static String getEmptyPageTitle(String var0) {
         return PackageResources.getFormattedMessage("History.OperationPage.EmptyPageTitle", var0);
      }
   }

   static class SourceDeleter {
      static final String ERROR_TITLE = PackageResources.getString("History.SourceDeleter.ErrorTitle");
   }

   static class SourceDisplayer {
      static class Actions {
         static final String DELETE_TOOLTIP = PackageResources.getString("History.SourceDisplayer.Action.Delete.ToolTip");
      }
   }

   static class SourceFilter {
      static final String TITLE = PackageResources.getString("History.SourceFilter.Title");
      static final String FILE_SIZE = PackageResources.getString("History.SourceFilter.FileSize");
   }

   static class SourceJob {
      static final String STARTED = PackageResources.getString("History.SourceJob.Started");
      static final String ERROR_TITLE = PackageResources.getString("History.SourceJob.ErrorTitle");

      static String getName(ActivitySubject var0) {
         return PackageResources.getFormattedMessage("History.SourceJob.Name", var0.getJobName());
      }
   }

   static class SourceTable {
      static class Columns {
         static final String OPERATION = PackageResources.getString("History.SourceTable.Column.Operation");
         static final String SOURCE_FILES = PackageResources.getString("History.SourceTable.Column.SourceFiles");
         static final String FILE_SIZE = PackageResources.getString("History.SourceTable.Column.FileSize");
         static final String TIME_STAMP = PackageResources.getString("History.SourceTable.Column.TimeStamp");
      }
   }
}
