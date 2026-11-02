package org.etlforge.integration.data.binding.ui;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.ui.util.UiUtils;

class BoundTextPropertyControl implements BoundProperty {
   private final String name;
   private final JTextComponent field;
   private final Function<String, String> valueTransform;

   public BoundTextPropertyControl(String var1, JTextComponent var2) {
      this(var1, var2, Functions.identity());
   }

   public BoundTextPropertyControl(String var1, JTextComponent var2, Function<String, String> var3) {
      this.name = var1;
      this.field = var2;
      this.valueTransform = var3;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void newValue(Object var1) {
      if (var1 == null) {
         this.onNullValue();
      } else if (var1 instanceof String) {
         this.newValue((String)var1);
      }
   }

   private void onNullValue() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (BoundTextPropertyControl.this.field.getDocument().getLength() > 0) {
               BoundTextPropertyControl.this.field.setText("");
            }
         }
      });
   }

   private void newValue(String var1) {
      final String var2 = this.valueTransform.apply(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (!var2.equals(BoundTextPropertyControl.this.field.getText())) {
               BoundTextPropertyControl.this.field.setText(var2);
               BoundTextPropertyControl.this.field.setCaretPosition(0);
            }
         }
      });
   }
}
