package org.etlforge.integration.data.validation;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityCollectionValidationResult {
   EntityCollectionValidationResult EMPTY = new EntityCollectionValidationResult() {
      @Override
      public ImmutableSet<IntegrationEntity> getValidatedEntities() {
         return ImmutableSet.of();
      }

      @Override
      public ValidationResult getValidationResult(IntegrationEntity var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Optional<IntegrationEntity> getRoot() {
         return Optional.absent();
      }

      @Override
      public ImmutableList<IntegrationEntity> getPathFromRoot(IntegrationEntity var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean isEverythingOk() {
         return true;
      }

      @Override
      public ValidationStatus status() {
         return ValidationStatus.VALID;
      }

      @Override
      public boolean isFatal() {
         return false;
      }
   };

   ImmutableSet<IntegrationEntity> getValidatedEntities();

   ValidationResult getValidationResult(IntegrationEntity var1);

   Optional<IntegrationEntity> getRoot();

   ImmutableList<IntegrationEntity> getPathFromRoot(IntegrationEntity var1);

   boolean isEverythingOk();

   boolean isFatal();

   ValidationStatus status();
}
