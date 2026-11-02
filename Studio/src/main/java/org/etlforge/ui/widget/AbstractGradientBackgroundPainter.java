package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;

abstract class AbstractGradientBackgroundPainter implements ComponentPainter {
   private final Color firstEdgeColor;
   private final Color secondEdgeColor;

   protected AbstractGradientBackgroundPainter(Color var1, Color var2) {
      this.firstEdgeColor = Preconditions.checkNotNull(var1);
      this.secondEdgeColor = Preconditions.checkNotNull(var2);
   }

   @Override
   public final void postRendering(Component var1, Graphics2D var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      var2.setPaint(this.createPaintInstance(var3, var4, this.firstEdgeColor, this.secondEdgeColor));
      var2.fillRect(0, 0, var3, var4);
   }

   @Override
   public final Dimension getPreferredSize(Dimension var1) {
      return var1;
   }

   protected abstract Paint createPaintInstance(int var1, int var2, Color var3, Color var4);

   @Override
   public final boolean preRendering(Component var1, Graphics2D var2) {
      return false;
   }
}
