package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.integration.client.datalocation.connection.serverapi.DatabaseConnectionCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.database.BeginEndQuote;

public final class DatabaseConnectionTester extends LocationConnectionTester<DatabaseLocation> {
   public DatabaseConnectionTester(DatabaseLocation var1) {
      super(var1);
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source var1, TestSourceConnectionApi var2) {
      DatabaseConnectionTester.InternalCallback var3 = new DatabaseConnectionTester.InternalCallback(var1);
      var2.testDatabase(var1, this.location, var3);
      return var3;
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target var1, TestTargetConnectionApi var2) {
      DatabaseConnectionTester.InternalCallback var3 = new DatabaseConnectionTester.InternalCallback(var1);
      var2.testDatabase(var1, this.location, var3);
      return var3;
   }

   private static class InternalCallback extends ResultProducingServerCallback<TestConnectionResult> implements DatabaseConnectionCallback {
      private final IntegrationEntity entity;

      public InternalCallback(IntegrationEntity var1) {
         this.entity = var1;
      }

      @Override
      public void connected(BeginEndQuote var1) {
         TestConnectionResult var2 = TestConnectionResult.success(this.entity);
         this.set(var2);
      }
   }
}
