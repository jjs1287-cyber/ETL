package org.etlforge.integration.activity.ui.files;

import java.util.List;
import org.etlforge.integration.activity.client.files.SingleRunResult;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class ResultTableModel extends TypedAbstractKongaTableModel<SingleRunResult> {
   public static final TableColumnMetaData TIME_COMPLETED = new TableColumnMetaData("Time Completed", DateCell.class, false, 150);
   public static final TableColumnMetaData STATUS = new TableColumnMetaData("Status", LogStatus.class, false, 100);
   public static final TableColumnMetaData SUCCESS_RECS = new TableColumnMetaData("Success Records", Long.class, false, 125);
   public static final TableColumnMetaData FAILURE_RECS = new TableColumnMetaData("Failure Records", Long.class, false, 125);

   public ResultTableModel(List<SingleRunResult> var1) {
      super(SingleRunResult.class, TIME_COMPLETED, STATUS, SUCCESS_RECS, FAILURE_RECS);
      this.addRows(var1);
   }

   protected Object[] getColumnDataForTypedObject(SingleRunResult var1) {
      return new Object[]{
         new DateCell(var1.getLogEntry().getTimeCompleted()),
         var1.getLogEntry().getLogStatus(),
         getNumberOfRecords(var1, FileCategory.SFDC_SUCCESS),
         getNumberOfRecords(var1, FileCategory.SFDC_FAILURE)
      };
   }

   private static long getNumberOfRecords(SingleRunResult var0, FileCategory var1) {
      OperationFile var2 = var0.getFile(var1);
      return var2 == null ? -1L : var2.getNumberOfRecords();
   }
}
