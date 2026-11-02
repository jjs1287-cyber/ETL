package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;

final class OperationOperationLinkFactory {
   private final Operation operation;
   private final IntegrationEntityLookup dataLookup;

   public OperationOperationLinkFactory(Operation var1, IntegrationEntityLookup var2) {
      this.operation = var1;
      this.dataLookup = var2;
   }

   public EntityLink createFailureLink() {
      OperationId var1 = this.operation.getFailureOperationId();
      if (var1 != null) {
         Operation var2 = this.dataLookup.getEntity(var1, Operation.class);
         if (var2 != null) {
            return OperationOperationLink.createFailureLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.Operation, var1);
      }

      return null;
   }

   public EntityLink createSuccessAndFailureLink() {
      OperationId var1 = this.operation.getSuccessOperationId();
      if (var1 != null) {
         Operation var2 = this.dataLookup.getEntity(var1, Operation.class);
         if (var2 != null) {
            return OperationOperationLink.createSuccessAndFailureLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.Operation, var1);
      }

      return null;
   }

   public EntityLink createSuccessLink() {
      OperationId var1 = this.operation.getSuccessOperationId();
      if (var1 != null) {
         Operation var2 = this.dataLookup.getEntity(var1, Operation.class);
         if (var2 != null) {
            return OperationOperationLink.createSuccessLink(var2, this.operation);
         }

         LinkUtils.logMissingLink(this.operation, EntityType.Operation, var1);
      }

      return null;
   }
}
