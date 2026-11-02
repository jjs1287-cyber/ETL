package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.UseSourceFromHttpEndpointAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.UseSourceFromPreviousOperationAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.SourceActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;

public final class SourceActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public SourceActivityTypeConfig() {
      super(PipelineActivityType.SOURCE, EntityType.Source);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new SourceActivityNode((SourceActivity)var1, var2);
   }

   @Override
   protected void addToContextMenu(PipelineActivityNode var1, OperationGraphController var2, List<Action> var3) {
      SourceActivityNode var4 = (SourceActivityNode)var1;
      Collections.addAll(
         var3,
         new UseSourceFromPreviousOperationAction(var2, var4),
         new UseSourceFromHttpEndpointAction(var2, var4),
         var2.getTestConnectionAction(),
         var2.getAssignPluginAction()
      );
   }

   @Override
   public EntityContentProvider<Source> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      SourceActivity var4 = (SourceActivity)var3;
      OperationPipeline var5 = var2.getPipeline();
      ContentProviderContext var6 = ContentProviderContext.create(var5, var4);
      EntityFilterProvider var7 = var5.getType().getSourceActivityFilterProvider(var5);
      ActivityContentAcceptor var8 = ActivityContentAcceptor.create(var1, var2, var4);
      EntityContentProvider var9 = EntityContentProvider.create(var6, var1.getProject(), var7, EntityType.Source, Source.class, var8);
      var9.setEntityFactory(var1.getEntityFactory());
      return var9;
   }
}
