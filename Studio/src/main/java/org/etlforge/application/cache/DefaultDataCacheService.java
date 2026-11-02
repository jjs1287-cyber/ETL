package org.etlforge.application.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public final class DefaultDataCacheService implements DataCacheService {
   private final ConcurrentMap<Class<? extends DataCache>, DataCacheFactory> factoryLookup = Maps.newConcurrentMap();

   public DefaultDataCacheService() {
   }

   public DefaultDataCacheService(List<? extends DataCacheFactory<?>> var1) {
      for (DataCacheFactory var3 : var1) {
         this.factoryLookup.put(var3.getCacheType(), var3);
      }
   }

   @Override
   public <T extends DataCache> T getCache(Class<T> var1) {
      DataCacheFactory var2 = this.factoryLookup.get(var1);
      if (var2 == null) {
         return null;
      }

      DataCache var3 = var2.get();
      return (T)var1.cast(var3);
   }

   @Override
   public <T extends DataCache> void registerFactory(DataCacheFactory<T> var1) {
      Preconditions.checkNotNull(var1, "factory");
      this.factoryLookup.put(var1.getCacheType(), var1);
   }
}
