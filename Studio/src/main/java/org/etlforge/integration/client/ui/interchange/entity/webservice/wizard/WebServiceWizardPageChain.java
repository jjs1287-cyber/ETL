package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.WebServiceCall;

public class WebServiceWizardPageChain implements WizardPageChain {
   private final WebServiceWizard wizard;
   private final WebServiceWizardStartPage startPage;
   private final WebServiceWizardOperationPage operationPage;
   private final WebServiceWizardDetailsPage detailsPage;

   public WebServiceWizardPageChain(WebServiceWizard var1, WebServiceCall var2, Folder var3) {
      this.wizard = var1;
      this.startPage = new WebServiceWizardStartPage(var1, var2, var3);
      this.operationPage = new WebServiceWizardOperationPage(var1);
      this.detailsPage = new WebServiceWizardDetailsPage(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.startPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.detailsPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.startPage) {
         this.operationPage.setWsdlFile(this.startPage.getWsdlFile());
         this.operationPage.setWebServiceInfo(this.startPage.getWebServiceInfo());
         this.setSection("Operation");
         return this.operationPage;
      } else if (var1 == this.operationPage) {
         this.detailsPage.setWebServiceInfo(this.operationPage.getWebServiceInfo());
         this.detailsPage.setOperationInfo(this.operationPage.getSelectedOperation());
         this.setSection("Details");
         return this.detailsPage;
      } else {
         return null;
      }
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.detailsPage) {
         this.setSection("Operation");
         return this.operationPage;
      } else if (var1 == this.operationPage) {
         this.setSection("WSDL");
         return this.startPage;
      } else {
         return null;
      }
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 == this.startPage || var1 == this.operationPage;
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 == this.operationPage || var1 == this.detailsPage;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.detailsPage && var1.canContinue();
   }
}
