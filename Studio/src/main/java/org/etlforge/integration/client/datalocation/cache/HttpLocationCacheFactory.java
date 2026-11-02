package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class HttpLocationCacheFactory implements DataCacheFactory<HttpLocationCache> {
   private final HttpLocationCache cache;

   public HttpLocationCacheFactory(File var1) {
      this.cache = new HttpLocationFileCache(var1);
   }

   @Override
   public Class<HttpLocationCache> getCacheType() {
      return HttpLocationCache.class;
   }

   public HttpLocationCache get() {
      return this.cache;
   }
}
