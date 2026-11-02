package org.etlforge.integration.client.ui.connector.ebs.function;

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
import org.etlforge.integration.client.ui.connector.ebs.function.wizard.EBSFunctionWizardFactory;
import org.etlforge.integration.client.ui.connector.function.AbstractConnectorCallConfig;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.connector.EBSEndpoint;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.connector.operation.EBSOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class EBSFunctionConfig extends AbstractConnectorCallConfig<EBSFunction, EBSEndpoint> {
   private final ConnectorCallInfoProvider infoProvider;

   public EBSFunctionConfig(EBSFunctionWizardFactory var1, TreeMapperInfoProvider var2, ConnectorCallInfoProvider var3, MappingStructureFactory var4) {
      super(EntityType.EBSFunction, var1, var2, var4, EBSOperationType.TYPE);
      this.setSearchValuesCollector(new EBSFunctionSearchValues());
      this.infoProvider = var3;
   }

   @Override
   protected final TreeWidget<EBSEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.EBSEndpoint, EBSEndpoint.class, null);
   }

   protected Callable<Schemas> getSchemaGenerator(EBSFunction var1) {
      SchemaRequestData var2 = SchemaRequestData.singleSchemas(var1.getEndpoint(), var1.getCatalog(), var1.getProgram());
      return new SchemasDownloader(this.infoProvider, var2);
   }

   protected List<? extends FunctionProperty> getProperties(EBSFunction var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(new ReadOnlyFunctionProperty("Catalog", var1.getCatalog().getName()));
      var3.add(new ReadOnlyFunctionProperty("Program", var1.getProgram().getName()));
      return var3;
   }

   @Override
   protected FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
