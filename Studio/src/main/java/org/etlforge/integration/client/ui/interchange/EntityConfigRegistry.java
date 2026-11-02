package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.application.Application;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;

public final class EntityConfigRegistry implements EntityConfigLookup {
   private final Map<EntityType, EntityConfig> entityConfigs = Collections.synchronizedMap(new HashMap<>());

   public EntityConfigRegistry() {
   }

   public EntityConfigRegistry(Map<String, EntityConfig> var1) {
      for (EntityConfig var3 : var1.values()) {
         this.addConfig(var3);
      }
   }

   private void addConfig(EntityConfig var1) {
      this.entityConfigs.put(var1.getType(), var1);
   }

   public void loadAdditionalEntityConfigs() {
      for (EntityConfig var2 : Application.getBeansOfType(EntityConfig.class)) {
         this.addConfig(var2);
      }
   }

   @Override
   public EntityConfig getEntityConfig(EntityType var1) {
      EntityConfig var2 = this.entityConfigs.get(var1);
      return var2 != null ? var2 : EntityConfig.UNCONFIGURED;
   }

   public ImmutableSet<? extends EntityConfig> allConfigs() {
      return ImmutableSet.copyOf(this.entityConfigs.values());
   }
}
