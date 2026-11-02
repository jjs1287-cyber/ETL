package org.etlforge.integration.client.ui.interchange.entity.target;

import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.ui.interchange.location.LocationConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTesterFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.location.DataLocation;

public final class TargetConnectionTesterFactory implements ConnectionTesterFactory {
   private final TestTargetConnectionApi serverApi;

   public TargetConnectionTesterFactory(TestTargetConnectionApi var1) {
      this.serverApi = var1;
   }

   @Override
   public EntityFilter getApplicableItemsFilter() {
      return EntityFilters.allOfType(EntityType.Target);
   }

   @Override
   public ConnectionTester getConnectionTester(IntegrationEntity var1) {
      Target var2 = (Target)var1;
      DataLocation var3 = var2.getLocation();
      LocationConnectionTester var4 = LocationConnectionTester.forLocation(var3);
      return var4.asTargetConnectionTester(var2, this.serverApi);
   }
}
