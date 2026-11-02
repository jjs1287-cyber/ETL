package org.etlforge.integration.client.ui.interchange.testconnection;

import org.etlforge.integration.data.test.TestConnectionResult;

public interface TestAllConnectionsListener {
   void testStarted();

   void testStopped();

   void testDone();

   void connectionWasTested(TestConnectionResult var1);

   void progressUpdate(String var1);
}
