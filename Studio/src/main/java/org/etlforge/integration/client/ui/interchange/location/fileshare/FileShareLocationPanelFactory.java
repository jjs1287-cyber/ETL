package org.etlforge.integration.client.ui.interchange.location.fileshare;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.FileShareLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileShareLocation;

public final class FileShareLocationPanelFactory {
   public static FileShareLocationPanel newPanel(FileShareLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      FileShareLocationPanel var2 = new FileShareLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(FileShareLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      FileShareLocationCache var2 = var1.getCache(FileShareLocationCache.class);
      if (var2 != null) {
         var0.setDataCache(var2);
      }
   }

   private FileShareLocationPanelFactory() {
   }
}
