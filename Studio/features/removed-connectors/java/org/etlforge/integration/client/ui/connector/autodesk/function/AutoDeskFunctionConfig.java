package org.etlforge.integration.client.ui.connector.autodesk.function;

import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.autodesk.function.wizard.AutoDeskFunctionWizardFactory;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class AutoDeskFunctionConfig extends AbstractAutoDeskFunctionConfig<AutoDeskFunction> {
   private final ConnectorCallInfoProvider infoProvider;

   public AutoDeskFunctionConfig(
      EntityType var1, AutoDeskFunctionWizardFactory var2, TreeMapperInfoProvider var3, ConnectorCallInfoProvider var4, MappingStructureFactory var5
   ) {
      super(var1, var2, var3, var5);
      this.infoProvider = var4;
      this.setSearchValuesCollector(new AutoDeskFunctionSearchValues());
   }

   protected Callable<Schemas> getSchemaGenerator(AutoDeskFunction var1) {
      return new SchemaDownloader(this.infoProvider, var1);
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
