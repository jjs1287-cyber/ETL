package com.etlforge.integration.client.deploy;

import com.etlforge.integration.client.deploy.api.ServerDeployCheckCallback;
import com.etlforge.integration.client.deploy.api.ServerDeployCheckResult;
import com.etlforge.integration.client.deploy.api.ServerDeployChecker;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.system.callback.Receiver;

public final class RunServerDeployCheckJob extends ResultProducingUiJob<ServerDeployCheckResult> {
   private final DeployData data;
   private final RunServerDeployCheckJob.Callback callback;

   public RunServerDeployCheckJob(DeployData var1, Receiver<ServerDeployCheckResult> var2) {
      super("Run Pre-Deploy Check On The Server");
      this.data = var1;
      this.callback = new RunServerDeployCheckJob.Callback();
      this.setResultReceiver(var2);
      this.setIgnoreNullResult(false);
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Sending deploy information to the server");
      ServerDeployChecker var1 = IntegrationServer.getInstance().getServerCall(ServerDeployChecker.class);
      var1.checkProjectForDeploy(this.data.getProject(), this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback implements ServerDeployCheckCallback {
      private Callback() {
      }

      @Override
      public void deployCheckComplete(ServerDeployCheckResult var1) {
         RunServerDeployCheckJob.this.setMessage("Handling the returned result");
         RunServerDeployCheckJob.this.resultWasComputed(var1);
      }

      @Override
      public void cancelled() {
         RunServerDeployCheckJob.this.resultWasComputed(null);
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.log(RunServerDeployCheckJob.class, "An error was reported from the pre-deploy processing on the server side: " + var1.getMessage(), var1);
         RunServerDeployCheckJob.this.resultWasComputed(ServerDeployCheckResult.EMPTY);
      }
   }
}
