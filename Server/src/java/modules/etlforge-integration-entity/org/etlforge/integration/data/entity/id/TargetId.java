package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class TargetId extends IntegrationEntityId {
   public TargetId() {
   }

   public TargetId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.Target;
   }
}
