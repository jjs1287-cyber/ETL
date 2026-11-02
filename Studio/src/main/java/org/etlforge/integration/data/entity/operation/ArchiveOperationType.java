package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ArchiveTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchiveAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchiveInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchivePipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchivePipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchiveRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchiveSourceFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.archive.ArchiveTargetFilterProvider;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class ArchiveOperationType extends OperationType {
   public static final ArchiveOperationType TYPE = new ArchiveOperationType(1);

   private ArchiveOperationType(int var1) {
      super(var1, "Archive", "Archive", "Archive data", ImmutableSet.of(PipelineActivityType.SOURCE, PipelineActivityType.TARGET, PipelineActivityType.SCRIPT));
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return RunnableStatus.RUNNABLE;
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new ArchiveTemplate().getPipeline();
   }

   @Override
   public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new ArchiveInsertionPolicy();
   }

   @Override
   public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new ArchiveAutoCompleter(var1);
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      return new ArchivePipelineFactory();
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new ArchivePipelineValidator(var1, var2, var3);
   }

   @Override
   public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new ArchiveRemovingPolicy();
   }

   @Override
   public EntityFilterProvider<Source> getSourceActivityFilterProvider(OperationPipeline var1) {
      return new ArchiveSourceFilterProvider();
   }

   @Override
   public EntityFilter getSourceFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Source && ((Source)var1).isArchivable();
         }
      };
   }

   @Override
   public EntityFilterProvider<Target> getTargetActivityFilterProvider(OperationPipeline var1) {
      return new ArchiveTargetFilterProvider();
   }

   @Override
   public EntityFilter getTargetFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Target && ((Target)var1).canBeTargetForArchive();
         }
      };
   }

   @Override
   public boolean hasSourceHistory() {
      return true;
   }

   @Override
   public boolean isChunkable() {
      return false;
   }

   @Override
   public boolean isEmailNotificationsSupported() {
      return true;
   }

   @Override
   public String getIndefiniteArticle() {
      return "an";
   }
}
