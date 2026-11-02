package org.etlforge.integration.client.project.backup;

import java.io.File;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectBackupService {
   BackupResult createBackup(IntegrationProject var1);

   void createWorkingBackupIfNeeded(IntegrationProject var1);

   File createWorkingBackup(IntegrationProject var1);

   AvailableBackup[] getAvailableBackups(IntegrationProjectId var1);
}
