package com.etlforge.integration.client.ui.sfdc.connector;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceablePasswordProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;

final class ConnectorSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public ConnectorSearchAndReplaceProperties() {
      super(
         new ReplaceableKeyValueStringProperty("Server host", "server_host"),
         new ReplaceableKeyValueStringProperty("User name", "user_name"),
         new ReplaceablePasswordProperty("Password", "password"),
         new ReplaceablePasswordProperty("Security token", "security_token")
      );
   }

   @Override
   public boolean includeName() {
      return false;
   }
}
