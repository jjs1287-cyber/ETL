package org.etlforge.integration.filetransfer.client.download;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.serverapi.FileDownloadCallback;
import org.etlforge.integration.filetransfer.serverapi.FileDownloadDependency;
import org.etlforge.integration.filetransfer.serverapi.FileDownloader;

final class FileDownloadJob extends UiJob {
   private final FileDownloadManager downloadManager;
   private final String fileToDownload;
   private final String parentFile;
   private final CountDownLatch latch;
   private final FileDownloadCallback callback;

   public FileDownloadJob(FileDownloadManager var1, String var2, String var3) {
      super(Strings.format("FileDownload.Job.Name", var2));
      this.downloadManager = var1;
      this.fileToDownload = var2;
      this.parentFile = var3;
      this.latch = new CountDownLatch(1);
      this.callback = new FileDownloadJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("FileDownload.Job.Started"));
      IntegrationServer var1 = IntegrationServer.getInstance();
      FileDownloader var2 = var1.getServerCall(FileDownloader.class);
      var2.downloadFile(this.fileToDownload, this.callback);
      this.latch.await();
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   private final class Callback implements FileDownloadCallback {
      private Callback() {
      }

      @Override
      public void cancelled() {
         try {
            FileDownloadJob.this.downloadManager.userCancelledDownload();
         } finally {
            FileDownloadJob.this.latch.countDown();
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!FileDownloadJob.this.isCancelled()) {
            this.handleError(var1);
         }
      }

      private void handleError(Throwable var1) {
         try {
            FileDownloadJob.this.setStatus(JobStatus.ERROR);
            FileDownloadJob.this.setMessage(Strings.format("FileDownload.Job.ErrorMessage", FileDownloadJob.this.fileToDownload));
            FileDownloadJob.this.downloadManager.handleDownloadError(FileDownloadJob.this.fileToDownload, var1);
         } finally {
            FileDownloadJob.this.latch.countDown();
         }
      }

      @Override
      public void fileWasDownloaded(ServerInfo var1, String var2, String var3, List<? extends FileDownloadDependency> var4) {
         if (!FileDownloadJob.this.isCancelled()) {
            this.postDownload(var1, var2, var3, var4);
         }
      }

      private void postDownload(ServerInfo var1, String var2, String var3, List<? extends FileDownloadDependency> var4) {
         try {
            FileDownloadJob.this.setCanBeCancelled(false);
            FileDownloadJob.this.setMayBeInterrupted(false);
            FileDownloadJob.this.downloadManager.fileWasDownloaded(var1, var2, var3, var4, FileDownloadJob.this.parentFile);
         } finally {
            FileDownloadJob.this.latch.countDown();
         }
      }
   }
}
