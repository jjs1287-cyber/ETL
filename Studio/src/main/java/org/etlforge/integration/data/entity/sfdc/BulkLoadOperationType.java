package org.etlforge.integration.data.entity.sfdc;

import com.google.common.collect.ImmutableSet;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
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

public final class BulkLoadOperationType extends OperationType {
   public static final BulkLoadOperationType TYPE = new BulkLoadOperationType(101);
   private static final Version VERSION = VersionFactory.newVersion(4, 1, 0, 5);

   private BulkLoadOperationType(int var1) {
      super(
         var1,
         "BulkLoad",
         "Bulk Load",
         "Run a Salesforce Bulk Load",
         ImmutableSet.of(PipelineActivityType.SOURCE, PipelineActivityType.BULK_LOAD, PipelineActivityType.SCRIPT)
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
      return true;
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return var1 == OperationExecutionMode.RUN ? RunnableStatus.RUNNABLE : RunnableStatus.notRunnable("A bulk load operation cannot be tested.");
   }

   @Override
   public EntityFilter getSourceFilter() {
      return new FileBasedLocationFilter();
   }

   @Override
   public EntityFilter getTargetFilter() {
      throw new UnsupportedOperationException();
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new BulkLoadPipelineTemplate().getPipeline();
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      throw new UnsupportedOperationException(
         "The documentation for OperationPipelineFactory says it is needed when opening a pre-2.0 project. It is thus not applicable for Bulk Load operations."
      );
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new BulkLoadOperationType.PipelineValidatorImpl(var1, var2, var3);
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
   public EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline var1) {
      return new EntityFilterProvider<Source>() {
         @Override
         public EntityFilter getFilter(PipelineActivity<Source> var1) {
            return BulkLoadOperationType.this.getSourceFilter();
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
         Source var2 = var1.getSource();
         if (var2 == null) {
            this.fatal("No source specified");
         } else {
            this.validateSource(var2);
         }
      }

      private void validateSource(Source var1) {
         if (!BulkLoadOperationType.TYPE.getSourceFilter().apply(var1)) {
            this.fatal("The selected Source is not applicable for a Bulk Load operation.");
         }
      }
   }
}
