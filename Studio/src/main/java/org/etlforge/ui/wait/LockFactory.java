package org.etlforge.ui.wait;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.system.callback.Receiver;

final class LockFactory {
   private final Receiver<WaitLock> releaseHandler;

   public LockFactory(Receiver<WaitLock> var1) {
      this.releaseHandler = var1;
   }

   public WaitLock newLock() {
      return new LockFactory.WaitLockImpl(this.releaseHandler);
   }

   public WaitLock newDelayedWaitLock(int var1, Runnable var2) {
      assert var1 > 0 : "delay <= 0 -- you should start waiting immeditely";
      return new LockFactory.DelayedWaitLock(var1, var2, this.releaseHandler);
   }

   private static class DelayedWaitLock extends LockFactory.WaitLockImpl implements ActionListener {
      private final Runnable waitStarter;
      private Timer timer;

      public DelayedWaitLock(int var1, Runnable var2, Receiver<WaitLock> var3) {
         super(var3);
         this.waitStarter = var2;
         this.timer = new Timer(var1, this);
         this.timer.setRepeats(false);
         this.timer.start();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         synchronized (this.monitor) {
            if (!this.isReleased()) {
               this.startWait();
            }
         }
      }

      private void startWait() {
         this.waitStarter.run();
         this.timer = null;
      }

      @Override
      public void release() {
         synchronized (this.monitor) {
            super.release();
            this.stopTimer();
         }
      }

      private void stopTimer() {
         if (this.timer != null) {
            this.timer.stop();
            this.timer = null;
         }
      }
   }

   private static class WaitLockImpl implements WaitLock {
      private boolean released;
      private final Receiver<WaitLock> releaser;
      protected final Object monitor;

      public WaitLockImpl(Receiver<WaitLock> var1) {
         this.releaser = var1;
         this.monitor = new Object();
      }

      @Override
      public void release() {
         synchronized (this.monitor) {
            if (!this.released) {
               this.released = true;
               this.releaser.handle(this);
            }
         }
      }

      protected final boolean isReleased() {
         synchronized (this.monitor) {
            return this.released;
         }
      }

      @Override
      protected void finalize() throws Throwable {
         this.release();
      }
   }
}
