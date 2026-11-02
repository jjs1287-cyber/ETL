package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class HttpEndpointId extends IntegrationEntityId {
   public HttpEndpointId() {
   }

   public HttpEndpointId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.HttpEndpoint;
   }
}
