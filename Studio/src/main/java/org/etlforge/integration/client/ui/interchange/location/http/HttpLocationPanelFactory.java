package org.etlforge.integration.client.ui.interchange.location.http;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.HttpLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.HttpLocation;

public abstract class HttpLocationPanelFactory {
   public static HttpLocationPanel newPanel(HttpLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      HttpLocationPanel var2 = new HttpLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(HttpLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      HttpLocationCache var2 = var1.getCache(HttpLocationCache.class);
      if (var2 != null) {
         var0.setCache(var2);
      }
   }

   private HttpLocationPanelFactory() {
   }
}
