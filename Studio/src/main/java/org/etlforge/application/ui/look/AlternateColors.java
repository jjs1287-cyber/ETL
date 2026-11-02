package org.etlforge.application.ui.look;

import java.awt.Color;

public final class AlternateColors {
   private static final Color[] colors = new Color[]{Color.WHITE, new Color(250, 254, 255)};

   public static Color[] get() {
      return (Color[])colors.clone();
   }

   public static Color get(int var0) {
      return colors[var0 % colors.length];
   }

   private AlternateColors() {
   }
}
