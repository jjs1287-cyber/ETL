package org.etlforge.integration.client.ui.connector.sap.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ContentProviderContext;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityContentProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EntityTypeBasedActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.connector.operation.SapFunctionActivity;
import org.etlforge.integration.data.connector.operation.SapFunctionActivityType;
import org.etlforge.integration.data.connector.operation.SapOperationType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class SapFunctionActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public SapFunctionActivityTypeConfig() {
      super(SapFunctionActivityType.TYPE, EntityType.SapFunction);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new SapFunctionActivityNode((SapFunctionActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<SapFunction> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      SapFunctionActivity var4 = (SapFunctionActivity)var3;
      ActivityContentAcceptor var5 = ActivityContentAcceptor.create(var1, var2, var4);
      ContentProviderContext var6 = ContentProviderContext.create(var2.getPipeline(), var4);
      EntityContentProvider var7 = EntityContentProvider.create(
         var6, var1.getProject(), SapOperationType.TYPE.getFunctionCallFilterProvider(var2.getPipeline()), EntityType.SapFunction, SapFunction.class, var5
      );
      var7.setEntityFactory(var1.getEntityFactory());
      return var7;
   }
}
