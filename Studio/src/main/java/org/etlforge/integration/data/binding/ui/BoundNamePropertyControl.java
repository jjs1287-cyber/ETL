package org.etlforge.integration.data.binding.ui;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.name.Name;

class BoundNamePropertyControl implements BoundProperty {
   private final String name;
   private final JTextComponent field;

   public BoundNamePropertyControl(String var1, JTextComponent var2) {
      this.name = var1;
      this.field = var2;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void newValue(Object var1) {
      if (var1 instanceof Name) {
         this.newValue(((Name)var1).getName());
      }
   }

   private void newValue(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (!var1.equals(BoundNamePropertyControl.this.field.getText())) {
               BoundNamePropertyControl.this.field.setText(var1);
            }
         }
      });
   }
}
