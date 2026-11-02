package org.etlforge.integration.client.ui.interchange.deploy;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.deploy.DeployChecker;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.deploy.DeployPrompt;
import org.etlforge.integration.client.deploy.StrictDeployChecker;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.AbstractDeployPromptCallback;
import org.etlforge.integration.client.ui.deploy.DeployWorker;
import org.etlforge.integration.client.ui.deploy.DialogDeployPrompt;
import org.etlforge.integration.client.ui.deploy.PreDeployProcessor;
import org.etlforge.integration.client.ui.deploy.PreferenceCheckingDeployPrompt;
import org.etlforge.integration.client.ui.deploy.ReadOnlyProjectDeployPrompt;
import org.etlforge.integration.client.ui.deploy.SelectedObjectsDeployer;
import org.etlforge.integration.client.ui.deploy.prefs.AlwaysDeployDirtyItemsWhenTestingPreference;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class InterchangeDeployFacade implements DeployFacade {
   private final CurrentProjectService currentProjectService;
   private final DeployWorker deployWorker;
   private final PreDeployProcessor preDeployProcessor;

   public InterchangeDeployFacade(CurrentProjectService var1, DeployWorker var2, PreDeployProcessor var3) {
      this.currentProjectService = var1;
      this.deployWorker = var2;
      this.preDeployProcessor = var3;
   }

   @Override
   public DeployGate newDeployGate() {
      DependencyStore var1 = this.getDependencies();
      return new DeployGateImpl(this, var1);
   }

   private DependencyStore getDependencies() {
      ManagedProject var1 = this.currentProjectService.getManagedProject();
      return var1.getDependencyStore();
   }

   @Override
   public void deploy(Iterable<? extends IntegrationEntity> var1, DeployCallback var2) {
      SelectedObjectsDeployer var3 = this.createDeployer();
      var3.deploy(Lists.newArrayList(var1), var2);
   }

   @Override
   public DeployResult deploy(IntegrationEntity var1) {
      return this.deploy(Collections.singletonList(var1));
   }

   @Override
   public DeployResult deploy(Iterable<? extends IntegrationEntity> var1) {
      if (EventQueue.isDispatchThread()) {
         throw new RuntimeException("Cannot call this method from the event-dispatching thread.");
      }

      InterchangeDeployFacade.BlockingDeployCallback var2 = new InterchangeDeployFacade.BlockingDeployCallback();
      this.deploy(var1, var2);
      return var2.waitUntilDone();
   }

   @Override
   public void promptDeploy(DeployChecker var1, Runnable var2) {
      SelectedObjectsDeployer var3 = this.createDeployer();
      DeployPrompt var4 = this.createPrompt();
      InterchangeDeployFacade.DeployPromptCallbackImpl var5 = new InterchangeDeployFacade.DeployPromptCallbackImpl(var3, var2);
      var1.promptDeploy(var4, var5);
   }

   private DeployPrompt createPrompt() {
      IntegrationProject var1 = this.currentProjectService.getProject();
      if (var1.isDeployable()) {
         DialogDeployPrompt var2 = new DialogDeployPrompt();
         BooleanPreference var3 = AlwaysDeployDirtyItemsWhenTestingPreference.INSTANCE;
         var2.setAutomaticDeployPreference(var3);
         return new PreferenceCheckingDeployPrompt(var2, var3);
      } else {
         return new ReadOnlyProjectDeployPrompt();
      }
   }

   private SelectedObjectsDeployer createDeployer() {
      return new SelectedObjectsDeployer(this.deployWorker, this.currentProjectService.getProject(), this.preDeployProcessor);
   }

   DeployChecker createDefaultDeployChecker() {
      DependencyStore var1 = this.getDependencies();
      return new StrictDeployChecker(var1);
   }

   private static final class BlockingDeployCallback implements DeployCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      private DeployResult result;

      private BlockingDeployCallback() {
      }

      public DeployResult waitUntilDone() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
            var2.printStackTrace();
            Thread.currentThread().interrupt();
         }

         return this.result;
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         this.result = var2;
         this.unblock();
      }

      @Override
      public void cancelled() {
         this.unblock();
      }

      @Override
      public void caught(Throwable var1) {
         this.unblock();
      }

      private void unblock() {
         this.latch.countDown();
      }
   }

   private static final class DeployPromptCallbackImpl extends AbstractDeployPromptCallback {
      private final Runnable runOnSuccess;

      public DeployPromptCallbackImpl(SelectedObjectsDeployer var1, Runnable var2) {
         super(var1);
         this.runOnSuccess = var2;
      }

      @Override
      public void deployDeclined() {
         this.runOnSuccess.run();
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         if (var2.getCause() == null) {
            this.runOnSuccess.run();
         }
      }
   }
}
