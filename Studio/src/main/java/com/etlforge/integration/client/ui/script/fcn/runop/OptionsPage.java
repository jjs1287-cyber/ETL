package com.etlforge.integration.client.ui.script.fcn.runop;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class OptionsPage extends AbstractWizardPage {
   private final OptionsPageUi ui = new OptionsPageUi();

   public OptionsPage(Wizard var1) {
      super(var1, "Options");
      this.setCanContinue(true);
   }

   public boolean isSynchronous() {
      return this.ui.isSynchronous();
   }

   public boolean raiseErrorOnFailure() {
      return this.isSynchronous() && this.ui.raiseErrorOnFailure();
   }

   public boolean cancelChainOnFailure() {
      return this.isSynchronous() && this.ui.cancelChainOnFailure();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("");
      var1.add(InvisiblePanel.of(this.ui), true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }
}
