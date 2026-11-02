package org.etlforge.integration.client.ui.core.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

final class GettingStartedCaption extends KongaLabel {
   public GettingStartedCaption() {
      super("ETLForge");
      getStyle().makeOver(this);
      this.setBorder(Empty.border(16, 30, 16, 0));
      this.setAntiAliasingEnabled(true);
      this.setForeground(Color.WHITE);
   }

   private static TextStyle getStyle() {
      TextStyle var0 = WizardStyleSheet.SECTION_STYLE;
      return TextStyle.adjustSize(var0, 4);
   }

   @Override
   protected void paintComponent(Graphics var1) {
      Shape var2 = this.createBackgroundShape();
      Color var3 = var1.getColor();
      var1.setColor(Colors.ETLFORGE_ORANGE);
      ((Graphics2D)var1).fill(var2);
      var1.setColor(var3);
      super.paintComponent(var1);
   }

   private Shape createBackgroundShape() {
      int var1 = this.getWidth();
      int var2 = this.getHeight();
      int[] var3 = new int[]{0, var1, var1, var1 - 24, 0};
      int[] var4 = new int[]{0, 0, (int)(0.55 * var2), var2, var2};
      return new Polygon(var3, var4, var3.length);
   }
}

