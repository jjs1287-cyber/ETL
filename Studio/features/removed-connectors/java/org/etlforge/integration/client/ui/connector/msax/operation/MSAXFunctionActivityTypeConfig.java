package org.etlforge.integration.client.ui.connector.msax.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.connector.operation.MSAXFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSAXFunctionActivityType;
import org.etlforge.integration.data.connector.operation.MSAXOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSAXFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public MSAXFunctionActivityTypeConfig() {
      super(MSAXFunctionActivityType.TYPE, EntityType.MSAXFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new MSAXFunctionActivityNode((MSAXFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<MSAXFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      MSAXFunctionActivity var4 = (MSAXFunctionActivity)var3;
      ContentProviderContext var5 = ContentProviderContext.create(var2.getPipeline(), var4);
      ActivityContentAcceptor var6 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var5, var1.getProject(), MSAXOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()), EntityType.MSAXFunction, MSAXFunction.class, var6
      );
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
