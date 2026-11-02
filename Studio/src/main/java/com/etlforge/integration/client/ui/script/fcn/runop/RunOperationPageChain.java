package com.etlforge.integration.client.ui.script.fcn.runop;

import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.data.entity.Operation;

final class RunOperationPageChain extends WizardPageList {
   private final OperationSelectionPage opSelectionPage;
   private final OptionsPage optionsPage;

   public RunOperationPageChain(Wizard var1, List<Operation> var2) {
      this.opSelectionPage = new OperationSelectionPage(var1, var2);
      this.optionsPage = new OptionsPage(var1);
      this.addPage(this.opSelectionPage);
      this.addPage(this.optionsPage);
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return true;
   }

   @Override
   protected String getSectionForPage(WizardPage var1) {
      if (var1 == this.opSelectionPage) {
         return "Operation";
      } else {
         return var1 == this.optionsPage ? "Options" : null;
      }
   }

   public Operation getSelectedOperation() {
      return this.opSelectionPage.getSelectedOperation();
   }

   public boolean isSynchronous() {
      return this.optionsPage.isSynchronous();
   }

   public boolean raiseErrorOnFailure() {
      return this.optionsPage.raiseErrorOnFailure();
   }

   public boolean cancelChainOnFailure() {
      return this.optionsPage.cancelChainOnFailure();
   }
}
