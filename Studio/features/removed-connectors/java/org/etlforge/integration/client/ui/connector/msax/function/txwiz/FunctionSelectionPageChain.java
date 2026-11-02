package org.etlforge.integration.client.ui.connector.msax.function.txwiz;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.EntityType;

final class FunctionSelectionPageChain extends WizardPageList {
   private final EntitySelectionPage<MSAXFunction> page;

   public FunctionSelectionPageChain(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      this.page = new EntitySelectionPage<>(var1, "MSAX Function Selection Wizard", var2, EntityType.MSAXFunction, MSAXFunction.class, null);
      this.page.setMainCaption("Select an MSAX Function Definition");
      this.page.setSelectorLabel("&Available MSAX Function Definitions");
      this.addPage(this.page);
   }

   public void setSelectedFunction(MSAXFunction var1) {
      this.page.setSelectedEntityId(var1 == null ? null : var1.getID());
   }

   public MSAXFunction getSelectedFunction() {
      return this.page.getSelectedEntity();
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.page && var1.canContinue();
   }
}
