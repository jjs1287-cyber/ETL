package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.structure.text.validation.StringOptions;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class StringOptionsUi {
   private static final boolean TRIM_TO_LENGTH_SUPPORTED = false;
   private final FieldValidationModel model;
   private final CheckBoxInputField ignoreCase = createCheckBox("&Ignore case");
   private final CheckBoxInputField trimToLength = createCheckBox("&Trim to length");

   private static CheckBoxInputField createCheckBox(String var0) {
      String var1 = ApplicationResources.MNEMONIC_FACTORY.asText(var0);
      CheckBoxInputField var2 = new CheckBoxInputField(var1, new KongaCheckBox(), var0);
      var2.setSendInputChangeEvents(true);
      return var2;
   }

   public StringOptionsUi(FieldValidationModel var1) {
      this.model = var1;
   }

   public Binding bind() {
      return new StringOptionsUi.ModelBinding(this.model);
   }

   public void setEnabled(boolean var1) {
      this.ignoreCase.setEnabled(var1);
      this.trimToLength.setEnabled(var1);
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.ignoreCase);
      var1.addInputField(this.trimToLength);
   }

   public void layoutIn(TwoColumnPanel var1) {
      this.ignoreCase.layoutIn(var1);
   }

   private class ModelBinding extends AbstractBinding<StringOptions> implements ItemListener {
      public ModelBinding(FieldValidationModel var2) {
         super(var2, "stringOptions");
         StringOptionsUi.this.ignoreCase.addItemListener(this);
         StringOptionsUi.this.trimToLength.addItemListener(this);
      }

      @Override
      protected void disposeUiListener() {
         StringOptionsUi.this.ignoreCase.removeItemListener(this);
         StringOptionsUi.this.trimToLength.removeItemListener(this);
      }

      protected StringOptions getValueFromUi() {
         return new StringOptions(StringOptionsUi.this.ignoreCase.isSelected(), StringOptionsUi.this.trimToLength.isSelected());
      }

      protected void updateUiWithValue(StringOptions var1) {
         StringOptionsUi.this.ignoreCase.setSelected(var1.ignoreCase);
         StringOptionsUi.this.trimToLength.setSelected(var1.trimToLength);
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         switch (var1.getStateChange()) {
            case 1:
            case 2:
               if (this.isUiToModelEnabled()) {
                  this.syncModel();
               }
         }
      }
   }
}
