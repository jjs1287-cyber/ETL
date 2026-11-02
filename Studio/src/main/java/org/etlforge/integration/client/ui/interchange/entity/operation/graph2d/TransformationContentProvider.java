package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.NewTransformationPreparer;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;

public final class TransformationContentProvider extends EntityContentProvider<Transformation> {
   public TransformationContentProvider(
      ContentProviderContext<Transformation> var1, IntegrationProject var2, EntityFilterProvider<Transformation> var3, Receiver<Transformation> var4
   ) {
      super(var1, var2, var3, EntityType.Transformation, Transformation.class, var4);
   }

   protected void preProjectInsertion(Transformation var1) {
      ContentProviderContext var2 = this.getContext();
      if (var2 != null) {
         NewTransformationPreparer var3 = this.getTransformationPreparer(var2);
         var3.prepareTransformation(var1, var2.getPipeline(), var2.getActivity());
      }
   }

   private NewTransformationPreparer getTransformationPreparer(ContentProviderContext var1) {
      OperationPipeline var2 = var1.getPipeline();
      OperationType var3 = var2.getType();
      return var3.getNewTransformationPreparer();
   }
}
