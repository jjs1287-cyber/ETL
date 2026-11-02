package org.etlforge.integration.ldap.client.cache;

import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.data.location.LdapLocation;

public final class LdapLocationStructureCacheAccessor {
   public static LdapStructureCache getCache(LdapLocation var0) {
      DataCacheService var1 = Application.getCacheService();
      LdapStructureServerCache var2 = var1.getCache(LdapStructureServerCache.class);
      return var2.getCache(var0.getServer());
   }

   private LdapLocationStructureCacheAccessor() {
   }
}
