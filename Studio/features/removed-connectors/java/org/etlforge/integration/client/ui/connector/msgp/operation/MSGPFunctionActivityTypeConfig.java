package org.etlforge.integration.client.ui.connector.msgp.operation;

import org.etlforge.integration.client.ui.connector.operation.UmbrellaFunctionFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.connector.operation.MSGPFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSGPFunctionActivityType;
import org.etlforge.integration.data.connector.operation.MSGPOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSGPFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public MSGPFunctionActivityTypeConfig() {
      super(MSGPFunctionActivityType.TYPE, EntityType.MSGPFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new MSGPFunctionActivityNode((MSGPFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<MSGPFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      MSGPFunctionActivity var4 = (MSGPFunctionActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var5, var1.getProject(), MSGPOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()), EntityType.MSGPFunction, MSGPFunction.class, var6
      );
      UmbrellaFunctionFactory var8 = new UmbrellaFunctionFactory(EntityType.MSGPFunction, var1.getEntityFactory());
      var7.setEntityFactory(var8);
      return var7;
   }
}
