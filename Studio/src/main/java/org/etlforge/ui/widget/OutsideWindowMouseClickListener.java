package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.etlforge.collections.Pair;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.Disposable;

public class OutsideWindowMouseClickListener implements Disposable {
   private final JWindow window;
   private final OutsideWindowMouseClickListener.Listener listener = new OutsideWindowMouseClickListener.Listener();
   private final List<Predicate<Pair<Point, Integer>>> safeZones = Lists.newArrayList();
   private int padding = 10;
   private boolean disposeOnOutsideClick;

   public OutsideWindowMouseClickListener(JWindow var1) {
      this.window = Preconditions.checkNotNull(var1);
      Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 16L);
   }

   @Override
   public final void dispose() {
      Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
   }

   public final void addSafeZone(Predicate<Pair<Point, Integer>> var1) {
      this.safeZones.add(Preconditions.checkNotNull(var1));
   }

   public final void setPadding(int var1) {
      this.padding = ArgCheck.checkNotNegative(var1);
   }

   public final void setDisposeOnOutsideClick(boolean var1) {
      this.disposeOnOutsideClick = var1;
   }

   protected void onOutsideClick() {
   }

   private class Listener implements AWTEventListener {
      private Listener() {
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         if (this.isInterestingEvent(var1)) {
            MouseEvent var2 = (MouseEvent)var1;
            Point var3 = var2.getPoint();
            SwingUtilities.convertPointToScreen(var3, var2.getComponent());
            if (!this.isInside(var3)) {
               try {
                  OutsideWindowMouseClickListener.this.onOutsideClick();
               } finally {
                  if (OutsideWindowMouseClickListener.this.disposeOnOutsideClick) {
                     OutsideWindowMouseClickListener.this.dispose();
                  }
               }
            }
         }
      }

      private boolean isInside(Point var1) {
         return this.isInsideTokenList(var1) || this.isInsideSafeZone(var1);
      }

      private boolean isInsideTokenList(Point var1) {
         Rectangle var2 = OutsideWindowMouseClickListener.this.window.getBounds();
         var2.x = var2.x - OutsideWindowMouseClickListener.this.padding;
         var2.width = var2.width + 2 * OutsideWindowMouseClickListener.this.padding;
         var2.y = var2.y - OutsideWindowMouseClickListener.this.padding;
         var2.height = var2.height + 2 * OutsideWindowMouseClickListener.this.padding;
         return var2.contains(var1);
      }

      private boolean isInsideSafeZone(Point var1) {
         for (Predicate var3 : OutsideWindowMouseClickListener.this.safeZones) {
            if (var3.apply(Pair.of(var1, OutsideWindowMouseClickListener.this.padding))) {
               return true;
            }
         }

         return false;
      }

      private boolean isInterestingEvent(AWTEvent var1) {
         int var2 = var1.getID();
         return var2 == 501 || var2 == 500;
      }
   }
}
