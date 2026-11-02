package org.etlforge.integration.debug.client;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface TestInProgressStrategy {
   TestInProgressStrategy REJECT = new TestInProgressStrategy() {
      @Override
      public void handleNewTestRequest(DebugService var1, String var2, IntegrationEntityId var3, DebugController var4, SessionRequester var5) throws DebuggingException {
         throw new DebuggingException("Another test is already in progress. That test needs to be stopped before a new test can be started.");
      }
   };

   void handleNewTestRequest(DebugService var1, String var2, IntegrationEntityId var3, DebugController var4, SessionRequester var5) throws DebuggingException;
}
