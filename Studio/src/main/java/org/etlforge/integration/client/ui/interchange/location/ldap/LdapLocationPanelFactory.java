package org.etlforge.integration.client.ui.interchange.location.ldap;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.LdapLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LdapLocation;

public final class LdapLocationPanelFactory {
   public static LdapLocationPanel newPanel(LdapLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      LdapLocationPanel var2 = new LdapLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(LdapLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      LdapLocationCache var2 = var1.getCache(LdapLocationCache.class);
      if (var2 != null) {
         var0.setCache(var2);
      }
   }

   private LdapLocationPanelFactory() {
   }
}
