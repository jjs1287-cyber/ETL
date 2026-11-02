package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.project.backup.BackupResult;
import org.etlforge.integration.client.project.backup.ProjectBackupService;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;

public final class BackupServiceImpl implements ProjectBackupService {
   @Override
   public BackupResult createBackup(IntegrationProject var1) {
      return null;
   }

   @Override
   public void createWorkingBackupIfNeeded(IntegrationProject var1) {
   }

   @Override
   public File createWorkingBackup(IntegrationProject var1) {
      return null;
   }

   @Override
   public AvailableBackup[] getAvailableBackups(IntegrationProjectId var1) {
      return new AvailableBackup[0];
   }
}
