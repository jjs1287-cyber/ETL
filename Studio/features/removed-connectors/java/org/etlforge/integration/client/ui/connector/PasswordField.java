package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.button.KongaButton;

public final class PasswordField {
   private final PasswordField.PasswordAction passwordAction = new PasswordField.PasswordAction();
   private final JButton passwordButton;
   private Optional<Password> password = Optional.absent();

   public PasswordField() {
      this.passwordButton = new KongaButton(this.passwordAction);
      this.passwordAction.setTrigger(this.passwordButton);
   }

   public void registerAsChangeSource(InputPanel var1) {
      this.passwordAction.setInputPanel(var1);
   }

   public void setPassword(Optional<Password> var1) {
      this.password = Preconditions.checkNotNull(var1, "password");
   }

   public Optional<Password> getPassword() {
      return this.password;
   }

   public JComponent getUi() {
      return this.passwordButton;
   }

   public BoundProperty asBoundProperty() {
      return new PasswordField.PasswordProperty();
   }

   private class Edit extends AbstractUndoableEdit {
      private final Optional<Password> oldPassword;
      private final Optional<Password> newPassword;

      public Edit(Optional<Password> var2, Optional<Password> var3) {
         this.oldPassword = var2;
         this.newPassword = var3;
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         PasswordField.this.password = this.oldPassword;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         PasswordField.this.password = this.newPassword;
      }

      @Override
      public String getPresentationName() {
         return "Password Change";
      }
   }

   private class PasswordAction extends AbstractPasswordAction {
      private InputPanel inputPanel;

      private PasswordAction() {
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            Optional var2 = PasswordField.this.password;
            PasswordField.this.password = Optional.of(Password.encrypt(new String(var1)));
            this.firePasswordChanged(var2);
         }
      }

      public void setInputPanel(InputPanel var1) {
         this.inputPanel = var1;
      }

      private void firePasswordChanged(Optional<Password> var1) {
         if (this.inputPanel != null) {
            InputChangeEvent var2 = new InputChangeEvent(this, "Password");
            this.inputPanel.inputChanged(var2);
            UndoableEditEvent var3 = new UndoableEditEvent(this, PasswordField.this.new Edit(var1, PasswordField.this.password));
            this.inputPanel.undoableEditHappened(var3);
         }
      }
   }

   private class PasswordProperty implements BoundProperty {
      private PasswordProperty() {
      }

      @Override
      public String getName() {
         return "password";
      }

      @Override
      public void newValue(Object var1) {
         if (var1 == null) {
            PasswordField.this.password = Optional.absent();
         } else {
            PasswordField.this.password = Optional.of(Password.decrypt((String)var1));
         }
      }
   }
}
