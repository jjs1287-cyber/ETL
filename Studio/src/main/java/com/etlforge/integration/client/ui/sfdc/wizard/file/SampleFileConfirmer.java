package com.etlforge.integration.client.ui.sfdc.wizard.file;

import java.awt.Component;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.Vertical;

public final class SampleFileConfirmer {
   private final Component invoker;
   private final SampleFileViewer fileViewer;

   public SampleFileConfirmer(Component var1, SampleFileViewer var2) {
      this.invoker = var1;
      this.fileViewer = var2;
   }

   public boolean confirm(SampleFile var1) {
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return var2.wasOkPressed();
   }

   private KongaDialog createDialog(SampleFile var1) {
      UiProvider var2 = this.createLayout(var1);
      WizardStyleDialog var3 = new WizardStyleDialog(
         SwingUtilities.getWindowAncestor(this.invoker), "Confirm selected file?", WizardStyleDialog.wrapUi(var2), KongaDialog.CONTINUE_CANCEL, true
      ) {
         @Override
         protected boolean useFixedButtonSize() {
            return false;
         }
      };
      var3.setFixedButtonSize(false);
      var3.getCancelButton().setText("Select another file");
      var3.setContinueButtonAsDefault();
      var3.manageLocation(this.getClass());
      return var3;
   }

   private UiProvider createLayout(SampleFile var1) {
      String var2 = "This is the contents of the selected file.";
      if (!var1.isComplete()) {
         var2 = var2 + " (Only the first 10 lines are shown.)";
      }

      return Vertical.borderLayout().withCaption(this.fileViewer, var2);
   }
}
