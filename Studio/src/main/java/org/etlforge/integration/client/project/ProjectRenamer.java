package org.etlforge.integration.client.project;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

class ProjectRenamer {
   private final ProjectManager manager;
   private final ManagedProjectImpl managedProject;
   private final String newName;

   ProjectRenamer(ProjectManager var1, ManagedProjectImpl var2, String var3) {
      this.manager = var1;
      this.managedProject = var2;
      this.newName = var3;
   }

   void rename() {
      if (this.isNewName() && this.isRenameAllowed()) {
         this.createBackup();

         try {
            this.manager.closeProject(this.managedProject);
            ProjectLocation var1 = new ProjectLocation(this.managedProject.getProject().getName(), this.managedProject.getLocation());
            File var2 = this.getNewFolderName();
            ProjectFile var3 = this.managedProject.getProjectFile();
            var3.setNewName(this.newName);
            FileUtils.copyDirectory(var1.getLocation(), var2);
            IntegrationProject var4 = this.manager.loadProject(this.newName, var2, ProjectLoadingIssuesHandler.IGNORE);
            RecentProjects var5 = this.manager.getRecentProjects();
            var5.removeProjectLocation(var1);
            this.manager.setCurrentProject(var4);
            this.deleteOldLocation(var1.getLocation());
         } catch (CloseProjectVetoException var6) {
         } catch (Exception var7) {
            ErrorLog.attention(ProjectRenamer.class, "An error occurred when renaming the project: " + var7.getMessage(), var7);
         }
      }
   }

   private boolean isNewName() {
      IntegrationProject var1 = this.managedProject.getProject();
      String var2 = var1.getName();
      boolean var3 = !var2.equals(this.newName);
      if (var3 && SystemUtils.IS_OS_WINDOWS) {
         boolean var4 = var2.equalsIgnoreCase(this.newName);
         if (var4) {
            return false;
         }
      }

      return var3;
   }

   private boolean isRenameAllowed() {
      return this.validateName() && this.validateLocation();
   }

   private boolean validateName() {
      try {
         NameValidator var1 = NameValidatorStore.getInstance().get(IntegrationProject.class);
         var1.validateName(this.newName);
         return true;
      } catch (IllegalNameException var2) {
         Alert.error(var2.getMessage(), "Invalid Name");
         return false;
      }
   }

   private boolean validateLocation() {
      File var1 = this.getNewFolderName();
      return var1.exists() ? this.overwriteExisting(var1) : true;
   }

   private File getNewFolderName() {
      File var1 = this.managedProject.getLocation().getParentFile();
      return new File(var1, this.newName);
   }

   private boolean overwriteExisting(File var1) {
      Answer var2 = Ask.continueOrCancel(
         null,
         "A file or folder with the path " + var1.getAbsolutePath() + " already exists and will be overwritten.\n\nDo you want to continue?",
         "Overwrite Existing?",
         Alert.Option.CANCEL,
         Alert.Type.WARNING
      );
      if (var2.isContinue()) {
         try {
            FileUtils.forceDelete(var1);
            return true;
         } catch (IOException var4) {
            ErrorLog.attention(ProjectRenamer.class, "Could not delete the existing path " + var1.getAbsolutePath(), var4);
         }
      }

      return false;
   }

   private void createBackup() {
      File var1 = this.manager.getBackupService().createWorkingBackup(this.managedProject.getProject());
      if (var1 != null) {
         String var2 = "A backup of the project before it was renamed was stored at:\n\n" + var1.getAbsolutePath();
         ApplicationMessageEvent.postInformationMessage(ProjectRenamer.class, var2);
      }
   }

   private void deleteOldLocation(File var1) {
      try {
         FileUtils.forceDelete(var1);
      } catch (IOException var3) {
         ErrorLog.log(ProjectRenamer.class, "Failed to delete old project @ " + var1.getAbsolutePath(), var3);
      }
   }
}
