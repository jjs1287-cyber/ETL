package org.etlforge.integration.client.ui.interchange.location.tempstore;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.TemporaryStorageLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.TemporaryStorageLocation;

public final class TemporaryStorageLocationPanelFactory {
   public static TemporaryStorageLocationPanel newPanel(TemporaryStorageLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      TemporaryStorageLocationPanel var2 = new TemporaryStorageLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(TemporaryStorageLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      TemporaryStorageLocationCache var2 = var1.getCache(TemporaryStorageLocationCache.class);
      if (var2 != null) {
         var0.setDataCache(var2);
      }
   }
}
