package org.etlforge.integration.client.project.persistor.v20;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.InterchangeLoadingException;
import org.etlforge.integration.client.project.ProjectDataLoader;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.integration.client.project.ProjectLoadingIssue;
import org.etlforge.integration.client.project.ProjectLoadingTracker;
import org.etlforge.integration.client.project.ProjectPipelinePluginStore;
import org.etlforge.integration.client.project.SystemObjects;
import org.etlforge.integration.client.project.persistor.v10.InterchangeXmlReader;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.id.FolderId;
import org.etlforge.integration.data.entity.metadata.EntityTypeConfig;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.persist.XmlPersistor;
import org.xml.sax.SAXException;

public final class ProjectDataLoaderImpl implements ProjectDataLoader {
   private final ProjectDataLoader version;
   private final IntegrationProject project;
   private final ProjectFile projectFile;
   private final File rootDirectory;
   private final File dataDirectory;
   private final Logger logger;

   public ProjectDataLoaderImpl(IntegrationProject var1, ProjectFile var2, Logger var3) {
      this.project = Preconditions.checkNotNull(var1, "project");
      this.logger = Preconditions.checkNotNull(var3, "logger");
      this.projectFile = Preconditions.checkNotNull(var2, "projectFile");
      this.version = new InterchangeXmlReader(var2, var3);
      this.rootDirectory = var2.getParentFolder();
      this.dataDirectory = ProjectFolders.getProjectDataFolder(this.rootDirectory).asJavaFile();
   }

   @Override
   public void loadDeletedItems(Receiver<EntityDescriptor> var1) throws InterchangeLoadingException {
      this.version.loadDeletedItems(var1);
   }

   @Override
   public void loadUiData() {
      this.version.loadUiData();
   }

   @Override
   public List<EntityDescriptor> getEntitiesToDisplayOnStartUp() {
      return this.version.getEntitiesToDisplayOnStartUp();
   }

   @Override
   public EntityDescriptor getActiveEntityOnStartUp() {
      return this.version.getActiveEntityOnStartUp();
   }

   @Override
   public List<EntityDescriptor> getExpandedOnStartUp() {
      return this.version.getExpandedOnStartUp();
   }

   @Override
   public ProjectPipelinePluginStore loadPluginInfo() throws InterchangeLoadingException {
      return this.version.loadPluginInfo();
   }

   @Override
   public Set<ProjectLoadingIssue> loadProject(ProjectLoadingTracker var1) throws InterchangeLoadingException {
      this.logger.info("Starting loading the project " + this.project.getName() + " from " + this.rootDirectory.getAbsolutePath());

      try {
         HashSet var2 = Sets.newHashSet();
         XmlPersistor var3 = XmlPersistor.load(this.getProjectStructureFile());
         Map var4 = ProjectStructurePersistor.load(var3, var2);
         IntegrationEntityMapLookup var5 = this.loadEntities(var4);
         SystemObjects.populate(var5);
         this.restoreDependentData(var5);
         return var2;
      } catch (IOException var6) {
         throw new InterchangeLoadingException(var6);
      } catch (SAXException var7) {
         throw new InterchangeLoadingException(var7);
      } catch (InterchangeLoadingException var8) {
         this.logger.throwing(this.getClass().getName(), "loadProject", var8);
         throw var8;
      }
   }

   private File getProjectStructureFile() {
      return new File(this.rootDirectory, this.projectFile.getProperty("structure-file"));
   }

   private IntegrationEntityMapLookup loadEntities(Map<EntityType, ProjectStructurePersistor.Node> var1) throws InterchangeLoadingException {
      IntegrationEntityMapLookup var2 = new IntegrationEntityMapLookup();

      for (Entry var4 : var1.entrySet()) {
         EntityType var5 = (EntityType)var4.getKey();
         ProjectDataLoaderImpl.EntityTypeHandler var6 = new ProjectDataLoaderImpl.EntityTypeHandler(var5, (ProjectStructurePersistor.Node)var4.getValue(), var2);
         var6.load();
      }

      return var2;
   }

   private void restoreDependentData(IntegrationEntityLookup var1) throws InterchangeLoadingException {
      this.logger.info("Restoring dependent data");

      for (EntityType var3 : this.project.getSupportedEntityTypes()) {
         for (IntegrationEntity var5 : this.project.getAllEntitiesOfType(var3)) {
            this.restoreDependentData(var5, var1);
         }
      }
   }

   private void restoreDependentData(IntegrationEntity var1, IntegrationEntityLookup var2) throws InterchangeLoadingException {
      if (!(var1 instanceof Folder)) {
         File var3 = FileSupport.getFile(this.dataDirectory, var1);

         try {
            XmlPersistor var4 = XmlPersistor.load(var3);
            var1.restoreDependentData(var4, var2);
         } catch (Exception var5) {
            throw new InterchangeLoadingException(
               "Failed to load dependent data from the file " + var3.getAbsolutePath() + ". Reason: " + var5.getMessage(), var5
            );
         }
      }
   }

   private final class EntityTypeHandler {
      private final EntityType type;
      private final ProjectStructurePersistor.Node root;
      private final IntegrationEntityMapLookup lookup;
      private final Set<File> files;

      private EntityTypeHandler(EntityType var2, ProjectStructurePersistor.Node var3, IntegrationEntityMapLookup var4) {
         this.type = var2;
         this.root = var3;
         this.lookup = var4;
         this.files = Sets.newHashSet();
      }

      public void load() throws InterchangeLoadingException {
         ProjectDataLoaderImpl.this.logger.info("Loading " + this.type.getPlural());
         RootFolder var1 = ProjectDataLoaderImpl.this.project.getRootFolder(this.type);
         this.loadEntities(this.root, var1);
         this.cleanupOldFiles();
         ProjectDataLoaderImpl.this.logger.info("Done loading " + this.type.getPlural());
      }

      private void loadEntities(ProjectStructurePersistor.Node var1, IntegrationEntity var2) throws InterchangeLoadingException {
         EntityDescriptor var3 = var1.getEntity();
         if (var3 != null) {
            String var4 = var1.getName();
            File var5 = FileSupport.getFile(ProjectDataLoaderImpl.this.dataDirectory, var3);
            this.files.add(var5);
            if (var5.exists()) {
               ProjectDataLoaderImpl.this.logger.fine("Loading " + var3.toString() + " from file " + var5.getAbsolutePath());
               IntegrationEntity var6 = this.createEntityShell(this.type, var3);
               this.loadItemData(var2, var5, var6);
               var2 = var6;
            } else {
               String var9 = "Could not restore the "
                  + var3.getEntityType()
                  + " \""
                  + var4
                  + "\" [GUID = "
                  + var3.getID()
                  + "] that is referred to in the project file, because the corresponding data file "
                  + var5.getAbsolutePath()
                  + " does not exist.";
               ProjectDataLoaderImpl.this.logger.severe(var9);
               ErrorLog.log(ProjectDataLoaderImpl.class, var9, new Exception(var9));
            }
         }

         for (ProjectStructurePersistor.Node var8 : var1.children()) {
            this.loadEntities(var8, var2);
         }
      }

      private void loadItemData(IntegrationEntity var1, File var2, IntegrationEntity var3) throws InterchangeLoadingException {
         try {
            XmlPersistor var4 = XmlPersistor.load(var2);
            var3.restoreFrom(var4);
         } catch (Exception var6) {
            String var5 = String.format(
               "Failed to load the entity \"%s\" of type %s: %s\n\nData file: %s",
               var3.getName(),
               var3.getEntityType(),
               var6.getMessage(),
               var2.getAbsoluteFile()
            );
            throw new InterchangeLoadingException(var5, var6);
         }

         var3.setParent(var1);
         this.lookup.addItem(var3);
      }

      private IntegrationEntity createEntityShell(EntityType var1, EntityDescriptor var2) {
         return var2.getEntityType() == EntityType.Folder ? new Folder((FolderId)var2.getID(), var2.getName(), var1) : this.instantiateEntity(var1, var2);
      }

      private IntegrationEntity instantiateEntity(EntityType var1, EntityDescriptor var2) {
         try {
            return EntityTypeConfig.recreateEntity(var1, var2.getID(), var2.getName());
         } catch (Exception var4) {
            throw new RuntimeException("Unexpected EntityType: " + var1, var4);
         }
      }

      private void cleanupOldFiles() {
         File var1 = FileSupport.getDirectory(ProjectDataLoaderImpl.this.dataDirectory, this.type);

         for (File var5 : var1.listFiles(FileSupport.DATA_FILE_FILTER)) {
            if (!this.files.contains(var5)) {
               var5.delete();
            }
         }
      }
   }
}
