package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class OperationId extends IntegrationEntityId {
   public OperationId() {
   }

   public OperationId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.Operation;
   }
}
