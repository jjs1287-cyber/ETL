package org.etlforge.ui.widget.window;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public final class WindowUtils {
   public static void centerWindowOnScreen(Window var0) {
      Point var1 = getLocationToCenterOnScreen(var0);
      var0.setLocation(var1.x, var1.y);
   }

   public static Point getLocationToCenterOnScreen(Window var0) {
      Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension var2 = var0.getSize();
      int var3 = (var1.width - var2.width) / 2;
      int var4 = (var1.height - var2.height) / 2;
      return new Point(var3, var4);
   }

   public static boolean isWindowCompletelyVisibleOnScreen(Window var0) {
      Rectangle var1 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      return var1.contains(var0.getBounds());
   }

   public static void ensureWindowCompletelyVisibleOnScreen(Window var0) {
      if (!isWindowCompletelyVisibleOnScreen(var0)) {
         Rectangle var1 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         Rectangle var2 = var0.getBounds();
         if (var2.y < var1.y) {
            var2.y = var1.y;
         } else if (var2.y + var2.height > var1.y + var1.height) {
            var2.y = var2.y - (var2.y + var2.height - (var1.y + var1.height));
         }

         if (var2.x + var2.width > var1.x + var1.width) {
            var2.x = var2.x - (var2.x + var2.width - (var1.x + var1.width));
         } else if (var2.x < var1.x) {
            var2.x = var1.x;
         }

         var0.setBounds(var2);
      }
   }

   public static void shrinkWindowIfNeededToFitScreen(Window var0) {
      if (!isWindowCompletelyVisibleOnScreen(var0)) {
         Rectangle var1 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         Rectangle var2 = var0.getBounds();
         int var3 = var2.x + var2.width - var1.width;
         var2.width = var2.width - Math.max(0, var3);
         int var4 = var2.y + var2.height - var1.height;
         var2.height = var2.height - Math.max(0, var4);
         var0.setBounds(var2);
      }
   }

   public static boolean contains(Window var0, Point var1, int var2, int var3) {
      Rectangle var4 = var0.getBounds();
      var4.x -= var2;
      var4.width += 2 * var2;
      var4.y -= var3;
      var4.height += 2 * var3;
      return var4.contains(var1);
   }

   public static void ensureMinimumSize(Window var0, int var1, int var2) {
      boolean var3 = false;
      Dimension var4 = var0.getSize();
      if (var4.width < var1) {
         var4.width = var1;
         var3 = true;
      }

      if (var4.height < var2) {
         var4.height = var2;
         var3 = true;
      }

      if (var3) {
         var0.setSize(var4);
      }
   }

   public static void ensureChildWindowNotBiggerThanOwner(Dimension var0, Window var1, int var2) {
      Dimension var3 = var1.getSize();
      if (var0.width > var3.width - var2) {
         var0.width = var3.width - var2;
      }

      if (var0.height > var3.height - var2) {
         var0.height = var3.height - var2;
      }
   }

   private WindowUtils() {
   }
}
