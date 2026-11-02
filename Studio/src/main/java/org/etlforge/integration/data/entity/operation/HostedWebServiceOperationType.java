package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.SystemTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.HostedWebServiceCallTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceCallFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServicePipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServicePipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServicePipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServiceTransformationFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class HostedWebServiceOperationType extends OperationType {
   public static final HostedWebServiceOperationType TYPE = new HostedWebServiceOperationType();

   private HostedWebServiceOperationType() {
      super(
         7,
         "HostedWebService",
         "Hosted Web Service",
         "Host a web service",
         ImmutableSet.of(PipelineActivityType.WEBSERVICECALL, PipelineActivityType.TRANSFORMATION)
      );
   }

   @Override
   public boolean isStartedByEngine() {
      return false;
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return RunnableStatus.notRunnable(
         "A hosted web service operation cannot be " + (var1 == OperationExecutionMode.RUN ? "started" : "tested") + " as a stand-alone operation"
      );
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new HostedWebServiceCallTemplate().getPipeline();
   }

   @Override
   public OperationFunctionCollector getFunctionCollector() {
      return new HostedWebServiceFunctionCollector();
   }

   @Override
   public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new HostedWebServiceInsertionPolicy(var1);
   }

   @Override
   public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new HostedWebServiceAutoCompleter(var1);
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      return new HostedWebServicePipelineFactory();
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new HostedWebServicePipelineValidator(var1, var2, var3);
   }

   @Override
   public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new HostedWebServiceRemovingPolicy(var1);
   }

   public EntityFilterProvider<WebServiceCall> getWebServiceCallActivityFilterProvider(OperationPipeline var1) {
      return new HostedWebServiceCallFilterProvider(var1);
   }

   @Override
   public EntityFilter getSourceFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 == SystemSource.WEB_SERVICE_CALL;
         }
      };
   }

   @Override
   public EntityFilter getTargetFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 == SystemTarget.WEB_SERVICE_CALL;
         }
      };
   }

   @Override
   public EntityFilterProvider<Transformation> getTransformationActivityFilterProvider(OperationPipeline var1) {
      return new HostedWebServiceTransformationFilterProvider(var1);
   }

   @Override
   public boolean hasPostConnectionPoint(OperationPipeline var1) {
      HostedWebServicePipelineBrowser var2 = new HostedWebServicePipelineBrowser(var1);
      return var1.getLastActivity() == var2.getFirstTransformationActivity();
   }

   @Override
   public boolean hasPreConnectionPoint() {
      return false;
   }

   @Override
   public boolean hasSourceHistory() {
      return false;
   }

   @Override
   public boolean isChunkable() {
      return false;
   }

   @Override
   public boolean isEmailNotificationsSupported() {
      return false;
   }

   @Override
   public NewTransformationPreparer getNewTransformationPreparer() {
      return new HostedWebServiceOperationType.NewTransformationPreparerImpl();
   }

   private static class NewTransformationPreparerImpl implements NewTransformationPreparer {
      private NewTransformationPreparerImpl() {
      }

      @Override
      public void prepareTransformation(Transformation var1, OperationPipeline var2, PipelineActivity var3) {
         HostedWebServicePipelineBrowser var4 = new HostedWebServicePipelineBrowser(var2);
         WebServiceCall var5 = var4.getWebServiceCall();
         if (var5 != null) {
            if (var3 == var4.getFirstTransformationActivity()) {
               this.configureFirstTransformation(var1, var5, var4);
            } else if (var3 == var4.getSecondTransformationActivity()) {
               this.configureResponse(var1, var5);
            }
         }
      }

      private void configureFirstTransformation(Transformation var1, WebServiceCall var2, HostedWebServicePipelineBrowser var3) {
         WebServiceRequestStructure var4 = var2.getInputStructure();
         var1.setInputStructure(var4);
         if (var3.getSecondTransformationActivity() == null) {
            this.configureResponse(var1, var2);
            var1.setName(var2.getName() + " - Implementation");
         }
      }

      private void configureResponse(Transformation var1, WebServiceCall var2) {
         WebServiceResponseStructure var3 = var2.getOutputStructure();
         var1.setOutputStructure(var3);
      }
   }
}
