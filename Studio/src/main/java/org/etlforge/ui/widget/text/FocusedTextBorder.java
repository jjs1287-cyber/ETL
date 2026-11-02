package org.etlforge.ui.widget.text;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D.Double;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.UiUtils;

public class FocusedTextBorder implements Border {
   private final Border normalBorder;

   public FocusedTextBorder(JTextComponent var1) {
      this.normalBorder = var1.getBorder();
   }

   @Override
   public Insets getBorderInsets(Component var1) {
      return this.normalBorder.getBorderInsets(var1);
   }

   @Override
   public boolean isBorderOpaque() {
      return false;
   }

   @Override
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Graphics2D var7 = (Graphics2D)var2.create();
      UiUtils.makeAntiAliased(var7);
      var7.setStroke(new BasicStroke(1.0F));
      var7.setColor(new Color(5547230));
      Double var8 = new Double(var3, var4, var5 - 0.5, var6 - 0.5);
      var7.draw(var8);
      var7.dispose();
   }
}
