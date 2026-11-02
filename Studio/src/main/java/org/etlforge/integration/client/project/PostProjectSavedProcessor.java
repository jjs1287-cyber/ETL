package org.etlforge.integration.client.project;

import java.util.concurrent.TimeUnit;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.backup.ProjectBackupService;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;

final class PostProjectSavedProcessor {
   private final ProjectBackupService backupService;
   private final IntegrationProject project;
   private final ProjectFile projectFile;
   private long timeOfLastBackupCheck;
   private final Object lock;

   public PostProjectSavedProcessor(IntegrationProject var1, ProjectBackupService var2, ProjectFile var3) {
      this.project = var1;
      this.backupService = var2;
      this.projectFile = var3;
      this.lock = new Object();
      this.timeOfLastBackupCheck = -1L;
   }

   public void run(boolean var1) {
      synchronized (this.lock) {
         this.updateProjectFile();
         if (var1) {
            this.doBackupIfNeeded();
         }
      }
   }

   private void updateProjectFile() {
      try {
         ProjectUtils.saveProjectFile(this.project, this.projectFile);
      } catch (InterchangeSavingException var2) {
         var2.printStackTrace();
      }
   }

   private void doBackupIfNeeded() {
      if (!this.project.isTemporaryProject()) {
         long var1 = System.currentTimeMillis();
         if (var1 - this.timeOfLastBackupCheck >= 5000L) {
            this.timeOfLastBackupCheck = var1;
            this.startBackup();
         }
      }
   }

   private void startBackup() {
      ApplicationWorker.SafeRunnable var1 = new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            try {
               TimeUnit.SECONDS.sleep(1L);
               PostProjectSavedProcessor.this.backupService.createWorkingBackupIfNeeded(PostProjectSavedProcessor.this.project);
            } catch (InterruptedException var2) {
               Thread.currentThread().interrupt();
            }
         }
      };
      Application.getWorker().submitForParallel(var1);
   }
}
