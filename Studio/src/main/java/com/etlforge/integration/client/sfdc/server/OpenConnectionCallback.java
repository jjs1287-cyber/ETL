package com.etlforge.integration.client.sfdc.server;

import org.etlforge.integration.client.server.ServerCallback;

public interface OpenConnectionCallback extends ServerCallback {
   void connectionWasOpened(SessionData var1);
}
