package org.etlforge.integration.client.ui.deploy.validation;

public final class DeployItemValidatorFactory {
   public static DeployItemValidator newPromptOnWarningValidator() {
      PromptingValidationWarningDeployStrategy var0 = new PromptingValidationWarningDeployStrategy();
      return new DeployItemValidator(var0);
   }

   private DeployItemValidatorFactory() {
   }
}
