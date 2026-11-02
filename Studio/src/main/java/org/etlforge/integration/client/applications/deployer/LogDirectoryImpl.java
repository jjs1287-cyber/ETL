package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.application.log.LogFileRepository;

public final class LogDirectoryImpl extends ApplicationDirectoryBase implements LogDirectory {
   public LogDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "logs");
   }

   @Override
   public File getSessionLogFolder() {
      return this.getLocation();
   }

   @Override
   public File getSessionErrorLog() {
      return null;
   }

   @Override
   public LogFileRepository asRepository() {
      return null;
   }

   @Override
   public Level getSavedLogLevel(Logger var1) {
      return null;
   }

   @Override
   public void saveLogLevels(Logger... var1) {
   }
}
