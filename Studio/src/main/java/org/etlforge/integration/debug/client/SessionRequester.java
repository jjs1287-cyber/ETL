package org.etlforge.integration.debug.client;

public interface SessionRequester {
   void newSessionStarted(DebugSession var1);

   void requestDenied();
}
