package org.etlforge.integration.client.database.ui.drivers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.database.serverapi.OdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

final class GetOdbcDbDriversJob extends UiJob {
   private final OdbcDriverListCallback callback = new GetOdbcDbDriversJob.Callback();
   private final CountDownLatch latch = new CountDownLatch(1);
   private boolean succeeded;
   private ServerInfo serverInfo;
   private String[] downloadedDriverNames;

   public GetOdbcDbDriversJob() {
      super(DatabaseUiResources.getString("GetDriversJob.Name"));
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   public boolean succeeded() {
      return this.succeeded;
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   public String[] getDownloadedDriverNames() {
      return this.downloadedDriverNames;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Downloading list of drivers from the server");
      IntegrationServer var1 = IntegrationServer.getInstance();
      OdbcInfoProvider var2 = var1.getServerCall(OdbcInfoProvider.class);
      var2.getDrivers(this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends AbstractServerCallback implements OdbcDriverListCallback {
      public Callback() {
         this.setErrorTitle(DatabaseUiResources.getString("GetDriversJob.ErrorMessage"));
      }

      @Override
      public void driversRetrieved(List<OdbcDriverDescriptor> var1, ServerInfo var2) {
         if (!GetOdbcDbDriversJob.this.isCancelled()) {
            GetOdbcDbDriversJob.this.setCanBeCancelled(false);
            GetOdbcDbDriversJob.this.setMayBeInterrupted(false);
            GetOdbcDbDriversJob.this.setMessage(DatabaseUiResources.getString("GetDriversJob.UpdateClient"));
            GetOdbcDbDriversJob.this.succeeded = true;
            GetOdbcDbDriversJob.this.serverInfo = var2;
            int var3 = var1.size();
            GetOdbcDbDriversJob.this.downloadedDriverNames = new String[var3];

            for (int var4 = 0; var4 < var3; var4++) {
               GetOdbcDbDriversJob.this.downloadedDriverNames[var4] = ((OdbcDriverDescriptor)var1.get(var4)).getName();
            }

            GetOdbcDbDriversJob.this.latch.countDown();
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetOdbcDbDriversJob.this.isCancelled()) {
            GetOdbcDbDriversJob.this.setStatus(JobStatus.ERROR);
            GetOdbcDbDriversJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
            GetOdbcDbDriversJob.this.latch.countDown();
         }
      }

      @Override
      public void cancelled() {
         super.cancelled();
         GetOdbcDbDriversJob.this.latch.countDown();
      }
   }
}
