package org.etlforge.ui.widget.overlay;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import org.etlforge.ui.Direction;

public final class OverlayRelativePositioner implements OverlayPositioner {
   private final JComponent relativeTo;
   private final Point offset;
   private boolean strict;

   public OverlayRelativePositioner(JComponent var1, Point var2) {
      this.relativeTo = var1;
      this.offset = new Point(var2);
   }

   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      Point var3 = SwingUtilities.convertPoint(this.relativeTo, new Point(), var1);
      var3.x = var3.x + this.offset.x;
      var3.y = var3.y + this.offset.y;
      if (!this.isStrict()) {
         ensureWithinContainer(var1, var2, var3);
      }

      JViewport var4 = getViewPortAncestor(var1);
      if (var4 != null) {
         ensureWithinViewPort(var1, var2, var4, var3);
      }

      return var3;
   }

   private static void ensureWithinContainer(JComponent var0, JComponent var1, Point var2) {
      int var3 = var2.x + var1.getWidth() - var0.getWidth();
      if (var3 > 0) {
         var2.x = Math.max(0, var2.x - var3);
      }

      int var4 = var2.y + var1.getHeight() - var0.getHeight();
      if (var4 > 0) {
         var2.y = Math.max(0, var2.y - var4);
      }
   }

   private static JViewport getViewPortAncestor(JComponent var0) {
      Container var1 = var0.getParent();

      while (var1 != null && !(var1 instanceof JViewport)) {
         var1 = var1.getParent();
      }

      return (JViewport)var1;
   }

   private static void ensureWithinViewPort(JComponent var0, JComponent var1, JViewport var2, Point var3) {
      Dimension var4 = var2.getExtentSize();
      Point var5 = SwingUtilities.convertPoint(var0, var3, var2.getView());
      int var6 = var5.x + var1.getWidth() - var4.width;
      if (var6 > 0) {
         var3.x = Math.max(0, var3.x - var6);
      }

      int var7 = var5.y + var1.getHeight() - var4.height;
      if (var7 > 0) {
         var3.y = Math.max(0, var3.y - var7);
      }
   }

   public void setStrict(boolean var1) {
      this.strict = var1;
   }

   public boolean isStrict() {
      return this.strict;
   }

   public OverlayPositioner animated(final Direction var1) {
      return new AnimatedOverlayPositioner(this) {
         @Override
         protected Point getStartPoint(JComponent var1x, JComponent var2) {
            Point var3 = OverlayRelativePositioner.this.getPosition(var1x, var2);
            switch (var1) {
               case UP:
                  return new Point(var3.x, var1x.getHeight());
               case RIGHT:
                  return new Point(-var2.getWidth(), var3.y);
               case DOWN:
                  return new Point(var3.x, -var2.getHeight());
               case LEFT:
                  return new Point(var1x.getWidth(), var3.y);
               default:
                  throw new RuntimeException("Unexpected direction: " + var1);
            }
         }
      };
   }
}
