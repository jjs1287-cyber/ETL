package org.etlforge.integration.client.project;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.application.filestore.DefaultFileStore;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.entity.settings.CloseableEntityResources;
import org.etlforge.integration.client.entity.settings.EntityResources;

import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.project.event.EntitySavedEvent;
import org.etlforge.integration.client.project.event.UpdatedDependenciesEvent;
import org.etlforge.integration.client.project.export.ExportJitterpackDataPreparer;
import org.etlforge.integration.client.project.export.ProjectPostExportManager;
import org.etlforge.integration.client.project.jitterpack.ExportPreparationException;
import org.etlforge.integration.client.project.jitterpack.PostExportJob;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.client.project.permission.PermissionsFileStorage;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.validation.DefaultValidationModel2;
import org.etlforge.integration.client.structure.cache.FileBasedInputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.sfdc.BulkLoadPipelineBrowser;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.Dependencies;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ItemRevivalOrder;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.TrashCan;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.io.Folder;
import org.etlforge.system.callback.Receiver;

final class ManagedProjectImpl implements ManagedProject {
   private final ProjectManager manager;
   private final IntegrationProject project;
   private final ProjectFile projectFile;
   private final File rootDirectory;
   private final Set<IntegrationEntity> savedEntities = Sets.newHashSet();
   private Set<File> filesToDeleteOnExit;
   private final FileStore fileStore;
   private Dependencies dataLinks = new Dependencies();
   private DependencyStore dependencyStore;
   private ProjectUiState uiState;
   private ManagedProjectPersistor projectSaver;
   private final TrashCan trashCan;
   private InputAndOutputMappingTreeStructureCache transformationStructureCache;
   private InputAndOutputMappingTreeStructureCache wsCallStructureCache;
   private InputAndOutputMappingTreeStructureCache connectorCallStructureCache;
   private final ProjectPluginInfoImpl plugins;
   private final ProjectTestHistoryImpl testResultStore;
   private final DeployHistory deployHistory;
   private final PermissionsManager permissionsManager;
   private final CloseableEntityResources entityResources;
   private boolean readOnly;
   private final Multimap<EntityType, IntegrationId> deletedItems = HashMultimap.create();
   private final ValidationModel validationModel;
   private final PostProjectSavedProcessor postSaveProcessor;
   private final EventBus eventBus = new EventBus();
   private final Object lock = new Object();

   ManagedProjectImpl(ProjectManager var1, IntegrationProject var2, ProjectFile var3, File var4) {
      this.manager = var1;
      this.project = var2;
      this.rootDirectory = var4;
      this.projectFile = var3;
      this.dependencyStore = new DependencyStore(this.dataLinks);
      this.validationModel = new DefaultValidationModel2(this);
      this.trashCan = new TrashCan(var2);
      this.plugins = new ProjectPluginInfoImpl(this);
      this.testResultStore = new ProjectTestHistoryImpl(this);
      this.deployHistory = new ProjectDeployHistoryImpl(this);
      this.permissionsManager = this.createPermissionsManager();
      this.createMappingTreeStructureCaches(var4);
      this.entityResources = this.createEntityResources(var4);
      this.fileStore = new DefaultFileStore(ProjectFolders.getFileStoreFolder(this.rootDirectory));
      this.postSaveProcessor = new PostProjectSavedProcessor(var2, var1.getBackupService(), var3);
      ExtensionObjectService.getService().registerInstance(var2, ManagedProject.class, this);
   }

   public void setReadOnly(boolean var1) {
      this.readOnly = var1;
      ((ManagedProjectPersistor)this.getProjectPersistor()).setReadOnly(var1);
   }

   @Override
   public boolean isReadOnly() {
      return this.readOnly;
   }

   @Override
   public ValidationModel getValidationModel() {
      return this.validationModel;
   }

   public void projectWasLoaded() {
      this.buildDataStore();
      this.buildDependencies();
      this.validationModel.validateProject();
      this.getProject().projectWasLoaded();
      this.listenToProject();
   }

   private void listenToProject() {
      ManagedProjectImpl.ProjectListenerImpl var1 = new ManagedProjectImpl.ProjectListenerImpl();
      this.project.addProjectListener(var1);
      this.project.addPropertyChangeListener(var1);
      new ProjectPropertyChangeSupport(this.project, false).addPropertyChangeListener(new ManagedProjectImpl.DeployDirtyMonitor());
   }

   private PermissionsManager createPermissionsManager() {
      PermissionsFileStorage var1 = new PermissionsFileStorage(
         ProjectFolders.getPermissionsFolder(this.rootDirectory), getEntityTypesForPermissions(this.project)
      );
      EntityPersistor var2 = new EntityPersistor() {
         @Override
         public void save(IntegrationEntity var1) throws InterchangeSavingException {
            IntegrationProjectLock.acquire();

            try {
               var1.setDeployDirty(true);
               ManagedProjectImpl.this.saveIntegrationEntity(var1);
            } catch (InterchangeSavingException var6) {
               ErrorLog.log(
                  ManagedProject.class,
                  "Failed to save the changes made to \"" + var1.getName() + "\" to disk after it was made deploy dirty by new permissions settings.",
                  var6
               );
            } finally {
               IntegrationProjectLock.release();
            }
         }
      };
      return new PermissionsManager(this.project, var1, new ManagedProjectImpl.ProjectDependenciesImpl(), var2);
   }

   private static Collection<EntityType> getEntityTypesForPermissions(IntegrationProject var0) {
      HashSet var1 = Sets.newHashSet(var0.getSupportedEntityTypes());
      var1.add(EntityType.Project);
      var1.add(EntityType.RootFolder);
      var1.add(EntityType.Folder);
      return var1;
   }

   private CloseableEntityResources createEntityResources(File var1) {
      Folder var2 = ProjectFolders.getSettingsFolder(var1);
      return new EntityResourcesImpl(var2.subFolder("Entity"));
   }

   private void createMappingTreeStructureCaches(File var1) {
      Folder var2 = ProjectFolders.getCacheFolder(var1);
      this.transformationStructureCache = new FileBasedInputAndOutputMappingTreeStructureCache("TransformationStructures", var2);
      this.wsCallStructureCache = new FileBasedInputAndOutputMappingTreeStructureCache("WebServiceCallStructures", var2);
      this.connectorCallStructureCache = new FileBasedInputAndOutputMappingTreeStructureCache("ConnectorCallStructures", var2);
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public ProjectFile getProjectFile() {
      return this.projectFile;
   }

   @Override
   public TrashCan getTrashCan() {
      return this.trashCan;
   }

   @Override
   public FileStore getFileStore() {
      return this.fileStore;
   }

   @Override
   public InputAndOutputMappingTreeStructureCache getTransformationMappingStructureCache() {
      return this.transformationStructureCache;
   }

   @Override
   public InputAndOutputMappingTreeStructureCache getWebServiceMappingStructureCache() {
      return this.wsCallStructureCache;
   }

   @Override
   public InputAndOutputMappingTreeStructureCache getConnectorCallMappingStructureCache() {
      return this.connectorCallStructureCache;
   }

   @Override
   public DependencyStore getDependencyStore() {
      return this.dependencyStore;
   }

   @Override
   public File getLocation() {
      return this.rootDirectory;
   }

   @Override
   public ProjectUiState getUiState() {
      return this.uiState;
   }

   @Override
   public PermissionsManager getPermissions() {
      return this.permissionsManager;
   }

   @Override
   public void rebuildDependencies() {
      this.dataLinks.dispose();
      this.buildDependencies();
   }

   private void addLink(EntityLink var1) {
      this.dataLinks.addLink(var1);
   }

   @Override
   public void renameProject(String var1) {
      this.checkReadOnly();
      IntegrationProjectLock.ensureLockHeld();
      ProjectRenamer var2 = new ProjectRenamer(this.manager, this, var1);
      var2.rename();
   }

   @Override
   public void saveIntegrationEntity(IntegrationEntity var1) throws InterchangeSavingException {
      this.checkReadOnly();
      IntegrationProjectLock.ensureLockHeld();
      if (var1 != this.project) {
         this.getProjectPersistor().save(var1);
         this.eventBus.post(new EntitySavedEvent(this, var1));
      }

      this.projectWasSaved();
   }

   private void projectWasSaved() {
      boolean var1 = this.manager.getCurrentProject() == this;
      this.postSaveProcessor.run(var1);
   }

   @Override
   public void saveProjectRelationships() throws InterchangeSavingException {
      this.checkReadOnly();
      IntegrationProjectLock.ensureLockHeld();
      this.getProjectPersistor().saveProjectRelations();
      this.projectWasSaved();
   }

   @Override
   public void saveToDisk() throws InterchangeSavingException {
      this.checkReadOnly();
      IntegrationProjectLock.ensureLockHeld();
      ManagedProjectPersistor var1 = this.getExtendedProjectPersistor();
      var1.saveProjectRelations();
      var1.saveDeletedItemsInformation();

      for (IntegrationEntity var3 : this.project.getAllEntities()) {
         var1.save(var3);
      }

      var1.savePluginInfo();
      this.projectWasSaved();
   }

   @Override
   public void saveUiState() throws InterchangeSavingException {
      this.checkReadOnly();
      this.getExtendedProjectPersistor().saveUiState(this.uiState);
   }

   @Override
   public void savePluginInfo() throws InterchangeSavingException {
      this.checkReadOnly();
      this.getExtendedProjectPersistor().savePluginInfo();
   }

   private void checkReadOnly() {
      if (this.readOnly) {
         throw new UnsupportedOperationException("This project is read-only");
      }
   }

   @Override
   public ProjectPersistor getProjectPersistor() {
      return this.getExtendedProjectPersistor();
   }

   private ManagedProjectPersistor getExtendedProjectPersistor() {
      synchronized (this.lock) {
         if (this.projectSaver == null) {
            this.projectSaver = new ManagedProjectPersistor(this);
         }

         return this.projectSaver;
      }
   }

   @Override
   public void eraseDeletedItemsHistory() {
      this.deletedItems.clear();
   }

   void buildDataStore() {
      this.dataLinks = new Dependencies();
      this.dependencyStore = new DependencyStore(this.dataLinks);
      this.uiState = new ProjectUiState(IntegrationEntityMapLookup.build(this.project));
      this.project.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            ManagedProjectImpl.this.savedEntities.add(var1);
         }
      });
      this.fireDependenciesUpdated();
   }

   void buildDependencies() {
      final IntegrationEntityMapLookup var1 = IntegrationEntityMapLookup.build(this.project);
      SystemObjects.populate(var1);
      this.project.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1x) {
            for (EntityLink var3 : var1x.getLinks(var1)) {
               ManagedProjectImpl.this.addLink(var3);
            }
         }
      });
      this.fireDependenciesUpdated();
   }

   void entityWasSaved(IntegrationEntity var1) {
      if (!var1.isDeleted()) {
         this.savedEntities.add(var1);
         this.updateLinksForSavedEntity(var1);
         this.validationModel.validateItem(var1);
         this.fireDependenciesUpdated();
      }
   }

   private void updateLinksForSavedEntity(IntegrationEntity var1) {
      for (EntityLink var4 : this.dataLinks.updateLinks(var1, this.project.getItemLookup())) {
         this.validationModel.validateItem(var4.getMainObject());
      }
   }

   private void removeFileFromDeleteOnExit(File var1) {
      if (this.filesToDeleteOnExit != null) {
         this.filesToDeleteOnExit.remove(var1);
      }
   }

   void close() {
      if (this.isReadOnly()) {
         this.entityResources.close(Collections.emptyList());
      } else {
         this.deleteFiles();
         this.saveUiStateSilently();
         this.entityResources.close(this.getTrashCan().getContents());
         this.emptyTrashCan();
         this.testResultStore.onProjectClosed();
      }

      this.testResultStore.dispose();
      ExtensionObjectService.getService().registerInstance(this.project, ManagedProject.class, null);
   }

   @Override
   public void emptyTrashCan() {
      this.deleteStructureFilesForTrashCanContents();
      this.plugins.removeEntities(this.trashCan.getContents());
      this.trashCan.empty();
   }

   private void deleteStructureFilesForTrashCanContents() {
      List var1 = this.trashCan.getContents();

      for (Object __etlItem417 : Iterables.filter(var1, EntityFilters.allOfType(EntityType.Transformation))) {
         IntegrationEntity var3 = (IntegrationEntity) __etlItem417;
         this.transformationStructureCache.delete(var3.getID());
      }

      for (Object __etlItem421 : Iterables.filter(var1, EntityFilters.allOfType(EntityType.WebServiceCall))) {
         IntegrationEntity var6 = (IntegrationEntity) __etlItem421;
         this.wsCallStructureCache.delete(var6.getID());
      }

      for (Object __etlItem425 : Iterables.filter(var1, Predicates.instanceOf(ConnectorCall.class))) {
         IntegrationEntity var7 = (IntegrationEntity) __etlItem425;
         this.connectorCallStructureCache.delete(var7.getID());
      }
   }

   private void deleteFiles() {
      if (this.filesToDeleteOnExit != null) {
         for (File var2 : this.filesToDeleteOnExit) {
            var2.delete();
         }
      }
   }

   private void saveUiStateSilently() {
      try {
         this.saveUiState();
      } catch (InterchangeSavingException var2) {
         var2.printStackTrace();
      }
   }

   @Override
   public PostExportManager prepareForExport(boolean var1) throws ExportPreparationException {
      ProjectPostExportManager var2 = new ProjectPostExportManager(this, var1);

      try {
         this.prepareForExportImpl(var2);
         return var2;
      } catch (ExportPreparationException var4) {
         var2.runJobs(PostExportJob.Condition.ON_FAILURE);
         throw var4;
      }
   }

   private void prepareForExportImpl(PostExportManager var1) throws ExportPreparationException {
      IntegrationProjectLock.acquire();

      try {
         this.project.visitAllEntities(new ExportJitterpackDataPreparer(var1));
         this.saveToDisk();
      } catch (InterchangeSavingException var6) {
         throw new ExportPreparationException("Failed to prepare the project for export.", var6);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   @Override
   public ProjectPluginInfo getPipelinePlugins() {
      return this.plugins;
   }

   @Override
   public EntityResources getEntityResources() {
      return this.entityResources;
   }

   @Override
   public ProjectTestHistory getTestHistory() {
      return this.testResultStore;
   }

   @Override
   public void projectWasDeployed() throws InterchangeSavingException {
      PostDeployProcessor.processDeploy(this);
   }

   @Override
   public void partialProjectWasDeployed(IntegrationProject var1) throws InterchangeSavingException {
      Preconditions.checkArgument(var1.getID().equals(this.getProject().getID()), "project does not represent items from this ManagedProject");
      PostDeployProcessor.processPartialDeploy(this, var1);
   }

   @Override
   public DeployHistory getDeployHistory() {
      return this.deployHistory;
   }

   @Override
   public Collection<IntegrationId> getDeletedItemsOfType(EntityType var1) {
      return this.deletedItems.get(Preconditions.checkNotNull(var1, "type"));
   }

   private void fireDependenciesUpdated() {
      this.eventBus.post(new UpdatedDependenciesEvent(this));
   }

   @Override
   public EventBus getEventBus() {
      return this.eventBus;
   }

   void markEntityAsDeleted(EntityDescriptor var1) {
      this.deletedItems.put(var1.getEntityType(), var1.getID());
   }

   void setPlugins(ProjectPipelinePluginStore var1) {
      this.plugins.addAllFrom(var1);
   }

   private static class DeployDirtyMonitor implements PropertyChangeListener {
      private static final Set<String> propertiesToIgnore = Sets.newHashSet("IntegrationEntity#DeployDirty", "IntegrationEntity#MofidiedAt");

      private DeployDirtyMonitor() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!propertiesToIgnore.contains(var1.getPropertyName())) {
            ((IntegrationEntity)var1.getSource()).setDeployDirty(true);
         }
      }
   }

   private class ProjectDependenciesImpl implements ProjectDependencies {
      private ProjectDependenciesImpl() {
      }

      @Override
      public FluentIterable<IntegrationEntity> getRequiredObjects(IntegrationEntity var1) {
         return ManagedProjectImpl.this.dependencyStore == null ? Iterables2.empty() : ManagedProjectImpl.this.dependencyStore.getRequiredObjects(var1);
      }

      @Override
      public FluentIterable<IntegrationEntity> getDependingObjects(IntegrationEntity var1) {
         return ManagedProjectImpl.this.dependencyStore == null ? Iterables2.empty() : ManagedProjectImpl.this.dependencyStore.getDependingObjects(var1);
      }
   }

   private class ProjectListenerImpl extends ProjectListener.Adapter implements PropertyChangeListener {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            this.clearDeletedFlag(var3);
         }
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         try {
            this.itemsWereRevived(var1.getEntities());
         } catch (InterchangeSavingException var3) {
            ErrorLog.attention(this, "Failed to revive the selected item(s).", var3);
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
      }

      private void itemsWereRevived(List<? extends IntegrationEntity> var1) throws InterchangeSavingException {
         for (IntegrationEntity var4 : Iterables2.sortedCopy(var1, new ItemRevivalOrder())) {
            this.processRevival(var4);
         }

         ManagedProjectImpl.this.validationModel.validateProject();
         ManagedProjectImpl.this.saveToDisk();
         ManagedProjectImpl.this.fireDependenciesUpdated();
      }

      private void processRevival(IntegrationEntity var1) {
         this.clearDeletedFlag(var1);
         this.addLinksForRevivedItem(var1);
         File var2 = ManagedProjectImpl.this.getExtendedProjectPersistor().getFile(var1);
         ManagedProjectImpl.this.removeFileFromDeleteOnExit(var2);
      }

      private void clearDeletedFlag(IntegrationEntity var1) {
         ManagedProjectImpl.this.deletedItems.remove(var1.getEntityType(), var1.getID());
      }

      private void addLinksForRevivedItem(IntegrationEntity var1) {
         IntegrationEntityLookup var2 = ManagedProjectImpl.this.project.getItemLookup();

         for (EntityLink var4 : var1.getLinks(var2)) {
            ManagedProjectImpl.this.addLink(var4);
         }

         if (var1 instanceof SalesforceBulkLoad) {
            this.recreateBulkLoadOperationPipeline((SalesforceBulkLoad)var1, var2);
         }
      }

      private void recreateBulkLoadOperationPipeline(SalesforceBulkLoad var1, IntegrationEntityLookup var2) {
         Operation var3 = var2.getEntity(var1.getImplementingOperationId(), Operation.class);
         if (var3 == null) {
            ErrorLog.log(
               this, null, new Exception("Cannot find the Operation with ID " + var1.getImplementingOperationId() + " in the project. This is a bug.")
            );
         } else {
            OperationPipeline var4 = new OperationPipeline(var3.getPipeline());
            BulkLoadPipelineBrowser var5 = new BulkLoadPipelineBrowser(var4);
            var5.setBulkLoad(var1);
            var3.setPipeline(var4);
         }
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         try {
            List var2 = var1.getEntities();
            this.processDeletedEnties(var2);
         } catch (InterchangeSavingException var3) {
            ErrorLog.attention(this, "An error occurred while saving the project.", var3);
         }
      }

      private void processDeletedEnties(List<IntegrationEntity> var1) throws InterchangeSavingException {
         this.checkDeployStatus(var1);
         ManagedProjectPersistor var2 = ManagedProjectImpl.this.getExtendedProjectPersistor();
         var2.saveProjectRelations();

         for (IntegrationEntity var4 : var1) {
            this.processDeletion(var4, var2);
         }

         var2.saveDeletedItemsInformation();
         ProjectUtils.saveProjectFile(ManagedProjectImpl.this.project, ManagedProjectImpl.this.projectFile);
         ManagedProjectImpl.this.fireDependenciesUpdated();
      }

      private void processDeletion(IntegrationEntity var1, ManagedProjectPersistor var2) throws InterchangeSavingException {
         Set var3 = ManagedProjectImpl.this.dataLinks.objectDeleted(var1);
         this.validateBrokenLinkObjects(var3, var1);
         ManagedProjectImpl.this.dataLinks.objectDeleted(var1);
         if (this.hasEntityBeenSaved(var1)) {
            var2.save(var1);
            if (!var1.hasBeenDeployed()) {
               this.addFileToDeleteOnExit(var2.getFile(var1));
            }

            ManagedProjectImpl.this.trashCan.throwAway(var1);
         }

         ManagedProjectImpl.this.permissionsManager.removeEntity(var1);
      }

      private void validateBrokenLinkObjects(Collection<EntityLink> var1, IntegrationEntity var2) {
         for (EntityLink var4 : var1) {
            IntegrationEntity var5 = var4.getMainObject() == var2 ? var4.getDependantObject() : var4.getMainObject();
            ManagedProjectImpl.this.validationModel.validateItem(var5);
         }
      }

      private void checkDeployStatus(Iterable<IntegrationEntity> var1) {
         for (IntegrationEntity var3 : var1) {
            if (var3.hasBeenDeployed()) {
               ManagedProjectImpl.this.deletedItems.put(var3.getEntityType(), var3.getID());
            }
         }
      }

      private boolean hasEntityBeenSaved(IntegrationEntity var1) {
         return ManagedProjectImpl.this.savedEntities.contains(var1) || var1 instanceof org.etlforge.integration.data.entity.Folder;
      }

      private void addFileToDeleteOnExit(File var1) {
         if (ManagedProjectImpl.this.filesToDeleteOnExit == null) {
            ManagedProjectImpl.this.filesToDeleteOnExit = new HashSet<>();
            ManagedProjectImpl.this.filesToDeleteOnExit.add(var1);
         } else {
            ManagedProjectImpl.this.filesToDeleteOnExit.add(var1);
         }
      }
   }
}
