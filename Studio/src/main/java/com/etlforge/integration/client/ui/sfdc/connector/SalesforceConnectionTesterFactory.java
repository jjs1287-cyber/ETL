package com.etlforge.integration.client.ui.sfdc.connector;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResultStringError;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.SalesforceConnectionTester;

public final class SalesforceConnectionTesterFactory implements ConnectionTesterFactory {
   private final SalesforceConnectionTester worker;

   public SalesforceConnectionTesterFactory(SalesforceConnectionTester var1) {
      this.worker = Preconditions.checkNotNull(var1);
   }

   @Override
   public EntityFilter getApplicableItemsFilter() {
      return EntityFilters.allOfType(EntityType.SalesforceConnector);
   }

   @Override
   public ConnectionTester getConnectionTester(IntegrationEntity var1) {
      return new SalesforceConnectionTesterFactory.TesterImpl(this.worker, (SalesforceConnector)var1);
   }

   private static class TesterImpl implements ConnectionTester {
      private final SalesforceConnectionTester worker;
      private final SalesforceConnector endpoint;

      public TesterImpl(SalesforceConnectionTester var1, SalesforceConnector var2) {
         this.worker = var1;
         this.endpoint = var2;
      }

      @Override
      public TestConnectionResult testConnection() {
         ConnectionResult var1 = this.worker.testConnection(this.endpoint.getOrgIdentifier());
         return var1.isSuccess()
            ? TestConnectionResult.success(this.endpoint)
            : TestConnectionResult.error(this.endpoint, new TestResultStringError(var1.getMessage()));
      }
   }
}
