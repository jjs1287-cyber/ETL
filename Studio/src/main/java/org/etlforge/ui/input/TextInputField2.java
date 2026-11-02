package org.etlforge.ui.input;

import com.google.common.base.Preconditions;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public abstract class TextInputField2 extends TextInputField implements InputFieldValidator, InputFieldDecorator {
   private boolean required = true;
   private boolean decorateDynamically = true;
   private boolean showValidationMessageInToolTip = true;
   private InputFieldDecorator decorator = new TextFieldIconDecorator();

   public TextInputField2(String var1, JTextComponent var2) {
      super(var1, var2, (JLabel)null);
   }

   public TextInputField2(String var1, JTextComponent var2, String var3) {
      super(var1, var2, var3);
   }

   public TextInputField2(String var1, JTextComponent var2, JLabel var3) {
      super(var1, var2, var3);
   }

   public final void setDecorator(InputFieldDecorator var1) {
      Preconditions.checkNotNull(var1, "decorator");
      this.decorator = var1;
   }

   @Override
   public final boolean isFieldRequired(InputField var1) {
      Preconditions.checkArgument(var1 == this, "A TextInputField2 can only validate and decorate itself.");
      return this.required;
   }

   public final boolean isFieldValid() {
      return this.isFieldValid(this);
   }

   @Override
   public boolean isFieldValid(InputField var1) {
      Preconditions.checkArgument(var1 == this, "A TextInputField2 can only validate and decorate itself.");
      boolean var2 = this.isInputValid(this.getValue());
      this.setToolTipText(var2);
      return var2;
   }

   public final void validateField() throws InputFieldException {
      this.validateField(this);
   }

   @Override
   public void validateField(InputField var1) throws InputFieldException {
      Preconditions.checkArgument(var1 == this, "A TextInputField2 can only validate and decorate itself.");
      if (!this.isInputValid(this.getValue())) {
         throw new InputFieldException(this, this.getInvalidMessage(this.getValue()));
      }
   }

   public final void decorateField() {
      this.decorateField(this, this.isFieldValid());
   }

   @Override
   public final void decorateField(InputField var1, boolean var2) {
      Preconditions.checkArgument(var1 == this, "A TextInputField2 can only validate and decorate itself.");
      this.decorator.decorateField(var1, var2);
   }

   public abstract boolean isInputValid(String var1);

   public String getInvalidMessage(String var1) {
      return PackageResources.INVALID_VALUE;
   }

   public final void setRequired(boolean var1) {
      if (var1 != this.required) {
         this.required = var1;
         if (this.isActive()) {
            this.decorateField();
         }
      }
   }

   public final boolean isRequired() {
      return this.required;
   }

   @Override
   public final void setActive(boolean var1) {
      if (var1 != this.isActive()) {
         super.setActive(var1);
         this.decorator.setActive(var1);
         UiUtils.setEnabled(var1, this.getInputComponent(), this.getLabel());
         this.decorate();
      }
   }

   public final void setShowValidationMessageInToolTip(boolean var1) {
      this.showValidationMessageInToolTip = var1;
   }

   private void decorate() {
      if (this.isActive()) {
         boolean var1 = this.isFieldValid();
         this.decorator.decorateField(this, var1);
         this.setToolTipText(var1);
      } else {
         this.decorator.decorateField(this, true);
         this.getInputComponent().setToolTipText(null);
      }
   }

   private void setToolTipText(boolean var1) {
      if (this.showValidationMessageInToolTip) {
         if (var1) {
            this.getInputComponent().setToolTipText(null);
         } else {
            this.getInputComponent().setToolTipText(this.getInvalidMessage(this.getValue()));
         }
      }
   }

   public final void setDecorateDynamically(boolean var1) {
      this.decorateDynamically = var1;
   }

   public final boolean isDecoratedDynamically() {
      return this.decorateDynamically;
   }

   @Override
   protected final DocumentContentChangeListener createDocumentListener() {
      final DocumentContentChangeListener var1 = super.createDocumentListener();
      return new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1x) {
            if (TextInputField2.this.decorateDynamically) {
               TextInputField2.this.decorate();
            }

            var1.contentChanged(var1x);
         }
      };
   }

   @Override
   protected final void enabledStateChanged(boolean var1) {
      this.decorateField();
   }
}
