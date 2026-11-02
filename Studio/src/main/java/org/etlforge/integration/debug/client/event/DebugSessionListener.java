package org.etlforge.integration.debug.client.event;

import java.util.EventListener;

public interface DebugSessionListener extends EventListener {
   void sessionStateChanged(DebugSessionEvent var1);

   void sessionParametersChanged(DebugSessionEvent var1);
}
