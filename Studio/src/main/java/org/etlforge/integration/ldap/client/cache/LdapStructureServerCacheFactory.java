package org.etlforge.integration.ldap.client.cache;

import java.io.File;
import org.etlforge.application.cache.DataCacheFactory;

public final class LdapStructureServerCacheFactory implements DataCacheFactory<LdapStructureServerCache> {
   private final LdapStructureServerCache cache;

   public LdapStructureServerCacheFactory(File var1) {
      this.cache = new LdapStructureServerCacheImpl(var1);
   }

   @Override
   public Class<LdapStructureServerCache> getCacheType() {
      return LdapStructureServerCache.class;
   }

   public LdapStructureServerCache get() {
      return this.cache;
   }
}
