package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;

public abstract class ServerTestCallbackAdapter implements ServerTestCallback {
   @Override
   public void aborted(DebugSession var1) {
   }

   @Override
   public void completed(DebugSession var1, TestResult var2) {
   }

   @Override
   public void paused(DebugSession var1, TestResult var2) {
   }

   @Override
   public void resumed(DebugSession var1) {
   }

   @Override
   public void cancelled() {
   }

   @Override
   public void caught(Throwable var1) {
   }
}
