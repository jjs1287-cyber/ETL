package org.etlforge.integration.client.ui.structure.editor.json;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.structure.JsonStructure;

public interface JsonStructureWizardPage extends WizardPage {
   void applyTo(JsonStructure var1);

   JsonStructure getDefinedStructure();
}
