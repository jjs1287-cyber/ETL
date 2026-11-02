package org.etlforge.integration.client.ui.connector.msgp.function;

import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.msgp.function.wizard.MSGPFunctionWizardFactory;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;

public final class MSGPFunctionConfig extends AbstractMSGPFunctionConfig<MSGPFunction> {
   private final ConnectorCallInfoProvider infoProvider;

   public MSGPFunctionConfig(
      EntityType var1, MSGPFunctionWizardFactory var2, TreeMapperInfoProvider var3, ConnectorCallInfoProvider var4, MappingStructureFactory var5
   ) {
      super(var1, var2, var3, var5);
      this.infoProvider = var4;
      this.setSearchValuesCollector(new MSGPFunctionSearchValues());
   }

   protected Callable<Schemas> getSchemaGenerator(MSGPFunction var1) {
      SchemaRequestData var2 = SchemaRequestData.singleSchemas(var1.getEndpoint(), var1.getFunctionType(), new SimpleConnectorObject(var1.getObjectName()));
      return new SchemasDownloader(this.infoProvider, var2);
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
