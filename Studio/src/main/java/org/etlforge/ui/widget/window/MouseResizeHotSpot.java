package org.etlforge.ui.widget.window;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public enum MouseResizeHotSpot {
   SOUTHEAST_CORNER(5) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(var2.width - 8, var2.height - 8, 16, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new SouthEastResizeStrategy(var1);
      }
   },
   SOUTHWEST_CORNER(4) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(-8, var2.height - 8, 16, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new SouthWestResizeStrategy(var1);
      }
   },
   NORTHEAST_CORNER(7) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(var2.width - 8, -8, 16, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new NorthEastResizeStrategy(var1);
      }
   },
   NORTHWEST_CORNER(6) {
      @Override
      protected Rectangle getBounds(Component var1) {
         return new Rectangle(-8, -8, 16, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new NorthWestResizeStrategy(var1);
      }
   },
   SOUTH_EDGE(9) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(0, var2.height - 8, var2.width, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new SouthEdgeResizeStrategy(var1);
      }
   },
   EAST_EDGE(11) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(var2.width - 8, 0, 16, var2.height);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new EastEdgeResizeStrategy(var1);
      }
   },
   WEST_EDGE(10) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(-8, 0, 16, var2.height);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new WestEdgeResizeStrategy(var1);
      }
   },
   NORTH_EDGE(8) {
      @Override
      protected Rectangle getBounds(Component var1) {
         Dimension var2 = var1.getSize();
         return new Rectangle(0, -8, var2.width, 16);
      }

      @Override
      protected MouseResizeStrategy createStrategy(Window var1) {
         return new NorthEdgeResizeStrategy(var1);
      }
   };

   private final Cursor cursor;

   MouseResizeHotSpot(int var3) {
      this.cursor = Cursor.getPredefinedCursor(var3);
   }

   public static MouseResizeHotSpot getHotSpot(Window var0, MouseEvent var1) {
      return getHotSpot(var0, getPoint(var0, var1));
   }

   public static MouseResizeHotSpot getHotSpot(Window var0, Point var1) {
      for (MouseResizeHotSpot var5 : values()) {
         if (var5.isActive(var0, var1)) {
            return var5;
         }
      }

      return null;
   }

   public final boolean isActive(Component var1, MouseEvent var2) {
      Point var3 = getPoint(var1, var2);
      return this.isActive(var1, var3);
   }

   public final boolean isActive(Component var1, Point var2) {
      Rectangle var3 = this.getBounds(var1);
      return var3.contains(var2);
   }

   protected abstract Rectangle getBounds(Component var1);

   protected abstract MouseResizeStrategy createStrategy(Window var1);

   public final Cursor getCursor() {
      return this.cursor;
   }

   private static Point getPoint(Component var0, MouseEvent var1) {
      Component var2 = var1.getComponent();
      SwingUtilities.convertMouseEvent(var2, var1, var0);
      return var1.getPoint();
   }
}
