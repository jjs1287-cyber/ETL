package org.etlforge.integration.debug.client.event;

import java.util.EventObject;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingState;

public final class DebugSessionEvent extends EventObject {
   private final DebuggingState state;

   public DebugSessionEvent(DebugSession var1) {
      this(var1, var1.getState());
   }

   public DebugSessionEvent(DebugSession var1, DebuggingState var2) {
      super(var1);
      this.state = var2;
   }

   public DebugSession getSource() {
      return (DebugSession)super.getSource();
   }

   public final DebuggingState getState() {
      return this.state;
   }
}
