package org.etlforge.application.appdir;

import java.io.File;

public class DefaultApplicationDirectory extends ApplicationDirectoryBase implements ApplicationDirectory {
   private final LogDirectory logDirectory;
   private final CacheDirectory cacheDirectory;

   public DefaultApplicationDirectory(File var1) throws AppDirInitializationException {
      this(var1, new LogDirectoryImpl(var1));
   }

   public DefaultApplicationDirectory(File var1, LogDirectory var2) throws AppDirInitializationException {
      super(var1.getParentFile(), var1.getName());
      this.logDirectory = var2;
      this.cacheDirectory = new CacheDirectoryImpl(var1);
   }

   @Override
   public final CacheDirectory getCacheDirectory() {
      return this.cacheDirectory;
   }

   @Override
   public final LogDirectory getLogDirectory() {
      return this.logDirectory;
   }

   @Override
   public final void markAsPartOfApplicationDirectoryStructure(File var1) {
      ReadMeGenerator.generate(var1);
   }
}
