package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.integration.client.datalocation.connection.serverapi.LdapConnectionCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.test.TestConnectionResult;

public final class LdapConnectionTester extends LocationConnectionTester<LdapLocation> {
   public LdapConnectionTester(LdapLocation var1) {
      super(var1);
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source var1, TestSourceConnectionApi var2) {
      LdapConnectionTester.InternalCallback var3 = new LdapConnectionTester.InternalCallback(var1);
      var2.testLdap(var1, this.location, var3);
      return var3;
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target var1, TestTargetConnectionApi var2) {
      LdapConnectionTester.InternalCallback var3 = new LdapConnectionTester.InternalCallback(var1);
      var2.testLdap(var1, this.location, var3);
      return var3;
   }

   private static class InternalCallback extends ResultProducingServerCallback<TestConnectionResult> implements LdapConnectionCallback {
      private final IntegrationEntity entity;

      public InternalCallback(IntegrationEntity var1) {
         this.entity = var1;
      }

      @Override
      public void connected() {
         TestConnectionResult var1 = TestConnectionResult.success(this.entity);
         this.set(var1);
      }
   }
}
