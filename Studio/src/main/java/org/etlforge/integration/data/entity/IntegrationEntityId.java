package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.IntegrationId;

public abstract class IntegrationEntityId extends IntegrationId {
   public IntegrationEntityId() {
   }

   public IntegrationEntityId(String var1) {
      super(var1);
   }

   public abstract EntityType getEntityType();
}
