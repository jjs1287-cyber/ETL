package org.etlforge.ui.widget;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class ComponentIcon implements Icon, Serializable {
   private final ImageIcon image;

   public ComponentIcon(Component var1) {
      this(var1, 1.0);
   }

   public ComponentIcon(Component var1, double var2) {
      BufferedImage var4 = this.createImage(var1, var2);
      this.image = new ImageIcon(var4);
   }

   @Override
   public int getIconHeight() {
      return this.image.getIconHeight();
   }

   @Override
   public int getIconWidth() {
      return this.image.getIconWidth();
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      this.image.paintIcon(var1, var2, var3, var4);
   }

   private BufferedImage createImage(Component var1, double var2) {
      Rectangle var4 = new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
      BufferedImage var5 = new BufferedImage((int)(var2 * var4.width), (int)(var2 * var4.height), 1);
      Graphics2D var6 = var5.createGraphics();
      if (var2 < 1.0) {
         var6.scale(var2, var2);
      }

      var6.setColor(var1.getBackground());
      var6.fill(var4);
      var6.setClip(var4);
      var1.paint(var6);
      var6.dispose();
      return var5;
   }
}
