package com.etlforge.integration.server.engine.bulkload;

import com.etlforge.integration.server.engine.bulkload.db.BulkLoadJobInfoUpdate;
import com.etlforge.integration.server.engine.bulkload.rest.BulkLoadBatchInfoGetter;
import com.etlforge.integration.server.engine.bulkload.rest.HttpClientCreator;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadBatchInfo;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;

public class BulkLoadJobResultProcess implements BulkLoadResultProcess {
   private long successRecords;
   private long failureRecords;
   private final BulkLoadJobInfoUpdate jobinfoupdate;
   private final BulkLoadEndpoint endpoint;
   private volatile boolean isDone;
   private final HttpClient client;

   public BulkLoadJobResultProcess(Connection var1, long var2, BulkLoadEndpoint var4) {
      this.jobinfoupdate = new BulkLoadJobInfoUpdate(var1, var2);
      this.endpoint = var4;
      this.isDone = false;
      this.client = HttpClientCreator.newHttpClientDB(var1);
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void dispose() {
      this.jobinfoupdate.dispose();
   }

   @Override
   public void waitForFuture() {
   }

   @Override
   public RecordCounts ResultProcess() throws SQLException, IOException, Exception {
      RecordCounts var1 = RecordCounts.ZERO;
      BulkLoadJobInfo var2 = this.jobinfoupdate.getBulkLoadJobInfo();
      this.isDone = var2.isDone();
      if (this.isDone) {
         return var1;
      }

      if (var2.getJobId().isEmpty()) {
         return var1;
      }

      List var3 = this.jobinfoupdate.getBulkLoadBatchInfoList();
      String var4 = this.endpoint.getCloseJobUrl(var2.getJobId());
      String var5 = this.endpoint.getSessionId();
      BulkLoadBatchInfoGetter var6 = new BulkLoadBatchInfoGetter(var4, var5, this.client);
      Map var7 = var6.getBulkLoadBatchInfoListMap();
      BulkLoadBatchInfo var8 = null;

      for (Object __etlItem67 : var3) {
         BulkLoadBatchInfo var10 = (BulkLoadBatchInfo) __etlItem67;
         var8 = (BulkLoadBatchInfo)var7.get(var10.getBatchId());
         if (var8 != null) {
            var8.setNumberOfRecordsSent(var10.getNumberOfRecordsSent());
            this.jobinfoupdate.updateBatchInfo(var8);
            if (!var10.isDone() && var8.isDone()) {
               var8.setStartPos(var10.getStartPos());
               var8.setEndPos(var10.getEndPos());
               this.ResultProcessBatch(var2, var8, var6);
            }

            var1 = var1.add(var8.getRecordCounts());
         }
      }

      var2 = this.jobinfoupdate.getBulkLoadJobInfo();
      if (!var2.isClosed()) {
         return var1;
      }

      var3 = this.jobinfoupdate.getBulkLoadBatchInfoList();
      BulkLoadJobInfo.BulkLoadJobInfoEnum var19 = this.getStatusForJob(var3);
      boolean var20 = BulkLoadJobInfo.BulkLoadJobInfoEnum.isDone(var19);
      if (!var20) {
         return var1;
      }

      BulkLoadResultReport.BulkLoadResultReportHelper var11 = new BulkLoadResultReport.BulkLoadResultReportHelper(var2.getSourceFilename());
      var11.initSuccessFailureFileDir(var2.getOperationid(), this.jobinfoupdate.getOperationInstanceId());
      BulkLoadResultReport var12 = new BulkLoadResultReport(var11, var2.getSalesforceAction());
      var12.combineResultFiles(this.successRecords, this.failureRecords, var2, var3);
      File var13 = var11.getSuccessFile();
      File var14 = var11.getFailureFile();
      String var15 = var2.getSourceFileCharEncode().name();
      this.jobinfoupdate
         .addOperationFileStatusTab(
            var2.getOperationid(), this.successRecords, 0, var13.length(), var11.getRelativePath(var13), var15, "Salesforce BulkLoad Success"
         );
      this.jobinfoupdate
         .addOperationFileStatusTab(
            var2.getOperationid(), this.failureRecords, 1, var14.length(), var11.getRelativePath(var14), var15, "Salesforce BulkLoad Failure"
         );
      this.isDone = this.jobinfoupdate.updateJobInfo(var19);
      return var1;
   }

   private BulkLoadJobInfo.BulkLoadJobInfoEnum getStatusForJob(List<BulkLoadBatchInfo> var1) {
      BulkLoadJobInfo.BulkLoadJobInfoEnum var2 = BulkLoadJobInfo.BulkLoadJobInfoEnum.COMPLETED;
      this.successRecords = this.failureRecords = 0L;

      for (BulkLoadBatchInfo var4 : var1) {
         if (!var4.isDone()) {
            return BulkLoadJobInfo.BulkLoadJobInfoEnum.OPEN;
         }

         if (var4.getStatusEnum() == BatchStatus.NOTPROCESSED) {
            this.failureRecords = this.failureRecords + var4.getNumberOfRecordsSent();
            var2 = BulkLoadJobInfo.BulkLoadJobInfoEnum.ABORTED;
         } else if (var4.getStatusEnum() == BatchStatus.FAILED) {
            this.failureRecords = this.failureRecords + var4.getNumberOfRecordsSent();
            if (var2 == BulkLoadJobInfo.BulkLoadJobInfoEnum.COMPLETED) {
               var2 = BulkLoadJobInfo.BulkLoadJobInfoEnum.FAILED;
            }
         } else {
            this.successRecords = this.successRecords + var4.getSuccessRecords();
            this.failureRecords = this.failureRecords + var4.getFailureRecords();
         }
      }

      return var2;
   }

   private boolean ResultProcessBatch(BulkLoadJobInfo var1, BulkLoadBatchInfo var2, BulkLoadBatchInfoGetter var3) throws SQLException, Exception {
      BulkLoadResultReport.BulkLoadResultReportHelper var4 = new BulkLoadResultReport.BulkLoadResultReportHelper(var1.getSourceFilename());
      String var5 = var4.getBatchFileName(var2.getBatchId());
      boolean var6 = var3.getBulkLoadBatchResult(var2, var5, var1.getSourceFileCharEncode());
      if (var6) {
         BulkLoadResultReport var7 = new BulkLoadResultReport(var4, var1.getSalesforceAction());
         return var7.produceBatchResultReport(var2, var1);
      } else {
         return true;
      }
   }
}
