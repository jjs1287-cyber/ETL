package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.integration.client.project.backup.BackupDirectory;

public final class BackupDirectoryImpl extends ApplicationDirectoryBase implements BackupDirectory {
   public BackupDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "backup");
   }

   @Override
   public File getWorkingDirectory() {
      throw new UnsupportedOperationException();
   }

   @Override
   public File getPermanentBackupDirectory() {
      throw new UnsupportedOperationException();
   }
}
