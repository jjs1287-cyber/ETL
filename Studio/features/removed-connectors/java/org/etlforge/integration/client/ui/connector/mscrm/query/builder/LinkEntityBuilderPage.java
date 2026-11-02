package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.undo.UndoRedoController;

final class LinkEntityBuilderPage extends AbstractWizardPage {
   private final LinkEntityBuilderUi ui;
   private UndoRedoController undoRedo;

   public LinkEntityBuilderPage(Wizard var1, LinkEntityModel var2, QueryBuilderController var3) {
      super(var1, "Build Link Entity: " + var2.getTitle());
      this.ui = new LinkEntityBuilderUi(var2, var3);
      this.setCanContinue(true);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.undoRedo = UndoRedo.install(this.ui, var2);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected void dispose() {
      this.undoRedo.dispose();
   }
}
