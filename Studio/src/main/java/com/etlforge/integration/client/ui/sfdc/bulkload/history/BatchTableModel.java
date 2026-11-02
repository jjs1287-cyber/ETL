package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.util.Date;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class BatchTableModel extends TypedAbstractKongaTableModel<RecordOfSentBatch> {
   public static final TableColumnMetaData BATCH_ID = new TableColumnMetaData("Batch ID", String.class, false, 100);
   public static final TableColumnMetaData TIME_SENT = new TableColumnMetaData("Time Sent", Date.class, false, 100);
   public static final TableColumnMetaData STATUS = new TableColumnMetaData("Status", BatchStatus.class, false, 100);
   public static final TableColumnMetaData RECORDS_SENT = new TableColumnMetaData("Records Sent", Integer.class, false, 100);
   public static final TableColumnMetaData RECORDS_PROCESSED = new TableColumnMetaData("Records Processed", Integer.class, false, 100);
   public static final TableColumnMetaData RECORDS_FAILED = new TableColumnMetaData("Records Failed", Integer.class, false, 100);

   public BatchTableModel() {
      super(RecordOfSentBatch.class, BATCH_ID, TIME_SENT, STATUS, RECORDS_SENT, RECORDS_PROCESSED, RECORDS_FAILED);
   }

   protected Object[] getColumnDataForTypedObject(RecordOfSentBatch var1) {
      return new Object[]{
         var1.getBatch().salesforceId,
         var1.getTimeSent(),
         var1.getStatus(),
         var1.getNumberOfRecordsSent(),
         var1.getNumberOfProcessedRecords(),
         var1.getNumberOfFailedRecords()
      };
   }
}
