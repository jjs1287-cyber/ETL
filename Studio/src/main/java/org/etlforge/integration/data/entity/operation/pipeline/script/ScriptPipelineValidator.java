package org.etlforge.integration.data.entity.operation.pipeline.script;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class ScriptPipelineValidator extends OperationPipelineValidator {
   public ScriptPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      super(var1, var2, var3);
   }

   @Override
   public void validate() {
      for (PipelineActivity var2 : this.pipeline.getActivities()) {
         assert var2.getType() == PipelineActivityType.SCRIPT : "I'm expecting nothing but Script activities in an OperationPipeline of type Script";
         if (var2.isEmpty()) {
            this.fatal("No script specified");
            break;
         }
      }
   }
}
