package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.Range;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

final class RangeUi extends ArgumentUi {
   private final TextInputField lower = createField("Lower");
   private final TextInputField upper = createField("Upper");
   private final RangeUi.ModelBinding modelBinding;
   private final JComponent ui;
   private InputPanel inputPanel;

   private static TextInputField createField(String var0) {
      return new TextInputField(var0, new KongaTextField(20));
   }

   public RangeUi(FieldValidationModel var1) {
      this.modelBinding = new RangeUi.ModelBinding(var1);
      this.modelBinding.syncUi();
      this.ui = this.doLayout();
   }

   private JComponent doLayout() {
      JLabel var1 = new JLabel("and");
      var1.setHorizontalAlignment(0);
      return new OneColumnPanel()
         .stretchHorizontally()
         .withInsets(0, 0, 5, 0)
         .of(this.lower.getInputComponent(), var1, this.upper.getInputComponent())
         .nonOpaque()
         .container();
   }

   @Override
   public void setEnabled(boolean var1) {
      UiUtils.setEnabledRecursively(this.ui, var1);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = var1;
      var1.addInputField(this.lower);
      var1.addInputField(this.upper);
   }

   @Override
   public void dispose() {
      this.modelBinding.dispose();
      UiUtils.removeFromParent(this.ui);
      if (this.inputPanel != null) {
         this.inputPanel.removeInputField(this.lower);
         this.inputPanel.removeInputField(this.upper);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.lower.requestFocus();
   }

   private class ModelBinding extends AbstractBinding<OperatorArgument> {
      private final RangeUi.ModelBinding.UiListener uiListener = new RangeUi.ModelBinding.UiListener();

      public ModelBinding(FieldValidationModel var2) {
         super(var2, "argument");
         RangeUi.this.lower.addDocumentListener(this.uiListener);
         RangeUi.this.upper.addDocumentListener(this.uiListener);
      }

      @Override
      protected void disposeUiListener() {
         RangeUi.this.lower.removeDocumentListener(this.uiListener);
         RangeUi.this.upper.removeDocumentListener(this.uiListener);
      }

      protected OperatorArgument getValueFromUi() {
         return new Range(RangeUi.this.lower.getValue(), Range.BoundType.CLOSED, RangeUi.this.upper.getValue(), Range.BoundType.CLOSED);
      }

      protected void updateUiWithValue(OperatorArgument var1) {
         if (var1 instanceof Range) {
            this.updateUi((Range)var1);
         } else {
            RangeUi.this.lower.setValue("");
            RangeUi.this.upper.setValue("");
         }
      }

      private void updateUi(Range var1) {
         RangeUi.this.lower.setValue(var1.lowerEndpoint());
         RangeUi.this.upper.setValue(var1.upperEndpoint());
      }

      private class UiListener extends DocumentContentChangeListener {
         private UiListener() {
         }

         @Override
         public void contentChanged(DocumentEvent var1) {
            if (ModelBinding.this.isUiToModelEnabled()) {
               ModelBinding.this.syncModel();
            }
         }
      }
   }
}
