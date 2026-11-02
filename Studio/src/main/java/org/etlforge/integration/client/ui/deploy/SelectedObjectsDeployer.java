package org.etlforge.integration.client.ui.deploy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.deploy.DeployException;
import org.etlforge.integration.client.deploy.RequiredItemsMode;
import org.etlforge.integration.client.deploy.prefs.DeployRequiredItemsPreference;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.validation.DeployItemValidator;
import org.etlforge.integration.client.ui.deploy.validation.DeployItemValidatorFactory;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ProjectDependencies;

public final class SelectedObjectsDeployer {
   private final DeployWorker worker;
   private final IntegrationProject project;
   private final PreDeployProcessor preDeployProcessor;
   private final RequiredItemsMode requiredItemsMode;

   public SelectedObjectsDeployer(DeployWorker var1, IntegrationProject var2, PreDeployProcessor var3) {
      this.worker = var1;
      this.project = var2;
      this.preDeployProcessor = var3;
      this.requiredItemsMode = DeployRequiredItemsPreference.get();
   }

   public void deploy(List<? extends IntegrationEntity> var1, DeployCallback var2) {
      if (!this.project.isDeployable()) {
         var2.cancelled();
         ReadOnlyProjectMessage.show();
      } else if (this.preProcessItems(var1, var2)) {
         Set var3 = this.getItemsToDeploy(var1);
         if (var3.isEmpty()) {
            var2.cancelled();
         } else if (this.preProcessDependencies(var3, var1, var2)) {
            DeployItemValidator var4 = DeployItemValidatorFactory.newPromptOnWarningValidator();
            if (var4.validate(var3)) {
               this.submitForDeploy(var3, var2);
            } else {
               var2.caught(new Exception("Could not deploy due to invalid items."));
            }
         }
      }
   }

   private boolean preProcessDependencies(Set<? extends IntegrationEntity> var1, Collection<? extends IntegrationEntity> var2, DeployCallback var3) {
      HashSet var4 = Sets.newHashSet(var1);
      var4.removeAll(var2);
      return this.preProcessItems(var4, var3);
   }

   private boolean preProcessItems(Collection<? extends IntegrationEntity> var1, DeployCallback var2) {
      for (IntegrationEntity var4 : var1) {
         try {
            this.preDeployProcessor.process(var4);
         } catch (DeployException var6) {
            Alert.error("Nothing was deployed: " + var6.getMessage(), "Error");
            var2.caught(var6);
            return false;
         }
      }

      return true;
   }

   private Set<IntegrationEntity> getItemsToDeploy(List<? extends IntegrationEntity> var1) {
      DependencyStore var2 = this.project.getCurrentDependencies();

      try {
         HashSet var3 = Sets.newHashSet();
         HashSet var4 = Sets.newHashSet();

         for (IntegrationEntity var6 : var1) {
            this.addItemToDeploy(var6, var2, var3, var4, true);
         }

         Iterables.removeIf(var4, EntityFilters.BUILT_IN);
         return var4;
      } finally {
         var2.dispose();
      }
   }

   private void addItemToDeploy(IntegrationEntity var1, ProjectDependencies var2, Set<IntegrationEntity> var3, Set<IntegrationEntity> var4, boolean var5) {
      if (!var3.contains(var1)) {
         var4.add(var1);
         this.addDependendices(var2, var1, var3, var4);
         if (var1 instanceof Folder && var5) {
            for (IntegrationEntity var7 : var1.getAllChildren()) {
               this.addItemToDeploy(var7, var2, var3, var4, true);
            }
         }
      }
   }

   private void addDependendices(ProjectDependencies var1, IntegrationEntity var2, Set<IntegrationEntity> var3, Set<IntegrationEntity> var4) {
      if (var3.add(var2)) {
         for (IntegrationEntity var6 : var1.getRequiredObjects(var2)) {
            if (this.includeInDeploy(var6)) {
               this.addItemToDeploy(var6, var1, var3, var4, false);
            }
         }

         IntegrationEntity var7 = var2.getParent();
         if (var7 != null) {
            this.addItemToDeploy(var7, var1, var3, var4, false);
         }
      }
   }

   private void submitForDeploy(Set<IntegrationEntity> var1, DeployCallback var2) {
      SelectedObjectsDeployer.DeployJob var3 = new SelectedObjectsDeployer.DeployJob(this.worker, this.project, var2, Lists.newArrayList(var1));
      var3.run();
   }

   private boolean includeInDeploy(IntegrationEntity var1) {
      switch (this.requiredItemsMode) {
         case ALWAYS_DEPLOY:
            return true;
         case DEPLOY_IF_NECESSARY:
            return !var1.hasBeenDeployed() || var1.isDeployDirty();
         default:
            ErrorLog.log(this.getClass(), null, new Exception("Unhandled RequiredItemsMode: " + this.requiredItemsMode));
            return true;
      }
   }

   private static final class DeployJob {
      private final DeployWorker worker;
      private final IntegrationProject project;
      private final DeployCallback externalCallback;
      private final List<IntegrationEntity> itemsToDeploy;
      private IntegrationProject partialProject;

      public DeployJob(DeployWorker var1, IntegrationProject var2, DeployCallback var3, List<IntegrationEntity> var4) {
         this.worker = var1;
         this.project = var2;
         this.externalCallback = var3;
         this.itemsToDeploy = var4;
      }

      public void run() {
         this.partialProject = IntegrationProject.createPartialProject(this.project, this.itemsToDeploy);
         ManagedProject var1 = this.project.getExtensionObject(ManagedProject.class);
         DeployDataImpl var2 = new DeployDataImpl(var1, this.partialProject, true);
         this.worker.addCallback(new SelectedObjectsDeployer.DeployJob.CallbackImpl(var1));
         this.worker.deploy(var2, var1.getDeployHistory());
      }

      private final class CallbackImpl implements DeployCallback {
         private final ManagedProject managedProject;

         public CallbackImpl(ManagedProject var2) {
            this.managedProject = var2;
         }

         @Override
         public void deployComplete(DeployData var1, DeployResult var2) {
            SelectedObjectsDeployer.DeployJob.CompletedJob var3 = DeployJob.this.new CompletedJob(this.managedProject, var2);
            if (EventQueue.isDispatchThread()) {
               ApplicationWorker var4 = Application.getWorker();
               var4.submitForParallel(ApplicationWorker.GUARD.protect(var3));
            } else {
               var3.run();
            }

            DeployJob.this.externalCallback.deployComplete(var1, var2);
         }

         @Override
         public void cancelled() {
            DeployJob.this.externalCallback.cancelled();
         }

         @Override
         public void caught(Throwable var1) {
            ErrorLog.attention(SelectedObjectsDeployer.class, Strings.get("SelectedObjectsDeployer.Error"), var1);
            DeployJob.this.externalCallback.caught(var1);
         }
      }

      private final class CompletedJob implements Runnable {
         private final ManagedProject managedProject;
         private final DeployResult result;

         public CompletedJob(ManagedProject var2, DeployResult var3) {
            this.managedProject = var2;
            this.result = var3;
         }

         @Override
         public void run() {
            this.displayDeployResult();
            if (this.result.getCause() == null) {
               this.processProject();
            }
         }

         private void displayDeployResult() {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DeployResultDisplayer.display(CompletedJob.this.result, DeployJob.this.project);
               }
            });
         }

         private void processProject() {
            try {
               IntegrationProjectLock.acquire();
               this.managedProject.partialProjectWasDeployed(DeployJob.this.partialProject);
            } catch (InterchangeSavingException var5) {
               this.handleError(var5);
            } finally {
               IntegrationProjectLock.release();
            }
         }

         private void handleError(Exception var1) {
            ErrorLog.attention(SelectedObjectsDeployer.DeployJob.class, Strings.get("SelectedObjectsDeployer.PostDeployError"), var1);
            DeployJob.this.externalCallback.caught(var1);
         }
      }
   }
}
