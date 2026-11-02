package org.etlforge.integration.activity.ui.log;

public final class LogTableColumnsPreference {
   public static OperationLogTableColumn[] selectedColumns() {
      return defaultColumns();
   }

   private static OperationLogTableColumn[] defaultColumns() {
      return new OperationLogTableColumn[]{
         OperationLogTableColumn.OPERATION,
         OperationLogTableColumn.STATUS,
         OperationLogTableColumn.TIME_STARTED,
         OperationLogTableColumn.TIME_DONE,
         OperationLogTableColumn.DURATION,
         OperationLogTableColumn.MESSAGE
      };
   }

   private LogTableColumnsPreference() {
   }
}
