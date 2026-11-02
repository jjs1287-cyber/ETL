package org.etlforge.ui.widget.text;

import com.google.common.base.Supplier;
import javax.swing.text.JTextComponent;

public final class TextComponentStringSupplier implements Supplier<String> {
   private final JTextComponent textComponent;
   private final boolean autoTrim;

   public TextComponentStringSupplier(JTextComponent var1) {
      this(var1, false);
   }

   public TextComponentStringSupplier(JTextComponent var1, boolean var2) {
      this.textComponent = var1;
      this.autoTrim = var2;
   }

   public String get() {
      String var1 = this.textComponent.getText();
      if (this.autoTrim) {
         var1 = var1.trim();
      }

      return var1;
   }
}
