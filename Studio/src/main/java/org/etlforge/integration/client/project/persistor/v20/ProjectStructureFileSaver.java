package org.etlforge.integration.client.project.persistor.v20;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.persistor.v10.XmlSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.persist.XmlPersistor;

final class ProjectStructureFileSaver {
   private final ProjectFile projectFile;
   private final File rootDirectory;

   ProjectStructureFileSaver(ProjectFile var1, File var2) {
      this.projectFile = var1;
      this.rootDirectory = var2;
   }

   void saveToFile(IntegrationProject var1) throws XmlSavingException {
      File var2 = this.getProjectStructureFile();
      File var3 = this.createCopyOfProjectStructureFile(var2);

      try {
         XmlPersistor var4 = XmlPersistor.newRoot("Project");
         ProjectStructurePersistor.save(var1, var4);
         var4.store(var2);
         this.validateProjectStructureFile(var2, var3);
         if (var3 != null) {
            var3.delete();
         }
      } catch (Exception var5) {
         throw new XmlSavingException("An I/O error occurred: " + var5.getMessage(), var5);
      }
   }

   private File getProjectStructureFile() {
      return new File(this.rootDirectory, this.projectFile.getProperty("structure-file"));
   }

   private File createCopyOfProjectStructureFile(File var1) {
      if (var1.exists()) {
         try {
            File var2 = new File("__project_file_copy__.xml");
            Files.copy(var1, var2);
            return var2;
         } catch (IOException var3) {
         }
      }

      return null;
   }

   private void validateProjectStructureFile(File var1, File var2) {
      try {
         XmlPersistor.load(var1);
      } catch (Exception var4) {
         ErrorLog.log(
            ProjectXmlPersistor.class,
            "An error occurred while saving the project. All changes may not have been saved correctly. Please try and save all recent changes again.",
            var4
         );
         if (var2 != null) {
            this.restoreCopyOfProjectFile(var2);
         }
      }
   }

   private void restoreCopyOfProjectFile(File var1) {
      try {
         Files.copy(var1, this.getProjectStructureFile());
      } catch (IOException var3) {
         ErrorLog.log(ProjectXmlPersistor.class, "An error occurred while trying to restore the project structure file to its previous state.", var3);
      }
   }
}
