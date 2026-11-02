package org.etlforge.ui.util.border;

import com.google.common.base.Preconditions;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import javax.swing.border.Border;

public class UnderlineBorder implements Border {
   private final Color color;
   private final int thickness;

   public UnderlineBorder() {
      this(Color.BLACK, 1);
   }

   public UnderlineBorder(Color var1) {
      this(var1, 1);
   }

   public UnderlineBorder(int var1) {
      this(Color.BLACK, var1);
   }

   public UnderlineBorder(Color var1, int var2) {
      Preconditions.checkNotNull(var1, "color");
      Preconditions.checkArgument(var2 > 0, "thickness must be > 0");
      this.color = var1;
      this.thickness = var2;
   }

   @Override
   public Insets getBorderInsets(Component var1) {
      return new Insets(0, 0, this.thickness, 0);
   }

   @Override
   public boolean isBorderOpaque() {
      return false;
   }

   @Override
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Graphics2D var7 = (Graphics2D)var2;
      Stroke var8 = var7.getStroke();
      Color var9 = var7.getColor();
      var7.setStroke(new BasicStroke(this.thickness));
      var7.setColor(this.color);
      int var10 = var4 + var6 - this.thickness / 2 - 1;
      var7.drawLine(var3, var10, var3 + var5, var10);
      var7.setColor(var9);
      var7.setStroke(var8);
   }
}
