package org.etlforge.integration.client.ui.connector.mscrm.endpoint;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class MSCrmEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public MSCrmEndpointSearchAndReplaceProperties() {
      super(
         new ReplaceableKeyValueStringProperty("Organization Unique Name", "organizationUniqueName"),
         new ReplaceableKeyValueStringProperty("Domain Name", "domain"),
         new MSCrmEndpointSearchAndReplaceProperties.ConnectivityTypeProperty()
      );
   }

   private static final class ConnectivityTypeProperty
      extends AbstractSearchableProperty<MSCrmEndpoint.ConnectivityType>
      implements ReplaceableProperty<MSCrmEndpoint.ConnectivityType> {
      public ConnectivityTypeProperty() {
         super("Connectivity type", MSCrmEndpoint.ConnectivityType.class);
      }

      public MSCrmEndpoint.ConnectivityType getValue(IntegrationEntity var1) {
         return ((MSCrmEndpoint)var1).getConnectivityType();
      }

      public void setValue(IntegrationEntity var1, MSCrmEndpoint.ConnectivityType var2) {
         ((MSCrmEndpoint)var1).setConnectivityType(var2);
      }
   }
}
