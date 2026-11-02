package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Preconditions;
import javax.swing.JLabel;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class BooleanCheckBox {
   private final IntegrationData data;
   private final String property;
   private final CheckBoxInputField checkBox;
   private final boolean defaultValue;

   public BooleanCheckBox(IntegrationData var1, String var2, boolean var3, InputPanel var4, String var5, String var6) {
      this.data = var1;
      this.property = var2;
      this.defaultValue = var3;
      this.checkBox = new CheckBoxInputField(var5, new KongaCheckBox(), var6);
      this.reset();
      if (var4 != null) {
         var4.addInputField(this.checkBox);
      }
   }

   public void reset() {
      boolean var1 = this.data.getBooleanProperty(this.property, this.defaultValue);
      this.checkBox.setSelected(var1);
   }

   public void apply() {
      if (this.checkBox.isEnabled()) {
         this.data.setBooleanProperty(this.property, this.checkBox.isSelected());
      }
   }

   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.checkBox.getLabel(), this.checkBox.getInputComponent());
   }

   public void setEnabled(boolean var1) {
      this.checkBox.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.checkBox.isEnabled();
   }

   public void requestFocus() {
      this.checkBox.requestFocus();
   }

   public void setToolTip(String var1) {
      this.checkBox.getInputComponent().setToolTipText(var1);
      JLabel var2 = this.checkBox.getLabel();
      if (var2 != null) {
         var2.setToolTipText(var1);
      }
   }

   public static BooleanCheckBox.Builder builder(IntegrationData var0, String var1) {
      return new BooleanCheckBox.Builder(var0, var1);
   }

   public static final class Builder {
      private final IntegrationData data;
      private final String property;
      private boolean defaultValue;
      private InputPanel parent;
      private String name;
      private String label;

      public Builder(IntegrationData var1, String var2) {
         this.data = Preconditions.checkNotNull(var1);
         this.property = ArgCheck.checkNotBlank(var2, "property");
         this.name = var2;
      }

      public BooleanCheckBox.Builder withDefaultValue(boolean var1) {
         this.defaultValue = var1;
         return this;
      }

      public BooleanCheckBox.Builder withName(String var1) {
         this.name = var1;
         return this;
      }

      public BooleanCheckBox.Builder withLabel(String var1) {
         this.label = var1;
         return this;
      }

      public BooleanCheckBox.Builder inPanel(InputPanel var1) {
         this.parent = var1;
         return this;
      }

      public BooleanCheckBox build() {
         String var1 = this.label == null ? this.name : this.label;
         return new BooleanCheckBox(this.data, this.property, this.defaultValue, this.parent, this.name, var1);
      }
   }
}
