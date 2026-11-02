package org.etlforge.integration.client.ui.connector.netsuite.function;

import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.netsuite.endpoint.NetSuiteEndpointConfig;
import org.etlforge.integration.client.ui.connector.netsuite.function.wizard.NetSuiteFunctionWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;

public final class NetSuiteFunctionConfig extends AbstractNetSuiteFunctionConfig<NetSuiteFunction> {
   private final NetSuiteEndpointConfig endpointConfig;

   public NetSuiteFunctionConfig(
      EntityType var1,
      NetSuiteFunctionWizardFactory var2,
      TreeMapperInfoProvider var3,
      ConnectorCallInfoProvider var4,
      MappingStructureFactory var5,
      NetSuiteEndpointConfig var6
   ) {
      super(var1, var2, var3, var4, var5);
      this.setSearchValuesCollector(new NetSuiteFunctionSearchValues());
      this.endpointConfig = var6;
   }

   @Override
   protected RefreshFunctionJobFactory getRefreshFunctionJobFactory(FileStore var1, MappingStructureRefresher var2) {
      return this.endpointConfig.getRefreshJobFactory();
   }
}
