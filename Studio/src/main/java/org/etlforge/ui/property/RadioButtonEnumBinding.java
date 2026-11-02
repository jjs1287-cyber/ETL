package org.etlforge.ui.property;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumMap;
import javax.swing.JRadioButton;
import org.etlforge.collections.KongaMaps;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class RadioButtonEnumBinding<E extends Enum<E>> extends AbstractBinding<E> {
   private final EnumMap<E, ? extends JRadioButton> buttons;
   private final RadioButtonEnumBinding<E>.ButtonListener buttonListener;

   public static <V extends Enum<V>> RadioButtonEnumBinding<V> forButtons(Property<V> var0, EnumMap<V, ? extends JRadioButton> var1) {
      return new RadioButtonEnumBinding<>(var0, var1);
   }

   public RadioButtonEnumBinding(AbstractPropertyBasedModel var1, String var2, EnumMap<E, ? extends JRadioButton> var3) {
      this(var1.getProperty(var2), var3);
   }

   public RadioButtonEnumBinding(Property<E> var1, EnumMap<E, ? extends JRadioButton> var2) {
      super(var1);
      this.buttons = var2.clone();
      this.buttonListener = new RadioButtonEnumBinding.ButtonListener();
      ButtonUtils.addItemListener(this.buttonListener, var2.values());
      ButtonUtils.makeMutuallyExclusive(var2.values());
   }

   protected final void updateUiWithValue(E var1) {
      JRadioButton var2 = this.buttons.get(var1);
      if (var2 != null) {
         var2.setSelected(true);
      }
   }

   @Override
   protected final void disposeUiListener() {
      for (JRadioButton var2 : this.buttons.values()) {
         var2.removeItemListener(this.buttonListener);
      }
   }

   protected final E getValueFromUi() {
      return KongaMaps.findKey(this.buttons, ButtonUtils.TOGGLE_BUTTON_SELECTED).orNull();
   }

   public final JRadioButton getButton(E var1) {
      return this.buttons.get(var1);
   }

   private class ButtonListener implements ItemListener {
      private ButtonListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (RadioButtonEnumBinding.this.isUiToModelEnabled() && this.isSelection(var1)) {
            RadioButtonEnumBinding.this.syncModel();
         }
      }

      private boolean isSelection(ItemEvent var1) {
         return var1.getStateChange() == 1;
      }
   }
}
