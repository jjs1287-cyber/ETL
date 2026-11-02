package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.script.de.DefaultGlobalDataElements;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.version.UnsupportedVersionException;

final class ProjectLoaderWorker {
   private final ProjectManager manager;
   private final ImmutableSet<EntityType> supportedEntityTypes;
   private final ProjectDataLoaderFactory projectLoaderFactory;
   private final Logger logger;

   public ProjectLoaderWorker(ProjectManager var1, ImmutableSet<EntityType> var2, ProjectDataLoaderFactory var3, Logger var4) {
      this.manager = var1;
      this.supportedEntityTypes = var2;
      this.projectLoaderFactory = var3;
      this.logger = var4;
   }

   public ManagedProjectImpl loadProject(String var1, File var2, ProjectLoadingIssuesHandler var3, Supplier<ProjectLoadingTracker> var4) throws InterchangeLoadingException, UnsupportedVersionException {
      return this.loadProjectImpl(
         var1,
         var2,
         var3 != null ? var3 : ProjectLoadingIssuesHandler.IGNORE,
         var4 != null ? (ProjectLoadingTracker)var4.get() : new ProjectLoadingTrackerAdapter() {}
      );
   }

   ManagedProjectImpl loadProjectSilently(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      return this.loadProject(var1, var2, ProjectLoadingIssuesHandler.IGNORE, null);
   }

   private ManagedProjectImpl loadProjectImpl(String var1, File var2, ProjectLoadingIssuesHandler var3, ProjectLoadingTracker var4) throws InterchangeLoadingException, UnsupportedVersionException {
      this.logger.info("Loading the project " + var1 + " from " + var2.getAbsolutePath());
      checkPreReqs(var1, var2);
      var4.beginLoadingProject();
      ProjectLoaderWorker.ProjectAndItsFile var5 = this.loadProjectFileAndCreateEmptyProject(var1, var2);
      ManagedProjectImpl var6 = new ManagedProjectImpl(this.manager, var5.project, var5.projectFile, var2);
      this.readProjectData(var4, var6);
      SourceAndTargetFolders.prepareExistingProject(var5.project);
      return var6;
   }

   private ProjectLoaderWorker.ProjectAndItsFile loadProjectFileAndCreateEmptyProject(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      ProjectFile var3 = this.loadProjectFile(var1, var2);
      IntegrationProjectId var4 = var3.getProjectID();
      if (var4 == null) {
         throw new InterchangeLoadingException("There is a problem with the project: the project ID is missing. Please contact ETLForge support.");
      }

      IntegrationProject var5 = IntegrationProject.createExistingProjectShell(var1, var4, this.supportedEntityTypes);
      var5.setGlobalDataElements(new DefaultGlobalDataElements());
      var3.applyToLoadedProject(var5);
      var3.setProject(var5);
      return new ProjectLoaderWorker.ProjectAndItsFile(var5, var3);
   }

   private void readProjectData(ProjectLoadingTracker var1, ManagedProjectImpl var2) throws InterchangeLoadingException, UnsupportedVersionException {
      SystemObjects.populate(var2.getProject());
      ProjectDataLoader var3 = this.projectLoaderFactory.newLoader(var2.getProject(), var2.getProjectFile());
      var3.loadProject(var1);
      var2.projectWasLoaded();
      this.loadDeletedItems(var3, var2);
      this.loadPluginInfo(var2, var3, var1);
      this.loadUiState(var3, var2, var1);
      var1.doneLoadingProject();
   }

   private ProjectFile loadProjectFile(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      try {
         ProjectFile var3 = ProjectFile.createForExistingProject(var1, var2, true);
         ProjectVersionCompatibilityChecker.checkProjectVersion(var1, var3);
         return var3;
      } catch (FileNotFoundException var4) {
         throw new InterchangeLoadingException(var4);
      } catch (IOException var5) {
         throw new InterchangeLoadingException(var5);
      }
   }

   private void loadPluginInfo(ManagedProjectImpl var1, ProjectDataLoader var2, ProjectLoadingTracker var3) throws InterchangeLoadingException {
      assert var3 != null;

      try {
         var3.beginLoadingPluginInfo();
         ProjectPipelinePluginStore var4 = var2.loadPluginInfo();
         var1.setPlugins(var4);
      } finally {
         var3.doneLoadingPluginInfo();
      }
   }

   private void loadDeletedItems(ProjectDataLoader var1, final ManagedProjectImpl var2) throws InterchangeLoadingException {
      var1.loadDeletedItems(new Receiver<EntityDescriptor>() {
         public void handle(EntityDescriptor var1) {
            var2.markEntityAsDeleted(var1);
         }
      });
   }

   private void loadUiState(ProjectDataLoader var1, ManagedProjectImpl var2, ProjectLoadingTracker var3) {
      assert var3 != null;
      var3.beginLoadingUiState();
      var1.loadUiData();
      var2.getUiState().initialize(var1.getEntitiesToDisplayOnStartUp(), var1.getActiveEntityOnStartUp(), var1.getExpandedOnStartUp());
      var3.doneLoadingUiState();
   }

   private static void checkPreReqs(String var0, File var1) {
      IntegrationProjectLock.ensureLockHeld();
      Preconditions.checkNotNull(var0, "name");
      Preconditions.checkNotNull(var1, "rootDirectory");
   }

   public IntegrationProject loadProjectDataForInspection(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      ProjectLoaderWorker.ProjectAndItsFile var3 = this.loadProjectFileAndCreateEmptyProject(var1, var2);
      SystemObjects.populate(var3.project);
      ProjectDataLoader var4 = this.projectLoaderFactory.newLoader(var3.project, var3.projectFile);
      var4.loadProject(new ProjectLoadingTrackerAdapter() {});
      return var3.project;
   }

   private static class ProjectAndItsFile {
      public final IntegrationProject project;
      public final ProjectFile projectFile;

      public ProjectAndItsFile(IntegrationProject var1, ProjectFile var2) {
         this.project = var1;
         this.projectFile = var2;
      }
   }
}
