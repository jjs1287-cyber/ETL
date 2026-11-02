package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class DatabaseLocationCacheFactory implements DataCacheFactory<DatabaseLocationCache> {
   private final DatabaseLocationCache cache;

   public DatabaseLocationCacheFactory(File var1) {
      this.cache = new DatabaseLocationFileCache(var1);
   }

   @Override
   public Class<DatabaseLocationCache> getCacheType() {
      return DatabaseLocationCache.class;
   }

   public DatabaseLocationCache get() {
      return this.cache;
   }
}
