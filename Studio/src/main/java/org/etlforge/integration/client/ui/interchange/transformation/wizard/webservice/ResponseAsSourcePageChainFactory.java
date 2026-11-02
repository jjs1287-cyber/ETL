package org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice;

import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class ResponseAsSourcePageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new ResponseAsSourcePageChainFactory.ChainImpl(var1);
   }

   private static final class ChainImpl extends WizardPageList implements TransformationStructurePageChain {
      private final EntitySelectionPage<WebServiceCall> page;

      public ChainImpl(TransformationWizard var1) {
         this.page = new EntitySelectionPage<>(
            var1, Strings.get("WebService.Source.SubTitle"), var1.getEntityExplorerSupport(), EntityType.WebServiceCall, WebServiceCall.class, null
         );
         this.page.setMainCaption(Strings.get("WebService.Source.MainCaption"));
         this.page.setSubCaption(Strings.get("WebService.Source.SubCaption"));
         this.page.setSelectorLabel(Strings.COMBO_LABEL);
         this.addPage(this.page);
      }

      @Override
      public void applyTo(Transformation var1) throws WizardPageException {
         WebServiceCall var2 = this.page.getSelectedEntity();
         if (var2 != null) {
            var1.setInputStructure(var2.getOutputStructure());
         } else {
            throw new WizardPageException(this.page, "No WebServiceCall defined.");
         }
      }

      @Override
      public void setOpposingStructureNature(WizardType.Nature var1) {
      }
   }
}
