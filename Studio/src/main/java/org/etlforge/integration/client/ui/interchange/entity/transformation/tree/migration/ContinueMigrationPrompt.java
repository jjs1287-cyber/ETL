package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.DialogPaneIcons;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;

final class ContinueMigrationPrompt extends KongaDialog {
   private ContinueMigrationPrompt(Window var1, String var2, String var3, String var4, String var5) {
      super(var1, var2, ModalityType.APPLICATION_MODAL);
      this.layoutDialog(var3, var4, var5);
      this.pack();
      this.getRootPane().setDefaultButton(this.getOKButton());
      this.manageLocation();
   }

   static boolean prompt(Window var0, String var1, String var2, String var3, String var4) {
      ContinueMigrationPrompt var5 = new ContinueMigrationPrompt(var0, var1, var2, var3, var4);
      var5.setVisible(true);
      return var5.wasOkPressed();
   }

   private void layoutDialog(String var1, String var2, String var3) {
      BorderLayoutBuilder var4 = new BorderLayoutBuilder();
      MigrationDialogUtils.addIcon(var4.container(), DialogPaneIcons.QUESTION);
      var4.center(this.getMessageArea(var1, var2, var3));
      var4.south(this.getDialogButtons());
      var4.asContentPaneFor(this);
   }

   private UiProvider getMessageArea(String var1, String var2, String var3) {
      OneColumnPanel var4 = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(Empty.border(10, 10, 0, 10));
      var4.setWeightX(1.0);
      var4.add(MigrationDialogUtils.getReadOnlyTextDisplay(var1));
      var4.fillOut(this.getExpressionArea(var2));
      var4.setFill(OneColumnPanel.HORIZONTAL);
      var4.setWeightY(0.0);
      var4.add(MigrationDialogUtils.getReadOnlyTextDisplay(var3));
      return var4;
   }

   private JComponent getExpressionArea(String var1) {
      JTextArea var2 = MigrationDialogUtils.getExpressionArea(var1, false);
      return new JScrollPane(var2);
   }

   private JComponent getDialogButtons() {
      JPanel var1 = this.getButtonPanel(OK_CANCEL, 10);
      this.getOKButton().setText("Yes");
      this.getCancelButton().setText("No");
      var1.setBorder(Empty.border(10, 10, 20, 10));
      var1.setOpaque(false);
      return var1;
   }
}
