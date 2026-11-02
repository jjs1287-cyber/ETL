package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.util.Date;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.SuccessAndFailureCounts;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class BulkLoadInstanceTableModel extends TypedAbstractKongaTableModel<BulkLoadInstance> {
   public static final TableColumnMetaData JOB_ID = new TableColumnMetaData("Job ID", String.class, false, 100);
   public static final TableColumnMetaData START_TIME = new TableColumnMetaData("Start Time", Date.class, false, 100);
   public static final TableColumnMetaData END_TIME = new TableColumnMetaData("End Time", Date.class, false, 100);
   public static final TableColumnMetaData SUCCESS_RECORDS = new TableColumnMetaData("Success Records", Long.class, false, 100);
   public static final TableColumnMetaData FAILURE_RECORDS = new TableColumnMetaData("Failure Records", Long.class, false, 100);
   public static final TableColumnMetaData FILE_SIZE = new TableColumnMetaData("File Size", Long.class, false, 100);

   public BulkLoadInstanceTableModel() {
      super(BulkLoadInstance.class, JOB_ID, START_TIME, END_TIME, SUCCESS_RECORDS, FAILURE_RECORDS);
   }

   protected Object[] getColumnDataForTypedObject(BulkLoadInstance var1) {
      SuccessAndFailureCounts var2 = var1.getSuccessAndFailureCounts();
      return new Object[]{var1.getJobId(), var1.getStartTime(), var1.getEndTime(), var2.successCount, var2.failureCount};
   }
}
