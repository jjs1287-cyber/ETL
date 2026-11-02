package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ActivityContentAcceptor;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.SetSystemTransformationAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.TransformationActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;

public final class TransformationActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public TransformationActivityTypeConfig() {
      super(PipelineActivityType.TRANSFORMATION, EntityType.Transformation);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new TransformationActivityNode((TransformationActivity)var1, var2);
   }

   @Override
   protected void addToContextMenu(PipelineActivityNode var1, OperationGraphController var2, List<Action> var3) {
      TransformationActivity var4 = (TransformationActivity)var1.getDataObject();
      OperationWrapper var5 = var1.getParent().getDataObject();
      OperationType var6 = var5.getPipeline().getType();

      for (SystemTransformation var9 : var6.getApplicableSystemTransformations(var5.getActivity(), var5.getPipeline(), var4)) {
         String var10 = var9 == SystemTransformation.PASS_THROUGH ? "Pass-Through" : "Use Built-In " + var9.getName();
         var3.add(new SetSystemTransformationAction(var10, var9, var2, var5, var4));
      }
   }

   @Override
   public EntityContentProvider<Transformation> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      TransformationActivity var4 = (TransformationActivity)var3;
      OperationPipeline var5 = var2.getPipeline();
      ContentProviderContext var6 = ContentProviderContext.create(var5, var4);
      EntityFilterProvider var7 = var5.getType().getTransformationActivityFilterProvider(var5);
      ActivityContentAcceptor var8 = ActivityContentAcceptor.create(var1, var2, var4);
      TransformationContentProvider var9 = new TransformationContentProvider(var6, var1.getProject(), var7, var8);
      var9.setEntityFactory(var1.getEntityFactory());
      return var9;
   }
}
