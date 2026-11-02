package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class MSCrmEndpointId extends IntegrationEntityId {
   private static final long serialVersionUID = 1L;

   public MSCrmEndpointId() {
   }

   public MSCrmEndpointId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.MSCrmEndpoint;
   }
}
