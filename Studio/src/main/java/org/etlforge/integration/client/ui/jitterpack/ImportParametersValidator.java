package org.etlforge.integration.client.ui.jitterpack;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.data.ProjectNameValidator;
import org.etlforge.util.name.IllegalNameException;

final class ImportParametersValidator {
   private final ImportParameters params;

   public ImportParametersValidator(ImportParameters var1) {
      this.params = var1;
   }

   public boolean validate() {
      return this.validateProjectName();
   }

   private boolean validateProjectName() {
      String var1 = this.params.getNewProjectName();
      if (var1 != null) {
         try {
            new ProjectNameValidator().validateName(var1);
         } catch (IllegalNameException var3) {
            Alert.error(var3.getMessage(), "Invalid New Project Name");
            return false;
         }
      }

      return true;
   }
}

