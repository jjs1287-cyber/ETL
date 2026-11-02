package com.etlforge.integration.client.project.share;

import org.etlforge.integration.data.entity.IntegrationEntity;

public class IdConflict implements Conflict {
   private final IntegrationEntity copiedEntity;
   private final IntegrationEntity existingEntity;

   public IdConflict(IntegrationEntity var1, IntegrationEntity var2) {
      if (!var1.getID().equals(var2.getID())) {
         throw new IllegalArgumentException("copiedEntity and existingEntity have different IDs - there is no conflict");
      }

      this.copiedEntity = var1;
      this.existingEntity = var2;
   }

   public IntegrationEntity getCopiedEntity() {
      return this.copiedEntity;
   }

   public IntegrationEntity getExistingEntity() {
      return this.existingEntity;
   }

   @Override
   public ConflictResolution resolve(ConflictResolver var1) {
      return var1.resolve(this);
   }
}
