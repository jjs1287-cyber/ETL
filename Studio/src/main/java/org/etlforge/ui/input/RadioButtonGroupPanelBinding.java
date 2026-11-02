package org.etlforge.ui.input;

import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.util.Property;

final class RadioButtonGroupPanelBinding<T> extends AbstractBinding<T> {
   private final RadioButtonGroupPanel<T> buttons;
   private final Receiver<T> uiListener = new Receiver<T>() {
      @Override
      public void handle(T var1) {
         RadioButtonGroupPanelBinding.this.syncModel();
      }
   };

   public RadioButtonGroupPanelBinding(Property<T> var1, RadioButtonGroupPanel<T> var2) {
      super(var1);
      this.buttons = var2;
      var2.addSelectionListener(this.uiListener);
   }

   @Override
   protected void disposeUiListener() {
      this.buttons.removeSelectionListener(this.uiListener);
   }

   @Override
   protected T getValueFromUi() {
      return this.buttons.getSelection();
   }

   @Override
   protected void updateUiWithValue(T var1) {
      this.buttons.setSelectedValue((T)var1);
   }
}
