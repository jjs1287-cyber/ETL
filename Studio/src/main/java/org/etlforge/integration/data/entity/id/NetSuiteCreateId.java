package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class NetSuiteCreateId extends IntegrationEntityId {
   public NetSuiteCreateId() {
   }

   public NetSuiteCreateId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.NetSuiteCreate;
   }
}
