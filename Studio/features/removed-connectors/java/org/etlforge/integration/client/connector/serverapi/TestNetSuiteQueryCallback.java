package org.etlforge.integration.client.connector.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface TestNetSuiteQueryCallback extends ServerCallback {
   void testComplete(TestNetSuiteQueryApi.Result var1);
}
