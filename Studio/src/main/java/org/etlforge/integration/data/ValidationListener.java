package org.etlforge.integration.data;

import org.etlforge.integration.data.validation.ValidationResult;

public interface ValidationListener {
   void validationComplete(IntegrationData var1, ValidationResult var2);
}
