package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collections;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.project.ProjectUtils;

public final class TransformationToOperationFactory extends ProjectItemTransform {
   private final ImmutableList<TransformationToOperationPipelineFactory> pipelineFactories;

   public TransformationToOperationFactory(UiEntityFactory var1) {
      this(var1, Collections.emptyList());
   }

   public TransformationToOperationFactory(UiEntityFactory var1, Iterable<TransformationToOperationPipelineFactory> var2) {
      super(var1, EntityType.Operation);
      this.pipelineFactories = new Builder<TransformationToOperationPipelineFactory>()
         .addAll(var2)
         .add(new FunctionCallTransformationToOperationPipelineFactory(WebServiceOperationType.TYPE))
         .add(new SimpleTransformationToOperationPipelineFactory())
         .build();
   }

   @Override
   public boolean isSupported(IntegrationEntity var1, Optional<Folder> var2) {
      if (var1 instanceof Transformation) {
         Transformation var3 = (Transformation)var1;

         for (TransformationToOperationPipelineFactory var5 : this.pipelineFactories) {
            if (var5.isSupported(var3)) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   protected void configureDestination(IntegrationEntity var1, IntegrationEntity var2) {
      OperationPipeline var3 = this.createPipeline((Transformation)var1);
      ((Operation)var2).setPipeline(var3);
   }

   private OperationPipeline createPipeline(Transformation var1) {
      IntegrationEntityLookup var2 = this.getItemLookup(var1);

      for (TransformationToOperationPipelineFactory var4 : this.pipelineFactories) {
         if (var4.isSupported(var1)) {
            return var4.createPipeline(var1, var2);
         }
      }

      throw new UnsupportedOperationException("The specified Transformation is not supported. This is a programming error.");
   }

   private IntegrationEntityLookup getItemLookup(Transformation var1) {
      return ProjectUtils.getProject(var1).getItemLookup();
   }
}
