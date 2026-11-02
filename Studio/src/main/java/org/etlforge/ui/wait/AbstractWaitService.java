package org.etlforge.ui.wait;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.UiUtils;

public abstract class AbstractWaitService implements WaitService {
   private static final WaitLock DUMMY_LOCK = new WaitLock() {
      @Override
      public void release() {
      }
   };
   private final LockFactory lockFactory = new LockFactory(new Receiver<WaitLock>() {
      public void handle(WaitLock var1) {
         AbstractWaitService.this.lockReleased(var1);
      }
   });
   private final Set<WaitLock> outstandingLocks = new HashSet<>();
   private volatile boolean verbose;
   private boolean inWaitState;

   protected AbstractWaitService() {
   }

   @Override
   public final WaitLock startWait() {
      return startWaitImpl(new AbstractWaitService.WaitLockSupplier());
   }

   @Override
   public final WaitLock startWait(int var1) {
      return var1 <= 0 ? this.startWait() : startWaitImpl(new AbstractWaitService.DelayedWaitLockSupplier(var1));
   }

   public final boolean isInWaitState() {
      return this.inWaitState;
   }

   public void cancelAllWaits() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            AbstractWaitService.this.outstandingLocks.clear();
            AbstractWaitService.this.stopWaitingIfInWaitingState();
         }
      });
   }

   public final void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public final boolean isVerbose() {
      return this.verbose;
   }

   private static WaitLock startWaitImpl(Supplier<WaitLock> var0) {
      UiThreadSupplier var1 = new UiThreadSupplier(var0);
      var1.setErrorHandler(ExceptionHandler.STANDARD_ERR);
      WaitLock var2 = (WaitLock)var1.get();
      return var2 != null ? var2 : DUMMY_LOCK;
   }

   private void goToWaitingStateIfNotAlreadyWaiting() {
      assert EventQueue.isDispatchThread() : "goToWaitingStateIfNotAlreadyWaiting should be called from the EDT";
      if (!this.inWaitState) {
         this.inWaitState = true;
         this.goToWaitingState();
      }
   }

   protected abstract void goToWaitingState();

   private void stopWaitingIfInWaitingState() {
      assert EventQueue.isDispatchThread() : "stopWaitingIfInWaitingState should be called from the EDT";
      if (this.inWaitState) {
         this.stopWaiting();
         this.inWaitState = false;
      }
   }

   protected abstract void stopWaiting();

   private void lockReleased(final WaitLock var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            boolean var1x = AbstractWaitService.this.outstandingLocks.remove(var1);
            AbstractWaitService.this.report("Was asked to remove lock: " + var1 + "\nDid I keep that lock? " + (var1x ? "Yes" : "No"));
            if (AbstractWaitService.this.outstandingLocks.isEmpty()) {
               AbstractWaitService.this.stopWaitingIfInWaitingState();
            }
         }
      });
   }

   protected final void report(String var1) {
      if (this.verbose) {
         System.out.println(Thread.currentThread() + " @ " + System.currentTimeMillis());
         System.out.println(var1);
      }
   }

   private class DelayedWaitLockSupplier implements Supplier<WaitLock> {
      private final int delay;

      public DelayedWaitLockSupplier(int var2) {
         this.delay = var2;
      }

      public WaitLock get() {
         assert EventQueue.isDispatchThread() : "WaitLocks must be created on the EDT";
         final Object[] var1 = new Object[1];
         WaitLock var2 = AbstractWaitService.this.lockFactory.newDelayedWaitLock(this.delay, new Runnable() {
            @Override
            public void run() {
               AbstractWaitService.this.report("Delayed lock " + var1[0] + " was activated");
               AbstractWaitService.this.goToWaitingStateIfNotAlreadyWaiting();
            }
         });
         var1[0] = var2;
         AbstractWaitService.this.outstandingLocks.add(var2);
         AbstractWaitService.this.report("Created delayed lock: " + var2 + "\nNumber of locks: " + AbstractWaitService.this.outstandingLocks.size());
         return var2;
      }
   }

   private class WaitLockSupplier implements Supplier<WaitLock> {
      private WaitLockSupplier() {
      }

      public WaitLock get() {
         assert EventQueue.isDispatchThread() : "WaitLocks must be created on the EDT";
         WaitLock var1 = AbstractWaitService.this.lockFactory.newLock();
         AbstractWaitService.this.outstandingLocks.add(var1);
         AbstractWaitService.this.report("Created lock: " + var1 + "\nNumber of locks: " + AbstractWaitService.this.outstandingLocks.size());
         AbstractWaitService.this.goToWaitingStateIfNotAlreadyWaiting();
         return var1;
      }
   }
}
