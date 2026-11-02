package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.NullDeployHistory;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DefaultDeployLauncher implements DeployLauncher {
   private final DeployWorker worker;
   private final IntegrationProject project;
   private DeployHistory history;

   public DefaultDeployLauncher(DeployWorker var1, IntegrationProject var2) {
      Preconditions.checkNotNull(var1, "worker");
      Preconditions.checkNotNull(var2, "project");
      this.worker = var1;
      this.project = var2;
      this.history = new NullDeployHistory();
   }

   public void setHistory(DeployHistory var1) {
      this.history = var1 != null ? var1 : new NullDeployHistory();
   }

   @Override
   public void startDeploy(DeployData var1) {
      this.worker.addCallback(new DefaultDeployLauncher.CallbackImpl(this.project, var1));
      this.worker.deploy(var1, this.history);
   }

   private static final class CallbackImpl implements DeployCallback {
      private final IntegrationProject project;
      private final DeployData deployData;

      public CallbackImpl(IntegrationProject var1, DeployData var2) {
         this.project = var1;
         this.deployData = var2;
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         DefaultDeployLauncher.CompletedJob var3 = new DefaultDeployLauncher.CompletedJob(this.project, this.deployData, var2);
         if (EventQueue.isDispatchThread()) {
            ApplicationWorker var4 = Application.getWorker();
            var4.submitForParallel(ApplicationWorker.GUARD.protect(var3));
         } else {
            var3.run();
         }
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DefaultDeployLauncher.class, Strings.get("Launcher.Error"), var1);
      }

      @Override
      public void cancelled() {
      }
   }

   private static final class CompletedJob implements Runnable {
      private final IntegrationProject project;
      private final DeployData deployData;
      private final DeployResult result;

      public CompletedJob(IntegrationProject var1, DeployData var2, DeployResult var3) {
         this.project = var1;
         this.deployData = var2;
         this.result = var3;
      }

      @Override
      public void run() {
         this.displayResult();
         this.updateProject();
      }

      private void displayResult() {
         DeployLog.LOG.fine("Displaying the deploy result");
         ApplicationWorker var1 = Application.getWorker();
         var1.submitForParallel(new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               DeployResultDisplayer.display(CompletedJob.this.result, CompletedJob.this.project);
            }
         });
      }

      private void updateProject() {
         DeployLog.LOG.fine("Updating the project data");
         ManagedProject var1 = this.project.getExtensionObject(ManagedProject.class);
         PostDeployProjectUpdater var2 = new PostDeployProjectUpdater(var1, this.deployData);
         var2.handle(this.result);
      }
   }
}
