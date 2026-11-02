package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.debug.client.DebugSession;

public interface ServerTestCall extends ServerCall {
   void resumeTest(DebugSession var1, ServerTestCallback var2);

   void stopTest(DebugSession var1);
}
