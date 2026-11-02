package org.etlforge.integration.client.ui.interchange.entity.source;

import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.ui.interchange.location.LocationConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.location.DataLocation;

public final class SourceConnectionTesterFactory implements ConnectionTesterFactory {
   private final TestSourceConnectionApi serverApi;

   public SourceConnectionTesterFactory(TestSourceConnectionApi var1) {
      this.serverApi = var1;
   }

   @Override
   public EntityFilter getApplicableItemsFilter() {
      return EntityFilters.allOfType(EntityType.Source);
   }

   @Override
   public ConnectionTester getConnectionTester(IntegrationEntity var1) {
      Source var2 = (Source)var1;
      DataLocation var3 = var2.getLocation();
      LocationConnectionTester var4 = LocationConnectionTester.forLocation(var3);
      return var4.asSourceConnectionTester(var2, this.serverApi);
   }
}
