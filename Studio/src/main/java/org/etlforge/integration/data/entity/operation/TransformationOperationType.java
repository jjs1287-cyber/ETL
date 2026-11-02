package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.SystemTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.DefaultOperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationSourceFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationTargetFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.structure.StructureLocationAssociations;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class TransformationOperationType extends OperationType {
   public static final TransformationOperationType TYPE = new TransformationOperationType(3);

   private TransformationOperationType(int var1) {
      super(
         var1,
         "Transformation",
         "Transformation",
         "Transform data",
         ImmutableSet.of(PipelineActivityType.SOURCE, PipelineActivityType.TRANSFORMATION, PipelineActivityType.TARGET, PipelineActivityType.SCRIPT)
      );
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return RunnableStatus.RUNNABLE;
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new TransformationTemplate().getPipeline();
   }

   @Override
   public OperationFunctionCollector getFunctionCollector() {
      return new DefaultOperationFunctionCollector();
   }

   @Override
   public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new TransformationInsertionPolicy(var1);
   }

   @Override
   public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new TransformationAutoCompleter(var1);
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      return new TransformationPipelineFactory();
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new TransformationPipelineValidator(var1, var2, var3);
   }

   @Override
   public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new TransformationRemovingPolicy();
   }

   @Override
   public EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline var1) {
      return new TransformationSourceFilterProvider(var1);
   }

   @Override
   public EntityFilter getSourceFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Source ? var1 != SystemSource.WEB_SERVICE_CALL : false;
         }
      };
   }

   @Override
   public EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline var1) {
      return new TransformationTargetFilterProvider(var1);
   }

   @Override
   public EntityFilter getTargetFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Target ? var1 != SystemTarget.WEB_SERVICE_CALL : false;
         }
      };
   }

   @Override
   public EntityFilterProvider<Transformation> getTransformationActivityFilterProvider(OperationPipeline var1) {
      return new TransformationFilterProvider(var1);
   }

   @Override
   public boolean hasSourceHistory() {
      return true;
   }

   @Override
   public boolean isChunkable() {
      return true;
   }

   @Override
   public boolean isEmailNotificationsSupported() {
      return true;
   }

   @Override
   public NewTransformationPreparer getNewTransformationPreparer() {
      return new TransformationOperationType.NewTransformationPreparerImpl();
   }

   private static class NewTransformationPreparerImpl implements NewTransformationPreparer {
      private NewTransformationPreparerImpl() {
      }

      @Override
      public void prepareTransformation(Transformation var1, OperationPipeline var2, PipelineActivity var3) {
         TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var2);
         checkSource(var1, var4);
         checkTarget(var1, var4);
      }

      private static void checkSource(Transformation var0, TransformationPipelineBrowser var1) {
         Source var2 = var1.getSource();
         if (isApplicable(var2)) {
            var0.setSourceId(var2.getID());
         }
      }

      private static boolean isApplicable(Locatable var0) {
         if (var0 == null) {
            return false;
         }

         DataLocation var1 = var0.getLocation();
         if (var1 == null) {
            return false;
         }

         DataLocationType var2 = var1.getDataLocationType();
         return StructureLocationAssociations.hasAssociatedStructure(var2);
      }

      private static void checkTarget(Transformation var0, TransformationPipelineBrowser var1) {
         Target var2 = var1.getTarget();
         if (isApplicable(var2)) {
            var0.setTargetId(var2.getID());
         }
      }
   }
}
