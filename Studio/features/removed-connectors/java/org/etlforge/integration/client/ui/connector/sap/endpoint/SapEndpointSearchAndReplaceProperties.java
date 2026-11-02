package org.etlforge.integration.client.ui.connector.sap.endpoint;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;

public final class SapEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public SapEndpointSearchAndReplaceProperties() {
      super(
         new ReplaceableKeyValueStringProperty("System number", "systemnumber"),
         new ReplaceableKeyValueStringProperty("Client", "client"),
         new ReplaceableKeyValueStringProperty("Lang", "lang")
      );
   }
}
