package org.etlforge.integration.client.server.ui.login;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaPasswordField;
import org.etlforge.ui.widget.text.KongaTextField;

class UserCredentialsUi {
   private final JTextField userNameField = this.createUserField();
   private final KongaPasswordField passwordField = new KongaPasswordField(20);
   private final JCheckBox rememberPasswordBox;

   public UserCredentialsUi(LoginModel var1) {
      this.passwordField.setSelectAllWhenFocused(true);
      this.rememberPasswordBox = new KongaCheckBox("&Remember password");
      this.bindModel(var1);
   }

   private JTextField createUserField() {
      KongaTextField var1 = new KongaTextField(20);
      var1.setSelectAllWhenFocused(true);
      return var1;
   }

   private void bindModel(LoginModel var1) {
      TextBinding.bindAndSyncUi(var1, "user", this.userNameField);
      TextBinding.bindAndSyncUi(var1, "password", this.passwordField);
      BooleanBinding.bindAndSyncUi(var1, "rememberPassword", this.rememberPasswordBox);
   }

   public String getUserName() {
      return this.userNameField.getText().trim();
   }

   public JComponent getComponentToFocus() {
      String var1 = this.getUserName();
      return var1.isEmpty() ? this.userNameField : this.passwordField;
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.userNameField);
      var1.install(this.passwordField);
   }

   public void layoutIn(JPanel var1) {
      this.layoutUserField(var1);
      this.layoutPasswordField(var1);
      this.layoutRememberPasswordChoice(var1);
   }

   private void layoutUserField(JPanel var1) {
      KongaLabel var2 = KongaLabel.withMnemonic(PackageResources.Labels.USER);
      var2.setIcon(ClientIcons.USER_16);
      var2.setLabelFor(this.userNameField);
      var1.add(var2);
      var1.add(this.userNameField);
   }

   private void layoutPasswordField(JPanel var1) {
      KongaLabel var2 = KongaLabel.withMnemonic(PackageResources.Labels.PASSWORD);
      var2.setLabelFor(this.passwordField);
      var1.add(var2);
      var1.add(this.passwordField);
   }

   private void layoutRememberPasswordChoice(JPanel var1) {
      var1.add(new JLabel(" "));
      var1.add(this.rememberPasswordBox);
   }
}
