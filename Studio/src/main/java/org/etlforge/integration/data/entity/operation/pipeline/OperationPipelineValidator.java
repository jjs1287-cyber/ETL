package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public abstract class OperationPipelineValidator {
   protected final Operation operation;
   protected final OperationPipeline pipeline;
   protected final ValidationMessageCollector collector;

   public OperationPipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      this.operation = var1;
      this.pipeline = var2;
      this.collector = var3;
   }

   public abstract void validate();

   protected final void warning(String var1) {
      this.collector.addMessage(var1, ValidationStatus.WARNING);
   }

   protected final void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }

   protected class TransformationSourceValidator {
      private final OperationPipelineBrowser browser;
      private final Transformation tf;

      public TransformationSourceValidator(OperationPipelineBrowser var2, Transformation var3) {
         this.browser = var2;
         this.tf = var3;
      }

      public void run() {
         Source var1 = this.browser.getSource();
         if (var1 != null) {
            this.validateSource(var1);
         } else {
            this.validateEmptySource();
         }
      }

      private void validateEmptySource() {
         if (!this.acceptsEmptySource()) {
            OperationPipelineValidator.this.fatal("The transformation requires a source");
         }
      }

      private boolean acceptsEmptySource() {
         DataStructure var1 = this.tf.getInputStructure();
         if (var1 == null) {
            String var3 = "The transformation \"" + this.tf.getName() + "\" does not have an input structure.";
            ErrorLog.log(this.getClass(), var3, new Exception(var3));
            return true;
         } else {
            DataStructureType var2 = var1.getStructureType();
            return !var2.isSourceDataRequired() || var2 == DataStructureType.WebServiceRequest;
         }
      }

      private void validateSource(Source var1) {
         if (!this.tf.canGetInputFrom(var1)) {
            OperationPipelineValidator.this.fatal("The source is not compatible with the selected transformation");
         }
      }
   }

   protected class TransformationTargetValidator {
      private final OperationPipelineBrowser browser;
      private final Transformation tf;

      public TransformationTargetValidator(OperationPipelineBrowser var2, Transformation var3) {
         this.browser = var2;
         this.tf = var3;
      }

      public void run() {
         Target var1 = this.browser.getTarget();
         if (var1 != null) {
            this.validateTarget(var1);
         } else {
            this.validateEmptyTarget();
         }
      }

      private void validateEmptyTarget() {
         if (this.tf.getTargetId() != null) {
            OperationPipelineValidator.this.fatal("The transformation requires a target");
         }
      }

      private void validateTarget(Target var1) {
         if (!this.tf.canSendOutputTo(var1)) {
            OperationPipelineValidator.this.fatal("The target is not compatible with the selected transformation");
         }
      }
   }
}
