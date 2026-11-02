package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.data.entity.WebServiceCall;

public interface WebServiceWizardPage extends WizardPage {
   void applyToWebServiceCall(WebServiceCall var1) throws WizardPageException;
}
