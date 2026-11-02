package org.etlforge.integration.client.ui.deploy;

import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.client.project.deploy.DeployManager;

public final class DeployJob extends UiJob implements DeployCallback {
   private final DeployManager deployManager;
   private final DeployData data;
   private final DeployCallback externalCallback;

   public DeployJob(DeployManager var1, DeployData var2, DeployCallback var3) {
      super(Strings.format("Deploy.Job.Name", var2.getProject().getName()));
      this.deployManager = var1;
      this.data = var2;
      this.externalCallback = var3;
      this.setUncaughtExceptionHandler(this);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      DeployLog.LOG.info("DeployJob started");
      this.setMessage(Strings.get("Deploy.Job.Started"));
      try {
         if (this.data instanceof DeployDataImpl && !org.etlforge.integration.client.ui.project.sourcecontrol.SourceControlUi.commit(((DeployDataImpl)this.data).getManagedProject(), true)) {
            this.cancelled(); return;
         }
      } catch (Exception ex) { this.caught(ex); return; }
      this.deployManager.deploy(this.data, this);
      DeployLog.LOG.info("DeployJob completed");
   }

   @Override
   protected void onJobCancelled() {
      this.cancelled();
   }

   @Override
   public void deployComplete(DeployData var1, DeployResult var2) {
      DeployJob.MessageFactory var3 = new DeployJob.MessageFactory();
      String var4 = var3.visit(var2);
      DeployLog.LOG.info("DeployJob completed. The result of deploy was: " + var4 + ". Result ID = " + var2.getId());
      if (!this.isCancelled()) {
         this.setMessage(Strings.get("Deploy.Job.UpdatingClient"));
         this.externalCallback.deployComplete(var1, var2);
      }
   }

   @Override
   public void cancelled() {
      DeployLog.LOG.info("Cancel request received");
      this.externalCallback.cancelled();
   }

   @Override
   public void caught(Throwable var1) {
      if (!this.isCancelled()) {
         DeployLog.LOG.info("DeployJob failed: " + var1);
         this.setStatus(JobStatus.ERROR);
         this.externalCallback.caught(var1);
      }
   }

   private static class MessageFactory extends DeployResultVisitor<String> {
      private MessageFactory() {
      }

      protected String onFailure(DeployResult var1) {
         return "deploy failed";
      }

      protected String onWarnings(DeployResult var1) {
         return "deploy completed with non-fatal warnings";
      }

      protected String onSuccess(DeployResult var1) {
         return "success";
      }
   }
}
