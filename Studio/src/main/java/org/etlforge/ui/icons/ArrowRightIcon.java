package org.etlforge.ui.icons;

import com.google.common.base.Preconditions;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.util.UiUtils;

public final class ArrowRightIcon implements Icon {
   private final int width;
   private final int height;
   private final Color color;

   public ArrowRightIcon(int var1, int var2, Color var3) {
      this.width = ArgCheck.checkPositive(var1);
      this.height = ArgCheck.checkPositive(var2);
      this.color = Preconditions.checkNotNull(var3);
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      Graphics2D var5 = (Graphics2D)var2;
      GraphicsState var6 = new GraphicsState(var5, GraphicsState.COLOR, GraphicsState.STROKE);
      this.prepareGraphics(var1, var5);
      this.drawTriangle(var5, var3, var4);
      var6.restore();
   }

   private void prepareGraphics(Component var1, Graphics2D var2) {
      var2.setStroke(new BasicStroke(1.5F));
      UiUtils.makeAntiAliased(var2);
      var2.setColor(this.color);
   }

   private void drawTriangle(Graphics2D var1, int var2, int var3) {
      var1.fillPolygon(new int[]{var2, var2 + this.getIconWidth(), var2}, new int[]{var3, var3 + this.getIconHeight() / 2, var3 + this.getIconHeight()}, 3);
   }

   @Override
   public int getIconWidth() {
      return this.width;
   }

   @Override
   public int getIconHeight() {
      return this.height;
   }
}
