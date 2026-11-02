package org.etlforge.ui.style;

import java.awt.Color;
import javax.swing.AbstractButton;
import javax.swing.border.Border;
import org.etlforge.ui.util.border.BorderBuilder;

public class ButtonStyle extends TextStyle {
   private final Border border;

   public ButtonStyle(String var1, int var2, int var3, Color var4, Border var5, Border var6) {
      super(var1, var2, var3, var4);
      this.border = new BorderBuilder(var5, var6);
   }

   public ButtonStyle(String var1, int var2, int var3, Border var4, Border var5) {
      super(var1, var2, var3);
      this.border = new BorderBuilder(var4, var5);
   }

   public ButtonStyle(String var1, int var2, Border var3, Border var4) {
      super(var1, var2);
      this.border = new BorderBuilder(var3, var4);
   }

   @Override
   public void makeOver(AbstractButton... var1) {
      super.makeOver(var1);

      for (AbstractButton var5 : var1) {
         var5.setBorder(this.border);
      }
   }
}
