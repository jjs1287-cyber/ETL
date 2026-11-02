package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.DefaultApplicationDirectory;
import org.etlforge.integration.client.project.ProjectDirectory;
import org.etlforge.integration.client.project.backup.BackupDirectory;
import org.etlforge.integration.client.server.ServerDirectory;

public final class ClientApplicationDirectoryImpl extends DefaultApplicationDirectory implements ClientApplicationDirectory {
   private final ServerDirectory serverDirectory;
   private final ProjectDirectory projectDirectory;
   private final BackupDirectory backupDirectory;

   public ClientApplicationDirectoryImpl(File var1, ServerDirectory var2, ProjectDirectory var3, BackupDirectory var4) throws AppDirInitializationException {
      super(var1);
      this.serverDirectory = var2;
      this.projectDirectory = var3;
      this.backupDirectory = var4;
   }

   @Override
   public ProjectDirectory getProjectDirectory() {
      return this.projectDirectory;
   }

   @Override
   public ServerDirectory getServerDirectory() {
      return this.serverDirectory;
   }

   @Override
   public BackupDirectory getBackupDirectory() {
      return this.backupDirectory;
   }

   @Override
   public String toString() {
      return this.getLocation().getAbsolutePath();
   }
}
