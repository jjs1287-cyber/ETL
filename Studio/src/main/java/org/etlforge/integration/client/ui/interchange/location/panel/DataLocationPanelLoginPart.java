package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Optional;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.LoginNameField;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.widget.button.KongaButton;

public abstract class DataLocationPanelLoginPart<T extends DataLocation> implements DataLocationPanelPart<T> {
   private final LoginNameField loginField;
   private final JButton passwordButton;
   private Optional<Password> password = Optional.absent();
   private final InputPanel site;
   private boolean loginEnabled;

   public DataLocationPanelLoginPart(InputPanel var1) {
      this.site = var1;
      this.loginField = new LoginNameField(false);
      this.loginField.registerAsChangeSource(var1);
      DataLocationPanelLoginPart.PasswordAction var2 = new DataLocationPanelLoginPart.PasswordAction();
      this.passwordButton = new KongaButton(var2);
      var2.setTrigger(this.passwordButton);
      this.loginEnabled = true;
   }

   public final void setEnabled(boolean var1) {
      this.loginField.setEnabled(this.loginEnabled && var1);
      this.passwordButton.setEnabled(var1);
   }

   public final void setLoginEnabled(boolean var1) {
      this.loginField.setEnabled(var1);
      this.loginEnabled = var1;
   }

   public final void transferFocusToLoginField() {
      this.loginField.requestFocus();
   }

   @Override
   public final void applyTo(T var1) {
      this.applyValues((T)var1, this.loginField.getLoginName(), this.password);
   }

   public final void installAutoCompletionInLoginField(StringValueHistory var1) {
      this.loginField.installAutoCompletion(var1);
   }

   @Override
   public final void addToLayout(DataLocationPanelLayout var1) {
      TextInputField2 var2 = this.loginField.asInputField();
      var1.add(var2.getLabel(), var2.getInputComponent(), this.passwordButton);
   }

   protected abstract void applyValues(T var1, String var2, Optional<Password> var3);

   @Override
   public final void reset(T var1) {
      this.loginField.setLoginName(this.getCurrentLogin((T)var1));
      this.password = this.getCurrentPassword((T)var1);
   }

   protected abstract String getCurrentLogin(T var1);

   protected abstract Optional<Password> getCurrentPassword(T var1);

   public final void setPassword(Optional<Password> var1) {
      this.password = var1;
   }

   @Override
   public final void dispose() {
      this.loginField.dispose();
   }

   protected final JComponent[] getLayoutComponents() {
      return new JComponent[]{InputFieldFactory.getCapsule(this.loginField.asInputField()), this.passwordButton};
   }

   private class PasswordAction extends AbstractPasswordAction {
      private PasswordAction() {
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            DataLocationPanelLoginPart.this.password = Optional.of(Password.encrypt(new String(var1)));
            this.firePasswordChanged();
         }
      }

      private void firePasswordChanged() {
         if (DataLocationPanelLoginPart.this.site != null) {
            InputChangeEvent var1 = new InputChangeEvent(DataLocationPanelLoginPart.this.passwordButton, "Password");
            DataLocationPanelLoginPart.this.site.inputChanged(var1);
         }
      }
   }
}
