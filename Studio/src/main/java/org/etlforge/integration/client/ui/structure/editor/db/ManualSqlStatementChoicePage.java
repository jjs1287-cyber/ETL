package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.system.ThreeValuedBool;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ManualSqlStatementChoicePage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final DatabaseLocation dbLocation;
   private final Source sourceDb;
   private final ManualModeSwitch choice;
   private ThreeValuedBool updateSourceTableAvailable;
   private boolean singleTableOnly;

   public ManualSqlStatementChoicePage(Wizard var1, Source var2) {
      super(var1, "Use Manual SQL Statement?");
      this.sourceDb = var2;
      this.dbLocation = (DatabaseLocation)var2.getLocation();
      this.choice = new ManualModeSwitch();
      this.updateSourceTableAvailable = ThreeValuedBool.MAYBE;
      this.setCanContinue(true);
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
   }

   @Override
   public WizardPage getNextPage() {
      if (this.choice.isSelected()) {
         return new ManualSqlStatementPage(this.getWizard(), this.sourceDb, this.dbLocation);
      }

      DatabaseTableSelectionPage var1 = new DatabaseTableSelectionPage(
         this.getWizard(), this.dbLocation, this.sourceDb.getID(), null, SourceTarget.Source, this.singleTableOnly
      );
      var1.setUpdateSourceTableAvailable(this.updateSourceTableAvailable);
      return var1;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Manual SQL");
      var1.setInsets(0, 0, 20, 0);
      var1.add(InvisiblePanel.of(this.choice.getUi()), true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      this.choice.requestFocus();
   }

   public void setUpdateSourceTableAvailable(ThreeValuedBool var1) {
      this.updateSourceTableAvailable = var1;
   }

   public void setSingleTableOnly() {
      this.singleTableOnly = true;
   }
}
