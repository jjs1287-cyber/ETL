package org.etlforge.integration.client.ui.structure.editor.text;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class FileTemplateWizardDialog extends KongaDialog {
   private final FileTemplateWizardUi ui;

   public FileTemplateWizardDialog(JFrame var1, FileTemplateWizardUi var2) {
      super(var1, PackageResources.FileTemplateWizard.TITLE, true);
      this.ui = var2;
      var2.setIncludeHelpButtonInLayout(false);
      this.layoutDialog(var2);
      this.pack();
      this.manageLocation();
      this.setOkButtonAsDefault();
   }

   @Override
   protected void onDialogClose() {
      try {
         this.ui.dispose();
      } finally {
         super.onDialogClose();
      }
   }

   private void layoutDialog(FileTemplateWizardUi var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(var1).south(this.createButtons(var1));
      var2.withBorder(Empty.border(10, 10, 20, 10)).asContentPaneFor(this);
   }

   private JPanel createButtons(FileTemplateWizardUi var1) {
      JButton var2 = this.createOKButton();
      JButton var3 = this.createCancelButton();
      JButton var4 = var1.getHelpButton();
      JPanel var5 = this.getButtonPanel(new JButton[]{var2, var3, var4}, -1, 10, 0);
      var5.setBorder(Empty.border(20, 0, 0, 0));
      return var5;
   }
}
