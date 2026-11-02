package org.etlforge.integration.data.binding.ui;

import javax.swing.JToggleButton;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.util.UiUtils;

final class ToggleButtonBoundPropertyControl implements BoundProperty {
   private final String name;
   private final JToggleButton button;

   public ToggleButtonBoundPropertyControl(String var1, JToggleButton var2) {
      this.name = var1;
      this.button = var2;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void newValue(final Object var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ToggleButtonBoundPropertyControl.this.button.setSelected(ToggleButtonBoundPropertyControl.this.getValueToSet(var1));
         }
      });
   }

   private boolean getValueToSet(Object var1) {
      if (var1 instanceof Boolean) {
         return (Boolean)var1;
      }

      if (!(var1 instanceof String)) {
         return false;
      }

      String var2 = (String)var1;
      return var2.equals("1") || var2.equals("true");
   }
}
