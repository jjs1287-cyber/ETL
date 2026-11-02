package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class MSGPUpdateId extends IntegrationEntityId {
   private static final long serialVersionUID = 20120912L;

   public MSGPUpdateId() {
   }

   public MSGPUpdateId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.MSGPUpdate;
   }
}
