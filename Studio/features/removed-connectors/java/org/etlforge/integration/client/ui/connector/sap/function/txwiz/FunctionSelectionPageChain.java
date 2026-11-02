package org.etlforge.integration.client.ui.connector.sap.function.txwiz;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;

final class FunctionSelectionPageChain extends WizardPageList {
   private final EntitySelectionPage<SapFunction> page;

   public FunctionSelectionPageChain(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      this.page = new EntitySelectionPage<>(var1, "SAP Function Selection Wizard", var2, EntityType.SapFunction, SapFunction.class, null);
      this.page.setMainCaption("Select an SAP Function Definition");
      this.page.setSelectorLabel("&Available SAP Function Definitions");
      this.addPage(this.page);
   }

   public void setSelectedFunction(SapFunction var1) {
      this.page.setSelectedEntityId(var1 == null ? null : var1.getID());
   }

   public SapFunction getSelectedFunction() {
      return this.page.getSelectedEntity();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.page && var1.canContinue();
   }
}
