package org.etlforge.integration.data.validation;

import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DataValidationUtils {
   public static EntityCollectionValidationResult extractWarningsOnly(EntityCollectionValidationResult var0) {
      EntityCollectionValidationResultBuilder var1 = new EntityCollectionValidationResultBuilder();

      for (IntegrationEntity var3 : var0.getValidatedEntities()) {
         ValidationResult var4 = var0.getValidationResult(var3);
         if (var4.status() == ValidationStatus.WARNING) {
            var1.add(var3, var4);
         }
      }

      return var1.getResult();
   }

   private DataValidationUtils() {
   }
}
