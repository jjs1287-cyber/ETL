package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.ImmutableList;
import java.awt.Insets;
import javax.swing.event.DocumentListener;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.application.ui.widget.password.PasswordField;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.text.KongaTextField;

public final class LoginCredentialsUi implements UiProvider {
   private final TextInputField2 userNameField;
   private TokenListCompletionSupport userNameAutoCompleteSupport;
   private final PasswordField passwordField;
   private final PasswordField securityTokenField;

   public LoginCredentialsUi() {
      this(Size.columns(20));
   }

   public LoginCredentialsUi(Size.Columns var1) {
      this.userNameField = createUsernameField(var1);
      this.passwordField = new PasswordField();
      this.securityTokenField = createSecurityTokenField();
   }

   private static TextInputField2 createUsernameField(Size.Columns var0) {
      KongaTextField var1 = new KongaTextField(var0);
      var1.setSelectAllWhenFocused(true);
      DefaultNonEmptyTextInputField var2 = new DefaultNonEmptyTextInputField("User Name", var1, "User &name:") {
         @Override
         public boolean isInputValid(String value) { return !getInputComponent().isEnabled() || super.isInputValid(value); }
         @Override
         public String getInvalidMessage(String var1) {
            return this.getName() + " cannot be empty";
         }
      };
      var2.decorateField();
      return var2;
   }

   private static PasswordField createSecurityTokenField() {
      String var0 = "Security Token";
      PasswordField var1 = new PasswordField();
      var1.setValidationEnabled(false);
      var1.setName(var0);
      var1.setLabel("&" + var0 + ":");
      var1.setShowPasswordText("Show to&ken");
      return var1;
   }

   public void setEnabled(boolean enabled) { userNameField.getInputComponent().setEnabled(enabled); passwordField.setEnabled(enabled); passwordField.setValidationEnabled(enabled); securityTokenField.setEnabled(enabled); }
   public void dispose() {
      if (this.userNameAutoCompleteSupport != null) {
         this.userNameAutoCompleteSupport.dispose();
      }

      this.userNameField.dispose();
      this.passwordField.dispose();
      this.securityTokenField.dispose();
   }

   public void installUsernameAutoCompletion(StringValueHistory var1) {
      this.userNameAutoCompleteSupport = new TextFieldCompletionSupport(this.userNameField, var1);
   }

   public void addChangeListener(DocumentListener var1) {
      this.userNameField.addDocumentListener(var1);
      this.passwordField.addDocumentListener(var1);
      this.securityTokenField.addDocumentListener(var1);
   }

   public void addToInputPanel(InputPanel var1) {
      var1.addInputField(this.userNameField, this.userNameField, this.userNameField);
      this.passwordField.addToInputPanel(var1);
      this.securityTokenField.addToInputPanel(var1);
   }

   public void setCredentials(LoginCredentials var1) {
      if (var1 == null) {
         this.userNameField.setValue("");
         this.passwordField.setPassword("");
         this.securityTokenField.setPassword("");
      } else {
         this.userNameField.setValue(var1.username);
         this.passwordField.setPassword(var1.password);
         this.securityTokenField.setPassword(var1.securityToken);
      }
   }

   public LoginCredentials getCredentials(boolean var1) {
      return new LoginCredentials(this.getUserName(), this.getPassword(), this.getSecurityToken(), var1);
   }

   private String getPassword() {
      return this.passwordField.getPassword();
   }

   private String getSecurityToken() {
      return this.securityTokenField.getPassword();
   }

   private String getUserName() {
      return this.userNameField.getValue().trim();
   }

   public boolean isInputValid() {
      return this.hasUserName() && this.hasPassword();
   }

   public boolean hasUserName() {
      return !this.getUserName().isEmpty();
   }

   private boolean hasPassword() {
      return this.passwordField.isFieldValid();
   }

   public void layoutIn(NColumnPanel var1) {
      var1.addRow(this.userNameField.getLabel(), this.userNameField.getInputComponent(), null);
      var1.addRow(this.passwordField.getLayoutComponents());
      var1.addRow(this.securityTokenField.getLayoutComponents());
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = emptyThreeColumnGrid();
      this.layoutIn(var2);
      var2.decorate(var1);
   }

   private static NColumnPanel emptyThreeColumnGrid() {
      NColumnPanel var0 = new NColumnPanel(3);
      var0.setXWeights(0.0, 1.0, 0.0).setYWeights(0.0, 0.0, 0.0);
      var0.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE);
      Insets var1 = new Insets(0, 0, 10, 5);
      var0.setInsets(0, var1);
      var0.setInsets(1, var1);
      return var0;
   }

   @Override
   public void requestFocus() {
      this.userNameField.requestFocus();
   }

   public Iterable<BoundProperty> getBoundProperties() {
      return ImmutableList.of(
         BoundPropertyFactory.textProperty("user_name", this.userNameField),
         BoundPropertyFactory.passwordProperty("password", this.passwordField.getInputComponent()),
         BoundPropertyFactory.passwordProperty("security_token", this.securityTokenField.getInputComponent())
      );
   }
}
