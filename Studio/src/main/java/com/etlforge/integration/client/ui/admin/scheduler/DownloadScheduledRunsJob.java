package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.GetScheduledRunsCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import java.awt.EventQueue;
import java.util.Date;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.client.server.ServerInfo;

public final class DownloadScheduledRunsJob extends UiJob {
   private final ScheduledRunsModel model;
   private final ProjectAdministration serverApi;
   private final GetScheduledRunsCallback callback;

   public DownloadScheduledRunsJob(ScheduledRunsModel var1, ProjectAdministration var2) {
      super("Download scheduled runs information");
      this.model = var1;
      this.serverApi = var2;
      this.callback = new DownloadScheduledRunsJob.Callback();
      this.model.setDownloading(true);
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Downloading information");
      this.serverApi.getScheduledRuns(this.model.getDay(), this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private class Callback implements GetScheduledRunsCallback {
      private Callback() {
      }

      @Override
      public void scheduledRunsDownloaded(ServerInfo var1, Date var2, List<ScheduledRun> var3) {
         DownloadScheduledRunsJob.this.setMessage("Updating the UI");
         this.updateModel(var2, var3);
      }

      @Override
      public void cancelled() {
         this.updateModel(null, null);
      }

      @Override
      public void caught(Throwable var1) {
         this.updateModel(null, null);
         ErrorLog.attention(DownloadScheduledRunsJob.class, "Failed to download information about scheduled jobs: " + var1.getMessage(), var1);
      }

      private void updateModel(final Date var1, final List<ScheduledRun> var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadScheduledRunsJob.this.model.setDownloading(false);
               if (var1 != null && var2 != null) {
                  DownloadScheduledRunsJob.this.model.setDayAndScheduledRuns(var1, var2);
               }
            }
         });
      }
   }
}
