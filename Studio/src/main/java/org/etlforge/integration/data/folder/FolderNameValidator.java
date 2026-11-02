package org.etlforge.integration.data.folder;

import org.etlforge.integration.data.DefaultIntegrationNameValidator;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;

public final class FolderNameValidator implements NameValidator {
   private final NameValidator defaultValidator = new DefaultIntegrationNameValidator();

   @Override
   public void validateName(String var1) throws IllegalNameException {
      this.defaultValidator.validateName(var1);
      if (var1.toLowerCase().startsWith("etlforge connect")) {
         throw new IllegalNameException("\"ETLForge Connect\" is a reserved folder name.");
      }
   }
}
