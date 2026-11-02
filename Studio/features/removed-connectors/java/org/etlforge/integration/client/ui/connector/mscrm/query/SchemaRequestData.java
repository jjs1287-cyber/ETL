package org.etlforge.integration.client.ui.connector.mscrm.query;

import java.io.IOException;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetResponseSchemaCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.integration.mscrm.query.ResponseSchemaBuilder;

public final class SchemaRequestData {
   private final MSCrmEndpoint endpoint;
   private final String fetchXml;

   public SchemaRequestData(MSCrmEndpoint var1, String var2) {
      this.endpoint = var1;
      this.fetchXml = var2;
   }

   public SchemaData generateSchema(MSCrmObjectLookupFactory var1) throws MSCrmMetaDataException, IOException {
      MSCrmObjectLookup var2 = var1.newLookup(this.endpoint);
      return ResponseSchemaBuilder.buildSchema(this.fetchXml, var2);
   }

   public void sendRequest(MSCrmDesignInfoProvider var1, GetResponseSchemaCallback var2) {
      var1.getQueryResponseSchema(this.endpoint, this.fetchXml, var2);
   }
}
