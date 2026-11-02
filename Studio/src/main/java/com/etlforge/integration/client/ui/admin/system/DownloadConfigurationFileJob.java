package com.etlforge.integration.client.ui.admin.system;

import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.admin.server.ServerConfigurationFile;
import com.etlforge.integration.client.admin.server.ViewServerConfigurationFileCallback;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;

final class DownloadConfigurationFileJob extends UiJob {
   private final ServerAdministration serverApi;
   private final DownloadConfigurationFileJob.Callback callback = new DownloadConfigurationFileJob.Callback();
   private volatile ServerConfigurationFile file;
   private final CountDownLatch latch = new CountDownLatch(1);

   public DownloadConfigurationFileJob(ServerAdministration var1) {
      super("Downloading etlforge.conf");
      this.serverApi = var1;
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   public ServerConfigurationFile getFile() {
      try {
         this.latch.await();
         return this.file;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return null;
      }
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Downloading the file");
      this.serverApi.viewServerConfigurationFile(this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private class Callback implements ViewServerConfigurationFileCallback {
      private Callback() {
      }

      @Override
      public void configurationFileReceived(ServerConfigurationFile var1) {
         DownloadConfigurationFileJob.this.file = var1;
         DownloadConfigurationFileJob.this.latch.countDown();
      }

      @Override
      public void cancelled() {
         DownloadConfigurationFileJob.this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         DownloadConfigurationFileJob.this.latch.countDown();
         ErrorLog.attention(DownloadConfigurationFileJob.class, "Failed to retrive etlforge.conf from the server: " + var1.getMessage(), var1);
      }
   }
}
