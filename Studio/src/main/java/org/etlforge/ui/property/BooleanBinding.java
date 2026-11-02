package org.etlforge.ui.property;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JToggleButton;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public class BooleanBinding extends AbstractBinding<Boolean> {
   private final JToggleButton component;
   private final BooleanBinding.UiListener uiListener;

   public static BooleanBinding bind(AbstractPropertyBasedModel var0, String var1, JToggleButton var2) {
      Property var3 = var0.getProperty(var1);
      return bind(var3, var2);
   }

   public static BooleanBinding bind(Property<Boolean> var0, JToggleButton var1) {
      return new BooleanBinding(var0, var1);
   }

   public static BooleanBinding bindAndSyncUi(AbstractPropertyBasedModel var0, String var1, JToggleButton var2) {
      Property var3 = var0.getProperty(var1);
      return bindAndSyncUi(var3, var2);
   }

   public static BooleanBinding bindAndSyncUi(Property<Boolean> var0, JToggleButton var1) {
      BooleanBinding var2 = bind(var0, var1);
      var2.syncUi();
      return var2;
   }

   public BooleanBinding(Property<Boolean> var1, JToggleButton var2) {
      super(var1);
      this.component = var2;
      this.uiListener = new BooleanBinding.UiListener();
      var2.addItemListener(this.uiListener);
   }

   @Override
   protected void disposeUiListener() {
      this.component.removeItemListener(this.uiListener);
   }

   protected Boolean getValueFromUi() {
      return this.component.isSelected();
   }

   protected void updateUiWithValue(Boolean var1) {
      this.component.setSelected(var1);
   }

   private class UiListener implements ItemListener {
      private UiListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         switch (var1.getStateChange()) {
            case 1:
            case 2:
               if (BooleanBinding.this.isUiToModelEnabled()) {
                  BooleanBinding.this.syncModel();
               }
         }
      }
   }
}
