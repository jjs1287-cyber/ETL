package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class MSCrmDeleteId extends IntegrationEntityId {
   private static final long serialVersionUID = 20120912L;

   public MSCrmDeleteId() {
   }

   public MSCrmDeleteId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.MSCrmDelete;
   }
}
