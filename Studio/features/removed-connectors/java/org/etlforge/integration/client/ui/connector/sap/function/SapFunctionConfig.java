package org.etlforge.integration.client.ui.connector.sap.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.schema.SchemaRequestData;
import org.etlforge.integration.client.connector.schema.SchemasDownloader;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.ReadOnlyFunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.function.AbstractConnectorCallConfig;
import org.etlforge.integration.client.ui.connector.sap.function.wizard.SapFunctionWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.connector.SapEndpoint;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.connector.operation.SapOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class SapFunctionConfig extends AbstractConnectorCallConfig<SapFunction, SapEndpoint> {
   private final ConnectorCallInfoProvider infoProvider;

   public SapFunctionConfig(SapFunctionWizardFactory var1, TreeMapperInfoProvider var2, ConnectorCallInfoProvider var3, MappingStructureFactory var4) {
      super(EntityType.SapFunction, var1, var2, var4, SapOperationType.TYPE);
      this.setSearchValuesCollector(new SapFunctionSearchValues());
      this.infoProvider = var3;
   }

   @Override
   protected final TreeWidget<SapEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.SapEndpoint, SapEndpoint.class, null);
   }

   protected Callable<Schemas> getSchemaGenerator(SapFunction var1) {
      SchemaRequestData var2 = SchemaRequestData.singleSchemas(var1.getEndpoint(), var1.getFunctionType(), var1.getCategory(), var1.getApiFunction());
      return new SchemasDownloader(this.infoProvider, var2);
   }

   protected List<? extends FunctionProperty> getProperties(SapFunction var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      SapFunction.FunctionType var4 = var1.getFunctionType();
      var3.add(new ReadOnlyFunctionProperty("Type", var4.toString()));
      if (var4 == SapFunction.FunctionType.IDOC) {
         var3.add(new ReadOnlyFunctionProperty("Category", var1.getCategory().getName()));
      }

      var3.add(new ReadOnlyFunctionProperty("Function", var1.getApiFunction().getName()));
      return var3;
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
