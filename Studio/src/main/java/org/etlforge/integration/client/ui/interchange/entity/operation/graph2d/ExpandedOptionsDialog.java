package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Supplier;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelDialogMediator;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;

public class ExpandedOptionsDialog {
   private final ExpandedOptionsPanel panel;
   private final KongaDialog dialog;

   public ExpandedOptionsDialog(Window var1, OperationGraphController var2, OperationWrapperNode var3) {
      this.panel = new ExpandedOptionsPanel(var2, var3);
      this.dialog = this.createDialog(var1);
   }

   public void show() {
      this.dialog.pack();
      this.dialog.manageLocation(ExpandedOptionsDialog.class);
      this.dialog.setVisible(true);
      if (this.dialog.wasOkPressed()) {
         this.panel.apply();
      }
   }

   private KongaDialog createDialog(Window var1) {
      InputPanel var2 = this.panel.getInputPanel();
      JComponent var3 = this.createDialogContents();
      WizardStyleDialog var4 = new WizardStyleDialog(var1, "Configure Operation", var3);
      InputPanelDialogMediator.bind(var2, var4);
      var4.addOkCondition(new Supplier<Boolean>() {
         public Boolean get() {
            return ExpandedOptionsDialog.this.panel.validate();
         }
      });
      var4.setOkButtonAsDefault();
      return var4;
   }

   private JComponent createDialogContents() {
      JPanel var1 = this.panel.getInputPanel().getUi();
      UiUtils.setPanelBackgrounds(var1, Colors.NEUTRAL_BACKGROUND);
      return new KongaScrollPane(var1);
   }
}
