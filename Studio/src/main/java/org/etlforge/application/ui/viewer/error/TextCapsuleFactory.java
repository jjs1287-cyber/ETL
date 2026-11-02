package org.etlforge.application.ui.viewer.error;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

final class TextCapsuleFactory {
   private TextCapsuleFactory() {
      throw new UnsupportedOperationException();
   }

   static JComponent encapsulate(JTextComponent var0, Icon var1, boolean var2) {
      JLabel var3 = new JLabel(var1);
      Box var4 = Box.createVerticalBox();
      var4.add(var3);
      var4.add(Box.createVerticalGlue());
      Box var5 = Box.createHorizontalBox();
      var5.add(var4);
      var5.add(Box.createHorizontalStrut(5));
      if (var2) {
         var5.add(new JScrollPane(var0));
      } else {
         var0.setOpaque(false);
         var5.add(var0);
      }

      return var5;
   }
}
