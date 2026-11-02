package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
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
import org.etlforge.ui.text.ConditionalTextEnabler;
import org.etlforge.ui.text.TextContent;
import org.etlforge.ui.text.TextEnablerCondition;
import org.etlforge.ui.widget.button.KongaButton;

final class ManualMigrationConversionDialog extends KongaDialog {
   private final JTextArea inputArea;
   private final String suggestedValue;
   private final Action resetAction;

   private ManualMigrationConversionDialog(Window var1, String var2, String var3, String var4) {
      super(var1, var2, ModalityType.APPLICATION_MODAL);
      this.suggestedValue = var4;
      this.inputArea = MigrationDialogUtils.getExpressionArea(var4, true);
      this.resetAction = new ManualMigrationConversionDialog.ResetAction();
      this.installResetEnabling();
      this.setFocusedComponent(this.inputArea);
      this.layoutDialog(var3);
      this.pack();
      this.setResizable(true);
      this.manageLocation();
      this.setOkButtonAsDefault();
   }

   static String getConvertedValue(Window var0, String var1, String var2, String var3) {
      Preconditions.checkNotNull(var2, "de_migrating");
      Preconditions.checkNotNull(var3, "de_migrated");
      ManualMigrationConversionDialog var4 = new ManualMigrationConversionDialog(var0, var1, var2, var3);
      var4.setVisible(true);
      return var4.getConvertedValue();
   }

   private String getConvertedValue() {
      return this.wasOkPressed() ? this.inputArea.getText() : null;
   }

   private void layoutDialog(String var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      MigrationDialogUtils.addIcon(var2.container(), DialogPaneIcons.WARNING);
      var2.center(this.layoutInputArea(var1)).south(this.layoutDialogButtons());
      var2.asContentPaneFor(this);
   }

   private UiProvider layoutInputArea(String var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 5, 0).withBorder(Empty.border(10)).setWeightX(1.0);
      var2.add(this.getCaption(var1));
      var2.fillOut(new JScrollPane(this.inputArea));
      var2.setAnchor(OneColumnPanel.EAST);
      var2.noFill(new KongaButton(this.resetAction));
      return var2;
   }

   private JComponent getCaption(String var1) {
      String var2 = "The node:\n\n"
         + var1
         + "\n\nneeds manual conversion since some of the instance markers were unresolved during migration. The corresponding migrated node is shown below. Please modify it if necessary (e.g., by adding instance markers).";
      return MigrationDialogUtils.getReadOnlyTextDisplay(var2);
   }

   private JComponent layoutDialogButtons() {
      this.setFixedButtonSize(false);
      JPanel var1 = this.getButtonPanel(OK_CANCEL, 10);
      var1.setOpaque(false);
      var1.setBorder(Empty.border(10, 10, 20, 10));
      JButton var2 = this.getOKButton();
      var2.setText("Continue");
      return var1;
   }

   private void installResetEnabling() {
      TextEnablerCondition var1 = new TextEnablerCondition() {
         @Override
         public boolean isEnabled(TextContent var1) {
            return !var1.getText().equals(ManualMigrationConversionDialog.this.suggestedValue);
         }
      };
      ConditionalTextEnabler var2 = ConditionalTextEnabler.install(this.inputArea);
      var2.addDependency(this.resetAction, var1);
   }

   private final class ResetAction extends AbstractAction {
      public ResetAction() {
         super("&Reset");
         this.putValue("ShortDescription", "Reset to the original value");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ManualMigrationConversionDialog.this.inputArea.setText(ManualMigrationConversionDialog.this.suggestedValue);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ManualMigrationConversionDialog.this.inputArea.requestFocusInWindow();
            }
         });
      }
   }
}
