package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.datalocation.cache.DataLocationCache;
import org.etlforge.integration.client.datalocation.cache.DatabaseLocationCache;
import org.etlforge.integration.client.datalocation.cache.FileShareLocationCache;
import org.etlforge.integration.client.datalocation.cache.FtpLocationCache;
import org.etlforge.integration.client.datalocation.cache.HttpLocationCache;
import org.etlforge.integration.client.datalocation.cache.LdapLocationCache;
import org.etlforge.integration.client.datalocation.cache.LocalFileLocationCache;
import org.etlforge.integration.client.datalocation.cache.TemporaryStorageLocationCache;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.data.project.IntegrationProject;

final class ValueCacheUpdater {
   private final EntityConfigRegistry entityConfigs;

   public ValueCacheUpdater(EntityConfigRegistry var1) {
      this.entityConfigs = var1;
   }

   public void update(final IntegrationProject var1) {
      Thread var2 = new Thread(new Runnable() {
         @Override
         public void run() {
            ValueCacheUpdater.this.updateImpl(var1);
         }
      });
      var2.setPriority(3);
      var2.start();
   }

   private void updateImpl(IntegrationProject var1) {
      for (EntityConfig var3 : this.entityConfigs.allConfigs()) {
         var3.updateValueCache(var1);
      }

      this.updateDataLocationCaches(var1);
   }

   private void updateDataLocationCaches(IntegrationProject var1) {
      this.updateCache(var1, FileShareLocationCache.class);
      this.updateCache(var1, DatabaseLocationCache.class);
      this.updateCache(var1, FtpLocationCache.class);
      this.updateCache(var1, LdapLocationCache.class);
      this.updateCache(var1, HttpLocationCache.class);
      this.updateCache(var1, TemporaryStorageLocationCache.class);
      this.updateCache(var1, LocalFileLocationCache.class);
   }

   private <T extends DataLocationCache> void updateCache(IntegrationProject var1, Class<T> var2) {
      DataCacheService var3 = Application.getCacheService();
      DataLocationCache var4 = var3.getCache(var2);
      if (var4 != null) {
         var4.update(var1);
      }
   }
}
