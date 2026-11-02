package org.etlforge.integration.debug.client.event;

import java.util.EventListener;

public interface DebugServiceListener extends EventListener {
   void newDebugSessionStarted(DebugServiceEvent var1);

   void debugSessionEnded(DebugServiceEvent var1);
}
