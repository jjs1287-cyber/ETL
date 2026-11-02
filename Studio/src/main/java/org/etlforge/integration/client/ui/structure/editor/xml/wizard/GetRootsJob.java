package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.mapping.serverapi.xml.GetRootNamesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.structure.editor.AbstractWizardServerCallback;
import org.etlforge.integration.filetransfer.client.ServerFile;

final class GetRootsJob extends UiJob {
   private final XmlStructureWizardStartPage page;
   private final ServerFile file;
   private final CountDownLatch latch;
   private final GetRootNamesCallback callback;

   public GetRootsJob(XmlStructureWizardStartPage var1, ServerFile var2) {
      super(PackageResources.GetRootsJob.NAME);
      this.page = var1;
      this.file = var2;
      this.latch = new CountDownLatch(1);
      this.callback = new GetRootsJob.GetRootsCallback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PackageResources.GetRootsJob.STARTED);
      IntegrationServer var1 = IntegrationServer.getInstance();
      XmlInfoProvider var2 = var1.getServerCall(XmlInfoProvider.class);
      var2.getPossibleRootNames(this.file, this.callback);
      this.latch.await();
   }

   @Override
   protected void onJobCancelled() {
      this.page.stopWait();
      this.callback.cancelled();
   }

   private final class GetRootsCallback extends AbstractWizardServerCallback implements GetRootNamesCallback {
      public GetRootsCallback() {
         super(GetRootsJob.this.page, GetRootsJob.this.latch);
         this.setErrorMessage(PackageResources.GetRootsJob.ERROR_MESSAGE);
      }

      @Override
      public void rootNamesRetrieved(Collection<String> var1) {
         if (!GetRootsJob.this.isCancelled()) {
            GetRootsJob.this.setCanBeCancelled(false);
            GetRootsJob.this.setMayBeInterrupted(false);
            GetRootsJob.this.setMessage(UiJobMessages.UPDATE_UI);
            GetRootsJob.this.page.updateRootCache(var1);
            GetRootsJob.this.page.stopWait();
            this.countDown();
         }
      }

      @Override
      public void cancelled() {
         super.cancelled();
         GetRootsJob.this.page.stopWait();
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetRootsJob.this.isCancelled()) {
            GetRootsJob.this.setStatus(JobStatus.ERROR);
            GetRootsJob.this.setMessage(UiJobMessages.ERROR);
            GetRootsJob.this.page.stopWait();
            super.caught(var1);
         }
      }
   }
}
