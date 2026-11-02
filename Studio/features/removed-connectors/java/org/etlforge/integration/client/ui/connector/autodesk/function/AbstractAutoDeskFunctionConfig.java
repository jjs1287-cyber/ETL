package org.etlforge.integration.client.ui.connector.autodesk.function;

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
import org.etlforge.integration.data.connector.AutoDeskEndpoint;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.connector.operation.AutoDeskOperationType;
import org.etlforge.integration.data.entity.EntityType;

public abstract class AbstractAutoDeskFunctionConfig<F extends AutoDeskFunction> extends AbstractConnectorCallConfig<F, AutoDeskEndpoint> {
   public AbstractAutoDeskFunctionConfig(EntityType var1, EntityWizardFactory var2, TreeMapperInfoProvider var3, MappingStructureFactory var4) {
      super(var1, var2, var3, var4, AutoDeskOperationType.TYPE);
   }

   @Override
   protected final TreeWidget<AutoDeskEndpoint> createEndpointSelector() {
      return new TreeWidget<>(this.getProjectBrowser(), EntityType.AutoDeskEndpoint, AutoDeskEndpoint.class, null);
   }

   protected List<? extends FunctionProperty> getProperties(F var1, RefreshFunctionJobFactory var2) {
      ArrayList var3 = Lists.newArrayList();
      AutoDeskFunction.FunctionType var4 = var1.getFunctionType();
      var3.add(new ReadOnlyFunctionProperty("Type", var4.toString()));
      var3.add(new ReadOnlyFunctionProperty("Workspace name", var1.getWorkspaceName()));
      var3.add(new WorkspaceIdProperty(var1));
      return var3;
   }
}
