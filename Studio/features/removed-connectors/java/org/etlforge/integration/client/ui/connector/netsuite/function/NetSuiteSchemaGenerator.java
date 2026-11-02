package org.etlforge.integration.client.ui.connector.netsuite.function;

import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public class NetSuiteSchemaGenerator {
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final ConnectorCallInfoProvider infoProvider;

   public NetSuiteSchemaGenerator(NetSuiteDefinitionDesignLookup var1, NetSuiteCustomFieldsDesignLookup var2, ConnectorCallInfoProvider var3) {
      this.infoProvider = var3;
      this.defLookup = var1;
      this.cfLookup = var2;
   }

   public Callable<Schemas> getSchemaGenerator(NetSuiteFunction var1) {
      SchemaRequestData var2 = this.getSchemaRequestData(var1);
      return new SchemasDownloader(this.infoProvider, var2);
   }

   private SchemaRequestData getSchemaRequestData(NetSuiteFunction var1) {
      SchemaRequestData var2 = null;
      if (var1.getFunctionType() == NetSuiteFunction.FunctionType.DELETE) {
         var2 = SchemaRequestDataNetSuite.refreshingMultipleSchemasNetSuite(
            var1.getEndpoint(), var1.getFunctionType(), null, null, this.defLookup, this.cfLookup
         );
      } else if (var1.getFunctionType() == NetSuiteFunction.FunctionType.QUERY) {
         NetSuiteQuery var3 = (NetSuiteQuery)var1;
         var2 = SchemaRequestDataNetSuite.refreshingMultipleSchemasNetSuite(
            var1.getEndpoint(),
            var1.getFunctionType(),
            new NetSuiteConnectorObject(var1.getObjectName()),
            var3.getAdvancedSearchProperty(),
            this.defLookup,
            this.cfLookup
         );
      } else {
         var2 = SchemaRequestDataNetSuite.refreshingMultipleSchemasNetSuite(
            var1.getEndpoint(), var1.getFunctionType(), new NetSuiteConnectorObject(var1.getObjectName()), null, this.defLookup, this.cfLookup
         );
      }

      return var2;
   }
}
