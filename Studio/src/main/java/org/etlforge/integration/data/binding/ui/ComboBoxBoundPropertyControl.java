package org.etlforge.integration.data.binding.ui;

import javax.swing.JComboBox;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.util.UiUtils;

class ComboBoxBoundPropertyControl implements BoundProperty {
   private final String name;
   private final JComboBox combo;

   public ComboBoxBoundPropertyControl(String var1, JComboBox var2) {
      this.name = var1;
      this.combo = var2;
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
            if (var1 != ComboBoxBoundPropertyControl.this.combo.getSelectedItem()) {
               ComboBoxBoundPropertyControl.this.combo.setSelectedItem(var1);
            }
         }
      });
   }
}
