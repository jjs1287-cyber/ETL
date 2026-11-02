package org.etlforge.ui.util.border;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public final class BorderUtils {
   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

   public static Border getEmptyPaddedBorder(Border var0, Insets var1, Insets var2) {
      if (var1 == null) {
         var1 = EMPTY_INSETS;
      }

      if (var2 == null) {
         var2 = EMPTY_INSETS;
      }

      return new BorderBuilder().empty(var1).add(var0).empty(var2);
   }

   public static Border getEmptyPaddedLineBorder(Insets var0, Insets var1) {
      return getEmptyPaddedLineBorder(var0, var1, Color.BLACK, 1);
   }

   public static Border getEmptyPaddedLineBorder(Insets var0, Insets var1, Color var2, int var3) {
      if (var0 == null) {
         var0 = EMPTY_INSETS;
      }

      if (var1 == null) {
         var1 = EMPTY_INSETS;
      }

      return new BorderBuilder().empty(var0).line(var2, var3).empty(var1);
   }

   public static Border getEmptyPaddedTitledBorder(Insets var0, Insets var1, String var2, Border var3) {
      return getEmptyPaddedTitledBorder(var0, var1, var2, var3, 0, 0, null, null);
   }

   public static Border getEmptyPaddedTitledBorder(Insets var0, Insets var1, String var2, Border var3, Color var4, Font var5) {
      return getEmptyPaddedTitledBorder(var0, var1, var2, var3, 0, 0, var4, var5);
   }

   public static Border getEmptyPaddedTitledBorder(Insets var0, Insets var1, String var2, Border var3, int var4, int var5) {
      return getEmptyPaddedTitledBorder(var0, var1, var2, var3, var4, var5, null, null);
   }

   public static Border getEmptyPaddedTitledBorder(Insets var0, Insets var1, String var2, Border var3, int var4, int var5, Color var6, Font var7) {
      if (var0 == null) {
         var0 = EMPTY_INSETS;
      }

      if (var1 == null) {
         var1 = EMPTY_INSETS;
      }

      TitledBorder var8 = BorderFactory.createTitledBorder(var3, var2, var4, var5);
      if (var6 != null) {
         var8.setTitleColor(var6);
      }

      if (var7 != null) {
         var8.setTitleFont(var7);
      }

      return new BorderBuilder().empty(var0).add(var8).empty(var1);
   }

   private BorderUtils() {
   }
}
