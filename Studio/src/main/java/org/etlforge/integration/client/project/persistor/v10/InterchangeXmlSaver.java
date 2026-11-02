package org.etlforge.integration.client.project.persistor.v10;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.XmlStringBuilder;

public final class InterchangeXmlSaver implements ProjectPersistor {
   private final ManagedProject managedProject;
   private final ProjectFile projectFile;
   private final File rootDirectory;

   public InterchangeXmlSaver(ManagedProject var1) {
      this.managedProject = var1;
      this.projectFile = var1.getProjectFile();
      this.rootDirectory = var1.getLocation();
   }

   @Override
   public void saveProjectRelations() throws XmlSavingException {
      throw new UnsupportedOperationException();
   }

   @Override
   public void save(IntegrationEntity var1) throws XmlSavingException {
      throw new UnsupportedOperationException();
   }

   public void saveDeletedItemsInformation() throws XmlSavingException {
      IntegrationProjectLock.ensureLockHeld();
      File var1 = new File(this.rootDirectory, this.projectFile.getProperty("deleted-file"));

      try {
         KongaIoUtils.createAndWrite(var1, this.createDeletedItemsXml());
      } catch (IOException var3) {
         throw new XmlSavingException(var3);
      }
   }

   public void saveUiState(ProjectUiState var1) throws XmlSavingException {
      String var2 = this.projectFile.getProperty("uistate-file");
      File var3 = new File(this.rootDirectory, var2);

      try {
         KongaIoUtils.createAndWrite(var3, var1.getXml());
      } catch (IOException var5) {
         throw new XmlSavingException(var5);
      }
   }

   public File getFile(IntegrationEntity var1) {
      throw new UnsupportedOperationException();
   }

   private String createDeletedItemsXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.addElement("DeletedItems");

      for (EntityType var5 : EntityType.values()) {
         for (IntegrationId var7 : this.managedProject.getDeletedItemsOfType(var5)) {
            var1.addElement(var5.toString());
            var1.addAttribute("id_val", var7.toString());
            var1.closeElement();
         }
      }

      var1.closeElement();
      return var1.getXml();
   }

   public void savePluginInfo() throws InterchangeSavingException {
      try {
         PipelinePluginInfoWriter var1 = new PipelinePluginInfoWriter(this.managedProject);
         var1.write();
      } catch (IOException var2) {
         throw new InterchangeSavingException(var2);
      }
   }
}
