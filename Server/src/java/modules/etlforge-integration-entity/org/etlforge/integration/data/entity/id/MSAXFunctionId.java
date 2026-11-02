package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class MSAXFunctionId extends IntegrationEntityId {
   public MSAXFunctionId() {
   }

   public MSAXFunctionId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.MSAXFunction;
   }
}
