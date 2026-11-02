package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import com.google.common.base.Preconditions;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.name.IllegalNameException;

final class WebServiceWizardStartPage extends AbstractWebServiceWizardPage {
   private final WebServiceWizardStartPageUi ui;
   private volatile WsdlProcessor wsdlProcessingJob;
   private WsdlFile wsdlFile;
   private WebServiceInfo webServiceCallInfo;

   public WebServiceWizardStartPage(WebServiceWizard var1, WebServiceCall var2, Folder var3) {
      super(var1, Strings.get("Wizard.StartPage.SubTitle"));
      Preconditions.checkNotNull(var2, "ws");
      Preconditions.checkNotNull(var3, "owner");
      this.setCanContinue(false);
      this.ui = new WebServiceWizardStartPageUi(this, var2, var3);
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
   }

   public WsdlFile getWsdlFile() {
      return this.wsdlFile;
   }

   public WebServiceInfo getWebServiceInfo() {
      return this.webServiceCallInfo;
   }

   @Override
   public boolean prepareForForwardNavigation() {
      this.updateWizardTitle();
      this.wsdlProcessingJob = new WsdlProcessor(this.getWizard(), this.ui);
      this.wsdlProcessingJob.setUseProgressDialog(100);
      this.wsdlProcessingJob.submit();

      try {
         this.wsdlProcessingJob.waitUntilDone();
         boolean var1 = this.wsdlProcessingJob.succeeded();
         if (var1) {
            this.wsdlFile = this.wsdlProcessingJob.getWsdlFile();
            this.webServiceCallInfo = this.wsdlProcessingJob.getParsedInfo();
         }

         return var1;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private void updateWizardTitle() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            WebServiceWizardStartPage.this.getWizard().setTitle(WebServiceWizardStartPage.this.ui.getName());
         }
      });
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected void dispose() {
      if (this.wsdlProcessingJob != null) {
         ApplicationUi.getJobManager().cancelJob(this.wsdlProcessingJob);
      }

      this.ui.dispose();
   }

   @Override
   public void applyToWebServiceCall(WebServiceCall var1) throws WizardPageException {
      try {
         var1.setName(this.ui.getName());
      } catch (IllegalNameException var3) {
         throw new WizardPageException(this, var3);
      }
   }

   @Override
   protected String getMainCaption() {
      return null;
   }

   @Override
   protected String getSubCaption() {
      return null;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null, null);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
