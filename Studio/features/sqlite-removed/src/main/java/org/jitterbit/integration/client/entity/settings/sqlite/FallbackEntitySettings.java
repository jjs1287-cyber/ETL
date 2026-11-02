package org.jitterbit.integration.client.entity.settings.sqlite;

import com.google.common.collect.Maps;
import java.util.Map;
import org.jitterbit.integration.client.entity.settings.EntitySettings;
import org.jitterbit.integration.data.entity.EntityType;
import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.util.persist.BinaryPersistor;
import org.jitterbit.util.persist.Persistor;

final class FallbackEntitySettings implements EntitySettings {
   private final Map<EntityType, Persistor> classSettings = Maps.newHashMap();
   private final Map<IntegrationEntityId, Persistor> entitySettings = Maps.newHashMap();

   @Override
   public synchronized Persistor getClassSettings(EntityType var1) {
      Persistor var2 = this.classSettings.get(var1);
      if (var2 == null) {
         var2 = BinaryPersistor.newRoot(var1.name());
         this.classSettings.put(var1, var2);
      }

      return var2;
   }

   @Override
   public synchronized Persistor getEntitySettings(IntegrationEntity var1) {
      IntegrationEntityId var2 = var1.getID();
      Persistor var3 = this.entitySettings.get(var2);
      if (var3 == null) {
         var3 = BinaryPersistor.newRoot(var2.toString());
         this.entitySettings.put(var2, var3);
      }

      return var3;
   }

   @Override
   public synchronized void deleteSettings(IntegrationEntity var1) {
      this.entitySettings.remove(var1.getID());
   }

   @Override
   public void persist() {
   }
}
