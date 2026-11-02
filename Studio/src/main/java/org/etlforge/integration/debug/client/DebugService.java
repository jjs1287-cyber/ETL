package org.etlforge.integration.debug.client;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.integration.debug.client.event.DebugServiceListener;
import org.etlforge.util.id.KongaGuid;

public final class DebugService {
   private DebugSession currentSession;
   private TestInProgressStrategy testInProgressStrategy;
   private final List<DebugServiceListener> listeners;
   private final boolean breakpointsSupported;

   public DebugService() {
      this(true);
   }

   public DebugService(boolean var1) {
      this.breakpointsSupported = var1;
      this.testInProgressStrategy = TestInProgressStrategy.REJECT;
      this.listeners = new CopyOnWriteArrayList<>();
   }

   public boolean areBreakpointsSupported() {
      return this.breakpointsSupported;
   }

   public synchronized void setTestInProgressStrategy(TestInProgressStrategy var1) {
      this.testInProgressStrategy = Preconditions.checkNotNull(var1);
   }

   public synchronized boolean isDebugging() {
      return this.currentSession != null;
   }

   public synchronized DebugSession getCurrentSession() {
      return this.currentSession;
   }

   public synchronized KongaGuid getCurrentSessionId() {
      return this.currentSession != null ? this.currentSession.getId() : null;
   }

   public synchronized DebuggingState getState() {
      return this.currentSession != null ? this.currentSession.getState() : DebuggingState.IDLE;
   }

   public void startNewDebuggingSession(String var1, IntegrationEntityId var2, DebugController var3, SessionRequester var4) throws DebuggingException {
      DebugSessionImpl var5 = null;
      synchronized (this) {
         if (this.isDebugging()) {
            this.testInProgressStrategy.handleNewTestRequest(this, var1, var2, var3, var4);
            return;
         }

         var5 = new DebugSessionImpl(this, var1, var2, var3);
         this.currentSession = var5;
         var5.started();
      }

      this.fireNewSessionStarted(var5);
      var4.newSessionStarted(var5);
   }

   void stopSession(DebugSession var1) {
      assert var1 != null;
      boolean var2 = false;
      synchronized (this) {
         if (this.currentSession == var1) {
            var2 = true;
            this.currentSession = null;
         }
      }

      if (var2) {
         this.fireSessionEnded(var1);
      }
   }

   public void addListener(DebugServiceListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(DebugServiceListener var1) {
      this.listeners.remove(var1);
   }

   private void fireSessionEnded(DebugSession var1) {
      DebugServiceEvent var2 = new DebugServiceEvent(this, var1);

      for (DebugServiceListener var4 : this.listeners) {
         var4.debugSessionEnded(var2);
      }
   }

   private void fireNewSessionStarted(DebugSession var1) {
      DebugServiceEvent var2 = new DebugServiceEvent(this, var1);

      for (DebugServiceListener var4 : this.listeners) {
         var4.newDebugSessionStarted(var2);
      }
   }
}
