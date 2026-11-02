package org.etlforge.integration.client.ui.connector.mscrm.function;

import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.MSCrmFunctionWizardFactory;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;

public final class MSCrmFunctionConfig extends AbstractMSCrmFunctionConfig<MSCrmFunction> {
   private final ConnectorCallInfoProvider infoProvider;

   public MSCrmFunctionConfig(
      EntityType var1, MSCrmFunctionWizardFactory var2, TreeMapperInfoProvider var3, ConnectorCallInfoProvider var4, MappingStructureFactory var5
   ) {
      super(var1, var2, var3, var5);
      this.infoProvider = var4;
      this.setSearchValuesCollector(new MSCrmFunctionSearchValues());
   }

   protected Callable<Schemas> getSchemaGenerator(MSCrmFunction var1) {
      SchemaRequestData var2 = SchemaRequestData.singleSchemas(var1.getEndpoint(), var1.getFunctionType(), new SimpleConnectorObject(var1.getObjectName()));
      return new SchemasDownloader(this.infoProvider, var2);
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
