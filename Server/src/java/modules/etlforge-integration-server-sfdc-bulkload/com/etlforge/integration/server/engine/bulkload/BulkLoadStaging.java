package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import com.etlforge.integration.server.engine.bulkload.db.OperationProgressTab;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.system.Disposable;

final class BulkLoadStaging implements Disposable {
   private final Connection backendDb;
   private final BulkLoadParams bulkLoadParams;
   private final BulkLoadDbPersistor persistor;
   private BatchProcessConfig batchConfig;
   private ErrorFileLog errorLog;
   private BulkLoadProcess bulkLoad;
   private boolean bypassDeployedData;
   private volatile Logger logger = Logger.getAnonymousLogger();
   private final Object lock = new Object();

   public BulkLoadStaging(Connection var1, BulkLoadParams var2, BatchProcessConfig var3) {
      this.backendDb = var1;
      this.bulkLoadParams = var2;
      this.batchConfig = var3;
      this.persistor = new BulkLoadDbPersistor(var1, var2, this.bulkLoadParams.isQuery());
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
      this.persistor.setLogger(var1);
   }

   public BulkLoadResult runBulkLoad(HttpClient var1) throws BulkLoadException, InterruptedException {
      try {
         this.createProcess(var1);
         return this.runProcess();
      } finally {
         synchronized (this.lock) {
            this.cleanup();
         }
      }
   }

   private void cleanup() {
      try {
         this.disposeBulkLoad();
      } finally {
         this.closeErrorLog();
      }
   }

   private void disposeBulkLoad() {
      if (this.bulkLoad != null) {
         this.bulkLoad.dispose();
         this.bulkLoad = null;
      }
   }

   private void closeErrorLog() {
      if (this.errorLog != null) {
         this.errorLog.close();
         this.errorLog = null;
      }
   }

   private void createProcess(HttpClient var1) throws BulkLoadException {
      synchronized (this.lock) {
         Preconditions.checkState(this.bulkLoad == null, "A bulkload process is already running");
         this.readDeployedProperties();
         JobResultProcessThread var3 = this.createResultThread();
         this.bulkLoad = new BulkLoadProcess(this.bulkLoadParams, var3, var1);
         this.bulkLoad.setBatchProcessConfig(this.batchConfig);
         this.bulkLoad.setMonitor(this.persistor);
         this.persistor.setLogger(this.logger);
         this.bulkLoad.setMessagePersistor(this.persistor);
         this.bulkLoad.setLogger(this.logger);
         this.errorLog = this.createErrorLog();
         this.bulkLoad.setErrorLog(this.errorLog);
      }
   }

   private void readDeployedProperties() throws BulkLoadException {
      if (!this.bypassDeployedData) {
         try {
            this.augmentBulkLoadParams();
            this.augmentBatchProcessConfig();
         } catch (ServerDbException var2) {
            throw new BulkLoadException(100, var2);
         }
      }
   }

   private void augmentBulkLoadParams() throws ServerDbException {
      BulkLoadParamsFactory var1 = new BulkLoadParamsFactory(this.backendDb, this.bulkLoadParams);
      var1.fromId(this.bulkLoadParams.getBulkloadId());
   }

   private void augmentBatchProcessConfig() throws ServerDbException {
      BatchProcessConfigFactory var1 = new BatchProcessConfigFactory(this.backendDb);
      var1.setTemplate(this.batchConfig);
      this.batchConfig = var1.getConfig(this.bulkLoadParams.getBulkloadId());
   }

   private JobResultProcessThread createResultThread() throws BulkLoadException {
      try {
         Connection var1 = ServerDbInfo.getTranDbConnectionInfo().getConnection();
         BulkLoadResultProcess var2;
         if (this.bulkLoadParams.getCreateJobParams().isQuery()) {
            String var3 = this.bulkLoadParams.getTargetFile().getAbsolutePath();
            Charset var4 = this.bulkLoadParams.getEncoding();
            var2 = new BulkLoadQueryResult(var1, this.bulkLoadParams.getOperationInstance(), this.bulkLoadParams.getEndpoint(), var3, var4);
         } else {
            var2 = new BulkLoadJobResultProcess(var1, this.bulkLoadParams.getOperationInstance(), this.bulkLoadParams.getEndpoint());
         }

         OperationProgressTab var6 = new OperationProgressTab(var1, this.bulkLoadParams);
         return new JobResultProcessThread(var2, var6);
      } catch (SQLException var5) {
         throw new BulkLoadException(105, "Failed to open a DB connection for the result monitor thread.", var5);
      }
   }

   private ErrorFileLog createErrorLog() {
      ErrorFileLog var1 = new ErrorFileLog(this.bulkLoadParams);
      var1.setLogger(this.logger);
      var1.setMaxEntries(100);
      return var1;
   }

   private BulkLoadResult runProcess() throws InterruptedException {
      try {
         this.updateBackendDbOnProcessStarted();
         this.bulkLoad.run();
         return this.getResult();
      } catch (BulkLoadException var2) {
         return BulkLoadResult.fatalError(var2);
      }
   }

   private void updateBackendDbOnProcessStarted() throws BulkLoadException {
      try {
         this.persistor.processStarted();
      } catch (PersistorException var2) {
         throw new BulkLoadException(var2);
      }
   }

   private BulkLoadResult getResult() {
      boolean var1 = this.persistor.isComplete();
      if (!var1) {
         this.errorLog.writeError("Not all records in the source file were sent to salesforce.", null);
      }

      String var2 = this.persistor.getBatchTab().getFailedBatchesMessage();
      if (this.persistor.getBatchTab().allBatchesFailed()) {
         BulkLoadException var4 = new BulkLoadException(-1, var2);
         return BulkLoadResult.fatalError(var4);
      } else if (var2 != null) {
         BulkLoadException var3 = new BulkLoadException(0, var2);
         return BulkLoadResult.success(var1, var3, this.bulkLoad.getNumberOfRecordsSent(), this.errorLog.isEmpty() ? null : this.errorLog.getFilePath());
      } else {
         return BulkLoadResult.success(var1, null, this.bulkLoad.getNumberOfRecordsSent(), this.errorLog.isEmpty() ? null : this.errorLog.getFilePath());
      }
   }

   public void cancel() {
      synchronized (this.lock) {
         if (this.bulkLoad != null) {
            this.logCancelRequest();
            this.bulkLoad.cancel();
         }
      }
   }

   private void logCancelRequest() {
      if (this.logger != null) {
         this.logger.info("Retrieved request to cancel a running bulk load process");
      }
   }

   public boolean cancel(long var1, TimeUnit var3) throws InterruptedException {
      BulkLoadProcess var4;
      synchronized (this.lock) {
         var4 = this.bulkLoad;
      }

      if (var4 == null) {
         return true;
      }

      this.logCancelRequest();
      return var4.cancel(var1, var3);
   }

   @Override
   public void dispose() {
      this.persistor.dispose();
   }

   public void setBypassDeployedData(boolean var1) {
      this.bypassDeployedData = var1;
   }
}
