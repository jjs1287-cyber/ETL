package org.etlforge.ui.wait;

import com.google.common.base.Preconditions;

public class WaitLockHoldingCallback extends ResourceReleasingCallback {
   private final WaitLock waitLock;

   public WaitLockHoldingCallback(WaitLock var1) {
      Preconditions.checkNotNull(var1, "waitLock");
      this.waitLock = var1;
   }

   @Override
   protected final void releaseResource() {
      this.waitLock.release();
   }
}
