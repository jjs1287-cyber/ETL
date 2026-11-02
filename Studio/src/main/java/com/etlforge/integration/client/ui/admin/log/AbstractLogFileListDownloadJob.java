package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;

public abstract class AbstractLogFileListDownloadJob extends UiJob {
   private final ServerAdministration serverApi;
   private final GetServerFileListCallback callback;

   protected AbstractLogFileListDownloadJob(String var1, ServerAdministration var2, GetServerFileListCallback var3) {
      super(var1);
      this.serverApi = var2;
      this.callback = new AbstractLogFileListDownloadJob.Callback(var3);
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected final void runImpl() throws InterruptedException {
      this.setMessage(EnterpriseAdminResources.getString("Log.Download.Start"));
      this.makeServerCall(this.serverApi, this.callback);
   }

   @Override
   protected final void onJobCancelled() {
      this.callback.cancelled();
   }

   protected abstract void makeServerCall(ServerAdministration var1, GetServerFileListCallback var2);

   private static class Callback implements GetServerFileListCallback {
      private final GetServerFileListCallback external;

      public Callback(GetServerFileListCallback var1) {
         this.external = var1;
      }

      @Override
      public void fileListRetrieved(ServerFileLocator[] var1) {
         this.external.fileListRetrieved(var1);
      }

      @Override
      public void cancelled() {
         this.external.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DownloadDebugLogFileListJob.class, "Failed to download the list of log files", var1);
         this.external.caught(var1);
      }
   }
}
