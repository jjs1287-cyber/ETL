package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.structure.TextStructure;

public interface TreeMapperWizardContainer extends WizardDisplayer {
   void setWizard(Wizard var1);

   void handleNewDocumentRequest(TextStructure var1, IntegrationEntity var2);
}
