package org.etlforge.integration.client.database.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.util.UiUtils;

public final class TableIcon implements Icon {
   private static final int HEIGHT = 16;
   private static final int WIDTH = 16;
   private Color color = Colors.THEME_TEXT_COLOR;

   public void setColor(Color var1) {
      this.color = var1;
   }

   public void restoreDefaultColor() {
      this.color = Colors.THEME_TEXT_COLOR;
   }

   @Override
   public int getIconHeight() {
      return 16;
   }

   @Override
   public int getIconWidth() {
      return 16;
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      Graphics2D var5 = (Graphics2D)var2;
      GraphicsState var6 = new GraphicsState(var5, GraphicsState.COLOR, GraphicsState.STROKE, GraphicsState.RENDERING_HINTS);
      this.prepareGraphics(var1, var5);
      this.drawRect(var3, var4, var5);
      this.drawT(var3, var4, var5);
      var6.restore();
   }

   private void prepareGraphics(Component var1, Graphics2D var2) {
      var2.setStroke(new BasicStroke(1.5F));
      UiUtils.makeAntiAliased(var2);
   }

   private void drawRect(int var1, int var2, Graphics2D var3) {
      var3.setColor(Color.WHITE);
      var3.fillRect(var1 + 1, var2 + 1, 14, 14);
      var3.setColor(this.color);
      var3.drawRoundRect(var1 + 1, var2 + 1, 14, 14, 2, 2);
   }

   private void drawT(int var1, int var2, Graphics2D var3) {
      int var4 = var2 + 5;
      var3.drawLine(var1 + 5, var4, var1 + 16 - 5, var4);
      int var5 = var1 + 8;
      var3.drawLine(var5, var2 + 6, var5, var2 + 16 - 5);
   }
}
