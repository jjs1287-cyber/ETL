package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class TemporaryStorageLocationCacheFactory implements DataCacheFactory<TemporaryStorageLocationCache> {
   private final TemporaryStorageLocationCache cache;

   public TemporaryStorageLocationCacheFactory(File var1) {
      this.cache = new TemporaryStorageLocationFileCache(var1);
   }

   @Override
   public Class<TemporaryStorageLocationCache> getCacheType() {
      return TemporaryStorageLocationCache.class;
   }

   public TemporaryStorageLocationCache get() {
      return this.cache;
   }
}
