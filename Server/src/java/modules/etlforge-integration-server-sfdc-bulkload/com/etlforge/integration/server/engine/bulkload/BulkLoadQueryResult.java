package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.etlforge.integration.server.engine.bulkload.db.BulkLoadJobInfoUpdate;
import com.etlforge.integration.server.engine.bulkload.rest.BulkLoadBatchInfoGetter;
import com.etlforge.integration.server.engine.bulkload.rest.BulkLoadCSVResultGetter;
import com.etlforge.integration.server.engine.bulkload.rest.HttpClientCreator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadBatchInfo;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.io.FileUtils;

public class BulkLoadQueryResult implements BulkLoadResultProcess {
   private BulkLoadBatchInfoGetter batchinfogetter;
   private final String targetFilepath;
   private final Charset targetFileCharset;
   private volatile boolean isDone;
   private boolean isJobOpen = true;
   private List<BulkLoadQueryResultParameter> resultParameters = null;
   private long queryTotalRecords = 0L;
   private volatile long receivedRecords = 0L;
   private final BulkLoadEndpoint endpoint;
   private final BulkLoadJobInfoUpdate jobinfoupdate;
   private BulkLoadJobInfo jobinfodb;
   private List<BulkLoadBatchInfo> batchInfoListSF;
   private Future<Void> future = null;
   private final HttpClient client;
   private final HttpClient resultClient;
   private boolean isExecuted = false;

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void dispose() {
      this.jobinfoupdate.dispose();
   }

   @Override
   public RecordCounts ResultProcess() throws SQLException, IOException, Exception {
      return this.ResultProcessImpl();
   }

   @Override
   public void waitForFuture() throws Exception {
      if (this.future != null) {
         try {
            this.future.get();
         } catch (ExecutionException var3) {
            Throwable var2 = var3.getCause();
            if (var2 instanceof Exception) {
               throw (Exception)var2;
            }

            throw Throwables.propagate(var2);
         }
      }
   }

   BulkLoadQueryResult(Connection var1, long var2, BulkLoadEndpoint var4, String var5, Charset var6) {
      this.targetFilepath = var5;
      this.targetFileCharset = var6;
      this.endpoint = var4;
      this.isDone = false;
      this.batchinfogetter = null;
      this.jobinfoupdate = new BulkLoadJobInfoUpdate(var1, var2);
      this.jobinfodb = null;
      ProxySettingsSupplier var7 = new ProxySettingsSupplier(var1);
      this.client = HttpClientCreator.newHttpClient(var7);
      this.resultClient = HttpClientCreator.newHttpClient(var7);
   }

   public static String getResultFilename(String var0, String var1, String var2, int var3) {
      String var4 = getResultFilePostfix(var1, var2, var3);
      return FileUtils.getPostfixFilename(var0, var4, ".csv");
   }

   public static String getResultFilePostfix(String var0, String var1, int var2) {
      return "_" + var0 + "_" + var1 + "_" + var2;
   }

   private String getJobId() throws SQLException {
      if (this.jobinfodb == null) {
         this.jobinfodb = this.jobinfoupdate.getBulkLoadJobInfo();
      }

      return this.jobinfodb.getJobId();
   }

   private RecordCounts checkReceivedRecords() {
      return this.queryTotalRecords <= 0L
         ? RecordCounts.ZERO
         : new RecordCounts((int)this.queryTotalRecords, (int)this.receivedRecords, (int)this.receivedRecords, 0);
   }

   private RecordCounts ResultProcessImpl() throws SQLException, IOException, Exception {
      this.batchinfogetter = new BulkLoadBatchInfoGetter(this.endpoint.getCloseJobUrl(this.getJobId()), this.endpoint.getSessionId(), this.client);
      if (this.isJobOpen) {
         BulkLoadJobInfo var1 = this.batchinfogetter.getBulkLoadJobInfo();
         if (var1 == null) {
            throw new Exception("fatal error: can not get job information for jobid=" + this.getJobId());
         }

         if (var1.isOpen()) {
            return RecordCounts.ZERO;
         }

         this.isJobOpen = false;
      }

      this.batchInfoListSF = this.batchinfogetter.getBulkLoadBatchInfoList();
      boolean var4 = this.checkQueryAllBatch();
      if (var4) {
         if (this.queryTotalRecords > 0L) {
            this.doOnlyOnceTask();
         } else {
            new File(this.targetFilepath).createNewFile();
            this.isDone = true;
         }

         this.receivedRecords = 0L;
         if (this.resultParameters != null) {
            for (int var2 = 0; var2 < this.resultParameters.size(); var2++) {
               BulkLoadQueryResultParameter var3 = this.resultParameters.get(var2);
               this.receivedRecords = this.receivedRecords + var3.getReceivedRecords();
            }
         }
      }

      return this.checkReceivedRecords();
   }

   private void doOnlyOnceTask() throws Exception {
      if (!this.isExecuted) {
         this.isExecuted = true;
         this.obtainResultParameters();
         this.doLongTask();
      }
   }

   private void doLongTask() {
      ExecutorService var1 = Executors.newSingleThreadExecutor();
      this.future = var1.submit(new Callable<Void>() {
         public Void call() throws Exception {
            try {
               BulkLoadQueryResult.this.ProcessBatchResult();
               BulkLoadQueryResult.this.CombineResults();
               return null;
            } catch (Exception var5) {
               throw var5;
            } finally {
               BulkLoadQueryResult.this.isDone = true;
            }
         }
      });
   }

   private void obtainResultParameters() throws Exception {
      if (this.resultParameters == null) {
         for (int var1 = 0; var1 < this.batchInfoListSF.size(); var1++) {
            BulkLoadBatchInfo var2 = this.batchInfoListSF.get(var1);
            if (var2.getStatusEnum() == BatchStatus.COMPLETED) {
               this.jobinfoupdate.insertBulkQueryBatchInfo(var2, this.jobinfodb, var1);
               if (this.resultParameters == null) {
                  this.resultParameters = Lists.newArrayList();
               }

               List var3 = this.batchinfogetter.getBulkQueryResultIDList(var2.getBatchId());
               if (var3 == null) {
                  String var6 = "Salesforce failed to return result list for batch " + var2.getBatchId();
                  throw new Exception(var6);
               }

               for (int var4 = 0; var4 < var3.size(); var4++) {
                  BulkLoadQueryResultParameter var5 = new BulkLoadQueryResultParameter(var2.getBatchId(), (String)var3.get(var4));
                  this.resultParameters.add(var5);
               }
            }
         }
      }
   }

   private boolean checkQueryAllBatch() throws Exception {
      this.queryTotalRecords = 0L;

      for (int var1 = 0; var1 < this.batchInfoListSF.size(); var1++) {
         BulkLoadBatchInfo var2 = this.batchInfoListSF.get(var1);
         if (!var2.isDone()) {
            return false;
         }

         if (var2.getStatusEnum() == BatchStatus.COMPLETED) {
            this.queryTotalRecords = this.queryTotalRecords + var2.getNumberRecordsProcessed();
         } else if (var2.getStatusEnum() == BatchStatus.FAILED) {
            String var3 = var2.getstatusMessage();
            throw new Exception(var3);
         }
      }

      return true;
   }

   private void ProcessBatchResult() throws Exception {
      for (int var1 = 0; var1 < this.resultParameters.size(); var1++) {
         BulkLoadQueryResultParameter var2 = this.resultParameters.get(var1);
         String var3 = getResultFilename(this.targetFilepath, var2.batchid, var2.resultid, var1);
         File var4 = new File(var3);
         boolean var5 = !var4.exists() || var4.length() <= 50L;
         if (var5) {
            String var6 = this.batchinfogetter.getJobBaseUrl() + "/batch/" + var2.batchid + "/result/" + var2.resultid;
            boolean var7 = var1 != 0;
            BulkLoadCSVResultGetter var8 = new BulkLoadCSVResultGetter(
               var6, this.batchinfogetter.getSessionID(), this.resultClient, var3, this.targetFileCharset, var7
            );
            var8.getResult(var2);
         }
      }
   }

   private boolean CombineResults() throws Exception {
      boolean var1 = true;

      for (int var2 = 0; var2 < this.resultParameters.size(); var2++) {
         BulkLoadQueryResultParameter var3 = this.resultParameters.get(var2);
         String var4 = getResultFilename(this.targetFilepath, var3.batchid, var3.resultid, var2);
         if (var2 == 0) {
            if (!FileUtils.moveFile(this.targetFilepath, var4, true)) {
               var1 = false;
            }
         } else if (!FileUtils.appendFile(this.targetFilepath, var4)) {
            var1 = false;
         }
      }

      return var1;
   }

   public static void main(String[] var0) throws Exception {
      String var1 = "Z:/dev/dataloader/bulkload/test/bulkquery.csv";
      Charset var2 = ETLForgeSystemProperty.getCharset("");
      String var3 = "https://na14-api.salesforce.com/services/async/24.0/job/750d0000000PcOVAA0";
      String var4 = "00Dd0000000cV0m!ARUAQOgnTG132lYTB_LvvWU9TQ0vnSNq9MtuGITwBh2UGNB874ecTxz3_8AIqT2hgaDp6Jj7mDUAe1KzOdxT5Zy_Dgcjj5uA";
      System.out.println("start...");
      System.out.println("end...");
   }
}
