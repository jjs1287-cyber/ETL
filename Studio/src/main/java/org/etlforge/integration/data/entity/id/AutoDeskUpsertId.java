package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class AutoDeskUpsertId extends IntegrationEntityId {
   private static final long serialVersionUID = 20121031L;

   public AutoDeskUpsertId() {
   }

   public AutoDeskUpsertId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.AutoDeskUpsert;
   }
}
