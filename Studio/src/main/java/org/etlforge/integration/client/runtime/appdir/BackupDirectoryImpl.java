package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.application.appdir.ReadMeGenerator;
import org.etlforge.integration.client.project.backup.BackupDirectory;

public final class BackupDirectoryImpl extends ApplicationDirectoryBase implements BackupDirectory {
   private static final String NAME = "backup";
   private static final String PERMANENT = "projects";
   private static final String WORKING = "working";

   public BackupDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "backup");
   }

   @Override
   public File getWorkingDirectory() {
      return this.createSubDir("working");
   }

   @Override
   public File getPermanentBackupDirectory() {
      return this.createSubDir("projects");
   }

   private File createSubDir(String var1) {
      File var2 = new File(this.getLocation(), var1);
      ReadMeGenerator.generate(var2);
      return var2;
   }
}
