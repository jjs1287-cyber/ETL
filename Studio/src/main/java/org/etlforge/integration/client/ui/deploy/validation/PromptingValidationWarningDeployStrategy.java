package org.etlforge.integration.client.ui.deploy.validation;

import org.etlforge.integration.data.validation.EntityCollectionValidationResult;

public class PromptingValidationWarningDeployStrategy implements ValidationWarningDeployStrategy {
   @Override
   public boolean allowDeploy(EntityCollectionValidationResult var1) {
      String var2 = Strings.get("Deploy.Validation.WarningPrompter.Title");
      String var3 = Strings.get("Deploy.Validation.WarningPrompter.Caption");
      String var4 = Strings.get("Deploy.Validation.WarningPrompter.Message");
      ResultDisplayer var5 = new ResultDisplayer(var1, var2, var3, var4);
      return var5.promptForWarning();
   }
}
