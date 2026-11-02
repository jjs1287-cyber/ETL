package org.etlforge.ui.widget.text;

import javax.swing.JComponent;

public final class TextDecoratorUtils {
   public static void decorate(JComponent var0, TextDecorator var1) {
      var0.setFont(var1.getFont());
      var0.setForeground(var1.getTextColor());
   }

   private TextDecoratorUtils() {
      throw new UnsupportedOperationException();
   }
}
