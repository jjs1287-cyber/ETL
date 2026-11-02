package org.etlforge.integration.client.project.copy;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.InterchangeLoadingException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.util.version.UnsupportedVersionException;

public final class ProjectCopier {
   private final ProjectManager projectManager;
   private ProjectLocation fromLoc;
   private ProjectLocation toLoc;
   private IntegrationProject project;

   public ProjectCopier(ProjectManager var1) {
      this.projectManager = var1;
   }

   public void copy(ProjectLocation var1, ProjectLocation var2) throws Exception {
      this.fromLoc = var1;
      this.toLoc = var2;
      IntegrationProjectLock.acquire();

      try {
         this.generateNewGUIDs();
         this.saveToDisk();
         this.copyToDestinationFolder();
      } finally {
         this.doCleanup();
      }
   }

   private void generateNewGUIDs() throws InterchangeLoadingException, ProjectLoadingCancelledException, UnsupportedVersionException {
      this.project = this.projectManager.loadProject(this.fromLoc.getProjectName(), this.fromLoc.getLocation(), ProjectLoadingIssuesHandler.IGNORE);
      this.project.flagAsTemporaryProject();
      this.updateReferences();
      this.createNewFolderIDs();
      this.createNewProjectID();
   }

   private void updateReferences() {
      EntityResources var1 = ProjectUtils.getProjectResources(this.project);
      DeepEntityCopyMachine var2 = new DeepEntityCopyMachine(new EntityCopyMachine(), var1, this.project.getItemLookup());
      DeepCopyResult var3 = var2.cloneProjectWithoutSaving(this.project);
      var3.replaceOriginalItems();
   }

   private void createNewFolderIDs() {
      for (Folder var2 : this.project.getAllEntitiesOfClass(Folder.class)) {
         var2.generateNewId();
      }
   }

   private void createNewProjectID() {
      this.project.generateNewId();
   }

   private void saveToDisk() throws InterchangeSavingException {
      ManagedProject var1 = this.projectManager.getProject(this.project);
      var1.emptyTrashCan();
      var1.eraseDeletedItemsHistory();
      var1.saveToDisk();
   }

   private void copyToDestinationFolder() throws IOException {
      if (this.toLoc.getLocation().exists()) {
         FileUtils.cleanDirectory(this.toLoc.getLocation());
      }

      FileUtils.copyDirectory(this.fromLoc.getLocation(), this.toLoc.getLocation());
   }

   private void doCleanup() {
      try {
         this.closeProject();
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private void closeProject() {
      if (this.project != null) {
         try {
            ManagedProject var1 = this.project.getExtensionObject(ManagedProject.class);
            this.projectManager.closeProject(var1);
         } catch (CloseProjectVetoException var2) {
            throw new RuntimeException("This should not have happened. This is a bug.", var2);
         }
      }
   }
}
