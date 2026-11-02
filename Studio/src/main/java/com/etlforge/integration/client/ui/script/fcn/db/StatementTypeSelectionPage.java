package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.db.sql.Template;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class StatementTypeSelectionPage extends AbstractWizardPage {
   private final RadioButtonGroupPanel<Template> buttons = RadioButtonGroupPanel.forEnum(Template.class);

   public StatementTypeSelectionPage(Wizard var1) {
      super(var1, "Type of SQL Statement");
      this.setCanContinue(true);
   }

   public Template getSelectedTemplate() {
      return this.buttons.getSelection();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("SQL Statement Type", "Select the type of SQL statement you want to run");
      JPanel var2 = InvisiblePanel.of(this.buttons.getUi(null, RadioButtonGroupPanel.Layout.ONE_COLUMN));
      var2.setBorder(Empty.border(10, 20, 0, 0));
      var1.add(var2, false, false);
      JPanel var3 = var1.asFlowLayout();
      UiUtils.setOpaqueRecursively(var3, false);
      this.getUndoRedoInstaller().installIntoComponentTree(var3);
      return var3;
   }

   @Override
   public void requestFocus() {
      this.buttons.requestFocus();
   }
}
