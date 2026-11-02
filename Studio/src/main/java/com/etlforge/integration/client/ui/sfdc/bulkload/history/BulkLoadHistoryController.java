package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.bulkload.serverapi.BulkLoadInfoProvider;
import com.etlforge.integration.client.bulkload.serverapi.ListBatchesCallback;
import com.etlforge.integration.client.bulkload.serverapi.ListInstancesCallback;
import java.util.List;
import java.util.Set;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.activity.ui.files.DefaultOperationFileController;
import org.etlforge.integration.activity.ui.files.OperationFileController;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.csv.CSVParams;

public final class BulkLoadHistoryController implements OperationFileController {
   private final BulkLoadHistoryModel model;
   private final BulkLoadInfoProvider backend;
   private final Object lock = new Object();
   private final Set<UiJob> outstandingJobs = Sets.newHashSet();
   private final DefaultOperationFileController fileController;

   public BulkLoadHistoryController(BulkLoadHistoryModel var1, BulkLoadInfoProvider var2) {
      this.model = Preconditions.checkNotNull(var1);
      this.backend = Preconditions.checkNotNull(var2);
      this.fileController = this.createFileController();
   }

   private DefaultOperationFileController createFileController() {
      DefaultOperationFileController var1 = new DefaultOperationFileController(CSVParams.DEFAULT);
      var1.setWaitService(this.model.asWaitService());
      return var1;
   }

   public void enableDownloadOfRemoteFiles(StatusFileDownloaderFactory var1) {
      this.fileController.enableDownloadOfRemoteFiles(var1);
   }

   public void cancelOutstandingJobs() {
      synchronized (this.lock) {
         for (UiJob var3 : this.outstandingJobs) {
            var3.cancel();
         }

         this.outstandingJobs.clear();
      }
   }

   public void downloadHistory() {
      this.startJob(new BulkLoadHistoryController.InstanceDownloader());
   }

   private void startJob(UiJob var1) {
      this.model.setTalkingToServer(true);
      synchronized (this.lock) {
         this.outstandingJobs.add(var1);
      }

      var1.submit();
   }

   public void downloadBatches(BulkLoadInstance var1) {
      this.startJob(new BulkLoadHistoryController.BatchDownloader(var1));
   }

   private void removeOutstandingJob(UiJob var1) {
      synchronized (this.lock) {
         this.outstandingJobs.remove(var1);
      }
   }

   @Override
   public void openFile(OperationFile var1, Receiver<ResultFileSummary> var2) {
      this.fileController.openFile(var1, var2);
   }

   private class BatchDownloader extends UiJob {
      private final BulkLoadInstance instance;
      private final BulkLoadHistoryController.BatchDownloader.Callback callback = new BulkLoadHistoryController.BatchDownloader.Callback();

      public BatchDownloader(BulkLoadInstance var2) {
         super("Downloading list of bulk load batches");
         this.instance = var2;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         BulkLoadHistoryController.this.backend.listBatches(this.instance.getId(), this.callback);
      }

      private class Callback extends AbstractServerCallback implements ListBatchesCallback {
         private Callback() {
         }

         @Override
         public void gotBatches(List<RecordOfSentBatch> var1) {
            if (!BatchDownloader.this.isCancelled()) {
               this.done(var1);
            }
         }

         private void done(List<RecordOfSentBatch> var1) {
            BulkLoadHistoryController.this.removeOutstandingJob(BatchDownloader.this);
            if (var1 != null) {
               BulkLoadHistoryController.this.model.setBatches(BatchDownloader.this.instance, var1);
            }

            BulkLoadHistoryController.this.model.setTalkingToServer(false);
         }

         @Override
         public void caught(Throwable var1) {
            this.done(null);
            super.caught(var1);
         }

         @Override
         public void cancelled() {
            this.done(null);
            super.cancelled();
         }
      }
   }

   private class InstanceDownloader extends UiJob {
      private final BulkLoadHistoryController.InstanceDownloader.Callback callback = new BulkLoadHistoryController.InstanceDownloader.Callback();

      public InstanceDownloader() {
         super("Downloading bulk load history");
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         BulkLoadHistoryController.this.backend.listInstances(BulkLoadHistoryController.this.model.getInstanceFilter(), this.callback);
      }

      private class Callback extends AbstractServerCallback implements ListInstancesCallback {
         private Callback() {
         }

         @Override
         public void gotInstances(List<BulkLoadInstance> var1) {
            if (!InstanceDownloader.this.isCancelled()) {
               this.done(var1);
            }
         }

         private void done(List<BulkLoadInstance> var1) {
            BulkLoadHistoryController.this.removeOutstandingJob(InstanceDownloader.this);
            if (var1 != null) {
               BulkLoadHistoryController.this.model.setInstances(var1);
            }

            BulkLoadHistoryController.this.model.setTalkingToServer(false);
         }

         @Override
         public void caught(Throwable var1) {
            this.done(null);
            super.caught(var1);
         }

         @Override
         public void cancelled() {
            this.done(null);
            super.cancelled();
         }
      }
   }
}
