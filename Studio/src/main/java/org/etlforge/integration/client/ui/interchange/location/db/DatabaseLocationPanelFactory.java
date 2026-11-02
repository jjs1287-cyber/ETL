package org.etlforge.integration.client.ui.interchange.location.db;

import com.google.common.base.Preconditions;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.DatabaseLocationCache;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DatabaseLocation;

public final class DatabaseLocationPanelFactory {
   public static DatabaseLocationPanel newPanel(IntegrationEntity var0, DatabaseLocation var1, SourceTarget var2) {
      Preconditions.checkArgument(var0 instanceof Source || var0 instanceof Target, "locatable must be a Source or a Target");
      Preconditions.checkNotNull(var1, "location");
      Preconditions.checkNotNull(var2, "type");
      DatabaseLocationPanel var3 = new DatabaseLocationPanel(var0, var1, var2);
      setCache(var3);
      return var3;
   }

   private static void setCache(DatabaseLocationPanel var0) {
      DataCacheService var1 = Application.getCacheService();
      DatabaseLocationCache var2 = var1.getCache(DatabaseLocationCache.class);
      if (var2 != null) {
         var0.setDataCache(var2);
      }
   }

   private DatabaseLocationPanelFactory() {
   }
}
