package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.awt.EventQueue;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class WebServiceWizardOperationPage extends AbstractWebServiceWizardPage {
   private WebServiceInfo webServiceInfo;
   private WsdlFile wsdlFile;
   private WebServiceOperationInfo currentOpInfo;
   private final WebServiceWizardOperationPageUi ui = new WebServiceWizardOperationPageUi(this);

   public WebServiceWizardOperationPage(WebServiceWizard var1) {
      super(var1, lookupSubTitle());
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
   }

   public void setWsdlFile(WsdlFile var1) {
      this.wsdlFile = var1;
   }

   public void setWebServiceInfo(WebServiceInfo var1) {
      this.webServiceInfo = var1;
      this.ui.setWebServiceInfo(var1);
   }

   WebServiceWizardOperationPageUi getUi() {
      return this.ui;
   }

   @Override
   public void applyToWebServiceCall(WebServiceCall var1) throws WizardPageException {
      WebServiceOperationInfo var2 = this.getSelectedOperation();
      assert var2 != null;
      var1.setOperationInfo(var2);
      var1.setWsdlFile(this.wsdlFile);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   public WebServiceOperationInfo getSelectedOperation() {
      return this.ui.getSelectedOperation();
   }

   public WebServiceInfo getWebServiceInfo() {
      return this.webServiceInfo;
   }

   @Override
   protected String getMainCaption() {
      return Strings.get("Wizard.OperationPage.MainCaption");
   }

   @Override
   protected String getSubCaption() {
      return Strings.get("Wizard.OperationPage.SubCaption");
   }

   private static String lookupSubTitle() {
      return Strings.get("Wizard.OperationPage.SubTitle");
   }

   void onCallStructuresRetrieved(final WebServiceOperationInfo var1, final WebServiceCallStructures var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WebServiceWizardOperationPage.this.getWizard().getStructureCache().add(var1, var2);
            WebServiceWizardOperationPage.this.ui.displayTreeStructures(var2);
            WebServiceWizardOperationPage.this.stopWait();
         }
      });
   }

   synchronized WebServiceOperationInfo getCurrentOp() {
      return this.currentOpInfo;
   }

   synchronized void setCurrentOpInfo(WebServiceOperationInfo var1) {
      this.currentOpInfo = var1;
      this.setCanContinue(var1 != null);
      if (var1 != null) {
         this.webServiceInfo.setWebServiceURL(var1.getProperty("WebService_Url"));
      }

      this.ui.displayOperationInfo(var1);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getMainCaption(), this.getSubCaption());
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
