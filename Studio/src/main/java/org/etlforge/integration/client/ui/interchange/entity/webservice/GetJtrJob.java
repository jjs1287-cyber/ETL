package org.etlforge.integration.client.ui.interchange.entity.webservice;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.mapping.serverapi.webservice.GetWebServiceJtrCallback;
import org.etlforge.integration.client.mapping.serverapi.webservice.WebServiceInfoProvider;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.wait.NullWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class GetJtrJob extends UiJob {
   private final CallStructureRetriever retriever;
   private final WebServiceOperationInfo opInfo;
   private final GetJtrJob.Callback callback;
   private final WaitLock waitLock;

   public GetJtrJob(CallStructureRetriever var1, WebServiceOperationInfo var2, WaitService var3) {
      super("Constructing webservice structure trees");
      this.retriever = var1;
      this.opInfo = var2;
      this.callback = new GetJtrJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
      this.waitLock = var3 != null ? var3.startWait() : new NullWaitService().startWait();
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Requesting structures from server");
      IntegrationServer var1 = IntegrationServer.getInstance();
      WebServiceInfoProvider var2 = var1.getServerCall(WebServiceInfoProvider.class);
      var2.getInputAndOutputJTR(this.opInfo, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback implements GetWebServiceJtrCallback {
      private Callback() {
      }

      @Override
      public void jtrGenerated(WebServiceCallStructures var1) {
         if (!GetJtrJob.this.isCancelled()) {
            GetJtrJob.this.setCanBeCancelled(false);
            GetJtrJob.this.setMayBeInterrupted(false);
            GetJtrJob.this.retriever.onJTRsRetrieved(var1);
            GetJtrJob.this.waitLock.release();
         }
      }

      @Override
      public void cancelled() {
         GetJtrJob.this.waitLock.release();
      }

      @Override
      public void caught(final Throwable var1) {
         if (!GetJtrJob.this.isCancelled()) {
            GetJtrJob.this.setStatus(JobStatus.ERROR);
            GetJtrJob.this.setMessage(UiJobMessages.ERROR);
            GetJtrJob.this.waitLock.release();
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ErrorLog.attention(GetJtrJob.class, "Failed to create the request and response structures.", var1);
               }
            });
         }
      }
   }
}
