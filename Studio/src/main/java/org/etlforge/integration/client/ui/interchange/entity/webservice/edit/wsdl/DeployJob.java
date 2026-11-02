package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.data.entity.WebServiceCall;

final class DeployJob extends UiJob {
   private final DeployFacade deployFacade;
   private final List<WebServiceCall> wsCalls;
   private final DeployJob.Callback callback = new DeployJob.Callback();

   public DeployJob(DeployFacade var1, List<WebServiceCall> var2) {
      super(Strings.get("EditWsdl.DeployJob.Name"));
      this.deployFacade = var1;
      this.wsCalls = var2;
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   public boolean awaitResult() {
      try {
         Boolean var1 = this.callback.awaitResult().or(Boolean.FALSE);
         return var1;
      } catch (Exception var2) {
         ErrorLog.attention(this.getClass(), null, var2);
         return false;
      }
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.deployFacade.deploy(this.wsCalls, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends ResultProducingServerCallback<Boolean> implements DeployCallback {
      private Callback() {
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         boolean var3 = var2.getCause() == null;
         this.set(var3);
      }
   }
}
