package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.LongInputField;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class LongTextField {
   private final IntegrationData data;
   private final String property;
   private final LongInputField field;
   private final long defaultValue;

   public LongTextField(IntegrationData var1, String var2, long var3, long var5, long var7, InputPanel var9, String var10, String var11, Size.Columns var12) {
      this(var1, var2, var3, var5, var7, var9, var10, var11, new KongaTextField(var12));
   }

   public LongTextField(IntegrationData var1, String var2, long var3, long var5, long var7, InputPanel var9, String var10, String var11, KongaTextField var12) {
      this.data = var1;
      this.property = var2;
      this.defaultValue = var3;
      this.field = new LongInputField(var10, var12, var5, var7, var11);
      this.reset();
      if (var9 != null) {
         var9.addInputField(this.field);
      }
   }

   public void reset() {
      long var1 = this.data.getLongProperty(this.property, this.defaultValue);
      this.field.setValue(var1);
   }

   public void apply() {
      if (this.field.isEnabled()) {
         this.data.setLongProperty(this.property, this.field.getLongValue(this.defaultValue));
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

   public static LongTextField.Builder builder(IntegrationData var0, String var1) {
      return new LongTextField.Builder(var0, var1);
   }

   public static final class Builder {
      private final IntegrationData data;
      private final String property;
      private long defaultValue;
      private long minValue = Long.MIN_VALUE;
      private long maxValue = Long.MAX_VALUE;
      private InputPanel parent;
      private String name;
      private String label;
      private Size.Columns size = Size.columns(10);
      private boolean enforceNumericInput = true;
      private boolean fixedWidth = false;

      public Builder(IntegrationData var1, String var2) {
         this.data = Preconditions.checkNotNull(var1);
         this.property = ArgCheck.checkNotBlank(var2, "property");
         this.name = var2;
      }

      public LongTextField.Builder withDefaultValue(long var1) {
         this.defaultValue = var1;
         return this;
      }

      public LongTextField.Builder withMinValue(long var1) {
         this.minValue = var1;
         return this;
      }

      public LongTextField.Builder withMaxValue(long var1) {
         this.maxValue = var1;
         return this;
      }

      public LongTextField.Builder withName(String var1) {
         this.name = var1;
         return this;
      }

      public LongTextField.Builder withLabel(String var1) {
         this.label = var1;
         return this;
      }

      public LongTextField.Builder withSize(Size.Columns var1) {
         this.size = Preconditions.checkNotNull(var1);
         return this;
      }

      public LongTextField.Builder withFixedWidth() {
         this.fixedWidth = true;
         return this;
      }

      public LongTextField.Builder inPanel(InputPanel var1) {
         this.parent = var1;
         return this;
      }

      public LongTextField.Builder allowNonNumericInput() {
         this.enforceNumericInput = false;
         return this;
      }

      public LongTextField build() {
         String var1 = this.label == null ? this.name : this.label;
         KongaTextField var2 = new KongaTextField(this.size);
         if (this.enforceNumericInput) {
            var2.setDocument(new LongInputField.LongDocument(this.minValue < 0L));
         }

         var2.setFixedSize(this.fixedWidth);
         return new LongTextField(this.data, this.property, this.defaultValue, this.minValue, this.maxValue, this.parent, this.name, var1, var2);
      }
   }
}
