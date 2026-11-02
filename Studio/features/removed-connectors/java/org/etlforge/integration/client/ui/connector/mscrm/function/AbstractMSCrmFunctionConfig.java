package org.etlforge.integration.client.ui.connector.mscrm.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.ReadOnlyFunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.function.AbstractConnectorCallConfig;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.operation.MSCrmOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.mscrm.FunctionType;

public abstract class AbstractMSCrmFunctionConfig<F extends MSCrmFunction> extends AbstractConnectorCallConfig<F, MSCrmEndpoint> {
   public AbstractMSCrmFunctionConfig(EntityType var1, EntityWizardFactory var2, TreeMapperInfoProvider var3, MappingStructureFactory var4) {
      super(var1, var2, var3, var4, MSCrmOperationType.TYPE);
   }

   @Override
   protected final TreeWidget<MSCrmEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.MSCrmEndpoint, MSCrmEndpoint.class, null);
   }

   protected List<? extends FunctionProperty> getProperties(F var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      FunctionType var4 = var1.getFunctionType();
      var3.add(new ReadOnlyFunctionProperty("Type", var4.toString()));
      var3.add(new ReadOnlyFunctionProperty("Object", var1.getObjectName()));
      return var3;
   }
}
