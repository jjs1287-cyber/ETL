package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.util.List;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.wsdl.cache.WsdlListCache;
import org.etlforge.integration.client.wsdl.serverapi.GetWsdlFilesCallback;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public final class GetWsdlFilesJob extends UiJob {
   private final WsdlFileDisplayer fileDisplayer;
   private final boolean checkCacheFirst;
   private final GetWsdlFilesJob.GetWsdlFilesCallbackImpl callback;

   public GetWsdlFilesJob(WsdlFileDisplayer var1, boolean var2) {
      super(PackageResources.GetWsdlFilesJob.NAME);
      this.fileDisplayer = var1;
      this.checkCacheFirst = var2;
      this.callback = new GetWsdlFilesJob.GetWsdlFilesCallbackImpl();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      if (this.checkCacheFirst) {
         boolean var1 = this.checkCache();
         if (var1) {
            return;
         }
      }

      this.askServerForWsdls();
   }

   private boolean checkCache() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      ServerInfo var2 = var1.getServerInfo();
      if (var2 != null) {
         List var3 = WsdlListCache.restore(var2.getGuid());
         if (!var3.isEmpty()) {
            this.fileDisplayer.displayFiles(var2, var3);
            return true;
         }
      }

      return false;
   }

   private void askServerForWsdls() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      this.setMessage(PackageResources.GetWsdlFilesJob.STARTED);
      WsdlProvider var2 = var1.getServerCall(WsdlProvider.class);
      var2.getWsdlFiles(this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class GetWsdlFilesCallbackImpl extends AbstractServerCallback implements GetWsdlFilesCallback {
      private GetWsdlFilesCallbackImpl() {
      }

      @Override
      public void gotWsdlFiles(ServerInfo var1, List<WsdlFile> var2) {
         if (!GetWsdlFilesJob.this.isCancelled()) {
            GetWsdlFilesJob.this.setCanBeCancelled(false);
            GetWsdlFilesJob.this.setMayBeInterrupted(false);
            WsdlListCache.store(var1.getGuid(), var2);
            GetWsdlFilesJob.this.fileDisplayer.displayFiles(var1, var2);
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetWsdlFilesJob.this.isCancelled()) {
            GetWsdlFilesJob.this.setStatus(JobStatus.ERROR);
            GetWsdlFilesJob.this.setMessage(PackageResources.GetWsdlFilesJob.ERROR_STATUS);
            GetWsdlFilesJob.this.fileDisplayer.displayFiles(null, null);
            super.caught(var1);
         }
      }

      @Override
      public void cancelled() {
         super.cancelled();
         GetWsdlFilesJob.this.fileDisplayer.displayFiles(null, null);
      }
   }
}
