package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Iterables;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

final class SourceFromPreviousOperationValidator {
   private final Operation operation;
   private final ValidationMessageCollector collector;

   public SourceFromPreviousOperationValidator(Operation var1, ValidationMessageCollector var2) {
      this.operation = var1;
      this.collector = var2;
   }

   public void validate() {
      OperationChainModel var1 = this.getOperationChain();
      Iterable var2 = var1.getPreceedingSuccessOperations();
      if (Iterables.isEmpty(var2)) {
         this.fatal(SystemSource.SOURCE_FROM_PREVIOUS_OPERATION.getName() + " can only be used in a success operation.");
      } else {
         this.validateFunctionJoins(var2);
      }
   }

   private OperationChainModel getOperationChain() {
      ValidationContext var1 = this.collector.context();
      return new OperationChainModel(this.operation, var1.getDataStore(), var1.getDependencies());
   }

   private void validateFunctionJoins(Iterable<Operation> var1) {
      DataStructure var2 = this.getInputStructure();
      if (var2 != null) {
         for (Operation var4 : var1) {
            this.validateFunctionJoin(var2, var4);
         }
      }
   }

   private DataStructure getInputStructure() {
      OperationFunctionInfo var1 = new OperationFunctionInfo(this.operation);
      Function var2 = var1.getFirstFunction();
      return var2 instanceof Transformation ? ((Transformation)var2).getInputStructure() : null;
   }

   private void validateFunctionJoin(DataStructure var1, Operation var2) {
      assert var1 != null : "This method should not be called with a null input structure";
      OperationFunctionInfo var3 = new OperationFunctionInfo(var2);
      Function var4 = var3.getLastNonPassThroughFunction();
      if (var4 != null) {
         DataStructure var5 = var4.getOutputStructure();
         if (var5 != null && var1.isCompatibleWith(var5)) {
            return;
         }
      }

      this.fatal("The source structure of this operation is not compatible with the target structure of the previous operation \"" + var2.getName() + "\".");
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
