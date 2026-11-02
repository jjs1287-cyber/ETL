package org.etlforge.integration.client.ui.deploy.validation;

import org.etlforge.integration.data.validation.EntityCollectionValidationResult;

public interface ValidationWarningDeployStrategy {
   ValidationWarningDeployStrategy ALWAYS_REJECT = new ValidationWarningDeployStrategy() {
      @Override
      public boolean allowDeploy(EntityCollectionValidationResult var1) {
         return false;
      }
   };
   ValidationWarningDeployStrategy ALWAYS_ALLOW = new ValidationWarningDeployStrategy() {
      @Override
      public boolean allowDeploy(EntityCollectionValidationResult var1) {
         return true;
      }
   };

   boolean allowDeploy(EntityCollectionValidationResult var1);
}
