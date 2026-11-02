package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;

import com.google.common.base.Function;
import com.etlforge.integration.server.engine.bulkload.db.BulkLoadBatchTabQuery;
import java.util.List;
import org.etlforge.integration.common.server.ServerDateFormat;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.salesforce.bulkload.backend.Batch;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceId;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.integration.server.ExternalApiSupport;

final class ListBatchesTask extends Task<WsRecordOfSentBatch> {
   private final WsBulkLoadInstanceId instanceId;

   public ListBatchesTask(String var1, String var2, WsBulkLoadInstanceId var3) {
      super("listBatches", var1, var2);
      this.instanceId = var3;
   }

   protected WsRecordOfSentBatch[] runTask(ExternalApiSupport var1) throws Exception {
      BulkLoadBatchTabQuery var2 = new BulkLoadBatchTabQuery(var1.getTranDbConnection(), createInstanceId(this.instanceId));
      List var3 = var2.listBatches();
      WsRecordOfSentBatch[] var4 = new WsRecordOfSentBatch[var3.size()];
      this.populateResponse(var4, var3, new ListBatchesTask.BatchConverter());
      return var4;
   }

   private static BulkLoadInstanceId createInstanceId(WsBulkLoadInstanceId var0) {
      return new BulkLoadInstanceId(new SalesforceBulkLoadId(var0.getBulkLoadGuid()), var0.getOperationInstanceId());
   }

   private static class BatchConverter implements Function<RecordOfSentBatch, WsRecordOfSentBatch> {
      private final ServerDateFormat dateFormat = Task.getDateFormat();

      private BatchConverter() {
      }

      public WsRecordOfSentBatch apply(RecordOfSentBatch var1) {
         WsRecordOfSentBatch var2 = new WsRecordOfSentBatch();
         var2.setInstanceId(convertId(var1));
         var2.setTimeSent(this.dateFormat.format(var1.getTimeSent()));
         var2.setBatch(convertBatch(var1.getBatch()));
         return var2;
      }

      private static WsBulkLoadInstanceId convertId(RecordOfSentBatch var0) {
         BulkLoadInstanceId var1 = var0.getInstanceId();
         return new WsBulkLoadInstanceId(var1.getBulkloadGuid().toString(), var1.getOperationInstanceId());
      }

      private static WsBatch convertBatch(Batch var0) {
         WsBatch var1 = new WsBatch();
         var1.setSalesforceId(var0.salesforceId);
         var1.setStatus(var0.getStatus().ordinal());
         var1.setStartPos(var0.getStartPosition());
         var1.setEndPos(var0.getEndPosition());
         var1.setNumberOfRecords(var0.getNumberOfRecordsSent());
         var1.setProcessedRecords(var0.getNumberOfProcessedRecords());
         var1.setSuccessRecords(var0.getNumberOfSuccessRecords());
         var1.setFailureRecords(var0.getNumberOfFailedRecords());
         return var1;
      }
   }
}
