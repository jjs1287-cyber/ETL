package org.etlforge.integration.client.ui.connector.netsuite.endpoint;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;

public final class NetSuiteEndpointSearchAndReplaceProperties extends EndpointSearchAndReplaceMetaData {
   public NetSuiteEndpointSearchAndReplaceProperties() {
      super(
         new ReplaceableKeyValueStringProperty("Email", "email"),
         new ReplaceableKeyValueStringProperty("WsdlDownLoadUrl", "wsdldownloadurl"),
         new ReplaceableKeyValueBooleanProperty("Sandbox", "sandbox", false)
      );
   }
}
