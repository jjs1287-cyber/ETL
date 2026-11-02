package org.etlforge.integration.client.ui.connector.netsuite.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.ReadOnlyFunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.function.AbstractConnectorCallConfig;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.NetSuiteEntityWizardFactory;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsDesignLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionDesignLookup;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.operation.NetSuiteOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public abstract class AbstractNetSuiteFunctionConfig<F extends NetSuiteFunction> extends AbstractConnectorCallConfig<F, NetSuiteEndpoint> {
   private final NetSuiteDefinitionDesignLookup defLookup;
   private final NetSuiteCustomFieldsDesignLookup cfLookup;
   private final ConnectorCallInfoProvider infoProvider;

   protected AbstractNetSuiteFunctionConfig(
      EntityType var1, NetSuiteEntityWizardFactory var2, TreeMapperInfoProvider var3, ConnectorCallInfoProvider var4, MappingStructureFactory var5
   ) {
      super(var1, var2, var3, var5, NetSuiteOperationType.TYPE);
      this.infoProvider = var4;
      this.defLookup = var2.getNetSuiteDefinitionDesignLookup();
      this.cfLookup = var2.getNetSuiteCustomFieldsDesignLookup();
   }

   @Override
   protected final TreeWidget<NetSuiteEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.NetSuiteEndpoint, NetSuiteEndpoint.class, null);
   }

   protected Callable<Schemas> getSchemaGenerator(F var1) {
      NetSuiteSchemaGenerator var2 = new NetSuiteSchemaGenerator(this.defLookup, this.cfLookup, this.infoProvider);
      return var2.getSchemaGenerator(var1);
   }

   protected List<? extends FunctionProperty> getProperties(F var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      NetSuiteFunction.FunctionType var4 = var1.getFunctionType();
      var3.add(new ReadOnlyFunctionProperty("Type", var4.toString()));
      if (NetSuiteFunction.FunctionType.DELETE != var4) {
         NetSuiteConnectorObject var5 = new NetSuiteConnectorObject(var1.getObjectName());
         var3.add(new ReadOnlyFunctionProperty("Object", var5.getName()));
      }

      return var3;
   }

   @Override
   protected final FileUploadApi getFileUploadApi() {
      return this.infoProvider;
   }
}
