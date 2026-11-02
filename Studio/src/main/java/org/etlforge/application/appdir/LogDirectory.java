package org.etlforge.application.appdir;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.log.LogFileRepository;

public interface LogDirectory {
   String SYSTEM_OUT_TO_CONSOLE = "SystemOutToConsole";
   String SYSTEM_ERR_TO_CONSOLE = "SystemErrToConsole";

   File getLocation();

   File getSessionLogFolder();

   File getSessionErrorLog();

   LogFileRepository asRepository();

   Level getSavedLogLevel(Logger var1);

   void saveLogLevels(Logger... var1);
}
