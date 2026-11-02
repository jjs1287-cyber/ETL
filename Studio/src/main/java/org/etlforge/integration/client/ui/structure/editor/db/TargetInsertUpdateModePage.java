package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.info.DatabaseObject;

public final class TargetInsertUpdateModePage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final TargetInsertUpdateModeUi ui;

   public TargetInsertUpdateModePage(Wizard var1, DatabaseObject[] var2) {
      super(var1, PackageResources.TargetInsertUpdateModePage.SUB_TITLE);
      this.ui = new TargetInsertUpdateModeUi(var2);
      this.setCanContinue(true);
   }

   @Override
   public WizardPage getNextPage() {
      return null;
   }

   public DbInsertMode getInsertMode() {
      return this.ui.getInsertMode();
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      DbFields var2 = this.ui.createDbFields();
      var1.setDbFields(var2);
      var1.setTruncateTargetFields(this.ui.isAutoTruncatingFields());
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      this.ui.editStructure(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.TargetInsertUpdateModePage.MAIN_CAPTION, PackageResources.TargetInsertUpdateModePage.SUB_CAPTION
      );
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }
}
