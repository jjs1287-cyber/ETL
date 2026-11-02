package org.etlforge.ui.widget.overlay;

import java.awt.Container;
import java.awt.Point;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.util.UiUtils;

public class AnimatedOverlayRemover {
   private final OverlayContainer container;
   private final JComponent overlay;
   private final Point endPoint;

   public AnimatedOverlayRemover(OverlayContainer var1, JComponent var2, Point var3) {
      this.container = var1;
      this.overlay = var2;
      this.endPoint = var3;
   }

   public void start() {
      Animator var1 = new Animator(150, new AnimatedOverlayRemover.Worker());
      var1.setAcceleration(0.2F);
      var1.setDeceleration(0.3F);
      var1.start();
   }

   protected void done() {
   }

   private class Worker extends TimingTargetAdapter {
      private Point startPoint;

      private Worker() {
      }

      @Override
      public void begin() {
         this.startPoint = AnimatedOverlayRemover.this.overlay.getLocation();
      }

      @Override
      public void end() {
         AnimatedOverlayRemover.this.container.removeOverlay(AnimatedOverlayRemover.this.overlay);
      }

      @Override
      public void timingEvent(float var1) {
         Container var2 = AnimatedOverlayRemover.this.overlay.getParent();
         if (var2 != null) {
            int var3 = this.startPoint.x + (int)(var1 * (AnimatedOverlayRemover.this.endPoint.x - this.startPoint.x));
            int var4 = this.startPoint.y + (int)(var1 * (AnimatedOverlayRemover.this.endPoint.y - this.startPoint.y));
            AnimatedOverlayRemover.this.overlay.setLocation(new Point(var3, var4));
            UiUtils.revalidate(AnimatedOverlayRemover.this.overlay);
         }
      }
   }
}
