package org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice;

import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class HostedWebServicePage extends EntitySelectionPage<WebServiceCall> {
   public HostedWebServicePage(TransformationWizard var1) {
      super(var1, Strings.get("WebService.Hosted.SubTitle"), var1.getEntityExplorerSupport(), EntityType.WebServiceCall, WebServiceCall.class, null);
      this.setMainCaption(Strings.get("WebService.Hosted.MainCaption"));
      this.setSubCaption(Strings.get("WebService.Hosted.SubCaption"));
      this.setSelectorLabel(Strings.COMBO_LABEL);
   }

   public void applyToTransformation(Transformation var1) throws WizardPageException {
      WebServiceCall var2 = this.getSelectedEntity();
      if (var2 != null) {
         var1.setInputStructure(var2.getInputStructure());
         var1.setOutputStructure(var2.getOutputStructure());
      } else {
         throw new WizardPageException(this, "No WebServiceCall defined.");
      }
   }
}
