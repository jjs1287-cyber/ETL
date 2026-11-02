package org.etlforge.ui.widget.scroll;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class AutoScroller {
   private final JScrollPane scroll;
   private int dx;
   private int dy;
   private Timer timer;
   private int delay;

   public AutoScroller(JScrollPane var1) {
      this.scroll = var1;
      this.delay = 100;
   }

   public AutoScroller(JScrollPane var1, int var2, int var3) {
      this(var1);
      this.setDeltaX(var2);
      this.setDeltaY(var3);
   }

   public void setDeltaX(int var1) {
      this.dx = var1;
   }

   public int getDeltaX() {
      return this.dx;
   }

   public void setDeltaY(int var1) {
      this.dy = var1;
   }

   public int getDeltaY() {
      return this.dy;
   }

   public void setDelay(int var1) {
      this.delay = var1;
      if (this.timer != null) {
         this.timer.setDelay(var1);
      }
   }

   public void start() {
      if (this.timer == null) {
         this.timer = new Timer(this.delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               AutoScroller.this.updateScroll();
            }
         });
         this.timer.setRepeats(true);
         this.timer.start();
      }
   }

   public void stop() {
      if (this.timer != null) {
         this.timer.stop();
         this.timer = null;
      }
   }

   private void updateScroll() {
      updateDirection(this.scroll.getHorizontalScrollBar(), this.dx);
      updateDirection(this.scroll.getVerticalScrollBar(), this.dy);
   }

   private static void updateDirection(JScrollBar var0, int var1) {
      if (var1 != 0) {
         int var2 = Math.max(Math.min(var0.getValue() + var1, var0.getMaximum()), var0.getMinimum());
         var0.setValue(var2);
      }
   }
}
