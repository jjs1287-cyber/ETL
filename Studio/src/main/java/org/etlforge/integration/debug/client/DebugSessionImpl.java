package org.etlforge.integration.debug.client;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.event.DebugSessionEvent;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.util.id.KongaGuid;

final class DebugSessionImpl implements DebugSession {
   private final DebugService debugService;
   private final String testProviderId;
   private final KongaGuid sessionId = KongaGuid.createGuid();
   private final IntegrationEntityId debuggeeId;
   private TargetNode targetNode;
   private KongaGuid sourceDataGuid;
   private final DebugController controller;
   private DebuggingParameters params;
   private TestResult result;
   private DebuggingState state = DebuggingState.IDLE;
   private final Object lock = new Object();
   private final List<DebugSessionListener> listeners = new CopyOnWriteArrayList<>();

   DebugSessionImpl(DebugService var1, String var2, IntegrationEntityId var3, DebugController var4) {
      this.debugService = Preconditions.checkNotNull(var1, "debugService");
      this.testProviderId = Preconditions.checkNotNull(var2, "testProviderId");
      this.debuggeeId = Preconditions.checkNotNull(var3, "debuggeeId");
      this.controller = Preconditions.checkNotNull(var4, "controller");
   }

   @Override
   public KongaGuid getId() {
      return this.sessionId;
   }

   @Override
   public String getTestProviderId() {
      return this.testProviderId;
   }

   @Override
   public DebuggingParameters getParams() {
      synchronized (this.lock) {
         return this.params;
      }
   }

   @Override
   public void setParams(DebuggingParameters var1) {
      Preconditions.checkNotNull(var1, "params");
      synchronized (this.lock) {
         this.params = var1;
      }

      this.fireParamsChanged(new DebugSessionEvent(this));
   }

   @Override
   public IntegrationEntityId getDebuggeeId() {
      return this.debuggeeId;
   }

   @Override
   public DebugController getController() {
      return this.controller;
   }

   @Override
   public void setTargetNode(TargetNode var1) {
      synchronized (this.lock) {
         this.targetNode = var1;
      }
   }

   @Override
   public TargetNode getTargetNode() {
      return this.targetNode;
   }

   @Override
   public void setSourceDataGuid(KongaGuid var1) {
      synchronized (this.lock) {
         this.sourceDataGuid = var1;
      }
   }

   @Override
   public KongaGuid getSourceDataGuid() {
      synchronized (this.lock) {
         return this.sourceDataGuid;
      }
   }

   @Override
   public final TestResult getResult() {
      synchronized (this.lock) {
         switch (this.state) {
            case PAUSED:
            case DONE:
               return this.result;
            default:
               return null;
         }
      }
   }

   public final void setResult(TestResult var1) {
      synchronized (this.lock) {
         this.result = var1;
      }
   }

   @Override
   public final DebuggingState getState() {
      synchronized (this.lock) {
         return this.state;
      }
   }

   void started() {
      synchronized (this.lock) {
         if (this.state != DebuggingState.IDLE) {
            return;
         }

         this.state = DebuggingState.RUNNING;
      }

      this.fireStateChanged(new DebugSessionEvent(this, DebuggingState.RUNNING));
   }

   @Override
   public void paused(TestResult var1) {
      synchronized (this.lock) {
         if (this.state != DebuggingState.RUNNING) {
            return;
         }

         this.result = var1;
         this.state = DebuggingState.PAUSED;
      }

      this.fireStateChanged(new DebugSessionEvent(this, DebuggingState.PAUSED));
   }

   @Override
   public void resumed() {
      synchronized (this.lock) {
         if (this.state != DebuggingState.PAUSED) {
            return;
         }

         this.state = DebuggingState.RUNNING;
      }

      this.fireStateChanged(new DebugSessionEvent(this, DebuggingState.RUNNING));
   }

   @Override
   public void aborted() {
      this.stopImpl(null);
   }

   @Override
   public void completed(TestResult var1) {
      this.stopImpl(var1);
   }

   private void stopImpl(TestResult var1) {
      synchronized (this.lock) {
         if (this.state != DebuggingState.RUNNING && this.state != DebuggingState.PAUSED) {
            return;
         }

         this.result = var1;
         this.state = DebuggingState.DONE;
      }

      this.fireStateChanged(new DebugSessionEvent(this, DebuggingState.DONE));
      this.listeners.clear();
      this.debugService.stopSession(this);
   }

   @Override
   public boolean isIsolatedTest() {
      return false;
   }

   @Override
   public void addListener(DebugSessionListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(DebugSessionListener var1) {
      this.listeners.remove(var1);
   }

   private void fireStateChanged(DebugSessionEvent var1) {
      for (DebugSessionListener var3 : this.listeners) {
         var3.sessionStateChanged(var1);
      }
   }

   private void fireParamsChanged(DebugSessionEvent var1) {
      for (DebugSessionListener var3 : this.listeners) {
         var3.sessionParametersChanged(var1);
      }
   }

   @Override
   public String toString() {
      return this.sessionId + " - " + this.state;
   }
}
