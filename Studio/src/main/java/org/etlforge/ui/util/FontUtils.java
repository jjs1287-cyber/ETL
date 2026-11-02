package org.etlforge.ui.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public final class FontUtils {
   private static Font[] availableFonts;
   private static String[] availableFontFamilyNames;

   public static synchronized Font[] getAvailableFonts() {
      if (availableFonts == null) {
         GraphicsEnvironment var0 = GraphicsEnvironment.getLocalGraphicsEnvironment();
         availableFonts = var0.getAllFonts();
         if (availableFonts == null) {
            availableFonts = new Font[0];
         }
      }

      return (Font[])availableFonts.clone();
   }

   public static synchronized String[] getAvailableFontFamilyNames() {
      if (availableFontFamilyNames == null) {
         GraphicsEnvironment var0 = GraphicsEnvironment.getLocalGraphicsEnvironment();
         availableFontFamilyNames = var0.getAvailableFontFamilyNames();
         if (availableFontFamilyNames == null) {
            availableFontFamilyNames = new String[0];
         }
      }

      return (String[])availableFontFamilyNames.clone();
   }

   public static Font getMatchingFont(String[] var0, int var1, int var2) {
      if (var0 != null) {
         for (String var6 : var0) {
            for (String var10 : getAvailableFontFamilyNames()) {
               if (var6.trim().equalsIgnoreCase(var10)) {
                  return new Font(var6, var2, var1);
               }
            }
         }
      }

      return null;
   }

   private FontUtils() {
   }
}
