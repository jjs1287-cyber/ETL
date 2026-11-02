package org.etlforge.integration.client.connect.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;

public final class SummaryPage extends AbstractWizardPage {
   private final SummaryPageUi ui;

   public SummaryPage(AbstractConnectWizard<?, ?> var1, Folders var2) {
      super(var1, "Summary");
      this.ui = new SummaryPageUi(var1.getOperation(), var2, var1.getName());
      this.ui.addValidationListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            SummaryPage.this.updateCanContinue();
         }
      });
      this.updateCanContinue();
   }

   private void updateCanContinue() {
      this.setCanContinue(this.ui.isValid());
   }

   public String getWizardName() {
      return this.ui.getName();
   }

   public boolean isAutoMapSelected() {
      return this.ui.isAutoMapSelected();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null);
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }
}
