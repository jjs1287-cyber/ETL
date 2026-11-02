package org.etlforge.integration.client.ui.connector.msgp.function;

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
import org.etlforge.integration.data.connector.MSGPEndpoint;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.connector.operation.MSGPOperationType;
import org.etlforge.integration.data.entity.EntityType;

public abstract class AbstractMSGPFunctionConfig<F extends MSGPFunction> extends AbstractConnectorCallConfig<F, MSGPEndpoint> {
   public AbstractMSGPFunctionConfig(EntityType var1, EntityWizardFactory var2, TreeMapperInfoProvider var3, MappingStructureFactory var4) {
      super(var1, var2, var3, var4, MSGPOperationType.TYPE);
   }

   @Override
   protected final TreeWidget<MSGPEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.MSGPEndpoint, MSGPEndpoint.class, null);
   }

   protected List<? extends FunctionProperty> getProperties(F var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      MSGPFunction.FunctionType var4 = var1.getFunctionType();
      var3.add(new ReadOnlyFunctionProperty("Type", var4.toString()));
      var3.add(new ReadOnlyFunctionProperty("Object", var1.getObjectName()));
      return var3;
   }
}
