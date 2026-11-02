package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;

abstract class BooleanOperationOptionUi implements OperationOptionUi {
   private final CheckBoxInputField checkBox;

   protected BooleanOperationOptionUi(String var1) {
      this.checkBox = InputFieldFactory.getCheckBoxInputField(var1, var1);
   }

   @Override
   public final void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.checkBox);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.checkBox.getInputComponent());
   }

   @Override
   public final void requestFocus() {
      this.checkBox.requestFocus();
   }

   protected final boolean isSelected() {
      return this.checkBox.isSelected();
   }

   protected final void setSelected(boolean var1) {
      this.checkBox.setSelected(var1);
   }

   protected final void setEnabled(boolean var1) {
      this.checkBox.setEnabled(var1);
   }
}
