package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class NetSuiteUpsertId extends IntegrationEntityId {
   private static final long serialVersionUID = 20121031L;

   public NetSuiteUpsertId() {
   }

   public NetSuiteUpsertId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.NetSuiteUpsert;
   }
}
