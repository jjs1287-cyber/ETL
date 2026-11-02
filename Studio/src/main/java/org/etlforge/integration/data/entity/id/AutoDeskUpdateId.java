package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class AutoDeskUpdateId extends IntegrationEntityId {
   private static final long serialVersionUID = 20120912L;

   public AutoDeskUpdateId() {
   }

   public AutoDeskUpdateId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.AutoDeskUpdate;
   }
}
