package org.etlforge.integration.client.entity.settings;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

public final class InMemoryEntitySettings implements EntitySettings {
   private final LoadingCache<IntegrationEntityId, Persistor> cache = CacheBuilder.newBuilder().build(new CacheLoader<IntegrationEntityId, Persistor>() {
      public Persistor load(IntegrationEntityId var1) throws Exception {
         return XmlPersistor.newRoot(var1.toString());
      }
   });

   @Override
   public void persist() {
   }

   @Override
   public Persistor getEntitySettings(IntegrationEntity var1) {
      return this.cache.getUnchecked(var1.getID());
   }

   @Override
   public Persistor getClassSettings(EntityType var1) {
      return XmlPersistor.newRoot(var1.name());
   }

   @Override
   public void deleteSettings(IntegrationEntity var1) {
      this.cache.invalidate(var1.getID());
   }
}
