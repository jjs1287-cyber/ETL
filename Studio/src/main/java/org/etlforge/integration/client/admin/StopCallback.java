package org.etlforge.integration.client.admin;

import org.etlforge.integration.client.server.ServerCallback;

public interface StopCallback extends ServerCallback {
   void stopComplete();
}
