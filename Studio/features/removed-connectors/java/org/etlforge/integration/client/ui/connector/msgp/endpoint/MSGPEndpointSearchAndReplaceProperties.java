package org.etlforge.integration.client.ui.connector.msgp.endpoint;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;

public final class MSGPEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public MSGPEndpointSearchAndReplaceProperties() {
      super(new ReplaceableKeyValueStringProperty("Database Name", "databasename"), new ReplaceableKeyValueStringProperty("Service URL", "service_url"));
   }
}
