package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class LdapLocationCacheFactory implements DataCacheFactory<LdapLocationCache> {
   private final LdapLocationCache cache;

   public LdapLocationCacheFactory(File var1) {
      this.cache = new LdapLocationFileCache(var1);
   }

   @Override
   public Class<LdapLocationCache> getCacheType() {
      return LdapLocationCache.class;
   }

   public LdapLocationCache get() {
      return this.cache;
   }
}
