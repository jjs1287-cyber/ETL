package org.etlforge.ui.wait;

import java.io.Serializable;

public final class NullWaitService implements WaitService, Serializable {
   @Override
   public WaitLock startWait() {
      return new NullWaitService.NullLock();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.startWait();
   }

   private static final class NullLock implements WaitLock, Serializable {
      private NullLock() {
      }

      @Override
      public void release() {
      }
   }
}
