package org.etlforge.integration.client.ui.connector.mscrm.operation;

import org.etlforge.integration.client.ui.connector.operation.UmbrellaFunctionFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.operation.MSCrmFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSCrmFunctionActivityType;
import org.etlforge.integration.data.connector.operation.MSCrmOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSCrmFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public MSCrmFunctionActivityTypeConfig() {
      super(MSCrmFunctionActivityType.TYPE, EntityType.MSCrmFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new MSCrmFunctionActivityNode((MSCrmFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<MSCrmFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      MSCrmFunctionActivity var4 = (MSCrmFunctionActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var5,
         var1.getProject(),
         MSCrmOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()),
         EntityType.MSCrmFunction,
         MSCrmFunction.class,
         var6
      );
      UmbrellaFunctionFactory var8 = new UmbrellaFunctionFactory(EntityType.MSCrmFunction, var1.getEntityFactory());
      var7.setEntityFactory(var8);
      return var7;
   }
}
