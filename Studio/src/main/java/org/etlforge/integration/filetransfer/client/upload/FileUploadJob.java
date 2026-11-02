package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.Application;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;

public final class FileUploadJob extends UiJob {
   private final FileUploader manager;
   private final ServerFile parentFile;
   private final ImmutableList<File> filesToUpload;
   private volatile ServerFileFolder serverFolder;
   private volatile String currentFile;
   private volatile boolean failed;
   private final FileUploadJob.Callback callback;
   private final CountDownLatch latch;

   public FileUploadJob(String var1, FileUploader var2, File var3) {
      this(var1, var2, null, ImmutableList.of(var3));
   }

   public FileUploadJob(String var1, FileUploader var2, ServerFile var3, ImmutableList<File> var4) {
      super(var1);
      this.manager = var2;
      this.parentFile = var3;
      this.filesToUpload = var4;
      this.callback = new FileUploadJob.Callback();
      this.latch = new CountDownLatch(var4.size());
      this.setUncaughtExceptionHandler(this.callback);
   }

   public void setServerFolder(ServerFileFolder var1) {
      this.serverFolder = var1;
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("FileUpload.Job.Started"));
      this.runJobs();
   }

   public final void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   private void runJobs() throws InterruptedException {
      for (File var2 : this.filesToUpload) {
         if (this.isCancelled() || this.failed) {
            break;
         }

         this.setMessage(Strings.format("FileUpload.Job.UploadingFileStatus", var2.getAbsolutePath()));
         ApplicationWorker var3 = Application.getWorker();
         Runnable var4 = this.getJobToRun(var2);
         var3.submitForParallelAndWait(var4, this.callback);
      }
   }

   private Runnable getJobToRun(final File var1) {
      return new Runnable() {
         @Override
         public void run() {
            try {
               FileUploadJob.this.currentFile = var1.getAbsolutePath();
               FileUploadJob.this.uploadFile(var1, FileUploadJob.this.parentFile, FileUploadJob.this.callback);
            } catch (Throwable var2) {
               FileUploadJob.this.callback.caught(var2);
            }
         }
      };
   }

   private void uploadFile(File var1, ServerFile var2, UploadServerFileCallback var3) {
      FileUploadApi var4 = this.manager.getUploadApi();
      String var5 = var2 == null ? "" : var2.getFilePath();
      var4.uploadFile(var1, this.serverFolder, var5, var3);
   }

   private final class Callback implements UploadServerFileCallback {
      private Callback() {
      }

      @Override
      public void fileWasUploaded(ServerFileUploadResult var1) {
         if (!FileUploadJob.this.isCancelled()) {
            try {
               FileUploadJob.this.setCanBeCancelled(false);
               FileUploadJob.this.setMayBeInterrupted(false);
               String var2 = FileUploadJob.this.currentFile;
               FileUploadJob.this.currentFile = null;
               FileUploadJob.this.manager.fileWasUploaded(var2, var1, FileUploadJob.this.parentFile);
            } finally {
               FileUploadJob.this.latch.countDown();
            }
         }
      }

      @Override
      public void cancelled() {
         try {
            FileUploadJob.this.setStatus(JobStatus.CANCEL);
            FileUploadJob.this.setMessage(UiJobMessages.CANCELLED);
            FileUploadJob.this.manager.cancelled();
         } finally {
            FileUploadJob.this.latch.countDown();
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!FileUploadJob.this.isCancelled()) {
            try {
               FileUploadJob.this.failed = true;
               FileUploadJob.this.setStatus(JobStatus.ERROR);
               FileUploadJob.this.setMessage(UiJobMessages.ERROR);
               FileUploadJob.this.manager.caught(var1);
               FileUploadJob.this.latch.countDown();
            } finally {
               FileUploadJob.this.latch.countDown();
            }
         }
      }
   }
}
