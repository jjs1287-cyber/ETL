package org.etlforge.integration.debug.client;

import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.util.id.KongaGuid;

public interface DebugSession {
   String getTestProviderId();

   KongaGuid getId();

   DebuggingParameters getParams();

   void setParams(DebuggingParameters var1);

   IntegrationEntityId getDebuggeeId();

   DebugController getController();

   void setTargetNode(TargetNode var1);

   TargetNode getTargetNode();

   void setSourceDataGuid(KongaGuid var1);

   KongaGuid getSourceDataGuid();

   TestResult getResult();

   DebuggingState getState();

   void paused(TestResult var1);

   void resumed();

   void aborted();

   void completed(TestResult var1);

   void addListener(DebugSessionListener var1);

   void removeListener(DebugSessionListener var1);

   boolean isIsolatedTest();
}
