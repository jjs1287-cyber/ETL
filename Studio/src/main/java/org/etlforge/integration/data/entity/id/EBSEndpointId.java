package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class EBSEndpointId extends IntegrationEntityId {
   private static final long serialVersionUID = 1L;

   public EBSEndpointId() {
   }

   public EBSEndpointId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.EBSEndpoint;
   }
}
