package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import java.net.URL;
import java.util.concurrent.Callable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.connector.serverapi.SingleSchemas;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.etlforgeconnector.SchemaData;

@ParametersAreNonnullByDefault
public final class SchemaGenerator implements Callable<Schemas> {
   private final MSCrmObjectLookupFactory objectLookupFactory;
   private final SchemaRequestData responseSchemaData;

   public SchemaGenerator(MSCrmObjectLookupFactory var1, MSCrmQuery var2) {
      this(var1, new SchemaRequestData(var2.getEndpoint(), var2.getQueryXml()));
   }

   public SchemaGenerator(MSCrmObjectLookupFactory var1, SchemaRequestData var2) {
      this.objectLookupFactory = Preconditions.checkNotNull(var1, "objectLookupFactory");
      this.responseSchemaData = Preconditions.checkNotNull(var2, "requestData");
   }

   public Schemas call() throws Exception {
      SchemaData var1 = this.getRequestSchema();
      SchemaData var2 = this.responseSchemaData.generateSchema(this.objectLookupFactory);
      return new SingleSchemas(var1, var2);
   }

   private SchemaData getRequestSchema() throws Exception {
      URL var1 = Resources.getResource("etlforge.connector.mscrm.fetchxml.xsd");
      String var2 = "fetch";
      return SchemaData.fromOriginalContents(var2, Resources.toString(var1, Charsets.UTF_8));
   }
}
