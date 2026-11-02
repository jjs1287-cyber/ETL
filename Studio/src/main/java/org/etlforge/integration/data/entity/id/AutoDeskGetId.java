package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class AutoDeskGetId extends IntegrationEntityId {
   private static final long serialVersionUID = 20120912L;

   public AutoDeskGetId() {
   }

   public AutoDeskGetId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.AutoDeskGet;
   }
}
