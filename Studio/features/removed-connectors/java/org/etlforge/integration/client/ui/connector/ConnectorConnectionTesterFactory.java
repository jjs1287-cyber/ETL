package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.connector.serverapi.TestConnectorConnectionApi;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;

public final class ConnectorConnectionTesterFactory implements ConnectionTesterFactory {
   private final TestConnectorConnectionApi serverApi;

   public ConnectorConnectionTesterFactory(TestConnectorConnectionApi var1) {
      this.serverApi = Preconditions.checkNotNull(var1, "serverApi");
   }

   @Override
   public EntityFilter getApplicableItemsFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof ConnectorEndpoint;
         }
      };
   }

   @Override
   public ConnectionTester getConnectionTester(IntegrationEntity var1) {
      return new ConnectorConnectionTester(this.serverApi, (ConnectorEndpoint)var1);
   }
}
