package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import javax.swing.JComponent;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.StringList;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.util.UiUtils;

final class StringListUi extends ArgumentUi {
   private final org.etlforge.application.ui.widget.StringListUi impl = new org.etlforge.application.ui.widget.StringListUi();
   private final StringListUi.ModelBinding binding;
   private final JComponent ui = BorderLayoutBuilder.fillWith(this.impl);
   private InputPanel inputPanel;

   public StringListUi(FieldValidationModel var1) {
      this.binding = new StringListUi.ModelBinding(var1);
      this.binding.syncUi();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.impl.setEnabled(var1);
   }

   @Override
   public void dispose() {
      this.binding.dispose();
      UiUtils.removeFromParent(this.ui);
      if (this.inputPanel != null) {
         this.inputPanel.removeChangeSource(this.impl.asChangeSource());
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.impl.requestFocus();
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = var1;
      var1.addChangeSource(this.impl.asChangeSource());
   }

   private class ModelBinding extends AbstractBinding<OperatorArgument> {
      private final StringListUi.ModelBinding.UiListener uiListener = new StringListUi.ModelBinding.UiListener();

      public ModelBinding(FieldValidationModel var2) {
         super(var2, "argument");
         StringListUi.this.impl.asChangeSource().addInputChangeListener(this.uiListener);
      }

      protected void updateUiWithValue(OperatorArgument var1) {
         StringList var2 = this.getModelValue(var1);
         this.updateUi(var2);
      }

      private StringList getModelValue(Object var1) {
         return var1 instanceof StringList ? (StringList)var1 : StringList.empty();
      }

      private void updateUi(StringList var1) {
         StringListUi.this.impl.asChangeSource().removeInputChangeListener(this.uiListener);
         StringListUi.this.impl.setStringList(var1.getValues());
         StringListUi.this.impl.asChangeSource().addInputChangeListener(this.uiListener);
      }

      @Override
      protected void disposeUiListener() {
         StringListUi.this.impl.asChangeSource().removeInputChangeListener(this.uiListener);
      }

      protected StringList getValueFromUi() {
         return new StringList(StringListUi.this.impl.getStringList());
      }

      private class UiListener implements InputChangeListener {
         private UiListener() {
         }

         @Override
         public void inputChanged(InputChangeEvent var1) {
            if (ModelBinding.this.isUiToModelEnabled()) {
               ModelBinding.this.syncModel();
            }
         }
      }
   }
}
