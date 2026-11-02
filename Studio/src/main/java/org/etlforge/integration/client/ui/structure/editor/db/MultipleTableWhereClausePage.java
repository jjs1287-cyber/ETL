package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.database.info.DatabaseObject;

public final class MultipleTableWhereClausePage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final MultipleTableWhereClausePageUi ui;

   public MultipleTableWhereClausePage(Wizard var1, DatabaseObject[] var2) {
      super(var1, PackageResources.MultipleTableWhereClausePage.SUB_TITLE);
      Preconditions.checkArgument(var2.length > 0, "tables cannot be empty");
      this.ui = new MultipleTableWhereClausePageUi(var2);
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
      this.setCanContinue(true);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public WizardPage getNextPage() {
      return null;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      DbTranFilters var2 = this.ui.getDefinedFilters();
      var1.setHierarchicalFilters(var2);
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      DbTranFilters var2 = var1.getStructure().getHierarchicalFilters();
      this.ui.setFilters(var2);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.MultipleTableWhereClausePage.MAIN_CAPTION, PackageResources.MultipleTableWhereClausePage.SUB_CAPTION
      );
      var1.setTransparent(false);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
