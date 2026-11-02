package org.etlforge.integration.client.ui.connector.ebs.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.connector.operation.EBSFunctionActivity;
import org.etlforge.integration.data.connector.operation.EBSFunctionActivityType;
import org.etlforge.integration.data.connector.operation.EBSOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class EBSFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public EBSFunctionActivityTypeConfig() {
      super(EBSFunctionActivityType.TYPE, EntityType.EBSFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new EBSFunctionActivityNode((EBSFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<EBSFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      EBSFunctionActivity var4 = (EBSFunctionActivity)var3;
      ActivityContentAcceptor var5 = ActivityContentAcceptor.create(var1, var2, var4);
      ContentProviderContext var6 = ContentProviderContext.create(var2.getPipeline(), var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var6, var1.getProject(), EBSOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()), EntityType.EBSFunction, EBSFunction.class, var5
      );
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
