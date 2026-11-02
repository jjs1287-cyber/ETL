package org.etlforge.ui.widget;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public final class ComponentPainters {
   private ComponentPainters() {
   }

   public static ComponentPainter horizontalGradient(Color var0, Color var1) {
      return new AbstractGradientBackgroundPainter(var0, var1) {
         @Override
         protected Paint createPaintInstance(int var1, int var2, Color var3, Color var4) {
            return new GradientPaint(0.0F, 0.0F, var3, var1, 0.0F, var4);
         }
      };
   }

   public static ComponentPainter verticalGradient(Color var0, Color var1) {
      return new AbstractGradientBackgroundPainter(var0, var1) {
         @Override
         protected Paint createPaintInstance(int var1, int var2, Color var3, Color var4) {
            return new GradientPaint(0.0F, 0.0F, var3, 0.0F, var2, var4);
         }
      };
   }
}
