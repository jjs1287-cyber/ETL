package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import java.util.EnumSet;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.data.structure.text.validation.InvalidRecordAction;
import org.etlforge.integration.data.structure.text.validation.NumberOfFieldsValidation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;

final class NumberOfFieldsValidationUi implements UiProvider {
   private final IntegerInputField numberField = new IntegerInputField("Must contain X columns", new KongaTextField(3), 1, Integer.MAX_VALUE, "");
   private final ComboBoxInputField actionSelector;

   public NumberOfFieldsValidationUi() {
      this.numberField.setDocument(new IntegerDocument());
      this.numberField.setRequired(false);
      this.actionSelector = createActionSelector();
      this.numberField.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            boolean var2 = NumberOfFieldsValidationUi.this.numberField.getIntValue(-1) > 0;
            NumberOfFieldsValidationUi.this.actionSelector.setEnabled(var2);
         }
      });
   }

   private static ComboBoxInputField createActionSelector() {
      EnumComboBox var0 = EnumComboBox.of(EnumSet.of(InvalidRecordAction.SKIP_ROW, InvalidRecordAction.RAISE_ERROR));
      var0.generateDisplayStrings(Functions.toStringFunction());
      var0.adjustPrototypeValue();
      ComboBoxInputField var1 = new ComboBoxInputField("Action", var0, "Action");
      var1.setEnabled(false);
      return var1;
   }

   public void setNumberOfFieldsInStructure(int var1) {
      if (var1 != this.numberField.getMaximumValue()) {
         this.numberField.setMaximumValue(var1);
         this.numberField.setEnabled(var1 > 0);
         this.numberField.decorateField();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = Horizontal.box()
         .withBorder(StyleSheet.getDefaultTitleBorder("Validation (Optional)"))
         .add("Row must contain at least ")
         .add(this.numberField.getInputComponent())
         .add(" columns. If not: ")
         .add(this.actionSelector.getInputComponent())
         .invisible();
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.numberField.requestFocus();
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.numberField);
      var1.addInputField(this.actionSelector);
   }

   public void setValue(Optional<NumberOfFieldsValidation> var1) {
      if (var1.isPresent()) {
         NumberOfFieldsValidation var2 = (NumberOfFieldsValidation)var1.get();
         this.numberField.setValue(var2.getRequiredNumberOfColumns());
         this.actionSelector.setSelectedItem(var2.getAction());
         this.actionSelector.setEnabled(true);
      } else {
         this.numberField.setValue("");
         this.actionSelector.setEnabled(false);
      }
   }

   public Optional<NumberOfFieldsValidation> getValue() {
      int var1 = this.numberField.getIntValue(-1);
      if (var1 > 0) {
         InvalidRecordAction var2 = (InvalidRecordAction)this.actionSelector.getSelectedItem();
         return Optional.of(new NumberOfFieldsValidation(var1, var2));
      } else {
         return Optional.absent();
      }
   }

   public void validateInput() throws InputPanelException {
      try {
         this.numberField.validateField();
      } catch (InputFieldException var2) {
         throw new InputPanelException("Invalid validation setting: Number of columns must be between 1 and " + this.numberField.getMaximumValue());
      }
   }
}
