package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class FunctionCallPipelineValidator<F extends IntegrationEntity & Function> extends OperationPipelineValidator {
   private final PipelineActivityType activityType;
   private final FunctionCallPipelineBrowser<F, ?> browser;
   private F function;

   public FunctionCallPipelineValidator(
      PipelineActivityType var1, Operation var2, OperationPipeline var3, FunctionCallPipelineBrowser<F, ?> var4, ValidationMessageCollector var5
   ) {
      super(var2, var3, var5);
      this.activityType = var1;
      this.browser = var4;
   }

   @Override
   public final void validate() {
      this.function = this.functionMustBeThere();
      if (this.function != null) {
         this.mustHaveRequestThatIsCompatibleWithFunction();
         this.mustHaveResponseThatIsCompatibleWithFunction();
      }
   }

   private F functionMustBeThere() {
      IntegrationEntity var1 = this.browser.getFunction();
      if (var1 == null) {
         this.errorMessageWithTypeName("No %s specified");
      }

      return (F)var1;
   }

   private void errorMessageWithTypeName(String var1) {
      String var2 = String.format(var1, this.activityType.toString());
      this.fatal(var2);
   }

   private void mustHaveRequestThatIsCompatibleWithFunction() {
      Transformation var1 = this.browser.getRequest();
      if (var1 != null) {
         if (this.function.hasRequestTransformation()) {
            this.validateRequest(var1);
         } else {
            this.fatal("This function call does not use a request Transformation");
         }
      } else if (this.function.hasRequestTransformation()) {
         this.fatal("Must define a request Transformation");
      }
   }

   private void validateRequest(Transformation var1) {
      if (!this.function.acceptsOutputFrom(var1)) {
         this.errorMessageWithTypeName("The request transformation is not compatible with the selected %s");
      }

      OperationPipelineValidator.TransformationSourceValidator var2 = new OperationPipelineValidator.TransformationSourceValidator(this.browser, var1);
      var2.run();
   }

   private void mustHaveResponseThatIsCompatibleWithFunction() {
      Transformation var1 = this.browser.getResponse();
      if (var1 == null) {
         this.fatal("Must define a response Transformation");
      } else {
         this.validateResponse(var1);
      }
   }

   private void validateResponse(Transformation var1) {
      if (!var1.acceptsOutputFrom(this.function)) {
         this.errorMessageWithTypeName("The response transformation is not compatible with the selected %s");
      }

      OperationPipelineValidator.TransformationTargetValidator var2 = new OperationPipelineValidator.TransformationTargetValidator(this.browser, var1);
      var2.run();
   }
}
