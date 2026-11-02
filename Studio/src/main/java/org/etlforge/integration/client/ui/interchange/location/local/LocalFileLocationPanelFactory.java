package org.etlforge.integration.client.ui.interchange.location.local;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.LocalFileLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LocalFileLocation;

public final class LocalFileLocationPanelFactory {
   public static LocalFileLocationPanel newPanel(LocalFileLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      LocalFileLocationPanel var2 = new LocalFileLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(LocalFileLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      LocalFileLocationCache var2 = var1.getCache(LocalFileLocationCache.class);
      if (var2 != null) {
         var0.setCache(var2);
      }
   }
}
