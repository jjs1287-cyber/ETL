package com.etlforge.integration.client.ui.admin.file;

import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.io.File;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;

public final class DownloadServerFileJob extends UiJob {
   private final String path;
   private final ServerAdministration serverApi;
   private final DownloadServerFileCallback callback;

   public DownloadServerFileJob(String var1, ServerAdministration var2, DownloadServerFileCallback var3) {
      super(EnterpriseAdminResources.getString("ServerFile.Download.Job.Name"));
      this.serverApi = var2;
      this.path = var1;
      this.callback = new DownloadServerFileJob.Callback(var3);
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(EnterpriseAdminResources.getString("ServerFile.Download.Job.Start"));
      this.serverApi.downloadServerFile(this.path, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private static class Callback implements DownloadServerFileCallback {
      private final DownloadServerFileCallback external;

      public Callback(DownloadServerFileCallback var1) {
         this.external = var1;
      }

      @Override
      public void cancelled() {
         this.external.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DownloadServerFileJob.class, "Failed to download the requested file from the server.", var1);
         this.external.caught(var1);
      }

      @Override
      public void fileWasDownloaded(File var1) {
         this.external.fileWasDownloaded(var1);
      }
   }
}
