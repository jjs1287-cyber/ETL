package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderController;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderRootModel;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.QueryBuilderUi;
import org.etlforge.integration.client.ui.connector.mscrm.query.builder.UndoRedo;
import org.etlforge.ui.undo.UndoRedoController;

final class QueryBuilderPage extends AbstractWizardPage {
   private final QueryBuilderUi ui;
   private UndoRedoController undoRedo;

   public QueryBuilderPage(Wizard var1, QueryBuilderRootModel var2, QueryBuilderController var3) {
      super(var1, "Build the Query: " + var2.getTitle());
      var3.setWaitService(this.getWaitService());
      this.ui = new QueryBuilderUi(var2, var3);
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
   protected void dispose() {
      this.undoRedo.dispose();
   }
}
