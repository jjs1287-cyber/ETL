package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import java.util.List;

public interface WizardConfiguration {
   List<WizardType> getSupportedInputTypes();

   List<WizardType> getSupportedOutputTypes();
}
