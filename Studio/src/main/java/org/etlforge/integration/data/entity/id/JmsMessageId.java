package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class JmsMessageId extends IntegrationEntityId {
   public JmsMessageId() {
   }

   public JmsMessageId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.JmsMessage;
   }
}
