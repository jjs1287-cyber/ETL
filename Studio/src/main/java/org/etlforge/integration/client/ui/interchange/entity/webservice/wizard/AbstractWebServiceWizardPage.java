package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import org.etlforge.application.ui.wizard.AbstractWizardPage;

abstract class AbstractWebServiceWizardPage extends AbstractWizardPage implements WebServiceWizardPage {
   public AbstractWebServiceWizardPage(WebServiceWizard var1) {
      this(var1, "");
   }

   public AbstractWebServiceWizardPage(WebServiceWizard var1, String var2) {
      super(var1, var2);
      this.setHelpId("Create_a_Web_Service_Method");
   }

   public WebServiceWizard getWizard() {
      return (WebServiceWizard)super.getWizard();
   }

   protected abstract String getMainCaption();

   protected abstract String getSubCaption();
}
