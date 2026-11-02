package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class LocalFileLocationCacheFactory implements DataCacheFactory<LocalFileLocationCache> {
   private final LocalFileLocationCache cache;

   public LocalFileLocationCacheFactory(File var1) {
      this.cache = new LocalFileLocationFileCache(var1);
   }

   @Override
   public Class<LocalFileLocationCache> getCacheType() {
      return LocalFileLocationCache.class;
   }

   public LocalFileLocationCache get() {
      return this.cache;
   }
}
