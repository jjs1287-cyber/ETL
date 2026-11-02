package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.entity.valuecache.ClientValueCacheFactory;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;

public final class DataLocationValueCaches {
   private final ClientValueCacheFactory factory;
   private final Map<DataLocationType, ValueCache> caches = Maps.newHashMap();

   public DataLocationValueCaches(ClientValueCacheFactory var1) {
      this.factory = Preconditions.checkNotNull(var1, "factory");
   }

   public <T extends DataLocation> ValueCache<T> getCache(T var1) {
      ValueCache var2 = this.caches.get(var1.getDataLocationType());
      if (var2 == null) {
         var2 = this.createCache(var1);
         this.caches.put(var1.getDataLocationType(), var2);
      }

      return var2;
   }

   private <T extends DataLocation> ValueCache<T> createCache(T var1) {
      ImmutableSet var2 = var1.getNamesOfPropertiesToCache();
      return var2.isEmpty() ? ValueCache.EMPTY : this.factory.newValueCache(var1.getClass().getSimpleName(), var2);
   }
}
