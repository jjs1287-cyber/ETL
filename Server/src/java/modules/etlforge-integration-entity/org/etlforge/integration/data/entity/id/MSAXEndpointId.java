package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class MSAXEndpointId extends IntegrationEntityId {
   private static final long serialVersionUID = 1L;

   public MSAXEndpointId() {
   }

   public MSAXEndpointId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.MSAXEndpoint;
   }
}
