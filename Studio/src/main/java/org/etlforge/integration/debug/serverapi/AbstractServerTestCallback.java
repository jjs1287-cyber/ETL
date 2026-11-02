package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;

public abstract class AbstractServerTestCallback implements ServerTestCallback {
   private final DebugService debugService;

   protected AbstractServerTestCallback(DebugService var1) {
      this.debugService = var1;
   }

   @Override
   public final void paused(DebugSession var1, TestResult var2) {
      if (this.isSameSession(var1)) {
         var1.paused(var2);
      }
   }

   @Override
   public final void resumed(DebugSession var1) {
      if (this.isSameSession(var1)) {
         var1.resumed();
      }
   }

   @Override
   public final void aborted(DebugSession var1) {
      if (this.isSameSession(var1)) {
         var1.aborted();
      }
   }

   @Override
   public final void completed(DebugSession var1, TestResult var2) {
      if (this.isSameSession(var1)) {
         var1.completed(var2);
      }

      this.doCompletionWork(var1, var2);
   }

   protected void doCompletionWork(DebugSession var1, TestResult var2) {
   }

   protected final void abort(DebugSession var1) {
      if (this.isSameSession(var1)) {
         var1.aborted();
      }
   }

   private boolean isSameSession(DebugSession var1) {
      return var1.getId().equals(this.debugService.getCurrentSessionId());
   }
}
