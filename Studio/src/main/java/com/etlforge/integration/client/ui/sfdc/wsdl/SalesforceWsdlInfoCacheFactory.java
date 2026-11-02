package com.etlforge.integration.client.ui.sfdc.wsdl;

import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfoCache;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfoFileCache;
import java.io.File;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheFactory;
import org.etlforge.application.cache.DataCacheService;

public final class SalesforceWsdlInfoCacheFactory implements DataCacheFactory<SalesforceWsdlInfoCache> {
   private final SalesforceWsdlInfoCache cache;

   public SalesforceWsdlInfoCacheFactory(File var1) {
      File var2 = new File(var1, SalesforceWsdlInfoCache.ID + "2");
      this.cache = new SalesforceWsdlInfoFileCache(var2);
   }

   public static SalesforceWsdlInfoCache getCache() {
      DataCacheService var0 = Application.getCacheService();
      SalesforceWsdlInfoCache var1 = var0.getCache(SalesforceWsdlInfoCache.class);
      if (var1 == null) {
         var1 = SalesforceWsdlInfoCache.NONE;
      }

      return var1;
   }

   @Override
   public Class<SalesforceWsdlInfoCache> getCacheType() {
      return SalesforceWsdlInfoCache.class;
   }

   public SalesforceWsdlInfoCache get() {
      return this.cache;
   }
}
