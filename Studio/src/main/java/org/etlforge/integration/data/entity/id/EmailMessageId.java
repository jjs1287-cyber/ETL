package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class EmailMessageId extends IntegrationEntityId {
   public EmailMessageId() {
   }

   public EmailMessageId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.EmailMessage;
   }
}
