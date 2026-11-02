package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class OperationValidator {
   private final Operation operation;
   private final ValidationMessageCollector collector;

   public OperationValidator(Operation var1, ValidationMessageCollector var2) {
      this.operation = var1;
      this.collector = var2;
   }

   public void validatePipeline() {
      OperationPipelineValidator var1 = this.operation.getOperationType().getPipelineValidator(this.operation, this.operation.getPipeline(), this.collector);
      var1.validate();
   }

   public void validateOperationChains() {
      this.validateSuccessOperationChain();
      this.validateFailureOperationChain();
   }

   private void validateSuccessOperationChain() {
      OperationChainValidator var1 = new OperationChainValidator(
         "On Success", new OperationValidator.SuccessOperationGetter(this.collector.context().getDataStore())
      );
      var1.validate(this.operation, this.collector);
   }

   private void validateFailureOperationChain() {
      OperationChainValidator var1 = new OperationChainValidator(
         "On Failure", new OperationValidator.FailureOperationGetter(this.collector.context().getDataStore())
      );
      var1.validate(this.operation, this.collector);
   }

   public void validateSource() {
      new OperationSourceValidator(this.operation, this.collector).validate();
   }

   public void validateChunkingParameter() {
      int var1 = this.operation.getChunkSize();
      if (var1 < 0) {
         this.fatal("Invalid chunk size: must be > 0");
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }

   private static class FailureOperationGetter implements OperationChainValidator.NextOperation {
      private final IntegrationEntityLookup dataLookup;

      public FailureOperationGetter(IntegrationEntityLookup var1) {
         this.dataLookup = var1;
      }

      @Override
      public Operation get(Operation var1) {
         return this.dataLookup.getEntity(var1.getFailureOperationId(), Operation.class);
      }
   }

   private static class SuccessOperationGetter implements OperationChainValidator.NextOperation {
      private final IntegrationEntityLookup dataLookup;

      public SuccessOperationGetter(IntegrationEntityLookup var1) {
         this.dataLookup = var1;
      }

      @Override
      public Operation get(Operation var1) {
         return this.dataLookup.getEntity(var1.getSuccessOperationId(), Operation.class);
      }
   }
}
