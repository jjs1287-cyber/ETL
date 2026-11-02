package org.etlforge.ui.util;

import java.awt.Component;
import java.awt.Font;

public final class Fonts {
   public static Font bold(Component var0) {
      return bold(var0.getFont());
   }

   public static Font bold(Font var0) {
      return var0.deriveFont(1);
   }

   public static void convertToBold(Component var0) {
      Font var1 = bold(var0);
      var0.setFont(var1);
   }

   public static Font italic(Component var0) {
      return italic(var0.getFont());
   }

   public static Font italic(Font var0) {
      return var0.deriveFont(2);
   }

   public static void convertToItalic(Component var0) {
      Font var1 = italic(var0);
      var0.setFont(var1);
   }

   private Fonts() {
   }
}
