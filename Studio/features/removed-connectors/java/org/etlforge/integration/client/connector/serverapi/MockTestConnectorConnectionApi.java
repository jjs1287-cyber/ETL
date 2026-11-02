package org.etlforge.integration.client.connector.serverapi;

import java.util.concurrent.TimeUnit;
import org.etlforge.integration.data.connector.ConnectorEndpoint;

public final class MockTestConnectorConnectionApi implements TestConnectorConnectionApi {
   @Override
   public void testConnection(ConnectorEndpoint var1, TestConnectionCallback var2) {
      try {
         TimeUnit.SECONDS.sleep(2L);
         var2.success(null);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         var2.caught(var4);
      }
   }
}
