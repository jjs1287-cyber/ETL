package org.etlforge.ui.util.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.border.AbstractBorder;
import org.etlforge.ui.util.UiUtils;

public class RoundedBorder extends AbstractBorder {
   private final int width;
   private final int height;
   private final Color color;
   private final int thickness;
   private transient Stroke stroke;

   public RoundedBorder() {
      this(6, 6);
   }

   public RoundedBorder(int var1, int var2) {
      this(var1, var2, 1, Color.BLACK);
   }

   public RoundedBorder(int var1, int var2, Color var3) {
      this(var1, var2, 1, var3);
   }

   public RoundedBorder(int var1, int var2, int var3, Color var4) {
      this.width = var1;
      this.height = var2;
      this.color = var4;
      this.thickness = var3;
   }

   @Override
   public Insets getBorderInsets(Component var1) {
      return new Insets(this.thickness, this.thickness, this.thickness, this.thickness);
   }

   @Override
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      var5--;
      var6--;
      Graphics2D var7 = (Graphics2D)var2;
      Color var8 = var7.getColor();
      Stroke var9 = var7.getStroke();
      RenderingHints var10 = var7.getRenderingHints();
      var7.setColor(this.color);
      var7.setStroke(this.getStroke());
      UiUtils.makeAntiAliased(var7);
      var7.drawRoundRect(var3, var4, var5, var6, this.width, this.height);
      var7.setRenderingHints(var10);
      var7.setStroke(var9);
      var7.setColor(var8);
   }

   private Stroke getStroke() {
      if (this.stroke == null) {
         this.stroke = new BasicStroke(this.thickness);
      }

      return this.stroke;
   }
}
