package org.etlforge.integration.client.ui.jitterpack;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class ImportParametersDefiner implements UiProvider {
   private final KongaCheckBox encryptionBox = this.createEncryptionCheckBox();
   private final ImportParametersDefiner.PasswordField pwdField = this.createPasswordField();
   private final KongaCheckBox createNewGUIDsBox = this.createNewGUIDsBox();
   private final KongaTextField newProjectNameField = this.createNewProjectNameField();
   private final JComponent newProjectNameFieldCapsule = this.createNewProjectNameFieldCapsule();

   public ImportParametersDefiner() {
      this.createNewGUIDsBox.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ImportParametersDefiner.this.updateNewProjectNameField(var1);
         }
      });
      this.updateNewProjectNameField(false);
   }

   private void updateNewProjectNameField(boolean var1) {
      UiUtils.setEnabledRecursively(this.newProjectNameFieldCapsule, var1);
      String var2 = var1 ? null : Strings.get("Import.NewName.Disabled");
      this.newProjectNameFieldCapsule.setToolTipText(var2);
   }

   private KongaCheckBox createEncryptionCheckBox() {
      String var1 = Strings.getEncryptionLabel(PackMode.IMPORT);
      KongaCheckBox var2 = new KongaCheckBox(var1);
      var2.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ImportParametersDefiner.this.pwdField.setEnabled(var1);
         }
      });
      return var2;
   }

   private ImportParametersDefiner.PasswordField createPasswordField() {
      ImportParametersDefiner.PasswordField var1 = new ImportParametersDefiner.PasswordField();
      var1.setEnabled(false);
      return var1;
   }

   private KongaCheckBox createNewGUIDsBox() {
      KongaCheckBox var1 = new KongaCheckBox(Strings.get("Import.NewGuids"));
      var1.setToolTipText(Strings.get("Import.NewGuids.ToolTip"));
      return var1;
   }

   private KongaTextField createNewProjectNameField() {
      KongaTextField var1 = new KongaTextField(20);
      var1.setToolTipText(Strings.get("Import.NewName.ToolTip"));
      return var1;
   }

   private JComponent createNewProjectNameFieldCapsule() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      KongaLabel var2 = KongaLabel.withMnemonic(Strings.get("Import.NewName"));
      var2.setLabelFor(this.newProjectNameField);
      return var1.addAllWithSpace(5, var2, this.newProjectNameField).container();
   }

   public ImportParameters getParameters() {
      char[] var1 = this.encryptionBox.isSelected() ? this.pwdField.getPassword() : null;
      boolean var2 = this.createNewGUIDsBox.isSelected();
      String var3 = var2 ? this.newProjectNameField.getText().trim() : null;
      return new ImportParameters(var1, var2, var3);
   }

   public void reset() {
      this.encryptionBox.setSelected(false);
      this.createNewGUIDsBox.setSelected(false);
      this.newProjectNameField.setText("");
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createDisplayer();
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.encryptionBox.requestFocusInWindow();
   }

   private JComponent createDisplayer() {
      BoxBuilder var1 = BoxBuilder.vertical().leftAligned().withBorder(new BorderBuilder().empty(8, 32, 0, 0).titled(null));
      var1.addAllWithSpace(10, this.layoutEncryptionSection(), this.layoutOptionsSection());
      DropDownPanel var2 = new DropDownPanel(var1.container(), Strings.get("Options"), false);
      var2.decorateTitle(WizardStyleSheet.MEDIUM_CAPTION_STYLE);
      return var2.getDisplayer();
   }

   private UiProvider layoutEncryptionSection() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.withInsets(0, 0, 5, 0);
      var1.add(this.encryptionBox);
      var1.withInsets(0, 0, 0, 0);
      this.pwdField.addTo(var1);
      return var1;
   }

   private UiProvider layoutOptionsSection() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.withInsets(0, 0, 5, 0);
      var1.add(this.createNewGUIDsBox);
      var1.withInsets(0, 0, 0, 0);
      var1.add(this.newProjectNameFieldCapsule);
      return var1;
   }

   private static class PasswordField {
      private final JLabel label = KongaLabel.withMnemonic(Strings.get("Password") + ": ");
      private final JPasswordField password = new JPasswordField(15);

      public PasswordField() {
         this.label.setLabelFor(this.password);
      }

      public void setEnabled(boolean var1) {
         this.label.setEnabled(var1);
         this.password.setEnabled(var1);
         if (var1) {
            this.password.requestFocusInWindow();
         }
      }

      public char[] getPassword() {
         return this.password.getPassword();
      }

      public void addTo(OneColumnPanel var1) {
         var1.addAll(this.label, this.password);
      }
   }
}

