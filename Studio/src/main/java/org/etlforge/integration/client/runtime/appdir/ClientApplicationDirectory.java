package org.etlforge.integration.client.runtime.appdir;

import org.etlforge.application.appdir.ApplicationDirectory;
import org.etlforge.integration.client.project.ProjectDirectory;
import org.etlforge.integration.client.project.backup.BackupDirectory;
import org.etlforge.integration.client.server.ServerDirectory;

public interface ClientApplicationDirectory extends ApplicationDirectory {
   ServerDirectory getServerDirectory();

   ProjectDirectory getProjectDirectory();

   BackupDirectory getBackupDirectory();
}
