package org.etlforge.ui.wait;

import com.google.common.base.Preconditions;
import java.util.Stack;

public class IncrementalWaitService {
   private final WaitService waitService;
   private final Stack<WaitLock> waitLocks = new Stack<>();

   public IncrementalWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   public synchronized void startWait() {
      this.waitLocks.push(this.waitService.startWait());
   }

   public synchronized void stopWait() {
      if (!this.waitLocks.isEmpty()) {
         this.waitLocks.pop().release();
      }
   }
}
