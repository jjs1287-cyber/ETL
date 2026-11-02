package org.etlforge.ui.input;

import com.google.common.base.Preconditions;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public abstract class TextBasedInputField extends InputField implements InputFieldValidator, InputFieldDecorator {
   private boolean required;
   private boolean decorateDynamically;
   private transient String errorMessage;
   private final TextInputFieldEditSender undoableEditSender;
   private final DocumentListener docListener;
   private final InputFieldDecorator decorator = new TextFieldIconDecorator();

   public TextBasedInputField(String var1, JTextComponent var2, JLabel var3) {
      super(var1, var2, var3);
      this.decorateDynamically = true;
      this.docListener = new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            TextBasedInputField.this.fireChange();
         }
      };
      var2.getDocument().addDocumentListener(this.docListener);
      this.undoableEditSender = new TextInputFieldEditSender(this);
   }

   public JTextComponent getInputComponent() {
      return (JTextComponent)super.getInputComponent();
   }

   @Override
   public void dispose() {
      this.getInputComponent().getDocument().removeDocumentListener(this.docListener);
      this.undoableEditSender.dispose();
   }

   @Override
   public void makeReadOnly() {
      this.getInputComponent().setEditable(false);
   }

   public abstract boolean isInputValid(String var1);

   public String getText() {
      return this.getInputComponent().getText().trim();
   }

   public void setText(String var1) {
      this.getInputComponent().setText(var1);
   }

   @Override
   public boolean isFieldRequired(InputField var1) {
      return this.required;
   }

   public void setRequired(boolean var1) {
      this.required = var1;
   }

   @Override
   public void validateField(InputField var1) throws InputFieldException {
      if (!this.isFieldValid(var1)) {
         throw new InputFieldException(this, this.errorMessage);
      }
   }

   private void fireChange() {
      if (this.decorateDynamically) {
         this.decorate();
      }

      if (this.isSendingInputChangeEvents()) {
         this.fireInputChangeEvent();
      }
   }

   public boolean isFieldValid() {
      return this.isFieldValid(this);
   }

   public void decorateField() {
      this.decorateField(this, this.isFieldValid());
   }

   @Override
   public void decorateField(InputField var1, boolean var2) {
      Preconditions.checkArgument(var1 == this, "A TextBasedInputField can only validate and decorate itself.");
      this.decorator.decorateField(var1, var2);
      if (!this.isActive()) {
         this.getInputComponent().setToolTipText(null);
      }
   }

   public void setDecorateDynamically(boolean var1) {
      this.decorateDynamically = var1;
   }

   public boolean isDecoratedDynamically() {
      return this.decorateDynamically;
   }

   private void decorate() {
      if (this.isActive()) {
         boolean var1 = this.isFieldValid(this);
         this.decorator.decorateField(this, var1);
         this.setToolTipText(var1);
      } else {
         this.decorator.decorateField(this, true);
         this.getInputComponent().setToolTipText(null);
      }
   }

   private void setToolTipText(boolean var1) {
      if (var1) {
         this.getInputComponent().setToolTipText(null);
      } else {
         this.getInputComponent().setToolTipText(this.errorMessage);
      }
   }

   @Override
   public void setActive(boolean var1) {
      if (var1 != this.isActive()) {
         super.setActive(var1);
         UiUtils.setEnabled(var1, this.getInputComponent(), this.getLabel());
         this.decorate();
      }
   }

   @Override
   public boolean isFieldValid(InputField var1) {
      Preconditions.checkArgument(var1 == this, "A TextBasedInputField can only validate and decorate itself.");
      this.errorMessage = null;
      String var2 = this.getText();
      if (var2.length() == 0) {
         this.errorMessage = PackageResources.VALUE_CANNOT_BE_EMPTY;
         return !this.required;
      } else {
         return this.isInputValid(this.getText());
      }
   }

   protected final void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }

   protected void setTextUndoableEditsBlocked(boolean var1) {
      this.undoableEditSender.setBlocked(var1);
   }
}
