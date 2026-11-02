package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.TargetActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;

public final class TargetActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public TargetActivityTypeConfig() {
      super(PipelineActivityType.TARGET, EntityType.Target);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new TargetActivityNode((TargetActivity)var1, var2);
   }

   @Override
   protected void addToContextMenu(PipelineActivityNode var1, OperationGraphController var2, List<Action> var3) {
      Collections.addAll(var3, var2.getTestConnectionAction(), var2.getAssignPluginAction());
   }

   @Override
   public EntityContentProvider<Target> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      TargetActivity var4 = (TargetActivity)var3;
      OperationPipeline var5 = var2.getPipeline();
      ContentProviderContext var6 = ContentProviderContext.create(var5, var4);
      EntityFilterProvider var7 = var5.getType().getTargetActivityFilterProvider(var5);
      ActivityContentAcceptor var8 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var9 = EntityContentProvider.create(var6, var1.getProject(), var7, EntityType.Target, Target.class, var8);
      var9.setEntityFactory(var1.getEntityFactory());
      return var9;
   }
}
