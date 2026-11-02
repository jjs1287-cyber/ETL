package org.etlforge.integration.client.ui.connector.ebs.function.txwiz;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;

final class FunctionSelectionPageChain extends WizardPageList {
   private final EntitySelectionPage<EBSFunction> page;

   public FunctionSelectionPageChain(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      this.page = new EntitySelectionPage<>(var1, "EBS Function Selection Wizard", var2, EntityType.EBSFunction, EBSFunction.class, null);
      this.page.setMainCaption("Select an EBS Function Definition");
      this.page.setSelectorLabel("&Available EBS Function Definitions");
      this.addPage(this.page);
   }

   public void setSelectedFunction(EBSFunction var1) {
      this.page.setSelectedEntityId(var1 == null ? null : var1.getID());
   }

   public EBSFunction getSelectedFunction() {
      return this.page.getSelectedEntity();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.page && var1.canContinue();
   }
}
