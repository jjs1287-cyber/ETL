package org.etlforge.integration.client.ui.connector.ebs.endpoint;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;

public final class EBSEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public EBSEndpointSearchAndReplaceProperties() {
      super(new ReplaceableKeyValueStringProperty("Connect URL", "connecturl"));
   }
}
