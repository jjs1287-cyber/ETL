package org.etlforge.integration.data.entity.sfdc;

import com.google.common.collect.ImmutableSet;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.OperationExecutionMode;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.RunnableStatus;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.util.version.Version;

public final class BulkQueryOperationType extends OperationType {
   public static final BulkQueryOperationType TYPE = new BulkQueryOperationType(102);
   private static final Version VERSION = VersionFactory.newVersion(5, 0, 0, 41);

   private BulkQueryOperationType(int var1) {
      super(
         var1,
         "BulkQuery",
         "Bulk Query",
         "Run a Salesforce Bulk Query",
         ImmutableSet.of(PipelineActivityType.BULK_LOAD, PipelineActivityType.TARGET, PipelineActivityType.SCRIPT)
      );
   }

   @Override
   public boolean isEmailNotificationsSupported() {
      return true;
   }

   @Override
   public boolean isChunkable() {
      return false;
   }

   @Override
   public boolean hasSourceHistory() {
      return false;
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return var1 == OperationExecutionMode.RUN ? RunnableStatus.RUNNABLE : RunnableStatus.notRunnable("A bulk query operation cannot be tested.");
   }

   @Override
   public EntityFilter getSourceFilter() {
      throw new UnsupportedOperationException();
   }

   @Override
   public EntityFilter getTargetFilter() {
      return new FileBasedLocationFilter();
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new BulkQueryPipelineTemplate().getPipeline();
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      throw new UnsupportedOperationException(
         "The documentation for OperationPipelineFactory says it is needed when opening a pre-2.0 project. It is thus not applicable for Bulk Query operations."
      );
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new BulkQueryOperationType.PipelineValidatorImpl(var1, var2, var3);
   }

   @Override
   public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new PipelineAutoCompleter(var1) {
         @Override
         public void complete(OperationPipeline var1, PipelineActivity var2) {
         }
      };
   }

   @Override
   public EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline var1) {
      return new EntityFilterProvider<Target>() {
         @Override
         public EntityFilter getFilter(PipelineActivity<Target> var1) {
            return BulkQueryOperationType.this.getTargetFilter();
         }
      };
   }

   @Override
   public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new ActivityInsertionPolicy() {
         @Override
         public ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1) {
            return ImmutableSet.of(PipelineActivityType.SCRIPT);
         }
      };
   }

   @Override
   public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new ActivityRemovingPolicy() {
         @Override
         public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
            if (var1.isReadOnly()) {
               throw new RemoveActivityVetoException("The selected activity cannot be removed.");
            }
         }
      };
   }

   @Override
   public boolean isSelectable() {
      return false;
   }

   @Override
   public Version getVersionIntroduced() {
      return VERSION;
   }

   private static class PipelineValidatorImpl extends OperationPipelineValidator {
      public PipelineValidatorImpl(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
         super(var1, var2, var3);
      }

      @Override
      public void validate() {
         OperationPipelineBrowser var1 = new OperationPipelineBrowser(this.pipeline);
         Target var2 = var1.getTarget();
         if (var2 == null) {
            this.fatal("No target specified");
         } else {
            this.validateTarget(var2);
         }
      }

      private void validateTarget(Target var1) {
         if (!BulkQueryOperationType.TYPE.getTargetFilter().apply(var1)) {
            this.fatal("The selected Target is not applicable for a Bulk Query operation.");
         }
      }
   }
}
