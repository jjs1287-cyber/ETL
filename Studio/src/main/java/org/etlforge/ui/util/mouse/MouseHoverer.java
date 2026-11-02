package org.etlforge.ui.util.mouse;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;

public abstract class MouseHoverer implements Disposable {
   private final Component target;
   private MouseHoverer.Listener listener;
   private boolean disposed;
   private final int delay;
   private DelayedJob timer;
   private boolean resetOnMouseClick;

   public MouseHoverer(Component var1, int var2) {
      Preconditions.checkNotNull(var1, "target");
      this.target = var1;
      this.delay = var2;
      this.resetOnMouseClick = true;
   }

   public void install() throws IllegalStateException {
      Preconditions.checkState(!this.disposed, "This instance has been disposed.");
      if (this.listener == null) {
         this.listener = new MouseHoverer.Listener();
         this.target.addMouseListener(this.listener);
         this.target.addMouseMotionListener(this.listener);
      }
   }

   @Override
   public void dispose() {
      if (this.listener != null) {
         this.target.removeMouseListener(this.listener);
         this.target.removeMouseMotionListener(this.listener);
         this.listener = null;
      }

      this.disposed = true;
   }

   public void setResetOnMouseClick(boolean var1) {
      this.resetOnMouseClick = var1;
   }

   protected abstract void hovering(MouseEvent var1);

   protected final boolean isDisposed() {
      return this.disposed;
   }

   protected void mouseMoved(MouseEvent var1) {
      this.startTimer(var1);
   }

   protected abstract boolean didMouseMove(MouseEvent var1, MouseEvent var2);

   protected final void stopTimer() {
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }
   }

   protected final void startTimer(MouseEvent var1) {
      this.stopTimer();
      this.timer = DelayedJob.createAndSchedule(this.delay, new MouseHoverer.TimerTask(var1));
   }

   protected Component getTarget() {
      return this.target;
   }

   private final class Listener extends MouseInputAdapter {
      private MouseEvent lastEvent;

      private Listener() {
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         if (this.lastEvent != null) {
            this.testEvent(var1);
         }

         this.lastEvent = var1;
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (MouseHoverer.this.resetOnMouseClick) {
            MouseHoverer.this.mouseMoved(var1);
         }
      }

      private void testEvent(MouseEvent var1) {
         if (MouseHoverer.this.didMouseMove(this.lastEvent, var1)) {
            MouseHoverer.this.mouseMoved(var1);
         }
      }
   }

   private final class TimerTask implements Runnable {
      private final MouseEvent event;

      public TimerTask(MouseEvent var2) {
         this.event = var2;
      }

      @Override
      public void run() {
         assert MouseHoverer.this.timer != null;
         MouseHoverer.this.timer = null;
         MouseHoverer.this.hovering(this.event);
      }
   }
}
