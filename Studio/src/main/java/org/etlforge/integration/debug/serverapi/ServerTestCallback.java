package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;

public interface ServerTestCallback extends ServerCallback {
   void paused(DebugSession var1, TestResult var2);

   void resumed(DebugSession var1);

   void aborted(DebugSession var1);

   void completed(DebugSession var1, TestResult var2);
}
