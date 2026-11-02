package org.etlforge.integration.client.ui.connector.msax.endpoint;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;

public final class MSAXEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public MSAXEndpointSearchAndReplaceProperties() {
      super(new ReplaceableKeyValueStringProperty("Service URL", "service_url"), new ReplaceableKeyValueStringProperty("WSDL Name", "domain"));
   }
}
