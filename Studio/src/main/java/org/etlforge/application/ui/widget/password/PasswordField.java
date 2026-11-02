package org.etlforge.application.ui.widget.password;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaPasswordField;

public final class PasswordField {
   private static final boolean INCLUDE_SHOW_PASSWORD_DEFAULT = Boolean.getBoolean("passwordfield.supportshowpassword");
   private final PasswordField.InputFieldImpl field;
   private final JCheckBox showPasswordBox;
   private boolean includeShowPasswordBox = INCLUDE_SHOW_PASSWORD_DEFAULT;

   public PasswordField() {
      this.field = new PasswordField.InputFieldImpl();
      this.showPasswordBox = this.createShowPasswordBox();
   }

   private KongaCheckBox createShowPasswordBox() {
      KongaCheckBox var1 = new KongaCheckBox("Sh&ow password");
      var1.setBorder(Empty.border());
      var1.setBorderPainted(true);
      var1.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            PasswordField.this.updateShowPassword();
         }
      });
      return var1;
   }

   private void updateShowPassword() {
      this.field.setShowPassword(this.showPasswordBox.isSelected());
   }

   public void dispose() {
      this.field.dispose();
   }

   public void setName(String var1) {
      this.field.setName(var1);
   }

   public void setLabel(String var1) {
      KongaLabel var2 = (KongaLabel)this.field.getLabel();
      var2.setMnemonicText(var1);
   }

   public void setShowPasswordText(String var1) {
      this.showPasswordBox.setText(var1);
   }

   public void setValidationEnabled(boolean var1) {
      this.field.setValidationEnabled(var1);
   }

   public void addDocumentListener(DocumentListener var1) {
      this.field.addDocumentListener(var1);
   }

   public void removeDocumentListener(DocumentListener var1) {
      this.field.removeDocumentListener(var1);
   }

   public boolean isFieldValid() {
      return this.field.isFieldValid();
   }

   public boolean isEmpty() {
      return this.field.isEmpty();
   }

   public String getPassword() {
      return this.field.getValue();
   }

   public void setPassword(String var1) {
      this.field.setValue(var1);
   }

   public JLabel getLabel() {
      return this.field.getLabel();
   }

   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
      this.showPasswordBox.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.field.isEnabled();
   }

   public void requestFocus() {
      this.field.requestFocus();
   }

   public void addToInputPanel(InputPanel var1) {
      var1.addInputField(this.field, this.field, this.field);
   }

   public JComponent[] getLayoutComponents() {
      return new JComponent[]{this.field.getLabel(), this.field.getInputComponent(), this.includeShowPasswordBox ? this.showPasswordBox : null};
   }

   public JComponent layoutWithoutLabel() {
      if (this.includeShowPasswordBox) {
         TwoColumnPanel var1 = new TwoColumnPanel();
         var1.withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL).withRightFill(GridBagLayoutPanel.Fill.NONE);
         var1.withLeftWeights(1.0, 0.0).withRightWeights(0.0, 0.0);
         var1.withLeftInsets(0, 0, 0, 5);
         var1.addRow(this.field.getInputComponent(), this.showPasswordBox);
         return var1.container();
      } else {
         return this.field.getInputComponent();
      }
   }

   public JTextComponent getInputComponent() {
      return this.field.getInputComponent();
   }

   public void setIncludeShowPasswordBox(boolean var1) {
      this.includeShowPasswordBox = var1;
   }

   private static class InputFieldImpl extends DefaultNonEmptyTextInputField {
      private boolean validate = true;

      public InputFieldImpl() {
         super("Password", new KongaPasswordField(20), "&Password:");
         this.setSelectAllWhenFocused(true);
         this.decorateField();
      }

      public void setValidationEnabled(boolean var1) {
         this.validate = var1;
         this.decorateField();
      }

      public void setShowPassword(boolean var1) {
         ((KongaPasswordField)super.getInputComponent()).setShowPassword(var1);
      }

      @Override
      public boolean isInputValid(String var1) {
         return this.validate ? super.isInputValid(var1) : true;
      }

      @Override
      public String getInvalidMessage(String var1) {
         return "Must provide a password";
      }
   }
}
