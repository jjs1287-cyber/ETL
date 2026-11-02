package org.etlforge.integration.client.project.persistor.v20;

import java.io.File;
import org.etlforge.integration.client.project.ExtendedProjectPersistor;
import org.etlforge.integration.client.project.FileBasedProjectPersistor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.client.project.persistor.v10.InterchangeXmlSaver;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.util.persist.XmlPersistor;

public final class ProjectXmlPersistor implements ExtendedProjectPersistor, FileBasedProjectPersistor {
   private final InterchangeXmlSaver version1;
   private final IntegrationProject project;
   private final ProjectFile projectFile;
   private final File rootDirectory;
   private final File dataRoot;
   private final ProjectStructureFileSaver structureFileSaver;
   private final Object lock;

   public ProjectXmlPersistor(ManagedProject var1) {
      this.version1 = new InterchangeXmlSaver(var1);
      this.project = var1.getProject();
      this.projectFile = var1.getProjectFile();
      this.rootDirectory = var1.getLocation();
      this.dataRoot = ProjectFolders.getProjectDataFolder(this.rootDirectory).asJavaFile();
      this.structureFileSaver = new ProjectStructureFileSaver(this.projectFile, this.rootDirectory);
      this.lock = new Object();
   }

   @Override
   public void saveDeletedItemsInformation() throws InterchangeSavingException {
      synchronized (this.lock) {
         this.version1.saveDeletedItemsInformation();
      }
   }

   @Override
   public void save(IntegrationEntity var1) throws InterchangeSavingException {
      synchronized (this.lock) {
         try {
            XmlPersistor var3 = XmlPersistor.newRoot("Entity");
            var3.putString("type", var1.getEntityType().name());
            var1.willBeSaved();
            var1.writeTo(var3);
            var3.store(this.getFile(var1));
            var1.setTransient(false);
         } catch (Exception var5) {
            throw new InterchangeSavingException(
               "An error occurred while saving \"" + var1.getName() + "\" [" + var1.getEntityType() + "]: " + var5.getMessage(), var5
            );
         }
      }
   }

   @Override
   public void savePluginInfo() throws InterchangeSavingException {
      synchronized (this.lock) {
         this.version1.savePluginInfo();
      }
   }

   @Override
   public void saveProjectRelations() throws InterchangeSavingException {
      IntegrationProjectLock.ensureLockHeld();
      synchronized (this.lock) {
         this.structureFileSaver.saveToFile(this.project);
      }
   }

   @Override
   public void saveUiState(ProjectUiState var1) throws InterchangeSavingException {
      synchronized (this.lock) {
         this.version1.saveUiState(var1);
      }
   }

   @Override
   public File getFile(IntegrationEntity var1) {
      return FileSupport.getFile(this.dataRoot, var1);
   }
}
