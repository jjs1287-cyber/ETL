package com.etlforge.integration.client.ui.project.download;

import com.etlforge.integration.client.admin.project.ProjectDownloadTracker;
import com.etlforge.integration.client.admin.project.ProjectDownloader;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.project.ProjectLocation;

public class DownloadProjectJob extends UiJob {
   private final ProjectDownloader downloader;
   private final ProjectDownloadParams params;
   private final ProjectDownloaderCallback internalCallback;
   private final ProjectDownloaderCallback externalCallback;

   public DownloadProjectJob(ProjectDownloader var1, ProjectDownloadParams var2, ProjectDownloaderCallback var3) {
      super(ProjectDownloadResources.getFormattedMessage("Job.Name", var2.project.getName()));
      this.downloader = var1;
      this.params = var2;
      this.externalCallback = var3;
      this.internalCallback = new DownloadProjectJob.InternalCallback();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.internalCallback);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(ProjectDownloadResources.getString("Job.Started"));
      this.downloader.downloadProject(this.params.project.getID(), this.params.destinationFolder, this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private class InternalCallback implements ProjectDownloaderCallback, ProjectDownloadTracker {
      private InternalCallback() {
      }

      @Override
      public void cancelled() {
         DownloadProjectJob.this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         DownloadProjectJob.this.externalCallback.caught(var1);
         String var2 = ProjectDownloadResources.getFormattedMessage("Job.Error", DownloadProjectJob.this.params.project.getName(), var1.getMessage());
         ErrorLog.attention(DownloadProjectJob.class, var2, var1);
      }

      @Override
      public void downloadCompleted(ProjectLocation var1) {
         DownloadProjectJob.this.externalCallback.downloadCompleted(var1);
      }

      @Override
      public ProjectDownloadTracker getTracker() {
         return this;
      }

      @Override
      public void downloadingProjectDefinitions() {
         DownloadProjectJob.this.setMessage("Downloading project definitions");
      }

      @Override
      public void downloadingRequiredFiles() {
         DownloadProjectJob.this.setMessage("Downloading required files");
      }
   }
}
