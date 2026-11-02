package org.etlforge.integration.client.project;

import org.etlforge.integration.client.project.deploy.DefaultPostDeployEntityProcessor;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.client.project.deploy.PostDeployEntityProcessor;
import org.etlforge.integration.client.project.prefs.BlankOutPasswordsOnDeployPreference;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

final class PostDeployProcessor {
   private final ManagedProjectImpl currentProject;
   private final IntegrationProject partialProject;
   private final boolean blankOutPasswords = BlankOutPasswordsOnDeployPreference.get();

   public static void processDeploy(ManagedProjectImpl var0) throws InterchangeSavingException {
      PostDeployProcessor var1 = new PostDeployProcessor(var0);
      var1.processDeploy();
   }

   public static void processPartialDeploy(ManagedProjectImpl var0, IntegrationProject var1) throws InterchangeSavingException {
      PostDeployProcessor var2 = new PostDeployProcessor(var0, var1);
      var2.processPartialDeploy();
   }

   private PostDeployProcessor(ManagedProjectImpl var1) {
      this.currentProject = var1;
      this.partialProject = null;
   }

   private PostDeployProcessor(ManagedProjectImpl var1, IntegrationProject var2) {
      this.currentProject = var1;
      this.partialProject = var2;
   }

   private void processDeploy() throws InterchangeSavingException {
      DeployLog.LOG.fine("Post-processing all deployed items");

      for (RootFolder var2 : this.currentProject.getProject().getAllRootFolders()) {
         this.postDeployProcessingImpl(var2);
      }

      this.updateProject();
      this.saveProjectToDisk();
      DeployLog.LOG.fine("processDeploy() completed successfully.");
   }

   private void postDeployProcessingImpl(IntegrationEntity var1) {
      PostDeployEntityProcessor var2 = var1.getExtensionObject(PostDeployEntityProcessor.class);
      if (var2 == null) {
         var2 = new DefaultPostDeployEntityProcessor(var1);
      }

      var2.doPostDeployProcessing(this.blankOutPasswords);

      for (IntegrationEntity var4 : var1.getAllChildren()) {
         this.postDeployProcessingImpl(var4);
      }
   }

   private void processPartialDeploy() throws InterchangeSavingException {
      DeployLog.LOG.fine("Post-processing all deployed items");

      for (RootFolder var2 : this.partialProject.getAllRootFolders()) {
         this.postPartialDeployProcessingImpl(var2);
      }

      DeployLog.LOG.fine("Saving all deployed items");
      ProjectPersistor var5 = this.currentProject.getProjectPersistor();

      for (IntegrationEntity var3 : this.partialProject.getAllEntities()) {
         IntegrationEntity var4 = this.currentProject.getProject().getItemLookup().getEntity(var3.getID());
         if (var4 != null) {
            var5.save(var4);
         }
      }

      this.updateProject();
      this.saveProjectFile();
      DeployLog.LOG.fine("processPartialDeploy() completed successfully.");
   }

   private void postPartialDeployProcessingImpl(IntegrationEntity var1) {
      IntegrationEntity var2 = this.currentProject.getProject().getItemLookup().getEntity(var1.getID());
      if (var2 != null) {
         PostDeployEntityProcessor var3 = var2.getExtensionObject(PostDeployEntityProcessor.class);
         if (var3 == null) {
            var3 = new DefaultPostDeployEntityProcessor(var2);
         }

         var3.doPostDeployProcessing(this.blankOutPasswords);
         var2.setDeployed(true);
         var2.setDeployDirty(false);
      }

      for (IntegrationEntity var4 : var1.getAllChildren()) {
         this.postPartialDeployProcessingImpl(var4);
      }
   }

   private void updateProject() {
      DeployLog.LOG.fine("Updating the project");
      IntegrationProject var1 = this.currentProject.getProject();
      PostDeployProcessor.DeployState var2 = new PostDeployProcessor.DeployState();
      if (var1.hasBeenDeployed()) {
         var2.setDeployed();
      }

      collectDeployState(var1, var2, false);
      var1.setDeployed(var2.isDeployed());
      var1.setDeployDirty(var2.isDirty());
   }

   private void saveProjectToDisk() throws InterchangeSavingException {
      DeployLog.LOG.fine("Saving the project to disk");
      this.currentProject.saveToDisk();
   }

   private void saveProjectFile() throws InterchangeSavingException {
      DeployLog.LOG.fine("Saving the project file");
      ProjectUtils.saveProjectFile(this.currentProject.getProject(), this.currentProject.getProjectFile());
   }

   private static void collectDeployState(IntegrationEntity var0, PostDeployProcessor.DeployState var1, boolean var2) {
      if (var2 && var0.isDeployDirty()) {
         var1.setDirty();
      }

      if (var0.hasBeenDeployed()) {
         var1.setDeployed();
      }

      if (!var1.isComplete()) {
         for (IntegrationEntity var4 : var0.getAllChildren()) {
            collectDeployState(var4, var1, true);
            if (var1.isComplete()) {
               return;
            }
         }
      }
   }

   private static final class DeployState {
      private boolean dirty;
      private boolean deployed;

      private DeployState() {
      }

      public void setDirty() {
         this.dirty = true;
      }

      public void setDeployed() {
         this.deployed = true;
      }

      public boolean isDirty() {
         return this.dirty;
      }

      public boolean isDeployed() {
         return this.deployed;
      }

      public boolean isComplete() {
         return this.dirty && this.deployed;
      }
   }
}
