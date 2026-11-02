package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.DefaultOperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionTransformationFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationSourceFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationTargetFilterProvider;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallRequestStructure;
import org.etlforge.integration.data.structure.FunctionCallResponseStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public abstract class FunctionCallOperationType<T extends IntegrationEntity & Function, A extends EntityActivity<T>> extends OperationType {
   private final Class<T> functionClass;
   private final PipelineActivityType functionType;
   private final FunctionCallPipelineBrowserFactory<T, A> browserFactory;

   public FunctionCallOperationType(int var1, String var2, String var3, String var4, Class<T> var5, PipelineActivityType var6) {
      super(
         var1,
         var2,
         var3,
         var4,
         ImmutableSet.of(PipelineActivityType.SOURCE, PipelineActivityType.TRANSFORMATION, var6, PipelineActivityType.TARGET, PipelineActivityType.SCRIPT)
      );
      this.functionClass = var5;
      this.functionType = var6;
      this.browserFactory = this.createPipelineBrowserFactory();
   }

   protected abstract FunctionCallPipelineBrowserFactory<T, A> createPipelineBrowserFactory();

   @Override
   public final boolean isEmailNotificationsSupported() {
      return true;
   }

   @Override
   public final boolean isChunkable() {
      return true;
   }

   @Override
   public final boolean hasSourceHistory() {
      return true;
   }

   @Override
   public final RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return RunnableStatus.RUNNABLE;
   }

   @Override
   public final boolean hasPreConnectionPoint() {
      return true;
   }

   @Override
   public EntityFilter getSourceFilter() {
      return EntityFilters.allOfType(EntityType.Source);
   }

   @Override
   public EntityFilter getTargetFilter() {
      return EntityFilters.allOfType(EntityType.Target);
   }

   @Override
   public final OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      FunctionCallPipelineBrowser var4 = this.browserFactory.createBrowser(var2);
      return new FunctionCallPipelineValidator(this.functionType, var1, var2, var4, var3);
   }

   @Override
   public final PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new FunctionCallAutoCompleter<>(this.functionClass, this.browserFactory, var1);
   }

   @Override
   public final ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new FunctionCallRemovingPolicy(this, this.browserFactory.createBrowser(var1));
   }

   @Override
   public final OperationPipelineFactory getPipelineFactory() {
      throw new UnsupportedOperationException(
         "The documentation for OperationPipelineFactory says it is needed when opening a pre-2.0 project. It is thus not applicable for Connector Call operations."
      );
   }

   @Override
   public final EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline var1) {
      return new TransformationSourceFilterProvider(var1);
   }

   @Override
   public final EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline var1) {
      return new TransformationTargetFilterProvider(var1);
   }

   @Override
   public final EntityFilterProvider<Transformation> getTransformationActivityFilterProvider(OperationPipeline var1) {
      return new FunctionTransformationFilterProvider(this.createPipelineBrowser(var1));
   }

   @Override
   public final NewTransformationPreparer getNewTransformationPreparer() {
      return new FunctionCallOperationType.FunctionCallTransformationPreparer(this.browserFactory);
   }

   @Override
   public final OperationFunctionCollector getFunctionCollector() {
      return new DefaultOperationFunctionCollector();
   }

   @Override
   public ImmutableList<SystemTransformation> getApplicableSystemTransformations(Operation var1, OperationPipeline var2, TransformationActivity var3) {
      FunctionCallPipelineBrowser var4 = this.createPipelineBrowser(var2);
      return var3 == var4.getResponseActivity() ? ImmutableList.of(SystemTransformation.PASS_THROUGH) : ImmutableList.of();
   }

   @Override
   public final ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new FunctionCallInsertionPolicy(var1, this.functionType);
   }

   public final FunctionCallFilterProvider<T, A> getFunctionCallFilterProvider(OperationPipeline var1) {
      return new FunctionCallFilterProvider<>(this.createPipelineBrowser(var1));
   }

   public final FunctionCallPipelineBrowser<T, A> createPipelineBrowser(OperationPipeline var1) {
      return this.browserFactory.createBrowser(var1);
   }

   public final boolean isRequest(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      return var2 instanceof FunctionCallRequestStructure && this.isRecognizedFunctionCall((FunctionCallRequestStructure)var2);
   }

   public final boolean isResponse(Transformation var1) {
      DataStructure var2 = var1.getInputStructure();
      return var2 instanceof FunctionCallResponseStructure && this.isRecognizedFunctionCall((FunctionCallResponseStructure)var2);
   }

   private boolean isRecognizedFunctionCall(FunctionCallStructure var1) {
      IntegrationEntityId var2 = var1.getFunctionCallId();
      return this.isRecognizedFunctionCallType(var2.getEntityType());
   }

   protected abstract boolean isRecognizedFunctionCallType(EntityType var1);

   private static class FunctionCallTransformationPreparer implements NewTransformationPreparer {
      private final FunctionCallPipelineBrowserFactory browserFactory;

      public FunctionCallTransformationPreparer(FunctionCallPipelineBrowserFactory var1) {
         this.browserFactory = var1;
      }

      @Override
      public void prepareTransformation(Transformation var1, OperationPipeline var2, PipelineActivity var3) {
         FunctionCallPipelineBrowser var4 = this.browserFactory.createBrowser(var2);
         Function var5 = (Function)var4.getFunction();
         if (var5 != null) {
            if (var3 == var4.getRequestActivity()) {
               this.configureRequest(var1, var5);
            } else if (var3 == var4.getResponseActivity()) {
               this.configureResponse(var1, var5);
            }
         }
      }

      private void configureResponse(Transformation var1, Function var2) {
         DataStructure var3 = var2.getOutputStructure();
         var1.setInputStructure(var3);
         var1.setName(var2.getName() + " - Response");
      }

      private void configureRequest(Transformation var1, Function var2) {
         DataStructure var3 = var2.getInputStructure();
         var1.setOutputStructure(var3);
         var1.setName(var2.getName() + " - Request");
      }
   }
}
