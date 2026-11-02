package org.etlforge.application.ui.look;

import java.awt.Color;
import org.etlforge.application.config.ApplicationConfiguration;

public final class ApplicationPropertyColor extends Color {
   private ApplicationPropertyColor(int[] var1) {
      super(var1[0], var1[1], var1[2]);
   }

   public static ApplicationPropertyColor load(String var0, Color var1) {
      String var2 = ApplicationConfiguration.getString(var0, null);
      if (var2 != null) {
         try {
            int[] var3 = getRGB(var2);
            return new ApplicationPropertyColor(var3);
         } catch (RuntimeException var4) {
         }
      }

      return new ApplicationPropertyColor(getRGB(var1));
   }

   private static int[] getRGB(String var0) {
      return new int[]{Integer.decode("0x" + var0.substring(0, 2)), Integer.decode("0x" + var0.substring(2, 4)), Integer.decode("0x" + var0.substring(4, 6))};
   }

   private static int[] getRGB(Color var0) {
      return new int[]{var0.getRed(), var0.getGreen(), var0.getBlue()};
   }
}
