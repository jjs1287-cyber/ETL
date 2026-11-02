package org.etlforge.application.appdir;

import java.io.File;

final class CacheDirectoryImpl extends ApplicationDirectoryBase implements CacheDirectory {
   private static final String NAME = "cache";

   CacheDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "cache");
   }
}
