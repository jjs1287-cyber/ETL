package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.project.backup.ProjectBackupService;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.client.project.event.ProjectManagerListeners;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.script.de.DefaultGlobalDataElements;
import org.etlforge.util.version.UnsupportedVersionException;

public final class ProjectManager {
   private final Map<IntegrationProject, ManagedProjectImpl> projects = Collections.synchronizedMap(new IdentityHashMap<>());
   private final ProjectFileLock fileLock = new ProjectFileLock();
   private final ProjectManagerListeners listeners = new ProjectManagerListeners();
   private final List<CloseProjectRequestListener> closeProjectRequestListeners = Lists.newCopyOnWriteArrayList();
   private final ImmutableSet<EntityType> supportedEntityTypes;
   private final RecentProjects recentProjects;
   private ManagedProjectImpl currentProject;
   private final ProjectLoaderWorker projectLoader;
   private Supplier<ProjectLoadingTracker> projectLoadingTrackerProvider;
   private final ProjectVersionHandlerManager versionHandlerManager;
   private final ProjectEventLogger projectEventLogger;
   private final ProjectBackupService backupService;
   private final ProjectDirectory projectDirectory;
   private final Exporter exporter;
   private volatile UpgradeStrategy upgradeStrategy = UpgradeStrategy.AUTOMATIC;
   private final Object projectLock = new Object();

   public ProjectManager(
      ProjectDirectory var1, Iterable<EntityType> var2, ProjectDataLoaderFactory var3, RecentProjects var4, ProjectBackupService var5, Exporter var6
   ) {
      this.backupService = var5;
      this.projectDirectory = var1;
      this.supportedEntityTypes = collectSupportedEntityTypes(var2);
      this.exporter = var6;
      this.projectLoader = new ProjectLoaderWorker(this, this.supportedEntityTypes, var3, ProjectDataLoaderLog.LOG);
      this.projectEventLogger = new ProjectEventLoggerImpl(this);
      this.versionHandlerManager = new ProjectVersionHandlerManager();
      this.recentProjects = var4;
   }

   private static ImmutableSet<EntityType> collectSupportedEntityTypes(Iterable<EntityType> var0) {
      return ImmutableSet.<EntityType>builder().addAll(var0).add(EntityType.Project, EntityType.RootFolder, EntityType.Folder).build();
   }

   public void setUpgradeStrategy(UpgradeStrategy var1) {
      this.upgradeStrategy = Preconditions.checkNotNull(var1, "s");
   }

   public ImmutableSet<EntityType> getSupportedEntityTypes() {
      return this.supportedEntityTypes;
   }

   public ManagedProject getCurrentProject() {
      synchronized (this.projectLock) {
         return this.currentProject;
      }
   }

   public ManagedProject getProject(IntegrationEntity var1) {
      return this.lookupManagedProject(ProjectUtils.getProject(var1));
   }

   public ProjectDirectory getProjectDirectory() {
      return this.projectDirectory;
   }

   public ProjectEventLogger getProjectEventLogger() {
      return this.projectEventLogger;
   }

   public String getCurrentProjectName() {
      synchronized (this.projectLock) {
         return this.currentProject == null ? null : this.currentProject.getProject().getName();
      }
   }

   public File getCurrentProjectLocation() {
      synchronized (this.projectLock) {
         return this.currentProject == null ? null : this.currentProject.getLocation();
      }
   }

   public ManagedProject setCurrentProject(IntegrationProject var1) {
      Preconditions.checkNotNull(var1, "project");
      ManagedProjectImpl var2;
      synchronized (this.projectLock) {
         Preconditions.checkState(this.currentProject == null, "Another project is still open.");
         var2 = this.projects.get(var1);
         Preconditions.checkState(var2 != null, "The ProjectManager was requested to open a project it does not manage.");
         this.currentProject = var2;
      }

      this.fireProjectOpened(var1);
      ProjectLocation var6 = new ProjectLocation(var1.getName(), var2.getLocation());
      this.recentProjects.addProjectLocation(var6);
      return var2;
   }

   public void closeCurrentProject() throws CloseProjectVetoException {
      ManagedProject var1 = this.getCurrentProject();
      if (var1 != null) {
         this.closeProject(var1);
      }
   }

   public void closeProject(ManagedProject var1) throws CloseProjectVetoException {
      ManagedProject var2 = this.getCurrentProject();
      if (var1 == var2) {
         this.askPermissionToClose(var1);
      }

      synchronized (this.projectLock) {
         this.closeProjectImpl(var1);
      }
   }

   private void closeProjectImpl(ManagedProject var1) {
      boolean var2 = false;

      try {
         ((ManagedProjectImpl)var1).close();
         if (var1 == this.currentProject) {
            this.currentProject = null;
            var2 = true;
         }
      } finally {
         this.projects.remove(var1.getProject());
         this.fileLock.release(var1);
      }

      if (var2) {
         this.sendProjectClosedEvent(var1);
      }
   }

   private void sendProjectClosedEvent(ManagedProject var1) {
      if (!var1.isReadOnly()) {
         this.fireProjectClosed(var1.getProject());
      }
   }

   private void askPermissionToClose(ManagedProject var1) throws CloseProjectVetoException {
      if (!var1.isReadOnly()) {
         for (CloseProjectRequestListener var3 : this.closeProjectRequestListeners) {
            var3.closeProjectRequested(var1.getProject());
         }
      }
   }

   public void saveProject(IntegrationProject var1) throws InterchangeSavingException {
      ManagedProjectImpl var2 = this.lookupManagedProject(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unknown project: " + var1);
      }

      var2.saveToDisk();
   }

   private ManagedProjectImpl lookupManagedProject(IntegrationProject var1) {
      synchronized (this.projectLock) {
         return this.projects.get(var1);
      }
   }

   public ManagedProject inspect(String var1, File var2) throws InterchangeLoadingException, ProjectLoadingCancelledException, UnsupportedVersionException {
      ProjectInspector var3 = new ProjectInspector(this.projectLoader, this.versionHandlerManager);
      ManagedProjectImpl var4 = var3.inspect(var1, var2);
      this.addProjectForManagement(var4);
      return var4;
   }

   public IntegrationProject inspectProjectData(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      ProjectInspector var3 = new ProjectInspector(this.projectLoader, this.versionHandlerManager);
      return var3.inspectProjectData(var1, var2);
   }

   public IntegrationProject loadProject(String var1, File var2, ProjectLoadingIssuesHandler var3) throws InterchangeLoadingException, ProjectLoadingCancelledException, UnsupportedVersionException {
      this.acquireFileLock(var1, var2);
      boolean var4 = false;

      try {
         ProjectVersionHandlerManager.Job var5 = this.versionHandlerManager.getJob(var1, var2);
         if (!var5.preLoading(this.upgradeStrategy)) {
            throw new ProjectLoadingCancelledException();
         }

         ManagedProjectImpl var6 = this.projectLoader.loadProject(var1, var2, var3, this.projectLoadingTrackerProvider);
         var5.postLoading(var6);
         this.addProjectForManagement(var6);
         var4 = true;
         return var6.getProject();
      } finally {
         if (!var4) {
            this.fileLock.release(var2);
         }
      }
   }

   private void acquireFileLock(String var1, File var2) throws InterchangeLoadingException {
      if (!this.fileLock.acquire(var2) && !this.isCurrentProjectReopened(var1, var2)) {
         throw new InterchangeLoadingException("This project is already open for editing, perhaps by another client application.");
      }
   }

   public ManagedProject createManagedProject(IntegrationProject var1, File var2) throws InterchangeSavingException {
      ProjectFile var3 = ProjectFile.createForNewProject(var1, var2);
      ManagedProjectImpl var4 = new ManagedProjectImpl(this, var1, var3, var2);
      var1.refreshItemLookup();
      var4.buildDataStore();
      var4.buildDependencies();
      var4.saveToDisk();
      return var4;
   }

   private boolean isCurrentProjectReopened(String var1, File var2) {
      ManagedProject var3 = this.getCurrentProject();
      return var3 != null && var3.getProject().getName().equals(var1) && var3.getLocation().equals(var2);
   }

   public JitterPack createJitterPack(String var1, File var2, File var3, boolean var4) throws InterchangeLoadingException, UnsupportedVersionException, JitterPackException {
      JitterPackCreator var5 = new JitterPackCreator(this.projectLoader, this.exporter);
      return var5.createJitterPack(var1, var2, var3, var4);
   }

   public ProjectBackupService getBackupService() {
      return this.backupService;
   }

   public IntegrationProject createNewProject(String var1, File var2) throws InterchangeSavingException {
      IntegrationProjectLock.ensureLockHeld();
      Preconditions.checkArgument(var1.length() > 0, "name cannot be empty");
      Preconditions.checkNotNull(var2, "rootDirectory");
      ProjectUtils.prepareRootDirectory(var2);
      IntegrationProject var3 = IntegrationProject.createNewProject(var1, this.supportedEntityTypes);
      SourceAndTargetFolders.prepareNewProject(var3);
      var3.setGlobalDataElements(new DefaultGlobalDataElements());
      var3.setLocationOnDisk(var2);
      ProjectFile var4 = ProjectFile.createForNewProject(var3, var2);
      var4.setCreationTimestamp(System.currentTimeMillis());
      var4.update(var3);
      SystemObjects.populate(var3);
      ManagedProjectImpl var5 = new ManagedProjectImpl(this, var3, var4, var2);
      var5.projectWasLoaded();
      this.addProjectForManagement(var5);
      var5.saveToDisk();
      return var3;
   }

   public IntegrationProject unpackProject(String var1, File var2, ProjectLoadingIssuesHandler var3, boolean var4) throws InterchangeLoadingException, ProjectLoadingCancelledException, InterchangeSavingException, UnsupportedVersionException {
      IntegrationProjectLock.acquire();

      try {
         IntegrationProject var5 = this.loadProject(var1, var2, var3);
         var5.setDeployable(!var4);
         ManagedProjectImpl var6 = this.lookupManagedProject(var5);
         ProjectUtils.saveProjectFile(var5, var6.getProjectFile());
         return var5;
      } finally {
         IntegrationProjectLock.release();
      }
   }

   public void addProjectManagerListener(ProjectManagerListener var1) {
      this.listeners.add(var1);
   }

   public void removeProjectManagerListener(ProjectManagerListener var1) {
      this.listeners.remove(var1);
   }

   public void addCloseProjectRequestListener(CloseProjectRequestListener var1) {
      this.closeProjectRequestListeners.add(var1);
   }

   public void removeCloseProjectRequestListener(CloseProjectRequestListener var1) {
      this.closeProjectRequestListeners.remove(var1);
   }

   private void fireProjectOpened(IntegrationProject var1) {
      ProjectManagerEvent var2 = new ProjectManagerEvent(this, var1);
      this.listeners.call().projectOpened(var2);
   }

   private void fireProjectClosed(IntegrationProject var1) {
      ProjectManagerEvent var2 = new ProjectManagerEvent(this, var1);
      this.listeners.call().projectClosed(var2);
   }

   void addProjectForManagement(ManagedProjectImpl var1) {
      synchronized (this.projectLock) {
         this.projects.put(var1.getProject(), var1);
      }
   }

   public void setProjectLoadingTrackerProvider(Supplier<ProjectLoadingTracker> var1) {
      this.projectLoadingTrackerProvider = var1;
   }

   public RecentProjects getRecentProjects() {
      return this.recentProjects;
   }
}
