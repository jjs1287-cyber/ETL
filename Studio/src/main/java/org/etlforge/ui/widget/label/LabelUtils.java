package org.etlforge.ui.widget.label;

import javax.swing.JLabel;
import org.etlforge.ui.MnemonicString;

public final class LabelUtils {
   public static void applyMnemonics(JLabel var0, MnemonicString var1) {
      var0.setText(var1.getText());
      char var2 = var1.getMnemonic();
      if (var2 != 0) {
         var0.setDisplayedMnemonic(var2);
      }

      int var3 = var1.getDisplayedMnemonicIndex();
      if (var3 >= 0) {
         var0.setDisplayedMnemonicIndex(var3);
      }
   }

   private LabelUtils() {
   }
}
