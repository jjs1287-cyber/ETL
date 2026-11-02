package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.script.ScriptAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.script.ScriptInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.script.ScriptPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.script.ScriptRemovingPolicy;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class ScriptOperationType extends OperationType {
   public static final ScriptOperationType TYPE = new ScriptOperationType();

   private ScriptOperationType() {
      super(8, "Script", "Script", "Call a script", ImmutableSet.of(PipelineActivityType.SCRIPT));
   }

   @Override
   public RunnableStatus checkRunnable(OperationExecutionMode var1) {
      return RunnableStatus.RUNNABLE;
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new ScriptTemplate().getPipeline();
   }

   @Override
   public ActivityInsertionPolicy getInsertionPolicy(OperationPipeline var1) {
      return new ScriptInsertionPolicy();
   }

   @Override
   public PipelineAutoCompleter getPipelineAutoCompleter(IntegrationEntityLookup var1) {
      return new ScriptAutoCompleter(var1);
   }

   @Override
   public OperationPipelineFactory getPipelineFactory() {
      throw new UnsupportedOperationException();
   }

   @Override
   public OperationPipelineValidator getPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      return new ScriptPipelineValidator(var1, var2, var3);
   }

   @Override
   public ActivityRemovingPolicy getRemovingPolicy(OperationPipeline var1) {
      return new ScriptRemovingPolicy(var1);
   }

   @Override
   public EntityFilter getSourceFilter() {
      return EntityFilter.REJECT_ALL;
   }

   @Override
   public EntityFilter getTargetFilter() {
      return EntityFilter.REJECT_ALL;
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
      return true;
   }
}
