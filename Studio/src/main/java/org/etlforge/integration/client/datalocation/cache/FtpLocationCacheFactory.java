package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class FtpLocationCacheFactory implements DataCacheFactory<FtpLocationCache> {
   private final FtpLocationCache cache;

   public FtpLocationCacheFactory(File var1) {
      this.cache = new FtpLocationFileCache(var1);
   }

   @Override
   public Class<FtpLocationCache> getCacheType() {
      return FtpLocationCache.class;
   }

   public FtpLocationCache get() {
      return this.cache;
   }
}
