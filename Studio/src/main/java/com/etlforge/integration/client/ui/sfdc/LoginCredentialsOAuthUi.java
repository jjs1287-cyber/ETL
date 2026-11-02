package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.ImmutableList;
import org.etlforge.application.ui.widget.password.PasswordField;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.*;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class LoginCredentialsOAuthUi {
   private final TextInputField2 instanceUrl;
   private final TextInputField2 clientId;
   private final PasswordField clientSecret = new PasswordField();
   public LoginCredentialsOAuthUi(Size.Columns size) {
      instanceUrl = field("Instance URL", "Instance &URL:", size);
      clientId = field("Client ID", "Client &ID:", size);
      clientSecret.setName("Client Secret");
      clientSecret.setLabel("Client &Secret:");
      clientSecret.setShowPasswordText("Show secret");
   }
   private static TextInputField2 field(String name, String label, Size.Columns size) {
      return new DefaultNonEmptyTextInputField(name, new KongaTextField(size), label) {
         @Override public boolean isInputValid(String value) {
            return !getInputComponent().isEnabled() || super.isInputValid(value);
         }
      };
   }
   public void setEnabled(boolean enabled) {
      instanceUrl.getInputComponent().setEnabled(enabled);
      clientId.getInputComponent().setEnabled(enabled);
      clientSecret.setEnabled(enabled);
      clientSecret.setValidationEnabled(enabled);
   }
   public void setCredentials(LoginCredentials value) {
      instanceUrl.setValue(value.instanceUrl); clientId.setValue(value.clientId); clientSecret.setPassword(value.clientSecret);
   }
   public LoginCredentials getCredentials(boolean sandbox) {
      if (instanceUrl.getValue().trim().isEmpty() || clientId.getValue().trim().isEmpty() || clientSecret.getPassword().isEmpty()) return null;
      return new LoginCredentials("", "", "", sandbox, LoginCredentials.OAUTH, instanceUrl.getValue(), clientId.getValue(), clientSecret.getPassword());
   }
   public void addToInputPanel(InputPanel panel) {
      panel.addInputField(instanceUrl, instanceUrl, instanceUrl);
      panel.addInputField(clientId, clientId, clientId);
      clientSecret.addToInputPanel(panel);
   }
   public void layoutIn(NColumnPanel panel) {
      panel.addRow(instanceUrl.getLabel(), instanceUrl.getInputComponent(), null);
      panel.addRow(clientId.getLabel(), clientId.getInputComponent(), null);
      panel.addRow(clientSecret.getLayoutComponents());
   }
   public Iterable<BoundProperty> getBoundProperties() {
      return ImmutableList.of(BoundPropertyFactory.textProperty("instance_url", instanceUrl),
         BoundPropertyFactory.textProperty("client_id", clientId),
         BoundPropertyFactory.passwordProperty("client_secret", clientSecret.getInputComponent()));
   }
   public void requestFocus() { instanceUrl.requestFocus(); }
   public void dispose() { instanceUrl.dispose(); clientId.dispose(); clientSecret.dispose(); }
}