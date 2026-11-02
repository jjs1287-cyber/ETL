package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class TransformationPipelineValidator extends OperationPipelineValidator {
   private final TransformationPipelineBrowser browser;

   public TransformationPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      super(var1, var2, var3);
      this.browser = new TransformationPipelineBrowser(var2);
   }

   @Override
   public void validate() {
      Transformation var1 = this.validateTransformation();
      if (var1 != null) {
         this.validateSource(var1);
         this.validateTarget(var1);
      }
   }

   private Transformation validateTransformation() {
      Transformation var1 = this.browser.getTransformation();
      if (var1 == null) {
         this.fatal("No transformation specified");
      }

      return var1;
   }

   private void validateSource(Transformation var1) {
      new OperationPipelineValidator.TransformationSourceValidator(this.browser, var1).run();
   }

   private void validateTarget(Transformation var1) {
      new OperationPipelineValidator.TransformationTargetValidator(this.browser, var1).run();
   }
}
