package org.etlforge.integration.client.ui.connector;

import javax.annotation.Nullable;
import org.etlforge.integration.client.connector.serverapi.TestConnectionCallback;
import org.etlforge.integration.client.connector.serverapi.TestConnectorConnectionApi;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResultException;

final class ConnectorConnectionTester implements ConnectionTester {
   private final TestConnectorConnectionApi serverApi;
   private final ConnectorEndpoint endpoint;

   public ConnectorConnectionTester(TestConnectorConnectionApi var1, ConnectorEndpoint var2) {
      this.serverApi = var1;
      this.endpoint = var2;
   }

   @Nullable
   @Override
   public TestConnectionResult testConnection() {
      try {
         ConnectorConnectionTester.Callback var1 = new ConnectorConnectionTester.Callback();
         this.serverApi.testConnection(this.endpoint, var1);
         return var1.awaitResult().orNull();
      } catch (Exception var3) {
         TestResultException var2 = new TestResultException(var3);
         return TestConnectionResult.error(this.endpoint, var2);
      }
   }

   private class Callback extends ResultProducingServerCallback<TestConnectionResult> implements TestConnectionCallback {
      private Callback() {
      }

      @Override
      public void success(@Nullable String var1) {
         TestConnectionResult var2 = TestConnectionResult.success(ConnectorConnectionTester.this.endpoint);
         this.set(var2);
      }
   }
}
