package org.etlforge.integration.client.ui.operation.options;

import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputPanelDialogMediator;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;

public class OperationOptionsDialog {
   private final OperationOptionsPanel panel;
   private final KongaDialog dialog;

   public OperationOptionsDialog(Window var1, OperationOptionsPanel var2) {
      this.panel = var2;
      var2.setBorder(Empty.border(10));
      this.dialog = this.createDialog(var1);
   }

   private KongaDialog createDialog(Window var1) {
      JComponent var2 = this.createDialogContent();
      WizardStyleDialog var3 = new WizardStyleDialog(var1, "Operation Options", var2);
      InputPanelDialogMediator.bind(this.panel, var3);
      var3.setOkButtonAsDefault();
      return var3;
   }

   private JComponent createDialogContent() {
      JPanel var1 = this.panel.getUi();
      UiUtils.setPanelBackgrounds(var1, Colors.NEUTRAL_BACKGROUND);
      return new KongaScrollPane(var1);
   }

   public OperationOptions show() {
      this.dialog.pack();
      this.dialog.manageLocation(OperationOptionsDialog.class);
      this.dialog.setVisible(true);
      this.panel.dispose();
      return this.dialog.wasOkPressed() ? this.panel.getDefinedOptions() : null;
   }
}
