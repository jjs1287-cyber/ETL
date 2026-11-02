package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;

final class OperationMessageLinkFactory {
   private final Operation operation;
   private final IntegrationEntityLookup dataLookup;

   public OperationMessageLinkFactory(Operation var1, IntegrationEntityLookup var2) {
      this.operation = var1;
      this.dataLookup = var2;
   }

   public EntityLink createFailureLink() {
      EmailMessageId var1 = this.operation.getFailureMessageId();
      if (var1 != null) {
         EmailMessage var2 = this.dataLookup.getEntity(var1, EmailMessage.class);
         if (var2 != null) {
            return OperationMessageLink.createFailureLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.EmailMessage, var1);
      }

      return null;
   }

   public EntityLink createSuccessAndFailureLink() {
      EmailMessageId var1 = this.operation.getSuccessMessageId();
      if (var1 != null) {
         EmailMessage var2 = this.dataLookup.getEntity(var1, EmailMessage.class);
         if (var2 != null) {
            return OperationMessageLink.createSuccessAndFailureLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.EmailMessage, var1);
      }

      return null;
   }

   public EntityLink createSuccessLink() {
      EmailMessageId var1 = this.operation.getSuccessMessageId();
      if (var1 != null) {
         EmailMessage var2 = this.dataLookup.getEntity(var1, EmailMessage.class);
         if (var2 != null) {
            return OperationMessageLink.createSuccessLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.EmailMessage, var1);
      }

      return null;
   }
}
