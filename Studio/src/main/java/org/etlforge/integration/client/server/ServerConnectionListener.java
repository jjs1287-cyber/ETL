package org.etlforge.integration.client.server;

import java.util.EventListener;

public interface ServerConnectionListener extends EventListener {
   void serverConnectionChanged(ServerConnectionEvent var1);
}
