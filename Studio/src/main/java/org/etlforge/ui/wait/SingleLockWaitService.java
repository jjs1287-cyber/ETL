package org.etlforge.ui.wait;

import com.google.common.base.Preconditions;

public final class SingleLockWaitService implements WaitService {
   private WaitService wrappee;
   private WaitLock waitLock;
   private final Object lock = new Object();

   public SingleLockWaitService() {
      this(WaitService.NONE);
   }

   public SingleLockWaitService(WaitService var1) {
      this.setWrappee(var1);
   }

   public void setWrappee(WaitService var1) {
      synchronized (this.lock) {
         this.wrappee = Preconditions.checkNotNull(var1);
      }
   }

   @Override
   public WaitLock startWait() {
      return this.startWait(0);
   }

   @Override
   public WaitLock startWait(int var1) {
      synchronized (this.lock) {
         this.releaseCurrentLock();
         this.waitLock = this.wrappee.startWait(var1);
         return this.waitLock;
      }
   }

   private void releaseCurrentLock() {
      if (this.waitLock != null) {
         this.waitLock.release();
      }
   }

   public void stopWait() {
      synchronized (this.lock) {
         this.releaseCurrentLock();
      }
   }

   public void setWaiting(boolean var1) {
      synchronized (this.lock) {
         if (var1) {
            this.startWait();
         } else {
            this.stopWait();
         }
      }
   }
}
