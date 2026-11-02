package org.etlforge.integration.debug.client;

public interface DebugController {
   void resume(DebuggingParameters var1);

   void step(DebuggingParameters var1);

   void pause();

   void stop();

   void stopAndWait() throws InterruptedException;
}
