package org.etlforge.integration.client.project.deploy;

import java.io.File;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.BackupResult;
import org.etlforge.integration.client.project.backup.ProjectBackupPolicy;
import org.etlforge.integration.data.project.IntegrationProject;

final class ProjectBackupHandler {
   private final ProjectManager manager;
   private final IntegrationProject project;
   private String tag;

   public ProjectBackupHandler(ProjectManager var1, IntegrationProject var2) {
      this.manager = var1;
      this.project = var2;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public void handleDeployResult(DeployResult var1) {
      if (var1.getCause() == null) {
         this.doBackup();
      }
   }

   private void doBackup() {
      ProjectBackupPolicy var1 = ProjectBackupPolicy.getPolicy();
      if (var1.isEnabled()) {
         if (!Thread.currentThread().isInterrupted()) {
            BackupResult var2 = this.createProjectBackup();
            if (var2 != null) {
               if (!Thread.currentThread().isInterrupted()) {
                  File var3 = var2.getBackupFolder();
                  if (var3 != null) {
                     this.uploadBackup(var3);
                  } else if (!var2.noBackupNeeded()) {
                     this.logFailureToDeployLog();
                  }
               }
            }
         }
      }
   }

   private BackupResult createProjectBackup() {
      try {
         DeployLog.LOG.info("Creating a Jitterpak backup to send to the server");
         return this.manager.getBackupService().createBackup(this.project);
      } catch (RuntimeException var2) {
         this.logFailure(var2);
         return null;
      }
   }

   private void uploadBackup(File var1) {
      if (!Thread.currentThread().isInterrupted()) {
         new ProjectBackupSender(this.manager).uploadBackup(this.project, var1, this.tag);
      }
   }

   private void logFailure(Throwable var1) {
      ErrorLog.log(ProjectBackupSender.class, "Failed to create a Jitterpak backup of the project.", var1);
      this.logFailureToDeployLog();
   }

   private void logFailureToDeployLog() {
      DeployLog.LOG.info("Failed to create the Jitterpak backup. Check the ErrorLog for more details.");
   }
}
