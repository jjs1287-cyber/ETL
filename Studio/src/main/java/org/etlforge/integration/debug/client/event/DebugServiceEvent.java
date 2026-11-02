package org.etlforge.integration.debug.client.event;

import java.util.EventObject;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;

public final class DebugServiceEvent extends EventObject {
   private final DebugSession session;

   public DebugServiceEvent(DebugService var1, DebugSession var2) {
      super(var1);
      this.session = var2;
   }

   public DebugService getSource() {
      return (DebugService)super.getSource();
   }

   public final DebugSession getSession() {
      return this.session;
   }
}
