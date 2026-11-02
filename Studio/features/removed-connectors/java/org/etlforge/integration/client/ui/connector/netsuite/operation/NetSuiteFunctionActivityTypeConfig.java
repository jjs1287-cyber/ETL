package org.etlforge.integration.client.ui.connector.netsuite.operation;

import org.etlforge.integration.client.ui.connector.operation.UmbrellaFunctionFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.operation.NetSuiteFunctionActivity;
import org.etlforge.integration.data.connector.operation.NetSuiteFunctionActivityType;
import org.etlforge.integration.data.connector.operation.NetSuiteOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class NetSuiteFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public NetSuiteFunctionActivityTypeConfig() {
      super(NetSuiteFunctionActivityType.TYPE, EntityType.NetSuiteFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new NetSuiteFunctionActivityNode((NetSuiteFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<NetSuiteFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      NetSuiteFunctionActivity var4 = (NetSuiteFunctionActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var5,
         var1.getProject(),
         NetSuiteOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()),
         EntityType.NetSuiteFunction,
         NetSuiteFunction.class,
         var6
      );
      UmbrellaFunctionFactory var8 = new UmbrellaFunctionFactory(EntityType.NetSuiteFunction, var1.getEntityFactory());
      var7.setEntityFactory(var8);
      return var7;
   }
}
