package org.etlforge.integration.client.project.backup;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.client.project.prefs.TemporaryBackupFrequencyPreference;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectBackupServiceImpl implements ProjectBackupService {
   private final BackupDirectory backupDir;
   private final Logger logger;
   private final Map<IntegrationProjectId, ProjectBackupCreator> deployBackupCreators;
   private final Map<IntegrationProjectId, TemporaryBackupCreator> tempBackupCreators;
   private long timeOfLastBackup;
   private final Object lock;

   public ProjectBackupServiceImpl(BackupDirectory var1) {
      this(var1, ApplicationLog.getLogger(ProjectBackupService.class, true));
   }

   public ProjectBackupServiceImpl(BackupDirectory var1, Logger var2) {
      this.backupDir = var1;
      this.logger = var2;
      this.deployBackupCreators = Maps.newHashMap();
      this.tempBackupCreators = Maps.newHashMap();
      this.lock = new Object();
   }

   @Override
   public BackupResult createBackup(IntegrationProject var1) {
      if (ProjectBackupPolicy.getPolicy().isEnabled()) {
         synchronized (this.lock) {
            ProjectBackupCreator var3 = this.getBackupCreator(var1);
            BackupResult var4 = var3.createBackup(var1);
            if (var4 != null) {
               this.timeOfLastBackup = System.currentTimeMillis();
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   @Override
   public void createWorkingBackupIfNeeded(IntegrationProject var1) {
      synchronized (this.lock) {
         if (this.isWorkingBackupNeeded()) {
            this.createWorkingBackup(var1);
         }
      }
   }

   private boolean isWorkingBackupNeeded() {
      long var1 = TemporaryBackupFrequencyPreference.inMillis();
      if (var1 <= 0L) {
         return false;
      }

      long var3 = System.currentTimeMillis() - this.timeOfLastBackup;
      return var3 > var1;
   }

   @Override
   public File createWorkingBackup(IntegrationProject var1) {
      synchronized (this.lock) {
         TemporaryBackupCreator var3 = this.tempBackupCreators.get(var1.getID());
         if (var3 == null) {
            FileStructure var4 = new FileStructure(this.backupDir);
            var3 = new TemporaryBackupCreator(var4.getTemporaryBackupLocation(var1.getID()), this.logger);
            this.tempBackupCreators.put(var1.getID(), var3);
         }

         File var7 = var3.createBackup(var1);
         if (var7 != null) {
            this.timeOfLastBackup = System.currentTimeMillis();
         }

         return var7;
      }
   }

   @Override
   public AvailableBackup[] getAvailableBackups(IntegrationProjectId var1) {
      AvailableBackupCollector var2 = new AvailableBackupCollector(this.backupDir, var1);
      return var2.collectBackups();
   }

   private ProjectBackupCreator getBackupCreator(IntegrationProject var1) {
      ProjectBackupCreator var2 = this.deployBackupCreators.get(var1.getID());
      if (var2 == null) {
         FileStructure var3 = new FileStructure(this.backupDir);
         var2 = new ProjectBackupCreator(var3.getPermanentBackupLocation(var1.getID()), this.logger);
         this.deployBackupCreators.put(var1.getID(), var2);
      }

      return var2;
   }
}
