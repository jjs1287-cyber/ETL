package org.etlforge.ui.widget.window;

import java.awt.Color;
import java.awt.Font;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;

public class PopupWindowTitle implements LabelDecorator {
   private final MnemonicString text;
   private final Color foreground;
   private final Color background;
   private final Font font;

   public PopupWindowTitle(String var1) {
      this(var1, null, null, null);
   }

   public PopupWindowTitle(String var1, Color var2) {
      this(var1, var2, null, null);
   }

   public PopupWindowTitle(String var1, Color var2, Color var3, Font var4) {
      this.text = MnemonicStringFactory.NO_MNEMONIC_FACTORY.fromString(var1);
      this.foreground = var2;
      this.background = var3;
      this.font = var4;
   }

   @Override
   public void decorate(Label var1) {
      var1.setText(this.text);
      if (this.font != null) {
         var1.setFont(this.font);
      }

      if (this.foreground != null) {
         var1.setTextColor(this.foreground);
      }

      if (this.background != null) {
         var1.setBackgroundColor(this.background);
      }
   }
}
