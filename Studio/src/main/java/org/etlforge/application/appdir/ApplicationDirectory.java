package org.etlforge.application.appdir;

import java.io.File;

public interface ApplicationDirectory {
   File getLocation();

   LogDirectory getLogDirectory();

   CacheDirectory getCacheDirectory();

   void markAsPartOfApplicationDirectoryStructure(File var1);
}
