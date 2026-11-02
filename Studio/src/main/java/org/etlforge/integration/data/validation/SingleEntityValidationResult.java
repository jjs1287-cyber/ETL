package org.etlforge.integration.data.validation;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class SingleEntityValidationResult implements EntityCollectionValidationResult {
   private final IntegrationEntity entity;
   private final ValidationResult result;

   public SingleEntityValidationResult(IntegrationEntity var1, ValidationResult var2) {
      this.entity = Preconditions.checkNotNull(var1, "entity");
      this.result = Preconditions.checkNotNull(var2, "result");
   }

   @Override
   public ImmutableSet<IntegrationEntity> getValidatedEntities() {
      return ImmutableSet.of(this.entity);
   }

   @Override
   public ValidationResult getValidationResult(IntegrationEntity var1) {
      Preconditions.checkArgument(var1 == this.entity, "This instance does not know about entity");
      return this.result;
   }

   @Override
   public Optional<IntegrationEntity> getRoot() {
      return Optional.of(this.entity);
   }

   @Override
   public ImmutableList<IntegrationEntity> getPathFromRoot(IntegrationEntity var1) {
      Preconditions.checkArgument(var1 == this.entity, "This instance does not know about entity");
      return ImmutableList.of(var1);
   }

   @Override
   public boolean isEverythingOk() {
      return this.status() == ValidationStatus.VALID;
   }

   @Override
   public boolean isFatal() {
      return this.result.isFatal();
   }

   @Override
   public ValidationStatus status() {
      return this.result.status();
   }
}
