package org.etlforge.ui.widget.overlay;

import java.awt.Point;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public abstract class AnimatedOverlayPositioner implements OverlayPositioner {
   private final OverlayPositioner positionerWhenDone;
   private boolean started;
   private Point startPoint;
   private Point currentPoint;
   private boolean done;

   protected AnimatedOverlayPositioner(OverlayPositioner var1) {
      this.positionerWhenDone = var1;
   }

   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      if (this.done) {
         return this.positionerWhenDone.getPosition(var1, var2);
      }

      if (!this.started) {
         this.started = true;
         this.startPoint = this.getStartPoint(var1, var2);
         this.currentPoint = this.startPoint;
         this.startAnimation(var1, var2);
      }

      return new Point(this.currentPoint);
   }

   protected abstract Point getStartPoint(JComponent var1, JComponent var2);

   private void startAnimation(JComponent var1, JComponent var2) {
      var2.putClientProperty("konga.overlay.animating", Boolean.TRUE);
      Animator var3 = new Animator(250, new AnimatedOverlayPositioner.Worker(var1, var2));
      var3.setAcceleration(0.3F);
      var3.setDeceleration(0.2F);
      var3.start();
   }

   private class Worker extends TimingTargetAdapter {
      private final JComponent container;
      private final JComponent overlay;

      public Worker(JComponent var2, JComponent var3) {
         this.container = var2;
         this.overlay = var3;
      }

      @Override
      public void end() {
         AnimatedOverlayPositioner.this.done = true;
         this.overlay.putClientProperty("konga.overlay.animating", Boolean.FALSE);
      }

      @Override
      public void timingEvent(float var1) {
         Point var2 = AnimatedOverlayPositioner.this.positionerWhenDone.getPosition(this.container, this.overlay);
         int var3 = AnimatedOverlayPositioner.this.startPoint.x + (int)(var1 * (var2.x - AnimatedOverlayPositioner.this.startPoint.x));
         int var4 = AnimatedOverlayPositioner.this.startPoint.y + (int)(var1 * (var2.y - AnimatedOverlayPositioner.this.startPoint.y));
         AnimatedOverlayPositioner.this.currentPoint = new Point(var3, var4);
         this.overlay.setLocation(AnimatedOverlayPositioner.this.currentPoint);
         this.container.revalidate();
         this.container.repaint();
      }
   }
}
