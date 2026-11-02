package org.etlforge.integration.client.ui.connector.mscrm.upsert.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderModel;

final class UpsertBuilderPage extends AbstractWizardPage {
   public UpsertBuilderPage(Wizard var1, QueryBuilderModel var2) {
      super(var1, "Build the Upsert data");
      this.setCanContinue(true);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      return var1.asCenterBorderLayout();
   }
}
