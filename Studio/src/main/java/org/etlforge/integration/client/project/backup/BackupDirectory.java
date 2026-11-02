package org.etlforge.integration.client.project.backup;

import java.io.File;

public interface BackupDirectory {
   File getLocation();

   File getWorkingDirectory();

   File getPermanentBackupDirectory();
}
