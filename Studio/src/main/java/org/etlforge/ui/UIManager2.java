package org.etlforge.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.UIManager;

public final class UIManager2 {
   public static Color getColor(Object var0, Color var1) {
      Color var2 = UIManager.getColor(var0);
      return var2 != null ? var2 : var1;
   }

   public static int getInt(Object var0, int var1) {
      int var2 = UIManager.getInt(var0);
      return var2 != 0 ? var2 : var1;
   }

   public static Icon getIcon(Object var0, Icon var1) {
      Icon var2 = UIManager.getIcon(var0);
      return var2 != null ? var2 : var1;
   }

   public static Font getFont(Object var0, Font var1) {
      Font var2 = UIManager.getFont(var0);
      return var2 != null ? var2 : var1;
   }

   private UIManager2() {
   }
}
