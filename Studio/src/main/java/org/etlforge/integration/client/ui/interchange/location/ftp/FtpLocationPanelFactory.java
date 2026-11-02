package org.etlforge.integration.client.ui.interchange.location.ftp;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.FtpLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FtpLocation;

public final class FtpLocationPanelFactory {
   public static FtpLocationPanel newPanel(FtpLocation var0, SourceTarget var1) {
      Preconditions.checkNotNull(var0, "loc");
      Preconditions.checkNotNull(var1, "type");
      FtpLocationPanel var2 = new FtpLocationPanel(var0, var1);
      setCache(var2);
      return var2;
   }

   private static void setCache(FtpLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      FtpLocationCache var2 = var1.getCache(FtpLocationCache.class);
      if (var2 != null) {
         var0.setDataCache(var2);
      }
   }

   private FtpLocationPanelFactory() {
   }
}
