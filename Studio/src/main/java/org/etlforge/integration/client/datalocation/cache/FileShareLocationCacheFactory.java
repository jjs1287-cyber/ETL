package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class FileShareLocationCacheFactory implements DataCacheFactory<FileShareLocationCache> {
   private final FileShareLocationCache cache;

   public FileShareLocationCacheFactory(File var1) {
      this.cache = new FileShareLocationFileCache(var1);
   }

   @Override
   public Class<FileShareLocationCache> getCacheType() {
      return FileShareLocationCache.class;
   }

   public FileShareLocationCache get() {
      return this.cache;
   }
}
