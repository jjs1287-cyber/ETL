package org.etlforge.integration.client.ui.connector.msax.function;

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
import org.etlforge.integration.client.ui.connector.msax.function.wizard.MSAXFunctionWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.connector.MSAXEndpoint;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.connector.operation.MSAXOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;

public final class MSAXFunctionConfig extends AbstractConnectorCallConfig<MSAXFunction, MSAXEndpoint> {
   private final ConnectorCallInfoProvider infoProvider;

   public MSAXFunctionConfig(MSAXFunctionWizardFactory var1, TreeMapperInfoProvider var2, ConnectorCallInfoProvider var3, MappingStructureFactory var4) {
      super(EntityType.MSAXFunction, var1, var2, var4, MSAXOperationType.TYPE);
      this.setSearchValuesCollector(new MSAXFunctionSearchValues());
      this.infoProvider = var3;
   }

   @Override
   protected final TreeWidget<MSAXEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.MSAXEndpoint, MSAXEndpoint.class, null);
   }

   protected Callable<Schemas> getSchemaGenerator(MSAXFunction var1) {
      SchemaRequestData var2 = SchemaRequestData.refreshingMultipleSchemas(
         var1.getEndpoint(), new SimpleConnectorObject(var1.getWSDLUrl()), new SimpleConnectorObject(var1.getMSAXServiceOperation())
      );
      return new SchemasDownloader(this.infoProvider, var2);
   }

   protected List<? extends FunctionProperty> getProperties(MSAXFunction var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(new ReadOnlyFunctionProperty("Function", var1.getWSDLUrl()));
      var3.add(new ReadOnlyFunctionProperty("Function", var1.getMSAXServiceOperation()));
      return var3;
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
