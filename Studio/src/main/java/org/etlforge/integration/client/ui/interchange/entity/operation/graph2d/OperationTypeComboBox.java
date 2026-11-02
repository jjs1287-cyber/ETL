package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.awt.event.ItemListener;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class OperationTypeComboBox implements UiProvider {
   private final KongaComboBox combobox = new KongaComboBox(OperationType.getSelectableTypes());

   public OperationTypeComboBox() {
      this.combobox.setSelectedItem(OperationType.getDefaultType());
   }

   public void setSelectedOperationType(OperationType var1) {
      this.combobox.setSelectedItem(var1);
   }

   public OperationType getSelectedOperationType() {
      return (OperationType)this.combobox.getSelectedItem();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.combobox);
   }

   @Override
   public void requestFocus() {
      this.combobox.requestFocusInWindow();
   }

   public void addItemListener(ItemListener var1) {
      this.combobox.addItemListener(var1);
   }

   public void removeItemListener(ItemListener var1) {
      this.combobox.removeItemListener(var1);
   }

   public ComboBoxInputField asInputField(String var1) {
      return new ComboBoxInputField(var1, this.combobox);
   }
}
