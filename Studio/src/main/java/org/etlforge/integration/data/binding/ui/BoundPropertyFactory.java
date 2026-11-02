package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Function;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.ToggleButtonInputField;

public final class BoundPropertyFactory {
   public static BoundProperty nameProperty(TextInputField var0) {
      return new BoundNamePropertyControl("Name", var0.getInputComponent());
   }

   public static BoundProperty descriptionProperty(TextInputField var0) {
      return textProperty("Description", var0.getInputComponent());
   }

   public static BoundProperty textProperty(String var0, TextInputField var1) {
      return new BoundTextPropertyControl(var0, var1.getInputComponent());
   }

   public static BoundProperty textProperty(String var0, JTextComponent var1) {
      return new BoundTextPropertyControl(var0, var1);
   }

   public static BoundProperty passwordProperty(String var0, JTextComponent var1) {
      return new BoundTextPropertyControl(var0, var1, new Function<String, String>() {
         public String apply(String var1) {
            return var1 == null ? null : CC.reveal(var1);
         }
      });
   }

   public static BoundProperty comboProperty(String var0, ComboBoxInputField var1) {
      return comboProperty(var0, var1.getInputComponent());
   }

   public static BoundProperty comboProperty(String var0, JComboBox var1) {
      return new ComboBoxBoundPropertyControl(var0, var1);
   }

   public static BoundProperty toggleProperty(String var0, ToggleButtonInputField var1) {
      return toggleProperty(var0, var1.getInputComponent());
   }

   public static BoundProperty toggleProperty(String var0, JToggleButton var1) {
      return new ToggleButtonBoundPropertyControl(var0, var1);
   }

   private BoundPropertyFactory() {
   }
}
