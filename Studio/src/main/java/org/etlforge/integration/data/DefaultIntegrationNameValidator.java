package org.etlforge.integration.data;

import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;

public class DefaultIntegrationNameValidator implements NameValidator {
   public static final int MAX_LENGTH = 256;

   @Override
   public void validateName(String var1) throws IllegalNameException {
      if (var1 != null && var1.trim().length() != 0) {
         if (var1.length() > 256) {
            throw new IllegalNameException("The name cannot be longer than 256 characters.");
         }
      } else {
         throw new IllegalNameException("The name cannot be empty.");
      }
   }
}
