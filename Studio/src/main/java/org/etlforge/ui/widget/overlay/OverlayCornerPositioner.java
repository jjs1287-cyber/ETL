package org.etlforge.ui.widget.overlay;

import java.awt.Point;
import javax.swing.JComponent;
import org.etlforge.ui.graphics.RectangleCorner;

public enum OverlayCornerPositioner implements OverlayPositioner {
   UPPER_LEFT {
      @Override
      public Point getPosition(JComponent var1, JComponent var2) {
         return new Point(2, 2);
      }

      @Override
      public OverlayPositioner animated() {
         return new AnimatedOverlayPositioner(this) {
            @Override
            protected Point getStartPoint(JComponent var1, JComponent var2) {
               return new Point(2, -var2.getHeight());
            }
         };
      }
   },
   UPPER_RIGHT {
      @Override
      public Point getPosition(JComponent var1, JComponent var2) {
         return new Point(Math.max(0, var1.getWidth() - var2.getWidth() - 2), 2);
      }

      @Override
      public OverlayPositioner animated() {
         return new AnimatedOverlayPositioner(this) {
            @Override
            protected Point getStartPoint(JComponent var1, JComponent var2) {
               return new Point(Math.max(0, var1.getWidth() - var2.getWidth() - 2), -var2.getHeight());
            }
         };
      }
   },
   LOWER_LEFT {
      @Override
      public Point getPosition(JComponent var1, JComponent var2) {
         return new Point(2, Math.max(0, var1.getHeight() - var2.getHeight() - 2));
      }

      @Override
      public OverlayPositioner animated() {
         return new AnimatedOverlayPositioner(this) {
            @Override
            protected Point getStartPoint(JComponent var1, JComponent var2) {
               return new Point(2, var1.getHeight());
            }
         };
      }
   },
   LOWER_RIGHT {
      @Override
      public Point getPosition(JComponent var1, JComponent var2) {
         return new Point(Math.max(0, var1.getWidth() - var2.getWidth() - 2), Math.max(0, var1.getHeight() - var2.getHeight() - 2));
      }

      @Override
      public OverlayPositioner animated() {
         return new AnimatedOverlayPositioner(this) {
            @Override
            protected Point getStartPoint(JComponent var1, JComponent var2) {
               return new Point(Math.max(0, var1.getWidth() - var2.getWidth() - 2), var1.getHeight());
            }
         };
      }
   };

   public static final int PAD = 2;

   OverlayCornerPositioner() {
   }

   public abstract OverlayPositioner animated();

   public static OverlayCornerPositioner fromCorner(RectangleCorner var0) {
      switch (var0) {
         case UPPER_LEFT:
            return UPPER_LEFT;
         case UPPER_RIGHT:
            return UPPER_RIGHT;
         case LOWER_LEFT:
            return LOWER_LEFT;
         case LOWER_RIGHT:
            return LOWER_RIGHT;
         default:
            throw new IllegalArgumentException("Unknown corner: " + var0);
      }
   }
}
