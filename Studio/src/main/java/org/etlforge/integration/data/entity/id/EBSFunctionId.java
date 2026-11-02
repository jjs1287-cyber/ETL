package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class EBSFunctionId extends IntegrationEntityId {
   public EBSFunctionId() {
   }

   public EBSFunctionId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.EBSFunction;
   }
}
