package org.etlforge.ui.util.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

public final class NoTopEtchedBorder implements Border {
   private Color highlightColor;
   private Color shadowColor;

   public void setHighlightColor(Color var1) {
      this.highlightColor = var1;
   }

   public void setShadowColor(Color var1) {
      this.shadowColor = var1;
   }

   @Override
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      var2.translate(var3, var4);
      var2.setColor(var1.getBackground());
      var2.drawLine(0, 0, var5, 0);
      var2.setColor(this.getShadowColor(var1));
      var2.drawLine(0, 1, 0, var6 - 2);
      var2.drawLine(0, var6 - 2, var5 - 2, var6 - 2);
      var2.drawLine(var5 - 2, var6 - 2, var5 - 2, 1);
      var2.setColor(this.getHighlightColor(var1));
      var2.drawLine(1, var6 - 3, 1, 1);
      var2.drawLine(0, var6 - 1, var5 - 1, var6 - 1);
      var2.drawLine(var5 - 1, var6 - 1, var5 - 1, 0);
      var2.translate(-var3, -var4);
   }

   private Color getShadowColor(Component var1) {
      return this.shadowColor == null ? var1.getBackground().darker() : this.shadowColor;
   }

   private Color getHighlightColor(Component var1) {
      return this.highlightColor == null ? var1.getBackground().brighter() : this.highlightColor;
   }

   @Override
   public Insets getBorderInsets(Component var1) {
      return new Insets(2, 2, 2, 2);
   }

   @Override
   public boolean isBorderOpaque() {
      return true;
   }
}
