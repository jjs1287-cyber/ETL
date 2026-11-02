package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class StringTextField {
   private final IntegrationData data;
   private final String property;
   private final TextInputField2 field;
   private final String defaultValue;

   public StringTextField(IntegrationData var1, String var2, String var3, InputPanel var4, TextInputField2 var5) {
      this.data = var1;
      this.property = var2;
      this.defaultValue = var3;
      this.field = var5;
      this.reset();
      if (var4 != null) {
         var4.addInputField(var5, var5, var5);
      }
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.textProperty(this.property, this.field);
   }

   public boolean isEmpty() {
      return this.field.getValue().isEmpty();
   }

   public void setValue(String var1) {
      this.field.setValue(var1);
   }

   public void reset() {
      String var1;
      synchronized (this.data.getDataLock()) {
         var1 = this.data.getProperty(this.property, this.defaultValue);
      }

      this.field.setValue(var1);
   }

   public void apply() {
      if (this.field.isEnabled()) {
         this.data.setStringProperty(this.property, this.field.getValue());
      }
   }

   public void layoutIn(TwoColumnLayout var1) {
      KongaTextField var2 = (KongaTextField)this.field.getInputComponent();
      JComponent var3 = var2.isFixedSize() ? InvisiblePanel.of(var2) : var2;
      var1.addRow(this.field.getLabel(), var3);
   }

   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.field.isEnabled();
   }

   public void requestFocus() {
      this.field.requestFocus();
   }

   public void setToolTip(String var1) {
      this.field.getInputComponent().setToolTipText(var1);
      JLabel var2 = this.field.getLabel();
      if (var2 != null) {
         var2.setToolTipText(var1);
      }
   }

   public static StringTextField.Builder builder(IntegrationData var0, String var1) {
      return new StringTextField.Builder(var0, var1);
   }

   public static final class Builder {
      private final IntegrationData data;
      private final String property;
      private String defaultValue;
      private InputPanel parent;
      private String name;
      private String label;
      private Size.Columns size;
      private boolean fixedWidth;
      private boolean autoTrim = true;
      private ValueCache valueCache;
      private StringTextField.Validator validator = new StringTextField.Validator() {
         @Override
         public boolean isValid(String var1) {
            return true;
         }

         @Override
         public String whyNot() {
            throw new UnsupportedOperationException();
         }
      };

      public Builder(IntegrationData var1, String var2) {
         this.data = Preconditions.checkNotNull(var1, "data");
         this.property = ArgCheck.checkNotBlank(var2, "property");
         this.name = var2;
      }

      public StringTextField.Builder withDefaultValue(String var1) {
         this.defaultValue = var1;
         return this;
      }

      public StringTextField.Builder withName(String var1) {
         this.name = var1;
         return this;
      }

      public StringTextField.Builder withLabel(String var1) {
         this.label = var1;
         return this;
      }

      public StringTextField.Builder withSize(Size.Columns var1) {
         this.size = Preconditions.checkNotNull(var1, "size");
         return this;
      }

      public StringTextField.Builder withAutoTrim(boolean var1) {
         this.autoTrim = var1;
         return this;
      }

      public StringTextField.Builder withFixedWidth() {
         this.fixedWidth = true;
         return this;
      }

      public StringTextField.Builder withAutoCompletion(ValueCache var1) {
         this.valueCache = Preconditions.checkNotNull(var1, "valueCache");
         return this;
      }

      public StringTextField.Builder inPanel(InputPanel var1) {
         this.parent = var1;
         return this;
      }

      public StringTextField.Builder withValidator(StringTextField.Validator var1) {
         this.validator = Preconditions.checkNotNull(var1, "validator");
         return this;
      }

      public StringTextField build() {
         String var1 = this.label == null ? this.name : this.label;
         KongaTextField var2 = this.size == null ? new KongaTextField() : new KongaTextField(this.size);
         var2.setFixedSize(this.fixedWidth);
         StringTextField.InputFieldImpl var3 = new StringTextField.InputFieldImpl(this.name, var2, var1, this.validator);
         var3.setAutoTrim(this.autoTrim);
         if (this.valueCache != null) {
            StringValueHistory var4 = PropertyValueHistory.of(this.valueCache, this.property);
            var3.installAutoCompletion(var4);
         }

         return new StringTextField(this.data, this.property, this.defaultValue, this.parent, var3);
      }
   }

   private static class InputFieldImpl extends TextInputField2 {
      private final StringTextField.Validator validator;
      private Disposable autoCompleter;

      public InputFieldImpl(String var1, KongaTextField var2, String var3, StringTextField.Validator var4) {
         super(var1, var2, var3);
         this.validator = var4;
      }

      @Override
      public boolean isInputValid(String var1) {
         return this.validator.isValid(var1);
      }

      @Override
      public String getInvalidMessage(String var1) {
         return this.validator.whyNot();
      }

      public void installAutoCompletion(StringValueHistory var1) {
         this.autoCompleter = new TextFieldCompletionSupport(this.getInputComponent(), var1);
      }

      @Override
      public void dispose() {
         super.dispose();
         if (this.autoCompleter != null) {
            this.autoCompleter.dispose();
         }
      }
   }

   public interface Validator {
      StringTextField.Validator NOT_EMPTY = new StringTextField.Validator() {
         @Override
         public boolean isValid(String var1) {
            return !var1.isEmpty();
         }

         @Override
         public String whyNot() {
            return "The field cannot be empty.";
         }
      };

      boolean isValid(String var1);

      String whyNot();
   }
}
